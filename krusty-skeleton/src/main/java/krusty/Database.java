package krusty;


import spark.Request;
import spark.Response;

import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.naming.spi.DirStateFactory.Result;

import static krusty.Jsonizer.toJson;

public class Database {
	/**
	 * Modify it to fit your environment and then use this string when connecting to your database!
	 */
	private Connection conn;
	private static final String jdbcString = "jdbc:mysql://puccini.cs.lth.se/hbg37";

	// For use with MySQL or PostgreSQL
	private static final String jdbcUsername = "hbg37";
	private static final String jdbcPassword = "efg441xi";

	public void connect() {
		try{
			conn = DriverManager.getConnection(jdbcString, jdbcUsername, jdbcPassword);

		} catch (SQLException e) {
			System.err.println(e);
			e.printStackTrace();
		}
	}

	// TODO: Implement and change output in all methods below!

	public String getCustomers(Request req, Response res) {

		try {
			ResultSet rs = conn.createStatement().executeQuery("SELECT customerName as name, address from Customers");
			String json = Jsonizer.toJson(rs, "customers");
			return json;
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return "{}";
	}
	public String getCookies(Request req, Response res) {

		try{
			ResultSet rs = conn.createStatement().executeQuery("SELECT cookieName as name FROM Cookies");
			String json = Jsonizer.toJson(rs, "cookies");
			return json;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "{\"cookies\":[]}";
	}

	public String getRawMaterials(Request req, Response res) {


		try{
			ResultSet rs = conn.createStatement().executeQuery("SELECT ingredientName as name, quantityInStock as amount, unit FROM Warehouses");
			String json = Jsonizer.toJson(rs, "raw-materials");
			return json;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "{}";
	}


	public String getRecipes(Request req, Response res) {

		try {
			ResultSet rs = conn.createStatement().executeQuery("SELECT Recipes.*, Warehouses.unit FROM Recipes, Warehouses WHERE Recipes.ingredientName = Warehouses.ingredientName");
			String json = Jsonizer.toJson(rs, "recipes");
			return json;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "{}";
	}

	public String getPallets(Request req, Response res) {
		
		List<String> query = new ArrayList<String>();
		List<String> values = new ArrayList<String>();

		String json = "";

		String sql = "SELECT palletId AS id, cookieName AS cookie, productionDate AS production_date, Customers.customerName AS customer, blockedStatus as blocked" 
		+ " FROM Pallets"
		+ " LEFT JOIN Customers ON Customers.customerName = Pallets.customerName ";
		
		if(req.queryParams("from") != null){
			query.add("productionDate >= ? ");
			values.add(req.queryParams("from"));
		}

		if(req.queryParams("to") != null){
			if(query.size() > 0){
				query.add("AND ");
			}
			query.add("productionDate <= ? ");
			values.add(req.queryParams("to"));
		}


		if(req.queryParams("cookie") != null){
			if(query.size() > 0){
				query.add("AND ");
			}
			query.add("cookieName = ? ");
			values.add(req.queryParams("cookie"));
		}

		if(req.queryParams("blocked") != null){
			if(query.size() > 0){
				query.add("AND ");
			}
			query.add("blockedStatus = ? ");
			values.add(req.queryParams("blocked"));
		}
		
		if(query.size() > 0){
			query.add(0, "WHERE ");
			for(String s : query){
				sql += s;
			}
		}

		sql += "ORDER BY (productionDate)";

		try {

			PreparedStatement ps = conn.prepareStatement(sql);
			
			for(int i = 0; i < values.size(); i++){
				ps.setString(i+1, values.get(i));	
			}
			
			ResultSet rs = ps.executeQuery();
			json = krusty.Jsonizer.toJson(rs, "pallets");
			return json;
		} 
		
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		//return "{\"pallets\":[]}";
		return json;
	}

	public String reset(Request req, Response res) {
		
		try(Statement ps = conn.createStatement()){
			
			String[] sql;
			Path filePath = Path.of("krusty-skeleton/src/main/sql_files/initial-data.sql");
			
				conn.setAutoCommit(false);
				String stringOfFile = Files.readString(filePath);
				sql = stringOfFile.split(";");
				Statement stmt = conn.createStatement();
				conn.commit();
				
				for (String str : sql) {
					stmt.executeUpdate(str);
				}
				

			return "{" + "\"status\": \"ok\"}";



		} catch (Exception e) {
			e.printStackTrace();
		}
		return "{"+ "\"status\": \"ok\"}";	
	}

	public String createPallet(Request req, Response res) {
		
		String cookie = req.queryParams("cookie");
		String sql = "SELECT cookieName FROM Cookies WHERE cookieName = '" + cookie + "';";
		PreparedStatement ps = null;
		ResultSet rs = null;


		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();

			if(!rs.next()){
				return krusty.Jsonizer.anythingToJson("unknown cookie", "error");
			}

		} catch (Exception e) {
			e.printStackTrace();
			return krusty.Jsonizer.anythingToJson("error", "status");
		}


		sql = "insert into Pallets(productionDate, cookieName, blockedStatus)" 
		+ "values(NOW(), '" + cookie + "', true)";

		String json = "";

		try {
			
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.executeUpdate();
			ResultSet rs1 = ps.getGeneratedKeys();
			rs1.next();
			conn.commit();


			sql = "UPDATE Warehouses, Recipes SET Warehouses.quantityInStock = Warehouses.quantityInStock - Recipes.amount"
                +
                " WHERE Warehouses.ingredientName = Recipes.ingredientName AND Recipes.cookieName = '"
                + cookie + "';";




			ps = conn.prepareStatement(sql);
			ps.executeUpdate();
			conn.commit();

			json = "{\n \"status\": \"ok\", \n \"id\": " + rs1.getInt(1) + " \n}"; 

		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			return krusty.Jsonizer.anythingToJson("error", "status");
		} finally {
			try {
				conn.setAutoCommit(true);
				return json;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return "";
	}
}
