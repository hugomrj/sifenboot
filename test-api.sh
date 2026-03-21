#!/bin/bash

# Test script for SIFEN API Java
echo "Testing SIFEN API Java..."

# Start the application in background
echo "Starting application..."
cd apps/api-java
mvn spring-boot:run &
APP_PID=$!

# Wait for application to start
echo "Waiting for application to start..."
sleep 30

# Test health endpoint
echo "Testing health endpoint..."
curl -s http://localhost:3001/api/v1/health | jq .

# Test OpenAPI documentation
echo "Testing OpenAPI documentation..."
curl -s http://localhost:3001/api/swagger-ui.html

# Test companies endpoint
echo "Testing companies endpoint..."
curl -s http://localhost:3001/api/v1/companies | jq .

# Test document creation endpoint
echo "Testing document creation..."
curl -X POST http://localhost:3001/api/v1/documents/invoice/contado \
  -H "Content-Type: application/json" \
  -d '{
    "companyId": "test-company-id",
    "docType": "FE",
    "series": "001",
    "number": 1,
    "currency": "PYG",
    "totalAmount": 105000.0,
    "company": {
      "ruc": "12345678-9",
      "razonSocial": "Empresa Test S.A.",
      "tipoContribuyente": "2",
      "cscId": "123456",
      "csc": "ABC123",
      "ambiente": "2"
    },
    "customer": {
      "docType": "RUC",
      "docNumber": "87654321-0",
      "name": "Cliente Test"
    },
    "timbrado": {
      "number": "12345678",
      "validFrom": "2023-01-01T00:00:00",
      "validTo": "2024-12-31T23:59:59"
    },
    "branch": {
      "code": "001",
      "name": "Sucursal Central"
    },
    "pos": {
      "code": "001",
      "description": "Punto de Venta 1"
    },
    "items": [
      {
        "id": "item-1",
        "codigo": "PROD001",
        "description": "Producto de Prueba",
        "qty": 1.0,
        "unitPrice": 100000.0,
        "lineTotal": 100000.0
      }
    ],
    "payments": [
      {
        "id": "payment-1",
        "monto": 105000.0,
        "metodoPago": {
          "codigo": "01",
          "metodo": "Efectivo"
        }
      }
    ]
  }' | jq .

# Cleanup
echo "Stopping application..."
kill $APP_PID

echo "Test completed!"
