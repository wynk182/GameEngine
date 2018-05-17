package application;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Obstacle extends Rectangle {
	
	int type = 0;
	
	public Obstacle(){
		this.setWidth(15);
		this.setHeight(15);
		this.setFill(Color.LIGHTGREEN);
	}

}
