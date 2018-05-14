package application;

import java.util.ArrayList;
import java.util.LinkedHashMap;

//import javafx.scene.control.Button;

public class CharacterList extends LinkedHashMap<String,Character>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int current_character = 0;
	boolean myTeam = false;
	
	public Character getNext() {
		//System.out.println(current_character);
		if(++current_character < this.size()) {
			//return this.get(current_character);
			return (new ArrayList<Character>(this.values())).get(current_character);
		}
		else {
			//Button end_turn = new Button("End Turn");
			//Main.info.showInfo(end_turn, 325, 275);
			current_character = 0;
		}
		return (new ArrayList<Character>(this.values())).get(0);
	}
}
