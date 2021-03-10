/**
 * Copyright &copy; 2017-2018 <a href="https://github.com/xusheng1987/jeelite">jeelite</a> All rights reserved.
 */
package com.github.flying.jeelite.modules.sys.api;

import java.util.List;

import com.github.flying.jeelite.common.persistence.CrudTreeService;
import com.github.flying.jeelite.modules.sys.entity.Menu;

/**
 * 菜单管理
 *
 * @author flying
 * @version 2013-12-05
 */
public interface MenuService extends CrudTreeService<Menu> {

	/**
	 * 获取当前用户授权菜单
	 */
	List<Menu> findByUserId(Menu menu);

	/**
	 * 获取系统所有授权菜单
	 */
	List<Menu> findAllList();

	void updateMenuSort(Menu menu);

}