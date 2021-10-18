package com.example.demo.dto;

import java.util.Date;

import lombok.Data;

@Data
public class Board {
   private int bno;
   private String btitle;
   private String bcontent;
   private String mid;
   private Date bdate;
}