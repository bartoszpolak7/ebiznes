# Ebiznes 2026
Repozytorium z zadaniami.
Wszystkie nagrania znajdują się w folderze 'nagrania'.

https://github.com/bartoszpolak7/ebiznes/tree/main/nagrania

## Zadanie 1
✅ 3.0 obraz ubuntu z Pythonem w wersji 3.10

kod: https://github.com/siemamen7/ebiznes/tree/main/zad1-3-0

obraz: https://hub.docker.com/repository/docker/szczuras7/zad1-3-0/general

✅ 3.5 obraz ubuntu:24.02 z Javą w wersji 8 oraz Kotlinem

kod: https://github.com/siemamen7/ebiznes/tree/main/zad1-3-5

obraz: https://hub.docker.com/repository/docker/szczuras7/zad1-3-5/general

✅ 4.0 do powyższego należy dodać najnowszego Gradle’a oraz paczkę JDBC
SQLite w ramach projektu na Gradle (build.gradle)

kod: https://github.com/siemamen7/ebiznes/tree/main/zad1-4-0

obraz: https://hub.docker.com/repository/docker/szczuras7/zad1-4-0/general

✅ 4.5 stworzyć przykład typu HelloWorld oraz uruchomienie aplikacji
przez CMD oraz gradle

kod: https://github.com/siemamen7/ebiznes/tree/main/zad1-4-5

obraz: https://hub.docker.com/repository/docker/szczuras7/zad1-4-5/general

✅ 5.0 dodać konfigurację docker-compose

kod: https://github.com/siemamen7/ebiznes/tree/main/zad1-5-0

## Zadanie 2

✅ 3.0 Należy stworzyć kontroler do Produktów
+ ✅ 3.5 Do kontrolera należy stworzyć endpointy zgodnie z CRUD - dane
pobierane z listy

commit: https://github.com/siemamen7/ebiz-zad2/commit/14e41d3b99d7b2f2c43c6f4741e7086adfffad12

✅ 4.0 Należy stworzyć kontrolery do Kategorii oraz Koszyka + endpointy
zgodnie z CRUD

commit: https://github.com/siemamen7/ebiz-zad2/commit/8c681f549293bb42e87b4de127e7230bf3670190

✅ 4.5 Należy aplikację uruchomić na dockerze (stworzyć obraz) oraz dodać
skrypt uruchamiający aplikację via ngrok

commit: https://github.com/siemamen7/ebiz-zad2/commit/ce0b42c858b741f004c7fda3f045a31fc631a1ec

obraz: https://hub.docker.com/repository/docker/szczuras7/ebiz-zad2/general

✅ 5.0 Należy dodać konfigurację CORS dla dwóch hostów dla metod CRUD

commit: https://github.com/siemamen7/ebiz-zad2/commit/c96ead9c9e46b0213642ad5f7ac9eb81f580768f

## Zadanie 3

✅ 3.0 Należy stworzyć aplikację kliencką w Kotlinie we frameworku Ktor, która pozwala na przesyłanie wiadomości na platformę Discord

✅ 3.5 Aplikacja jest w stanie odbierać wiadomości użytkowników z platformy Discord skierowane do aplikacji (bota)

✅ 4.0 Zwróci listę kategorii na określone żądanie użytkownika

✅ 4.5 Zwróci listę produktów wg żądanej kategorii

❌ 5.0 Aplikacja obsłuży dodatkowo jedną z platform: Slack lub Messenger

kod: https://github.com/siemamen7/ebiznes/tree/main/zad3

obraz: https://hub.docker.com/repository/docker/szczuras7/ebiz3/general


## Zadanie 4
✅ 3.0 Należy stworzyć aplikację we frameworki echo w j. Go, która będzie
miała kontroler Produktów zgodny z CRUD

✅ 3.5 Należy stworzyć model Produktów wykorzystując gorm oraz
wykorzystać model do obsługi produktów (CRUD) w kontrolerze (zamiast
listy)

✅ 4.0 Należy dodać model Koszyka oraz dodać odpowiedni endpoint

✅ 4.5 Należy stworzyć model kategorii i dodać relację między kategorią,
a produktem

✅ 5.0 pogrupować zapytania w gorm’owe scope'y

kod: https://github.com/siemamen7/ebiznes/tree/main/zad4

## Zadanie 5
✅3.0 W ramach projektu należy stworzyć dwa komponenty: Produkty oraz
Płatności; Płatności powinny wysyłać do aplikacji serwerowej dane, a w
Produktach powinniśmy pobierać dane o produktach z aplikacji
serwerowej;

✅3.5 Należy dodać Koszyk wraz z widokiem; należy wykorzystać routing

✅4.0 Dane pomiędzy wszystkimi komponentami powinny być przesyłane za
pomocą React hooks

✅4.5 Należy dodać skrypt uruchamiający aplikację serwerową oraz
kliencką na dockerze via docker-compose

✅5.0 Należy wykorzystać axios’a oraz dodać nagłówki pod CORS

kod: https://github.com/bartoszpolak7/ebiznes/tree/main/zad5

## Zadanie 6
✅3.0 Należy stworzyć 20 przypadków testowych w CypressJS lub Selenium
(Kotlin, Python, Java, JS, Go, Scala)

✅3.5 Należy rozszerzyć testy funkcjonalne, aby zawierały minimum 50
asercji

✅4.0 Należy stworzyć testy jednostkowe do wybranego wcześniejszego
projektu z minimum 50 asercjami

✅4.5 Należy dodać testy API, należy pokryć wszystkie endpointy z
minimum jednym scenariuszem negatywnym per endpoint

✅5.0 Należy uruchomić testy funkcjonalne na Browserstacku

kod: https://github.com/bartoszpolak7/ebiznes/tree/main/zad6

## Zadanie 7

✅ 3.0 Należy dodać litera do odpowiedniego kodu aplikacji serwerowej w
hookach gita

✅ 3.5 Należy wyeliminować wszystkie bugi w kodzie w Sonarze (kod
aplikacji serwerowej)

✅ 4.0 Należy wyeliminować wszystkie zapaszki w kodzie w Sonarze (kod
aplikacji serwerowej)

✅ 4.5 Należy wyeliminować wszystkie podatności oraz błędy bezpieczeństwa
w kodzie w Sonarze (kod aplikacji serwerowej)

✅ 5.0 Należy wyeliminować wszystkie błędy oraz zapaszki w kodzie
aplikacji klienckiej

kod serwera: https://github.com/bartoszpolak7/ebiz-zad7-server/tree/8b2639d8edefe617f781e6c618b6b8cacafc9f5e

kod klienta: https://github.com/bartoszpolak7/ebiz-zad7-client/tree/c9c23adc2120f3861da90f04d267c87019e9eafe

## Zadanie 8
✅ 3.0 logowanie przez aplikację serwerową (bez Oauth2)

✅ 3.5 rejestracja przez aplikację serwerową (bez Oauth2)

✅ 4.0 logowanie via Google OAuth2

✅ 4.5 logowanie via Facebook lub Github OAuth2

✅ 5.0 zapisywanie danych logowania OAuth2 po stronie serwera

kod serwera: https://github.com/bartoszpolak7/ebiznes/tree/main/zad6/backend

kod klienta: https://github.com/bartoszpolak7/ebiznes/tree/main/zad6/frontend

## Zadanie 9
✅ 3.0 należy stworzyć po stronie serwerowej osobny serwis do łącznia z
chatGPT

✅ 3.5 należy połączyć serwis z interfejsem frontendowym via serwis w
Kotlinie (zadanie 3) - discord + JS

❌ 4.0 stworzyć listę 5 różnych otwarć oraz zamknięć rozmowy

❌ 4.5 filtrowanie po zagadnieniach związanych ze sklepem (np.
ograniczenie się jedynie do ubrań oraz samego sklepu) do GPT

❌ 5.0 filtrowanie odpowiedzi po sentymencie

kod serwera (i frontendu) w kotlinie: https://github.com/bartoszpolak7/ebiznes/tree/main/zad3

kod części w Pythonie: https://github.com/bartoszpolak7/ebiznes/tree/main/zad9

## Zadanie 10
✅ 3.0 Należy stworzyć odpowiednie instancje po stronie chmury na
dockerze

❌ 3.5 Stworzyć odpowiedni pipeline w Github Actions do budowania
aplikacji (np. via fatjar)

❌ 4.0 Dodać notyfikację mailową o wynikach z sonara

❌ 4.5 Dodać krok z deploymentem aplikacji klienckiej na chmurę (obie ze
sobą rozmawiają)

❌ 5.0 Dodać uruchomienie regresyjnych testów automatycznych
(funkcjonalnych) jako krok w Actions w Browserstacku

TYLKO WIDEO
