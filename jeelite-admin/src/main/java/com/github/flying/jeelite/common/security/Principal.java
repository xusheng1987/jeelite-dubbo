package com.github.flying.jeelite.common.security;

import java.io.Serializable;

import com.github.flying.jeelite.modules.sys.entity.User;

/**
 * 授权用户信息
 */
public class Principal implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id; // 编号
	private String loginName; // 登录名
	private String name; // 姓名
	private String host; // 主机
	private String ipAddress; // IP对应的地址
	private String browser; // 浏览器类型
	private String os; // 操作系统

	public Principal(User user) {
		this.id = user.getId();
		this.loginName = user.getLoginName();
		this.name = user.getName();
	}

	public String getId() {
		return id;
	}

	public String getLoginName() {
		return loginName;
	}

	public String getName() {
		return name;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	@Override
	public String toString() {
		return id;
	}

}