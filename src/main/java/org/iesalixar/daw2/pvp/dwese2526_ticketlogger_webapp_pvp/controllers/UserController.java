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

    @GetMapping("")
    public String listUsers(Model model) {
        List<User> listUsers = userDAO.listAllUsers();
        model.addAttribute("listUsers", listUsers);
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
}

