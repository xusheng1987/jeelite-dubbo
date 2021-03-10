/**
 * Copyright &copy; 2017-2018 <a href="https://github.com/xusheng1987/jeelite">jeelite</a> All rights reserved.
 */
package com.github.flying.jeelite.modules.sys.service;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

import com.github.flying.jeelite.common.service.BaseService;
import com.github.flying.jeelite.modules.sys.api.RoleService;
import com.github.flying.jeelite.modules.sys.api.UserService;
import com.github.flying.jeelite.modules.sys.dao.RoleDao;
import com.github.flying.jeelite.modules.sys.entity.Role;
import com.github.flying.jeelite.modules.sys.entity.User;

/**
 * 角色管理
 *
 * @author flying
 * @version 2013-12-05
 */
@DubboService
@Transactional(readOnly = true)
public class RoleProvider extends BaseService<RoleDao, Role> implements RoleService {

	@DubboReference
	private UserService userService;

	@Override
	public Role getRoleByName(String name) {
		Role r = new Role();
		r.setName(name);
		return dao.getByName(r);
	}

	@Override
	public List<Role> findAllRole() {
		return dao.findAllList(new Role());
	}

	@Override
	@Transactional(readOnly = false)
	public void saveRole(Role role) {
		super.save(role);
		// 更新角色与菜单关联
		dao.deleteRoleMenu(role);
		if (role.getMenuList().size() > 0) {
			dao.insertRoleMenu(role);
		}
		// 更新角色与部门关联
		dao.deleteRoleOffice(role);
		if (role.getOfficeList().size() > 0) {
			dao.insertRoleOffice(role);
		}
	}

	@Override
	@Transactional(readOnly = false)
	public Boolean outUserInRole(Role role, User user) {
		List<Role> roles = user.getRoleList();
		for (Role e : roles) {
			if (e.getId().equals(role.getId())) {
				roles.remove(e);
				userService.saveUserRole(user);
				return true;
			}
		}
		return false;
	}

	@Override
	@Transactional(readOnly = false)
	public User assignUserToRole(Role role, User user) {
		if (user == null) {
			return null;
		}
		List<String> roleIds = user.getRoleIdList();
		if (roleIds.contains(role.getId())) {
			return null;
		}
		user.getRoleList().add(role);
		userService.saveUserRole(user);
		return user;
	}

}