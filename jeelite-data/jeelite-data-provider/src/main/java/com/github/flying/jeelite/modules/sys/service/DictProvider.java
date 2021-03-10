/**
 * Copyright &copy; 2017-2018 <a href="https://github.com/xusheng1987/jeelite">jeelite</a> All rights reserved.
 */
package com.github.flying.jeelite.modules.sys.service;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

import com.github.flying.jeelite.common.service.BaseService;
import com.github.flying.jeelite.modules.sys.api.DictService;
import com.github.flying.jeelite.modules.sys.dao.DictDao;
import com.github.flying.jeelite.modules.sys.entity.Dict;

/**
 * 字典Service
 *
 * @author flying
 * @version 2014-05-16
 */
@DubboService
@Transactional(readOnly = true)
public class DictProvider extends BaseService<DictDao, Dict> implements DictService {

	/**
	 * 查询字段类型列表
	 */
	@Override
	public List<String> findTypeList() {
		return dao.findTypeList(new Dict());
	}

	@Override
	public List<Dict> findAllList() {
		return dao.findAllList(new Dict());
	}

	@Override
	@Transactional(readOnly = false)
	public int batchDelete(List<String> idList) {
		int row = dao.deleteBatchIds(idList);
		return row;
	}

}