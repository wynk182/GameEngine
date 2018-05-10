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
									Character c = Character.fromJson(json_request);
									Main.opponents.add(c);
									Main.grid.add(c, c.coordinates[0], c.coordinates[1]);
									break;
								case "connection":									
									Main.opponent_address = json_request.getString("address");
									Main.lan_info.setText("Recieved Connection from: " + Main.opponent_address);
									break;	
								case "end_turn":
									//set next character and 
									break;
								case "ready":
									//opponent is ready to start game
									break;
								case "game_start":
									//host has started game opponents game will now start if they are ready.
									break;
								case "game_board":
									int rows = json_request.getInt("rows");
									int columns = json_request.getInt("columns");
									int [][] board = new int[rows][columns];
									for(int i = 0; i < rows;i++){
										for(int j = 0; j < columns;j++){
											board[i][j] = json_request.getJSONArray("data").getJSONArray(i).getInt(j);
										}
									}
									//Main.game_board = board;
									Main.renderPreviewMap(board);
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
