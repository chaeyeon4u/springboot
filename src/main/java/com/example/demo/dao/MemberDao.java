package com.example.demo.dao;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.dto.Member;

@Mapper
public interface MemberDao {
	public int insert(Member member);	
	public Member selectByMid(String mid);
}
