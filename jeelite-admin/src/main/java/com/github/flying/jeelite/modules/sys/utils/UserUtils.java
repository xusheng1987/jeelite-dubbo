/**
 * Copyright &copy; 2017-2018 <a href="https://github.com/xusheng1987/jeelite">jeelite</a> All rights reserved.
 */
package com.github.flying.jeelite.modules.sys.utils;

import java.util.Date;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import com.github.flying.jeelite.common.persistence.DataEntity;
import com.github.flying.jeelite.common.security.Principal;
import com.github.flying.jeelite.common.utils.CacheUtils;
import com.github.flying.jeelite.common.utils.SpringContextHolder;
import com.github.flying.jeelite.common.utils.StringUtils;
import com.github.flying.jeelite.modules.sys.api.MenuService;
import com.github.flying.jeelite.modules.sys.api.OfficeService;
import com.github.flying.jeelite.modules.sys.api.RoleService;
import com.github.flying.jeelite.modules.sys.api.UserService;
import com.github.flying.jeelite.modules.sys.entity.Menu;
import com.github.flying.jeelite.modules.sys.entity.Office;
import com.github.flying.jeelite.modules.sys.entity.Role;
import com.github.flying.jeelite.modules.sys.entity.User;

/**
 * 用户工具类
 *
 * @author flying
 * @version 2013-12-05
 */
public class UserUtils {

	private static UserService userService = SpringContextHolder.getBean(UserService.class);
	private static RoleService roleService = SpringContextHolder.getBean(RoleService.class);
	private static MenuService menuService = SpringContextHolder.getBean(MenuService.class);
	private static OfficeService officeService = SpringContextHolder.getBean(OfficeService.class);

	public static final String USER_CACHE = "userCache";
	public static final String USER_CACHE_ID_ = "id_";
	public static final String USER_CACHE_LOGIN_NAME_ = "ln";
	public static final String USER_CACHE_LIST_BY_OFFICE_ID_ = "oid_";

	public static final String CACHE_AUTH_INFO = "authInfo";
	public static final String CACHE_ROLE_LIST = "roleList";
	public static final String CACHE_MENU_LIST = "menuList";
	public static final String CACHE_OFFICE_LIST = "officeList";
	public static final String CACHE_OFFICE_ALL_LIST = "officeAllList";

	/**
	 * 根据ID获取用户
	 */
	public static User get(String id) {
		User user = (User) CacheUtils.get(USER_CACHE, USER_CACHE_ID_ + id);
		if (user == null) {
			user = userService.get(id);
			if (user == null) {
				return null;
			}
			user.setRoleList(roleService.findList(new Role(user)));
			CacheUtils.put(USER_CACHE, USER_CACHE_ID_ + user.getId(), user);
			CacheUtils.put(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getLoginName(), user);
		}
		return user;
	}

	/**
	 * 根据登录名获取用户
	 */
	public static User getByLoginName(String loginName) {
		User user = (User) CacheUtils.get(USER_CACHE, USER_CACHE_LOGIN_NAME_ + loginName);
		if (user == null) {
			user = userService.getUserByLoginName(loginName);
			if (user == null) {
				return null;
			}
			user.setRoleList(roleService.findList(new Role(user)));
			CacheUtils.put(USER_CACHE, USER_CACHE_ID_ + user.getId(), user);
			CacheUtils.put(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getLoginName(), user);
		}
		return user;
	}

	/**
	 * 清除当前用户缓存
	 */
	public static void clearCache() {
		removeCache(CACHE_AUTH_INFO);
		removeCache(CACHE_ROLE_LIST);
		removeCache(CACHE_MENU_LIST);
		removeCache(CACHE_OFFICE_LIST);
		removeCache(CACHE_OFFICE_ALL_LIST);
		clearCache(getUser());
	}

	/**
	 * 清除指定用户缓存
	 */
	public static void clearCache(User user) {
		CacheUtils.remove(USER_CACHE, USER_CACHE_ID_ + user.getId());
		CacheUtils.remove(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getLoginName());
		CacheUtils.remove(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getOldLoginName());
		if (user.getOffice() != null && user.getOffice().getId() != null) {
			CacheUtils.remove(USER_CACHE, USER_CACHE_LIST_BY_OFFICE_ID_ + user.getOffice().getId());
		}
	}

	/**
	 * 获取当前用户
	 */
	public static User getUser() {
		Principal principal = getPrincipal();
		if (principal != null) {
			User user = get(principal.getId());
			if (user != null) {
				return user;
			}
			return new User();
		}
		// 如果没有登录，则返回实例化空的User对象。
		return new User();
	}

	/**
	 * 获取当前用户角色列表
	 */
	public static List<Role> getRoleList() {
		List<Role> roleList = (List<Role>) getCache(CACHE_ROLE_LIST);
		if (roleList == null) {
			User user = getUser();
			if (user.isAdmin()) {
				roleList = roleService.findAllRole();
			} else {
				Role role = new Role();
				role.getSqlMap().put("dsf", UserService.dataScopeFilter(getUser(), "o", "u"));
				roleList = roleService.findList(role);
			}
			putCache(CACHE_ROLE_LIST, roleList);
		}
		return roleList;
	}

	/**
	 * 获取当前用户授权菜单
	 */
	public static List<Menu> getMenuList() {
		List<Menu> menuList = (List<Menu>) getCache(CACHE_MENU_LIST);
		if (menuList == null) {
			User user = getUser();
			if (user.isAdmin()) {
				menuList = menuService.findAllList();
			} else {
				Menu m = new Menu();
				m.setUserId(user.getId());
				menuList = menuService.findByUserId(m);
			}
			putCache(CACHE_MENU_LIST, menuList);
		}
		return menuList;
	}

	/**
	 * 获取当前用户有权限访问的部门
	 */
	public static List<Office> getOfficeList() {
		List<Office> officeList = (List<Office>) getCache(CACHE_OFFICE_LIST);
		if (officeList == null) {
			User user = getUser();
			if (user.isAdmin()) {
				officeList = officeService.findAllList();
			} else {
				Office office = new Office();
				office.getSqlMap().put("dsf", UserService.dataScopeFilter(user, "a", ""));
				officeList = officeService.findList(office);
			}
			putCache(CACHE_OFFICE_LIST, officeList);
		}
		return officeList;
	}

	/**
	 * 获取当前用户有权限访问的部门
	 */
	public static List<Office> getOfficeAllList() {
		List<Office> officeList = (List<Office>) getCache(CACHE_OFFICE_ALL_LIST);
		if (officeList == null) {
			officeList = officeService.findAllList();
		}
		return officeList;
	}

	/**
	 * 通过部门ID获取用户列表，仅返回用户id和name（树查询用户时用）
	 */
	public static List<User> findUserByOfficeId(String officeId) {
		List<User> list = (List<User>)CacheUtils.get(USER_CACHE, USER_CACHE_LIST_BY_OFFICE_ID_ + officeId);
		if (list == null){
			list = userService.findUserByOfficeId(officeId);
			CacheUtils.put(USER_CACHE, USER_CACHE_LIST_BY_OFFICE_ID_ + officeId, list);
		}
		return list;
	}

	/**
	 * 插入或更新数据之前执行的方法
	 */
	public static void preSave(DataEntity e) {
		User user = UserUtils.getUser();
		Date date = new Date();
		e.setUpdateBy(user.getId());
		e.setUpdateDate(date);
		if (StringUtils.isEmpty(e.getId())) {
			e.setCreateBy(user.getId());
			e.setCreateDate(date);
		}
	}

	/**
	 * 获取授权主要对象
	 */
	public static Subject getSubject() {
		return SecurityUtils.getSubject();
	}

	/**
	 * 获取当前登录者对象
	 */
	public static Principal getPrincipal() {
		try {
			Subject subject = SecurityUtils.getSubject();
			Principal principal = (Principal) subject.getPrincipal();
			if (principal != null) {
				return principal;
			}
		} catch (UnavailableSecurityManagerException e) {

		} catch (InvalidSessionException e) {

		}
		return null;
	}

	public static Session getSession() {
		try {
			Subject subject = SecurityUtils.getSubject();
			Session session = subject.getSession(false);
			if (session == null) {
				session = subject.getSession();
			}
			if (session != null) {
				return session;
			}
		} catch (InvalidSessionException e) {

		}
		return null;
	}

	// ============== User Cache ==============

	public static Object getCache(String key) {
		return getCache(key, null);
	}

	public static Object getCache(String key, Object defaultValue) {
		Object obj = getSession().getAttribute(key);
		return obj == null ? defaultValue : obj;
	}

	public static void putCache(String key, Object value) {
		getSession().setAttribute(key, value);
	}

	public static void removeCache(String key) {
		getSession().removeAttribute(key);
	}

}