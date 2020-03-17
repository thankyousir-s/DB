package com.cy.pj.sys.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cy.pj.common.vo.JsonResult;
import com.cy.pj.sys.service.SysUserService;

@SpringBootTest
public class Test {

	@Autowired
	private SysUserService sysUserDao;
	@org.junit.jupiter.api.Test
	public void test() {
		int rows = sysUserDao.isExists("username","abcd");
		JsonResult jsonResult = new JsonResult(rows);
		System.out.println(jsonResult);
	}
	
	
	
	
	
	
	
}
