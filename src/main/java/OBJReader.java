import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

public class OBJReader {

    public Vertex[] vertices;
    public Face[] faces;
    public int vertexCount = 0;
    public int faceCount = 0;

    public OBJReader(String fileName) throws FileNotFoundException, IOException {
	BufferedReader br = new BufferedReader(new FileReader(fileName));
	String line;
	while ((line = br.readLine()) != null) {
	    String[] parsed = line.split("\\s+");
	    if (parsed[0].equals("v")) {
		vertexCount++;
	    } else if (parsed[0].equals("f")) {
		faceCount++;
	    }
	}
	vertices = new Vertex[vertexCount];
	faces = new Face[faceCount];
	
	
	BufferedReader br2 = new BufferedReader(new FileReader(fileName));
	int i = 0;
	int j = 0;
	while ((line = br2.readLine()) != null) {
	    String[] parsed = line.split("\\s+");
	    if (parsed[0].equals("v")) {
		Double x = Double.parseDouble(parsed[1]);
		Double y = Double.parseDouble(parsed[2]);
		Double z = Double.parseDouble(parsed[3]);
		vertices[i] = new Vertex(x, y, z);
		//System.out.println(vertices[i]);
		i++;
	    } else if (parsed[0].equals("f")) {
		int v1 = Integer.parseInt(parsed[1].split("/")[0]) - 1;
		int v2 = Integer.parseInt(parsed[2].split("/")[0]) - 1;
		int v3 = Integer.parseInt(parsed[3].split("/")[0]) - 1;
		faces[j] = new Face(v1, v2, v3);
		//System.out.println(faces[i]);
		j++;
	    }
	}
    }


	

    public Vertex[] getVertices() {
	return vertices;
    }

    public Face[] getFaces() {
	return faces;
    }
}
