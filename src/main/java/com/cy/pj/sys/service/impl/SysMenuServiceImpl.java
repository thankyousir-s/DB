package com.cy.pj.sys.service.impl;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.cy.pj.common.exception.ServiceException;
import com.cy.pj.common.util.Assert;
import com.cy.pj.common.vo.Node;
import com.cy.pj.sys.dao.SysMenuDao;
import com.cy.pj.sys.dao.SysRoleMenuDao;
import com.cy.pj.sys.entity.SysMenu;
import com.cy.pj.sys.service.SysMenuService;

@Service
public class SysMenuServiceImpl implements SysMenuService {

	@Autowired
	private SysMenuDao sysMenuDao;
	
	@Autowired
	private SysRoleMenuDao sysRoleMenuDao;
	@CacheEvict(allEntries =true,value = "menuCache")
	@Override
	public int updateObject(SysMenu entity) {
		//1.参数校验
		Assert.isNull(entity, "保存对象不能为空");
		Assert.isEmpty(entity.getName(), "菜单名不能为空");
		//2.持久化数据
		int rows=sysMenuDao.updateObject(entity);
		//3.返回 结果
		return rows;
	}
	@CacheEvict(allEntries =true,value = "menuCache")
	@Override
	public int saveObject(SysMenu entity) {
		//1.参数校验
	    Assert.isNull(entity, "保存对象不能为空");
	    Assert.isEmpty(entity.getName(), "菜单名不能为空");
		//2.持久化数据
	    int rows=sysMenuDao.insertObject(entity);
		//3.返回 结果
		return rows;
	}
	@CacheEvict(allEntries =true,value = "menuCache")
	@Override
	public List<Node> findZtreeMenuNodes() {
		return sysMenuDao.findZtreeMenuNodes();
	}
	@CacheEvict(allEntries =true,value = "menuCache")
	@Override
	public int deleteObject(Integer id) {
		//1.判定参数有效性
		Assert.isValid(id!=null&&id>1, "id值无效");
		//2.查询当前菜单是否有子菜单,并进行校验
		int childCount=sysMenuDao.getChildCount(id);
		if(childCount>0)
			throw new ServiceException("请先删除子菜单");
		//3.删除角色菜单关系数据
		sysRoleMenuDao.deleteObjectsByMenuId(id);
		//4.删除菜单自身数据
		int rows=sysMenuDao.deleteObject(id);
		if(rows==0)
			throw new ServiceException("记录已经不存在");
		//5.返回结果
		return rows;
	}
	
	@Cacheable(value="menuCache")
    @Override
    public List<Map<String, Object>> findObjects() {
    	// TODO Auto-generated method stub
    	return sysMenuDao.findObjects();
    }
}









