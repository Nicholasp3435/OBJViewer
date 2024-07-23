import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

public class OBJReader {

    public Vertex[] vertices;
    public Face[] faces;
    public int vertexCount = 0;
    public int faceCount = 0;

    public OBJReader(String fileName) {
	try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {
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
	} catch (FileNotFoundException fnfe) {
	    System.out.println(fnfe.getMessage());  
	} catch (IOException ioe) {
	    System.out.println(ioe.getMessage());
	}
	
	try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {
	    String line;
	    int i = 0;
	    while ((line = br.readLine()) != null) {
		String[] parsed = line.split("\\s+");
		if (parsed[0].equals("v")) {
		    Double x = Double.parseDouble(parsed[1]);
		    Double y = Double.parseDouble(parsed[2]);
		    Double z = Double.parseDouble(parsed[3]);
		    vertices[i] = new Vertex(x, y, z);
		    System.out.println(vertices[i]);
		    i++;
		}
	    }
	} catch (FileNotFoundException fnfe) {
	    System.out.println(fnfe.getMessage());  
	} catch (IOException ioe) {
	    System.out.println(ioe.getMessage());
	}

	System.out.println();
	
	try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {
	    String line;
	    int i = 0;
	    while ((line = br.readLine()) != null) {
		String[] parsed = line.split("\\s+");
		if (parsed[0].equals("f")) {
		    Vertex v1 = vertices[Integer.parseInt(parsed[1]) - 1];
		    Vertex v2 = vertices[Integer.parseInt(parsed[2]) - 1];
		    Vertex v3 = vertices[Integer.parseInt(parsed[3]) - 1];
		    faces[i] = new Face(v1, v2, v3);
		    System.out.println(faces[i]);
		    i++;
		}
	    }
	} catch (FileNotFoundException fnfe) {
	    System.out.println(fnfe.getMessage());  
	} catch (IOException ioe) {
	    System.out.println(ioe.getMessage());
	}
    }
}
