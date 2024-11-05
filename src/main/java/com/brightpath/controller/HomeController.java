package com.brightpath.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.brightpath.model.Member;

@Controller
public class HomeController {

	@Value("${gym.image.path}")
	private String gymImagePath;

	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("newMember", new Member());

		System.out.println("Error message retrieved: " + model.getAttribute("errorMessage"));
		// Directly retrieve the flash attributes
		if (model.containsAttribute("message")) {
			model.addAttribute("message", model.getAttribute("message"));
			System.out.println("Message retrieved: " + model.getAttribute("message"));
		}
		if (model.containsAttribute("errorMessage")) {
			model.addAttribute("errorMessage", model.getAttribute("errorMessage"));
			System.out.println("Error message retrieved: " + model.getAttribute("errorMessage"));
		}

		model.addAttribute("gymImagePath", gymImagePath);

		return "index";
	}

	@GetMapping("/about")
	public String about() {

		return "about";
	}

}
