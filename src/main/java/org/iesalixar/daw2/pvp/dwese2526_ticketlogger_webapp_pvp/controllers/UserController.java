package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.controllers;

import jakarta.transaction.Transactional;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.daos.UserDAO;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.daos.UserProfileDAO;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.entities.User;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.entities.UserProfile;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.dtos.UserProfileFormDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private UserProfileDAO userProfileDAO;

    @Autowired
    private MessageSource messageSource;

    /* ============================================
       LISTADO DE USUARIOS
     ============================================ */
    @GetMapping("")
    public String listUsers(Model model, Locale locale) {
        List<User> users = userDAO.listAllUsers();
        model.addAttribute("users", users);
        return "views/user/user-list";
    }

    /* ============================================
       DETALLE DE USUARIO
     ============================================ */
    @GetMapping("/detail")
    public String showUserDetail(@RequestParam("id") Long id, Model model, RedirectAttributes redirectAttributes, Locale locale) {
        User user = userDAO.getUserById(id);
        if (user == null) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    messageSource.getMessage("msg.user-controller.detail.notFound", null, locale));
            return "redirect:/users";
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
    public String showEditForm(@RequestParam("id") Long id, Model model, RedirectAttributes redirectAttributes, Locale locale) {
        User user = userDAO.getUserById(id);
        if (user == null) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    messageSource.getMessage("msg.user-controller.edit.notFound", null, locale));
            return "redirect:/users";
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
                               Model model,
                               RedirectAttributes redirectAttributes,
                               Locale locale) {

        if (result.hasErrors()) {
            return "views/user/user-form";
        }

        User user;
        boolean isNew = form.getUserId() == null;

        if (isNew) {
            user = createNewUser(form);
        } else {
            user = userDAO.getUserById(form.getUserId());
            if (user == null) {
                redirectAttributes.addFlashAttribute("errorMessage",
                        messageSource.getMessage("msg.user-controller.update.notFound", null, locale));
                return "redirect:/users";
            }
        }

        updateUserProfile(user, form);

        return "redirect:/users/detail?id=" + user.getId();
    }

    /* ============================================
       ELIMINAR USUARIO
     ============================================ */
    @GetMapping("/delete")
    @Transactional
    public String deleteUser(@RequestParam("id") Long id, RedirectAttributes redirectAttributes, Locale locale) {
        User user = userDAO.getUserById(id);
        if (user == null) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    messageSource.getMessage("msg.user-controller.delete.notFound", null, locale));
            return "redirect:/users";
        }

        userDAO.deleteUser(id);
        return "redirect:/users";
    }

    /* ============================================
       MÉTODOS AUXILIARES
     ============================================ */

    private User createNewUser(UserProfileFormDTO form) {
        User user = new User();
        user.setEmail(form.getEmail());
        user.setActive(true);
        user.setRoles(new HashSet<>());
        user.setPasswordHash("default123");  // Temporario hasta implementes el sistema de contraseñas.
        userDAO.insertUser(user);
        return user;
    }

    private void updateUserProfile(User user, UserProfileFormDTO form) {
        UserProfile profile = userProfileDAO.getUserProfileByUserId(user.getId());
        if (profile == null) {
            profile = new UserProfile();
            profile.setUser(user);
        }

        profile.setFirstName(form.getFirstName());
        profile.setLastName(form.getLastName());
        profile.setPhoneNumber(form.getPhoneNumber());
        profile.setProfileImage(form.getProfileImage());
        profile.setBio(form.getBio());
        profile.setLocale(form.getLocale());

        userProfileDAO.saveOrUpdateUserProfile(profile);
    }
}
