package com.cy.pj.sys.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cy.pj.common.anno.RequestLog;
import com.cy.pj.common.config.PageProperties;
import com.cy.pj.common.exception.ServiceException;
import com.cy.pj.common.util.Assert;
import com.cy.pj.common.util.ShiroUtil;
import com.cy.pj.common.vo.PageObject;
import com.cy.pj.sys.dao.SysUserDao;
import com.cy.pj.sys.dao.SysUserRoleDao;
import com.cy.pj.sys.entity.SysUser;
import com.cy.pj.sys.service.SysUserService;
import com.cy.pj.sys.vo.SysUserDeptVo;


@Service
public class SysUserServiceImpl implements SysUserService {

	@Autowired
	private SysUserDao sysUserDao;

	@Autowired
	private SysUserRoleDao sysUserRoleDao;

	@Autowired
	private PageProperties pageProperties;
	@RequestLog(name = "查询")
	@Override
	public Map<String, Object> findObjectById(Integer id) {
		//1.参数校验
		Assert.isValid(id!=null&&id>0, "id值无效");
		//2.查询用户以及用户对应的部门信息
		SysUserDeptVo user=sysUserDao.findObjectById(id);
		if(user==null)
			throw new ServiceException("用户不存在");
		//3.查询用户对应的角色信息
		List<Integer> roleIds=sysUserRoleDao.findRoleIdsByUserId(id);
		//4.封装结果并返回
		Map<String,Object> map=new HashMap<>();
		map.put("user", user);
		map.put("roleIds", roleIds);
		return map;
	}
	@RequestLog(name = "updateObject")
	@Override
	public int updateObject(SysUser entity, Integer[] roleIds) {
		//1.参数校验
		Assert.isNull(entity, "保存对象不能为空");
		Assert.isEmpty(entity.getUsername(), "用户名不能为空");
		Assert.isEmpty(roleIds, "必须为用户分配角色");
		//2.保存用户自身信息
		int rows=sysUserDao.updateObject(entity);
		//3.保存用户和角色关系数据
		sysUserRoleDao.deleteObjectsByUserId(entity.getId());
		sysUserRoleDao.insertObjects(entity.getId(), roleIds);
		//4.返回结果
		return rows;
	}
	@RequestLog(name = "saveObject")
	@Override
	public int saveObject(SysUser entity, Integer[] roleIds) {
		//1.参数校验
		Assert.isNull(entity, "保存对象不能为空");
		Assert.isEmpty(entity.getUsername(), "用户名不能为空");
		Assert.isEmpty(entity.getPassword(), "密码不能为空");
		Assert.isEmpty(roleIds, "必须为用户分配角色");
		//2.保存用户自身信息
		//2.1对密码进行加密
		String salt=UUID.randomUUID().toString();
		//String newPassword=
		//DigestUtils.md5DigestAsHex((entity.getPassword()+salt).getBytes());
		SimpleHash sh=new SimpleHash(
				"MD5",//algorithmName 算法名称
				entity.getPassword(),//source 未加密的密码
				salt,//盐值
				1);//hashIterations 加密次数
		String newPassword=sh.toHex();
		//2.2重新存储到entity对象
		entity.setSalt(salt);
		entity.setPassword(newPassword);
		//2.3持久化用户信息
		int rows=sysUserDao.insertObject(entity);
		//3.保存用户和角色关系数据
		sysUserRoleDao.insertObjects(entity.getId(), roleIds);
		//4.返回结果
		return rows;
	}
	@RequiresPermissions("sys:user:update")
	@RequestLog(name = "禁用启用")
	@Override
	public int validById(Integer id, Integer valid, String modifiedUser) {
		//1.参数校验
		Assert.isValid(id!=null&&id>0, "id值无效");
		Assert.isValid(valid!=null&&(valid==1||valid==0), "状态无效");
		//2.修改状态
		int rows=sysUserDao.validById(id, valid, modifiedUser);
		if(rows==0)
			throw new ServiceException("记录可能已经不存在");
		//3.返回结果
		return rows;
	}
	@RequestLog(name = "查询")
	@Override
	public PageObject<SysUserDeptVo> findPageObjects(String username, Integer pageCurrent) {
		//1.参数校验
		Assert.isValid(pageCurrent!=null&&pageCurrent>0, "当前页码值不正确");
		//2.查询总记录数并校验
		int rowCount=sysUserDao.getRowCount(username);
		if(rowCount==0)
			throw new ServiceException("没有对应记录"); 
		//3.查询当前页记录
		int pageSize=pageProperties.getPageSize();
		int startIndex=(pageCurrent-1)*pageSize;
		List<SysUserDeptVo> records=
				sysUserDao.findPageObjects(username, startIndex, pageSize);
		//4.封装查询结果
		return new PageObject<>(records, rowCount, pageCurrent, pageSize);
	}
	@RequestLog(name = "isExists")
	@Override
	public int isExists(String columnName, String columnValue) {
		return sysUserDao.isExists("sys_users",columnName, columnValue);
	}
	@RequestLog(name = "updatePassword")
	@Override
	public int updatePassword(String password, 
			String newPassword, String cfgPassword) {
		Assert.isEmpty(password, "原密码不能为空");
		Assert.isEmpty(newPassword, "新密码不能为空");
		Assert.isValid(newPassword.equals(cfgPassword), "两次密码输入不一致");
		SysUser user = ShiroUtil.getUser();
		String salt = user.getSalt();
		SimpleHash sh=new SimpleHash("MD5",password,salt,1);
		String hashedPwd = sh.toHex();
		Assert.isValid(user.getPassword().equals(hashedPwd),"原密码不正确");
		String newSalt = UUID.randomUUID().toString();
		String newHasherPwd=new SimpleHash("MD5",cfgPassword,newSalt,1).toHex();
		int rows=sysUserDao.updatePassword(newHasherPwd, newSalt, user.getId());
        if (rows==0) {
			throw new ServiceException("更新失败");
		}
		return rows;
	}


}
