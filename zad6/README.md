# Zad6 – testy (React + Kotlin + Selenium + BrowserStack)

Aplikacja sklepowa z testami jednostkowymi, API, E2E (Selenium Kotlin) oraz uruchamianiem na BrowserStack.

## Struktura

```text
zad6/
  backend/          # Spring Boot Kotlin + testy API/jednostkowe
  frontend/         # React + Vitest
  e2e/              # Selenium Kotlin + BrowserStack Local (20 testów E2E)
  compose.yaml      # Docker: backend :8080, frontend :5173
```

## Wymagania punktowe

| Punkt | Opis | Lokalizacja |
|------|------|-------------|
| 3.0 | 20 testów funkcjonalnych Selenium | [`e2e/src/test/.../ShopE2ETest.kt`](e2e/src/test/kotlin/io/github/siemamen7/zad6/ShopE2ETest.kt) |
| 3.5 | min. 50 asercji E2E | `ShopE2ETest.kt` |
| 4.0 | min. 50 asercji unit (backend + frontend) | [`backend/src/test`](backend/src/test), [`frontend/src`](frontend/src) |
| 4.5 | testy API + 1 negatywny scenariusz / endpoint | `ProductsApiTest.kt`, `PaymentsApiTest.kt` |
| 5.0 | BrowserStack | [`e2e/`](e2e/) – RemoteWebDriver + BrowserStack Local |

## Uruchomienie aplikacji

```powershell
cd zad6
docker compose up --build
```

Alternatywnie lokalnie:

```powershell
# backend
cd backend
.\gradlew bootRun

# frontend (drugie okno)
cd frontend
npm install
npm run dev
```

## Testy jednostkowe i API

### Backend

```powershell
cd zad6\backend
.\gradlew test
```

### Frontend

```powershell
cd zad6\frontend
npm install
npm test
```

## Testy E2E na BrowserStack

1. Załóż konto BrowserStack (np. [GitHub Student Developer Pack](https://education.github.com/pack)).
2. Uruchom aplikację (`docker compose up` w `zad6`).
3. Ustaw zmienne środowiskowe:

```powershell
$env:BROWSERSTACK_USERNAME="twoj_username"
$env:BROWSERSTACK_ACCESS_KEY="twoj_access_key"
$env:BROWSERSTACK_BUILD_NAME="zad6-e2e-local"
```

Opcjonalnie:

- `ZAD6_FRONTEND_URL` (domyślnie `http://localhost:5173`)
- `ZAD6_BACKEND_HEALTH_URL` (domyślnie `http://localhost:8080/products`)
- `BROWSERSTACK_LOCAL_IDENTIFIER`
- `BROWSERSTACK_LOCAL_FORCELOCAL` (domyślnie `true`)

4. Uruchom testy:

```powershell
cd zad6\e2e
.\gradlew test
```

Testy automatycznie startują BrowserStack Local (tunel do localhost), łączą się z `hub-cloud.browserstack.com` i wykonują 20 scenariuszy UI.

Bez `BROWSERSTACK_USERNAME` / `BROWSERSTACK_ACCESS_KEY` testy E2E są pomijane.

## Endpointy API

- `GET /products` – lista produktów
- `POST /payments` – płatność (`total` > 0); błędny payload → 400

## GitHub Actions (opcjonalnie)

```yaml
name: zad6-browserstack
on: workflow_dispatch
jobs:
  e2e:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: '24'
      - name: Start app
        run: docker compose -f zad6/compose.yaml up -d --build
      - name: Run BrowserStack E2E
        working-directory: zad6/e2e
        env:
          BROWSERSTACK_USERNAME: ${{ secrets.BROWSERSTACK_USERNAME }}
          BROWSERSTACK_ACCESS_KEY: ${{ secrets.BROWSERSTACK_ACCESS_KEY }}
        run: ./gradlew test
```
