package utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Stack;

public class ConnectionPool {

	private String username;
	private String userpass;
	private String driver;
	private String url;
	
	private Stack<Connection> pool;
	
	public ConnectionPool() {
		Properties p = new Properties();
		try {
            p.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("database_info.properties"));
            this.username = p.getProperty("USER");
            this.userpass = p.getProperty("PASSWORD");
            this.url = p.getProperty("URL");

            this.driver = "com.mysql.jdbc.Driver";

            this.pool = new Stack<Connection>();

            Class.forName(this.driver).newInstance();
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        } catch (InstantiationException e) {

            e.printStackTrace();
        } catch (IllegalAccessException e) {

            e.printStackTrace();
        } catch (ClassNotFoundException e) {

            e.printStackTrace();
        }
	}
	
	public Connection getConnection() throws SQLException {
		if (this.pool.isEmpty()) {
            return DriverManager.getConnection(this.url, this.username, this.userpass);
        } else {
            return (Connection) this.pool.pop();
        }
	}

	public void releaseConnection(Connection conn) throws SQLException {
		this.pool.push(conn);

	}

	public void refreshConnectionPool() {
		while(!pool.empty()){
            try {
                pool.pop().close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

	}

}
