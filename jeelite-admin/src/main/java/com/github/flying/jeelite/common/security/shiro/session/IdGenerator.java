/**
 * Copyright &copy; 2017-2018 <a href="https://github.com/xusheng1987/jeelite">jeelite</a> All rights reserved.
 */
package com.github.flying.jeelite.common.security.shiro.session;

import java.io.Serializable;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;

import com.github.flying.jeelite.common.utils.IdGen;

/**
 * 自定义生成会话ID
 *
 * @author flying
 * @version 2021-2-19
 */
public class IdGenerator implements SessionIdGenerator {

	@Override
	public Serializable generateId(Session session) {
		return IdGen.uuid();
	}

}