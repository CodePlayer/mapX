package mapx.jdbc.adapter;

import mapx.jdbc.JdbcCommand;

/**
 * Oracle时间适配器类，用于获取Oracle数据库的当前系统时间
 * @author Ready
 * @date 2012-6-6
 */
public class OracleTimeAdapter implements TimeAdapter {
	private JdbcCommand jdbcCommand;

	public JdbcCommand getJdbcCommand() {
		return jdbcCommand;
	}

	public void setJdbcCommand(JdbcCommand jdbcCommand) {
		this.jdbcCommand = jdbcCommand;
	}

	public String currentDate() {
		return jdbcCommand.queryForObject("SELECT SYSDATE FROM DUAL", String.class);
	}

	public String currentDatetime() {
		return jdbcCommand.queryForObject("SELECT SYSDATE FROM DUAL", String.class);
	}

	public String currentTime() {
		return jdbcCommand.queryForObject("SELECT TO_CHAR(SYSDATE,'HH24:MI:SS') FROM DUAL", String.class);
	}
}
