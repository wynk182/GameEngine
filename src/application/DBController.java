package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
	
	public static void loadCharacters(Connection conn){
		
		try {
			String sql = "select * from items;";
			ResultSet items = conn.createStatement().executeQuery(sql);
			while(items.next()){
				Item item = new Item();
				item.name = items.getString("name");
				item.item_type = items.getInt("item_type");
				item.attack_bonus = items.getInt("attack_bonus");
				item.defense_bonus = items.getInt("defense_bonus");
				item.move_bonus = items.getInt("move_bonus");
				item.health_bonus = items.getInt("health_bonus");
				item.worth = items.getInt("worth");
				item.range = items.getInt("range");
				Main.items.put(items.getInt("id"),item);
			}
			items.close();
			sql = "select * from characters;";		
			ResultSet characters = conn.createStatement().executeQuery(sql);
			while(characters.next()){
				ResultSet loadout = conn.createStatement().executeQuery("select * from loadouts "
						+ "where character_id = " + characters.getInt("id"));
				
				LoadOut load_out = new LoadOut();
				while(loadout.next()){
					load_out.right_hand = Main.items.get(loadout.getInt("right_hand"));
					load_out.left_hand = Main.items.get(loadout.getInt("left_hand"));
					load_out.body = Main.items.get(loadout.getInt("body"));
					load_out.feet = Main.items.get(loadout.getInt("feet"));
				}
				//int spec = characters.getInt("spec");
				
				Character character = new Character();
				//character.load_out = load_out;
				character.name = characters.getString("name");
				character.gender = characters.getString("gender");
				character.health = characters.getInt("health");
				character.attack = characters.getInt("attack");
				character.defense = characters.getInt("defense");
				character.moves = characters.getInt("moves");
				character.applyLoadOut(load_out);
				Main.characters.add(character);
				System.out.println("Item in right hand is " + character.load_out.right_hand.name);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void createTables(Connection conn) {
		//boolean created = false;
		String sql = "CREATE TABLE IF NOT EXISTS characters (\n"
                + "	id integer PRIMARY KEY AUTOINCREMENT,\n"
                + "	name text NOT NULL,\n"
                + "	spec integer,\n"
                + " health integer,\n"
                + " attack integer,\n"
                + " defense integer,\n"
                + " moves integer,\n"
                + " gender text\n"
                + ");";
		 Statement stmt;
		try {
			stmt = conn.createStatement();
			stmt.execute(sql);
			stmt.close();
			sql = "CREATE TABLE IF NOT EXISTS items (\n"
	                + "	id integer PRIMARY KEY AUTOINCREMENT,\n"
	                + "	name text NOT NULL,\n"
	                + "	item_type integer,\n"
	                + " attack_bonus integer,\n"
	                + " defense_bonus integer,\n"
	                + " move_bonus integer,\n"
	                + " health_bonus integer,\n"
	                + " worth integer,\n"
	                + " range integer\n"
	                + ");";
			stmt = conn.createStatement();
			stmt.execute(sql);
			stmt.close();
			sql = "CREATE TABLE IF NOT EXISTS loadouts (\n"
	                + "	id integer PRIMARY KEY AUTOINCREMENT,\n"
	                + "	character_id integer NOT NULL,\n"
	                + "	right_hand integer,\n"
	                + " left_hand integer,\n"
	                + " body integer,\n"
	                + " head integer,\n"
	                + " feet integer\n"
	                + ");";
			stmt = conn.createStatement();
			stmt.execute(sql);
			stmt.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		 
	}
	
	public static void seedData(Connection conn){
		
		try {
			String sql = "insert into characters (name,spec,health,attack,defense,moves,gender) "
					+ "values (?,?,?,?,?,?,?);";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "Buster");
			pstmt.setInt(2, 0);
			pstmt.setInt(3, 100);
			pstmt.setInt(4, 5);
			pstmt.setInt(5, 5);
			pstmt.setInt(6, 2);
			pstmt.setString(7, "Male");
			pstmt.execute();
			pstmt.close();
			sql = "insert into items (name,item_type,attack_bonus,defense_bonus,move_bonus,"
					+ "health_bonus,worth,range) "
					+ "values (?,?,?,?,?,?,?,?);";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "Wooden Sword");
			pstmt.setInt(2, 1);
			pstmt.setInt(3, 8);
			pstmt.setInt(4, 2);
			pstmt.setInt(5, 0);
			pstmt.setInt(6, 0);
			pstmt.setInt(7, 5);
			pstmt.setInt(8, 1);
			pstmt.execute();
			pstmt.close();
			sql = "insert into items (name,item_type,attack_bonus,defense_bonus,move_bonus,"
					+ "health_bonus,worth,range) "
					+ "values (?,?,?,?,?,?,?,?);";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "Wooden Sheild");
			pstmt.setInt(2, 0);
			pstmt.setInt(3, 2);
			pstmt.setInt(4, 10);
			pstmt.setInt(5, -1);
			pstmt.setInt(6, 10);
			pstmt.setInt(7, 7);
			pstmt.setInt(8, 0);
			pstmt.execute();
			pstmt.close();
			sql = "insert into loadouts (character_id,right_hand,left_hand,body,head,feet) "
					+ "values (?,?,?,?,?,?);";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, 1);
			pstmt.setInt(2, 1);
			pstmt.setInt(3, 2);
			pstmt.setInt(4, 0);
			pstmt.setInt(5, 0);
			pstmt.setInt(6, 0);			
			pstmt.execute();
			pstmt.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
