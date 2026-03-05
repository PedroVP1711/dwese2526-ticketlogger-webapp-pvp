package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.services;

import lombok.RequiredArgsConstructor;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.dtos.UserProfileFormDTO;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.entities.User;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.entities.UserProfile;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.exceptions.ResourceNotFoundException;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.mappers.UserProfileMapper;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.repositories.userProfileRepository;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.repositories.userRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class userProfileServiceImpl implements userProfileService {

    @Autowired
    private userRepository userRepository;

    @Autowired
    private userProfileRepository userProfileRepository;

    @Override
    public void updateProfile(String email,
                              UserProfileFormDTO profileDto,
                              MultipartFile profileImageFile) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("user", "email", email));

        UserProfile profile = userProfileRepository
                .findByUserId(user.getId())
                .orElse(null);

        if (profile == null) {
            profile = UserProfileMapper.toNewEntity(profileDto, user);
        } else {
            UserProfileMapper.copyToExistingEntity(profileDto, profile);
        }

        userProfileRepository.save(profile);
    }
}