import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The main game scene for Tank 2025.
 * Handles player input, rendering, updates for game objects, UI overlays, scrolling, and game state.
 */
public class GameScene extends StackPane {
    private final GraphicsContext gc;
    private final Canvas canvas;

    private final List<Wall> walls = new ArrayList<>();
    private final List<Bullet> bullets = new ArrayList<>();
    private final List<EnemyTank> enemies = new ArrayList<>();

    private final ScrollPane scrollPane = new ScrollPane();
    private final Pane uiLayer = new Pane();
    private final Label score = new Label();
    private final Label lives = new Label();
    private final Label gameOverScreen = new Label();
    private final Label pausedScreen = new Label();
    private final Set<KeyCode> activeKeys = new HashSet<>();

    private PlayerTank player;
    private int enemySpawnTimer, timeSinceLastSpawn , menuDelay=120;
    private int gameScore = 0;
    private boolean isGameOver=false,isPaused=false;

    /**
     * Registers listeners for key press and release events.
     *
     * @param scene the main scene to attach input listeners to
     */
    public void setupKeyListeners(Scene scene) {
            scene.setOnKeyPressed(event -> activeKeys.add(event.getCode()));
            scene.setOnKeyReleased(event -> activeKeys.remove(event.getCode()));
    }

    /**
     * Starts the main game loop using an AnimationTimer.
     */
    public void startGame() {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
                render();
            }
        }.start();
    }

    /**
     * Constructs the GameScene and sets up UI, scrolling, canvas, and game elements.
     */
    public GameScene() {
        this.getChildren().addAll(scrollPane, uiLayer);
        setupUI();
        scrollPane.setPrefSize(600,600);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setPannable(false);

        Pane content = new Pane();
        canvas = new Canvas(800, 800);
        content.getChildren().add(canvas);
        content.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
        scrollPane.setContent(content);

        gc = canvas.getGraphicsContext2D();
        gameSetup();
    }

    /**
     * Initializes the player and wall setup.
     */
    public void gameSetup() {
        player = new PlayerTank();
        setupWalls();
    }

    /**
     * Sets up UI labels for score, lives, and game state (paused/game over).
     */
    public void setupUI() {
        score.setLayoutX(40);
        score.setLayoutY(40);
        score.setTextFill(Color.WHITE);
        uiLayer.getChildren().add(score);

        lives.setLayoutX(40);
        lives.setLayoutY(60);
        lives.setTextFill(Color.WHITE);
        uiLayer.getChildren().add(lives);

        gameOverScreen.setFont(Font.font(50));
        gameOverScreen.setTextAlignment(TextAlignment.CENTER);
        gameOverScreen.setTextFill(Color.FORESTGREEN);
        gameOverScreen.setLayoutX(100);
        gameOverScreen.setLayoutY(100);
        gameOverScreen.setVisible(false);
        uiLayer.getChildren().add(gameOverScreen);

        pausedScreen.setFont(Font.font(50));
        pausedScreen.setTextAlignment(TextAlignment.CENTER);
        pausedScreen.setTextFill(Color.FORESTGREEN);
        pausedScreen.setLayoutX(100);
        pausedScreen.setLayoutY(100);
        pausedScreen.setText("Game Paused\n\n\nPress R to restart!\nPress esc to close!");
        pausedScreen.setVisible(false);
        uiLayer.getChildren().add(pausedScreen);
    }

    /**
     * Updates the game state, including player input, game logic, bullet/enemy interactions, and pause handling.
     */
    public void update() {
        if(menuDelay>0){
            menuDelay--;
        }


        if((isPaused|isGameOver) && menuDelay==0) {
            if(activeKeys.contains(KeyCode.R)) {
                restartGame();
                menuDelay = 120;
            }
            if (activeKeys.contains(KeyCode.ESCAPE)) {
                javafx.application.Platform.exit();
            }
        }

        if(player.lives<=0 && !isGameOver) {
            isGameOver=true;
            menuDelay=120;
        }

        if(activeKeys.contains(KeyCode.P) && !isGameOver && menuDelay==0) {
            isPaused=!isPaused;
            pausedScreen.setVisible(true);
            menuDelay=120;
        }

        if(!isGameOver && !isPaused) {
            updateBulletsAndCollisions();
            if (activeKeys.contains(KeyCode.UP)) {
                player.move(1, walls);
            } else if (activeKeys.contains(KeyCode.DOWN)) {
                player.move(3, walls);
            } else if (activeKeys.contains(KeyCode.LEFT)) {
                player.move(2, walls);
            } else if (activeKeys.contains(KeyCode.RIGHT)) {
                player.move(0, walls);
            }
            gameOverScreen.setVisible(false);
            pausedScreen.setVisible(false);

            if (activeKeys.contains(KeyCode.X)) {
                player.shoot(bullets);
            }

            player.update();
            updateEnemies();
        } else if (isGameOver) {
            gameOverScreen.setText(String.format("GAME OVER!\nYour Score: %d\n\nPress R to restart!\nPress esc to close!", gameScore ));
            gameOverScreen.setVisible(true);
        }
    }

    /**
     * Renders the game scene: background, player, bullets, enemies, and UI text.
     */
    public void render() {
        gc.clearRect(0, 0 , 800, 800);

        scrollPane.setHvalue(Math.max(0, Math.min(1,(player.x - 300) / (canvas.getWidth() - 600))));
        scrollPane.setVvalue(Math.max(0, Math.min(1,(player.y - 300) / (canvas.getHeight() - 600))));

        walls.forEach(wall -> wall.render(gc));
        player.render(gc);
        enemies.forEach(enemyTank -> enemyTank.render(gc));
        bullets.forEach(bullet -> bullet.render(gc));

        score.setText("Score: " + gameScore);
        lives.setText("Lives: " + player.lives);
    }

    /**
     * Initializes the level boundary and internal walls.
     */
    public void setupWalls(){
        for (int wallx=0;wallx<785;wallx+=16){
            walls.add(new Wall(wallx,0));
            walls.add(new Wall(wallx,784));
        }
        for (int wally=16;wally<769;wally+=16){
            walls.add(new Wall(0,wally));
            walls.add(new Wall(784,wally));
        }
        for(int wally=128;wally<257;wally+=16){
                walls.add(new Wall(128,wally));
                walls.add(new Wall(256,wally));
                walls.add(new Wall(656,wally));
                walls.add(new Wall(528,wally));
                walls.add(new Wall(272,2*(wally+64)));
                walls.add(new Wall(528,2*(wally+64)));
        }
        for(int wallx=192;wallx<337;wallx+=16){
            walls.add(new Wall(wallx,320));
            walls.add(new Wall(800-wallx,320));
        }
    }

    /**
     * Updates bullets and handles all bullet-related collisions.
     * Removes bullets and enemies as needed, updates scores and explosions.
     */
    public void updateBulletsAndCollisions(){
        List<Bullet> bulletsToRemove = new ArrayList<>();
        List<EnemyTank> enemiesToRemove = new ArrayList<>();

        for (Bullet bullet : bullets) {
            if (bullet.isExploded()) {
                bullet.updateExplosion();
                if (bullet.shouldBeRemoved()) {
                    bulletsToRemove.add(bullet);
                }
                continue;
            }

            bullet.move();

            if (bullet.fromPlayer) {
                for (EnemyTank enemy : enemies) {
                    if (bullet.hitbox.intersects(enemy.getNextBoundingBox(4))){
                        bulletsToRemove.add(bullet);
                        enemy.explode();
                        gameScore += 15;
                    }
                }
            } else if (!player.isExploded && player.invinciblePeriod==0){
                if (bullet.hitbox.intersects(player.getNextBoundingBox(4))) {
                    bulletsToRemove.add(bullet);
                    player.explode();
                }
            }

            for (Wall wall : walls) {
                if (wall.boundingBox.intersects(bullet.hitbox)) {
                    bullet.explode();
                    break;
                }
            }
        }
        for (EnemyTank enemy : enemies) {
            if(!player.isExploded && !enemy.isExploded && player.invinciblePeriod<=0 && enemy.getNextBoundingBox(4).intersects(player.getNextBoundingBox(4))) {
                player.explode();
                enemy.explode();
            }
            if (enemy.toBeRemoved) enemiesToRemove.add(enemy);
        }

        enemies.removeAll(enemiesToRemove);
        bullets.removeAll(bulletsToRemove);
    }

    /**
     * Spawns and updates all enemy tanks over time.
     */
    public void updateEnemies() {
        if(enemySpawnTimer<=timeSinceLastSpawn && enemies.size()<10){
            enemySpawnTimer= 600+(int)(240*Math.random());
            timeSinceLastSpawn=0;
            enemies.add(new EnemyTank(32+16*(int)(43*Math.random()),64));
        }
        timeSinceLastSpawn++;
        enemies.forEach(enemyTank -> enemyTank.update(bullets,walls));
    }

    /**
     * Resets the game state for a fresh restart.
     */
    public void restartGame() {
        enemies.clear();
        bullets.clear();
        player.x=400;
        player.y=600;
        player.lives=3;
        player.isExploded=false;
        gameScore=0;
        isPaused=false;
        isGameOver=false;
    }
}