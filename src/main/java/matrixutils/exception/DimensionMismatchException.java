package matrixutils.exception;

public class DimensionMismatchException extends Exception {
    
    /**
     * Creates a custom exception for specifying that the dims of a matrix is not proper for the opperation.
     *
     * @param message The message of the exception.
     */
    public DimensionMismatchException(final String message) {
	super(message);
    }
}
