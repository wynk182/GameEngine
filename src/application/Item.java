package application;

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
	
	public Rectangle drawItem() {
		Rectangle item  = new Rectangle(1,1,20,20);
		item.setFill(Color.RED);
		item.setStyle("-fx-cursor: hand;");
		item.setOnMouseClicked(e -> {
			System.out.println(name);
			Main.item_info.setText("Item info:\n" + name + "\nAttack:" + attack_bonus 
					+ "\nDefense:" + defense_bonus + "\nMove:" + move_bonus 
					+ "\nHealth:" + health_bonus + "\nValue:" + worth + "\nRange:" + range);
		});
		return item;
	}
}
