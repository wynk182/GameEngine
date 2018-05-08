package application;

public class Weapon {
	WeaponType weapon_type;
	int attack_bonus;
	int defense_bonus;
	String name;
	int value;
	int range;
	
	public Weapon(WeaponType weapon, int att, int def, String name, int value, int range) {
		this.weapon_type = weapon;
		this.attack_bonus = att;
		this.defense_bonus = def;
		this.name = name;
		this.value = value;
		this.range = range;
	}
	
}
