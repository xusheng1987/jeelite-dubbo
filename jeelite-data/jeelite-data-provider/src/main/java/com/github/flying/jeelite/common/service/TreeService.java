/**
 * Copyright &copy; 2017-2018 <a href="https://github.com/xusheng1987/jeelite">jeelite</a> All rights reserved.
 */
package com.github.flying.jeelite.common.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.github.flying.jeelite.common.persistence.CrudTreeService;
import com.github.flying.jeelite.common.persistence.TreeDao;
import com.github.flying.jeelite.common.persistence.TreeEntity;
import com.github.flying.jeelite.common.rest.RestException;
import com.github.flying.jeelite.common.utils.Reflections;
import com.github.flying.jeelite.common.utils.StringUtils;

/**
 * Service基类
 *
 * @author flying
 * @version 2014-05-16
 */
@Transactional(readOnly = true)
public abstract class TreeService<M extends TreeDao<T>, T extends TreeEntity<T>> extends BaseService<M, T> implements CrudTreeService<T> {

	@Override
	@Transactional(readOnly = false)
	public int save(T entity) {
		Class<T> entityClass = Reflections.getClassGenricType(getClass(), 1);

		// 如果没有设置父节点，则代表为跟节点，有则获取父节点实体
		if (entity.getParent() == null || StringUtils.isBlank(entity.getParentId())
				|| "0".equals(entity.getParentId())) {
			entity.setParent(null);
		} else {
			entity.setParent(super.get(entity.getParentId()));
		}
		if (entity.getParent() == null) {
			T parentEntity = null;
			try {
				parentEntity = entityClass.getConstructor(String.class).newInstance("0");
			} catch (Exception e) {
				throw new RestException(e.getMessage());
			}
			entity.setParent(parentEntity);
			entity.getParent().setParentIds(StringUtils.EMPTY);
		}

		// 获取修改前的parentIds，用于更新子节点的parentIds
		String oldParentIds = entity.getParentIds();

		// 设置新的父节点串
		entity.setParentIds(entity.getParent().getParentIds() + entity.getParent().getId() + ",");

		// 保存或更新实体
		int row = super.save(entity);

		// 更新子节点 parentIds
		T o = null;
		try {
			o = entityClass.newInstance();
		} catch (Exception e) {
			throw new RestException(e.getMessage());
		}
		o.setParentIds("%," + entity.getId() + ",%");
		List<T> list = dao.findByParentIdsLike(o);
		for (T e : list) {
			if (e.getParentIds() != null && oldParentIds != null) {
				e.setParentIds(e.getParentIds().replace(oldParentIds, entity.getParentIds()));
				row += dao.updateParentIds(e);
			}
		}
		return row;
	}

	/**
	 * 删除数据
	 */
	@Override
	@Transactional(readOnly = false)
	public int delete(T entity) {
		return dao.delete(entity);
	}
}