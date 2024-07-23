import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;

import java.lang.Math;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create a canvas
        Canvas canvas = new Canvas(600, 400);
        GraphicsContext gc = canvas.getGraphicsContext2D();

	VBox sliderBox = new VBox(8);

	HBox yzBox = new HBox(8);
	HBox xzBox = new HBox(8);
	HBox xyBox = new HBox(8);
	
	Slider yzSlider = new Slider(0, Math.PI * 2, 0);
	Slider xzSlider = new Slider(0, Math.PI * 2, 0);
	Slider xySlider = new Slider(0, Math.PI * 2, 0);

	yzBox.getChildren().addAll(new Label("YZ Rotation"), yzSlider);
	xzBox.getChildren().addAll(new Label("XZ Rotation"), xzSlider);
	xyBox.getChildren().addAll(new Label("XY Rotation"), xySlider);
	
	sliderBox.getChildren().addAll(yzBox, xzBox, xyBox);
	
        // Set initial color and width for drawing
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);

	// draws an orthographic projection
       	drawOrthographic(gc);

        // Create a border pane layout
        BorderPane root = new BorderPane();
        root.setCenter(canvas);
	root.setBottom(sliderBox);

        // Set up the scene
        Scene scene = new Scene(root, 600, 400);

        // Set the stage
        primaryStage.setTitle("OBJ Viewer");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void drawLine(GraphicsContext gc, double startX, double startY, double endX, double endY) {
        gc.strokeLine(startX, startY, endX, endY);
    }

    private void drawOrthographic(GraphicsContext gc) {
	OBJReader obj = new OBJReader("dodecahedron.obj");
	for (int i = 0; i < obj.faces.length; i++) {
	    int v1 = obj.faces[i].v1;
	    int v2 = obj.faces[i].v2;
	    int v3 = obj.faces[i].v3;
	    int tX = 300;
	    int tY = 200;
	    int s = 50;

	    drawLine(gc, s*obj.vertices[v1].x + tX, s*obj.vertices[v1].y + tY,
		     s*obj.vertices[v2].x + tX, s*obj.vertices[v2].y + tY);
	    drawLine(gc, s*obj.vertices[v1].x + tX, s*obj.vertices[v1].y + tY,
		     s*obj.vertices[v3].x + tX, s*obj.vertices[v3].y + tY);
	    drawLine(gc, s*obj.vertices[v2].x + tX, s*obj.vertices[v2].y + tY,
		     s*obj.vertices[v3].x + tX, s*obj.vertices[v3].y + tY);
	    
	}
    }

    public static void main(String[] args) {
        launch(args);
    }

}
