package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.daos;

import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.entities.Role;

import java.util.List;
import java.util.Set;

public interface RoleDAO {

    List<Role> listAllRoles();
    List<Role> findAllByIds(Set<Long> ids);
}
