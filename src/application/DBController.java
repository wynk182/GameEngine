package application;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBController {

	public static Connection connect(String fileName) {
			
        Connection conn = null;
        
        try {
        	Class.forName("org.sqlite.JDBC");

            // db parameters
            String url = "jdbc:sqlite:" + fileName;
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            
            System.out.println("Connection to SQLite has been established.");
            
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        } 
        return conn;
    }
	
	public static void createTables(Connection conn) {
		String sql = "CREATE TABLE IF NOT EXISTS characters (\n"
                + "	id integer PRIMARY KEY,\n"
                + "	name text NOT NULL,\n"
                + "	spec text,\n"
                + " health integer,\n"
                + " attack integer,\n"
                + " defense integer,\n"
                + " moves integer,\n"
                + " gender text,\n"
                + ");";
		 Statement stmt;
		try {
			stmt = conn.createStatement();
			stmt.execute(sql);
			stmt.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		 
	}
}
