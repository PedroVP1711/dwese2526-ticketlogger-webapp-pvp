package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.controllers;

import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.daos.UserDAO;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.entities.User;
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

// UserController.java

    @GetMapping("")
    public String listUsers(Model model) {
        // ... Tu lógica de paginación/sort

        // CAMBIA "listUsers" por "users"
        // model.addAttribute("listUsers", userPage.getContent()); // Si usas Page
        // O:
        List<User> userList = userDAO.listAllUsers();
        model.addAttribute("users", userList); // <--- ¡Corregido!

        // ... Las otras variables de paginación si las usas (totalPages, currentPage, etc.)

        return "views/user/user-list.html";
    }

    @GetMapping("/new")
    public String showNewForm(Model model) {
        model.addAttribute("user", new User());
        return "views/user/user-form";
    }

    @PostMapping("/save")
    public String saveUser(@ModelAttribute("user") User user) {
        userDAO.insertUser(user);
        return "redirect:/users"; // Redirigir a la lista de usuarios después de guardar
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id, Model model) {
        User existingUser = userDAO.getUserById(id);
        model.addAttribute("user", existingUser);
        return "views/user/user-form";  // El formulario de edición
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute("user") User user) {
        // Aquí estamos actualizando el usuario
        userDAO.updateUser(user);
        return "redirect:/users";  // Redirigir a la lista de usuarios
    }

    @GetMapping("/delete")
    public String deleteUser(@RequestParam("id") Long id) {
        userDAO.deleteUser(id);
        return "redirect:/users";
    }

    // UserController.java

// ... otros métodos ...

    // Este método maneja la URL: /users/detail?id=X
    @GetMapping("/detail")
    public String showUserDetail(@RequestParam("id") Long id, Model model) {

        // 1. Busca el usuario en la base de datos usando el ID que viene en la URL
        User user = userDAO.getUserById(id);

        // 2. Si el usuario existe, lo añade al "Modelo" con el nombre "user"
        if (user != null) {
            model.addAttribute("user", user);
        } else {
            // Opcional: manejar el caso en que el ID no exista
            return "redirect:/users?error=notFound";
        }

        // 3. Indica a Spring que debe usar la plantilla "views/user/detail"
        // (Asumiendo que tu archivo se llama detail.html y está en la ruta views/user/)
        return "views/user/user-detail";
    }

// ... resto de métodos ...

}

