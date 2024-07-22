

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class App extends Application {

    private Canvas canvas;
    private GraphicsContext gc;
    private Label inLbl;
    private TextField inField;
    private HBox inBox;
    private Button inBtn;
    private VBox root;
    private Stage stage;
    private Scene scene;

    public App() {
	this.stage = null;
	this.scene = null;
	this.canvas = new Canvas(1000, 250);
	this.gc = this.canvas.getGraphicsContext2D();
	this.gc.setFill(Color.BLACK);
	this.gc.fillRect(0, 0, this.canvas.getWidth(), this.canvas.getHeight());
	this.gc.setTextAlign(TextAlignment.CENTER);
	this.root = new VBox();
	this.inBox = new HBox(5);
	this.inBox.setAlignment(Pos.CENTER);
	this.inLbl = new Label("Enter any string and press enter");
	this.inField = new TextField("I am bored");
	this.inField.setOnAction(e -> actionHandler());
	this.inBtn = new Button("Click me to continue");
	this.inBtn.setOnAction(e -> actionHandler());
    } // GraphicsApp

    @Override
    public void init() {
	System.out.println("init() called");
	connectNodes();
	
    } // init

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        this.scene = new Scene(this.root);
        this.stage.setOnCloseRequest(event -> Platform.exit());
        this.stage.setTitle("EPIC Java Graphics");
	this.stage.getIcons().add(new Image("file:resources/EPIC.png"));
        this.stage.setScene(this.scene);
        this.stage.sizeToScene();
        this.stage.show();
        Platform.runLater(() -> this.stage.setResizable(false));
    } // start

    @Override
    public void stop() {
	System.out.println("stop() called");
    } // stop


    public void connectNodes() {
	this.inBox.getChildren().addAll(this.inLbl, this.inField, this.inBtn);

	this.root.getChildren().addAll(this.inBox, this.canvas);
    } // connectNodes

    public void drawText(GraphicsContext gc, double x, double y) {
	this.gc.setFill(Color.BLACK);
	this.gc.fillRect(0, 0, this.canvas.getWidth(), this.canvas.getHeight());
	for (int i = 0; i < 13; i++) {
	    double level = (i % 4.0) / 3;
	    Color fillColor = Color.color(level, level, level);
	    gc.setFill(fillColor);
	    String text = this.inField.getText();
	    gc.setFont(Font.font("Helvetica", FontWeight.BOLD, (i + 10.0) * 10));
	    gc.fillText(text, x, y + 12 * i);
	} // for
    } // drawTexr

    public void actionHandler() {
	double x = this.canvas.getWidth() / 2;
	double y = this.canvas.getHeight() / 3;
	drawText(this.gc, x, y);
    } // drawBtn
} // GraphicsApp
