/**
 * Copyright &copy; 2017-2018 <a href="https://github.com/xusheng1987/jeelite">jeelite</a> All rights reserved.
 */
package com.github.flying.jeelite.modules.sys.api;

import java.util.List;

import com.github.flying.jeelite.common.persistence.CrudTreeService;
import com.github.flying.jeelite.modules.sys.entity.Office;

/**
 * 机构Service
 * @author flying
 * @version 2014-05-16
 */
public interface OfficeService extends CrudTreeService<Office> {

	List<Office> findAllList();

}