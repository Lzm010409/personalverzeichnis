package de.lukegoll.personalverzeichnis.web.controller;

import java.util.LinkedList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import de.lukegoll.personalverzeichnis.domain.entities.Employee;
import jakarta.annotation.Generated;

@Controller
@RequestMapping("/")
public class HomeController {

	
	@GetMapping("")
	public String getHome(Model model) {
		
		
		model.addAttribute("employees", new LinkedList<Employee>());
		
		return "content/home";
		
	}
	
	
}
