package com.cy.pj.sys.service;

import com.cy.pj.common.vo.PageObject;
import com.cy.pj.sys.entity.SysLog;

public interface SysLogService {

	void saveObject(SysLog entity); 
	
	int deleteObjects(Integer... ids);
	
	PageObject<SysLog> findPageObjects(String username,
			Integer pageCurrent);
}
