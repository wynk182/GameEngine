package application;

import javafx.scene.paint.Color;

public class CharacterClass {
	public int attack_bonus;
	public int defense_bonus;
	public int move_bonus;
	public int health_bonus;
	public ArmorType armor;
	public WeaponType weapon;
	public Color color;
	
	public CharacterClass(int att, int def, int move, int health, ArmorType armor, WeaponType weapon, Color color) {
		this.attack_bonus = att;
		this.defense_bonus = def;
		this.move_bonus = move;
		this.health_bonus = health;
		this.armor = armor;
		this.weapon = weapon;
		this.color = color;
	}
	
	
}
