package application;

import java.net.ServerSocket;
import java.net.Socket;

import org.json.JSONObject;

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
            		RecieveData recieve = new RecieveData(json_request, game);
            		recieve.start();
            		//game.close();
            		s.close();  		
            	}
            }
        };
	}
}
