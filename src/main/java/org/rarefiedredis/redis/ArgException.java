public final class ArgException extends Exception {
    
    public ArgException(String command) {
        super("ERR wrong number of arguments for \'" + command + "\' command");
    }
}
