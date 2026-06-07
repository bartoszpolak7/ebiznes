$KOTLIN = "../zad3"
$PYTHON = "./"

Write-Host "==================================================" -ForegroundColor Cyan
Write-Host "EBIZNES - ZADANIE 9" -ForegroundColor Cyan
Write-Host "==================================================" -ForegroundColor Cyan

$ENV_PATH = "$KOTLIN/.env"
$envVars = @{}

# 0. Load Environment Variables from .env file
if (Test-Path $ENV_PATH) {
    Write-Host "[0/3] Loading environment variables from $ENV_PATH..." -ForegroundColor Magenta
    Get-Content $ENV_PATH | ForEach-Object {
        if ($_ -and $_ -notmatch '^#') {
            $name, $value = $_ -split '=', 2
            if ($name -and $value) {
                $trimmedName = $name.Trim()
                $trimmedValue = $value.Trim()
                [Environment]::SetEnvironmentVariable($trimmedName, $trimmedValue, "Process")
                $envVars[$trimmedName] = $trimmedValue
            }
        }
    }
} else {
    Write-Warning "[!] .env file not found at $ENV_PATH! Proceeding with system defaults..."
}

$envSetCommands = ($envVars.GetEnumerator() | ForEach-Object {
    "`$env:$($_.Key)='$($_.Value)'"
}) -join "; "

# 1. Start Ollama
Write-Host ""
Write-Host "[1/3] Starting Ollama..." -ForegroundColor Yellow
Start-Process powershell -ArgumentList "-NoExit", "-Command", "Write-Host 'Starting Ollama...' -ForegroundColor Green; ollama serve"
Start-Sleep -Seconds 3

# 2. Start Python AI service
Write-Host ""
Write-Host "[2/3] Starting Python AI service..." -ForegroundColor Yellow
Start-Process powershell -ArgumentList "-NoExit", "-Command", "Set-Location '$PYTHON'; Write-Host 'Starting FastAPI...' -ForegroundColor Green; uvicorn main:app --port 8000"
Start-Sleep -Seconds 2

# 3. Start Ktor backend
Write-Host ""
Write-Host "[3/3] Starting Ktor backend..." -ForegroundColor Yellow
$kotlinCmd = "$envSetCommands; Set-Location '$KOTLIN'; Write-Host 'Starting Ktor...' -ForegroundColor Green; ./gradlew run"
Start-Process powershell -ArgumentList "-NoExit", "-Command", $kotlinCmd

Write-Host ""
Write-Host "==================================================" -ForegroundColor Green
Write-Host "All systems initiated! Check the spawning windows." -ForegroundColor Green
Write-Host "  Ollama:  http://localhost:11434"                  -ForegroundColor Green
Write-Host "  Python:  http://localhost:8000"                   -ForegroundColor Green
Write-Host "  Ktor:    http://localhost:8080"                   -ForegroundColor Green
Write-Host "==================================================" -ForegroundColor Green