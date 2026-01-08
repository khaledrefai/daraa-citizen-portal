# citizenServices mono-repo split

The project is now separated so the backend can live on your VPS while frontend developers work locally.

## Layout
- `backend/` – Spring Boot + JPA API server (no bundled UI).
- `frontend/` – React + Webpack SPA, talks to the API via `SERVER_API_URL`.
- Root – shared config files (.editorconfig, git attrs/ignore, etc.).

## Backend (VPS or local)
```
cd backend
./mvnw -Pprod clean verify   # build the runnable jar
java -jar target/*.jar       # run with prod profile
```
- Default CORS allows local dev hosts `http://localhost:9000` and `http://localhost:9060`. To extend for other origins set `CORS_ALLOWED_ORIGINS` (comma separated) or `CORS_ALLOWED_ORIGIN_PATTERNS` before starting the app.

## Frontend (local developers)
```
cd frontend
npm install
# point the UI at the deployed backend (PowerShell)
$env:SERVER_API_URL="https://your-backend-host/"
npm start                     # webpack dev server + browsersync

# POSIX shells: SERVER_API_URL=https://your-backend-host/ npm start
```
- Other scripts: `npm run build` (production bundle), `npm test`, `npm run lint`, `npm run lint:fix`, `npm run prettier:check`, `npm run prettier:format`.
- The Webpack config reads `SERVER_API_URL` (defaults to `http://localhost:8080/` if unset).

## Notes
- Backend and frontend now build/run independently; there is no npm build step in the Maven flow.
- Static UI bundles are emitted under `frontend/target/` if you need to host them separately (e.g., CDN or Nginx).
