# Setup Guide

## Prerequisites

Before running the application, ensure you have:

1. **Java Development Kit (JDK) 21 or higher**
   - Download from: https://adoptium.net/ or https://www.oracle.com/java/technologies/downloads/
   - Verify: `java -version`

2. **Node.js 18+ and npm**
   - Download from: https://nodejs.org/
   - Verify: `node --version` and `npm --version`

## Installation & Setup

### Step 1: Clone/Extract the Repository
```bash
cd c:\Users\Ziggy-TUF\Downloads\ovejera
```

### Step 2: Run the Startup Script (Windows)

**Recommended: Use the automated startup script**

From the root directory (`c:\Users\Ziggy-TUF\Downloads\ovejera`):

```bash
start-app.bat
```

This will automatically:
- Install npm dependencies for the frontend
- Start the backend Spring Boot application
- Start the Vite development server for the frontend
- Open two terminal windows for each service

**Alternative: PowerShell**
```powershell
.\start-app.ps1
```

### Step 3: Manual Setup (if preferred)

If you want to run services manually, open two separate terminal windows:

**Terminal 1 - Backend:**
```bash
cd ovejera\Backend\fullstackApp
set SPRING_PROFILES_ACTIVE=dev
mvnw.cmd -DskipTests spring-boot:run
```

**Terminal 2 - Frontend:**
```bash
cd ovejera\Frontend\Fullstack-App
npm install
npm run dev
```

## Verification

Once both services are running, verify the application is working:

1. **Frontend**: Open http://localhost:5173 in your browser
2. **Backend API**: http://localhost:8080/api/product (should show `[]` or a list of products)
3. **Swagger API Docs**: http://localhost:8080/swagger-ui.html
4. **H2 Database Console**: http://localhost:8080/h2-console (login with username: `sa`, no password)

## Database

The development environment uses **H2 in-memory database**:
- Automatically created when backend starts
- Data is lost when backend stops (reset on each restart)
- For persistent data, switch to Postgres (see Backend README)

## Troubleshooting

### "npm not found"
- Node.js is not installed or not in PATH
- Install from https://nodejs.org/

### "java: command not found" or "mvnw not found"
- Java 21 is not installed or not in PATH
- Install JDK from https://adoptium.net/

### Frontend can't connect to backend
- Ensure backend is running on http://localhost:8080
- Check that port 8080 is not blocked by a firewall
- CORS is configured for localhost:5173

### Port already in use
- Backend (8080): Kill process on port 8080, or change `server.port` in `application-dev.properties`
- Frontend (5173): Vite will automatically use 5174, 5175, etc. if 5173 is taken

## Next Steps

- See [CONTRIBUTING.md](../CONTRIBUTING.md) for development guidelines
- Read [CHANGELOG.md](../CHANGELOG.md) for recent changes
- Check [Backend README](../ovejera/Backend/fullstackApp/README.md) for backend-specific info
- Check [Frontend README](../ovejera/Frontend/Fullstack-App/README.md) for frontend-specific info
