import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Sample points (you can replace these with your own points)
        double startX = 100.0;
        double startY = 100.0;
        double endX = 300.0;
        double endY = 200.0;

        // Create a canvas
        Canvas canvas = new Canvas(600, 400);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Set initial color and width for drawing
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);

        // Draw a line between the points

	drawOrthographic(gc);

        // Create a border pane layout
        BorderPane root = new BorderPane();
        root.setCenter(canvas);

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
