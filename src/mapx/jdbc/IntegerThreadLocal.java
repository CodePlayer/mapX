package mapx.jdbc;

/**
 * 专门用于管理事务或共享连接Integer标记的ThreadLocal类<br />
 * 和ThreadLocal的主要区别是，此类在get()获取不到值时，返回0，而不是null
 * @author Ready
 * @date 2012-5-22
 */
public class IntegerThreadLocal extends ThreadLocal<Integer> {
	/**
	 * 重写的初始值方法，
	 */
	@Override
	protected Integer initialValue() {
		return 0;
	}
}
