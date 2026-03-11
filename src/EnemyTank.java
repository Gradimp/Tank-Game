import javafx.geometry.BoundingBox;
import javafx.scene.image.Image;
import java.util.List;
import java.util.Random;

/**
 * Represents an AI-controlled enemy tank in the game.
 * Inherits from {@link Tank} and adds autonomous movement, shooting, and removal behavior.
 */
public class EnemyTank extends Tank{
    private int framesSinceMove,framesSinceShoot,framesSinceDirChange;
    private int randomShootingBuffer=120;
    protected boolean toBeRemoved = false;

    private final Random rand = new Random();

    /**
     * Constructs a new enemy tank at a given position.
     *
     * @param x the initial x-coordinate
     * @param y the initial y-coordinate
     */
    public EnemyTank(int x, int y){
        speed=8;
        this.image1 = new Image("file:assets/whiteTank1.png");
        this.image2 = new Image("file:assets/whiteTank2.png");
        this.x = x;
        this.y = y;
    }

    /**
     * Causes the enemy tank to shoot a bullet in its current direction.
     *
     * @param bullets the list to which the bullet will be added
     */
    public void shoot(List<Bullet> bullets){
        super.shoot(false,bullets);
    }

    /**
     * Updates the tank's AI logic:
     * - Moves in the current direction every few frames if no wall is in the way.
     * - Randomly changes direction after a fixed buffer.
     * - Fires at a randomized interval.
     * - Handles explosion and self-removal if destroyed.
     *
     * @param bullets the list of bullets in the game
     * @param walls   the list of walls for collision detection
     */
    public void update(List<Bullet> bullets,List<Wall> walls){
        if(!isExploded) {
            framesSinceMove++;
            framesSinceShoot++;
            framesSinceDirChange++;

            // Change direction randomly every few frames
            int DIRECTION_BUFFER = 120;
            if (framesSinceDirChange >= DIRECTION_BUFFER) {
                framesSinceDirChange = 0;
                lastDir = rand.nextInt(4);
            }

            // Random shooting interval
            if (framesSinceShoot >= randomShootingBuffer) {
                framesSinceShoot = 0;
                randomShootingBuffer = 120+rand.nextInt(120);
                this.shoot(bullets);
            }

            // Movement every fixed buffer interval
            int MOVEMENT_BUFFER = 15;
            if (framesSinceMove >= MOVEMENT_BUFFER) {
                framesSinceMove = 0;
                BoundingBox next = getNextBoundingBox(lastDir);
                boolean collided = false;
                for (Wall wall : walls) {
                    if (wall.boundingBox.intersects(next)) {
                        collided = true;
                    }
                }
                if (!collided) {
                    imageBool = !imageBool;
                    reposition(lastDir);
                }
            }
        }else{
            // Explosion logic and eventual removal
            boomTime++;
            if (boomTime >= MAX_BOOM_TIME) {
                toBeRemoved=true;
            }
        }
    }


}
