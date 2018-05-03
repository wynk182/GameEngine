package application;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Item {
	String name = "";
	int item_type = 0;
	int attack_bonus = 0;
	int defense_bonus = 0;
	int move_bonus = 0;
	int health_bonus = 0;
	int worth = 0;
	int range = 0;
	boolean equipped = false;
	Rectangle item  = new Rectangle(1,1,20,20);
	
	public Item() {
		item.setOnMousePressed(e -> {
		    // record a delta distance for the drag and drop operation.
			
		    //x = item.getLayoutX();
		    //y = item.getLayoutY();
			//item.setLayoutX(e.getSceneX());
			//item.setLayoutY(e.getSceneY());
		    System.out.println(this.name);
		    //item.setCursor(Cursor.MOVE);
		  
		});
		item.setOnMouseDragged(e -> {
			if(equipped) {
				equipped = false;
				Main.selected_character.removeItem(this);
				int r = GridPane.getRowIndex(item);
				int c = GridPane.getColumnIndex(item);
				Main.equip.getChildren().remove(item);
				Main.equip.add(new Rectangle(1,1,20,20), c, r);
				//Main.equip.getChildren().get(0).rowIndex;
				item.setLayoutX(e.getSceneX());
				item.setLayoutY(e.getSceneY());
				Main.backpack.addToBackPack(item);
			}
			else {
				
			}
			
		});
	}

	public Rectangle drawItem() {
		item.setFill(Color.RED);
		item.setStyle("-fx-cursor: hand;");
		
		
		
		return item;
	}
}
