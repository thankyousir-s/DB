package com.cy.pj.sys.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface BaseDao {

	/*
	 * 判定此列的值是否存在
	 */
	@Select("select count(*) from ${table} where ${columnName}=#{columnValue}")
	int isExists(@Param("table")String table,
			@Param("columnName")String columnName,
			@Param("columnValue")String columnValue);
}
