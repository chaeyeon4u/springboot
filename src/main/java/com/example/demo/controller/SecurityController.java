package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/security")
@Slf4j
public class SecurityController {
	@RequestMapping("/content")
	public String content() {
		log.info("실행");
		return "security/content";
	}
	
	@RequestMapping("/loginForm")
	public String loginForm() {
		log.info("실행");
		return "security/loginForm";
	}
	
	@RequestMapping("/loginError")
	public String loginError(Model model) {
		log.info("실행");
		model.addAttribute("loginError","true");
		return "security/loginForm";
	}
	
	@RequestMapping("/accessDenied")
	public String accessDenied() {
		log.info("실행");
		return "security/accessDenied";
	}
	
   @RequestMapping("/admin/action")
   public String adminAction() {
      log.info("실행");
      return "redirect:/security/content";
   }
   
   @RequestMapping("/manager/action")
   public String managerAction() {
      log.info("실행");
      return "redirect:/security/content";
   }
   
   @RequestMapping("/user/action")
   public String userAction() {
      log.info("실행");
      return "redirect:/security/content";
   }
}
