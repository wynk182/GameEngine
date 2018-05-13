package application;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Item extends Rectangle{
	String name = "";
	String item_type = "";
	int attack_bonus = 0;
	int defense_bonus = 0;
	int move_bonus = 0;
	int health_bonus = 0;
	int worth = 0;
	int range = 0;
	boolean equipped = false;
	boolean saved = false;
	//Rectangle item  = 
	
	public Item() {
		//this = new Rectangle(1,1,20,20);
		
		this.setOnMousePressed(e -> {		    
		    System.out.println(this.name);		    
		});
		this.setOnMouseDragged(e -> {
			if(Main.selected_character!=null) {
				if(equipped) {
					equipped = false;
					Main.selected_character.load_out.removeItem(this);				
					Main.equip.getChildren().remove(this);				
					//this.setLayoutX(e.getSceneX());
					//this.setLayoutY(e.getSceneY());
					Main.backpack.addToBackPack(this);
				}
				else {
					Main.backpack.removeFromBackPack(this);
					if(!Main.selected_character.load_out.equipItem(this, this.item_type)){
						Main.backpack.addToBackPack(this);
					}				
				}
			}
			
		});
		this.setWidth(20);
		this.setHeight(20);
		this.setFill(Color.GREEN);
		this.setStyle("-fx-cursor: hand;");	
		
		this.setOnMouseEntered(e -> {
			Label l = new Label();
			String info = this.name 
					//+ "\n" + this.item_type					
					+ ((this.attack_bonus != 0)? "\natt: " + this.attack_bonus : "")
					+ ((this.defense_bonus != 0)? "\ndef: " + this.defense_bonus : "")
					+ ((this.move_bonus != 0)? "\nmv: " + this.move_bonus : "")
					+ ((this.health_bonus != 0)? "\nhp: " + this.health_bonus : "")
					+ ((this.range != 0)? "\nrng: " + this.range : "")
					+ ((this.worth != 0)? "\ngp: " + this.worth : "")
					;
			l = new Label(info);
			Main.info.showInfo(l, e.getSceneX(), e.getSceneY());			
		});
		this.setOnMouseExited(e -> {
			Main.info.hideInfo();
		});
		
	}		
	
}
