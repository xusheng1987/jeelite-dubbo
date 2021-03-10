/**
 * Copyright &copy; 2017-2018 <a href="https://github.com/xusheng1987/jeelite">jeelite</a> All rights reserved.
 */
package com.github.flying.jeelite.modules.gen.api;

import com.github.flying.jeelite.common.persistence.CrudService;
import com.github.flying.jeelite.modules.gen.entity.GenConfig;
import com.github.flying.jeelite.modules.gen.entity.GenScheme;

/**
 * 生成方案Service
 *
 * @author flying
 * @version 2013-10-15
 */
public interface GenSchemeService extends CrudService<GenScheme> {

	String saveScheme(GenScheme genScheme);

	GenConfig getGenConfig();
}