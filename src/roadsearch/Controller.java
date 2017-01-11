package roadsearch;

import java.util.ArrayList;
import java.util.List;

import geography.GeographicPoint;
import gmapsfx.javascript.JavascriptArray;
import gmapsfx.javascript.object.GoogleMap;
import gmapsfx.javascript.object.Marker;
import gmapsfx.shapes.Polyline;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import roadsearch.Alert;
//import miniGoogleMap.DataSet;
import roadsearch.DataSet;

public class Controller {
	private GoogleMap map;
	private DataSet ds;

	//=========level 1===========
	private ChoiceBox<String> choiceBox;
	private Button loadButton;
	//=========level 2===========
	private Label mapLabel;
	//=========level 3===========
	private Label startLabel;
	//=========level 4===========
	private Label destLabel;
	//=========level 5===========
	private static Label currPosLabel;
	//=========level 6===========
	private Button startButton;
	private Button destButton;
	//=========level 7===========
	private ChoiceBox<String> alg;	
	//=========level 8===========
	private Button path;
	private Button visualize;
	private Button reset;
	
	private JavascriptArray jsArray;
	
	public Controller(
			GoogleMap map, DataSet ds,
			ChoiceBox<String> choiceBox, ChoiceBox<String> alg,
			Button loadButton, Button startButton, Button destButton,
			Button path, Button visualize, Button reset, 
			Label mapLabel, Label startLabel, Label destLabel, Label currPosLabel,
			JavascriptArray jsArray) 
	{
		this.map = map;
		this.ds = ds;
		this.choiceBox = choiceBox;
		this.alg = alg;
		this.loadButton = loadButton;
		this.startButton = startButton;
		this.destButton = destButton;
		this.path = path;
		this.visualize = visualize;
		this.reset = reset;
		this.mapLabel = mapLabel;
		this.startLabel = startLabel;
		this.destLabel = destLabel;
		this.currPosLabel = currPosLabel;
		this.jsArray = jsArray;
		
		setUpComponents();
	}

	private void setUpComponents() {
		// TODO Auto-generated method stub
		loadButton.setOnAction(e -> {
			
			if(ds != null) {
				ds.reset();
			}
			
    		/* load the map */
    		if(choiceBox.getValue() == null) {
    			Alert.display("Warning!", "No map selected!");
    			return;
    		}
    		
    		String fileName = choiceBox.getValue();
    		if(ds != null && fileName.equals(ds.getFileName())) {
    			Alert.display("Warning!", "The map has been loaded!");
    			return;
    		}
    		
	    	System.out.println("Ready to load map file: " + fileName);
	    	ds = new DataSet(fileName, map);
	    	ds.initializeGraph();
	    	System.out.println("Map loaded successfully! ");
	    	
	    	/* set map label */
	    	mapLabel.setText("Current Map: " + fileName);
	    	
	    	/* Enable other buttons */
	    	startButton.setDisable(false);
	    	destButton.setDisable(false);
	    });
		
		
    	startButton.setOnAction(e -> {
    		System.out.println("Start button pressed!");
    		
    		/* check if any point selected */
    		if(ds.currPos() == null) {
    			Alert.display("Warning", "No point selected!");
    			return;
    		}
    		
    		/* set currPos to be starting point */
    		ds.setStart();
    		
    		/* set start label */
    		startLabel.setText("Starting Point: " + ds.startPoint());
    		
    		/* set show path */
    		if(ds != null && ds.dest() != null) {
    			path.setDisable(false);
    		}
    		
    	});		
    	
    	destButton.setOnAction(e -> {
    		System.out.println("Dest button pressed!");
    		
    		/* check if any point selected */
    		if(ds.currPos() == null) {
    			Alert.display("Warning", "No point selected!");
    			return;
    		}
    		
    		/* set currPos to be destination */
    		ds.setDest();
    		
    		/* set dest label */
    		destLabel.setText("Starting Point: " + ds.startPoint());
    		
    		/* set show path */
    		if(ds != null && ds.startPoint() != null) {
    			path.setDisable(false);
    		}
    	});
    	
    	
    	path.setOnAction(e -> {
    		System.out.println("Path is pressed!");
    		
    		ds.closeInfoWindow();
    		
    		String algorithm = alg.getValue();
    		List<GeographicPoint> exploredNodes = new ArrayList<>();
    		List<GeographicPoint> partialPath = null;
    		
    		if("BFS".equals(algorithm)) {
    			partialPath = ds.map().bfs(ds.startPoint(), ds.dest(), exploredNodes);
    		} else if("Dijkstra".equals(algorithm)) {
    			partialPath = ds.map().dijkstra(ds.startPoint(), ds.dest(), exploredNodes);
    		} else if("A Star".equals(algorithm)) {
    			partialPath = ds.map().aStarSearch(ds.startPoint(), ds.dest(), exploredNodes);
    		} else {
    			System.out.println("This is not gonna be!");
    		}
    		
    		if(partialPath == null) {
    			Alert.display("Warning", "No path found!");
    			return;
    		}
    		
    		ds.setPath(partialPath);
    		
    		ds.setExploredNodes(exploredNodes);
    		
    		Polyline route = ds.path();
    		
    		map.addMapShape(route);
    		map.fitBounds(ds.getBound("pathBound"));
    		
    		visualize.setDisable(false);
    		
    		ds.setUnselectedMarkers(false);
    	});
    	
    	reset.setOnAction(e -> {
    		if(ds != null) {
    			ds.reset();
    		}
	    	
    		if(jsArray != null) {
    			for(int i = 0; i < jsArray.length(); i++) {
        			((Marker)jsArray.get(i)).setVisible(false);
        			map.removeMarker((Marker)jsArray.get(i));
        		}
    		}
    		
    		String fileName = choiceBox.getValue();
    		ds = new DataSet(fileName, map);
	    	ds.initializeGraph();
	    	System.out.println("Map loaded successfully! ");
	    	
	    	/* set map label */
	    	mapLabel.setText("Current Map: " + fileName);
	    	startLabel.setText("Starting Point: No point selected");
	    	destLabel.setText("Destination: No point selected");
	    	
	    	/* Enable other buttons */
	    	startButton.setDisable(false);
	    	destButton.setDisable(false);
    		
    		
	    	/* disable other buttons */
	    	path.setDisable(true);
	    	visualize.setDisable(true);
    	});
		
	}
	
	
	
	
	
	
}
