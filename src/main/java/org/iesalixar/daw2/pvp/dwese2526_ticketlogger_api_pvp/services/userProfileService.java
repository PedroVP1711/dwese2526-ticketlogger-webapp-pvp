package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_api_pvp.services;

import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_api_pvp.dtos.UserProfileDTO;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_api_pvp.dtos.UserProfilePatchDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

public interface userProfileService {

    UserProfileDTO getFormByEmail(String email);

    void updateProfile(String email, UserProfilePatchDTO patchDto, MultipartFile profileImageFile);
}