package com.cy.test.insert;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cy.pj.sys.dao.SysUserDao;
import com.cy.pj.sys.entity.SysLog;
import com.cy.pj.sys.entity.SysUser;
import com.cy.pj.sys.service.SysLogService;
import com.cy.pj.sys.service.SysUserService;

@SpringBootTest
public class Test {
    @Autowired
	private SysLogService sys;
    
    @Autowired
    private SysUserDao sysuser;
	
    @org.junit.jupiter.api.Test
    public void test() {
    	long t=System.currentTimeMillis();
    	long t1=System.currentTimeMillis();
    	long t2=t1-t;
    	SysLog log=new SysLog();
    	log.setTime(t2);
    	log.setUsername("admin");
		log.setParams("params");
		log.setOperation("operation");
		log.setMethod("name+methodName");
		log.setIp("192.168.1.1");
		log.setCreatedTime(new Date());
    	sys.saveObject(log);
    }
    @org.junit.jupiter.api.Test
    public void test1() {
    	SysUser as = sysuser.findUserByUserName("admin");
    	System.out.println(as);
    }
    
}
