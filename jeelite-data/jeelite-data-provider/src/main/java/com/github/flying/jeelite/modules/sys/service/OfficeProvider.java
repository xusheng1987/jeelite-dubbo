/**
 * Copyright &copy; 2017-2018 <a href="https://github.com/xusheng1987/jeelite">jeelite</a> All rights reserved.
 */
package com.github.flying.jeelite.modules.sys.service;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

import com.github.flying.jeelite.common.service.TreeService;
import com.github.flying.jeelite.modules.sys.api.OfficeService;
import com.github.flying.jeelite.modules.sys.dao.OfficeDao;
import com.github.flying.jeelite.modules.sys.entity.Office;

/**
 * 机构Service
 * @author flying
 * @version 2014-05-16
 */
@DubboService
@Transactional(readOnly = true)
public class OfficeProvider extends TreeService<OfficeDao, Office> implements OfficeService {

	@Override
	public List<Office> findAllList() {
		return dao.findAllList(new Office());
	}

}