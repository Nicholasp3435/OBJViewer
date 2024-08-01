package MatrixUtils;

import MatrixUtils.Matrix;
import MatrixUtils.exception.DimensionMismatchException;

public class Vector extends Matrix {

    public Vector(int row, Double[][] vector) {
	super(row, 1, vector);
    }

    private Vector(int row) {
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
	    throw new DimensionMismatchException("Columns of A do not match the rows of v; " + a.getColLen() + " != " + v.getRowLen());
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
     * Rprojects a vector onto the 2D plane.
     *
     * @param v The vector to project.
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

    public static Vector perspectiveTransform(Vector v, Double zoom,
					      Double yz, Double xz, Double xy,
					      Double xTrans, Double yTrans, Double zTrans,
					      Double xPan, Double yPan,
					      Double height, Double width, Double fov,
					      Double zNear, Double zFar,
                          Double xPivot, Double yPivot, Double zPivot) {
    v = v.persTranslate(-xPivot, -yPivot, -zPivot);
    v = new Vector(3, new Double[][] {{v.getEntry(0,0)}, {v.getEntry(1,0)}, {v.getEntry(2,0)}});
	v = v.rotate(yz, xz, xy);
	v = v.persTranslate(xTrans, yTrans, zTrans);
	v = v.persDivide(Matrix.perspective(height, width, fov, zNear, zFar));
	v = v.project();
	v = v.scale(zoom);
	v = v.orthoTranslate(xPan, yPan);
	
	return v;
    }

    public Vector add(Vector b) {
        return new Vector(3, new Double[][] {{this.getEntry(0,0) + b.getEntry(0,0)},{this.getEntry(1,0) + b.getEntry(1,0)},{this.getEntry(2,0) + b.getEntry(2,0)}});
    }

    public Vector subtract(Vector b) {
        return new Vector(3, new Double[][] {{this.getEntry(0,0) - b.getEntry(0,0)},{this.getEntry(1,0) - b.getEntry(1,0)},{this.getEntry(2,0) - b.getEntry(2,0)}});
    }

    public String toString() {
        String s = "{";
        for (int i = 0; i < this.getRowLen(); i++) {
            s += this.getEntry(i,0) + ", ";
        }
        s += "}";
        return s;
    }
    
}
