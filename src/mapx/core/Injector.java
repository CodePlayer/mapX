package mapx.core;

import mapx.jdbc.JdbcCommand;
import mapx.util.SpringUtil;

/**
 * 用于向指定对象添加并注入JdbcCommand属性的注射器类
 * @author Ready
 * @date 2012-10-22
 */
public class Injector {
	private static final JdbcCommand staticJdbcCommand = (JdbcCommand) SpringUtil.getBean("jdbcCommand");
	protected JdbcCommand jdbcCommand;

	/**
	 * 获取JdbcCommand
	 * @return
	 */
	public JdbcCommand getJdbcCommand() {
		return jdbcCommand;
	}

	/**
	 * 设置JdbcCommand
	 * @param jdbcCommand
	 */
	public void setJdbcCommand(JdbcCommand jdbcCommand) {
		this.jdbcCommand = jdbcCommand;
	}

	/**
	 * 快速注入jdbcCommand(使用注射器内部static缓存的JdbcCommand对象)
	 */
	public void fastInject() {
		jdbcCommand = staticJdbcCommand;
	}

	/**
	 * 即时注入jdbcCommand(即时调用SpringUtil.getBean("jdbcCommand"))
	 */
	public void inject() {
		jdbcCommand = (JdbcCommand) SpringUtil.getBean("jdbcCommand");
	}
}
