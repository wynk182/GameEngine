package application;

import java.util.HashMap;

public class LoadOut {
	Item right_hand;
	Item left_hand;
	Item body;
	Item feet;
	
	HashMap<String, Item> items = new HashMap<String,Item>();
	public LoadOut(){
		items.put("right_hand", right_hand);
		items.put("left_hand", left_hand);
		items.put("body", body);
		items.put("feet", feet);		
	}
}
