package com.unisure.action;

import com.unisure.bean.User;
import easymapping.action.ProcessorAction;
import easymapping.core.Bean;
import easymapping.core.Messager;
import easymapping.util.btn.Button;

/**
 * 用于演示的用户类管理Action
 * 
 * @author Ready
 * @date 2012-6-14
 */
@SuppressWarnings("serial")
public class UserAction extends ProcessorAction {

	// private static final Log log = X.getLog();
	private Messager messager = new Messager();
	private User user;

	
	public User getUser() {
		System.out.println("diaoyong");
		return user;
	}

	
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * 显示用户列表
	 * 
	 * @return
	 */
	public String showList() {
		Bean bean = createBean();
		page.setSize(10);
		page = beanProcessor.getListAsPage("*", "USER", bean, page, null, true, "NAME_LK");
		return "showList";
	}

	/**
	 * 添加单个用户
	 * 
	 * @return
	 */
	public String addInfo() {
		return "to_showList";
	}

	/**
	 * 以Ajax方式显示用户列表
	 * @return
	 */
	public String ajaxShowList() {
		Bean bean = createBean();
		page.setSize(10);
		page = beanProcessor.getListAsPage("*", "USER", bean, page, null, true);
		page.output();
		return null;
	}

	/**
	 * 测试
	 * 
	 * @return
	 */
	public String test() {
		System.out.println(user);
		return null;
	}

	public Messager getEasyMessage() {
		return this.messager;
	}
}
