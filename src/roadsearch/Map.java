package roadsearch;

import gmapsfx.GoogleMapView;
import gmapsfx.MapComponentInitializedListener;
import gmapsfx.javascript.JavascriptArray;
import gmapsfx.javascript.object.GoogleMap;
import gmapsfx.javascript.object.LatLong;
import gmapsfx.javascript.object.MapOptions;
import gmapsfx.javascript.object.MapTypeIdEnum;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Map extends Application implements MapComponentInitializedListener{

	/* GUI components */
	private Stage primaryStage;
	
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
	
	
	//=========level 9============
	private TextField mapFile;
	private Button fetch;

	private GoogleMapView mapComponent;
	private GoogleMap map;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		this.primaryStage = primaryStage;
		mapComponent = new GoogleMapView();
		mapComponent.addMapInitializedListener(this);
		
		/* Right Panel */
	    VBox right = new VBox();
	    
	    /* box of map file dropdown and load button */
	    HBox mapFiles = new HBox(10);
	    
	    mapFiles.setAlignment(Pos.CENTER);
	    
	    choiceBox = new ChoiceBox<>();
	    choiceBox.setPrefWidth(200);
	    loadButton = new Button("Load Map");
	    mapFiles.getChildren().addAll(choiceBox, loadButton);
	    
	    /* box showing current selected map */
	    HBox currMap = new HBox();
	    currMap.setPadding(new Insets(20, 0, 0, 0));
	    mapLabel = new Label("Current Map: No Map Selected");
	    mapLabel.setGraphic(new ImageView("http://rolold.f2fmedia.tv/wp-content/uploads/2014/05/google-maps-logo-icon-small.png"));
	    mapLabel.setTextFill(Color.BLUE);
	    currMap.getChildren().addAll(mapLabel);
	    
	    /* box for start */
	    HBox startBox = new HBox();
	    startBox.setPadding(new Insets(20, 0, 0, 0));
	    startLabel = new Label("Starting Point: No point selected");
	    startLabel.setTextFill(Color.DARKMAGENTA);
//	    startLabel.setGraphic(new ImageView(DataSet.startURL));
	    startBox.getChildren().addAll(startLabel);
	    
	    /* box for dest. */
	    HBox destBox = new HBox();
	    destBox.setPadding(new Insets(20, 0, 0, 0));
	    destLabel = new Label("Destination: No point selected");
	    destLabel.setTextFill(Color.DARKMAGENTA);
//	    destLabel.setGraphic(new ImageView(DataSet.destinationURL));
	    destBox.getChildren().addAll(destLabel);
	    
	    
	    /* box for currPos */
	    HBox currPosBox = new HBox();
	    currPosBox.setPadding(new Insets(20, 0, 0, 0));
	    currPosLabel = new Label("Current: No point selected");
	    currPosLabel.setTextFill(Color.GREEN);
//	    currPosLabel.setGraphic(new ImageView(DataSet.SELECTED_URL));
	    currPosBox.getChildren().addAll(currPosLabel);
	    
	    /* box for start and dest buttons */
	    HBox pointButtonBox = new HBox(30);
	    pointButtonBox.setAlignment(Pos.CENTER);
	    pointButtonBox.setPadding(new Insets(20, 0, 0, 0));
	    startButton = new Button("Choose as Start");
	    startButton.setDisable(true);	//disable the button at first
	    destButton = new Button("Choose as Dest");
	    destButton.setDisable(true);	//disable the button at first
	    pointButtonBox.getChildren().addAll(startButton, destButton);
	    
	    
	    /* box for drop down menu */
	    HBox algBox = new HBox(10);
	    algBox.setPadding(new Insets(20, 0, 0, 0));
	    alg = new ChoiceBox<>();
	    alg.getItems().add("BFS");
	    alg.getItems().add("Dijkstra");
	    alg.getItems().add("A Star");
	    alg.setValue("Dijkstra");
	    algBox.getChildren().addAll(new Label("Choose search algorithm: "), alg);
	    
	    /* box for show path and visualization and reset */
	    HBox routeBox = new HBox(20);
	    path = new Button("Show Path");
	    path.setDisable(true);
	    visualize = new Button("Visualization");
	    visualize.setDisable(true);
	    reset = new Button("Reset");
	    routeBox.setPadding(new Insets(20, 0, 0, 0));
	    routeBox.setAlignment(Pos.CENTER);
	    routeBox.getChildren().addAll(path, visualize, reset);
	    
	    /* box for fetch */
	    HBox fetchBox = new HBox(20);
	    fetchBox.setPadding(new Insets(30, 0, 0, 0));
	    fetchBox.setAlignment(Pos.CENTER);
	    mapFile = new TextField();
	    mapFile.setPromptText("*Type example.map");
	    fetch = new Button("Fetch Data");
	    fetchBox.getChildren().addAll(mapFile, fetch);
	    
	    /* Add all the layouts to right box, in the order of levels */
	    Label chooseLabel = new Label("Choose a Map:");
	    chooseLabel.setTextFill(Color.DARKRED);
		right.getChildren().addAll(chooseLabel);
		right.getChildren().addAll(mapFiles, currMap);
		right.getChildren().addAll(startBox);
		right.getChildren().addAll(destBox);
		right.getChildren().addAll(currPosBox);
		right.getChildren().addAll(pointButtonBox);
		right.getChildren().addAll(algBox);
		right.getChildren().addAll(routeBox);
		right.getChildren().addAll(fetchBox);
		
		/* set size for the right box */
		right.setPadding(new Insets(20, 0, 0, 0));
		right.setPrefWidth(300);
		
		/* set overall layout */
		BorderPane bp = new BorderPane();
		Scene scene = new Scene(bp);
        bp.setCenter(mapComponent);
        bp.setRight(right);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Mini-Google Map");
        primaryStage.show();
	    
	}

	@Override
	public void mapInitialized() {
		// TODO Auto-generated method stub
		
	}

}
