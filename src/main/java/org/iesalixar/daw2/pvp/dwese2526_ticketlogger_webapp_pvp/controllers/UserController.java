package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.controllers;

import jakarta.validation.Valid;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.dtos.UserCreateDTO;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.dtos.UserDTO;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.dtos.UserDetailDTO;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.dtos.UserUpdateDTO;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.entities.User;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.repositories.roleRepository;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.repositories.userRepository;
import org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.mappers.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private userRepository userDAO;

    @Autowired
    private roleRepository roleDAO;

    @Autowired
    private MessageSource messageSource;

    // --------------------------
    // Listado de usuarios
    // --------------------------
    @GetMapping("")
    public String listUsers(Model model, @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        logger.info("Solicitando la lista de usuarios...");
        try {
            Page<User> usersPage = userDAO.findAll(pageable); // esto devuelve Page<User>
            Page<UserDTO> usersDTOPage = usersPage.map(UserMapper::toDTO); // convierte a DTO
            model.addAttribute("page", usersDTOPage); // ojo: la vista espera "page"
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
        model.addAttribute("allRoles", roleDAO.findAll());
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
            model.addAttribute("allRoles", roleDAO.findAll());
            return "views/user/user-form";
        }

        try {
            if (userDAO.existsByEmail(dto.getEmail())) {
                String errorMessage = messageSource.getMessage(
                        "msg.user-controller.insert.emailExist", null, locale);
                redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
                return "redirect:/user/new";
            }

            User user = UserMapper.toEntity(dto);
            user.setRoles(new HashSet<>(roleDAO.findAllByIdIn(dto.getRoleIds())));

            userDAO.save(user); // ✅ save() reemplaza insertUser()

            logger.info("Usuario {} insertado con éxito.", dto.getEmail());
        } catch (Exception e) {
            logger.error("Error al insertar usuario {}: {}", dto.getEmail(), e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage",
                    messageSource.getMessage("msg.user-controller.insert.error", null, locale));
        }

        return "redirect:/user";
    }

    // --------------------------
    // Formulario de edición
    // --------------------------
    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id, Model model) {
        logger.info("Mostrando formulario de edición para ID {}", id);

        Optional<User> userOpt = userDAO.findById(id);

        if (userOpt.isEmpty()) {
            model.addAttribute("errorMessage", "Usuario no encontrado");
            return "redirect:/user";
        }

        UserUpdateDTO dto = UserMapper.toUpdateDTO(userOpt.get());
        model.addAttribute("user", dto);
        model.addAttribute("allRoles", roleDAO.findAll());

        return "views/user/user-form";
    }

    // --------------------------
    // Actualizar usuario
    // --------------------------
    @PostMapping("/update")
    public String updateUser(@Valid @ModelAttribute("user") UserUpdateDTO dto,
                             BindingResult result,
                             RedirectAttributes redirectAttributes,
                             Locale locale,
                             Model model) {

        logger.info("Actualizando usuario ID {}", dto.getId());

        if (result.hasErrors()) {
            model.addAttribute("allRoles", roleDAO.findAll());
            return "views/user/user-form";
        }

        try {
            if (userDAO.existsByEmailAndIdNot(dto.getEmail(), dto.getId())) {
                redirectAttributes.addFlashAttribute("errorMessage",
                        messageSource.getMessage("msg.user-controller.update.emailExist", null, locale));
                return "redirect:/user/edit?id=" + dto.getId();
            }

            Optional<User> existingUserOpt = userDAO.findById(dto.getId());

            if (existingUserOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Usuario no encontrado");
                return "redirect:/user";
            }

            User user = UserMapper.toEntity(dto);
            user.setRoles(new HashSet<>(roleDAO.findAllByIdIn(dto.getRoleIds())));

            userDAO.save(user); // ✅ save() reemplaza updateUser()

            logger.info("Usuario ID {} actualizado con éxito.", dto.getId());

        } catch (Exception e) {
            logger.error("Error al actualizar usuario ID {}: {}", dto.getId(), e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage",
                    messageSource.getMessage("msg.user-controller.update.error", null, locale));
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
            userDAO.deleteById(id); // ✅ deleteById() reemplaza deleteUser()
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
    public String showDetail(@RequestParam("id") Long id,
                             Model model,
                             RedirectAttributes redirectAttributes) {

        logger.info("Mostrando detalle del usuario con ID {}", id);

        Optional<User> userOpt = userDAO.findById(id);

        if (userOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Usuario no encontrado");
            return "redirect:/user";
        }

        UserDetailDTO dto = UserMapper.toDetailDTO(userOpt.get());
        model.addAttribute("user", dto);

        return "views/user/user-detail";
    }
}