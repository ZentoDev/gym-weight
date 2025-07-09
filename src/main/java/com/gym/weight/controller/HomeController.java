package com.gym.weight.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
	
	@RequestMapping("/")
    public String index() {
        return "index";  // nome file src/main/resources/templates/index.html
    }
}
