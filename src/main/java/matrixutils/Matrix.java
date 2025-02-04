package matrixutils;

import matrixutils.exception.DimensionMismatchException;

public class Matrix {

    /** The number of rows. */
    private int row;
    /** The number of columns. */
    private int col;
    /** The 'matrix'. */
    private Double[][] matrix;

    /**
     * Creats a matrix with a Double[][].
     *
     * @param row The number of rows of the matrix.
     * @param col The number of columns of the matrix.
     * @param matrix The Double[][] to be treated as a matrix.
     */
    public Matrix(final int row, final int col, final Double[][] matrix) {
	this.row = row;
	this.col = col;
	this.matrix = matrix;
    } // Matrix

    /**
     * Creates a default matrix of only 0's.
     *
     * @param row The number of rows of the matrix.
     * @param col The number of columns of the matrix.
     */
    Matrix(final int row, final int col) {
	this.row = row;
	this.col = col;
	this.matrix = new Double[row][col];
	for (int i = 0; i < this.row; i++) {
	    for (int j = 0; j < this.col; j++) {
		matrix[i][j] = 0.0;
	    } // for
	} // for
    } // Matrix

    /**
     * Gets the number of columns of the matrix.
     *
     * @return The number of columns of the matrix.
     */
    public int getColLen() {
	return col;
    } // getColLen

    /**
     * Gets the number of rows of the matrix.
     *
     * @return The number of rows of the matrix.
     */
    public int getRowLen() {
	return row;
    } // getRowLen

    /**
     * Sets the matrix.
     *
     * @param matrix The matrix to be set.
     */
    private void setMatrix(final Double[][] matrix) {
	this.matrix = matrix;
    } // setMatrix

    /**
     * Gets the entry (row, column) of the matrix.
     *
     * @param r The row number.
     * @param c The column number.
     *
     * @return The entry at (row, column).
     */
    public double getEntry(final int r, final int c) {
	return this.matrix[r][c];
    } // getEntry

    /**
     * Sets the entry of the the matrix.
     *
     * @param r The row of the entry to set.
     * @param c The column of the entry to set.
     * @param value The value to set.
     */
    void setEntry(final int r, final int c, final Double value) {
	this.matrix[r][c] = value;
    } // getEntry

    @Override
    public String toString() {
	String str = "{";
	for (int i = 0; i < this.row; i++) {
	    str += "{";
	    for (int j = 0; j < this.col; j++) {
		str += this.matrix[i][j] + "\t";
	    } // for
	    str += "}\n";
	} // for
	str += "}";
	return str;
    } // toString

    /**
     * Multiplies two matrices in the order A × B.
     *
     * @param a Matrix A.
     * @param b Matrix B.
     *
     * @return Matrix A × Matrix B.
     */
    public static Matrix multiply(final Matrix a, final Matrix b) throws DimensionMismatchException {
	// check if compatible
	if (a.col != b.row) {
	    throw new DimensionMismatchException("Columns of A do not match the rows of B; " + a.col + " != " + b.col);
	} // if
	
	Matrix c = new Matrix(a.row, b.col);

	for (int i = 0; i < a.row; i++) {
	    for (int j = 0; j < b.col; j++) {
		for (int k = 0; k < b.row; k++) {
		   c.matrix[i][j] += a.matrix[i][k] * b.matrix[k][j];
		} // for
	    } // for
	} // for
	return c;
    } // multiply

    /**
     * Creates a matrix to rotate a vector about the YZ plane a specified angle.
     *
     * @param t The angle to rotate in radians.
     *
     * @return The rotated Vector.
     */
    static Matrix yzRotation(final Double t) {
	return new Matrix(3, 3, new Double[][] {{1., 0.,          0.},
						{0., Math.cos(t), -Math.sin(t)},
						{0., Math.sin(t), Math.cos(t)}});
    } // yzRotation

    /**
     * Creates a matrix to rotate a vector about the XZ plane a specified angle.
     *
     * @param t The angle to rotate in radians.
     *
     * @return The rotated Vector.
     */
    static Matrix xzRotation(final Double t) {
	return new Matrix(3, 3, new Double[][] {{Math.cos(t),    0., Math.sin(t)},
						{0.,             1., 0.},
						{-Math.sin(t),   0., Math.cos(t)}});
    } // xzRotation

    /**
     * Creates a matrix to rotate a vector about the XY plane a specified angle.
     *
     * @param t The angle to rotate in radians.
     *
     * @return The rotated Vector.
     */
    static Matrix xyRotation(final Double t) {
	return new Matrix(3, 3, new Double[][] {{Math.cos(t), -Math.sin(t),   0.},
						{Math.sin(t), Math.cos(t),    0.},
						{0.,          0.,             1.}});
    } // xyRotation



    /**
     * Makes a perspective matrix that can be used for perspective division.
     *
     * @param h The height of the image plane.
     * @param w The width of the image plane.
     * @param fov The angle of the field of view.
     * @param zNear The distance to the near plane.
     * @param zFar The distance to the far plane.
     *
     * @return The perspective matrix for performing perspective division
     */
    public static Matrix perspective(final Double h, final Double w, final Double fov,
				     final Double zNear, final Double zFar) {
	
	double d = zFar / (zFar - zNear);
	return new Matrix(4, 4, new Double[][] {{w / h, 0., 0., 0.},
						{0., 1 / Math.tan(fov / 2), 0., 0.},
						{0., 0., d, -d * zNear, 0.},
						{0., 0., 1., 0.}});
    } // perspective

    /**
     * Makes a translation matrix that can translate a vector.
     * The vector has to take the form [x, y, z, 1]
     *
     * @param x The amount to translate x.
     * @param y The amount to translate y.
     * @param z The amount to translate z.
     *
     * @return A 4x4 matrix to multply a vector to translate.
     */
    static Matrix translation(final Double x, final Double y, final Double z) {
	return new Matrix(4, 4, new Double[][] {{1., 0., 0., x},
						{0., 1., 0., y},
						{0., 0., 1., z},
						{0., 0., 0., 1.}});
    } // translation
    
} // Martrix
