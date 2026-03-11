import javafx.geometry.BoundingBox;
import javafx.scene.image.Image;
import java.util.List;

/**
 * Represents the player-controlled tank in the game.
 * Inherits behavior from {@link Tank}, and adds player-specific logic like
 * lives, invincibility frames, movement and shooting buffers, and respawning.
 */
public class PlayerTank extends Tank {
    private int framesSinceMove,framesSinceShoot = 0;
    protected int lives = 3;
    protected int invinciblePeriod=0;

    /**
     * Constructs a player tank, sets initial position and speed,
     * and loads tank images.
     */
    public PlayerTank() {
        x=400;
        y=600;
        speed=8;
        this.image1 = new Image("file:assets/yellowTank1.png");
        this.image2 = new Image("file:assets/yellowTank2.png");
    }

    /**
     * Attempts to shoot a bullet if the shooting buffer cooldown has passed.
     *
     * @param bullets the list to add the new bullet to
     */
    public void shoot(List<Bullet> bullets){
        int SHOOTING_BUFFER = 60;
        if(framesSinceShoot>= SHOOTING_BUFFER) {
            framesSinceShoot=0;
            super.shoot(true, bullets);
        }
    }

    /**
     * Attempts to move the player in the specified direction, if no wall collision occurs
     * and movement cooldown has passed.
     *
     * @param direction   the movement direction (0: right, 1: up, 2: left, 3: down)
     * @param walls       the list of walls for collision detection
     */
    public void move(int direction, List<Wall> walls){
        BoundingBox next;
        int MOVEMENT_BUFFER = 15;
        if (framesSinceMove >= MOVEMENT_BUFFER && !isExploded) {
            framesSinceMove = 0;
            lastDir = direction;
            boolean collided = false;
            next = getNextBoundingBox(lastDir);
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
    }

    /**
     * Updates the internal state of the player tank, including cooldowns,
     * explosion recovery, and invincibility timer.
     */
    public void update() {
        framesSinceMove++;
        framesSinceShoot++;
        if(invinciblePeriod>0) {
            invinciblePeriod--;
        }
        if(isExploded){
            boomTime++;
        }
        if(boomTime>=MAX_BOOM_TIME){
            invinciblePeriod=180;
            isExploded=false;
            boomTime=0;
            x=400;
            y=600;
        }
    }

    /**
     * Triggers explosion and decrements player lives.
     * Overrides {@link Tank#explode()} to add life tracking.
     */
    @Override
    public void explode(){
        lives--;
        super.explode();
    }
}
