package application;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class LANServer extends Service<Void>{
	
	public LANServer() {
		
	}

	@Override
	protected Task<Void> createTask() {
		return new Task<Void>() {           
            @Override
            protected Void call() throws Exception {
            	//Thread.sleep(time *1000);
            	while(true) {
					ServerSocket s = new ServerSocket(1337);
					Socket game = s.accept();
					BufferedReader in = new BufferedReader(new InputStreamReader(
                            game.getInputStream()));
					String line = "";
					while((line = in.readLine()) != null) {
						System.out.println(line);
					}
						
					game.close();
					s.close();
					
				}
            }
        };
	}

}
