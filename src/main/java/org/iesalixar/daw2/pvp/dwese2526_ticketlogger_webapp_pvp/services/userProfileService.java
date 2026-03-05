package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.services;

import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.dtos.UserProfileFormDTO;
import org.springframework.web.multipart.MultipartFile;

public interface userProfileService {

    void updateProfile(String email,
                       UserProfileFormDTO profileDto,
                       MultipartFile profileImageFile);
}