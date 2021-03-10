/**
 * Copyright &copy; 2017-2018 <a href="https://github.com/xusheng1987/jeelite">jeelite</a> All rights reserved.
 */
package com.github.flying.jeelite.modules.monitor.api;

import java.util.List;

import com.github.flying.jeelite.common.persistence.CrudService;
import com.github.flying.jeelite.common.persistence.Page;
import com.github.flying.jeelite.modules.monitor.entity.Job;

/**
 * 定时任务Service
 *
 * @author flying
 * @version 2019-01-11
 */
public interface JobService extends CrudService<Job> {

	/**
	 * 查询定时任务
	 */
	Page<Job> findPage(Page page, Job job);

	/**
	 * 保存定时任务
	 */
	int save(Job job);

	/**
	 * 更新定时任务
	 */
	void update(Job job);

	/**
	 * 删除定时任务
	 */
	void deleteBatch(List<String> jobIds);

	/**
	 * 执行定时任务
	 */
	void run(String jobId);

	/**
	 * 暂停定时任务
	 */
	void pause(List<String> jobIds);

	/**
	 * 恢复定时任务
	 */
	void resume(List<String> jobIds);
	
	/**
	 * 校验Cron表达式的有效性
	 */
	boolean checkCronExpressionIsValid(String cronExpression);

}