package com.example.demo.dto;

import lombok.Data;

@Data
public class Member {
	private String mid;
	private String mname;
	private String mpassword;
	private int menabled;
	private String mrole;
	private String memail;
}
