package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.daos;

import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.entities.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<User> listAllUsers() {
        String query = "SELECT * FROM users";
        return jdbcTemplate.query(query, (rs, rowNum) -> new User(
                rs.getLong("id"),
                rs.getString("username"),
                rs.getString("email"),
                rs.getString("password")
        ));
    }

    @Override
    public void deleteUser(Long id) {
        String query = "DELETE FROM users WHERE id = ?";
        jdbcTemplate.update(query, id);  // No es necesario cambiar nada relacionado con 'password_hash', ya que no lo estamos utilizando aquí.
    }


    @Override
    public void insertUser(User user) {
        String query = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
        jdbcTemplate.update(query, user.getUsername(), user.getEmail(), user.getPassword());  // Usar 'getPasswordHash'
    }

    @Override
    public void updateUser(User user) {
        String sql = "UPDATE users SET username = ?, email = ?, password = ? WHERE id = ?";
        jdbcTemplate.update(sql, user.getUsername(), user.getEmail(), user.getPassword(), user.getId());
    }


    @Override
    public User getUserById(Long id) {
        String query = "SELECT * FROM users WHERE id = ?";
        return jdbcTemplate.queryForObject(query, new Object[]{id}, (rs, rowNum) -> new User(
                rs.getLong("id"),
                rs.getString("username"),
                rs.getString("email"),
                rs.getString("password")  // Usar 'password_hash' en lugar de 'password'
        ));
    }
    }
