package com.unisure.action.common;

import java.io.File;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 具有文件上传功能的Action父类
 * @author Ready
 * @date 2012-4-27
 */
@SuppressWarnings("serial")
public abstract class UploadFileAction extends ActionSupport  {

	private String fileFileName; //文件名
	private File file; //文件

	/**
	 * 文件上传，由Struts调用
	 * @return
	 */
	public final String upload() {
		return processFile(this.file, this.fileFileName);
	}

	/**
	 * 处理文件，将上传的文件交给开发人员处理<br />
	 * 返回的字符串为struts.xml配置中result的名称
	 * @param file
	 * @param fileName
	 * @return
	 */
	public abstract String processFile(File file, String fileName);

	/**
	 * 此方法由Struts调用
	 * @param fileFileName
	 */
	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}

	/**
	 * 此方法由Struts调用
	 * @param file
	 */
	public void setFile(File file) {
		this.file = file;
	}

}
