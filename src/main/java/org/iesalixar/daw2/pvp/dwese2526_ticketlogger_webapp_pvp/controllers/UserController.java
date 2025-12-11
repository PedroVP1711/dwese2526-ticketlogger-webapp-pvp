package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.controllers;

import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.daos.UserDAO;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.entities.User;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.entities.UserProfile; // <<-- NECESITAS IMPORTAR ESTO
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserDAO userDAO;

    @GetMapping("")
    public String listUsers(Model model) {
        // ...
        List<User> userList = userDAO.listAllUsers();
        model.addAttribute("users", userList);
        return "views/user/user-list";
    }

    /**
     * Muestra el formulario para crear un nuevo usuario.
     * Se inicializa User y UserProfile para evitar errores de Thymeleaf (Property cannot be found on null).
     */

        @GetMapping("/new")
        public String showNewForm(Model model) {
            // 1. Inicializar el usuario principal
            User newUser = new User();

            // 2. Inicializar el perfil (Lo que el formulario espera como 'userProfileForm')
            UserProfile userProfile = new UserProfile();

            // 3. Enlazar ambos objetos para la persistencia
            newUser.setUserProfile(userProfile);
            userProfile.setUser(newUser);

            // 4. Añadir el objeto principal (User)
            model.addAttribute("user", newUser);

            // 5. AÑADIR EL OBJETO QUE EL FORMULARIO ESTÁ USANDO (userProfileForm)
            // Esto es necesario porque tu formulario usa "userProfileForm.profileImage"
            model.addAttribute("userProfileForm", userProfile);

            return "views/user/user-form";
        }

    @PostMapping("/save")
    public String saveUser(@ModelAttribute("user") User user) {
        // El @ModelAttribute ya ha enlazado el User y el UserProfile si la estructura es correcta.

        // Aseguramos la relación bidireccional antes de guardar
        if (user.getUserProfile() != null) {
            user.getUserProfile().setUser(user);
        }

        userDAO.insertUser(user);
        return "redirect:/users";
    }

// UserController.java

    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id, Model model) {
        User existingUser = userDAO.getUserById(id);

        // Si el perfil no existe, inicialízalo para que el formulario no falle
        UserProfile userProfile = existingUser.getUserProfile();
        if (userProfile == null) {
            userProfile = new UserProfile();
            existingUser.setUserProfile(userProfile);
            userProfile.setUser(existingUser);
        }

        // Añadir el objeto principal
        model.addAttribute("user", existingUser);

        // Añadir el objeto que el formulario espera
        model.addAttribute("userProfileForm", userProfile);

        return "views/user/user-form";
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute("user") User user) {
        // Aseguramos la relación bidireccional antes de actualizar
        if (user.getUserProfile() != null) {
            user.getUserProfile().setUser(user);
        }

        userDAO.updateUser(user);
        return "redirect:/users";
    }

    @GetMapping("/delete")
    public String deleteUser(@RequestParam("id") Long id) {
        userDAO.deleteUser(id);
        return "redirect:/users";
    }

    @GetMapping("/detail")
    public String showUserDetail(@RequestParam("id") Long id, Model model) {
        User user = userDAO.getUserById(id);
        if (user != null) {
            model.addAttribute("user", user);
        } else {
            return "redirect:/users?error=notFound";
        }
        return "views/user/user-detail";
    }
}