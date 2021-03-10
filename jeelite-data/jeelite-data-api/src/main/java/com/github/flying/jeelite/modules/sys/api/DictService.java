/**
 * Copyright &copy; 2017-2018 <a href="https://github.com/xusheng1987/jeelite">jeelite</a> All rights reserved.
 */
package com.github.flying.jeelite.modules.sys.api;

import java.util.List;

import com.github.flying.jeelite.common.persistence.CrudService;
import com.github.flying.jeelite.modules.sys.entity.Dict;

/**
 * 字典Service
 *
 * @author flying
 * @version 2014-05-16
 */
public interface DictService extends CrudService<Dict> {

	/**
	 * 查询字段类型列表
	 */
	List<String> findTypeList();
	
	List<Dict> findAllList();

	int batchDelete(List<String> idList);

}