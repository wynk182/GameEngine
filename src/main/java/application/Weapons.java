package application;

public enum Weapons {
	Sword{
		@Override
		Weapon getWeapon() {
			return new Weapon(WeaponType.Melee,10,0,"Iron Sword",10,1);
		}
	},
	Bow{
		@Override
		Weapon getWeapon() {
			return new Weapon(WeaponType.Range,20,0,"Hunter's Bow",15,5);
		}
	},
	Staff{
		@Override
		Weapon getWeapon() {
			return new Weapon(WeaponType.Melee,5,10,"Staff",5,1);
		}
	},
	SuperSword{
		@Override
		Weapon getWeapon() {
			return new Weapon(WeaponType.Melee,85,40,"Death Bringer",5000,1);
		}
	},
	None{
		@Override
		Weapon getWeapon() {
			return new Weapon(WeaponType.Melee,0,0,"Fists",0,1);
		}
	};
	
	abstract Weapon getWeapon();
}
