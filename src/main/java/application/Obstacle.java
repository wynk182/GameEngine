package application;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Obstacle extends Rectangle {
	
	int type = 0;
	
	public Obstacle(){
		this.setWidth(25);
		this.setHeight(25);
		this.setFill(Color.LIGHTGREEN);
	}

}
