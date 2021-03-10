/**
 * Copyright &copy; 2017-2018 <a href="https://github.com/xusheng1987/jeelite">jeelite</a> All rights reserved.
 */
package com.github.flying.jeelite.modules.sys.service;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

import com.github.flying.jeelite.common.service.TreeService;
import com.github.flying.jeelite.modules.sys.api.MenuService;
import com.github.flying.jeelite.modules.sys.dao.MenuDao;
import com.github.flying.jeelite.modules.sys.entity.Menu;

/**
 * 菜单管理
 *
 * @author flying
 * @version 2013-12-05
 */
@DubboService
@Transactional(readOnly = true)
public class MenuProvider extends TreeService<MenuDao, Menu> implements MenuService {

	@Override
	public List<Menu> findByUserId(Menu menu) {
		return dao.findByUserId(menu);
	}

	@Override
	public List<Menu> findAllList() {
		return dao.findAllList(new Menu());
	}

	@Override
	@Transactional(readOnly = false)
	public void updateMenuSort(Menu menu) {
		dao.updateSort(menu);
	}

}