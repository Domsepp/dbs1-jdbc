package java_introduction;

public class Coordinate {

    private int x;
    private int y;

    //Constructor has the same name as the class:
    public Coordinate(int x, int y) {
        this.x = x; //this identifies the current object
        this.y = y;
    }

    //method declaration:
    //<allowed access> <return type> <methodName>(<params>)
    //access types are either
    //  public (every class has access to this)
    //  package (every class in the same package has access to this) <-- package is the default and there is no keyword for it, if you leave out the access type, it defaults to package
    //  protected (only subclasses of this class has access to this)
    //  private (only this class has access to this)
    public double euclidianDistance(Coordinate point2) {
        int xDifference = x - point2.x;
        int yDifference = y - point2.y;
        return Math.sqrt(xDifference*xDifference + yDifference*yDifference);
    }

    //good practice in java: make variables private and grant access via getter and setter methods:
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
