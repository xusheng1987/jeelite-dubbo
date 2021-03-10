/**
 * Copyright &copy; 2017-2018 <a href="https://github.com/xusheng1987/jeelite">jeelite</a> All rights reserved.
 */
package com.github.flying.jeelite.modules.test.service;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

import com.github.flying.jeelite.common.service.BaseService;
import com.github.flying.jeelite.modules.test.api.TestDataService;
import com.github.flying.jeelite.modules.test.dao.TestDataDao;
import com.github.flying.jeelite.modules.test.entity.TestData;

/**
 * 单表生成Service
 *
 * @author flying
 * @version 2015-04-06
 */
@DubboService
@Transactional(readOnly = true)
public class TestDataProvider extends BaseService<TestDataDao, TestData> implements TestDataService {

}