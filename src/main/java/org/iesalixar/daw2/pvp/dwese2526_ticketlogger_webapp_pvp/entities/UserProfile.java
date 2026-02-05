package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.entities;

public class UserProfile {

    private User user;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String bio;
    private String profileImagePath;
    private String locale;

    // Constructor vacío
    public UserProfile() {}

    // Constructor con User
    public UserProfile(User user) {
        this.user = user;
    }

    // Getters y setters
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getProfileImagePath() { return profileImagePath; }
    public void setProfileImagePath(String profileImagePath) { this.profileImagePath = profileImagePath; }

    public String getLocale() { return locale; }
    public void setLocale(String locale) { this.locale = locale; }

}
