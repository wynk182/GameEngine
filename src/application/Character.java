package application;

import java.io.File;

import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Character extends Rectangle{
	
	public LoadOut load_out;
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
	
	public int[] coordinates;
	
	public Character(int h, int a, int d, int m) {	
		this.health = h;
		this.attack = a;
		this.defense = d;
		this.moves = m;		
		this.setWidth(50);
		this.setHeight(50);
		this.setFill(Color.BLUE);
		this.setCursor(new ImageCursor(new Image(new File("cursor.png").toURI().toString())));
		this.setOnMouseClicked(e -> {			
			Character attacker = Main.selected_character;			
			if(attacker != null && !attacker.has_attacked && Main.inRange(this) && !attacker.equals(this)) {
				attacker.has_attacked = true;
				int roll = (int) (Math.random() * 20);
				int damage = roll + attacker.attack();
				Label l = new Label("" + damage);
				StackPane att = new StackPane();
				att.getChildren().addAll(new ImageView(Main.damage), l);
				Main.damage_box.showInfo(att,e.getSceneX()-50,e.getSceneY()-10);
				DamageTask dt = new DamageTask(Main.damage_box, 1);
				dt.start();
				//Platform.runLater(new DamageTask(Main.damage_box, 5));
				this.damage_taken += (damage < defense()) ? 0 : damage-defense();
				if(this.damage_taken >= health()) {
					this.setVisible(false);
					Main.characters.remove(this);	
				}
			}			
		});
		//System.out.println(this.name + " is a " + spec + " with " + this.health + " health.");
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
		if(has_moved++ >= moves()) {
			//has_moved = 0;
			return false;
		}
		return true;
	}
	
	
}
