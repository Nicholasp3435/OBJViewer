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
import javafx.scene.control.Button;

import java.lang.Math;

public class App extends Application {

    Slider yzSlider;
    Slider xzSlider;
    Slider xySlider;
    Slider scaleSlider;
    Slider xTransSlider;
    Slider yTransSlider;
    Button update;

    Canvas canvas;
    GraphicsContext gc;

    @Override
    public void start(Stage primaryStage) {
        // Create a canvas
        this.canvas = new Canvas(600, 400);
        gc = canvas.getGraphicsContext2D();

	VBox sliderBox = new VBox(8);

	HBox xTransBox = new HBox();
	HBox yTransBox = new HBox();
	HBox yzBox = new HBox();
	HBox xzBox = new HBox();
	HBox xyBox = new HBox();
	HBox scaleBox = new HBox();

	xTransSlider = new Slider(0, 500, 0);
	yTransSlider = new Slider(0, 500, 0);
	yzSlider = new Slider(0, Math.PI * 2, 0);
	xzSlider = new Slider(0, Math.PI * 2, 0);
	xySlider = new Slider(0, Math.PI * 2, 0);
	scaleSlider = new Slider(0, 100, 1);
	update = new Button("update");
	update.setOnAction(e -> actionHandler());

	xTransBox.getChildren().addAll(new Label("X Translation"), xTransSlider);
	yTransBox.getChildren().addAll(new Label("Y Translation"), yTransSlider);
	yzBox.getChildren().addAll(new Label("YZ Rotation"), yzSlider);
	xzBox.getChildren().addAll(new Label("XZ Rotation"), xzSlider);
	xyBox.getChildren().addAll(new Label("XY Rotation"), xySlider);
	scaleBox.getChildren().addAll(new Label("Scale"), scaleSlider, update);
	
	sliderBox.getChildren().addAll(xTransBox, yTransBox, yzBox, xzBox, xyBox, scaleBox);
	
        // Set initial color and width for drawing
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);

	// draws an orthographic projection
       	drawOrthographic();

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

    private void drawLine(double startX, double startY, double endX, double endY) {
        gc.strokeLine(startX, startY, endX, endY);
    }

    private void drawTriangle(Matrix A, Matrix B, Matrix C) {
	if (A.getColLen() != 1 || B.getColLen() != 1 || C.getColLen() != 1) {
	    return;
	} else  if (A.getRowLen() != 2 || A.getRowLen() != 2 || C.getRowLen() != 2) {
	    System.out.println("NOTICE: Not a 2 x 1 matrix");
	}
	drawLine(A.entry(0,0), A.entry(1,0), B.entry(0,0), B.entry(1,0));
	drawLine(A.entry(0,0), A.entry(1,0), C.entry(0,0), C.entry(1,0));
	drawLine(C.entry(0,0), C.entry(1,0), B.entry(0,0), B.entry(1,0));	
    }

    private void drawOrthographic() {
	gc.setFill(Color.WHITE);
	gc.fillRect(0,0, canvas.getWidth(), canvas.getHeight());
	
	OBJReader obj = new OBJReader("dodecahedron.obj");
	Double transX = xTransSlider.getValue();
	Double transY = yTransSlider.getValue();
	Double yz = yzSlider.getValue();
	Double xz = xzSlider.getValue();
	Double xy = xySlider.getValue();
	Double scale = scaleSlider.getValue();
	//Double scale = 100.;
	for (int i = 0; i < obj.faces.length; i++) {
	    Vertex vert1 = obj.vertices[obj.faces[i].v1];
	    Matrix vect1 = new Matrix(3,1);
	    vect1.setMatrix(new Double[][] {{vert1.x}, {vert1.y}, {vert1.z}});
	    System.out.println(vect1);
	    vect1 = vect1.rotate(yz, xz, xy);
	    vect1 = vect1.scale(scale);
	    vect1 = vect1.orthoTranslate(transX, transY);
	    Matrix orthoVect1 = Matrix.orthographic(vect1);
	    
	    Vertex vert2 = obj.vertices[obj.faces[i].v2];
	    Matrix vect2 = new Matrix(3,1);
	    vect2.setMatrix(new Double[][] {{vert2.x}, {vert2.y}, {vert2.z}});
	    vect2 = vect2.rotate(yz, xz, xy);
	    vect2 = vect2.scale(scale);
	    vect2 = vect2.orthoTranslate(transX, transY);
	    Matrix orthoVect2 = Matrix.orthographic(vect2);
	    
	    Vertex vert3 = obj.vertices[obj.faces[i].v3];
	    Matrix vect3 = new Matrix(3,1);
	    vect3.setMatrix(new Double[][] {{vert3.x}, {vert3.y}, {vert3.z}});
	    vect3 = vect3.rotate(yz, xz, xy);
	    vect3 = vect3.scale(scale);
	    vect3 = vect3.orthoTranslate(transX, transY);
	    Matrix orthoVect3 = Matrix.orthographic(vect3);

	    drawTriangle(orthoVect1, orthoVect2, orthoVect3);
	}

    }

    public static void main(String[] args) {
        launch(args);
    }

    public void actionHandler() {
	drawOrthographic();
    }

}
