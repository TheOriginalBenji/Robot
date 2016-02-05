
import javafx.scene.*;
import java.util.*;
import javafx.application.Application;
import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.collections.*;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class JavaApplication10 extends Application {

    private Display display;
    private Stage stage;

    @Override
    public void start(Stage primaryStage) {
        // Create buttons for thesdsdsdsdsasasasaasasa user to interact with
        Button btPlus = new Button("Increase Polygon Size");
        Button btMinus = new Button("Decrease Polygon Size");
        Button btReset = new Button("Reset Environment");
        Button btCalculate = new Button("Calculate Shortest Path");
        
        // starting or resetting the display
        display = new Display(btPlus, btMinus, btReset, btCalculate);
        
        
        Scene scene = new Scene(display);
        primaryStage.setTitle("Assignment 1 - Artificial Intelligence - Benjamin Hamlin");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setMaximized(true);
        primaryStage.setResizable(true);
        primaryStage.setMinWidth(1500);
        primaryStage.setMinHeight(750);
        
        stage = primaryStage;
        
    }

    // Launch the window
    public static void main(String[] args) throws Exception{
        launch(args);
    }
}
