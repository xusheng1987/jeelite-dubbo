package com.github.flying.jeelite.common.persistence;

import java.util.ArrayList;
import java.util.List;

public interface CrudTreeService<T extends TreeEntity<T>> extends CrudService<T> {

	/**
	 * 构建树形结构list
	 */
	default List<T> buildTree(List<T> sourcelist) {
		List<T> resultList = new ArrayList<T>();
		for (T node : sourcelist) {
			if ("0".equals(node.getParentId())) {// 通过循环一级节点 就可以通过递归获取二级以下节点
				resultList.add(node);// 添加一级节点
				build(node, sourcelist, resultList);// 递归获取二级、三级、。。。节点
			}
		}
		return resultList;
	}

	/**
	 * 递归循环子节点
	 *
	 * @param node 当前节点
	 */
	default void build(T node, List<T> sourcelist, List<T> resultList) {
		List<T> children = getChildren(node, sourcelist);
		if (!children.isEmpty()) {// 如果存在子节点
			for (T child : children) {// 将子节点遍历加入返回值中
				resultList.add(child);
				build(child, sourcelist, resultList);
			}
		}
	}

	default List<T> getChildren(T node, List<T> sourcelist) {
		List<T> children = new ArrayList<T>();
		String id = node.getId();
		for (T child : sourcelist) {
			if (id.equals(child.getParentId())) {// 如果id等于父id
				children.add(child);// 将该节点加入循环列表中
			}
		}
		return children;
	}

}