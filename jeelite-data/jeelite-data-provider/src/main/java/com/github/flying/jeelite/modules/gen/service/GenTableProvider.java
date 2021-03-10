/**
 * Copyright &copy; 2017-2018 <a href="https://github.com/xusheng1987/jeelite">jeelite</a> All rights reserved.
 */
package com.github.flying.jeelite.modules.gen.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

import com.github.flying.jeelite.common.service.BaseService;
import com.github.flying.jeelite.common.utils.StringUtils;
import com.github.flying.jeelite.modules.gen.api.GenTableService;
import com.github.flying.jeelite.modules.gen.dao.GenTableColumnDao;
import com.github.flying.jeelite.modules.gen.dao.GenTableDao;
import com.github.flying.jeelite.modules.gen.entity.GenTable;
import com.github.flying.jeelite.modules.gen.entity.GenTableColumn;
import com.github.flying.jeelite.modules.gen.util.GenUtils;

/**
 * 业务表Service
 *
 * @author flying
 * @version 2013-10-15
 */
@DubboService
@Transactional(readOnly = true)
public class GenTableProvider extends BaseService<GenTableDao, GenTable> implements GenTableService {

	@Autowired
	private GenTableColumnDao genTableColumnDao;

	@Override
	public GenTable get(String id) {
		GenTable genTable = dao.selectById(id);
		GenTableColumn genTableColumn = new GenTableColumn();
		genTableColumn.setGenTable(new GenTable(genTable.getId()));
		genTable.setColumnList(genTableColumnDao.findList(genTableColumn));
		return genTable;
	}

	@Override
	public List<GenTable> findAll() {
		return dao.findAllList(new GenTable());
	}

	/**
	 * 获取物理数据表列表
	 */
	@Override
	public List<GenTable> findTableListFormDb(GenTable genTable) {
		List<GenTable> list = dao.findTableList(genTable);
		return list;
	}

	/**
	 * 验证表名是否可用，如果已存在，则返回false
	 */
	@Override
	public boolean checkTableName(String tableName) {
		if (StringUtils.isBlank(tableName)) {
			return true;
		}
		GenTable genTable = new GenTable();
		genTable.setName(tableName);
		List<GenTable> list = super.findList(genTable);
		return list.size() == 0;
	}

	/**
	 * 获取物理数据表列表
	 */
	@Override
	public GenTable getTableFormDb(GenTable genTable) {
		// 如果有表名，则获取物理表
		if (StringUtils.isNotBlank(genTable.getName())) {

			List<GenTable> list = dao.findTableList(genTable);
			if (list.size() > 0) {

				// 如果是新增，初始化表属性
				if (StringUtils.isBlank(genTable.getId())) {
					genTable = list.get(0);
					// 设置字段说明
					if (StringUtils.isBlank(genTable.getComments())) {
						genTable.setComments(genTable.getName());
					}
					genTable.setClassName(StringUtils.toCapitalizeCamelCase(genTable.getName().substring(genTable.getName().indexOf("_") + 1)));
				}

				// 添加新列
				List<GenTableColumn> columnList = genTableColumnDao.findTableColumnList(genTable);
				for (GenTableColumn column : columnList) {
					boolean b = false;
					for (GenTableColumn e : genTable.getColumnList()) {
						if (e.getName().equals(column.getName())) {
							b = true;
						}
					}
					if (!b) {
						genTable.getColumnList().add(column);
					}
				}

				// 删除已删除的列
				for (GenTableColumn e : genTable.getColumnList()) {
					boolean b = false;
					for (GenTableColumn column : columnList) {
						if (column.getName().equals(e.getName())) {
							b = true;
						}
					}
					if (!b) {
						e.setDelFlag(GenTableColumn.DEL_FLAG_DELETE);
					}
				}

				// 获取主键
				genTable.setPkList(dao.findTablePK(genTable));

				// 初始化列属性字段
				GenUtils.initColumnField(genTable);

			}
		}
		return genTable;
	}

	@Override
	@Transactional(readOnly = false)
	public int save(GenTable genTable) {
		int row = super.save(genTable);
		// 保存列
		for (GenTableColumn column : genTable.getColumnList()) {
			column.setGenTable(genTable);
			column.setUpdateBy(genTable.getUpdateBy());
			column.setUpdateDate(genTable.getUpdateDate());
			if (StringUtils.isBlank(column.getId())) {
				column.setCreateBy(genTable.getCreateBy());
				column.setCreateDate(genTable.getCreateDate());
				row += genTableColumnDao.insert(column);
			} else {
				row += genTableColumnDao.updateById(column);
			}
		}
		return row;
	}

	@Override
	@Transactional(readOnly = false)
	public int delete(GenTable genTable) {
		int row = super.delete(genTable);
		row += genTableColumnDao.deleteByGenTableId(genTable.getId());
		return row;
	}

	@Override
	@Transactional(readOnly = false)
	public void batchDelete(String ids) {
		List<String> idList = Arrays.asList(ids.split(","));
		dao.deleteBatchIds(idList);
		for (String id:idList) {
			genTableColumnDao.deleteByGenTableId(id);
		}
	}

}