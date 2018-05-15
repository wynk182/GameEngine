package application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
				BackgroundPosition.DEFAULT,
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
			switch (place){
			case "right":
				this.right_hand = item;
				break;
			case "left":
				this.left_hand = item;
				break;
			case "head":
				this.head = item;
				break;
			case "body":
				this.body = item;
				break;
			case "feet":
				this.feet = item;
				break;
			}
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
	
	public JSONObject toJson(){
		JSONObject load_out = null;
		try {
			load_out = new JSONObject()
					.put("request", "load_out")
					.put("game", GameUtil.GAME_ID);
			JSONArray items = new JSONArray();
			for(Node n : this.getChildren()){
				Item i = (Item) n;
				items.put(new JSONObject()
						.put("type", i.item_type)
						.put("name", i.name)
						.put("att", i.attack_bonus)
						.put("def", i.defense_bonus)
						.put("mv", i.move_bonus)
						.put("hp", i.health_bonus)
						.put("w", i.worth)
						.put("rng", i.range));
			}
			load_out.put("items", items);		//.put("", value);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return load_out;
	}
	
	public static LoadOut fromJson(JSONObject json){
		LoadOut load_out = new LoadOut();
		try {
			JSONArray items = json.getJSONArray("items");
			for(int i = 0; i < items.length(); i++){
				JSONObject index = items.getJSONObject(i);
				Item item = new Item();
				item.name = index.getString("name");
				item.item_type = index.getString("type");
				item.attack_bonus = index.getInt("att");
				item.defense_bonus = index.getInt("def");
				item.move_bonus = index.getInt("mv");
				item.health_bonus = index.getInt("hp");
				item.worth = index.getInt("w");
				item.range = index.getInt("rng");
				load_out.equipItem(item, item.item_type);
				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return load_out;
		
	}
	
	public void removeItem(Item item) {
		switch (item.item_type){
		case "right":
			this.right_hand = null;
			break;
		case "left":
			this.left_hand = null;
			break;
		case "head":
			this.head = null;
			break;
		case "body":
			this.body = null;
			break;
		case "feet":
			this.feet = null;
			break;
		}
		this.getChildren().remove(item);
		
	}
	
}
