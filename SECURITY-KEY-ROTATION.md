# SIFEN v150 - Security Key Rotation Guide

## Overview

This document describes the secure key management and rotation procedures for the SIFEN v150 application. The system implements automatic key rotation for JWT secrets, encryption keys, and other sensitive configuration values.

## Security Features

### 1. Environment Variable Based Configuration
- All sensitive values are stored in environment variables
- No hardcoded secrets in configuration files
- Support for different environments (dev, staging, production)

### 2. Key Rotation
- Automatic key rotation based on configurable intervals
- Manual key rotation support
- Key versioning and rollback capability
- Audit logging for all key operations

### 3. Encryption at Rest
- Sensitive values can be encrypted using the master key
- AES encryption with configurable algorithms
- Secure key generation and storage

## Required Environment Variables

### JWT Configuration
```bash
# JWT Secret (minimum 32 characters)
JWT_SECRET=your-super-secret-jwt-key-min-32-chars

# JWT Expiration (in milliseconds)
JWT_EXPIRATION=86400000
```

### SIFEN Security Configuration
```bash
# SIFEN JWT Secret (minimum 32 characters)
SIFEN_SECURITY_JWT_SECRET=your-sifen-jwt-secret-min-32-chars

# Master Encryption Key (minimum 32 characters)
SIFEN_SECURITY_MASTER_KEY=your-master-encryption-key-min-32-chars

# Encryption Settings
SIFEN_SECURITY_ENCRYPTION_ENABLED=true
SIFEN_SECURITY_LOGGING_SANITIZE=true
```

### Key Rotation Configuration
```bash
# Enable automatic key rotation
SIFEN_SECURITY_KEY_ROTATION_ENABLED=true

# JWT rotation interval (24 hours in milliseconds)
SIFEN_SECURITY_KEY_ROTATION_JWT_INTERVAL=86400000

# Master key rotation interval (7 days in milliseconds)
SIFEN_SECURITY_KEY_ROTATION_MASTER_KEY_INTERVAL=604800000

# Key retention period (30 days)
SIFEN_SECURITY_KEY_ROTATION_RETENTION_DAYS=30
```

### Database and Redis Configuration
```bash
# Database password
DATABASE_PASSWORD=your-database-password

# Redis password (optional)
REDIS_PASSWORD=your-redis-password
```

### SIFEN API Configuration
```bash
# SIFEN certificate password
SIFEN_CERT_PASS=your-certificate-password

# SIFEN client secret
SIFEN_AUTH_CLIENT_SECRET=your-sifen-client-secret
```

## Key Rotation Procedures

### 1. Automatic Rotation

The system automatically rotates keys based on the configured intervals:

- **JWT Secrets**: Every 24 hours (configurable)
- **Master Key**: Every 7 days (configurable)
- **Cleanup**: Old keys are automatically removed after 30 days

### 2. Manual Rotation

To manually rotate keys, use the KeyRotationService:

```java
@Autowired
private KeyRotationService keyRotationService;

// Rotate JWT secret
String newJwtSecret = keyRotationService.rotateJwtSecret();

// Rotate SIFEN JWT secret
String newSifenJwtSecret = keyRotationService.rotateSifenJwtSecret();

// Rotate master key
String newMasterKey = keyRotationService.rotateMasterKey();

// Rotate all keys
keyRotationService.rotateAllKeys();
```

### 3. Key Validation

Before starting the application, validate that all required secrets are configured:

```java
@Autowired
private SecureSecretsService secureSecretsService;

// Validate all required secrets
secureSecretsService.validateRequiredSecrets();
```

## Security Best Practices

### 1. Key Generation
- Use cryptographically secure random generators
- Minimum key length: 32 characters
- Use different keys for different environments
- Never reuse keys across environments

### 2. Key Storage
- Store keys in environment variables
- Use secure key management systems in production
- Never commit keys to version control
- Use different keys for different environments

### 3. Key Rotation
- Rotate keys regularly (recommended: 24 hours for JWT, 7 days for master key)
- Monitor key rotation logs
- Test key rotation procedures in staging environment
- Have rollback procedures ready

### 4. Logging and Monitoring
- Enable audit logging for key operations
- Monitor key rotation schedules
- Alert on key rotation failures
- Log key access for security auditing

## Production Deployment

### 1. Environment Setup
```bash
# Set all required environment variables
export JWT_SECRET="$(openssl rand -base64 32)"
export SIFEN_SECURITY_JWT_SECRET="$(openssl rand -base64 32)"
export SIFEN_SECURITY_MASTER_KEY="$(openssl rand -base64 32)"
export DATABASE_PASSWORD="your-secure-database-password"
export SIFEN_CERT_PASS="your-secure-certificate-password"
export SIFEN_AUTH_CLIENT_SECRET="your-secure-client-secret"

# Enable key rotation
export SIFEN_SECURITY_KEY_ROTATION_ENABLED=true
```

### 2. Key Management System Integration
For production environments, integrate with enterprise key management systems:

- HashiCorp Vault
- AWS Secrets Manager
- Azure Key Vault
- Google Secret Manager

### 3. Monitoring and Alerting
Set up monitoring for:
- Key rotation failures
- Missing environment variables
- Unauthorized key access
- Key rotation schedule compliance

## Troubleshooting

### Common Issues

1. **Missing Environment Variables**
   - Error: `JWT_SECRET environment variable is not configured`
   - Solution: Set the required environment variable

2. **Key Length Validation**
   - Error: `JWT_SECRET must be at least 32 characters long`
   - Solution: Generate a longer key

3. **Key Rotation Failures**
   - Check logs for specific error messages
   - Verify key rotation permissions
   - Test key rotation in staging environment

### Log Analysis

Monitor these log messages:
- `Secret access: JWT_SECRET - accessed`
- `Secret rotation completed: JWT_SECRET - Key ID: xxx`
- `All required secrets are properly configured`
- `Error during scheduled JWT secret rotation`

## Security Considerations

### 1. Key Compromise
If a key is compromised:
1. Immediately rotate the compromised key
2. Review access logs
3. Update all dependent systems
4. Investigate the compromise source

### 2. Key Recovery
- Maintain secure backups of key rotation procedures
- Document key recovery processes
- Test recovery procedures regularly

### 3. Compliance
- Ensure key rotation meets compliance requirements
- Document key management procedures
- Maintain audit trails for key operations

## Support

For security-related issues:
1. Check application logs for error messages
2. Verify environment variable configuration
3. Test key rotation procedures
4. Contact security team for key compromise incidents

## References

- [SIFEN v150 Technical Manual](https://sifen.gov.py)
- [Spring Security Documentation](https://spring.io/projects/spring-security)
- [OWASP Key Management Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Key_Management_Cheat_Sheet.html)
