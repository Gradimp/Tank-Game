/**
 * Represents a generic movable entity in the game world.
 * This class provides basic position and movement logic
 * based on directional input.
 */
public class MovableEntity {
    protected double x,y,speed;

    /**
     * Updates the position of the entity based on the given direction.
     * Directions are interpreted as:
     *     0 - Right
     *     1 - Up
     *     2 - Left
     *     3 - Down
     *
     * @param direction The direction in which to move the entity
     */
    public void reposition(int direction){
        switch (direction) {
            case 0:
                x += speed;
                break;
            case 1:
                y -= speed;
                break;
            case 2:
                x -= speed;
                break;
            case 3:
                y += speed;
                break;
        }
    }
}
