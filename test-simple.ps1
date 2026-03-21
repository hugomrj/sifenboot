$apiUrl = "http://localhost:3001/v1/fe/factura-contado"
$companyId = "test-company-001"
$jsonFilePath = ".\test-factura-contado.json"

Write-Host "Testing POST $apiUrl with $jsonFilePath"
Write-Host ""

try {
    $jsonBody = Get-Content $jsonFilePath -Raw
    
    $headers = @{
        "Content-Type" = "application/json"
        "X-Company-ID" = $companyId
    }
    
    $response = Invoke-RestMethod -Uri $apiUrl -Method Post -Headers $headers -Body $jsonBody
    
    Write-Host "✅ Request successful!" -ForegroundColor Green
    Write-Host "Response Body:"
    $response | ConvertTo-Json -Depth 10
    
} catch {
    Write-Host "❌ Error during request:" -ForegroundColor Red
    Write-Host $_.Exception.Message -ForegroundColor Red
    if ($_.Exception.Response) {
        $errorResponse = $_.Exception.Response.GetResponseStream()
        $reader = New-Object System.IO.StreamReader($errorResponse)
        $responseBody = $reader.ReadToEnd()
        Write-Host "Error Response Body:" -ForegroundColor Red
        Write-Host $responseBody -ForegroundColor Red
    }
}

Write-Host ""
Write-Host "Test finished."

