public class Face {

    /** The index of the first vertex. */
    private int v1;
    /** The index of the second vertex. */
    private int v2;
    /** The index of the third vertex. */
    private int v3;
    /** The array of vertices. */
    private int[] vertices;

    /**
     * Creates a Face with 3 vertices.
     *
     * @param v1 The index of the first vertex.
     * @param v2 The index of the second vertex.
     * @param v3 The index of the third index.
     */
    public Face(final int v1, final int v2, final int v3) {
	this.v1 = v1;
	this.v2 = v2;
	this.v3 = v3;
	this.vertices = new int[] {v1, v2, v3};
    } // Face

    /**
     * Gets the vertices of the face.
     *
     * @return The vertices of the face.
     */
    public int[] getVertices() {
	return vertices;
    } // getVertices

    @Override
    public final String toString() {
	return "(" + v1 + ", " + v2 + ", " + v3 + ")";
    } // toString
} // Face
