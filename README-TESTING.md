# 🧪 GUÍA DE PRUEBAS PARA ENDPOINT FACTURA CONTADO

## 📋 **ARCHIVOS DE PRUEBA CREADOS:**

### 1. **`test-factura-contado.json`**
- Datos de prueba para Factura Contado
- Estructura completa con cliente, items y pagos
- Basado en el ejemplo proporcionado

### 2. **`test-endpoint.bat`** (Windows Batch)
- Script para probar el endpoint con curl
- Verifica que la aplicación esté ejecutándose
- Ejecuta la petición POST

### 3. **`test-endpoint.ps1`** (PowerShell)
- Script más avanzado con manejo de errores
- Usa `Invoke-RestMethod` de PowerShell
- Mejor formato de salida

## 🚀 **INSTRUCCIONES DE PRUEBA:**

### **Paso 1: Iniciar la aplicación**
```bash
mvn spring-boot:run
```

### **Paso 2: Verificar que esté ejecutándose**
```bash
netstat -an | findstr :3001
```

### **Paso 3: Ejecutar la prueba**

#### **Opción A: Script Batch (Windows)**
```cmd
test-endpoint.bat
```

#### **Opción B: Script PowerShell**
```powershell
.\test-endpoint.ps1
```

#### **Opción C: Comando manual**
```bash
curl -X POST \
  -H "Content-Type: application/json" \
  -H "X-Company-ID: test-company-001" \
  -d @test-factura-contado.json \
  http://localhost:3001/v1/fe/factura-contado
```

## 📊 **DATOS DE PRUEBA:**

- **Company ID**: `test-company-001`
- **Receipt ID**: `test60` (debe ser único)
- **Cliente**: RUC 44444-1
- **Items**: 1 producto de 1000 PYG
- **Pagos**: 1 pago en efectivo

## 🔍 **RESPUESTAS ESPERADAS:**

### **✅ Éxito (202 Accepted)**
```json
{
  "documentId": "uuid-generado",
  "receiptId": "test60",
  "status": "RECEIVED",
  "message": "Factura Contado recibida y encolada para procesamiento."
}
```

### **❌ Error (400 Bad Request)**
```json
{
  "error": "Datos de entrada inválidos",
  "details": "..."
}
```

### **❌ Error (409 Conflict)**
```json
{
  "error": "Document with this idempotency key already exists: test60"
}
```

## 🛠️ **TROUBLESHOOTING:**

1. **Puerto 3001 no disponible**: Verificar que la aplicación esté ejecutándose
2. **Error de conexión**: Verificar que no haya firewall bloqueando
3. **Error 404**: Verificar que el endpoint esté correctamente mapeado
4. **Error 500**: Revisar logs de la aplicación para detalles

## 📝 **NOTAS:**

- El endpoint requiere el header `X-Company-ID`
- El `receiptid` debe ser único por empresa
- La aplicación debe estar conectada a la base de datos `sifen_local`
- RabbitMQ no es requerido para esta prueba básica

