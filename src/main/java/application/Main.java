package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Main extends Application {
	static LANServer lan;// = new LANServer();
	static String opponent_address;
	static Account me = new Account();
	static int board_width = 550;
	static int board_height = 550;
	static int box_size = 25;
	//int box_height = 50;
	static final Image CURSOR = new Image(new File("cursor.png").toURI().toString());
	static final Image armor_stand = new Image(new File("armorstand.png").toURI().toString());
	static final Image damage = new Image(new File("damage.png").toURI().toString());
	static GridPane grid = new GridPane();
	static InfoBox info = new InfoBox("whitesmoke;");
	static InfoBox damage_box = new InfoBox("transparent");
	VBox action_box = new VBox();
	static LoadOut equip = new LoadOut();
	static Pane bp = new Pane();
	Label character_info = new Label();
	Label moves = new Label();
	int zoom = 2;
	
	int players = 1;
	boolean map_loaded = false;

	static 	BackPack backpack = new BackPack();
	//static Dragboard db;
	static int[][] game_board = new int[22][22];
	static Label item_info = new Label("Item info:");
	static Label lan_info = new Label();
	static CharacterList characters = new CharacterList();
	static CharacterList opponents = new CharacterList();
	static HashMap<Integer,Item> items = new HashMap<Integer,Item>();
	
	@Override
	public void start(Stage primaryStage) {
		try {
			TabPane tp = new TabPane();
			Tab single_player = new Tab("Single Player");
			Tab multi_player = new Tab("Multi Player");
			tp.getTabs().addAll(single_player,multi_player);
			single_player.setClosable(false);
			multi_player.setClosable(false);
			GridPane multi_form = new GridPane();
			Button host = new Button("Host?");
			Button connect = new Button("Connect?");
			
			host.setOnAction(e -> {
				try {
				    InetAddress addr = Inet4Address.getLocalHost();				    
				    lan_info.setText("You're hosting a game. Provide the below address to your opponent.\n" + addr.getHostAddress());
				    lan = new LANServer();
				    lan.start();
				   
				    
				} catch (UnknownHostException et) {
				}
				Button b = new Button("Start Game");
				b.setOnAction(eb -> {
					startGame(primaryStage,22,22,2);
				});
				multi_form.add(b, 0, 3);
				//lan.start();
			});
			connect.setOnAction(e -> {
				Label con_info = new Label("Enter your opponents IP");
				TextField t = new TextField();
				Button b = new Button("Connect");
				multi_form.add(con_info, 0, 2);
				multi_form.add(t, 0, 3);
				multi_form.add(b, 0, 4);
				b.setOnAction(btn -> {
					if(!t.getText().isEmpty()){
						//lan = new LANServer();
						opponent_address = t.getText();
						lan = new LANServer();
						lan.start();
						try {
							SendData send = new SendData(new JSONObject("{\"request\":\"connection\",\"address\":\""+ Inet4Address.getLocalHost().getHostAddress() +"\"}"));
							send.start();
						} catch (JSONException | UnknownHostException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				});
			});
			multi_form.add(host, 0, 0);
			multi_form.add(connect, 0, 1);
			multi_form.add(lan_info, 0, 2);
			multi_player.setContent(multi_form);
			VBox gp = new VBox();
			gp.setPadding(new Insets(20));
			single_player.setContent(gp);
			
			if(me.new_account) {
				Label lb = new Label("Enter Your Name Adventurer!");
				TextField tf = new TextField();
				gp.getChildren().addAll(lb,tf);
			}
			//
			File[] maps = new File(".").listFiles(new FilenameFilter() {
			    public boolean accept(File dir, String name) {
			        return name.toLowerCase().endsWith(".map");
			    }
			});
			if(maps.length > 0) {
				map_loaded = loadMap(maps[0]);
			}
			
			Spinner<Integer> spin = new Spinner<Integer>();
			final int initialValue = 3;
			
			Label enemies = new Label("Opponents");
			
		    SpinnerValueFactory<Integer> valueFactory =
		    		new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, initialValue);
		 
		    spin.setValueFactory(valueFactory);
			Button start = new Button("Start Game");
			start.setOnAction(e ->{
				startGame(primaryStage,22,22,spin.getValue());
			});
			gp.getChildren().addAll(enemies,spin,start);
			Scene setup_scene = new Scene(tp,650,550);
			primaryStage.setScene(setup_scene);
			primaryStage.show();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void startGame(Stage primaryStage,int width, int height, int players) {
		try {
			
			//System.out.println(me.my_gold + "");
			
			characters.myTeam = true;
			//attack_button.setFocusTraversable(false);
			//defend_button.setFocusTraversable(false);

			grid.setGridLinesVisible(true);	
			if(!map_loaded) {
				game_board = new int[height][width];
			}
	        for(int r = 0; r < game_board.length; r++) {
	        	for(int c = 0; c < game_board[r].length; c++) {
	        		if(!map_loaded) {
	        			int obj = (int)(Math.random()*10);
	        			game_board[r][c] = obj;
	        		}
	        		if(game_board[r][c] > 7) {
	        			Rectangle seed = new Rectangle(1,1, box_size, box_size);
	        			seed.setFill(Color.BROWN);
	        			grid.add(seed, c,r);
	        		}
	        		else if(game_board[r][c] > 6){
	        			Rectangle gold = new Rectangle(1,1, box_size, box_size);
	        			gold.setFill(Color.GOLD);
	        			grid.add(gold, c,r);
	        		}
	        	}
	        }
	        
	        selected_character = characters.getNext();
			for(Item i : items.values()) {
				if(!i.equipped) {
					//backpack.add(i, 0, 0);
					backpack.addToBackPack(i);
				}
			}
			
			backpack.setPadding(new Insets(5));
			
			action_box.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, CornerRadii.EMPTY, Insets.EMPTY)));
			action_box.getChildren().addAll(character_info,moves,equip,backpack);
			action_box.setPrefWidth(100);
			action_box.setPrefHeight(550);
			grid.setLayoutX(100);
			setActiveCharacter();
			setZoom(box_size);
			grid.add(selected_character, 5, 5);
			
			bp.getChildren().addAll(grid,action_box,info, damage_box);
	        Scene scene = new Scene(bp,board_width+100,board_height);
	        scene.setOnKeyPressed(e -> {
	        	System.out.println(e.getCode());
	        	if(selected_character != null) {
	        		int x,y = 0;
	        		y = selected_character.coordinates[1];
	        		x = selected_character.coordinates[0];
		        	switch(e.getCode()) {		        	
		        	case UP :
		        	case W :
		        		y = selected_character.coordinates[1] - 1;
		        		x = selected_character.coordinates[0];
		        		break;
		        	case DOWN :
		        	case S :
		        		y = selected_character.coordinates[1] + 1;
		        		x = selected_character.coordinates[0];
		        		break;
		        	case LEFT :
		        	case A :
		        		y = selected_character.coordinates[1];
		        		x = selected_character.coordinates[0] -1;
		        		break;
		        	case RIGHT :
		        	case D :
		        		y = selected_character.coordinates[1];
		        		x = selected_character.coordinates[0] + 1;
		        		break;
		        	case MINUS :
		        		zoom = 2;
		        		box_size = 25;
		        		setZoom(box_size);
		        		for(Character c : characters) {
		        			c.setZoom(box_size);
		        		}
		        		break;
		        	case EQUALS :
		        		zoom = 1;
		        		box_size = 50;
		        		setZoom(box_size);
		        		for(Character c : characters) {
		        			c.setZoom(box_size);
		        		}
		        		break;
		        	case SPACE :
		        		setActiveCharacter();
					default:
						y = selected_character.coordinates[1];
		        		x = selected_character.coordinates[0];
						break;
		        	}
		        	//System.out.println(spaceOccupied(x,y));
		        	if(!spaceOccupied(x,y) && selected_character.move1Space()) {
		        		grid.getChildren().remove(selected_character);
			        	grid.add(selected_character, x, y);
			        	//game_board.get(y).put(x, 10);
			        	selected_character.coordinates = new int[] {x,y};
		        		moves.setText("Moves: " + (selected_character.moves() - selected_character.has_moved));
		        		centerScreen();
		        	}
		        	
	        	}
	        	
	        });
	        
	        bp.setStyle("-fx-background-color:darkgrey;");
	        grid.setStyle("-fx-background-color:lightgreen;");
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}	
	
	public boolean loadMap(File map) {
		try {
			BufferedReader map_reader = new BufferedReader(new FileReader(map));
			String row = "";
			int index = 0;
			List<int[]> board = new ArrayList<int[]>();
			while((row = map_reader.readLine()) != null) {
				//System.out.println(row);
				board.add(Arrays.stream(row.split(",")).mapToInt(Integer::parseInt).toArray());
				index++;
			}
			game_board = new int[index][board.get(0).length];
			for(int[] a_row : board) {
				game_board[board.indexOf(a_row)] = a_row;
			}
			map_reader.close();
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public void setActiveCharacter() {
		action_box.getChildren().remove(equip);
		SendData send = new SendData(selected_character.toJson());
		send.start();
		selected_character = characters.getNext();
		selected_character.has_moved = 0;
		selected_character.has_attacked = false;
		//System.out.println(selected_character.toJson());
		moves.setText("Moves: " + (selected_character.moves() - selected_character.has_moved));
		character_info.setText(selected_character.name 
				+ "\nHP: " + (selected_character.health() - selected_character.damage_taken)
				//+ "\nAttack: " + selected_character.attack() 
				//+ "\nDefense: " + selected_character.defense()
				);
		equip = selected_character.load_out;
		action_box.getChildren().add(equip);
		equip.setPadding(new Insets(5));

	
		centerScreen();
	}
	
	public void setZoom(int size) {
		
		grid.getColumnConstraints().removeAll(grid.getColumnConstraints());
		grid.getRowConstraints().removeAll(grid.getRowConstraints());
		for(Node n : grid.getChildren()) {
			try {
				Rectangle obj = (Rectangle) n;
				obj.setWidth(size);
				obj.setHeight(size);
			}
			catch(Exception e) {
				continue;
			}
		}
		
		for(int i = 0; i < game_board[0].length; i++) {
            ColumnConstraints column = new ColumnConstraints(size);
            grid.getColumnConstraints().add(column);
        }

        for(int i = 0; i < game_board.length; i++) {
            RowConstraints row = new RowConstraints(size);
            grid.getRowConstraints().add(row);
        }
        centerScreen();
	}
	
	public static boolean inRange(Character defender) {
		
		double distance = distance(selected_character.coordinates, defender.coordinates);	
		//System.out.println(selected_character.range());
		if(selected_character.range() < distance)
			return false;
		int r,c;
		for(r=0;r<game_board.length;r++) {
			for(c=0;c<game_board[r].length;c++) {	
				if(game_board[r][c] > 8){					
					double test = distance(selected_character.coordinates,new int[] {c,r}) + distance(defender.coordinates,new int[] {c,r});
					double tested = 0;
					if(distance > test)
						tested = distance - test;
					else
						tested = test - distance;					
					if(tested < .25)
						return false;						
				}					
			}
		}		
		return true;
	}
	/*
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
	*/
	public static double distance(int[] a, int[] b) {
		double distance = Math.sqrt(
				Math.pow((a[1] - b[1]), 2) 
				+ Math.pow((a[0] - b[0]), 2));
		//System.out.println(distance);
		return distance;//round(distance,0);
	}
		
	public void centerScreen() {
		//bp.getChildren().get(0).setLayoutX(value);
		grid.setLayoutX(((5*zoom) - selected_character.coordinates[0]) * box_size + 100);
		grid.setLayoutY(((5*zoom) - selected_character.coordinates[1]) * box_size);
	}
	
	public boolean spaceOccupied(int x, int y) {
		//System.out.println(game_board.get(y).get(x));
		if(game_board[y][x] > 7) 
			return true;
		else if(game_board[y][x] > 6) {
			System.out.println("GOLD");
			game_board[y][x] = 0;
			
			return false;
		}
		else {
			for(Character c : characters) {
				if(c.coordinates[0] == x && c.coordinates[1] == y)
					return true;
			}
			for(Character c : opponents) {
				if(c.coordinates[0] == x && c.coordinates[1] == y)
					return true;
			}
		}		
		return false;
	}
	
	public static Character selected_character = null;
	
	public static void main(String[] args) {	
		boolean seed = true;
		if(new File("test.db").exists())
			seed = false;
		Connection c = DBController.connect();
		DBController.createTables(c);
		if(seed){
			DBController.seedData(c);
		}
		me = DBController.loadMyAccount(c);
		
		DBController.loadCharacters(c);
		
		try{
			c.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}		
		
		launch(args);
	}
}
