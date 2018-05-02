package application;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
	GridPane grid = new GridPane();
	Pane bp = new Pane();
	Label character_info = new Label();
	Label moves = new Label();
	Button attack_button = new Button("Attack");
	Button defend_button = new Button("Defend");
	
	static int[][] game_board = new int[11][11];
	
	static CharacterList characters = new CharacterList();
	List<int[]> objects = new ArrayList<int[]>();
	
	@Override
	public void start(Stage primaryStage) {
		try {
			attack_button.setFocusTraversable(false);
			defend_button.setFocusTraversable(false);

			grid.setGridLinesVisible(true);;
			for(int i = 0; i < 11; i++) {
	            ColumnConstraints column = new ColumnConstraints(50);
	            grid.getColumnConstraints().add(column);
	        }

	        for(int i = 0; i < 11; i++) {
	            RowConstraints row = new RowConstraints(50);
	            grid.getRowConstraints().add(row);
	        }
	        for(int r = 0; r < game_board.length; r++) {
	        	for(int c = 0; c < game_board[r].length; c++) {
	        		int obj = (int)(Math.random()*10);
	        		game_board[r][c] = obj;
	        		if(obj > 8) {
	        			Rectangle seed = new Rectangle(1,1, 50, 50);
	        			seed.setFill(Color.BROWN);
	        			grid.add(seed, c,r);
	        		}
	        	}
	        }
	        /*
	        for(int r = 0; r < 11; r++) {
        		HashMap<Integer,Integer> row = new HashMap<>();

	        	for(int c = 0; c < 11; c++) {
	        		int obj = (int)(Math.random()*10);
	        		if(obj > 8) {
	        			Rectangle seed = new Rectangle(1,1, 50, 50);
	        			seed.setFill(Color.BROWN);
	        			grid.add(seed, c,r);
	        		}
	        		row.put(c, obj);
	        	}
        		System.out.println(row);

        		game_board.put(r, row);

	        }
	        */
	        //System.out.println();
	        
	        /*
	        for(int i = 0; i < 1000;i++) {
	        	int x = (int) (Math.random() *100);
	        	int y = (int) (Math.random() *100);
	        	objects.add(new int[] {x,y});
	        	Rectangle seed = new Rectangle(1,1, 50, 50);
	        	seed.setFill(Color.BROWN);
	        	grid.add(seed, x, y);
	        }
	        for(int i = 0; i < 2000;i++) {
	        	int x = (int) (Math.random() *100);
	        	int y = (int) (Math.random() *100);
	        	objects.add(new int[] {x,y});
	        	Rectangle seed = new Rectangle(1,1, 50, 50);
	        	seed.setFill(Color.DARKBLUE);
	        	grid.add(seed, x, y);
	        }
	        */
	        
	        Character warrior = new Character(Specialization.Warrior, new int[] {0,0},"Buster");
	        warrior.equipWeapon(Weapons.Sword);
	        Character ranger = new Character(Specialization.Ranger, new int[] {1,9},"Robin");
	        ranger.equipWeapon(Weapons.Bow);
	        Character healer = new Character(Specialization.Healer, new int[] {5,0},"Rosa");
	        Character peasant = new Character(Specialization.Peasant, new int[] {2,9}, "Leah");
	        //peasant.equipWeapon(Weapons.SuperSword);
	        characters.add(warrior);
	        characters.add(healer);
	        characters.add(ranger);
	        characters.add(peasant);
	        
	        selected_character = characters.getNext();
	        grid.add(warrior.drawCharacter(), 0,0);
			grid.add(healer.drawCharacter(), 5, 0);
			grid.add(ranger.drawCharacter(), 1, 9);
			grid.add(peasant.drawCharacter(), 2, 9);
	        /*
	        grid.setOnMouseClicked(e -> {
	        	int x = (int) e.getSceneX();
	        	int y = (int) e.getSceneY();
	        	//System.out.println(x + " | " + y);
	        	if(selected_character != null && !spaceOccupied(x,y)) {
	        		grid.getChildren().remove(selected_character.block);
	        		grid.add(selected_character.drawCharacter(), x/50, y/50);
	        		selected_character.coordinates = new int[] {x/50, y/50};
	        	}
	        });
			*/
			
			VBox action_box = new VBox();
			action_box.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, CornerRadii.EMPTY, Insets.EMPTY)));
			action_box.getChildren().addAll(character_info,moves);
			action_box.setPrefWidth(100);
			action_box.setPrefHeight(550);
			grid.setLayoutX(100);
			setActiveCharacter();
			//bp.setCenter(grid);
			//bp.setLeft(action_box);
			bp.getChildren().addAll(grid,action_box);
	        Scene scene = new Scene(bp,650,550);
	        scene.setOnKeyPressed(e -> {
	        	System.out.println(e.getCode());
	        	if(selected_character != null) {
	        		int x,y = 0;
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
		        	case SPACE :
		        		setActiveCharacter();
					default:
						y = selected_character.coordinates[1];
		        		x = selected_character.coordinates[0];
						break;
		        	}
		        	System.out.println(spaceOccupied(x,y));
		        	if(!spaceOccupied(x,y) && selected_character.move1Space()) {
		        		grid.getChildren().remove(selected_character.block);
			        	grid.add(selected_character.drawCharacter(), x, y);
			        	//game_board.get(y).put(x, 10);
			        	selected_character.coordinates = new int[] {x,y};
		        		moves.setText("Moves: " + (selected_character.moves - selected_character.has_moved));
		        		centerScreen();


			        	/*
			        	if(!selected_character.move1Space()) {
		        			selected_character = characters.getNext();
		        			System.out.println(selected_character.coordinates[0] * 50);
		        			System.out.println(selected_character.name);
		        			centerScreen();
		        			
			        	}
			        	*/
		        	}
		        	
	        	}
	        	
	        });
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setActiveCharacter() {
		selected_character = characters.getNext();
		selected_character.has_moved = 0;
		selected_character.has_attacked = false;
		moves.setText("Moves: " + (selected_character.moves - selected_character.has_moved));
		character_info.setText(selected_character.name 
				+ "\nHP: " + selected_character.health
				+ "\nAttack: " + selected_character.attack 
				+ "\nDefense: " + selected_character.defense);
		centerScreen();
	}
	
	public static boolean inRange(Character defender) {
		double distance = distance(selected_character.coordinates, defender.coordinates);
		if(selected_character.weapon.range <= distance)
			return false;
		int r,c;
		for(r=0;r<game_board.length;r++) {
			for(c=0;c<game_board[r].length;c++) {
				if((distance(selected_character.coordinates,new int[] {c,r}) + distance(new int[] {c,r},defender.coordinates) == distance) 
						&& game_board[r][c] > 8) {
					
					return false;
				}
			}
		}
		return selected_character.weapon.range >= distance;
	}
	
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
	
	public static double distance(int[] a, int[] b) {
		double distance = Math.sqrt(
				Math.pow((a[1] - b[1]), 2) 
				+ Math.pow((a[0] - b[0]), 2));
		//System.out.println(distance);
		return round(distance,1);
	}
		
	public void centerScreen() {
		//bp.getChildren().get(0).setLayoutX(value);
		grid.setLayoutX((5 - selected_character.coordinates[0]) * 50 + 100);
		grid.setLayoutY((5 - selected_character.coordinates[1]) * 50);

	}
	
	public boolean spaceOccupied(int x, int y) {
		//System.out.println(game_board.get(y).get(x));
		if(game_board[y][x] > 8) 
			return true;
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
		DBController.connect("test.db");
		launch(args);
	}
}
