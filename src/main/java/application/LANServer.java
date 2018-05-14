package application;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class LANServer extends Service<Void>{
	
	JSONObject json_request = new JSONObject();

	@Override
	protected Task<Void> createTask() {
		return new Task<Void>() {           
            @Override
            protected Void call() throws Exception {
            	//Thread.sleep(time *1000);
            	while(true) {
            		ServerSocket s = new ServerSocket(1337);
            		Socket game = s.accept();
            		BufferedReader in = new BufferedReader(new InputStreamReader(game.getInputStream()));
            		
            		String line = "";
            		int content_length = 0;
            		while((line=in.readLine())!=null){
            			System.out.println(line);
            			if(line.toLowerCase().contains("content-length")){
            				content_length = Integer.parseInt(line.split(": ")[1]);
            				break;
            			}
            			
            		}  
            		System.out.println(content_length);
            		String request = "";
            		String response = "";
            		for(int i = 0; i <= content_length+1; i++){
            			request += (char) in.read();
            		}
            		try{
            			json_request = new JSONObject(request);
            			System.out.println(json_request);
            			response = "{\"response\":true}";
            		}catch(Exception e){
            			response = "{\"response\":false}";
            		}
            		System.out.println();
            		PrintWriter out = new PrintWriter(game.getOutputStream());
            		out.println("HTTP/1.1 200 OK");
            		out.println("Content-Type: application/json");
            		//out.println("\r\n");
            		out.println("Content-Length: 20");
            		out.println("\r\n");
            		out.println(response);
            		out.close();
            		game.close();
            		s.close();
            		
            		Platform.runLater(new Runnable(){
						@Override
						public void run() {
							try {
								switch(json_request.getString("request")){
								case "character":
									if(json_request.getString("game").equals(GameUtil.GAME_ID)) {
										if(Main.opponents.containsKey(json_request.get("character_id"))) {
											Character c = Main.opponents.get(json_request.get("character_id"));
											Main.grid.getChildren().remove(c);
											Main.grid.add(c, json_request.getJSONArray("coordinates").getInt(0),
													json_request.getJSONArray("coordinates").getInt(1));
											break;
										}
										else {
											Character c = Character.fromJson(json_request);
											Main.opponents.put(c.game_id, c);
											Main.grid.add(c, c.coordinates[0], c.coordinates[1]);
										}
									}
									break;
								case "move":
									if(json_request.getString("game").equals(GameUtil.GAME_ID)) {
										Character c = Main.opponents.get(json_request.get("character_id"));
										c.coordinates = new int[] {json_request.getInt("x"),json_request.getInt("y")};
										Main.grid.getChildren().remove(c);
										Main.grid.add(c, c.coordinates[0], c.coordinates[1]);
										
									}
									break;
								case "attack":
									if(json_request.getString("game").equals(GameUtil.GAME_ID)) {
										Character c = Main.characters.get(json_request.getString("character_id"));
										c.damage_taken += json_request.getInt("damage");
										if(c.health() <= c.damage_taken) {
											Main.characters.remove(c.game_id);
											Main.grid.getChildren().remove(c);
											c.load_out.dropItems(c.coordinates);
										}
									}
									break;
								case "load_out":
									
									break;
								case "connection":									
									Main.opponent_address = json_request.getString("address");
									Main.lan_info.setText("Recieved Connection from: " + Main.opponent_address);
									//System.out.println(GameUtil.GAME_ID);
									SendData send = new SendData(new JSONObject()
											.put("game", GameUtil.GAME_ID)
											.put("request", "game_id"));
									send.start();
									
									break;	
								case "game_id":
									GameUtil.setGameId(json_request.getString("game"));
									Main.lan_info.setText("recieved game id: " + GameUtil.GAME_ID);
									//System.out.println(GameUtil.GAME_ID);
									break;
								case "end_turn":
									if(json_request.getString("game").equals(GameUtil.GAME_ID)) {
										
									}
									//set next character and 
									break;
								case "ready":
									if(json_request.getString("game").equals(GameUtil.GAME_ID)) {
										
									}
									//opponent is ready to start game
									break;
								case "game_start":
									if(json_request.getString("game").equals(GameUtil.GAME_ID)) {
										
									}
									//host has started game opponents game will now start if they are ready.
									break;
								case "game_board":
									if(json_request.getString("game").equals(GameUtil.GAME_ID)) {
										int rows = json_request.getInt("rows");
										int columns = json_request.getInt("columns");
										int [][] board = new int[rows][columns];
										for(int i = 0; i < rows;i++){
											for(int j = 0; j < columns;j++){
												board[i][j] = json_request.getJSONArray("data").getJSONArray(i).getInt(j);
											}
										}
										Main.game_board = board;
										Main.renderPreviewMap(board);
										for(Character c : Main.characters.values()) {
											//JSONObject character_data = new JSONObject();
											//character_data.put("character", c.toJson());
											//character_data.put("load_out", c.load_out.toJson());
											SendData send_character = new SendData(c.toJson());
											send_character.start();
											SendData send_load_out = new SendData(c.load_out.toJson().put("caracter_id", c.game_id));
											send_load_out.start();
										}
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
            	}
            }
        };
	}
}
