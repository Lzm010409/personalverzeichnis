package de.lukegoll.personalverzeichnis.web.controller;

import java.util.LinkedList;

import de.lukegoll.personalverzeichnis.domain.entities.CapabilityType;
import de.lukegoll.personalverzeichnis.domain.services.CapabilityService;
import de.lukegoll.personalverzeichnis.domain.services.CapabilityTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import de.lukegoll.personalverzeichnis.domain.entities.Employee;
import jakarta.annotation.Generated;

@Controller
@RequestMapping("/")
public class HomeController {
	private final CapabilityTypeService capabilityTypeService;

	@Autowired
	public HomeController(CapabilityTypeService capabilityTypeService) {
		this.capabilityTypeService = capabilityTypeService;
	}

	@GetMapping("")
	public String getHome(Model model) {
		capabilityTypeService.save(new CapabilityType());
		capabilityTypeService.findAll();
		model.addAttribute("employees", new LinkedList<Employee>());
		
		return "content/home";
		
	}
	
	
}
