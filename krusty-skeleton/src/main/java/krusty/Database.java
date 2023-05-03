package krusty;

import spark.Request;
import spark.Response;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

import com.fasterxml.jackson.databind.util.JSONPObject;

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
		return "{}";
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
		return "{\"pallets\":[]}";
	}

	public String reset(Request req, Response res) {
		return "{}";
	}

	public String createPallet(Request req, Response res) {
		return "{}";
	}
}
