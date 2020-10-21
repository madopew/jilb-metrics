package jilbMetrics.exceptions;

public class DirectionNotCorrectException extends RuntimeException{
    public DirectionNotCorrectException() {
        super("direction is not +1 or -1");
    }
}
