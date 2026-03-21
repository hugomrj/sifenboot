@echo off
echo Testing SIFEN API Java Security Features...

REM Test health endpoint (should work without auth)
echo Testing health endpoint...
curl -s http://localhost:3001/api/v1/health

echo.
echo Testing user creation...
curl -X POST http://localhost:3001/api/v1/users/create-test-user -H "Content-Type: application/json" -d "{\"companyId\":\"test-company-id\",\"email\":\"test@example.com\",\"fullName\":\"Test User\",\"password\":\"password123\",\"role\":\"admin\"}"

echo.
echo Testing login...
curl -X POST http://localhost:3001/api/v1/auth/login -H "Content-Type: application/json" -d "{\"email\":\"test@example.com\",\"password\":\"password123\"}"

echo.
echo Testing rate limiting (should fail after many requests)...
for /L %%i in (1,1,10) do (
    echo Request %%i
    curl -s http://localhost:3001/api/v1/health
    timeout /t 1 /nobreak > nul
)

echo.
echo Test completed!
pause



