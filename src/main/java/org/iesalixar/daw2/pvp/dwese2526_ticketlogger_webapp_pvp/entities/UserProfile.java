package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import org.springframework.web.multipart.MultipartFile;


@Data
@NoArgsConstructor
@Entity
@Table(name = "user_profiles")
public class UserProfile {

    @Id
    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(name = "Id", nullable = false)
    private Long Id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "bio")
    private String bio;

    @Column(name = "locale")
    private String locale;

    private String profileImagePath;

    
    @Transient
    private MultipartFile profileImageFile;

    public String getProfileImage() {
        return profileImage; // Devuelve la ruta de la imagen de perfil
    }

    public String getProfileImagePath() {
    return profileImagePath;
}

public void setProfileImagePath(String profileImagePath) {
    this.profileImagePath = profileImagePath;
}

}
