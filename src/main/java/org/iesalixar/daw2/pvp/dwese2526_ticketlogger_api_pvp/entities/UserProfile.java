package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_api_pvp.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_profiles")
public class UserProfile {

    @Id
    @Column(name = "user_id")
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "phone_number", length = 30)
    private String phoneNumber;

    @Column(name = "profile_image", length = 255)
    private String profileImage;

    @Column(name = "bio", length = 500)
    private String bio;

    @Column(name = "locale", length = 10)
    private String locale;

    @Column(name = "created_at", nullable = false, updatable = false, insertable = false)
    private LocalDateTime createAt;

    @Column(name = "created_at", nullable = false, updatable = false, insertable = false)
    private LocalDateTime updateAt;


    public UserProfile(User user,
                        String firstName,
                        String lastName,
                        String phoneNumber,
                        String profileImage,
                        String bio,
                        String locale) {
        this.user = user;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.profileImage = profileImage;
        this.bio = bio;
        this.locale = locale;
    }

}