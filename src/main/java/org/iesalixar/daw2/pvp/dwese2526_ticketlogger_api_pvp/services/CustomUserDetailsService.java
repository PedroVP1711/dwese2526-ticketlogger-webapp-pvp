package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_api_pvp.services;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Servicio mínimo para Spring Security.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Por ahora devolvemos un usuario dummy
        return org.springframework.security.core.userdetails.User
                .withUsername("admin")
                .password("{noop}admin123") // {noop} indica que la contraseña no está codificada
                .roles("ADMIN")
                .build();
    }
}