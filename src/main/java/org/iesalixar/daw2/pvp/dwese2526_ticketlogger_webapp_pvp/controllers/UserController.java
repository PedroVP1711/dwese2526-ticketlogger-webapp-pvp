package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.controllers;


import jakarta.validation.Valid;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.daos.RoleDAO;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.daos.UserDAO;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.dtos.UserCreateDTO;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.dtos.UserDTO;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.dtos.UserDetailDTO;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.dtos.UserUpdateDTO;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.entities.User;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.mappers.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private RoleDAO roleDAO;

    // --------------------------
    // Listado de usuarios
    // --------------------------
    @GetMapping("")
    public String listUsers(Model model) {
        logger.info("Solicitando la lista de usuarios...");
        try {
            List<User> users = userDAO.listAllUsers();
            List<UserDTO> usersDTO = UserMapper.toDTOList(users);
            model.addAttribute("listUsers", usersDTO);
        } catch (Exception e) {
            logger.error("Error al listar usuarios: {}", e.getMessage());
            model.addAttribute("errorMessage", messageSource.getMessage(
                    "msg.user-controller.list.error", null, Locale.getDefault()));
        }
        return "views/user/user-list";
    }

    // --------------------------
    // Formulario de nuevo usuario
    // --------------------------
    @GetMapping("/new")
    public String showNewForm(Model model) {
        logger.info("Mostrando formulario para crear usuario...");
        model.addAttribute("user", new UserCreateDTO());
        model.addAttribute("allRoles", roleDAO.listAllRoles());
        return "views/user/user-form";
    }

    // --------------------------
    // Insertar nuevo usuario
    // --------------------------
    @PostMapping("/insert")
    public String insertUser(@Valid @ModelAttribute("user") UserCreateDTO dto,
                             BindingResult result,
                             RedirectAttributes redirectAttributes,
                             Locale locale,
                             Model model) {
        logger.info("Insertando nuevo usuario: {}", dto.getEmail());

        if (result.hasErrors()) {
            model.addAttribute("allRoles",roleDAO.listAllRoles());
            return "views/user/user-form";
        }

        try {
            if (userDAO.existsByEmail(dto.getEmail())) {
                String errorMessage = messageSource.getMessage(
                        "msg.user-controller.insert.emailExist", null, locale);
                redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
                return "redirect:/user/new";
            }

            var roles = new HashSet<>(roleDAO.findAllByIds(dto.getRoleIds()));

            User user = UserMapper.toEntity(dto);
            userDAO.insertUser(user);
            logger.info("Usuario {} insertado con éxito.", dto.getEmail());
        } catch (Exception e) {
            logger.error("Error al insertar usuario {}: {}", dto.getEmail(), e.getMessage());
            String errorMessage = messageSource.getMessage(
                    "msg.user-controller.insert.error", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
        }

        return "redirect:/user";
    }

    // --------------------------
    // Formulario de edición
    // --------------------------
    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id, Model model) {
        logger.info("Mostrando formulario de edición para ID {}", id);
        try {
            User user = userDAO.getUserById(id);
            if (user == null) {
                model.addAttribute("errorMessage", "Usuario no encontrado");
                return "redirect:/user";
            }
            UserUpdateDTO dto = UserMapper.toUpdateDTO(user);
            model.addAttribute("user", dto);
            model.addAttribute("allRoles", roleDAO.listAllRoles());
        } catch (Exception e) {
            logger.error("Error al obtener usuario ID {}: {}", id, e.getMessage());
            model.addAttribute("errorMessage", "Error al obtener usuario");
            return "redirect:/user";
        }
        return "views/user/user-form";
    }

    // --------------------------
// Actualizar usuario
// --------------------------
    @PostMapping("/update")
    public String updateUser(@Valid @ModelAttribute("user") UserUpdateDTO dto,
                             BindingResult result,
                             RedirectAttributes redirectAttributes,
                             Locale locale) {
        logger.info("Actualizando usuario ID {}", dto.getId());

        if (result.hasErrors()) {
            return "views/user/user-form";
        }

        try {
            if (userDAO.existsUserByEmailAndNotId(dto.getEmail(), dto.getId())) {
                String errorMessage = messageSource.getMessage(
                        "msg.user-controller.update.emailExist", null, locale);
                redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
                return "redirect:/user/edit?id=" + dto.getId();
            }

            User existingUser = userDAO.getUserById(dto.getId());
            if (existingUser == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Usuario no encontrado");
                return "redirect:/user";
            }

            // >>> Obtener los roles desde roleIds que llegan en el DTO
            var roles = new HashSet<>(roleDAO.findAllByIds(dto.getRoleIds()));

            // Mapear DTO -> entidad User incluyendo roles
            User user = UserMapper.toEntity(dto);

            // Actualizar el usuario con los roles
            userDAO.updateUser(user);
            logger.info("Usuario ID {} actualizado con éxito.", dto.getId());
        } catch (Exception e) {
            logger.error("Error al actualizar usuario ID {}: {}", dto.getId(), e.getMessage());
            String errorMessage = messageSource.getMessage(
                    "msg.user-controller.update.error", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
        }

        return "redirect:/user";
    }


    // --------------------------
    // Eliminar usuario
    // --------------------------
    @PostMapping("/delete")
    public String deleteUser(@RequestParam("id") Long id,
                             RedirectAttributes redirectAttributes) {
        logger.info("Eliminando usuario ID {}", id);
        try {
            userDAO.deleteUser(id);
            logger.info("Usuario ID {} eliminado.", id);
        } catch (Exception e) {
            logger.error("Error al eliminar usuario ID {}: {}", id, e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage",
                    messageSource.getMessage("msg.user-controller.delete.error", null, Locale.getDefault()));
        }
        return "redirect:/user";
    }

    // --------------------------
    // Vista de detalle
    // --------------------------
    @GetMapping("/detail")
    public String showDetail(@RequestParam("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        logger.info("Mostrando detalle del usuario con ID {}", id);

        try {
            User user = userDAO.getUserById(id);

            if (user == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Usuario no encontrado");
                return "redirect:/user";
            }

            UserDetailDTO dto = UserMapper.toDetailDTO(user);
            model.addAttribute("user", dto);

        } catch (Exception e) {
            logger.error("Error al obtener detalle del usuario ID {}: {}", id, e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error cargando detalle del usuario");
            return "redirect:/user";
        }

        return "views/user/user-detail";
    }

}