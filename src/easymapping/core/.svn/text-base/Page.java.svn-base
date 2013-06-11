package easymapping.core;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import easymapping.util.JsonUtil;
import easymapping.util.StringUtil;
import easymapping.util.X;

/**
 * 分页引擎类，当使用数据分页功能时，将会返回此类<br>
 * 此类的属性大致如下：id=当前查询的页号，size=每页的显示记录数，count=总记录数，args=URL参数字符串,list=查询到的数据集合List(没有数据时，长度为0)
 * @author Ready
 * @date 2012-5-31
 */
public class Page<T> {

	private boolean isNeedCount = true; // 是否需要统计总记录数
	private int id; // 当前页数
	private int size; // 每页显示的记录数
	private int count; // 符合条件的总记录数
	private String orderBy; // 排序SQL语句
	private String args; // URL链接参数
	private List<T> list;

	/**
	 * 返回数据分页的当前分页页号，例如：查询第一页数据返回1，查询第二页数据返回2 <br>
	 * @return
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * 返回数据分页的每页显示条数
	 * @return
	 */
	public int getSize() {
		return this.size;
	}

	/**
	 * 返回数据分页的总记录数<br>
	 * 如果不需要返回总记录数，则默认返回0
	 * @return
	 */
	public int getCount() {
		return this.count;
	}

	/**
	 * 返回数据分页的分页数据集合<br>
	 * 没有数据时，返回空的集合(长度为0)
	 * @return
	 */
	public List<T> getList() {
		return this.list == null ? new ArrayList<T>(0) : this.list;
	}

	/**
	 * 设置当前要查询的数据页码，比如：要查询第二页的数据，请传入2 <br>
	 * 如果传入的值小于1，将会默认查询第一页
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * 设置每页显示条数，如果在查询前，被设置为大于0的值，将会开启使用数据分页功能<br>
	 * 如果页面请求中已经包含此参数值，则忽略代码中的设置
	 * @param size
	 */
	public void setSize(int size) {
		if (this.size <= 0) {
			this.size = size;
		}
	}

	public void setCount(int count) {
		this.count = count;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	/**
	 * 检测是否需要查询符合条件的总记录数
	 * @return
	 */
	public boolean isNeedCount() {
		return isNeedCount;
	}

	/**
	 * 获取排序部分的SQL语句
	 * @return
	 */
	public String getOrderBy() {
		return orderBy;
	}

	/**
	 * 设置排序SQL语句，例如：“USERID DESC, AGE ASC” 如果已经有排序语句，将会忽略后面设置的内容
	 * @param orderBy
	 */
	public void setOrderBy(String orderBy) {
		if (StringUtil.isEmpty(this.orderBy)) {
			this.orderBy = " ORDER BY " + orderBy;
		}
	}

	/**
	 * 返回分页链接的条件参数(不包括page.size、page.count、page.id等参数)
	 * @return
	 */
	public String getArgs() {
		return args;
	}

	/**
	 * 设置分页链接的条件参数(不包括page.size、page.count、page.id等参数)<br>
	 * 一般而言，如果没有需要特殊处理的字段，一般不需要用户设置，程序会自动将selectKeys中的每一个字段进行智能设置。 如果不是程序自动处理的列，才需要用户手动添加，而且只需要添加手动处理的那个列，其他交给程序自动处理的列仍然交给系统去处理
	 * @param args 例如：item.name=张&item.age=18
	 */
	public void setArgs(String args) {
		this.args = args;
	}

	/**
	 * 是否需要查询符合当前条件的总记录数，true=是，false=否<br>
	 * @param isNeedCount
	 */
	public void setNeedCount(boolean isNeedCount) {
		this.isNeedCount = isNeedCount;
	}

	/**
	 * 将Page封装为供JS调用的JSON格式的对象字符串<br>
	 * 字符串格式如下(如果没有数据，list将为空数组(length=0))：<br>
	 * {“id”:页码, “size”:每页条数, “count”:总记录数, “orderBy”:排序SQL, “args”:链接参数, “list”:[{Bean的JSON形式},{Bean的JSON形式}]} <br>
	 * 如果page.list数据为空，JSON对象的list得到的将会是一个空的数组。<br>
	 * 注意：本方法在处理java.util.Date类型时，将会直接调用toString()方法，如需转换，请先转换为相应字符串格式后再调用本方法<br>
	 * @return
	 */
	public String toString() {
		return JsonUtil.object2Json(this);
	}

	/**
	 * 使用当前的HttpServletResponse以JSON的形式输出分页对象<br>
	 * 内部会自行设置Content-Type为<code>text/html;charset=utf-8</code>
	 * @param keepOpenWriter 是否保持响应流的PrintWriter为打开状态<br>
	 * 如果是，这返回打开状态的相应流，如果否，则输出后直接关闭该响应流，并返回null
	 * @return
	 */
	public PrintWriter output(boolean keepOpenWriter) {
		PrintWriter out = X.getPrintWriter();
		out.write(this.toString());
		if (keepOpenWriter) {
			return out;
		}
		out.close();
		return null;
	}

	/**
	 * 使用当前的HttpServletResponse以JSON的形式输出分页对象<br>
	 * 内部会自行设置Content-Type为<code>text/html;charset=utf-8</code><br>
	 * 输出完后并自动关闭PrintWriter
	 */
	public void output() {
		output(false);
	}
}
