import MatrixUtils.Vector;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

public class OBJReader {

    /** The array of vertices. */
    private Vertex[] vertices;
    /** The array of faces. */
    private Face[] faces;
    /** The amount of vertices. */
    private int vertexCount = 0;
    /** The amound of faces. */
    private int faceCount = 0;

    /** Used by the constructor to get the X component of the OBJ vector. */
    private static final int X_COMPONENT = 1;
    /** Used by the constructor to get the Y component of the OBJ vector. */
    private static final int Y_COMPONENT = 2;
    /** Used by the constructor to get the Z component of the OBJ vector. */
    private static final int Z_COMPONENT = 3;

    /** Used by the constructor to get the first Vertex of the OBJ vector. */
    private static final int VERTEX_1 = 1; 
    /** Used by the constructor to get the second Vertex of the OBJ vector. */
    private static final int VERTEX_2 = 2;
    /** Used by the constructor to get the third Vertex of the OBJ vector. */
    private static final int VERTEX_3 = 3;

    /**
     * Creates an OBJReader. It will read an OBJ file and set the vertices and
     * faces into an array.
     *
     * @param fileName The OBJ file to read.
     */
    public OBJReader(final String fileName)
	throws FileNotFoundException, IOException {

	// Counts the faces and vertices to make the arrays
	BufferedReader br = new BufferedReader(new FileReader(fileName));
	String line;
	while ((line = br.readLine()) != null) {
	    String[] parsed = line.split("\\s+");
	    if (parsed[0].equals("v")) {
		vertexCount++;
	    } else if (parsed[0].equals("f")) {
		faceCount++;
	    }
	} // while
	
	vertices = new Vertex[vertexCount];
	faces = new Face[faceCount];
	
	// Populates the arrays with the vertices and faces
	BufferedReader br2 = new BufferedReader(new FileReader(fileName));
	int i = 0;
	int j = 0;
	while ((line = br2.readLine()) != null) {
	    String[] parsed = line.split("\\s+");
	    if (parsed[0].equals("v")) {
		// OBJ has the form 'v x y z (w)'
		Double x = Double.parseDouble(parsed[X_COMPONENT]);
		Double y = Double.parseDouble(parsed[Y_COMPONENT]);
		Double z = Double.parseDouble(parsed[Z_COMPONENT]);
		vertices[i] = new Vertex(x, y, z);
		i++;
	    } else if (parsed[0].equals("f")) {
		// OBJ has the form 'f v1/(formats) v2/(formats) v3/(formats)'
		int v1 = Integer.parseInt(parsed[VERTEX_1].split("/")[0]) - 1; 
		int v2 = Integer.parseInt(parsed[VERTEX_2].split("/")[0]) - 1;
		int v3 = Integer.parseInt(parsed[VERTEX_3].split("/")[0]) - 1;
		faces[j] = new Face(v1, v2, v3);
		j++;
	    } // if
	} // while
    } // OBJReader

    /**
     * @return The array of Vertices to make a face.
     */
    public Vertex[] getVertices() {
	return vertices;
    } // getVertices

    /**
     * @return The array of Faces to make each polygon.
     */
    public Face[] getFaces() {
	return faces;
    } // getFaces

    /**
     * @return The number of faces of the OBJ.
     */
    public int getFaceCount() {
	return faceCount;
    } // getFaceCount

    public Vertex getFurthest() {
	Vertex furthest = new Vertex(0., 0., 0.);

	for (int i = 0; i < vertices.length; i++) {
	    if (vertices[i].distanceFromCenter() > furthest.distanceFromCenter()) {
		furthest = vertices[i];
	    }
	}

	return furthest;
    }

    public Vector getCenter() {
	Double xMean = 0.;
	Double yMean = 0.;
	Double zMean = 0.;

	for (int i = 0; i < vertices.length; i++) {
	    xMean += vertices[i].getX();
	    yMean += vertices[i].getY();
	    zMean += vertices[i].getZ();
	}

	xMean /= vertices.length;
	yMean /= vertices.length;
	zMean /= vertices.length;

	return new Vector(3, new Double[][] {{xMean}, {yMean}, {zMean}});
    }
    
} // OBJReader
