public class Vertex {

    private int x;
    private int y;
    private int z;
    private int[] coords;
    
    public Vertex(int x, int y, int z) {
	this.x = x;
	this.y = y;
	this.z = z;
	this.coords = new int[] {x, y, z};
    }

    public int[] getCoords() {
	return coords;
    }
} // Point
