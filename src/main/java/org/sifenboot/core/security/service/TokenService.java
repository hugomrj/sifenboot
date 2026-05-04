package org.sifenboot.core.security.service;
import org.sifenboot.app.emisor.model.Emisor;
import org.sifenboot.app.emisor.repository.EmisorRepository;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    private final EmisorRepository emisorRepository;

    public TokenService(EmisorRepository emisorRepository) {
        this.emisorRepository = emisorRepository;
    }

    public boolean esTokenValido(String codEmisor, String token) {
        return emisorRepository.findByCodEmisor(codEmisor)
                .map(Emisor::getConfiguracion)
                .map(config -> {
                    String dbToken = config.getApiToken();

                    // Si el token es null, vacío o solo espacios, la auth está desactivada
                    if (dbToken == null || dbToken.trim().isEmpty()) {
                        return true;
                    }

                    // Si hay un token configurado, validamos que coincida con el recibido
                    return dbToken.equals(token);
                })
                .orElse(false); // Emisor no existe en BD, denegamos acceso
    }
}