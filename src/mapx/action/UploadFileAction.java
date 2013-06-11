package mapx.action;

import java.io.File;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 具有文件上传功能的Action父类
 * 注意： 由于实现文件上传，并不能减少代码量，并且有较大的局限性，因此文件上传的功能建议还是自己去写，此处不再做相应实现
 * @author Ready
 * @date 2012-4-27
 */
@SuppressWarnings("serial")

public abstract class UploadFileAction extends ActionSupport  {

	//上传多个文件的配置
	protected File[] files; //文件数组
	protected String[] filesFileName; //文件名数组
	protected String[] filesContentType;	//文件类型数组


	//上传单个文件的配置
	protected File file; //单个文件
	protected String fileFileName; //单个文件名
	protected String fileContentType;	//单个文件类型
	
	
}
