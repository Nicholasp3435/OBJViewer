public class Driver {
    public static void main(String[] args) {
	Matrix A = new Matrix(2,2);
	A.matrix = new Double[][] {{1.,0.},{0.,1.}};
	Matrix B = new Matrix(2,2);
	B.matrix = new Double[][] {{1.,1.},{2.,2.}};
	try {
	    Matrix C = Matrix.multiply(A, B);
	    System.out.println(C);
	} catch (Exception e) {
	    System.out.println("err");
	}

    }
}
