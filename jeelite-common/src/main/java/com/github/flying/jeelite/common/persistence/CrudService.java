package com.github.flying.jeelite.common.persistence;

import java.util.List;

public interface CrudService<T extends BaseEntity<T>> {

	/**
	 * 根据主键获取单表数据
	 */
	T getById(String id);

	/**
	 * 获取单条数据
	 */
	T get(String id);

	/**
	 * 查询列表数据
	 */
	List<T> findList(T entity);

	/**
	 * 查询分页数据
	 */
	Page<T> findPage(Page page, T entity);

	/**
	 * 保存数据
	 */
	int save(T entity);

	/**
	 * 删除数据（逻辑删除，更新del_flag字段为1）
	 */
	int delete(T entity);
}