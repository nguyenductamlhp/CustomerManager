package model.service;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.data.Customer;
import utils.ConnectionPool;

public class CustomerService {
	
	public List<Customer> getAllCustomer()  {
		List<Customer> list = new ArrayList<>();
		
		try {
			ConnectionPool connectionPool = new ConnectionPool();
			Connection connection = connectionPool.getConnection();
			String queryCustomer = "select * from custom";
			
			Statement statement;
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(queryCustomer);
			
			while (resultSet.next()) {
				Customer Customer = new Customer();
				
				Customer.setPhone(resultSet.getString("phone"));
				Customer.setName(resultSet.getString("name"));
				Customer.setAddress(resultSet.getString("address"));
				Customer.setNote(resultSet.getString("note"));
				
				list.add(Customer);
			}
			connection.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}
	
		return list;
	}
	
	public boolean isExist(String phone) {
		List<Customer> listCustom = getAllCustomer();
		for (Customer customer : listCustom) {
			if (customer.getPhone().equals(phone)) {
				return true;
			}
		}
		return false;
	}
	
	public List<Customer> findCustomer(String phone) {
		List<Customer> customerList = getAllCustomer();
		List<Customer> reList = new ArrayList<>();
		
		for (Customer customer : customerList) {
			if (customer.getPhone().contains(phone.trim())) {
				reList.add(customer);
			}
		
		}
		return reList;
	}
	
	public int getCustomer(String phone) throws SQLException {
		List<Customer> CustomerList = getAllCustomer();
		int i = 0;
		for (Customer customer : CustomerList) {
			if (customer.getPhone().equals(phone)) {
				return i;
			}
			i++;
		}
		return -1;
	}

	public boolean updateCustomer(Customer c) {
		ConnectionPool connectionPool = new ConnectionPool();
		Connection connection;
		try {
			if (isExist(c.getPhone())) {
				return false;
			} else {
				connection = connectionPool.getConnection();
				
				String updateCustomer = "update custom set phone='" + c.getPhone() + "' , address='";
				updateCustomer +=  c.getName() + "', address='";
				updateCustomer +=  c.getAddress() + "', note='";
				updateCustomer +=  c.getNote() + "' where phone='" + c.getPhone() + "'";
				
				Statement statement;
				statement = connection.createStatement();
				statement.executeUpdate(updateCustomer);
				
				connection.close();
				return true;
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
			return false;
		}
		
	}
	
	public boolean addCustomer(Customer c) {
		ConnectionPool connectionPool = new ConnectionPool();
		Connection connection;
		try {
			if (isExist(c.getPhone()) || c.getPhone().equals("")) {
				return false;
			} else {
				connection = connectionPool.getConnection();
				String insertCustomer = "insert into custom values ('" + c.getPhone() + "', '";
				insertCustomer += c.getName() + "', '";
				insertCustomer += c.getAddress() + "', '";
				insertCustomer += c.getNote() + "') ";		
				
				Statement statement;
				statement = connection.createStatement();
				statement.executeUpdate(insertCustomer);
				connection.close();
				return true;
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
			return false;
		}
		
	}
	
	public boolean deleteCustomer(String phone) {
		ConnectionPool connectionPool = new ConnectionPool();
		Connection connection;
		try {
			connection = connectionPool.getConnection();
			String insertCustomer = "delete from custom where phone='" + phone + "'";
			
			Statement statement;
			statement = connection.createStatement();
			statement.executeUpdate(insertCustomer);
			connection.close();
			return true;
		} catch (SQLException e) {
			
			e.printStackTrace();
			return false;
		}
	}
}
