package com.cy.pj.sys.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cy.pj.common.util.ShiroUtil;
import com.cy.pj.common.vo.JsonResult;
import com.cy.pj.sys.entity.SysUser;
import com.cy.pj.sys.service.SysUserService;

@RestController
@RequestMapping("/user/")
public class SysUserController {

	@Autowired
	private SysUserService sysUserService;

	@RequestMapping("doGetUser")
	public JsonResult doGetUser() {

		return new JsonResult(ShiroUtil.getUser());
	}

	@RequestMapping("doLogin")
	public JsonResult doLogin(String username, String password,boolean isRememberMe){
		//1.获取Subject对象
		Subject subject=SecurityUtils.getSubject();
		//2.通过Subject提交用户信息,交给shiro框架进行认证操作
		//2.1对用户进行封装
		UsernamePasswordToken token=new UsernamePasswordToken(username,password);//凭证信息,身份信息	
		if(isRememberMe) {
			token.setRememberMe(true); 
		}
		//2.2对用户信息进行身份认证
		subject.login(token);
		//分析:
		//1)token会传给shiro的SecurityManager
		//2)SecurityManager将token传递给认证管理器
		//3)认证管理器会将token传递给realm
		return new JsonResult("login ok");
	}


	@RequestMapping("doFindObjectById")
	public JsonResult doFindObjectById(Integer id) {
		return new JsonResult(sysUserService.findObjectById(id));
	}

	@RequestMapping("doUpdateObject")
	public JsonResult doUpdateObject(SysUser entity,
			Integer[] roleIds) {
		sysUserService.updateObject(entity, roleIds);
		return new JsonResult("update ok");
	}
	@RequestMapping("doSaveObject")
	public JsonResult doSaveObject(SysUser entity,
			Integer[] roleIds) {
		sysUserService.saveObject(entity, roleIds);
		return new JsonResult("save ok");
	}

	@RequestMapping("doValidById")
	public JsonResult doValidById(Integer id,Integer valid) {
		sysUserService.validById(id, valid, ShiroUtil.Username());
		return new JsonResult("update ok");
	}
	@RequestMapping("doFindPageObjects")
	public JsonResult doFindPageObjects(String username,Integer pageCurrent) {
		return new JsonResult(sysUserService.findPageObjects(username, pageCurrent));
	}
	@RequestMapping("isExists")
	public JsonResult isExists(String columnName, String columnValue) {
		int rows = sysUserService.isExists(columnName, columnValue);
		return new JsonResult(rows);
	}
	@RequestMapping("doUpdatePassword")
	public JsonResult doUpdatePassword(String pwd,
			String newPwd,String cfgPwd) {
		sysUserService.updatePassword(pwd,newPwd, cfgPwd);
		return new JsonResult("update OK");
	}


}







