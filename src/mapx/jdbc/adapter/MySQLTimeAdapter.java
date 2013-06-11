package mapx.jdbc.adapter;

import mapx.jdbc.JdbcCommand;

/**
 * MySQL时间适配器，用于获取MySQL数据库的当前系统时间
 * @author Ready
 * @date 2012-6-6
 */
public class MySQLTimeAdapter implements TimeAdapter {
	private JdbcCommand jdbcCommand;

	public JdbcCommand getJdbcCommand() {
		return jdbcCommand;
	}

	public void setJdbcCommand(JdbcCommand jdbcCommand) {
		this.jdbcCommand = jdbcCommand;
	}

	public String currentDate() {
		return jdbcCommand.queryForObject("SELECT DATE_FORMAT(NOW(),'%Y-%m-%d')", String.class);
	}

	public String currentDatetime() {
		return jdbcCommand.queryForObject("SELECT NOW()", String.class);
	}

	public String currentTime() {
		return jdbcCommand.queryForObject("SELECT DATE_FORMAT(NOW(),'%H:%i:%s')", String.class);
	}
}
