package com.unisure.action.common;

import java.io.InputStream;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 具有文件下载功能的Action父类
 * @author Ready
 * @date 2012-4-27
 */
@SuppressWarnings("serial")
public abstract class DownloadFileAction extends ActionSupport {

	/**
	 * 文件下载方法，此方法由Struts调用
	 * @return
	 */
	public final String download() {
		return "global_download";
	}

	/**
	 * 返回需要下载的文件的输入流，由开发人员实现
	 * @return
	 */
	public abstract InputStream getInputStream();

	/**
	 * 返回需要下载的文件名称，由开发人员实现
	 * @return
	 */
	public abstract String getDownloadFileName();

}
