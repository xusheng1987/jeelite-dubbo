/**
 * Copyright &copy; 2017-2018 <a href="https://github.com/xusheng1987/jeelite">jeelite</a> All rights reserved.
 */
package com.github.flying.jeelite.modules.sys.api;

import java.util.List;

import com.github.flying.jeelite.common.persistence.CrudService;
import com.github.flying.jeelite.modules.sys.entity.Role;
import com.github.flying.jeelite.modules.sys.entity.User;

/**
 * 角色管理
 *
 * @author flying
 * @version 2013-12-05
 */
public interface RoleService extends CrudService<Role> {

	Role getRoleByName(String name);

	List<Role> findAllRole();

	void saveRole(Role role);

	Boolean outUserInRole(Role role, User user);

	User assignUserToRole(Role role, User user);

}