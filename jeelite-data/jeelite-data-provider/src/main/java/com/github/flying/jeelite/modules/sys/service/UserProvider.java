/**
 * Copyright &copy; 2017-2018 <a href="https://github.com/xusheng1987/jeelite">jeelite</a> All rights reserved.
 */
package com.github.flying.jeelite.modules.sys.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

import com.github.flying.jeelite.common.persistence.Page;
import com.github.flying.jeelite.common.rest.RestException;
import com.github.flying.jeelite.common.service.BaseService;
import com.github.flying.jeelite.common.utils.IdGen;
import com.github.flying.jeelite.common.utils.StringUtils;
import com.github.flying.jeelite.modules.sys.api.UserService;
import com.github.flying.jeelite.modules.sys.dao.UserDao;
import com.github.flying.jeelite.modules.sys.dao.UserTokenDao;
import com.github.flying.jeelite.modules.sys.entity.Office;
import com.github.flying.jeelite.modules.sys.entity.User;
import com.github.flying.jeelite.modules.sys.entity.UserToken;

/**
 * 用户管理
 * @author flying
 * @version 2013-12-05
 */
@DubboService
@Transactional(readOnly = true)
public class UserProvider extends BaseService<UserDao, User> implements UserService {

	@Autowired
	private UserDao userDao;
	@Autowired
	private UserTokenDao userTokenDao;

	/**
	 * 根据登录名获取用户
	 */
	@Override
	public User getUserByLoginName(String loginName) {
		return userDao.getByLoginName(new User(null, loginName));
	}

	@Override
	public Page<User> findUser(Page<User> page, User user) {
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		user.getSqlMap().put("dsf", UserService.dataScopeFilter(user.getCurrentUser(), "o", "a"));
		return super.findPage(page, user);
	}

	/**
	 * 无分页查询人员列表
	 */
	@Override
	public List<User> findUser(User user){
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		user.getSqlMap().put("dsf", UserService.dataScopeFilter(user.getCurrentUser(), "o", "a"));
		List<User> list = super.findList(user);
		return list;
	}

	/**
	 * 通过部门ID获取用户列表，仅返回用户id和name（树查询用户时用）
	 */
	@Override
	public List<User> findUserByOfficeId(String officeId) {
		User user = new User();
		user.setOffice(new Office(officeId));
		List<User> list = dao.findUserByOfficeId(user);
		return list;
	}

	@Override
	@Transactional(readOnly = false)
	public void saveUser(User user) {
		if (StringUtils.isBlank(user.getId())){
			dao.insert(user);
		}else{
			// 更新用户数据
			dao.updateById(user);
		}
		saveUserRole(user);
	}

	@Override
	@Transactional(readOnly = false)
	public void saveUserRole(User user) {
		// 更新用户与角色关联
		dao.deleteUserRole(user);
		if (user.getRoleList() != null && user.getRoleList().size() > 0){
			dao.insertUserRole(user);
		}else{
			throw new RestException(user.getLoginName() + "没有设置角色！");
		}
	}

	@Override
	@Transactional(readOnly = false)
	public void updateUserInfo(User user) {
		dao.updateUserInfo(user);
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteUser(User user) {
		super.delete(user);
		// 清除用户缓存
		//UserUtils.clearCache(user);
	}

	@Override
	@Transactional(readOnly = false)
	public void batchDelete(List<String> idList) {
		dao.deleteBatchIds(idList);
	}

	@Override
	@Transactional(readOnly = false)
	public void updatePasswordById(String id, String loginName, String newPassword) {
		User user = new User(id);
		user.setPassword(UserService.entryptPassword(newPassword));
		dao.updatePasswordById(user);
	}

	@Override
	@Transactional(readOnly = false)
	public void updateUserLoginInfo(User user) {
		// 保存上次登录信息
		user.setOldLoginIp(user.getLoginIp());
		user.setOldLoginDate(user.getLoginDate());
		// 更新本次登录信息
		user.setLoginDate(new Date());
		dao.updateLoginInfo(user);
	}

	/**
	 * 根据token查询
	 */
	@Override
	public UserToken getByToken(String token) {
		return userTokenDao.getByToken(token);
	}

	/**
	 * 保存token
	 */
	@Override
	@Transactional(readOnly = false)
	public UserToken createToken(String userId) {
		//当前时间
		Date nowDate = new Date();
		//过期时间
		Date expireDate = new Date(nowDate.getTime() + 12 * 3600 * 1000);//12小时后过期
		String token = IdGen.uuid();
		UserToken userToken = userTokenDao.getByUserId(userId);
		if (userToken == null) {
			userToken = new UserToken();
			userToken.setUserId(userId);
			userToken.setToken(token);
			userToken.setUpdateDate(nowDate);
			userToken.setExpireDate(expireDate);
			userTokenDao.insert(userToken);
		} else {
			userToken.setToken(token);
			userToken.setUpdateDate(nowDate);
			userToken.setExpireDate(expireDate);
			userTokenDao.updateById(userToken);
		}
		return userToken;
	}

	/**
	 * 过期token
	 */
	@Override
	@Transactional(readOnly = false)
	public int expireToken(String userId) {
		//当前时间
		Date nowDate = new Date();
		UserToken userToken = userTokenDao.getByUserId(userId);
		userToken.setUpdateDate(nowDate);
		userToken.setExpireDate(nowDate);
		return userTokenDao.updateById(userToken);
	}

}