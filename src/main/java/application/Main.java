package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Main extends Application {
	static LANServer lan;
	static String opponent_address;
	static Account me = new Account();
	static int board_width = 550;
	static int board_height = 550;
	static int box_size = 25;
	static final Image CURSOR = new Image(new File("cursor.png").toURI().toString());
	static final Image armor_stand = new Image(new File("armorstand.png").toURI().toString());
	static final Image damage = new Image(new File("damage.png").toURI().toString());
	static GridPane grid = new GridPane();
	static InfoBox info = new InfoBox("whitesmoke");
	static InfoBox damage_box = new InfoBox("transparent");
	static InfoBox game_info = new InfoBox("whitesmoke");
	VBox action_box = new VBox();
	static LoadOut equip = new LoadOut();
	static Pane bp = new Pane();
	Label character_info = new Label();
	Label moves = new Label();
	int zoom = 2;
	
	static Button start_game = new Button("Start Game");

	
	int players = 1;
	boolean map_loaded = false;
	static GridPane map_preview = new GridPane();
	static 	BackPack backpack = new BackPack();
	static int[][] game_board = new int[22][22];
	static Label item_info = new Label("Item info:");
	static Label lan_info = new Label();
	static CharacterList characters = new CharacterList();
	static CharacterList opponents = new CharacterList();
	static HashMap<Integer,Item> items = new HashMap<Integer,Item>();
	static List<File> maps = new ArrayList<File>();	

	public Tab mapBuilderTab() {
		Tab map_builder = new Tab("Map Builder");
		BorderPane bp = new BorderPane();
		GridPane gp = new GridPane();
		RadioButton forest = new RadioButton("Forest");
		RadioButton water = new RadioButton("Water");
		RadioButton stone = new RadioButton("Stone");
		ToggleGroup land_forms = new ToggleGroup();
		HBox toggles = new HBox();
		Spinner<Integer> map_width = new Spinner<Integer>();
		Spinner<Integer> map_height = new Spinner<Integer>();
		Button save = new Button("Save Map");
		TextField map_name = new TextField("Map Name");
		
		save.setOnAction(e -> {
			int[][] map = new int[map_height.getValue()][map_width.getValue()];
			for(int r = 0; r < map.length; r++){
				for(int c = 0; c < map.length; c++){
					map[r][c] = 0;
				}
			}
			
			for(Node n : gp.getChildren()){
				try{
					System.out.println(GridPane.getRowIndex(n));
					System.out.println(GridPane.getColumnIndex(n));
					map[GridPane.getRowIndex(n)][GridPane.getColumnIndex(n)] = ((Obstacle)n).type;
				}catch(Exception e1){
					continue;
				}				
			}
			
			try {
				
				BufferedWriter writer = new BufferedWriter(new FileWriter(map_name.getText() + ".map"));
				for(int r = 0; r < map.length; r++){
					String row = Arrays.toString(map[r]);
					row = row.replace("[", "");
					row = row.replace("]", "");
					row = row.replace(" ", "");
					writer.write(row.trim() + "\n");
				}
				writer.flush();
				writer.close();
				maps.add(new File(map_name.getText() + ".map"));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}			
			
		});
		
	    map_builder.setClosable(false);
	    gp.setGridLinesVisible(true);

	    map_width.setOnMouseClicked(e -> {
	    	//System.out.println("width");
	    	//map = new int[map_height.getValue()][map_width.getValue()];
	    	gp.getColumnConstraints().removeAll(gp.getColumnConstraints());
	    	for(int i = 0; i < map_width.getValue(); i++) {
	            ColumnConstraints column = new ColumnConstraints(25);
	            gp.getColumnConstraints().add(column);
	        }
	    });
	    map_height.setOnMouseClicked(e -> {
	    	//System.out.println("height");
	    	//map = new int[map_height.getValue()][map_width.getValue()];

	    	gp.getRowConstraints().removeAll(gp.getRowConstraints());
	    	for(int i = 0; i < map_height.getValue(); i++) {
	    		RowConstraints column = new RowConstraints(25);
	            gp.getRowConstraints().add(column);
	        }
	    });
	    gp.setGridLinesVisible(true);
	    for(int i = 0; i <10; i++) {
            ColumnConstraints column = new ColumnConstraints(25);
            gp.getColumnConstraints().add(column);
        }

        for(int i = 0; i < 10; i++) {
            RowConstraints row = new RowConstraints(25);
            gp.getRowConstraints().add(row);
        }
        gp.setOnMouseClicked(e ->{
        	int x = (int) e.getX() / 25;
        	int y = (int) e.getY() / 25;        	
        	//System.out.println(x + ", " + y);
        	Obstacle seed = new Obstacle();
        	if(forest.isSelected()) {   
        		seed.type = 1;
        		seed.setFill(Color.BROWN);          		
        	}
        	else if(water.isSelected()) {    
        		seed.type = 2;
        		seed.setFill(Color.LIGHTBLUE);        		
        	}
        	else if(stone.isSelected()) {   
        		seed.type = 3;
        		seed.setFill(Color.GREY);        				
        	}
        	else {
        		seed.type = 3;
        		seed.setFill(Color.LIGHTGREEN);
        	}
        	gp.add(seed, x, y);
        });
	    //gp.setStyle("-fx-background-color:lightgreen;");
		map_width.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(10, 100));
		map_height.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(10, 100));
		
		forest.setToggleGroup(land_forms);
		water.setToggleGroup(land_forms);
		stone.setToggleGroup(land_forms);
		toggles.getChildren().addAll(map_width,map_height,forest,water,stone,map_name,save);
		bp.setTop(toggles);
		bp.setCenter(gp);
		map_builder.setContent(bp);
		return map_builder;
	}
	
	public Tab loadOutTab() {
		Tab lt = new Tab();
		GridPane bp = new GridPane();
		HBox top = new HBox();
		HBox center = new HBox();
		VBox info_box = new VBox();
		Label character_info = new Label();
		Button next = new Button("Next");
		Button save = new Button("Save");
		Pane content = new Pane();
		TextField tf = new TextField();
		
		next.setOnAction(e -> {
			top.getChildren().remove(selected_character.load_out);
			selected_character = characters.getNext();
			tf.setText(selected_character.name);
			character_info.setText("HP: " + selected_character.health()
					+ "\nAtt: "+ selected_character.attack()
					+ "\nDef: " + selected_character.defense()
					+ "\nMv: " + selected_character.moves()
					+ "\nRng: " + selected_character.range());
			top.getChildren().add(selected_character.load_out);
		});
		
		save.setOnAction(e -> {
			selected_character.name = tf.getText();
			DBController.updateCharacter(selected_character);
		});
		
		//backpack.setRotate(90);
		center.getChildren().addAll(next,save);
		selected_character = characters.getNext();
		tf.setText(selected_character.name);
		character_info.setText("HP: " + selected_character.health()
				+ "\nAtt: "+ selected_character.attack()
				+ "\nDef: " + selected_character.defense()
				+ "\nMv: " + selected_character.moves()
				+ "\nRng: " + selected_character.range());
		info_box.getChildren().addAll(tf,character_info,center);
		top.getChildren().addAll(info_box,selected_character.load_out);
		
		//center.getChildren().add(backpack);
		top.setPadding(new Insets(0, 0, 0, 60));
		top.setSpacing(25);
		info_box.setSpacing(25);
		content.getChildren().addAll(bp,info);
		bp.add(top,1,1);
		bp.add(backpack,0,1,1,3);
		//bp.add(next,0,4);
		bp.setPadding(new Insets(25));
		lt.setText("Characters");
		lt.setClosable(false);
		lt.setContent(content);
		return lt;
	}
	
	@Override
	public void start(Stage primaryStage) {
		try {
			
			for(Item i : items.values()) {
				if(!i.equipped) {
					//backpack.add(i, 0, 0);
					backpack.addToBackPack(i);
				}
			}
			
			backpack.setPadding(new Insets(5));
			try{
				File[] files = new File(".").listFiles(new FilenameFilter() {	
				    public boolean accept(File dir, String name) {
				        return name.toLowerCase().endsWith(".map");
				    }
				});
				for(File file : files){					
					maps.add(file);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			
			TabPane tp = new TabPane();
			Tab single_player = new Tab("Single Player");
			Tab multi_player = new Tab("Multi Player");
			GridPane multi_form = new GridPane();
			Button host = new Button("Host?");
			Button connect = new Button("Connect?");
			MapChooser multi = new MapChooser(maps);
			MapChooser single = new MapChooser(maps);
			VBox gp = new VBox();
			Spinner<Integer> spin = new Spinner<Integer>();
			Button start = new Button("Start Game");
			final int initialValue = 3;
			
			Label enemies = new Label("Opponents");
			
		    SpinnerValueFactory<Integer> valueFactory =
		    		new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, initialValue);
		    
			tp.getTabs().addAll(single_player,multi_player,loadOutTab(),mapBuilderTab());
			single_player.setClosable(false);
			multi_player.setClosable(false);
			
			host.setOnAction(e -> {
				GameUtil.MULTIPLAYER = true;
				multi_form.add(multi, 0, 5);
				try {
					GameUtil.createGameId();
				    InetAddress addr = Inet4Address.getLocalHost();				    
				    lan_info.setText("You're hosting a game ("+GameUtil.GAME_ID+")."
				    		+ " Provide the below address to your opponent.\n" + addr.getHostAddress());
				    lan = new LANServer();
				    lan.start();
				} catch (UnknownHostException et) {
					et.printStackTrace();
				}
				Button a = new Button("Start Game");
				a.setOnAction(ea -> {
					
					try {
						JSONArray character_array = new JSONArray();
						for(Character c : characters.values()) {														
							character_array.put(c.toJson().put("load_out", c.load_out.toJson()));							
						}
						SendData send_characters = new SendData(new JSONObject()
								.put("request", "character")
								.put("game", GameUtil.GAME_ID)
								.put("characters", character_array));
						send_characters.start();
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					startGame(primaryStage,22,22,0);
				});
				Button b = new Button("Set Map");
				b.setOnAction(eb -> {
					map_loaded = true;
					game_board = multi.getCurrentMap();
					try {
						SendData send = new SendData(new JSONObject()
								.put("game", GameUtil.GAME_ID)
								.put("rows", game_board.length)
								.put("columns", game_board[0].length)
								.put("data", game_board)
								.put("request", "game_board"));
						send.start();
						
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
				});
				multi_form.add(b, 0, 3);
				multi_form.add(a, 0, 8);
			});
			multi_form.add(start_game, 0, 6);
			start_game.setDisable(true);

			connect.setOnAction(e -> {
				GameUtil.MULTIPLAYER = true;

				multi_form.getChildren().remove(multi);
				multi_form.add(map_preview, 0, 5);
				//Label con_info = new Label("Enter your opponents IP");
				TextField t = new TextField();
				Button b = new Button("Connect");
				//multi_form.add(con_info, 0, 2);
				multi_form.add(t, 0, 3);
				multi_form.add(b, 0, 4);
				
				
				start_game.setOnAction(st -> {
					map_loaded = true;
					startGame(primaryStage,22,22,0);
				});
				
				b.setOnAction(btn -> {
					if(!t.getText().isEmpty()){
						opponent_address = t.getText();
						lan = new LANServer();
						lan.start();
						try {
							SendData send = new SendData(new JSONObject()
									.put("request", "connection")
									.put("address", Inet4Address.getLocalHost().getHostAddress()));
									//"{\"request\":\"connection\",\"address\":\""+ Inet4Address.getLocalHost().getHostAddress() +"\"}"
									send.start();
						} catch (JSONException | UnknownHostException e1) {
							e1.printStackTrace();
						}
					}
				});
			});
			multi_form.add(host, 0, 0);
			multi_form.add(connect, 0, 1);
			multi_form.add(lan_info, 0, 2);
			//map_preview.setPadding(new Insets(100));
			
			multi_player.setContent(multi_form);
			
			gp.setPadding(new Insets(20));
			single_player.setContent(gp);
			
			if(me.new_account) {
				Label lb = new Label("Enter Your Name Adventurer!");
				TextField tf = new TextField();
				gp.getChildren().addAll(lb,tf);
			}
		    spin.setValueFactory(valueFactory);
			
			start.setOnAction(e ->{
				GameUtil.MULTIPLAYER = false;
				map_loaded = true;
				game_board = single.getCurrentMap();
				startGame(primaryStage,22,22,spin.getValue());
			});
			gp.getChildren().addAll(enemies,spin,start,single);
			Scene setup_scene = new Scene(tp,650,550);
			primaryStage.setScene(setup_scene);
			primaryStage.show();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void startGame(Stage primaryStage,int width, int height, int players) {
		try {
			backpack.setRotate(0);
			for(int p = 0; p < players; p++){
				Character enemy = new Character(100, 0, 0, 3);
				enemy.name = "Enemy" + p;
				opponents.put(enemy.game_id,enemy);
				enemy.coordinates = new int[]{0,0};
				grid.add(enemy, 0, 0);
				enemy.setTriggers();
				enemy.setFill(Color.RED);
				Item enemy_sword = new Item();
				enemy_sword.name = "Cutlass";
				enemy_sword.attack_bonus = 15;
				enemy_sword.range = 1;
				enemy_sword.item_type = "right";
				Item enemy_armor = new Item();
				enemy_armor.name = "Leather Tunic";
				enemy_armor.defense_bonus = 15;
				enemy_armor.item_type = "body";
				enemy.load_out.equipItem(enemy_armor, "body");
				enemy.load_out.equipItem(enemy_sword, "right");
			}
			characters.myTeam = true;
			
			
			if(!map_loaded) {
				game_board = new int[height][width];
			}
	        for(int r = 0; r < game_board.length; r++) {
	        	for(int c = 0; c < game_board[r].length; c++) {
	        		if(!map_loaded) {
	        			int obj = (int)(Math.random()*10);
	        			game_board[r][c] = obj;
	        		}
	        		Rectangle seed = new Rectangle(1,1, box_size, box_size);
	        		switch(game_board[r][c]) {
	        		case 1:	        			
	        			seed.setFill(Color.BROWN);	        			
	        			break;
	        		case 2:	        			
	        			seed.setFill(Color.LIGHTBLUE);	        			
	        			break;
	        		case 3:
	        			seed.setFill(Color.GREY);
	        			break;
	        		case 10:
	        			seed.setFill(Color.BROWN);
	        			break;
	        		default:
	        			seed.setFill(Color.LIGHTGREEN);
	        			break;	        			
	        		}
	        		grid.add(seed, c,r);
	        		/*
	        		else if(game_board[r][c] > 6){
	        			Rectangle gold = new Rectangle(1,1, box_size, box_size);
	        			gold.setFill(Color.GOLD);
	        			grid.add(gold, c,r);
	        		}
	        		*/
	        	}
	        }
	        grid.setGridLinesVisible(true);	
	        
	        selected_character = characters.getNext();
			
			/*
			Pane end_btn = new Pane();
			Label text = new Label("End Turn");
			Rectangle end_turn = new Rectangle(1,1,80,20);
			end_turn.setFill(Color.ALICEBLUE);
			end_btn.getChildren().addAll(end_turn,text);
			end_btn.setCursor(Cursor.HAND);
			end_btn.setOnMouseClicked(e -> {
				JSONObject json_data = new JSONObject();
				try {
					json_data
						.put("request", "end_turn");
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				System.out.println(json_data);
				SendData send = new SendData(json_data);
				send.start();
			});
			*/
			//Label end_turn = new Label("End Turn");
			action_box.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, CornerRadii.EMPTY, Insets.EMPTY)));
			action_box.getChildren().addAll(character_info,moves,equip,backpack);
			//end_turn.setOnMouseClicked(e -> {
			//	playNpcTurn();
			//});
			//end_turn.setAlignment(Pos.BOTTOM_CENTER);
			action_box.setPrefWidth(100);
			action_box.setPrefHeight(550);
			grid.setLayoutX(100);
			setActiveCharacter();
			setZoom(box_size);
			grid.add(selected_character, 5, 5);
			
			bp.getChildren().addAll(grid,action_box,info, damage_box, game_info);
	        Scene scene = new Scene(bp,board_width+100,board_height);
	        scene.setOnKeyPressed(e -> {
	        	System.out.println(e.getCode());
	        	if(selected_character != null) {
	        		int x = 0,y = 0;
	        		boolean isMove = true;
	        		//y = selected_character.coordinates[1];
	        		//x = selected_character.coordinates[0];
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
		        		isMove = false;
		        		zoom = 2;
		        		box_size = 25;
		        		setZoom(box_size);
		        		for(Character c : characters.values()) {
		        			c.setZoom(box_size);
		        		}
		        		break;
		        	case EQUALS :
		        		isMove = false;
		        		zoom = 1;
		        		box_size = 50;
		        		setZoom(box_size);
		        		for(Character c : characters.values()) {
		        			c.setZoom(box_size);
		        		}
		        		break;
		        	case E :
		        		isMove = false;
		        		if(GameUtil.MULTIPLAYER){
		        			try {
		        				game_info.showInfo(new Label("Opponents Turn"), 325, 225);
								SendData send = new SendData(new JSONObject()
										.put("request", "end_turn")
										.put("game", GameUtil.GAME_ID));
								send.start();
							} catch (JSONException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
		        		}
		        		else{
			        		playNpcTurn();
		        		}
		        		break;
		        	case SPACE :
		        		isMove = false;
		        		setActiveCharacter();
		        		break;
					default:
						y = selected_character.coordinates[1];
		        		x = selected_character.coordinates[0];
						break;
		        	}
		        	//System.out.println(spaceOccupied(x,y));
		        	if(isMove && !spaceOccupied(x,y) && selected_character.move1Space()) {
		        		grid.getChildren().remove(selected_character);
			        	grid.add(selected_character, x, y);
			        	//game_board.get(y).put(x, 10);
			        	selected_character.coordinates = new int[] {x,y};
		        		moves.setText("Moves: " + (selected_character.moves() - selected_character.has_moved));
		        		centerScreen();
		        		if(GameUtil.MULTIPLAYER) {
		        			try {
								SendData send = new SendData(new JSONObject()
										.put("game", GameUtil.GAME_ID)
										.put("request", "move")
										.put("character_id", selected_character.game_id)
										.put("x", x)
										.put("y", y));
								send.start();
							} catch (JSONException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
		        		}
		        	}
		        	
	        	}
	        	
	        });
	        
	        bp.setStyle("-fx-background-color:darkgrey;");
	        //grid.setStyle("-fx-background-color:lightgreen;");
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void playNpcTurn(){
		for(Character c : opponents.values()){
			selected_character = c;
			while(c.move1Space()){					
				if(npcMove(c)){					
					break;
				}							
			}
			c.has_moved = 0;
		}
		for(Character c : characters.values()){
			c.has_attacked = false;
			c.has_moved = 0;
		}
		setActiveCharacter();
	}
	
	public static void renderPreviewMap(int[][] map) {
		map_preview.getChildren().removeAll(map_preview.getChildren());
		map_preview.getColumnConstraints().removeAll(map_preview.getColumnConstraints());
		map_preview.getRowConstraints().removeAll(map_preview.getRowConstraints());
		//map_preview = new GridPane();
		
		for(int i = 0; i < map[0].length; i++) {
            ColumnConstraints column = new ColumnConstraints(10);
            map_preview.getColumnConstraints().add(column);
        }

        for(int i = 0; i < map.length; i++) {
            RowConstraints row = new RowConstraints(10);
            map_preview.getRowConstraints().add(row);
        }
		
        for(int r = 0; r < map.length; r++) {
        	for(int c = 0; c < map[r].length; c++) {
        		
        		Rectangle seed = new Rectangle(1,1, 10, 10);
        		switch(map[r][c]) {
        		case 1:	        			
        			seed.setFill(Color.BROWN);	        			
        			break;
        		case 2:	        			
        			seed.setFill(Color.LIGHTBLUE);	        			
        			break;
        		case 3:
        			seed.setFill(Color.GREY);
        			break;
        		case 10:
        			seed.setFill(Color.BROWN);
        			break;
        		default:
        			seed.setFill(Color.LIGHTGREEN);
        			break;	        			
        		}
        		map_preview.add(seed, c,r);
        		/*
        		else if(game_board[r][c] > 6){
        			Rectangle gold = new Rectangle(1,1, box_size, box_size);
        			gold.setFill(Color.GOLD);
        			grid.add(gold, c,r);
        		}
        		*/
        	}
        }
        //map_preview.setGridLinesVisible(true);	
        
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
		
		selected_character = characters.getNext();
		
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
		if(selected_character.range() < distance)
			return false;
		int r,c;
		for(r=0;r<game_board.length;r++) {
			for(c=0;c<game_board[r].length;c++) {	
				//if(game_board[r][c] > 8){		
				switch(game_board[r][c]){
				case 1:				
				case 3:
				case 10:
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
	
	public static double distance(int[] a, int[] b) {
		double distance = Math.sqrt(
				Math.pow((a[1] - b[1]), 2) 
				+ Math.pow((a[0] - b[0]), 2));
		return distance;//round(distance,0);
	}
		
	public void centerScreen() {
		grid.setLayoutX(((5*zoom) - selected_character.coordinates[0]) * box_size + 100);
		grid.setLayoutY(((5*zoom) - selected_character.coordinates[1]) * box_size);
	}
	
	public boolean spaceOccupied(int x, int y) {
		try{			
			switch(game_board[y][x]){			
			case 1:
				return true;
			case 2:
				return true;
			case 3:
				return true;
			case 10:
				return true;
			case 12:
				game_board[y][x] = 0;
				System.out.println("Item");
				List<Item> pick_up = new ArrayList<Item>();
				for(Node n : grid.getChildren()){					
					try{
						if(n != null && GridPane.getRowIndex(n) == y && GridPane.getColumnIndex(n) == x){							
							Item item = (Item) n;
							pick_up.add(item);
						}	
					}catch(Exception e){						
						continue;
					}
				}				
				for(Item i : pick_up){					
					grid.getChildren().remove(i);
					backpack.addToBackPack(i);
				}				
				return false;			
			default:
				for(Character c : characters.values()) {
					if(c.coordinates[0] == x && c.coordinates[1] == y)
						return true;
				}
				for(Character c : opponents.values()) {
					if(c.coordinates[0] == x && c.coordinates[1] == y)
						return true;
				}
			}			
		}catch(Exception e){			
			return true;
		}
		return false;
	}
	
	public boolean npcMove(Character npc){
		int[] left = new int[]{npc.coordinates[0]-1,npc.coordinates[1]};
		int[] right = new int[]{npc.coordinates[0]+1,npc.coordinates[1]};
		int[] up = new int[]{npc.coordinates[0],npc.coordinates[1]-1};
		int[] down = new int[]{npc.coordinates[0],npc.coordinates[1]+1};
		int[][] moves = new int[][]{left,right,up,down};
		
		double least_distance = 20;
		int move = 4;
		Character target = null;
		for(Character c : characters.values()){
			for(int i = 0; i < 4; i++){
				double distance = distance(moves[i],c.coordinates);
				if(distance < least_distance && !spaceOccupied(moves[i][0],moves[i][1])){
					least_distance = distance;
					move = i;
					target = c;
				}
			}
		}
		if(move == 4){
			move = (int) Math.random()*4;
			//System.out.println(move);
			//move = random
			//return false;
		}
		if(moves[move][0] == -1 || moves[move][1] == -1)
			return false;
		grid.getChildren().remove(npc);
    	grid.add(npc, moves[move][0], moves[move][1]);
		npc.coordinates = new int[]{moves[move][0],moves[move][1]};
		if(inRange(target) && target != null){
			attack(npc,target);	
			System.out.println(least_distance);
			return true;
		}
		return false;
	}
	
	public static int attack(Character attacker, Character defender){				
		attacker.has_attacked = true;
		int roll = (int) (Math.random() * 20);
		int damage = roll + attacker.attack();		
		defender.damage_taken += (damage < defender.defense()) ? 0 : damage-defender.defense();
		System.out.println(attacker.name + " has attacked " + defender.name + " and dealt " +
				((damage < defender.defense()) ? 0 : damage-defender.defense()) + " damage!");
		if(defender.damage_taken >= defender.health()) {			
			grid.getChildren().remove(defender);
			Main.characters.remove(defender.game_id);
			Main.opponents.remove(defender.game_id);
			defender.load_out.dropItems(defender.coordinates);
			game_board[defender.coordinates[1]][defender.coordinates[0]] = 12;
		}		
		return (damage < defender.defense()) ? 0 : damage-defender.defense();
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
