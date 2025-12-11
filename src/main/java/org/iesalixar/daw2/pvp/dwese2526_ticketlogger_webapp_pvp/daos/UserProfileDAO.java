package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.daos;

import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.entities.UserProfile;

public interface UserProfileDAO {

    UserProfile findByUserId(Long userId);
    UserProfile findByProfileId(Long id);
    void save(UserProfile profile);
    UserProfile update(UserProfile profile);
    void delete(Long id);
}