import java.lang.Math;

public class Matrix {

    private int row;
    private int col;
    private Double[][] matrix;


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

    public int getColLen() {
	return col;
    }

    public int getRowLen() {
	return row;
    }

    public void setMatrix(Double[][] matrix) {
	this.matrix = matrix;
    }

    public double getEntry(int r, int c) {
	return this.matrix[r][c];
    }

    public String toString() {
	String str = "{";
	for (int i = 0; i < this.row; i++) {
	    str += "{";
	    for (int j = 0; j < this.col; j++) {
		str += this.matrix[i][j] + "\t";
	    }
	    str += "}\n";
	}
	str += "}";
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
	    {{1., 0.,          0.},
	     {0., Math.cos(t), -1*Math.sin(t)},
	     {0., Math.sin(t), Math.cos(t)}};
	return rotate;
    }

    public static Matrix xzRotation(Double t) {
	Matrix rotate = new Matrix(3,3);
	rotate.matrix = new Double[][]
	    {{Math.cos(t),    0., Math.sin(t)},
	     {0.,             1., 0.},
	     {-1*Math.sin(t), 0., Math.cos(t)}};
	return rotate;
    }

    public static Matrix xyRotation(Double t) {
	Matrix rotate = new Matrix(3,3);
	rotate.matrix = new Double[][]
	    {{Math.cos(t), -1*Math.sin(t), 0.,},
	     {Math.sin(t), Math.cos(t),    0.},
	     {0.,          0.,             1.}};
	return rotate;
    }

    

    public Matrix orthoTranslate(Double x, Double y) {
	Matrix trans = new Matrix(row, 1);
	trans.setMatrix(new Double[][] {{this.getEntry(0,0) + x}, {this.getEntry(1,0) + y}});
	return trans;
    }

    public Matrix persTranslate(Double x, Double y, Double z) {
	Matrix vect4 = new Matrix(4,1);
	vect4.setMatrix(new Double[][] {{this.getEntry(0,0)}, {this.getEntry(1,0)}, {this.getEntry(2,0)}, {1.}});
	try {
	    Matrix trans = multiply(translation(x, y, z), vect4);
	    return trans;
	} catch (Exception e) {
	    System.out.println(e.getMessage());
	    return new Matrix(4,1);
	}
    }

    public Matrix persDivide(Matrix perspective) {
	try {
	    Matrix pDiv = multiply(perspective, this);
	    Matrix aDiv = new Matrix(4,1);
	    aDiv.setMatrix(new Double[][]
		{{pDiv.getEntry(0,0)/pDiv.getEntry(3,0)},
		 {pDiv.getEntry(1,0)/pDiv.getEntry(3,0)},
		 {pDiv.getEntry(2,0)/pDiv.getEntry(3,0)},
		 {pDiv.getEntry(3,0)}});
	    return aDiv;	      
	} catch (Exception e) {
	    System.out.println(e.getMessage());
	    return new Matrix(4,1);
	}
    }

    public Matrix scale(Double factor) {
	Matrix scaling = new Matrix(1, 1);
	scaling.setMatrix(new Double[][] {{factor}});
	try {
	    return multiply(this, scaling);
	} catch (Exception e) {
	    return new Matrix(row, col);
	}
    }

    public Matrix rotate(Double yz, Double xz, Double xy) {
	try {
	    Matrix rotatedMatrixA = multiply(yzRotation(yz), this);
	    Matrix rotatedMatrixB = multiply(xzRotation(xz), rotatedMatrixA);
	    Matrix rotatedMatrixC = multiply(xyRotation(xy), rotatedMatrixB);
	    return rotatedMatrixC;
	} catch (Exception e) {
	    System.out.println("Rotation error " + e.getMessage());
	    return new Matrix(3, 3);
	}
    }

    public static Matrix orthographic(Matrix v) {
	Matrix ortho = new Matrix(2,1);
	ortho.setMatrix(new Double[][] {{v.getEntry(0,0)}, {v.getEntry(1,0)}});
	return ortho;
    }

    public static Matrix perspective(double h, double w, double fov, double zNear, double zFar) {
	Matrix pers = new Matrix(4,4);
	double d = zFar / (zFar - zNear);
	pers.matrix = new Double[][]
	    {{h/w, 0., 0., 0.},
	     {0., 1/Math.tan(fov/2), 0., 0.},
	     {0., 0., d, -d * zNear, 0.},
	     {0., 0., 1., 0.}};
	return pers;
    }

    public static Matrix translation(Double x, Double y, Double z) {
	Matrix trans = new Matrix(4,4);
	trans.setMatrix(new Double[][] {{1., 0., 0., x},
					{0., 1., 0., y},
					{0., 0., 1., z},
					{0., 0., 0., 1.}});
	return trans;
    }
}
