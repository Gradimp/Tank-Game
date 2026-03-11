import javafx.geometry.BoundingBox;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Represents a static wall block in the game world.
 * Walls are obstacles that block movement and bullets.
 */
public class Wall {
    private final Image image = new Image("file:assets/wall.png");
    private final double x;
    private final double y;

    public BoundingBox boundingBox;

    /**
     * Constructs a wall at the specified (x, y) position.
     *
     * @param x The x-coordinate of the wall's top-left corner
     * @param y The y-coordinate of the wall's top-left corner
     */
    public Wall(double x,double y) {
        this.x = x;
        this.y = y;
        this.boundingBox = new BoundingBox(x+1,y+1,14,14);
    }


    /**
     * Renders the wall onto the game canvas.
     *
     * @param gc The GraphicsContext used to draw the wall
     */
    public void render(GraphicsContext gc) {
        gc.drawImage(image, x, y,16,16);
    }

}
