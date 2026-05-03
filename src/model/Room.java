package model;
import java.io.Serializable;
import java.util.Map;

public class Room implements Serializable {
    
    private int myX;
    private int myY;
    private Map<Direction, Door> myDoors;
    private boolean myIsVisited;

    public Room(int theX, int theY) {
        this.myX = theX;
        this.myY = theY;
        this.myIsVisited = false;
    }

    public Door getDoor(Direction theDir) {
        return myDoors.get(theDir);
    }

    public boolean hasDoor(Direction theDir) {
        return myDoors.containsKey(theDir);
    }

    public int getX() {
        return myX;
    }

    public int getY() {
        return myY;
    }

    public boolean isVisited() {
        return myIsVisited;
    }

    public void setVisited(boolean theVisited) {
        this.myIsVisited = theVisited;
    }

    protected void addDoor(Direction theDir, Door theDoor) {
        myDoors.put(theDir, theDoor);
    }

    public String toString() {
        return String.format("Room [x=%b, y=%b, doors=%b, visited=%b]", 
            myX, myY, myDoors.toString(), myIsVisited);
    }
}
