/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/xusheng1987/jeelite">jeelite</a> All rights reserved.
 */
package com.github.flying.jeelite.modules.sys.api;

import com.github.flying.jeelite.common.persistence.CrudService;
import com.github.flying.jeelite.common.persistence.Page;
import com.github.flying.jeelite.modules.sys.entity.Log;

/**
 * 日志Service
 *
 * @author flying
 * @version 2014-05-16
 */
public interface LogService extends CrudService<Log> {

	Page<Log> findPage(Page<Log> page, Log log);

}