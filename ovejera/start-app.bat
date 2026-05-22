@echo off
REM Startup script for Ovejera Fullstack App (Windows)
REM This starts both backend (Java/Spring) and frontend (Node/React)

setlocal enabledelayedexpansion

REM Colors for output
echo.
echo ========================================
echo Starting Ovejera Fullstack Application
echo ========================================
echo.

REM Check if node/npm is available
where npm >nul 2>nul
if %errorlevel% neq 0 (
    echo ERROR: npm not found. Please install Node.js from https://nodejs.org/
    pause
    exit /b 1
)

REM Check if java is available
where java >nul 2>nul
if %errorlevel% neq 0 (
    echo ERROR: Java not found. Please install JDK 21.
    pause
    exit /b 1
)

echo [1/4] Installing frontend dependencies...
cd ovejera\Frontend\Fullstack-App
if not exist node_modules (
    call npm install
) else (
    echo Frontend dependencies already installed.
)
cd ..\..

echo.
echo [2/4] Starting backend (Spring Boot with H2 database)...
echo        Backend will be available at http://localhost:8080
start cmd /k "cd ovejera\Backend\fullstackApp && set SPRING_PROFILES_ACTIVE=dev && mvnw.cmd -DskipTests spring-boot:run"

echo.
echo [3/4] Waiting 10 seconds for backend to start...
timeout /t 10 /nobreak

echo.
echo [4/4] Starting frontend (React + Vite)...
echo        Frontend will be available at http://localhost:5173
start cmd /k "cd ovejera\Frontend\Fullstack-App && npm run dev"

echo.
echo ========================================
echo Ovejera Fullstack App is starting!
echo.
echo Frontend: http://localhost:5173
echo Backend:  http://localhost:8080
echo API Docs: http://localhost:8080/swagger-ui.html
echo H2 Console: http://localhost:8080/h2-console
echo.
echo Press any key to close this window...
pause
