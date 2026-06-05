$BACKEND_DIR = "./backend"
$FRONTEND_DIR = "./frontend"
$ENV_PATH = "$BACKEND_DIR/.env"

Write-Host "==================================================" -ForegroundColor Cyan
Write-Host "Launching Full Stack App Local Dev Environment" -ForegroundColor Cyan
Write-Host "==================================================" -ForegroundColor Cyan

$envVars = @{}

# 0. Load Environment Variables from .env file
if (Test-Path $ENV_PATH) {
    Write-Host "[0/3] Loading environment variables from $ENV_PATH..." -ForegroundColor Magenta
    Get-Content $ENV_PATH | ForEach-Object {
        # Skip comments and empty lines
        if ($_ -and $_ -notmatch '^#') {
            # Split by the first '=' character
            $name, $value = $_ -split '=', 2
            if ($name -and $value) {
                $trimmedName = $name.Trim()
                $trimmedValue = $value.Trim()
                
                # Global Process fallback
                [Environment]::SetEnvironmentVariable($trimmedName, $trimmedValue, "Process")
                
                # Save to our dictionary for explicit injection string
                $envVars[$trimmedName] = $trimmedValue
            }
        }
    }
} else {
    Write-Warning "[!] .env file not found at $ENV_PATH! Proceeding with system defaults..."
}

# Build env var string for the backend command
$envSetCommands = ($envVars.GetEnumerator() | ForEach-Object {
    "`$env:$($_.Key)='$($_.Value)'"
}) -join "; "

# 1. Boot up only the Database inside Docker (Detached mode)

Write-Host ""
Write-Host "[1/3] Spinning up PostgreSQL Container..." -ForegroundColor Yellow
docker compose up -d db

# Quick health check pause to let Postgres initialize its ports
Start-Sleep -Seconds 2

# 2. Open a new window and start the Spring Boot Backend natively
Write-Host ""
Write-Host "[2/3] Booting Kotlin/Spring Boot Backend natively..." -ForegroundColor Yellow

$backendCmd = "$envSetCommands; Set-Location '$BACKEND_DIR'; Write-Host 'Starting Spring Boot Engine...' -ForegroundColor Green; ./gradlew bootRun"
Start-Process powershell -ArgumentList "-NoExit", "-Command", $backendCmd

# 3. Open a new window and start the React Frontend dev server
Write-Host ""
Write-Host "[3/3] Booting React Frontend dev server..." -ForegroundColor Yellow
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$FRONTEND_DIR'; Write-Host 'Starting Frontend UI...' -ForegroundColor Green; npm run dev"

Write-Host ""
Write-Host "==================================================" -ForegroundColor Green
Write-Host "All systems initiated! Check the spawning windows." -ForegroundColor Green
Write-Host "==================================================" -ForegroundColor Green