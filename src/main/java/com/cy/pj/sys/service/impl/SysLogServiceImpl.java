package com.cy.pj.sys.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.cy.pj.common.config.PageProperties;
import com.cy.pj.common.exception.ServiceException;
import com.cy.pj.common.util.Assert;
import com.cy.pj.common.vo.PageObject;
import com.cy.pj.sys.dao.SysLogDao;
import com.cy.pj.sys.entity.SysLog;
import com.cy.pj.sys.service.SysLogService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
/**
  *  日志模块业务层对象.
  *  思考:业务层对象要处理哪些业务呢?
 * 1)核心业务 (数据和业务的基本操作)
 * 2)拓展业务 (权限控制,缓存,异步,...)
 */
@Service
public class SysLogServiceImpl implements SysLogService {

	@Autowired
	private SysLogDao sysLogDao;
	@Autowired
	private PageProperties pageProperties;
	/*@Transactional 如果抛出异常并且数据已被删除,需要回滚,这个注解即为将事务交给spring管理,而不是myabtis管理,就会回滚*/
    //@Transactional
	@Override
	public int deleteObjects(Integer... ids) {
		//1.参数校验
		//if(ids==null||ids.length==0)
		//	throw new IllegalArgumentException("请先选择");
		Assert.isValid(ids!=null&&ids.length>0,"请先选择");
		//2.执行删除业务
		int rows=sysLogDao.deleteObjects(ids);
		if(rows==0)
			throw new ServiceException("记录可能已经不存在");
		return rows;
	}

	@Override
	public PageObject<SysLog> findPageObjects(
			String username, 
			Integer pageCurrent){
		//1.参数校验
        //PageUtil.isValid(pageCurrent);
		Assert.isValid(pageCurrent!=null&&pageCurrent>=1, "页码值无效");
		//2.查询总记录数并校验
		//int rowCount=sysLogDao.getRowCount(username);
		//if(rowCount==0)
			//throw new ServiceException("记录不存在");
		//3.查询当前页日志记录
		int pageSize=pageProperties.getPageSize();
	//	int startIndex=(pageCurrent-1)*pageSize;
		Page<SysLog> page= PageHelper.startPage(pageCurrent, pageSize);
		List<SysLog> records=sysLogDao.findPageObjects(username);
		Integer rowCount=(int)page.getTotal();
	//	System.out.println(rowCount);
		//4.封装结果并返回
//		PageObject<SysLog> po=new PageObject<>();
//		po.setRecords(records);
//		po.setRowCount(rowCount);
//		po.setPageSize(pageSize);
//		po.setPageCurrent(pageCurrent);
//		po.setPageCount((rowCount-1)/pageSize+1);
		//return po;
		return new PageObject<>(records,rowCount, pageCurrent, pageSize);
	}
	/*@Async  异步执行,即两个线程执行 */
    @Async
	@Override
	public void saveObject(SysLog entity) {
		sysLogDao.insertObject(entity);		
	}

}
