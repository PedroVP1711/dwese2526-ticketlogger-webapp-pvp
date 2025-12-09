package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.daos;

import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.entities.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // RowMapper para mapear todas las columnas de la tabla 'users' a la entidad User
    private final RowMapper<User> userRowMapper = new RowMapper<User>() {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setUsername(rs.getString("username"));
            user.setEmail(rs.getString("email"));

            // CORRECCIÓN CLAVE: Usar 'password_hash'
            user.setPasswordHash(rs.getString("password_hash"));

            // Mapeo de los nuevos campos de seguridad
            user.setActive(rs.getBoolean("active"));
            user.setAccountNonLocked(rs.getBoolean("account_non_locked"));
            user.setLastPasswordChange(rs.getTimestamp("last_password_change").toInstant());

            // password_expires_at es NULLABLE, hay que comprobar
            user.setPasswordExpiresAt(rs.getTimestamp("password_expires_at") != null ?
                    rs.getTimestamp("password_expires_at").toInstant() : null);

            user.setFailedLoginAttempts(rs.getInt("failed_login_attempts"));
            user.setEmailVerified(rs.getBoolean("email_verified"));
            user.setMustChangePassword(rs.getBoolean("must_change_password"));

            return user;
        }
    };


    // ----------------------------------------
    // MÉTODOS CRUD
    // ----------------------------------------

    @Override
    public List<User> listAllUsers() {
        String query = "SELECT * FROM users ORDER BY username ASC";
        // Usamos el RowMapper completo
        return jdbcTemplate.query(query, userRowMapper);
    }

    @Override
    public void insertUser(User user) {
        String query = "INSERT INTO users (" +
                "username, email, password_hash, active, account_non_locked, " +
                "last_password_change, password_expires_at, failed_login_attempts, " +
                "email_verified, must_change_password" +
                ") VALUES (?, ?, ?, ?, ?, NOW(), ?, ?, ?, ?)";

        // El campo password_expires_at debe ser un valor de fecha o NULL.
        // Si quieres usarlo como en data.sql, necesitarías un DAO más complejo o hacerlo en el servicio.
        // Aquí lo simplificamos (se insertará con los valores por defecto si no se proporcionan, pero usamos los campos)
        jdbcTemplate.update(query,
                user.getUsername(),
                user.getEmail(),
                user.getPasswordHash(), // Usamos el campo con hash
                user.isActive(),
                user.isAccountNonLocked(),
                user.getPasswordExpiresAt(), // Si es null, funcionará
                user.getFailedLoginAttempts(),
                user.isEmailVerified(),
                user.isMustChangePassword()
        );
    }

    @Override
    public void updateUser(User user) {
        String sql = "UPDATE users SET " +
                "username = ?, email = ?, password_hash = ?, active = ?, " +
                "account_non_locked = ?, password_expires_at = ?, failed_login_attempts = ?, " +
                "email_verified = ?, must_change_password = ? " +
                "WHERE id = ?";

        // La actualización de last_password_change debe manejarse con lógica de negocio aparte
        jdbcTemplate.update(sql,
                user.getUsername(),
                user.getEmail(),
                user.getPasswordHash(),
                user.isActive(),
                user.isAccountNonLocked(),
                user.getPasswordExpiresAt(),
                user.getFailedLoginAttempts(),
                user.isEmailVerified(),
                user.isMustChangePassword(),
                user.getId()
        );
    }

    @Override
    public void deleteUser(Long id) {
        String query = "DELETE FROM users WHERE id = ?";
        jdbcTemplate.update(query, id);
    }

    @Override
    public User getUserById(Long id) {
        String query = "SELECT * FROM users WHERE id = ?";
        // Usamos el RowMapper completo
        return jdbcTemplate.queryForObject(query, new Object[]{id}, userRowMapper);
    }
}