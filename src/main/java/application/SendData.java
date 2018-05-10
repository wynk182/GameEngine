package application;

import java.io.PrintWriter;
import java.net.Socket;
import org.json.JSONObject;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class SendData extends Service<Void> {
	
	JSONObject json_data;
	int game_port = 1337;
	
	public SendData(JSONObject data){
		json_data = data;
	}

	@Override
	protected Task<Void> createTask() {
		return new Task<Void>() {    
			@Override
			protected Void call() throws Exception {
				Socket s = new Socket(Main.opponent_address, game_port);
				PrintWriter out = new PrintWriter(s.getOutputStream());
        		out.println("Content-Length: " + json_data.toString().length());
				out.write(json_data.toString());
        		out.close();        		
        		s.close();
				return null;                	
            }
		};
		
	}

}
