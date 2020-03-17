package com.cy.pj.sys.service;

import java.util.Map;
import com.cy.pj.common.vo.PageObject;
import com.cy.pj.sys.entity.SysUser;
import com.cy.pj.sys.vo.SysUserDeptVo;

public interface SysUserService {
	
	/*
	 * 判定此列的值是否存在
	 */
	int isExists(String columnName,String columnValue);
	/**
	 * 基于用户id获取用户以及用户对象的关系数据
	 * @param id
	 * @return
	 */
	Map<String,Object> findObjectById(Integer id);
	
	int validById(Integer id,Integer valid,String modifiedUser);
	/**
	 * 	保存用户以及用户对应的角色信息
	 * @param entity
	 * @param roleIds
	 * @return
	 */
	int updateObject(SysUser entity,Integer[]roleIds);
	/**
	 * 	保存用户以及用户对应的角色信息
	 * @param entity
	 * @param roleIds
	 * @return
	 */
	int saveObject(SysUser entity,Integer[]roleIds);

	 PageObject<SysUserDeptVo> findPageObjects(
			 String username,Integer pageCurrent);
	 /*
	  * 修改登录用户密码
	  */
   int updatePassword(String password,
		   String newPassword,String cfgPassword);
	 
}
