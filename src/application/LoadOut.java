package application;

import java.util.HashMap;

public class LoadOut {
	Item right_hand;
	Item left_hand;
	Item body;
	Item feet;
	Item head;
	
	HashMap<String, Item> items = new HashMap<String,Item>();
	public LoadOut(Item r, Item l, Item h, Item b, Item f){
		right_hand = r;
		left_hand = l;
		body = b;
		feet = f;
		head = h;
		items.put("right_hand", r);
		items.put("left_hand", l);
		items.put("body", b);
		items.put("feet", f);
		items.put("head", h);
	}
}
