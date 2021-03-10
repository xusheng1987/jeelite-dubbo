/**
 * Copyright &copy; 2017-2018 <a href="https://github.com/xusheng1987/jeelite">jeelite</a> All rights reserved.
 */
package com.github.flying.jeelite.modules.sys.api;

import java.util.List;

import com.github.flying.jeelite.common.persistence.CrudService;
import com.github.flying.jeelite.common.persistence.Page;
import com.github.flying.jeelite.common.security.Digests;
import com.github.flying.jeelite.common.utils.Encodes;
import com.github.flying.jeelite.common.utils.StringUtils;
import com.github.flying.jeelite.modules.sys.entity.Role;
import com.github.flying.jeelite.modules.sys.entity.User;
import com.github.flying.jeelite.modules.sys.entity.UserToken;
import com.google.common.collect.Lists;

/**
 * 用户管理
 * @author flying
 * @version 2013-12-05
 */
public interface UserService extends CrudService<User> {

	public static final int HASH_INTERATIONS = 1024;
	public static final int SALT_SIZE = 8;

	/**
	 * 根据登录名获取用户
	 */
	User getUserByLoginName(String loginName);

	Page<User> findUser(Page<User> page, User user);

	/**
	 * 无分页查询人员列表
	 */
	List<User> findUser(User user);

	/**
	 * 通过部门ID获取用户列表，仅返回用户id和name（树查询用户时用）
	 */
	List<User> findUserByOfficeId(String officeId);

	void saveUser(User user);

	void saveUserRole(User user);

	void updateUserInfo(User user);

	void deleteUser(User user);

	void batchDelete(List<String> idList);

	void updatePasswordById(String id, String loginName, String newPassword);

	void updateUserLoginInfo(User user);

	/**
	 * 生成安全的密码，生成随机的16位salt并经过1024次 sha-1 hash
	 */
	static String entryptPassword(String plainPassword) {
		String plain = Encodes.unescapeHtml(plainPassword);
		byte[] salt = Digests.generateSalt(SALT_SIZE);
		byte[] hashPassword = Digests.sha1(plain.getBytes(), salt, HASH_INTERATIONS);
		return Encodes.encodeHex(salt)+Encodes.encodeHex(hashPassword);
	}

	/**
	 * 验证密码
	 * @param plainPassword 明文密码
	 * @param password 密文密码
	 * @return 验证成功返回true
	 */
	static boolean validatePassword(String plainPassword, String password) {
		String plain = Encodes.unescapeHtml(plainPassword);
		byte[] salt = Encodes.decodeHex(password.substring(0,16));
		byte[] hashPassword = Digests.sha1(plain.getBytes(), salt, HASH_INTERATIONS);
		return password.equals(Encodes.encodeHex(salt)+Encodes.encodeHex(hashPassword));
	}

	/**
	 * 数据范围过滤
	 *
	 * @param user 当前用户对象，通过“UserUtils.getUser()”获取
	 * @param officeAlias 机构表别名
	 * @param userAlias 用户表别名，传递空，忽略此参数
	 * @return 需要拼接在原sql里的过滤条件sql片段
	 */
	static String dataScopeFilter(User user, String officeAlias, String userAlias) {
		StringBuilder sqlString = new StringBuilder();

		// 进行权限过滤，多个角色权限范围之间为或者关系。
		List<String> dataScope = Lists.newArrayList();

		// 超级管理员，跳过权限过滤
		if (!user.isAdmin()) {
			boolean isDataScopeAll = false;
			for (Role r : user.getRoleList()) {
				if (!dataScope.contains(r.getDataScope()) && StringUtils.isNotBlank(officeAlias)) {
					if (Role.DATA_SCOPE_ALL.equals(r.getDataScope())) {
						isDataScopeAll = true;
						break;
					} else if (Role.DATA_SCOPE_COMPANY_AND_CHILD.equals(r.getDataScope())) {
						sqlString.append(" OR " + officeAlias + ".id = '" + user.getCompany().getId() + "'");
						sqlString.append(" OR " + officeAlias + ".parent_ids LIKE '" + user.getCompany().getParentIds()
								+ user.getCompany().getId() + ",%'");
					} else if (Role.DATA_SCOPE_COMPANY.equals(r.getDataScope())) {
						sqlString.append(" OR " + officeAlias + ".id = '" + user.getCompany().getId() + "'");
						// 包括本公司下的部门 （type=1:公司；type=2：部门）
						sqlString.append(" OR (" + officeAlias + ".parent_id = '" + user.getCompany().getId() + "' AND " + officeAlias
								+ ".type = '2')");
					} else if (Role.DATA_SCOPE_OFFICE_AND_CHILD.equals(r.getDataScope())) {
						sqlString.append(" OR " + officeAlias + ".id = '" + user.getOffice().getId() + "'");
						sqlString.append(" OR " + officeAlias + ".parent_ids LIKE '" + user.getOffice().getParentIds()
								+ user.getOffice().getId() + ",%'");
					} else if (Role.DATA_SCOPE_OFFICE.equals(r.getDataScope())) {
						sqlString.append(" OR " + officeAlias + ".id = '" + user.getOffice().getId() + "'");
					} else if (Role.DATA_SCOPE_CUSTOM.equals(r.getDataScope())) {
						sqlString.append(
								" OR EXISTS (SELECT 1 FROM sys_role_office WHERE role_id = '" + r.getId() + "'");
						sqlString.append(" AND office_id = " + officeAlias + ".id)");
					}
					dataScope.add(r.getDataScope());
				}
			}
			// 如果没有全部数据权限，并设置了用户别名，则当前权限为本人
			if (!isDataScopeAll) {
				if (StringUtils.isNotBlank(userAlias)) {
					sqlString.append(" OR " + userAlias + ".id = '" + user.getId() + "'");
				}
			} else {
				// 如果包含全部权限，则去掉之前添加的所有条件，并跳出循环。
				sqlString = new StringBuilder();
			}
		}
		if (StringUtils.isNotBlank(sqlString.toString())) {
			return " AND (" + sqlString.substring(4) + ")";
		}
		return "";
	}

	/**
	 * 根据token查询
	 */
	UserToken getByToken(String token);

	/**
	 * 保存token
	 */
	UserToken createToken(String userId);

	/**
	 * 过期token
	 */
	int expireToken(String userId);

}