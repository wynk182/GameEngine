package application;

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
	//Rectangle item  = 
	
	public Item() {
		//this = new Rectangle(1,1,20,20);
		
		this.setOnMousePressed(e -> {		    
		    System.out.println(this.name);		    
		});
		this.setOnMouseDragged(e -> {
			if(equipped) {
				equipped = false;
				Main.selected_character.removeItem(this);
				//int r = GridPane.getRowIndex(this);
				//int c = GridPane.getColumnIndex(this);
				Main.equip.getChildren().remove(this);
				//Main.equip.add(new Rectangle(1,1,20,20), c, r);
				//Main.equip.getChildren().get(0).rowIndex;
				this.setLayoutX(e.getSceneX());
				this.setLayoutY(e.getSceneY());
				Main.backpack.addToBackPack(this);
			}
			else {
				Main.selected_character.load_out.equipItem(this, this.item_type);
				Main.backpack.removeFromBackPack(this);
			}
			
		});
		this.setWidth(20);
		this.setHeight(20);
		this.setFill(Color.RED);
		this.setStyle("-fx-cursor: hand;");		
	}		
	
}
