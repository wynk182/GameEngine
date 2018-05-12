package application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javafx.scene.Node;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class LoadOut extends GridPane{
	Item right_hand;
	Item left_hand;
	Item body;
	Item feet;
	Item head;
	int[] r = new int[]{0,1};
	int[] l = new int[]{2,1};
	int[] h = new int[]{1,0};
	int[] b = new int[]{1,1};
	int[] f = new int[]{1,2};
	
	HashMap<String, int[]> slots = new HashMap<String,int[]>();
	//List<Item> items = new ArrayList<Item>();
	
	public LoadOut(){
		//this.setStyle("-fx-background-image: url('armorstand.png');");
		this.setBackground(new Background(new BackgroundImage(Main.armor_stand,
				BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT,
				BackgroundPosition.CENTER,
				null
				)));
		slots.put("right", r);
		slots.put("left", l);
		slots.put("head", h);
		slots.put("body", b);
		slots.put("feet", f);
		
		//this.setGridLinesVisible(true);		
		for(int i = 0; i < 3; i++) {
            ColumnConstraints column = new ColumnConstraints(25);
            this.getColumnConstraints().add(column);
        }

        for(int i = 0; i < 3; i++) {
            RowConstraints row = new RowConstraints(25);
            this.getRowConstraints().add(row);
        }
	}
	
	public boolean equipItem(Item item, String place){
		int[] slot = slots.get(place);
		for(Node n : this.getChildren()){
			if(GridPane.getColumnIndex(n) == slot[0] && GridPane.getRowIndex(n) == slot[1]){
				return false;
			}
		}
		if(item != null){
			item.equipped = true;
			this.add(item,slot[0] , slot[1]);
		}	
		return true;
	}
	
	public void dropItems(int[] where) {
		List<Item> dropped = new ArrayList<Item>();
		for(Node n : this.getChildren()) {
			Item item = (Item) n;
			item.equipped = false;
			dropped.add(item);
			//Main.grid.add(item,where[0],where[1]);
		}
		for(Item item : dropped) {
			this.getChildren().remove(item);
			Main.grid.add(item,where[0],where[1]);
		}
		//this.getChildren().removeAll(this.getChildren());
	}
	
	/*
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
	*/
	public void removeItem(Item item) {
		this.getChildren().remove(item);
		//items.remove(item);
		/*
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
		*/
	}
	
}
