package application;

import java.io.File;
import java.sql.Connection;
import java.util.HashMap;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
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
	static Account me = new Account();
	static int board_width = 550;
	static int board_height = 550;
	static int box_size = 25;
	//int box_height = 50;
	static Image cursor = new Image(new File("cursor.png").toURI().toString());
	static Image armor_stand = new Image(new File("armorstand.png").toURI().toString());
	static Image damage = new Image(new File("damage.png").toURI().toString());
	GridPane grid = new GridPane();
	static InfoBox info = new InfoBox("whitesmoke;");
	static InfoBox damage_box = new InfoBox("transparent");
	VBox action_box = new VBox();
	static LoadOut equip = new LoadOut();
	static Pane bp = new Pane();
	Label character_info = new Label();
	Label moves = new Label();
	int zoom = 2;
	//Button attack_button = new Button("Attack");
	//Button defend_button = new Button("Defend");
	//Rectangle r = new Rectangle(1,1,20,20);
	//Rectangle l = new Rectangle(1,1,20,20);
	//Rectangle h = new Rectangle(1,1,20,20);
	//Rectangle b = new Rectangle(1,1,20,20);
	//Rectangle f = new Rectangle(1,1,20,20);
	int players = 2;

	static 	BackPack backpack = new BackPack();
	//static Dragboard db;
	static int[][] game_board = new int[100][100];
	static Label item_info = new Label("Item info:");
	static CharacterList characters = new CharacterList();
	static HashMap<Integer,Item> items = new HashMap<Integer,Item>();
	
	@Override
	public void start(Stage primaryStage) {
		try {
			System.out.println(me.my_gold + "");
			/*
			if(me.new_account){
				// Setup new account
				VBox gp = new VBox();
				Label lb = new Label("Enter Your Name Adventurer!");
				TextField tf = new TextField();
				gp.getChildren().addAll(lb,tf);
				Scene setup_scene = new Scene(gp,200,200);
				primaryStage.setScene(setup_scene);
			}
			*/
			characters.myTeam = true;
			//attack_button.setFocusTraversable(false);
			//defend_button.setFocusTraversable(false);

			grid.setGridLinesVisible(true);	
			
	        for(int r = 0; r < game_board.length; r++) {
	        	for(int c = 0; c < game_board[r].length; c++) {
	        		int obj = (int)(Math.random()*10);
	        		game_board[r][c] = obj;
	        		if(obj > 7) {
	        			Rectangle seed = new Rectangle(1,1, box_size, box_size);
	        			seed.setFill(Color.BROWN);
	        			grid.add(seed, c,r);
	        		}
	        		else if(obj > 6){
	        			Rectangle gold = new Rectangle(1,1, box_size/2, box_size/2);
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
	
	public void setActiveCharacter() {
		action_box.getChildren().remove(equip);
		selected_character = characters.getNext();
		selected_character.has_moved = 0;
		selected_character.has_attacked = false;
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
		System.out.println(board_width/size);
		System.out.println(board_height/size);
		//int columns = board_width/size;
		//int rows = board_height/size;
		grid.getColumnConstraints().removeAll(grid.getColumnConstraints());
		grid.getRowConstraints().removeAll(grid.getRowConstraints());
		
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
