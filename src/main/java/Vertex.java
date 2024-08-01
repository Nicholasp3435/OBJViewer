import MatrixUtils.Vector;


public class Vertex {

    /** The x component. */
    private Double x;
    /** The y component. */
    private Double y;
    /** The z component. */
    private Double z;
    /** The coordinates of the vertex; it is in the form [x, y, z]. */
    private Double[] coords;

    /**
     * Creates a 3D Vertex with an X, Y, and Z components.
     *
     * @param x The x component
     * @param y The y component
     * @param z The z component
     */
    public Vertex(final Double x, final Double y, final Double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.coords = new Double[] {x, y, z};
    } // Vertex

    /**
     * @return The x component.
     */
    public Double getX() {
	return this.x;
    } // getX
    
    /**
     * @return The y component.
     */
    public Double getY() {
	return this.y;
    } // getY
    
    /**
     * @return The z component.
     */
    public Double getZ() {
	return this.z;
    } // getZ

    /**
     * Retruns the coordinates of the vertex.
     *
     * @return the coordinates of the vertex.
     */
    public Double[] getCoords() {
	return coords;
    } // getCoords

    /**
     * Convetrs the coordinates of the vertex to a vector.
     *
     * @return A vector with the same coordinates as the vertex.
     */
    public Vector toVector() {
	return new Vector(3, new Double[][] {{this.x}, {this.y}, {this.z}});
    }

    public Double distanceFromCenter(Vector center) {
	Vector v = this.toVector();
	v = v.subtract(center);
	return Math.pow(Math.pow(v.getEntry(0,0), 2) + Math.pow(v.getEntry(1,0), 2) + Math.pow(v.getEntry(2,0), 2), 0.5);
    }

    @Override
    public final String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    } // toString

    
    
} // Vertex
