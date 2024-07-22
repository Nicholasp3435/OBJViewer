public class Face {

    private Vertex v1;
    private Vertex v2;
    private Vertex v3;
    private Vertex[] vertices;

    public Face(Vertex v1, Vertex v2, Vertex v3){
	this.v1 = v1;
	this.v2 = v2;
	this.v3 = v3;
	this.vertices = new Vertex[] {v1, v2, v3};
    }

    public Vertex[] getVertices() {
	return vertices;
    }
} // Face
