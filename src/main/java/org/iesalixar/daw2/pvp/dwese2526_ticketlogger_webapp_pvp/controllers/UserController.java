package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.controllers;

import jakarta.transaction.Transactional;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.daos.UserDAO;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.daos.UserProfileDAO;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.entities.User;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.entities.UserProfile;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.dtos.UserProfileFormDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.HashSet;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private UserProfileDAO userProfileDAO;

    /* ============================================
       LISTADO DE USUARIOS
     ============================================ */
    @GetMapping("")
    public String listUsers(Model model) {
        List<User> users = userDAO.listAllUsers();
        model.addAttribute("users", users);
        return "views/user/user-list";
    }

    /* ============================================
       DETALLE DE USUARIO
     ============================================ */
    @GetMapping("/detail")
    public String showUserDetail(@RequestParam("id") Long id, Model model) {
        User user = userDAO.getUserById(id);
        if (user == null) {
            return "redirect:/users?error=notFound";
        }
        model.addAttribute("user", user);
        return "views/user/user-detail";
    }

    /* ============================================
       FORMULARIO NUEVO USUARIO
     ============================================ */
    @GetMapping("/new")
    public String showNewForm(Model model) {
        UserProfileFormDTO form = new UserProfileFormDTO();
        model.addAttribute("userProfileForm", form);
        return "views/user/user-form";
    }

    /* ============================================
       FORMULARIO EDICIÓN USUARIO
     ============================================ */
    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id, Model model) {
        User user = userDAO.getUserById(id);
        if (user == null) {
            return "redirect:/users?error=notFound";
        }

        UserProfileFormDTO form = new UserProfileFormDTO();
        form.setUserId(user.getId());
        form.setEmail(user.getEmail());

        UserProfile profile = userProfileDAO.getUserProfileByUserId(user.getId());
        if (profile != null) {
            form.setFirstName(profile.getFirstName());
            form.setLastName(profile.getLastName());
            form.setPhoneNumber(profile.getPhoneNumber());
            form.setProfileImage(profile.getProfileImage());
            form.setBio(profile.getBio());
            form.setLocale(profile.getLocale());
        }

        model.addAttribute("userProfileForm", form);
        return "views/user/user-form";
    }

    /* ============================================
       GUARDAR O ACTUALIZAR USUARIO
     ============================================ */
    @PostMapping("/update")
    @Transactional
    public String saveOrUpdate(@Valid @ModelAttribute("userProfileForm") UserProfileFormDTO form,
                               BindingResult result,
                               Model model) {

        if (result.hasErrors()) {
            return "views/user/user-form";
        }

        User user;
        boolean isNew = false;

        if (form.getUserId() == null) {
            // NUEVO USUARIO
            user = new User();
            user.setEmail(form.getEmail());
            user.setActive(true);
            user.setRoles(new HashSet<>());       // inicializa roles vacíos
            user.setPasswordHash("default123");   // obligatorio según entity
            userDAO.insertUser(user);
            isNew = true;
        } else {
            // USUARIO EXISTENTE
            user = userDAO.getUserById(form.getUserId());
            if (user == null) {
                return "redirect:/users?error=notFound";
            }
        }

        // PERFIL
        UserProfile profile = userProfileDAO.getUserProfileByUserId(user.getId());
        if (profile == null) {
            profile = new UserProfile();
            profile.setId(user.getId());
            profile.setUser(user);
        }

        // Mapear campos del formulario
        profile.setFirstName(form.getFirstName());
        profile.setLastName(form.getLastName());
        profile.setPhoneNumber(form.getPhoneNumber());
        profile.setProfileImage(form.getProfileImage());
        profile.setBio(form.getBio());
        profile.setLocale(form.getLocale());

        // Guardar perfil
        userProfileDAO.saveOrUpdateUserProfile(profile);

        return "redirect:/users/detail?id=" + user.getId();
    }

    /* ============================================
       ELIMINAR USUARIO
     ============================================ */
    @GetMapping("/delete")
    @Transactional
    public String deleteUser(@RequestParam("id") Long id) {
        // 1️⃣ Buscar el usuario
        User user = userDAO.getUserById(id);
        if (user == null) {
            return "redirect:/users?error=notFound";
        }

        // 2️⃣ Borrar perfil si existe
        UserProfile profile = userProfileDAO.getUserProfileByUserId(user.getId());
        if (profile != null) {
            userProfileDAO.saveOrUpdateUserProfile(null); // opcional: si tu DAO tiene delete, mejor usarlo
            // Si no hay delete en el DAO, puedes usar entityManager.remove(profile) dentro de un DAO
        }

        // 3️⃣ Borrar usuario
        userDAO.deleteUser(id);

        return "redirect:/users";
    }
}
