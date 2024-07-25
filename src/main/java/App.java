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
import javafx.application.Platform;

import java.lang.Math;

public class App extends Application {

    /**
     *  controlBox
     *   rotateBox
     *    yzSliderBox (label, yzSlider)
     *    xzSliderBox (label, xzSlider)
     *    xySliderBox (label, xySlider)
     *   transBox
     *    xTransBox (label, xTransField)
     *    yTransBox (label, yTransField)
     *    zTransbox (label, zTransField)
     *   panBox
     *    xPanBox (label, xPanField)
     *    yPanBox (label, yPanField)
     *   zoomBox (label, zoomField)
     *   fileInBox (label, fileInput)
     *   updateBtn
     */
    private VBox controlBox;
    private VBox rotateBox;
    private HBox yzSliderBox;
    private Slider yzSlider;
    private HBox xzSliderBox;
    private Slider xzSlider;
    private HBox xySliderBox;
    private Slider xySlider;    
    private VBox transBox;
    private HBox xTransBox;
    private TextField xTransField;
    private HBox yTransBox;
    private TextField yTransField;
    private HBox zTransBox;
    private TextField zTransField;
    private VBox panBox;
    private HBox xPanBox;
    private TextField xPanField;
    private HBox yPanBox;
    private TextField yPanField;
    private HBox zoomBox;
    private TextField zoomField;
    private HBox fileInBox;
    private TextField fileInput;
    private Button updateBtn;

    private Canvas canvas;
    private GraphicsContext gc;

    private BorderPane root;
    private Scene scene;
    private Stage stage;

    public App() {
	this.stage = null;
	this.scene = null;
	this.root = new BorderPane();
	
	this.canvas = new Canvas(1000, 1000);
	this.gc = canvas.getGraphicsContext2D();
	this.gc.setStroke(Color.BLACK);
	this.gc.setLineWidth(1);
	this.controlBox = new VBox();
	this.rotateBox = new VBox();
	this.yzSliderBox = new HBox();
	this.yzSlider = new Slider(0, Math.PI * 2, 0);
	this.xzSliderBox = new HBox();
	this.xzSlider = new Slider(0, Math.PI * 2, 0);
	this.xySliderBox = new HBox();
	this.xySlider = new Slider(0, Math.PI * 2, 0);
	this.transBox = new VBox();
	this.xTransBox = new HBox();
	this.xTransField = new TextField("0");
	this.yTransBox = new HBox();
	this.yTransField = new TextField("0");
	this.zTransBox = new HBox();
	this.zTransField = new TextField("0");
	this.panBox = new VBox();
	this.xPanBox = new HBox();
	this.xPanField = new TextField("" + canvas.getWidth() / 2);
	this.yPanBox = new HBox();
	this.yPanField = new TextField("" + canvas.getHeight() / 2);
	this.zoomBox = new HBox();
	this.zoomField = new TextField("1");
	this.fileInBox = new HBox();
	this.fileInput = new TextField("yoshi.obj");
	this.updateBtn = new Button("Update");
	this.updateBtn.setOnAction(e -> updateHandler());
    }

    @Override
    public void init() {
	System.out.println("init() called");
	connectNodes();
    }

    @Override
    public void start(Stage stage) {
	this.scene = new Scene(this.root);

	drawPerspective("yoshi.obj");

	this.stage = stage;
	this.stage.setOnCloseRequest(e -> Platform.exit());
	this.stage.setTitle("OBJ Viewer");
	this.stage.setScene(this.scene);
	this.stage.sizeToScene();
	this.stage.show();
    }

    @Override
    public void stop() {
	System.out.println("stop() called");
    }

    private void connectNodes() {
	this.root.setCenter(this.canvas);
	this.root.setBottom(this.controlBox);
	this.controlBox.getChildren().addAll(this.rotateBox, this.transBox, this.panBox, this.zoomBox, this.fileInBox, this.updateBtn);
	this.rotateBox.getChildren().addAll(this.xzSliderBox, this.yzSliderBox, this.xySliderBox);
	this.xzSliderBox.getChildren().addAll(new Label("XZ Rotate: "), this.xzSlider);
	this.yzSliderBox.getChildren().addAll(new Label("YZ Rotate: "), this.yzSlider);
	this.xySliderBox.getChildren().addAll(new Label("XY Rotate: "), this.xySlider);
	this.transBox.getChildren().addAll(this.xTransBox, this.yTransBox, this.zTransBox);
	this.xTransBox.getChildren().addAll(new Label("X Translation: "), this.xTransField);
	this.yTransBox.getChildren().addAll(new Label("Y Translation: "), this.yTransField);
	this.zTransBox.getChildren().addAll(new Label("Z Translation: "), this.zTransField);
	this.panBox.getChildren().addAll(this.xPanBox, this.yPanBox);
	this.xPanBox.getChildren().addAll(new Label("X Pan: "), this.xPanField);
	this.yPanBox.getChildren().addAll(new Label("Y Pan: "), this.yPanField);
	this.zoomBox.getChildren().addAll(new Label("Zoom: "), this.zoomField);
	this.fileInBox.getChildren().addAll(new Label("File: "), this.fileInput);
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

	OBJReader obj = new OBJReader("yoshi.obj");
	
	Double transX;
	Double transY;
	Double transZ;
	Double panX;
	Double panY;
	Double zoom;
	
	try {
	    transX = Double.parseDouble(xTransField.getText());
	    transY = Double.parseDouble(yTransField.getText());
	    transZ = Double.parseDouble(zTransField.getText());
	    panX = Double.parseDouble(xPanField.getText());
	    panY = Double.parseDouble(yPanField.getText());
	    zoom = Double.parseDouble(zoomField.getText());
	} catch (NumberFormatException nfe) {
	    System.out.println("non-parsable input\n" + nfe.getMessage());
	    return;
	} catch (NullPointerException npe) {
	    System.out.println("empty input\n" + npe.getMessage());
	    return;
	}
	Double yz = yzSlider.getValue();
	Double xz = xzSlider.getValue();
	Double xy = xySlider.getValue();

	gc.setFill(Color.WHITE);
	gc.fillRect(0,0, canvas.getWidth(), canvas.getHeight());
	
	for (int i = 0; i < obj.faces.length; i++) {
	    Vertex vert1 = obj.vertices[obj.faces[i].v1];
	    Matrix vect1 = new Matrix(3,1);
	    vect1.setMatrix(new Double[][] {{vert1.x}, {vert1.y}, {vert1.z}});
	    vect1 = vect1.rotate(yz, xz, xy);
	    vect1 = vect1.scale(zoom);
	    vect1 = vect1.orthoTranslate(transX, transY);
	    Matrix orthoVect1 = Matrix.orthographic(vect1);
	    
	    Vertex vert2 = obj.vertices[obj.faces[i].v2];
	    Matrix vect2 = new Matrix(3,1);
	    vect2.setMatrix(new Double[][] {{vert2.x}, {vert2.y}, {vert2.z}});
	    vect2 = vect2.rotate(yz, xz, xy);
	    vect2 = vect2.scale(zoom);
	    vect2 = vect2.orthoTranslate(transX, transY);
	    Matrix orthoVect2 = Matrix.orthographic(vect2);
	    
	    Vertex vert3 = obj.vertices[obj.faces[i].v3];
	    Matrix vect3 = new Matrix(3,1);
	    vect3.setMatrix(new Double[][] {{vert3.x}, {vert3.y}, {vert3.z}});
	    vect3 = vect3.rotate(yz, xz, xy);
	    vect3 = vect3.scale(zoom);
	    vect3 = vect3.orthoTranslate(transX, transY);
	    Matrix orthoVect3 = Matrix.orthographic(vect3);

	    drawTriangle(orthoVect1, orthoVect2, orthoVect3);
	}

    }

    public void drawPerspective(String file) {

	OBJReader obj = new OBJReader(this.fileInput.getText());
	
	Double transX;
	Double transY;
	Double transZ;
	Double panX;
	Double panY;
	Double zoom;
	
	try {
	    transX = Double.parseDouble(this.xTransField.getText());
	    transY = Double.parseDouble(this.yTransField.getText());
	    transZ = Double.parseDouble(this.zTransField.getText());
	    panX = Double.parseDouble(this.xPanField.getText());
	    panY = Double.parseDouble(this.yPanField.getText());
	    zoom = Double.parseDouble(this.zoomField.getText());
	} catch (NumberFormatException nfe) {
	    System.out.println("non-parsable input\n" + nfe.getMessage());
	    return;
	} catch (NullPointerException npe) {
	    System.out.println("empty input\n" + npe.getMessage());
	    return;
	}
	Double yz = this.yzSlider.getValue();
	Double xz = this.xzSlider.getValue();
	Double xy = this.xySlider.getValue();

	gc.setFill(Color.WHITE);
	gc.fillRect(0,0, canvas.getWidth(), canvas.getHeight());

	for (int i = 0; i < obj.faces.length; i++) {
	    Vertex vert1 = obj.vertices[obj.faces[i].v1];
	    Matrix vect1 = new Matrix(3,1);
	    vect1.setMatrix(new Double[][] {{vert1.x}, {vert1.y}, {vert1.z}});
	    vect1 = vect1.rotate(yz, xz, xy);
	    vect1 = vect1.persTranslate(transX, transY, transZ);
	    vect1 = vect1.persDivide(Matrix.perspective(16, 9, 1, -5, 5));
	    Matrix orthoVect1 = Matrix.orthographic(vect1);
	    orthoVect1 = orthoVect1.scale(zoom);
	    orthoVect1 = orthoVect1.orthoTranslate(panX, panY);
	    
	    Vertex vert2 = obj.vertices[obj.faces[i].v2];
	    Matrix vect2 = new Matrix(3,1);
	    vect2.setMatrix(new Double[][] {{vert2.x}, {vert2.y}, {vert2.z}});
	    vect2 = vect2.rotate(yz, xz, xy);
	    vect2 = vect2.persTranslate(transX, transY, transZ);
	    vect2 = vect2.persDivide(Matrix.perspective(16, 9, 1, -5, 5));
	    Matrix orthoVect2 = Matrix.orthographic(vect2);
	    orthoVect2 = orthoVect2.scale(zoom);
	    orthoVect2 = orthoVect2.orthoTranslate(panX, panY);
	    
	    Vertex vert3 = obj.vertices[obj.faces[i].v3];
	    Matrix vect3 = new Matrix(3,1);
	    vect3.setMatrix(new Double[][] {{vert3.x}, {vert3.y}, {vert3.z}});
	    vect3 = vect3.rotate(yz, xz, xy);
	    vect3 = vect3.persTranslate(transX, transY, transZ);
	    vect3 = vect3.persDivide(Matrix.perspective(16, 9, 1, -5, 5));
	    Matrix orthoVect3 = Matrix.orthographic(vect3);
	    orthoVect3 = orthoVect3.scale(zoom);
	    orthoVect3 = orthoVect3.orthoTranslate(panX, panY);

	    drawTriangle(orthoVect1, orthoVect2, orthoVect3);
	}
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void updateHandler() {
	drawPerspective(fileInput.getText());
    }
}
