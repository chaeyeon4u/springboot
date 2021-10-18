package com.example.demo.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.service.BoardService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/dao")
@Slf4j
public class DaoController {
	@Resource
	private BoardService boardService;
	
	@RequestMapping("/content")
	public String content() {
		log.info("실행");
		return "dao/content";
	}
}
