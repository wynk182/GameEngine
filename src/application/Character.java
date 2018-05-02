package application;

import javafx.scene.shape.Rectangle;

public class Character{

	public String name = "Buster";
	public String gender = "Male";
	public int health = 100;
	public int attack;
	public int defense;
	public int moves = 2;
	public int has_moved = 0;
	boolean has_attacked = false;
	public CharacterClass spec = Specialization.Peasant.getSpec();
	Weapon weapon = Weapons.None.getWeapon();
	Rectangle block = new Rectangle(1,1, 50, 50);
	
	public int[] coordinates;
	
	public Character(Specialization spec, int[] start, String name) {
		this.name = name;
		this.coordinates = start;
		this.spec = spec.getSpec();
		this.moves += this.spec.move_bonus;
		this.attack += this.spec.attack_bonus;
		this.defense += this.spec.defense_bonus;
		this.health += this.spec.health_bonus;
		block.setOnMouseClicked(e -> {	
			//block = new Rectangle(1,1, 50, 50);
			//Main.inRange(this);
			Character attacker = Main.selected_character;
			if(attacker != null && !attacker.has_attacked && Main.inRange(this)) {
				attacker.has_attacked = true;
				int damage = (int) (Math.random() * 20) + attacker.attack;
				
				this.health -= (damage < defense) ? 0 : damage-defense;
				
				if(this.health < 0) {
					this.block.setVisible(false);
					Main.characters.remove(this);
					System.out.println(this.name + " takes " + damage + " damage from " 
							+ attacker.name + " and has died");
				}
				else {
					System.out.println(this.name + " takes " + damage + " damage from " 
							+ attacker.name + " and defends "+ defense +" and now has " + this.health + " health");
				}
			}
			
		});
		System.out.println(this.name + " is a " + spec + " with " + this.health + " health.");

	}
	
	public boolean move1Space() {
		if(has_moved++ >= moves) {
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
	*/
	
	public Rectangle drawCharacter() {
		block.setFill(spec.color);
		
		return block;
	}
	
}
