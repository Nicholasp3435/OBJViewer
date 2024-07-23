public class Vertex {

    private double x;
    private double y;
    private double z;
    private double[] coords;
    
    public Vertex(double x, double y, double z) {
	this.x = x;
	this.y = y;
	this.z = z;
	this.coords = new double[] {x, y, z};
    }

    public double[] getCoords() {
	return coords;
    }

    public String toString() {
	return "(" + x + ", " + y + ", " + z + ")"; 
    }
} // Point
