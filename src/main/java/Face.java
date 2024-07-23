public class Face {

    public int v1;
    public int v2;
    public int v3;
    private int[] vertices;

    public Face(int v1, int v2, int v3){
	this.v1 = v1;
	this.v2 = v2;
	this.v3 = v3;
	this.vertices = new int[] {v1, v2, v3};
    }

    public int[] getVertices() {
	return vertices;
    }

    public String toString() {
	return "(" + v1 + ", " + v2 + ", " + v3 + ")";
    }
} // Face
