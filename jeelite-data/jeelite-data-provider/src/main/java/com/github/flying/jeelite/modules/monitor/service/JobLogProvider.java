/**
 * Copyright &copy; 2017-2018 <a href="https://github.com/xusheng1987/jeelite">jeelite</a> All rights reserved.
 */
package com.github.flying.jeelite.modules.monitor.service;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

import com.github.flying.jeelite.common.service.BaseService;
import com.github.flying.jeelite.modules.monitor.api.JobLogService;
import com.github.flying.jeelite.modules.monitor.dao.JobLogDao;
import com.github.flying.jeelite.modules.monitor.entity.JobLog;

/**
 * 定时任务日志Service
 *
 * @author flying
 * @version 2019-01-11
 */
@DubboService
@Transactional(readOnly = true)
public class JobLogProvider extends BaseService<JobLogDao, JobLog> implements JobLogService {

}