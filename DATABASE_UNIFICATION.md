# 🗄️ UNIFICACIÓN DE BASE DE DATOS SIFEN

## 📊 **RESUMEN DE CAMBIOS**

### **✅ DECISIÓN TOMADA: USAR `sifen_local`**

**Razones:**
- 📅 **Más reciente**: Backup del 2025-10-16 23:06:02
- 🏢 **Más datos**: 17 empresas vs 1 empresa demo
- 📋 **Más completo**: Incluye tablas `activity` y `obligation`
- 🔧 **Más migraciones**: 4 vs 2 migraciones (evolución del esquema)

### **🔧 CONFIGURACIONES ACTUALIZADAS**

#### **1. `application.yml`**
```yaml
datasource:
  url: ${DATABASE_URL:jdbc:postgresql://localhost:5432/sifen_local}
  username: ${DATABASE_USERNAME:postgres}
  password: ${DATABASE_PASSWORD:postgres}
```

#### **2. `application.properties`**
```properties
spring.datasource.url=${DATABASE_URL:jdbc:postgresql://localhost:5432/sifen_local}
spring.datasource.username=${DATABASE_USERNAME:postgres}
spring.datasource.password=${DATABASE_PASSWORD:postgres}
```

#### **3. `application-dev.yml`**
```yaml
datasource:
  url: jdbc:postgresql://localhost:5432/sifen_local
  username: ${DATABASE_USERNAME:postgres}
  password: ${DATABASE_PASSWORD:postgres}
```

### **📋 TABLAS DISPONIBLES EN `sifen_local`**

#### **🏢 Tablas Principales:**
- `company` - Empresas (17 registros)
- `user` - Usuarios (1 registro)
- `branch` - Sucursales
- `pos` - Puntos de venta
- `timbrado` - Timbrados
- `invoice` - Facturas (0 registros)
- `invoice_item` - Items de factura
- `invoice_payment` - Pagos de factura

#### **📋 Tablas de Catálogo:**
- `metodo_pago` - Métodos de pago
- `moneda` - Monedas
- `tipo_transaccion` - Tipos de transacción
- `unidad_medida` - Unidades de medida
- `tipo_documento_electronico` - Tipos de documento
- `tipo_operacion` - Tipos de operación
- `tipo_iva` - Tipos de IVA
- `pais_receptor` - Países receptores
- `tipo_documento_identidad` - Tipos de documento de identidad
- `responsable_emision` - Responsables de emisión
- `motivo_emision` - Motivos de emisión
- `denominacion_tarjeta` - Denominaciones de tarjeta

#### **📊 Tablas Adicionales (solo en sifen_local):**
- `activity` - Actividades económicas (17 registros)
- `obligation` - Obligaciones fiscales (8 registros)

### **🔍 VERIFICACIÓN DE CONEXIÓN**

Se creó `DatabaseConnectionTest.java` para verificar:
- ✅ Conexión a la base de datos
- 📊 Información de la base de datos
- 🏢 Conteo de empresas
- 📋 Conteo de actividades
- 📄 Conteo de obligaciones
- 📄 Conteo de facturas

### **🚀 PRÓXIMOS PASOS**

1. **Verificar conexión**: Ejecutar la aplicación y revisar logs
2. **Probar endpoints**: Verificar que los endpoints funcionen
3. **Migrar datos**: Si es necesario, migrar datos de `sifen_db` a `sifen_local`
4. **Limpiar configuración**: Eliminar referencias a `sifen_db`

### **⚠️ NOTAS IMPORTANTES**

- **Backup recomendado**: Hacer backup de `sifen_local` antes de cambios importantes
- **Variables de entorno**: Usar `DATABASE_USERNAME` y `DATABASE_PASSWORD` consistentemente
- **Puerto**: Asegurar que PostgreSQL esté corriendo en puerto 5432
- **Base de datos**: Asegurar que `sifen_local` exista en PostgreSQL

### **🔧 COMANDOS ÚTILES**

```bash
# Verificar conexión a PostgreSQL
psql -h localhost -p 5432 -U postgres -d sifen_local

# Listar bases de datos
psql -h localhost -p 5432 -U postgres -c "\l"

# Verificar tablas en sifen_local
psql -h localhost -p 5432 -U postgres -d sifen_local -c "\dt"
```

