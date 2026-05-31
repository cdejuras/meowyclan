# Startup script for Ovejera Fullstack App (Windows PowerShell)
# This starts both backend (Java/Spring) and frontend (Node/React)

Write-Host ""
Write-Host "========================================"
Write-Host "Starting Ovejera Fullstack Application"
Write-Host "========================================" -ForegroundColor Green
Write-Host ""

# Check if node/npm is available
try {
    npm --version | Out-Null
} catch {
    Write-Host "ERROR: npm not found. Please install Node.js from https://nodejs.org/" -ForegroundColor Red
    exit 1
}

# Check if java is available
try {
    java -version 2>&1 | Out-Null
} catch {
    Write-Host "ERROR: Java not found. Please install JDK 21." -ForegroundColor Red
    exit 1
}

Write-Host "[1/4] Installing frontend dependencies..." -ForegroundColor Yellow
Push-Location "ovejera\Frontend\Fullstack-App"
if (-not (Test-Path "node_modules")) {
    npm install
} else {
    Write-Host "Frontend dependencies already installed." -ForegroundColor Green
}
Pop-Location

Write-Host ""
Write-Host "[2/4] Starting backend (Spring Boot with H2 database)..." -ForegroundColor Yellow
Write-Host "        Backend will be available at http://localhost:8080" -ForegroundColor Cyan
$backendProc = Start-Process -FilePath "powershell" -ArgumentList "-NoExit", "-Command", "cd 'ovejera\Backend\fullstackApp'; `$env:SPRING_PROFILES_ACTIVE='dev'; .\mvnw.cmd -DskipTests spring-boot:run" -PassThru

Write-Host ""
Write-Host "[3/4] Waiting 12 seconds for backend to start..." -ForegroundColor Yellow
Start-Sleep -Seconds 12

Write-Host ""
Write-Host "[4/4] Starting frontend (React + Vite)..." -ForegroundColor Yellow
Write-Host "        Frontend will be available at http://localhost:5173" -ForegroundColor Cyan
Start-Process -FilePath "powershell" -ArgumentList "-NoExit", "-Command", "cd 'ovejera\Frontend\Fullstack-App'; npm run dev"

Write-Host ""
Write-Host "========================================" -ForegroundColor Green
Write-Host "Ovejera Fullstack App is starting!"
Write-Host ""
Write-Host "Frontend: " -NoNewLine -ForegroundColor Green; Write-Host "http://localhost:5173"
Write-Host "Backend:  " -NoNewLine -ForegroundColor Green; Write-Host "http://localhost:8080"
Write-Host "API Docs: " -NoNewLine -ForegroundColor Green; Write-Host "http://localhost:8080/swagger-ui.html"
Write-Host "H2 Console: " -NoNewLine -ForegroundColor Green; Write-Host "http://localhost:8080/h2-console"
Write-Host ""
