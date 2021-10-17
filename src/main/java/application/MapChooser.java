package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class MapChooser extends BorderPane{
	
	List<File> maps = new ArrayList<File>();
	int current_index = 0;
	GridPane preview;
	
	public MapChooser(List<File> maps) {		
		this.maps = maps;
		int[][] initial_map = parseMapFile(maps.get(0));
		preview = new GridPane();
		Label top = new Label("Size: " + initial_map[0].length + "x" + initial_map.length
				+ " " + maps.get(0).getName());
		Label left = new Label("left");
		left.setOnMouseClicked(e -> {
			int[][] map = parseMapFile(getPreviousMap());
			top.setText("Size: " + map[0].length + "x" + map.length + " " + maps.get(current_index).getName());
			renderPreviewMap(map);
		});
		Label right = new Label("right");
		right.setOnMouseClicked(e -> {
			int[][] map = parseMapFile(getNextMap());
			top.setText("Size: " + map[0].length + "x" + map.length + " " + maps.get(current_index).getName());
			renderPreviewMap(map);
		});		
		
		this.setTop(top);
		this.setLeft(left);
		this.setRight(right);
		this.setCenter(preview);
		renderPreviewMap(initial_map);
		//this.getChildren().addAll(left,Main.map_preview,right);
	}
	
	public File getPreviousMap() {
		if(current_index == 0) {
			current_index = maps.size()-1;
		}
		else {
			current_index--;
		}
		return maps.get(current_index);
		
	}
	
	public File getNextMap() {
		if(current_index == maps.size()-1)
			current_index = 0;
		else
			current_index++;
		return maps.get(current_index);
	}
	
	public int[][] getCurrentMap(){
		return parseMapFile(maps.get(current_index));
	}
	
	public int[][] parseMapFile(File map) {
		int[][] parsed = new int[0][0];
		try {
			BufferedReader map_reader = new BufferedReader(new FileReader(map));
			String row = "";
			int index = 0;
			List<int[]> board = new ArrayList<int[]>();
			while((row = map_reader.readLine()) != null) {
				//System.out.println(row);
				board.add(Arrays.stream(row.split(",")).mapToInt(Integer::parseInt).toArray());
				index++;
			}
			parsed = new int[index][board.get(0).length];
			for(int[] a_row : board) {
				parsed[board.indexOf(a_row)] = a_row;
			}
			map_reader.close();
			//return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return parsed;
	}
	
	public void renderPreviewMap(int[][] map) {
		preview.getChildren().removeAll(preview.getChildren());
		preview.getColumnConstraints().removeAll(preview.getColumnConstraints());
		preview.getRowConstraints().removeAll(preview.getRowConstraints());
		//map_preview = new GridPane();
		
		for(int i = 0; i < map[0].length; i++) {
            ColumnConstraints column = new ColumnConstraints(10);
            preview.getColumnConstraints().add(column);
        }

        for(int i = 0; i < map.length; i++) {
            RowConstraints row = new RowConstraints(10);
            preview.getRowConstraints().add(row);
        }
		
        for(int r = 0; r < map.length; r++) {
        	for(int c = 0; c < map[r].length; c++) {
        		
        		Rectangle seed = new Rectangle(1,1, 10, 10);
        		switch(map[r][c]) {
        		case 1:	        			
        			seed.setFill(Color.BROWN);	        			
        			break;
        		case 2:	        			
        			seed.setFill(Color.LIGHTBLUE);	        			
        			break;
        		case 3:
        			seed.setFill(Color.GREY);
        			break;
        		case 10:
        			seed.setFill(Color.BROWN);
        			break;
        		default:
        			seed.setFill(Color.LIGHTGREEN);
        			break;	        			
        		}
        		preview.add(seed, c,r);
        		
        	}
        }
        
	}
	
	
	
}
