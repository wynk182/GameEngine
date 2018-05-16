package application;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.paint.Color;

public class RecieveData extends Service<Void>{
	
	JSONObject data;
	
	public RecieveData(JSONObject data){
		this.data = data;
	}

	@Override
	protected Task<Void> createTask() {
		return new Task<Void>() {    
			@Override
			protected Void call() throws Exception {
				Platform.runLater(new Runnable(){
					@Override
					public void run() {
						try {
							switch(data.getString("request")){
							case "character":
								if(data.getString("game").equals(GameUtil.GAME_ID)) {
									for(int i = 0; i < data.getJSONArray("characters").length(); i++){
										JSONObject character = data.getJSONArray("characters").getJSONObject(i);
										if(Main.opponents.containsKey(character.get("character_id"))) {
											Character c = Main.opponents.get(character.get("character_id"));
											Main.grid.getChildren().remove(c);
											Main.grid.add(c, character.getJSONArray("coordinates").getInt(0),
													character.getJSONArray("coordinates").getInt(1));
											break;
										}
										else {
											Character c = Character.fromJson(character);
											c.load_out = LoadOut.fromJson(character.getJSONObject("load_out"));
											c.setFill(Color.RED);
											Main.opponents.put(c.game_id, c);
											Main.grid.add(c, c.coordinates[0], c.coordinates[1]);
										}
									}									
								}
								break;
							case "move":
								if(data.getString("game").equals(GameUtil.GAME_ID)) {
									Character c = Main.opponents.get(data.get("character_id"));
									c.coordinates = new int[] {data.getInt("x"),data.getInt("y")};
									Main.grid.getChildren().remove(c);
									Main.grid.add(c, c.coordinates[0], c.coordinates[1]);
									
								}
								break;
							case "attack":
								if(data.getString("game").equals(GameUtil.GAME_ID)) {
									Character c = Main.characters.get(data.getString("character_id"));
									c.damage_taken += data.getInt("damage");
									if(c.health() <= c.damage_taken) {
										Main.characters.remove(c.game_id);
										Main.grid.getChildren().remove(c);
										c.load_out.dropItems(c.coordinates);
									}
								}
								break;
							case "load_out":
								if(data.getString("game").equals(GameUtil.GAME_ID)) {
									Character c = Main.opponents.get(data.getString("character_id"));
									c.load_out = LoadOut.fromJson(data);
								}
								break;
							case "connection":									
								Main.opponent_address = data.getString("address");
								Main.lan_info.setText("Recieved Connection from: " + Main.opponent_address);
								//System.out.println(GameUtil.GAME_ID);
								SendData send = new SendData(new JSONObject()
										.put("game", GameUtil.GAME_ID)
										.put("request", "game_id"));
								send.start();
								
								break;	
							case "game_id":
								GameUtil.setGameId(data.getString("game"));
								Main.lan_info.setText("recieved game id: " + GameUtil.GAME_ID);
								//System.out.println(GameUtil.GAME_ID);
								break;
							case "end_turn":
								if(data.getString("game").equals(GameUtil.GAME_ID)) {
									Main.game_info.hideInfo();
									for(Character c : Main.characters.values()){
										c.has_attacked = false;
										c.has_moved = 0;
									}
									//Main.selected_character = Main.characters.getNext();
								}
								//set next character and 
								break;
							case "ready":
								if(data.getString("game").equals(GameUtil.GAME_ID)) {
									
								}
								//opponent is ready to start game
								break;
							case "game_start":
								if(data.getString("game").equals(GameUtil.GAME_ID)) {
									
								}
								//host has started game opponents game will now start if they are ready.
								break;
							case "game_board":
								if(data.getString("game").equals(GameUtil.GAME_ID)) {
									int rows = data.getInt("rows");
									int columns = data.getInt("columns");
									int [][] board = new int[rows][columns];
									for(int i = 0; i < rows;i++){
										for(int j = 0; j < columns;j++){
											board[i][j] = data.getJSONArray("data").getJSONArray(i).getInt(j);
										}
									}
									Main.game_board = board;
									Main.renderPreviewMap(board);
									JSONObject json_data = new JSONObject();
									JSONArray character_array = new JSONArray();
									for(Character c : Main.characters.values()) {
										
										character_array.put(c.toJson().put("load_out", c.load_out.toJson()));
									}
									json_data.put("characters", character_array)
										.put("request", "character")
										.put("game", GameUtil.GAME_ID);
									SendData send_characters = new SendData(json_data);
									send_characters.start();
									Main.start_game.setDisable(false);
								}
								break;
							default:
								
								break;
							}	
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}							
					}            			
        		});
				return null;
			}
		};
	}

}
