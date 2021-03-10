/**
 * Copyright &copy; 2017-2018 <a href="https://github.com/xusheng1987/jeelite">jeelite</a> All rights reserved.
 */
package com.github.flying.jeelite.modules.test.api;

import java.util.List;

import com.github.flying.jeelite.common.persistence.CrudTreeService;
import com.github.flying.jeelite.modules.test.entity.TestTree;

/**
 * 树结构生成Service
 *
 * @author flying
 * @version 2015-04-06
 */
public interface TestTreeService extends CrudTreeService<TestTree> {

	public List<TestTree> findAll();

}