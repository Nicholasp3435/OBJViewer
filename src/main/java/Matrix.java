import java.lang.Math;

public class Matrix {

    private int row;
    private int col;
    public Double[][] matrix;


    public Matrix (int row, int col) {
	this.row = row;
	this.col = col;
	this.matrix = new Double[row][col];
	for (int i = 0; i < this.row; i++) {
	    for (int j = 0; j < this.col; j++) {
		matrix[i][j] = 0.0;
	    }
	}
    }

    public String toString() {
	String str = "";
	for (int i = 0; i < this.row; i++) {
	    for (int j = 0; j < this.col; j++) {
		str += this.matrix[i][j] + "\t";
	    }
	    str += "\n";
	}
	return str;
    }

    public static Matrix multiply(Matrix A, Matrix B) throws Exception {
	// check if compatible
	if (A.col != B.row) {
	    throw new Exception("Incompatible multiplication");
	}
	
	Matrix C = new Matrix(A.row, B.col);
	
	for (int i = 0; i < A.row; i++) {
	    for (int j = 0; j < B.col; j++) {
		for (int k = 0; k < B.row; k++) {
		    C.matrix[i][j] += A.matrix[i][k] * B.matrix[k][j];
		}
	    }
	}
	return C;
    }

    public static Matrix yzRotation(Double t) {
	Matrix rotate = new Matrix(3,3);
	rotate.matrix = new Double[][]
	    {{1., 0., 0.},
	     {0., Math.cos(t), 0-Math.sin(t)},
	     {0., Math.sin(t), Math.cos(t)}};
	return rotate;
    }

    public static Matrix xzRotation(Double t) {
	Matrix rotate = new Matrix(3,3);
	rotate.matrix = new Double[][]
	    {{Math.cos(t), 0., Math.sin(t)},
	     {0., 1., 0.},
	     {0-Math.sin(t), 0., Math.cos(t)}};
	return rotate;
    }

    public static Matrix xyRotation(Double t) {
	Matrix rotate = new Matrix(3,3);
	rotate.matrix = new Double[][]
	    {{Math.cos(t), 0-Math.sin(t), 0.,},
	     {Math.sin(t), Math.cos(t), 0.},
	     {0., 0., 1.}};
	return rotate;
    }

    public Matrix rotate(Double yz, Double xz, Double xy) {
	try {
	    Matrix rotatedMatrixA = multiply(this, yzRotation(yz));
	    Matrix rotatedMatrixB = multiply(rotatedMatrixA, xzRotation(xz));
	    Matrix rotatedMatrixC = multiply(rotatedMatrixB, xyRotation(xy));
	    return rotatedMatrixC;
	} catch (Exception e) {
	    return new Matrix(3, 3);
	}
    }

    public static Matrix orthographic(Matrix v) {
	Matrix ortho = new Matrix(2,1);
	ortho.matrix = new Double[][] {{v.matrix[0][0]},{v.matrix[1][0]}};
	return ortho;
    }
}
