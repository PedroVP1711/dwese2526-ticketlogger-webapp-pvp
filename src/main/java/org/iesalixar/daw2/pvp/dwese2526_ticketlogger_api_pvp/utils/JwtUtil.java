package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_api_pvp.utils;

import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.function.Function;

/**
 * Clase utilitaria para JWT.
 * Actualmente funciona como mock: devuelve valores por defecto
 * para que la app pueda arrancar sin configuración de claves.
 */
@Component
public class JwtUtil {

    private static final long JWT_EXPIRATION = 3_600_000L; // 1 hora

    /**
     * Extrae el username del token.
     * @param token token JWT
     * @return "user" por defecto
     */
    public String extractUsername(String token) {
        return "user";
    }

    /**
     * Extrae cualquier claim del token.
     * @param token token JWT
     * @param claimsResolver función que procesa los claims
     * @return null (mock)
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        return null;
    }

    /**
     * Extrae todos los claims del token.
     * @param token token JWT
     * @return null (mock)
     */
    public Claims extractAllClaims(String token) {
        return null;
    }

    /**
     * Genera un token para un usuario con roles.
     * @param username nombre de usuario
     * @param roles roles del usuario
     * @return "fake-token" por defecto
     */
    public String generateToken(String username, List<String> roles) {
        return "fake-token";
    }

    /**
     * Valida un token.
     * @param token token JWT
     * @param username nombre de usuario esperado
     * @return true siempre
     */
    public boolean validateToken(String token, String username) {
        return true;
    }

    /**
     * Comprueba si un token ha expirado.
     * @param claims claims del token
     * @return false siempre
     */
    private boolean isTokenExpired(Claims claims) {
        return false;
    }
}