package application;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class InfoBox extends Pane{
	
	public InfoBox(String bg) {
		this.setStyle("-fx-background-color: "+bg+";");
	}
	
	public void showInfo(Node node,double X, double Y){
		this.setLayoutX(X+20);
		this.setLayoutY(Y-20);
		this.getChildren().add(node);
	}
	public void hideInfo(){
		this.getChildren().removeAll(this.getChildren());
	}
}
