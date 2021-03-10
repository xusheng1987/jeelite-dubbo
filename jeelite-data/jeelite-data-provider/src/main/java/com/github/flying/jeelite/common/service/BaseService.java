/**
 * Copyright &copy; 2017-2018 <a href="https://github.com/xusheng1987/jeelite">jeelite</a> All rights reserved.
 */
package com.github.flying.jeelite.common.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.github.flying.jeelite.common.persistence.Page;
import com.github.flying.jeelite.common.persistence.BaseEntity;
import com.github.flying.jeelite.common.persistence.CrudDao;
import com.github.flying.jeelite.common.persistence.CrudService;
import com.github.flying.jeelite.common.persistence.DataEntity;
import com.github.flying.jeelite.common.utils.StringUtils;

/**
 * Service基类
 *
 * @author flying
 * @version 2014-05-16
 */
@Transactional(readOnly = true)
public abstract class BaseService<M extends CrudDao<T>, T extends BaseEntity<T>> implements CrudService<T> {

	/**
	 * 持久层对象
	 */
	@Autowired
	protected M dao;

	/**
	 * 日志对象
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 根据主键获取单表数据
	 */
	@Override
	public T getById(String id) {
		return dao.selectById(id);
	}

	/**
	 * 获取单条数据
	 */
	@Override
	public T get(String id) {
		return dao.get(id);
	}

	/**
	 * 查询列表数据
	 */
	@Override
	public List<T> findList(T entity) {
		return dao.findList(entity);
	}

	/**
	 * 查询分页数据
	 */
	@Override
	public Page<T> findPage(Page page, T entity) {
		return dao.findList(page, entity);
	}

	/**
	 * 保存数据
	 */
	@Override
	@Transactional(readOnly = false)
	public int save(T entity) {
		if (StringUtils.isEmpty(entity.getId())) {
			return dao.insert(entity);
		} else {
			return dao.updateById(entity);
		}
	}

	/**
	 * 删除数据（逻辑删除，更新del_flag字段为1）
	 */
	@Override
	@Transactional(readOnly = false)
	public int delete(T entity) {
		return dao.deleteById(entity.getId());// 使用mybatis-plus自带的逻辑删除
	}

}