package application;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Character extends Rectangle{
	
	public int localDBID = 0;
	public LoadOut load_out = new LoadOut();
	public String name = "Buster";
	public String gender = "Male";
	public int damage_taken;
	private int health;
	private int attack;
	private int defense;
	private int moves;	
	private int range =0;
	public int has_moved = 0;
	boolean has_attacked = false;
	String game_id;
	
	public int[] coordinates;
	
	public Character(int h, int a, int d, int m) {	
		this.game_id = GameUtil.getSaltString(5);
		//System.out.println(this.game_id);
		this.health = h;
		this.attack = a;
		this.defense = d;
		this.moves = m;		
		this.setWidth(Main.box_size);
		this.setHeight(Main.box_size);
		this.setFill(Color.BLUE);
		//this.setCursor(new ImageCursor(Main.CURSOR));
		this.setCursor(Cursor.CROSSHAIR);
		//this.setStyle("-fx-cursor: url('cursor.png');");
		this.setOnMouseClicked(e -> {			
			Character attacker = Main.selected_character;			
			if(attacker != null && !attacker.has_attacked && Main.inRange(this) && !attacker.equals(this)) {
				
				int damage = Main.attack(attacker, this);
				Label l = new Label("" + damage);
				StackPane att = new StackPane();
				att.getChildren().addAll(new ImageView(Main.damage), l);
				Main.damage_box.showInfo(att,e.getSceneX()-50,e.getSceneY()-10);
				DamageTask dt = new DamageTask(Main.damage_box, 1);
				dt.start();
				if(GameUtil.MULTIPLAYER) {
					try {
						SendData send = new SendData(new JSONObject()
								.put("game", GameUtil.GAME_ID)
								.put("request", "attack")
								.put("character_id", this.game_id)
								.put("damage", damage));
						send.start();
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}			
		});
	}
	
	public void setZoom(int size) {
		this.setWidth(size);
		this.setHeight(size);
	}
	
	public int attack() {
		int power = this.attack;
		for(Node i : load_out.getChildren()){			
			power += ((Item) i).attack_bonus;			
		}
		return power;
	}
	public int defense() {
		int power = this.defense;
		for(Node i : load_out.getChildren()){			
			power += ((Item) i).defense_bonus;			
		}
		return power;
	}
	public int health() {
		int power = this.health;
		for(Node i : load_out.getChildren()){			
			power += ((Item) i).health_bonus;			
		}
		return power;
	}
	public int moves() {
		int power = this.moves;
		for(Node i : load_out.getChildren()){			
			power += ((Item) i).move_bonus;			
		}
		return power;
	}
	public int range(){
		int power = range;
		for(Node i : load_out.getChildren()){			
			power += ((Item) i).range;			
		}
		return power;
	}
	/*
	public void setLoadOut(LoadOut load_out){
		this.load_out = load_out;
		this.current_health = health();
		for(Item i : load_out.items) {
			if(i != null)
				System.out.println("Wearing: " + i.name);
		}
	}
	*/
	public void setTriggers(){
		this.setOnMouseEntered(e -> {
			Label l = new Label();
			String info = this.name 
					+ "\nHealth: " + (this.health() - damage_taken)
					+ "\nAttack: " + this.attack()
					+ "\nDefense: " + this.defense();
			l = new Label(info);
			Main.info.showInfo(l, e.getSceneX(), e.getSceneY());			
		});
		this.setOnMouseExited(e -> {
			Main.info.hideInfo();
		});
	}
	
	public boolean move1Space() {
		if(has_moved >= moves()) {			
			//has_moved = 0;
			return false;
		}
		has_moved++;
		return true;
	}
	
	public JSONObject toJson() {
		JSONObject character = new JSONObject();
		try {
			character
				//.put("game", GameUtil.GAME_ID)
				//.put("request", "character")
				.put("character_id", this.game_id)
				.put("name", this.name)
				.put("damage_taken", this.damage_taken)
				.put("health", health())
				.put("attack", attack())
				.put("defense", defense())
				.put("coordinates", new JSONArray()
						.put(this.coordinates[0])
						.put(this.coordinates[1]));
			/*
			JSONArray json_loadout = new JSONArray();
			for(Node n : this.load_out.getChildren()) {
				Item node = (Item) n;
				JSONObject json_item = new JSONObject();
				json_item
					.put("name", node.name)
					.put("attack_bonus", node.attack_bonus)
					.put("defense_bonus", node.defense_bonus)
					.put("move_bonus", node.move_bonus)
					.put("health_bonus", node.health_bonus)
					.put("worth", node.worth)
					.put("range", node.range);
				json_loadout.put(json_item);
			}
			character.put("load_out", json_loadout);
			*/
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(character);
		return character;
	}
	
	public static Character fromJson(JSONObject json){
		try {
			Character c = new Character(
					json.getInt("health"),
					json.getInt("attack"),
					json.getInt("defense"),
					0
					);
			c.damage_taken = json.getInt("damage_taken");
			int[] coordinates = new int[2];
			for(int i = 0; i < json.getJSONArray("coordinates").length();i++){
				coordinates[i] = json.getJSONArray("coordinates").getInt(i);
			}
			c.coordinates = coordinates;
			c.load_out = new LoadOut();
			c.name = json.getString("name");
			c.game_id = json.getString("character_id");
			c.setTriggers();
			return c;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}
