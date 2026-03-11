import javafx.geometry.BoundingBox;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.List;

/**
 * Represents a tank entity in the game, including both the player and enemies.
 * Extends {@link MovableEntity} to include basic position and movement.
 * A tank can move, render itself with rotation based on direction, shoot bullets,
 * and handle explosion logic when destroyed.
 */
public class Tank extends MovableEntity {
    protected int lastDir,boomTime = 0,MAX_BOOM_TIME = 60;
    protected Image image1,image2,boom=new Image("file:assets/explosion.png");
    protected boolean imageBool,isExploded;


    /**
     * Renders the tank on the canvas with proper rotation.
     * If the tank is exploded, renders the explosion sprite instead.
     *
     * @param gc the GraphicsContext used for drawing
     */
    public void render(GraphicsContext gc) {
        if(!isExploded){
            gc.save();
            gc.translate(x+16, y+16);
            gc.rotate(lastDir*-90);
            gc.drawImage(imageBool ? image1:image2,-16,-16);
            gc.restore();
        }else{
            gc.drawImage(boom,x,y,32,32);
        }
    }

    /**
     * Calculates the bounding box at the next position, used for collision detection.
     *
     * @param direction The direction of intended movement (0-3), or 4 for current position
     * @return BoundingBox representing the tank's future or current space
     */
    public BoundingBox getNextBoundingBox(int direction) {
        BoundingBox box = null;
        switch (direction){
            case 0:
                box = new BoundingBox(x+speed,y,32,32);
                break;
            case 1:
                box = new BoundingBox(x,y-speed,32,32);
                break;
            case 2:
                box = new BoundingBox(x-speed,y,32,32);
                break;
            case 3:
                box = new BoundingBox(x,y+speed,32,32);
                break;
            case 4:
                box = new BoundingBox(x,y,32,32);
        }
        return box;
    }

    /**
     * Creates a new bullet and adds it to the provided bullet list.
     *
     * @param fromPlayer True if the bullet is fired by the player
     * @param bullets    The list to which the new bullet will be added
     */
    public void shoot(boolean fromPlayer, List<Bullet> bullets){
        bullets.add(new Bullet(this.x+16,this.y+16,this.lastDir,fromPlayer));
    }

    /**
     * Marks the tank as exploded, triggering explosion rendering.
     */
    public void explode(){
        isExploded=true;
    }
}
