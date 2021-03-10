/**
 * Copyright &copy; 2017-2018 <a href="https://github.com/xusheng1987/jeelite">jeelite</a> All rights reserved.
 */
package com.github.flying.jeelite.modules.gen.api;

import java.util.List;

import com.github.flying.jeelite.common.persistence.CrudService;
import com.github.flying.jeelite.modules.gen.entity.GenTable;

/**
 * 业务表Service
 *
 * @author flying
 * @version 2013-10-15
 */
public interface GenTableService extends CrudService<GenTable> {

	GenTable get(String id);

	List<GenTable> findAll();

	/**
	 * 获取物理数据表列表
	 */
	List<GenTable> findTableListFormDb(GenTable genTable);

	/**
	 * 验证表名是否可用，如果已存在，则返回false
	 */
	boolean checkTableName(String tableName);

	/**
	 * 获取物理数据表列表
	 */
	GenTable getTableFormDb(GenTable genTable);

	int save(GenTable genTable);

	int delete(GenTable genTable);

	void batchDelete(String ids);

}