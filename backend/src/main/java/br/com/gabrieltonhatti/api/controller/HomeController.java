package br.com.gabrieltonhatti.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/")
public class HomeController {
    
    @GetMapping
    public String Hello() {
        return "<h1>Hello World!</h1>";
    }

}
