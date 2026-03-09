package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_api_pvp.controllers;

import ch.qos.logback.core.model.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {


    @GetMapping("/")
    public String home(Model model) {
        return "index";
    }
}
