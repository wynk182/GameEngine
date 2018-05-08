package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBController {

	public static Connection connect() {
		String fileName = "test.db";	
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
	
	public static void addNewItem(String name,int item_type,int a,int d,int m,int h,int w,int r) {
		Connection c = connect();
		try {
			PreparedStatement pstmt = c.prepareStatement(
					"insert into items (name,item_type,attack_bonus,"
					+ "defense_bonus,move_bonus,"
					+ "health_bonus,worth,range) "
					+ "values (?,?,?,?,?,?,?,?);"
					);
			pstmt.setString(1, name);
			pstmt.setInt(2, item_type);
			pstmt.setInt(3, a);
			pstmt.setInt(4, d);
			pstmt.setInt(5, m);
			pstmt.setInt(6, h);
			pstmt.setInt(7, w);
			pstmt.setInt(8, r);
			pstmt.execute();
			pstmt.close();
			c.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Account loadMyAccount(Connection conn){
		String sql = "select * from account;";
		ResultSet account;
		Account me = new Account();
		try {
			account = conn.createStatement().executeQuery(sql);
			while(account.next()){				
				me.name = account.getString("name");
				me.auth_code = account.getString("auth_code");
				me.my_gold = account.getInt("my_gold");
				me.my_wins = account.getInt("my_wins");
				me.my_losses = account.getInt("my_losses");
				me.new_account = account.getBoolean("new_account");
			}
			account.close();
		} catch (SQLException e) {			
			e.printStackTrace();
		}	
		return me;
	}
	
	public static void loadCharacters(Connection conn){
		
		try {
			String sql = "select * from items;";
			ResultSet items = conn.createStatement().executeQuery(sql);
			while(items.next()){
				Item item = new Item();
				item.name = items.getString("name");
				item.item_type = items.getString("item_type");
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
				
				LoadOut load_out = null;
				while(loadout.next()){
					load_out = new LoadOut();		
					//List<Item> equipment = new ArrayList<Item>();
					load_out.equipItem(Main.items.get(loadout.getInt("right_hand")), "right");
					load_out.equipItem(Main.items.get(loadout.getInt("left_hand")), "left");
					load_out.equipItem(Main.items.get(loadout.getInt("head")), "head");
					load_out.equipItem(Main.items.get(loadout.getInt("body")),"body");
					load_out.equipItem(Main.items.get(loadout.getInt("feet")),"feet");					
				}
				//int spec = characters.getInt("spec");
				
				
				int health = characters.getInt("health");
				int attack = characters.getInt("attack");
				int defense = characters.getInt("defense");
				int moves = characters.getInt("moves");
				Character character = new Character(health,attack,defense,moves);
				//character.load_out = load_out;
				character.name = characters.getString("name");
				character.gender = characters.getString("gender");
				character.coordinates = new int[]{5,5};
				character.load_out = load_out;
				character.setTriggers();
				//character.setLoadOut(load_out);
				Main.characters.add(character);
				//System.out.println("Item in right hand is " + load_out.right_hand.name);
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
	                + "	item_type text,\n"
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
			sql = "CREATE TABLE IF NOT EXISTS account (\n"
	                + "	id integer PRIMARY KEY AUTOINCREMENT,\n"
	                + "	name text NOT NULL,\n"
	                + "	auth_code text,\n"
	                + " my_gold integer,\n"
	                + " my_wins integer,\n"
	                + " my_losses integer,\n"
	                + " new_account bit\n"	                
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
			sql = "insert into characters (name,spec,health,attack,defense,moves,gender) "
					+ "values (?,?,?,?,?,?,?);";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "Peep");
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
			pstmt.setString(2, "right");
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
			pstmt.setString(2, "left");
			pstmt.setInt(3, 2);
			pstmt.setInt(4, 10);
			pstmt.setInt(5, -1);
			pstmt.setInt(6, 10);
			pstmt.setInt(7, 7);
			pstmt.setInt(8, 0);
			pstmt.execute();
			pstmt.close();
			sql = "insert into items (name,item_type,attack_bonus,defense_bonus,move_bonus,"
					+ "health_bonus,worth,range) "
					+ "values (?,?,?,?,?,?,?,?);";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "Leather Shoes");
			pstmt.setString(2, "feet");
			pstmt.setInt(3, 0);
			pstmt.setInt(4, 2);
			pstmt.setInt(5, 2);
			pstmt.setInt(6, 0);
			pstmt.setInt(7, 3);
			pstmt.setInt(8, 0);
			pstmt.execute();
			pstmt.close();
			sql = "insert into items (name,item_type,attack_bonus,defense_bonus,move_bonus,"
					+ "health_bonus,worth,range) "
					+ "values (?,?,?,?,?,?,?,?);";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "Oak Barrel");
			pstmt.setString(2, "body");
			pstmt.setInt(3, -1);
			pstmt.setInt(4, 10);
			pstmt.setInt(5, -1);
			pstmt.setInt(6, 0);
			pstmt.setInt(7, 15);
			pstmt.setInt(8, 0);
			pstmt.execute();
			pstmt.close();
			sql = "insert into items (name,item_type,attack_bonus,defense_bonus,move_bonus,"
					+ "health_bonus,worth,range) "
					+ "values (?,?,?,?,?,?,?,?);";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "Rifle");
			pstmt.setString(2, "right");
			pstmt.setInt(3, 20);
			pstmt.setInt(4, 0);
			pstmt.setInt(5, 0);
			pstmt.setInt(6, 0);
			pstmt.setInt(7, 150);
			pstmt.setInt(8, 12);
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
			pstmt.setInt(6, 3);			
			pstmt.execute();
			pstmt.close();
			sql = "insert into loadouts (character_id,right_hand,left_hand,body,head,feet) "
					+ "values (?,?,?,?,?,?);";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, 2);
			pstmt.setInt(2, 0);
			pstmt.setInt(3, 0);
			pstmt.setInt(4, 0);
			pstmt.setInt(5, 0);
			pstmt.setInt(6, 0);			
			pstmt.execute();
			pstmt.close();
			sql = "insert into account (name,auth_code,my_gold,my_wins,my_losses,new_account) "
					+ "values (?,?,?,?,?,?);";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "");
			pstmt.setString(2, "");
			pstmt.setInt(3, 100);
			pstmt.setInt(4, 0);
			pstmt.setInt(5, 0);
			pstmt.setBoolean(6, false);
			pstmt.execute();
			pstmt.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
