package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_api_pvp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

@Configuration
public class KeyConfig {

    @Bean
    public KeyPair jwtKeyPair() throws NoSuchAlgorithmException {
        // Genera una clave RSA en memoria para desarrollo
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }
}