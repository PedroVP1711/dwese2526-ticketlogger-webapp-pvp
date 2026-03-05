package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_webapp_pvp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class LoginController {


    @GetMapping("/login")
    public String login(Model model) {
        return "views/login/login"; // Redirige a una plantilla personalizada de login
    }
}
