package net.leiligheta.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import net.leiligheta.x10.X10Device;
import net.leiligheta.x10.X10Type;

public class Database {
	private Connection connection;
	
	public Database(String host, int port, String database, String username, String password) {
		try {
			System.out.println("Connecting to database...");
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			String url = "jdbc:mysql://" + host + ":" + port + "/" + database;
			connection = DriverManager.getConnection(url, username, password);
			System.out.println("Database connected to: " + url);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private ResultSet query(String query) {
		try {
			Statement s = connection.createStatement();
			return s.executeQuery(query);
		} catch (SQLException e) {
			
		}
		return null;
	}
	
	public PreparedStatement getPreparedStatement(String query) throws SQLException {
		return connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
	}
	
	public List<X10Device> getX10Devices() {
		List<X10Device> list = new LinkedList<X10Device>();
		ResultSet rs = query("SELECT * FROM x10Devices");
		try {
			while (rs.next()) {
				if (rs.getString("type").equals("LAMP")) {
					list.add(new X10Device(rs.getInt("deviceCode"), rs.getString("houseCode").charAt(0), rs.getString("name"), X10Type.valueOf(rs.getString("type")), rs.getBoolean("deviceOn"), rs.getInt("value")));
				} else {
					list.add(new X10Device(rs.getInt("deviceCode"), rs.getString("houseCode").charAt(0), rs.getString("name"), X10Type.valueOf(rs.getString("type")), rs.getBoolean("deviceOn")));
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	public void updateX10Device(X10Device x10d) {
		try {
			if (x10d.getType() == X10Type.LAMP) {
				PreparedStatement ps = getPreparedStatement("UPDATE x10Devices SET deviceOn=?, value=? WHERE houseCode=? AND deviceCode=?");
				ps.setBoolean(1, x10d.isOn());
				ps.setInt(2, x10d.getValue());
				ps.setString(3, x10d.getHouseCode() + "");
				ps.setInt(4, x10d.getDeviceCode());
				ps.executeUpdate();
				
			} else {
				PreparedStatement ps = getPreparedStatement("UPDATE x10Devices SET deviceOn=? WHERE houseCode=? AND deviceCode=?");
				ps.setBoolean(1, x10d.isOn());
				ps.setString(2, x10d.getHouseCode() + "");
				ps.setInt(3, x10d.getDeviceCode());
				ps.executeUpdate();
			}	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}