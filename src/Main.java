import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
/**
 * The main entry point of the Tank 2025 game application.
 * Initializes the JavaFX application window and starts the game loop.
 */
public class Main extends Application{

    /**
     * Called when the JavaFX application starts.
     * Sets up the main game scene and window.
     *
     * @param primaryStage the main stage (window) for the application
     */
    @Override
    public void start(Stage primaryStage){
        GameScene gameScene = new GameScene();
        Scene scene = new Scene(gameScene);
        gameScene.setupKeyListeners(scene);
        primaryStage.setTitle("Tank 2025");
        primaryStage.setScene(scene);
        primaryStage.show();
        gameScene.startGame();
    }
}