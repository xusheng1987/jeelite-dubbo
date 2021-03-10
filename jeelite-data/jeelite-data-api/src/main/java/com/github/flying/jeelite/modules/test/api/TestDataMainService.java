/**
 * Copyright &copy; 2017-2018 <a href="https://github.com/xusheng1987/jeelite">jeelite</a> All rights reserved.
 */
package com.github.flying.jeelite.modules.test.api;

import com.github.flying.jeelite.common.persistence.CrudService;
import com.github.flying.jeelite.modules.test.entity.TestDataMain;

/**
 * 主子表生成Service
 *
 * @author flying
 * @version 2015-04-06
 */
public interface TestDataMainService extends CrudService<TestDataMain> {

	TestDataMain get(String id);

	int save(TestDataMain testDataMain);

	int delete(TestDataMain testDataMain);

}