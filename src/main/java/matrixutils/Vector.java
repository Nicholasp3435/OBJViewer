package matrixutils;

import matrixutils.exception.DimensionMismatchException;

public class Vector extends Matrix {

    /**
     * Creates a vector object as a Matrix with 1 column.
     *
     * @param row The amount of rows in the vector.
     * @param vector The Double[][] that represents a vector
     */
    public Vector(final int row, final Double[][] vector) {
	super(row, 1, vector);
    }

    private Vector(final int row) {
	super(row, 1);
    }

    /**
     * Multiplies a vector by a matrix in the order A × v.
     *
     * @param a Matrix A.
     * @param v Vector v.
     *
     * @return Matrix A × Vector v.
     */
    public static Vector multiply(final Matrix a, final Vector v) throws DimensionMismatchException {
	// check if compatible
	if (a.getColLen() != v.getRowLen()) {
	    throw new DimensionMismatchException("Columns of A do not match the rows of v; "
						 + a.getColLen() + " != " + v.getRowLen());
	} // if
	
	Vector c = new Vector(a.getRowLen());

	for (int i = 0; i < a.getRowLen(); i++) {
	    for (int j = 0; j < v.getRowLen(); j++) {
		c.setEntry(i, 0, a.getEntry(i, j) * v.getEntry(j, 0) + c.getEntry(i, 0));
	    } // for
	} // for
	return c;
    } // multiply

    /**
     * Performs an orthographic translation of a vector.
     *
     * @param x The distance to translate in the X.
     * @param y The distance to translate in the Y.
     *
     * @return The tranlated vector.
     */
    public Vector orthoTranslate(final Double x, final Double y) {
	return new Vector(this.getRowLen(), new Double[][] {{this.getEntry(0, 0) + x}, {this.getEntry(1, 0) + y}});
    } // orthoTanslate

    /**
     * Projects this vector onto the 2D plane.
     *
     * @return The projected vector.
     */
    public Vector project() {
	return new Vector(2, new Double[][] {{this.getEntry(0, 0)}, {this.getEntry(1, 0)}});
    } // project

    /**
     * Performs a perspective translation of a 3D vector.
     *
     * @param x The distance to translate in the X.
     * @param y The distance to translate in the Y,
     * @param z The distance to translate in the Z.
     *
     * @return The translated vector.
     */
    public Vector persTranslate(final Double x,	final Double y, final Double z) {
	// sets a [x, y, z] vector into the form [x, y, z, 1]
	Vector vect4 = new Vector(4, new Double[][] {{this.getEntry(0, 0)},
						     {this.getEntry(1, 0)},
						     {this.getEntry(2, 0)},
						     {1.}});
	try {
	    return multiply(Matrix.translation(x, y, z), vect4);
	} catch (Exception e) {
	    System.out.println(e.getMessage());
	    return new Vector(1, new Double[][] {{}});
	} // try
    } // persTranslate

    /**
     * Performs the perspective divide on a vector.
     *
     * @param perspective The perspective matrix to perform the divide.
     *
     * @return The divide matrix.
     */
    public Vector persDivide(final Matrix perspective) {
	try {
	    Matrix pDiv = multiply(perspective, this);
	    return new Vector(4, new Double[][] {{pDiv.getEntry(0, 0) / pDiv.getEntry(3, 0)},
						 {pDiv.getEntry(1, 0) / pDiv.getEntry(3, 0)},
						 {pDiv.getEntry(2, 0) / pDiv.getEntry(3, 0)},
						 {pDiv.getEntry(3, 0)}});
	} catch (Exception e) {
	    System.out.println(e.getMessage());
	    return new Vector(4, new Double[][] {{}});
	} // try
    } // persDivide

    /**
     * Rotates a vector.
     *
     * @param yz The angle to rotate about the YZ plane.
     * @param xz The angle to rotate about the XZ plane.
     * @param xy The angle to rotate about the XY plane.
     *
     * @return A rotated vector.
     */
    public Vector rotate(final Double yz, final Double xz, final Double xy) {
	try {
	    Vector rotatedMatrixA = multiply(yzRotation(yz), this);
	    Vector rotatedMatrixB = multiply(xzRotation(xz), rotatedMatrixA);
	    Vector rotatedMatrixC = multiply(xyRotation(xy), rotatedMatrixB);
	    return rotatedMatrixC;
	} catch (DimensionMismatchException dme) {
	    System.out.println("Rotation error " + dme.getMessage());
	    return null;
	} // try
    } // rotate

    /**
     * Scales a Vector.
     *
     * @param factor The scale factor.
     *
     * @return The scaled vactor.
     */
    public Vector scale(final Double factor) {
	Vector scaled = new Vector(this.getRowLen());
	for (int i = 0; i < scaled.getRowLen(); i++) {
	    scaled.setEntry(i, 0, this.getEntry(i, 0) * factor);
	} // for
	return scaled;
    } // scale

    /**
     * Perfoms a perspective transform of a vector.
     *
     * @param vector The vector to transform.
     * @param zoom The amount to scale up (to zoom) the vector.
     * @param yz The amount to rotate the vector about the yz plane.
     * @param xz The amount to rotate the vector about the xz plane.
     * @param xy The amount to rotate the vector about the xy plane.
     * @param xTrans The amount to translate the vector about the x axis.
     * @param yTrans The amount to translate the vector about the y axis.
     * @param zTrans the amount to translate the vector about the z axis.
     * @param xPan The amount to pan the vector about the x axis.
     * @param yPan The amount to pan the vector about the y axis.
     * @param height The screen height.
     * @param width The screen width.
     * @param fov The fov of the perspective.
     * @param zNear The z distance of the z near plane.
     * @param zFar The z distance of the z far plane.
     * @param xPivot The x coordinate of the yz plane to rotate about.
     * @param yPivot The y coordinate of the xz plane to rotate about.
     * @param zPivot The z coordinate of the xy plane to rotate about.
     *
     * @return The transformed vector.
     */
    public static Vector perspectiveTransform(final Vector vector, final Double zoom,
					      final Double yz, final Double xz, final Double xy,
					      final Double xTrans, final Double yTrans, final Double zTrans,
					      final Double xPan, final Double yPan,
					      final Double height, final Double width, final Double fov,
					      final Double zNear, final Double zFar,
					      final Double xPivot, final Double yPivot, final Double zPivot) {
	Vector v = vector;
	// translates the vector to the pivot
	v = v.persTranslate(-xPivot, -yPivot, -zPivot);
	v = new Vector(3, new Double[][] {{v.getEntry(0, 0)}, {v.getEntry(1, 0)}, {v.getEntry(2, 0)}});

	// rotates the vector
	v = v.rotate(yz, xz, xy);

	// translates the vector back to it's original spot
	v = v.persTranslate(xTrans, yTrans, zTrans);

	// does the perspective divide (makes far things smaller
	v = v.persDivide(Matrix.perspective(height, width, fov, zNear, zFar));

	// projects the vector onto the xy plane
	v = v.project();

	// scales the vector to zoom in/out
	v = v.scale(zoom);

	// pans the vector on the xy plane
	v = v.orthoTranslate(xPan, yPan);
	
	return v;
    } // perspectiveTransform

    /**
     * Subtracts a vector from this.
     *
     * @param b The vector to subtract.
     *
     * @return The vector This - b.
     */
    public final Vector subtract(final Vector b) {
        return new Vector(3, new Double[][] {
		{this.getEntry(0, 0) - b.getEntry(0, 0)},
		{this.getEntry(1, 0) - b.getEntry(1, 0)},
		{this.getEntry(2, 0) - b.getEntry(2, 0)}});
    }

    @Override
    public final String toString() {
        String s = "{";
        for (int i = 0; i < this.getRowLen(); i++) {
            s += this.getEntry(i, 0) + ", ";
        }
        s += "}";
        return s;
    } // toString
    
} // Vector
