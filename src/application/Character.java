package application;

import javafx.scene.shape.Rectangle;

public class Character{
	
	public LoadOut load_out;
	public String name = "Buster";
	public String gender = "Male";
	public int health;
	public int attack;
	public int defense;
	public int moves;	
	public int has_moved = 0;
	boolean has_attacked = false;
	public CharacterClass spec = Specialization.Peasant.getSpec();
	Weapon weapon = Weapons.None.getWeapon();
	Rectangle block = new Rectangle(1,1, 50, 50);
	
	public int[] coordinates;
	
	public Character() {		
		block.setOnMouseClicked(e -> {			
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
		//System.out.println(this.name + " is a " + spec + " with " + this.health + " health.");
	}
	
	public void setLoadOut(LoadOut load_out){
		this.load_out = load_out;
		for(Item i : load_out.items.values()){
			this.attack += i.attack_bonus;
			this.defense += i.defense_bonus;
			this.moves += i.move_bonus;
			this.health += i.health_bonus;
		}
	}
	
	public void equipItem(Item item, String slot){
		Item removed = this.load_out.items.remove(slot);
		if(removed != null){
			this.attack -= removed.attack_bonus;
			this.defense -= removed.defense_bonus;
			this.moves -= removed.move_bonus;
			this.health -= removed.health_bonus;
		}
		this.attack += item.attack_bonus;
		this.defense += item.defense_bonus;
		this.moves += item.move_bonus;
		this.health += item.health_bonus;
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
