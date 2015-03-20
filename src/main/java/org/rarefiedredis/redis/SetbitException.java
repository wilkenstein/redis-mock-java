public final class SetbitException extends Exception {
    
    public SetbitException() {
        super("ERR bit is not an integer or out of range");
    }
}
