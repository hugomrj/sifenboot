Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Probando endpoint /v1/fe/factura-contado" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan

Write-Host ""
Write-Host "1. Verificando que la aplicacion este ejecutandose..." -ForegroundColor Yellow
$portCheck = netstat -an | findstr :3001
if ($LASTEXITCODE -ne 0) {
    Write-Host "ERROR: La aplicacion no esta ejecutandose en el puerto 3001" -ForegroundColor Red
    Write-Host "Por favor ejecuta: mvn spring-boot:run" -ForegroundColor Yellow
    Read-Host "Presiona Enter para continuar"
    exit 1
}

Write-Host "✓ Aplicacion ejecutandose en puerto 3001" -ForegroundColor Green

Write-Host ""
Write-Host "2. Probando endpoint con Invoke-RestMethod..." -ForegroundColor Yellow

try {
    $headers = @{
        "Content-Type" = "application/json"
        "X-Company-ID" = "test-company-001"
    }
    
    $body = Get-Content -Path "test-factura-contado.json" -Raw
    
    $response = Invoke-RestMethod -Uri "http://localhost:3001/v1/fe/factura-contado" -Method POST -Headers $headers -Body $body
    
    Write-Host "✓ Respuesta exitosa:" -ForegroundColor Green
    $response | ConvertTo-Json -Depth 10
} catch {
    Write-Host "✗ Error en la peticion:" -ForegroundColor Red
    Write-Host $_.Exception.Message -ForegroundColor Red
    if ($_.Exception.Response) {
        $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
        $responseBody = $reader.ReadToEnd()
        Write-Host "Response Body: $responseBody" -ForegroundColor Red
    }
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Prueba completada" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Read-Host "Presiona Enter para continuar"

