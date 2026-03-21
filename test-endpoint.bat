@echo off
echo ========================================
echo Probando endpoint /v1/fe/factura-contado
echo ========================================

echo.
echo 1. Verificando que la aplicacion este ejecutandose...
netstat -an | findstr :3001
if %errorlevel% neq 0 (
    echo ERROR: La aplicacion no esta ejecutandose en el puerto 3001
    echo Por favor ejecuta: mvn spring-boot:run
    pause
    exit /b 1
)

echo.
echo 2. Probando endpoint con curl...
curl -X POST ^
  -H "Content-Type: application/json" ^
  -H "X-Company-ID: test-company-001" ^
  -d @test-factura-contado.json ^
  http://localhost:3001/v1/fe/factura-contado

echo.
echo ========================================
echo Prueba completada
echo ========================================
pause

