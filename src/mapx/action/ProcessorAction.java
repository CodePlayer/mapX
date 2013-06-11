package mapx.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import mapx.core.Bean;
import mapx.core.Messager;
import mapx.core.Page;
import mapx.jdbc.BeanProcessor;
import org.apache.struts2.interceptor.ServletRequestAware;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 对单表业务操作进行深度封装处理的抽象父类Action<br>
 * 注意：表单参数的名称区分大小写，大小写不一致无法得到正确的结果 <br>
 * 此类在调用前必须注入beanProcessor属性值(一般通过Spring进行依赖注入)<br>
 * 相关方法简介(详情查看对应方法的文档注释)：<br>
 * 深度封装的增删改查SQL操作方法：addItem、deleteItem、updateItem、queryItems(含数据分页功能)<br>
 * 自定义的增删改查SQL操作方法：customUpdate、customQuery <br>
 * SQL操作其他相关方法：开启缓存：setCache ; 追加WHERE条件SQL：appendCondition ;
 * 添加排序语句：appendOrderBy ; 设置列名大小写：setWordCase <br>
 * @author Ready
 * @date 2012-05-09
 */
public abstract class ProcessorAction extends ActionSupport implements ServletRequestAware {

	private static final long serialVersionUID = 1L;
	/**
	 * Messager图标常量：不使用图标的样式名称
	 */
	public static final String OFF = Messager.OFF;
	/**
	 * Messager图标常量：显示错误图标[X]
	 */
	public static final String ERROR = Messager.ERROR;
	/**
	 * Messager图标常量：显示警告图标[！]
	 */
	public static final String WARN = Messager.WARN;
	/**
	 * Messager图标常量：显示信息图标[√]
	 */
	public static final String INFO = Messager.INFO;
	/**
	 * 当前的HttpServletRequest对象
	 */
	protected HttpServletRequest request;
	/**
	 * Bean数据处理器
	 */
	@Resource
	protected BeanProcessor beanProcessor;
	/**
	 * 用于封装分页数据的对象实例
	 */
	protected Page<Bean> page = new Page<Bean>();

	/**
	 * 设置HttpServletRequest<br>
	 * <b>注意：</b>此方法供Struts2调用，用于注入当前的HttpServletRequest对象
	 */
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	};

	/**
	 * 注入processorService属性值，请使用Spring在Action中进行依赖注入
	 * @param processorService
	 */
	public void setBeanProcessor(BeanProcessor beanProcessor) {
		this.beanProcessor = beanProcessor;
	}

	/**
	 * 创建新的Bean对象，并将当前request中指定前缀的参数放入Bean中<br>
	 * <b>注意：</b>放入Bean中的参数名已经去掉对应的前缀，例如参数“e.name”将变为“name”
	 * @param prefix 指定的参数名前缀，如果没有，可以为null
	 * @return
	 */
	protected Bean createBean(String prefix) {
		return new Bean(request, prefix);
	}

	/**
	 * 创建新的Bean对象，并将当前request中所有前缀为"e."的参数放入Bean中<br>
	 * <b>注意：</b>放入Bean中的参数名已经去掉前缀，例如参数“e.name”将变为“name”
	 * @return
	 */
	protected Bean createBean() {
		return new Bean(request, "e.");
	}

	/**
	 * 获取Page对象<br>
	 * <b>注意：</b>此方法提供给Struts2调用
	 * @return
	 */
	public Page<Bean> getPage() {
		return page;
	}

	/**
	 * 设置Page对象
	 * <b>注意：</b>此方法提供给Struts2调用
	 * @param page
	 */
	public void setPage(Page<Bean> page) {
		this.page = page;
	}
}
