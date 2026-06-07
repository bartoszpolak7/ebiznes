import os
from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
import ollama
from pydantic import BaseModel
import random

client = ollama.Client(host=os.getenv("OLLAMA_HOST", "http://localhost:11434"))

MODEL = "llama3.2:3b"

OPENINGS = ['Witaj! Dziś zniżka 10\\% dla wszystkich akolitów!',
            'Czołem panie bracie! Co powiesz na tą ostrą jak bułat zupę?',
            'Witaj podróżniku! Czy grasują wilcy na szlaku?',
            'Całuję rączki... i przysięgam, że te szczeżuje są świeże!',
            'Niech mnie harpie rozdziobią! Witaj z powrotem przyjacielu!']
CLOSINGS = ["Jak skąpisz grosza to idź w diabły!",
            "Wesołego Przesilenia!",
            "Potrzebujesz czegoś jeszcze? Może świeżego konia?",
            "Do zobaczenia na szlaku!",
            "Niech R'hllor będzie z tobą, albowiem noc jest ciemna i pełna okropności"]

def build_system_prompt(products: list[str], categories: list[str]) -> str:
    product_context = "\n".join(f"- {p}" for p in products) or "Brak produktów"
    category_context = "\n".join(f"- {c}" for c in categories) or "Brak kategorii" 

    CLOSINGS_STR = "\n".join(f'- {c}' for c in CLOSINGS)


    return f"""Jesteś średniowiecznym karczmarzem w świecie fantasy.
        Możesz odpowiadać na pytania związane:
        - Dostępnymi napojami oraz daniami
        - Porady związane z wyborem zamówienia
        - Informacje o zamówieniach, na przykład cena lub alergeny

        Dostępne kategorie:
        {category_context}

        Dostępne produkty:
        {product_context}

        Oprócz tego możesz prowadzić kowersacje związane z fantasy, role play.
        Możesz opowiadać o plotkach z okolicy, możesz sugerować mu questy związane z pomocą wieśniakom i polowaniu na potwory.
        Zwracaj się do użytkownika "Podróżnik", jeśli nie poda swojego imienia.
        Na pytania niezwiązane ze sklepem lub tematyką fantasy odpowiadaj zmieszaniem i niezrozumieniem."""

app = FastAPI()

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_methods=["*"],
    allow_headers=["*"],
)

conversation_history: list[dict] = []

# jeśli new_conversation = false to podajemy kontekst poprzednich wiadomości
class ChatRequest(BaseModel):
    message: str
    new_conversation: bool = False
    products: list[str] = []
    categories: list[str] = []
 
class ChatResponse(BaseModel):
    response: str
    is_opening: bool


@app.post("/chat", response_model=ChatResponse)
async def chat(request: ChatRequest):
    global conversation_history
 
    is_opening = False
 
    if request.new_conversation or not conversation_history:
        conversation_history = []
        opening = random.choice(OPENINGS)
        conversation_history.append({
            "role": "assistant",
            "content": opening
        })
        is_opening = True
 
    conversation_history.append({
        "role": "user",
        "content": request.message
    })

    system = build_system_prompt(request.products, request.categories)
 
    try:
        completion = client.chat(
        model=MODEL,        
        messages=[
            {"role": "system", "content": system},
            *conversation_history
    ]
)
 
        ai_response = completion.message.content
 
        full_response = ai_response
 
        conversation_history.append({
            "role": "assistant",
            "content": full_response
        })
 
        return ChatResponse(
            response=full_response,
            is_opening=is_opening
        )
 
 
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
    
@app.post("/chat/reset")
async def reset_conversation():
    global conversation_history
    conversation_history = []
    return {"message": "Conversation reset"}