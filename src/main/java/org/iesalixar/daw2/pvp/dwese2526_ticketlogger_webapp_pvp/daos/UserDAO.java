package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.daos;

import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.entities.User;
import java.util.List;

public interface UserDAO {

    List<User> listAllUsers();

    void insertUser(User user);

    void updateUser(User user);

    void deleteUser(Long id);

    User getUserById(Long id);
}


