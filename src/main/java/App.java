import matrixutils.Vector;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.application.Platform;
import javafx.scene.layout.Priority;
import javafx.scene.input.MouseButton;
import javafx.stage.Screen;
import javafx.scene.image.Image;

import java.io.FileNotFoundException;
import java.io.IOException;

public class App extends Application {

    /** the box for all the controls. */
    private HBox controlBox;
    /** the box for the rotation boxes. */
    private VBox rotateBox;
    /** the box for yz rotations. */
    private HBox yzSliderBox;
    /** the slider for yz rotations. */
    private Slider yzSlider;
    /** the box for xz rotations. */
    private HBox xzSliderBox;
    /** the slider for xz rotations. */
    private Slider xzSlider;
    /** the box for xy rotations. */
    private HBox xySliderBox;
    /** the slider for the xy rotations. */
    private Slider xySlider;    
    /** the box for the trans boxes. */
    private VBox transBox;
    /** the box for the x trans. */
    private HBox xTransBox;
    /** the field for the x trans. */
    private TextField xTransField;
    /** the box for the y trans. */
    private HBox yTransBox;
    /** the field to translate y. */
    private TextField yTransField;
    /** the box for the z trans. */
    private HBox zTransBox;
    /** the field to translate z. */
    private TextField zTransField;
    /** the box containing the pan boxes. */
    private VBox panBox;
    /** the box containing the x pan nodes. */
    private HBox xPanBox;
    /** the field to type how much to pan x. */
    private TextField xPanField;
    /** the box containing the y pan nodes. */
    private HBox yPanBox;
    /** the field to type how much to pan y. */
    private TextField yPanField;
    /** box containing the zoom nodes. */
    private HBox zoomBox;
    /** field to type how much to zoom. */
    private TextField zoomField;
    /** box containing file input nodes. */
    private HBox fileInBox;
    /** the field to type the file to load. */
    private TextField fileInput;
    /** Loads the obj file. */
    private Button loadBtn;
    /** Updates the already loaded obj file to the control box settings. */
    private Button updateBtn;
    /** A label for dispolaying information. */
    private Label infoLbl;
    /** button for showing/hiding the advanced options. */
    private Button showBtn;

    /** the canvas to draw on. */
    private Canvas canvas;
    /** The graphics context for the canvas for drawing. */
    private GraphicsContext gc;

    /** the primary root. */
    private VBox root;
    /** the primary javafx scene. */
    private Scene scene;
    /** the javafx stage. */
    private Stage stage;

    /** the OBJ reader to parse the files. */
    private OBJReader obj;

    /** the x coord of the mouse cursor. */
    private Double mouseX;
    /** the y coord of the mouse cursor. */
    private Double mouseY;

    /** Instantiates all the nodes. */
    public App() {
        this.stage = null;
	this.scene = null;
	this.root = new VBox();
	this.canvas = new Canvas();
	this.canvas.widthProperty().bind(this.root.widthProperty());
	this.gc = canvas.getGraphicsContext2D();
	this.gc.setStroke(Color.BLACK);
	this.gc.setLineWidth(1);
	this.controlBox = new HBox();
	this.rotateBox = new VBox();
	this.yzSliderBox = new HBox();
	this.yzSlider = new Slider(0, Math.PI * 2, 0);
	this.xzSliderBox = new HBox();
	this.xzSlider = new Slider(0, Math.PI * 2, Math.PI);
	this.xySliderBox = new HBox();
	this.xySlider = new Slider(0, Math.PI * 2, Math.PI);
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
	this.fileInput = new TextField("yoshi");
	this.loadBtn = new Button("Load");
	this.loadBtn.setOnAction(e -> loadHandler());
	this.updateBtn = new Button("Update");
	this.updateBtn.setOnAction(e -> updateHandler());
	this.infoLbl = new Label("");
	this.showBtn = new Button("Advanced ↑");
	this.showBtn.setOnAction(e -> {
		if (this.controlBox.isVisible()) {
		    this.root.getChildren().remove(this.controlBox);
		    this.controlBox.setVisible(false);
		    this.updateHandler();
		    this.showBtn.setText("Advanced ↓");
		    canvas.setHeight(this.canvas.getHeight() +  this.controlBox.getHeight());
		    this.updateHandler();
		} else {
		    this.root.getChildren().addAll(this.controlBox);
		    this.controlBox.setVisible(true);
		    this.showBtn.setText("Advanced ↑");
		    canvas.setHeight(this.canvas.getHeight() -  this.controlBox.getHeight());
		    this.updateHandler();
		}
	    });
	
	try {
	    this.readOBJ("yoshi");	
	} catch (FileNotFoundException fnfe) {
	    this.setInfoLbl("File not found; " + fnfe.getMessage());
	    return;
	} catch (IOException ioe) {
	    this.setInfoLbl("Error in file; " + ioe.getMessage());
	    return;
	} catch (ArrayIndexOutOfBoundsException aioobe) {
	    this.setInfoLbl("Error in file; " + aioobe.getMessage());
	    return;
	} // try

	VBox.setVgrow(canvas, Priority.ALWAYS);
	
	this.root.heightProperty().addListener((obs, oldVal, newVal) -> {
		if (this.controlBox.isVisible()) {
		    canvas.setHeight(newVal.doubleValue() - controlBox.getHeight()
				     - this.fileInBox.getHeight() - this.infoLbl.getHeight());
		} else {
		    canvas.setHeight(newVal.doubleValue()
				     - this.fileInBox.getHeight() - this.infoLbl.getHeight());
		}
	    });
	
	this.canvas.setOnMousePressed(e -> {
		this.mouseX = e.getSceneX();
		this.mouseY = e.getSceneY();
	    });
	
	this.canvas.setOnMouseDragged(e -> {
		Double startX = this.mouseX;
		Double startY = this.mouseY;
		Double dX = (e.getSceneX() - startX);
		Double dY = (e.getSceneY() - startY);
		mouseX = e.getSceneX();
		mouseY = e.getSceneY();
		if (e.getButton().equals(MouseButton.PRIMARY)) {
		    Double sensitivity = 10.;
		    dX = dX / canvas.getWidth() * sensitivity;
		    dY = dY / canvas.getHeight() * sensitivity;
		    Double xzValue = xzSlider.getValue();
		    Double yzValue = yzSlider.getValue();
		    Double newXZvalue = modPositive(xzValue + dX, Math.PI * 2);
		    Double newYZvalue = modPositive(yzValue + dY, Math.PI * 2);
		    xzSlider.setValue(newXZvalue);
		    yzSlider.setValue(newYZvalue);
		} else if (e.getButton().equals(MouseButton.SECONDARY)) {
		    Double sensitivity = 1.;
		    dX = dX * sensitivity;
		    dY = dY * sensitivity;
		    Double xValue = Double.parseDouble(this.xTransField.getText());
		    Double yValue = Double.parseDouble(this.yTransField.getText());
		    Double transFactor = Double.parseDouble(this.zTransField.getText()) / 1000;
		    Double newXvalue = (xValue + dX * transFactor);
		    Double newYvalue = (yValue + dY * transFactor);
		    this.xTransField.setText("" + newXvalue);
		    this.yTransField.setText("" + newYvalue);
		}
		
		Runnable task = () -> {
		    Platform.runLater(() -> {
			    this.updateHandler();
			});
		};
		    
		runInNewThread(task, "DraggedHandler");
	    });
	
	this.canvas.setOnScroll(e -> {
		Double newZoomValue;
		try {
		    newZoomValue = Double.parseDouble(this.zoomField.getText()) + e.getDeltaY();
		} catch (NumberFormatException nfe) {
		    return;
		} catch (NullPointerException npe) {
		    return;
		} // try

		this.zoomField.setText("" + newZoomValue);
		this.updateHandler();
	    });
    } // App

    /**
     * Runs Runnable tasks in new demon threads.
     *
     * @param task is the task to run in a new thread.
     * @param name is the name of the task.
     */
    private void runInNewThread(final Runnable task, final String name) {
	Thread t = new Thread(task, name);
	t.setDaemon(true);
	t.start();
    } // runInNewThread

    @Override
    public final void init() {
	System.out.println("init() called");
	connectNodes();
    } // init

    @Override
    public final void start(final Stage stage) {
	this.scene = new Scene(this.root);
	this.stage = stage;
	this.stage.setOnCloseRequest(e -> Platform.exit());
	this.stage.setTitle("OBJ Viewer");
	this.stage.setScene(this.scene);
        this.stage.getIcons().add(new Image(getClass().getResourceAsStream("/icon.png")));
	this.stage.setMaximized(true);
	this.stage.show();

    } // start

    @Override
    public final void stop() {
	System.out.println("stop() called");
    } // stop

    /** Connects the nodes of the scene together. */
    private void connectNodes() {
	this.root.getChildren().addAll(this.canvas, this.fileInBox, this.infoLbl, this.controlBox);
	this.controlBox.getChildren().addAll(this.rotateBox, this.transBox, this.panBox, this.zoomBox,
					     this.updateBtn);
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
	this.fileInBox.getChildren().addAll(new Label("File: "), this.fileInput, this.loadBtn, this.showBtn);
    } // connectNodes

    /**
     * Draws a line.
     *
     * @param startX The X coord of the start of the line.
     * @param startY The Y coord of the start of the line.
     * @param endX The X coord of the end of the line.
     * @param endY The Y coord of the end of the line.
     */
    private void drawLine(final Double startX, final Double startY, final Double endX, final Double endY) {
        gc.strokeLine(startX, startY, endX, endY);
    } // drawLine

    private Double modPositive(final Double a, final Double b) {
	return (((a % b) + b) % b);
    }

    /**
     * Draws a triangle connecting the tips of 3 vectors.
     *
     * @param a The first vector.
     * @param b The second vector.
     * @param c The third vector.
     */
    private void drawTriangle(final Vector a, final Vector b, final Vector c) {
	drawLine(a.getEntry(0, 0), a.getEntry(1, 0), b.getEntry(0, 0), b.getEntry(1, 0));
	drawLine(a.getEntry(0, 0), a.getEntry(1, 0), c.getEntry(0, 0), c.getEntry(1, 0));
	drawLine(c.getEntry(0, 0), c.getEntry(1, 0), b.getEntry(0, 0), b.getEntry(1, 0));	
    } // drawTriangle

    /**
     * Draws a perspective drawing of an array of vertices and an array of faces.
     *
     * @param vertices The array of vertices to draw.
     * @param faces The array of faces to draw.
     */
    private void drawPerspective(final Vertex[] vertices, final Face[] faces) {
	
	Double xTrans;
	Double yTrans;
	Double zTrans;
	Double xPan;
	Double yPan;
	Double zoom;
	Double height = Screen.getPrimary().getBounds().getHeight();
	Double width = Screen.getPrimary().getBounds().getWidth();
	Double fov = 1.;
	Double zNear = -5.;
	Double zFar = 5.;
	Double xPivot = this.obj.getCenter().getEntry(0, 0);
	Double yPivot = this.obj.getCenter().getEntry(1, 0);
	Double zPivot = this.obj.getCenter().getEntry(2, 0);
	
	try {
	    xTrans = Double.parseDouble(this.xTransField.getText());
	    yTrans = Double.parseDouble(this.yTransField.getText());
	    zTrans = Double.parseDouble(this.zTransField.getText());
	    xPan = Double.parseDouble(this.xPanField.getText());
	    yPan = Double.parseDouble(this.yPanField.getText());
	    zoom = Double.parseDouble(this.zoomField.getText());
	} catch (NumberFormatException nfe) {
	    this.setInfoLbl("Non-parsable input; " + nfe.getMessage());
	    return;
	} catch (NullPointerException npe) {
	    this.setInfoLbl("Empty input; " + npe.getMessage());
	    return;
	} // try
	
	Double yz = this.yzSlider.getValue();
	Double xz = this.xzSlider.getValue();
	Double xy = this.xySlider.getValue();

	gc.setFill(Color.WHITE);
	gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

	for (int i = 0; i < faces.length; i++) {
	    Vector vect1 = vertices[faces[i].getVertices()[0]].toVector();
	    Vector vect2 = vertices[faces[i].getVertices()[1]].toVector();
	    Vector vect3 = vertices[faces[i].getVertices()[2]].toVector();

	    Vector v1 = Vector.perspectiveTransform(vect1, zoom,
						    yz, xz, xy,
						    xTrans, yTrans, zTrans,
						    xPan, yPan,
						    height, width, fov, zNear, zFar,
						    xPivot, yPivot, zPivot);

	    Vector v2 = Vector.perspectiveTransform(vect2, zoom,
						    yz, xz, xy,
						    xTrans, yTrans, zTrans,
						    xPan, yPan,
						    height, width, fov, zNear, zFar,
						    xPivot, yPivot, zPivot);

	    Vector v3 = Vector.perspectiveTransform(vect3, zoom,
						    yz, xz, xy,
						    xTrans, yTrans, zTrans,
						    xPan, yPan,
						    height, width, fov, zNear, zFar,
						    xPivot, yPivot, zPivot);
	    
	    drawTriangle(v1, v2, v3);
	} // for
    } // drawPerspective

    /**
     * Reads the OBJ file.
     *
     * @param file The filename to read.
     */
    private void readOBJ(final String file) throws FileNotFoundException, IOException {
	String modelFile = "models/" + file + ".obj";
	this.obj = new OBJReader(modelFile);
	this.xPanField.setText("" + (this.canvas.getWidth() / 2));
	this.yPanField.setText("" + (this.canvas.getHeight() / 2));
	Vertex furthest = this.obj.getFurthest();
	Double distanceFurthest = furthest.distanceFromVector(this.obj.getCenter());
	this.zTransField.setText("" + Math.floor(distanceFurthest * 2 * 100) / 100);
	this.zoomField.setText("" + this.canvas.getHeight() / 2.5);
    } // readOBJ

    /** Updates the existing OBJ. */
    private void updateHandler() {
	drawPerspective(obj.getVertices(), obj.getFaces());
    } // updateHandler

    /** Loads a new OBJ from a the file specified. */
    private void loadHandler() {
	try {
	    readOBJ(fileInput.getText());
	} catch (FileNotFoundException fnfe) {
	    this.setInfoLbl("File not found; " + fnfe.getMessage());
	    return;
	} catch (IOException ioe) {
	    this.setInfoLbl("Error in file; " + ioe.getMessage());
	    return;
	} catch (ArrayIndexOutOfBoundsException aioobe) {
	    this.setInfoLbl("Error in file; " + aioobe.getMessage());
	    return;
	} // try
	this.yzSlider.setValue(0.);
	this.xzSlider.setValue(Math.PI);
	this.xySlider.setValue(Math.PI);
	this.xTransField.setText("" + 0);
	this.yTransField.setText("" + 0);
	drawPerspective(obj.getVertices(), obj.getFaces());
	
	this.setInfoLbl("Loaded: " + this.obj.getName());
    } // loadHandler

    /**
     * Sets the info label to the mesage.
     *
     * @param message The message to set.
     */
    private void setInfoLbl(final String message) {
	this.infoLbl.setText(message);
    } // setInfoLbl
} // App
