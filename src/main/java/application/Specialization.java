package application;

import javafx.scene.paint.Color;
//attack, defense, move, health, armor, weapon, color/image
public enum Specialization {
	Warrior{
		@Override
		CharacterClass getSpec() {
			return new CharacterClass(10,10,2,30,ArmorType.Heavy,WeaponType.Melee,Color.RED);
		}
	},
	Ranger{
		@Override
		CharacterClass getSpec() {
			return new CharacterClass(15,5,1,10,ArmorType.Heavy,WeaponType.Range,Color.GREEN);
		}
	},
	Healer{
		@Override
		CharacterClass getSpec() {
			return new CharacterClass(5,15,1,30,ArmorType.Heavy,WeaponType.Melee,Color.YELLOW);
		}
	},
	Peasant{
		@Override
		CharacterClass getSpec() {
			return new CharacterClass(1,1,0,5,ArmorType.None,WeaponType.Melee,Color.BLUE);
		}
	};
	
	abstract CharacterClass getSpec();
}
