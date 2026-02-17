package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.daos;

import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.entities.User;
import java.util.List;

public interface UserDAO {

    List<User> listAllUsers();

    List<User> listUsersPage(int page, int size, String sortField, String sortDir);

    long countUsers();

    void insertUser(User user);

    void updateUser(User user);

    void deleteUser(Long id);

    User getUserById(Long id);

    boolean existsUserByEmail(String email);

    boolean existsUserByEmailAndNotId(String email, Long id);

    User getUserByEmail(String email);

    User findById(Long userId);


}