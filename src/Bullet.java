import javafx.geometry.BoundingBox;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Represents a bullet fired by either the player or an enemy tank.
 * Handles movement, rendering, and collision state (exploded or not).
 */
public class Bullet extends MovableEntity{
    private final Image image = new Image("file:assets/bullet.png");
    private final Image boom = new Image("file:assets/smallExplosion.png");
    private final int direction;
    protected final boolean fromPlayer;

    protected BoundingBox hitbox;
    private boolean exploded = false;
    private int explosionFrames = 0;

    /**
     * Constructs a new Bullet instance.
     *
     * @param x           Initial x-coordinate of the bullet
     * @param y           Initial y-coordinate of the bullet
     * @param direction   Direction in which the bullet travels (0: right, 1: up, 2: left, 3: down)
     * @param fromPlayer  True if the bullet was fired by the player
     */
    public Bullet(double x, double y, int direction, boolean fromPlayer) {
        speed = 2;
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.fromPlayer = fromPlayer;
        hitbox = new BoundingBox(x-7,y-7,14,14);
    }

    /**
     * Moves the bullet in its current direction and updates its hitbox.
     */
    public void move() {
        reposition(direction);
        this.hitbox=new BoundingBox(x-7,y-7,14,14);
    }

    /**
     * Renders the bullet (or its explosion) onto the canvas.
     *
     * @param gc GraphicsContext used for drawing
     */
    public void render(GraphicsContext gc) {
        gc.save();
        gc.translate(x, y);
        gc.rotate(direction*-90);
        gc.drawImage(exploded ? boom:image,-7,-7,14,14);
        gc.restore();
    }

    /**
     * Triggers the bullet's explosion state.
     */
    public void explode() {
        this.exploded = true;
        this.explosionFrames = 0;
    }

    /**
     * Checks whether the bullet is currently exploded.
     *
     * @return True if exploded, otherwise false
     */
    public boolean isExploded() {
        return exploded;
    }

    /**
     * Determines if the bullet should be removed from the game.
     * A bullet should be removed if it has finished its explosion animation.
     *
     * @return True if ready for removal, otherwise false
     */
    public boolean shouldBeRemoved() {
        int MAX_EXPLOSION_FRAMES = 30;
        return exploded && explosionFrames >= MAX_EXPLOSION_FRAMES;
    }

    /**
     * Advances the explosion animation frame counter.
     */
    public void updateExplosion(){
            explosionFrames++;
    }
}
