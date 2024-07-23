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
import javafx.scene.control.TextField;

import java.lang.Math;

public class App extends Application {

    Scene scene;
    
    Slider yzSlider;
    Slider xzSlider;
    Slider xySlider;
    Slider scaleSlider;
    Slider xTransSlider;
    Slider yTransSlider;
    Slider zTransSlider;
    Slider xPanSlider;
    Slider yPanSlider;
    Button update;
    Button load;
    TextField fileInput;

    Canvas canvas;
    GraphicsContext gc;

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
	
        scene = new Scene(root, 1000, 1000);	

        // Create a canvas
        this.canvas = new Canvas(scene.getWidth()*2/3, scene.getHeight()*2/3);
        gc = canvas.getGraphicsContext2D();

	HBox inputBox = new HBox();
	fileInput = new TextField("yoshi.obj");
	load = new Button("Load");
	load.setOnAction(e -> loadHandler());
	inputBox.getChildren().addAll(fileInput, load);
	
	
	VBox sliderBox = new VBox(8);
	HBox xTransBox = new HBox();
	HBox yTransBox = new HBox();
	HBox zTransBox = new HBox();
	HBox xPanBox = new HBox();
	HBox yPanBox = new HBox();
	HBox yzBox = new HBox();
	HBox xzBox = new HBox();
	HBox xyBox = new HBox();
	HBox scaleBox = new HBox();

	xTransSlider = new Slider(0, 50, 0);
	yTransSlider = new Slider(0, 50, 0);
	zTransSlider = new Slider(0, 50, 0);

	xPanSlider = new Slider(0, 500, 0);
	yPanSlider = new Slider(0, 500, 0);
	
	yzSlider = new Slider(0, Math.PI * 2, 0);
	xzSlider = new Slider(0, Math.PI * 2, 0);
	xySlider = new Slider(0, Math.PI * 2, 0);
	
	scaleSlider = new Slider(0, 200, 1);
	
	update = new Button("update");
	update.setOnAction(e -> actionHandler());

	
	xTransBox.getChildren().addAll(new Label("X Translation"), xTransSlider);
	yTransBox.getChildren().addAll(new Label("Y Translation"), yTransSlider);
	zTransBox.getChildren().addAll(new Label("Z Translation"), zTransSlider);
	xPanBox.getChildren().addAll(new Label("X Pan"), xPanSlider);
	yPanBox.getChildren().addAll(new Label("Y Pan"), yPanSlider);
	
	yzBox.getChildren().addAll(new Label("YZ Rotation"), yzSlider);
	xzBox.getChildren().addAll(new Label("XZ Rotation"), xzSlider);
	xyBox.getChildren().addAll(new Label("XY Rotation"), xySlider);
	
	scaleBox.getChildren().addAll(new Label("Scale"), scaleSlider, update);
	
	sliderBox.getChildren().addAll(xTransBox, yTransBox, zTransBox, xPanBox, yPanBox, yzBox, xzBox, xyBox, scaleBox);
	
        // Set initial color and width for drawing
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);

	// draws an orthographic projection
       	//drawOrthographic();

	//draws perspective
	drawPerspective("yoshi.obj");

        // Create a border pane layout
        root.setCenter(canvas);
	root.setBottom(sliderBox);
	root.setTop(inputBox);


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
	drawLine(A.getEntry(0,0), A.getEntry(1,0), B.getEntry(0,0), B.getEntry(1,0));
	drawLine(A.getEntry(0,0), A.getEntry(1,0), C.getEntry(0,0), C.getEntry(1,0));
	drawLine(C.getEntry(0,0), C.getEntry(1,0), B.getEntry(0,0), B.getEntry(1,0));	
    }

    private void drawOrthographic() {
	gc.setFill(Color.WHITE);
	gc.fillRect(0,0, canvas.getWidth(), canvas.getHeight());
	
	OBJReader obj = new OBJReader("yoshi.obj");
	Double transX = xTransSlider.getValue();
	Double transY = yTransSlider.getValue();
	Double transZ = zTransSlider.getValue();
	Double panX = xPanSlider.getValue();
	Double panY = yPanSlider.getValue();
	Double yz = yzSlider.getValue();
	Double xz = xzSlider.getValue();
	Double xy = xySlider.getValue();
	Double scale = scaleSlider.getValue();

	for (int i = 0; i < obj.faces.length; i++) {
	    Vertex vert1 = obj.vertices[obj.faces[i].v1];
	    Matrix vect1 = new Matrix(3,1);
	    vect1.setMatrix(new Double[][] {{vert1.x}, {vert1.y}, {vert1.z}});
	    //System.out.println(vect1);
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

    public void drawPerspective(String file) {
	gc.setFill(Color.WHITE);
	gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

	OBJReader obj = new OBJReader(file);

	Double transX = xTransSlider.getValue();
	Double transY = yTransSlider.getValue();
	Double transZ = zTransSlider.getValue();

	Double xPan = xPanSlider.getValue();
	Double yPan = yPanSlider.getValue();

	Double yz = yzSlider.getValue();
	Double xz = xzSlider.getValue();
	Double xy = xySlider.getValue();

	Double scale = scaleSlider.getValue();

	for (int i = 0; i < obj.faces.length; i++) {
	    Vertex vert1 = obj.vertices[obj.faces[i].v1];
	    Matrix vect1 = new Matrix(3,1);
	    vect1.setMatrix(new Double[][] {{vert1.x}, {vert1.y}, {vert1.z}});
	    //System.out.println(vect1);
	    vect1 = vect1.rotate(yz, xz, xy);
	    vect1 = vect1.persTranslate(transX, transY, transZ);
	    vect1 = vect1.persDivide(Matrix.perspective(16, 9, 1, -5, 5));
	    Matrix orthoVect1 = Matrix.orthographic(vect1);
	    orthoVect1 = orthoVect1.scale(scale);
	    orthoVect1 = orthoVect1.orthoTranslate(xPan, yPan);
	    
	    Vertex vert2 = obj.vertices[obj.faces[i].v2];
	    Matrix vect2 = new Matrix(3,1);
	    vect2.setMatrix(new Double[][] {{vert2.x}, {vert2.y}, {vert2.z}});
	    //System.out.println(vect2);
	    vect2 = vect2.rotate(yz, xz, xy);
	    vect2 = vect2.persTranslate(transX, transY, transZ);
	    vect2 = vect2.persDivide(Matrix.perspective(16, 9, 1, -5, 5));
	    Matrix orthoVect2 = Matrix.orthographic(vect2);
	    orthoVect2 = orthoVect2.scale(scale);
	    orthoVect2 = orthoVect2.orthoTranslate(xPan, yPan);
	    
	    Vertex vert3 = obj.vertices[obj.faces[i].v3];
	    Matrix vect3 = new Matrix(3,1);
	    vect3.setMatrix(new Double[][] {{vert3.x}, {vert3.y}, {vert3.z}});
	    //System.out.println(vect3);
	    vect3 = vect3.rotate(yz, xz, xy);
	    vect3 = vect3.persTranslate(transX, transY, transZ);
	    vect3 = vect3.persDivide(Matrix.perspective(16, 9, 1, -5, 5));
	    Matrix orthoVect3 = Matrix.orthographic(vect3);
	    orthoVect3 = orthoVect3.scale(scale);
	    orthoVect3 = orthoVect3.orthoTranslate(xPan, yPan);


	    System.out.println(orthoVect1 + "\n"
			       + orthoVect2 + "\n"
			       + orthoVect3 + "\n");
	    drawTriangle(orthoVect1, orthoVect2, orthoVect3);
	}
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void actionHandler() {
	//drawOrthographic();
	drawPerspective(fileInput.getText());
    }

    public void loadHandler() {
	drawPerspective(fileInput.getText());
	System.out.println("loaded " + fileInput.getText());
    }

}
