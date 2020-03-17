package com.cy.pj.sys.dao;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cy.pj.sys.entity.SysRole;
import com.cy.pj.sys.vo.SysRoleMenuVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class SysRoleDaoTests {
	  @Autowired
	  private SysRoleDao sysRoleDao;
	  
	  @Test
	  public void testFindObjectById() {
		 SysRoleMenuVo rm=sysRoleDao.findObjectById(1);
		 System.out.println(rm);
	  }
	  
	  @Test
	  public void testGetRowCount() {
		  int rowCount=sysRoleDao.getRowCount("运维");
		  log.info("查询到{}条记录",rowCount);//{}为日志的占位符
	  }
	  @Test
	  public void testFindPageObjects() {
		  List<SysRole> list=sysRoleDao.findPageObjects("运维",
				  0, 3);
		  for(SysRole r:list) {
			  log.info(r.toString());
		  }
		  //jdk8 lambda
		  list.forEach((r)->log.info(r.toString()));
	  }
}













