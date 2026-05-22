# Ovejera Fullstack Application

A Java Spring Boot backend with React + TypeScript frontend application.

## Quick Start (Recommended)

### Windows (Batch or PowerShell)

**Option 1: Batch file**
```bash
start-app.bat
```

**Option 2: PowerShell**
```powershell
.\start-app.ps1
```

Both scripts will:
- Install frontend dependencies (npm)
- Start the backend on `http://localhost:8080` (with H2 in-memory database)
- Start the frontend on `http://localhost:5173`

### macOS / Linux

```bash
# Terminal 1: Start backend
cd ovejera/Backend/fullstackApp
export SPRING_PROFILES_ACTIVE=dev
./mvnw -DskipTests spring-boot:run

# Terminal 2: Start frontend
cd ovejera/Frontend/Fullstack-App
npm install  # if needed
npm run dev
```

## Accessing the Application

Once running, open:
- **Frontend**: http://localhost:5173 (React app)
- **Backend API**: http://localhost:8080/api/product
- **API Documentation**: http://localhost:8080/swagger-ui.html
- **H2 Database Console**: http://localhost:8080/h2-console

## Development Details

- **Backend**: Spring Boot 4.0.6 with JDK 21, uses H2 in-memory database for local dev
- **Frontend**: React 19 + TypeScript with Vite
- **Database**: Postgres in production (see Docker Compose for local Postgres setup)

See individual module READMEs for more details:
- [Backend README](ovejera/Backend/fullstackApp/README.md)
- [Frontend README](ovejera/Frontend/Fullstack-App/README.md)

## Running Tests

**Backend tests:**
```bash
cd ovejera/Backend/fullstackApp
./mvnw test
```

**Frontend tests:**
```bash
cd ovejera/Frontend/Fullstack-App
npm test
```

## Project Rules

See [CONTRIBUTING.md](CONTRIBUTING.md) for project conventions and rules:
- Unit tests required for new code
- Update CHANGELOG.md for changes
- Open feature request issue before starting work
- See [feature-summaries](docs/feature-summaries/) for recent features

## CI/CD

GitHub Actions runs automated tests on push to `main` or `master` branches. See `.github/workflows/ci.yml`.
