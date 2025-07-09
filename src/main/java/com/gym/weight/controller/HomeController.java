package com.gym.weight.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
	
	@GetMapping("/")
    public String index() {
        return "index";  // nome file src/main/resources/templates/index.html
    }
}
