package application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LoadOut {
	Item right_hand;
	Item left_hand;
	Item body;
	Item feet;
	Item head;
	
	//HashMap<String, Item> items = new HashMap<String,Item>();
	List<Item> items = new ArrayList<Item>();
	public LoadOut(Item r, Item l, Item h, Item b, Item f){
		if(r != null)
			r.equipped=true;
		if(l != null)
			l.equipped=true;
		if(h != null)
			h.equipped=true;
		if(b != null)
			b.equipped=true;
		if(f != null)
			f.equipped=true;
		
		right_hand = r;
		left_hand = l;
		body = b;
		feet = f;
		head = h;
		items.add(r);
		items.add(l);
		items.add(b);
		items.add(f);
		items.add(h);
	}
	public void removeItem(Item item) {
		items.remove(item);
		if(item.equals(right_hand)) {
			right_hand = null;
		}
		if(item.equals(left_hand)) {
			left_hand = null;
		}
		if(item.equals(head)) {
			head = null;
		}
		if(item.equals(body)) {
			body = null;
		}
		if(item.equals(feet)) {
			feet = null;
		}
	}
}
