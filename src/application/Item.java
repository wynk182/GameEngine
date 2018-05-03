package application;

import javafx.scene.Cursor;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
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
	double x = 0;
	double y = 0;
	
	public Rectangle drawItem() {
		Rectangle item  = new Rectangle(1,1,20,20);
		item.setFill(Color.RED);
		item.setStyle("-fx-cursor: hand;");
		
		/*
		item.setOnMouseClicked(e -> {
			System.out.println(name);
			Main.item_info.setText("Item info:\n" + name + "\nAttack:" + attack_bonus 
					+ "\nDefense:" + defense_bonus + "\nMove:" + move_bonus 
					+ "\nHealth:" + health_bonus + "\nValue:" + worth + "\nRange:" + range);
		});
		
		item.setOnDragDetected(e ->{
			if(equipped) {
				equipped = false;
			}
			else {
				equipped = true;
			}
			Dragboard db = item.startDragAndDrop(TransferMode.ANY);
	        
	        ClipboardContent content = new ClipboardContent();
	        content.putString(this.name);
	        db.setContent(content);
	        
	        e.consume();
			System.out.println(equipped);
			//Main.selected_character.load_out.items.get(key)
		});
		*/
		//Delta dragDelta = new Delta();
		
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
				Main.equip.getChildren().remove(item);
				//Main.equip.row
				item.setLayoutX(e.getSceneX());
				item.setLayoutY(e.getSceneY());
				Main.backpack.addToBackPack(item);
			}
			else {
				
			}
			
		});
		
		return item;
	}
}
