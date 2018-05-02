package application;

import java.util.ArrayList;

public class CharacterList extends ArrayList<Character>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int current_character = 0;
	
	public Character getNext() {
		System.out.println(current_character);
		if(++current_character < this.size()) {
			return this.get(current_character);
		}
		else {
			current_character = 0;
		}
		return this.get(0);
	}
}
