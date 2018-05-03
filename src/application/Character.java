package application;

import java.io.File;

import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.image.Image;
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
	private int range =1;
	public int has_moved = 0;
	boolean has_attacked = false;
	public CharacterClass spec = Specialization.Peasant.getSpec();
	Weapon weapon = Weapons.None.getWeapon();
	//Rectangle block = new Rectangle(1,1, 50, 50);
	
	public int[] coordinates;
	
	public Character(int h, int a, int d, int m) {	
		this.health = h;
		this.attack = a;
		this.defense = d;
		this.moves = m;		
		this.setWidth(35);
		this.setHeight(35);
		this.setFill(Color.BLUE);
		this.setCursor(new ImageCursor(new Image(new File("cursor.png").toURI().toString())));
		this.setOnMouseClicked(e -> {			
			Character attacker = Main.selected_character;
			if(attacker != null && !attacker.has_attacked && Main.inRange(this)) {
				attacker.has_attacked = true;
				int damage = (int) (Math.random() * 20) + attacker.attack;
				
				this.damage_taken += (damage < defense()) ? 0 : damage-defense();
				
				if(this.damage_taken >= health()) {
					this.setVisible(false);
					Main.characters.remove(this);
					System.out.println(this.name + " takes " + damage + " damage from " 
							+ attacker.name + " and has died");
				}
				else {
					System.out.println(this.name + " takes " + damage + " damage from " 
							+ attacker.name + " and defends "+ defense() +" and now has " + (health() - this.damage_taken) + " health");
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
	
	public void equipItem(Item item, String slot){
		
	}
	public void removeItem(Item item) {
		this.load_out.removeItem(item);
	}
	
	public boolean move1Space() {
		if(has_moved++ >= moves()) {
			//has_moved = 0;
			return false;
		}
		return true;
	}
	
	public void equipWeapon(Weapons weapon) {
		this.weapon = weapon.getWeapon();
		this.attack += this.weapon.attack_bonus;
		this.defense += this.weapon.defense_bonus;
	}
	
	/*
	public boolean inRange(Character attacker, Character defender) {
		int range = attacker.weapon.range;
		//System.out.println(range);
		if(attacker.coordinates[0] == defender.coordinates[0]) {
			if(attacker.coordinates[1] > defender.coordinates[1]) {
				if(attacker.coordinates[1] - defender.coordinates[1] <= range) {
					return true;
				}
			}
			else {
				if(defender.coordinates[1] - attacker.coordinates[1] <= range) {
					return true;
				}
			}
		}
		else if(attacker.coordinates[1] == defender.coordinates[1]) {
			if(attacker.coordinates[0] > defender.coordinates[0]) {
				if(attacker.coordinates[0] - defender.coordinates[0] <= range) {
					return true;
				}
			}
			else {
				if(defender.coordinates[0] - attacker.coordinates[0] <= range) {
					return true;
				}
			}
		}
		return false;
	}
	
	
	public Rectangle drawCharacter() {
		block.setFill(spec.color);
		//block.setStyle("-fx-cursor: uri('cursor.png');");
		block.setCursor(new ImageCursor(new Image(new File("cursor.png").toURI().toString())));
		return block;
	}
	*/
	
}
