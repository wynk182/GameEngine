package application;

import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class BackPack extends GridPane{

	public BackPack() {
		this.setGridLinesVisible(true);
		for(int i = 0; i < 3; i++) {
            ColumnConstraints column = new ColumnConstraints(25);
            this.getColumnConstraints().add(column);
        }

        for(int i = 0; i < 10; i++) {
            RowConstraints row = new RowConstraints(25);
            this.getRowConstraints().add(row);
        }
	}
}
