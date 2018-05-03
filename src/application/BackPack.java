package application;

import javafx.scene.Node;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class BackPack extends GridPane{
	
	int[][] slots = new int[10][3];

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
	
	public void addToBackPack(Node item) {
		for(int r = 0; r < slots.length; r++) {
			for(int c = 0; c < slots[r].length; c++) {
				//System.out.println(slots[r][c]);
				if(slots[r][c] == 0) {
					slots[r][c] = 1;
					this.add(item, c, r);
					return;
				}
			}
		}
	}
}
