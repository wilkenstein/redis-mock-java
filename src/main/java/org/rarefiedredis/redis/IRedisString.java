/**
 * Interface for redis string commands.
 */
public interface IRedisString {
    /**
     * Append value onto key.
     *
     * @param key The key
     * @param value The value
     *
     * @throws WrongTypeException If key is not a string
     * @throws NotImplementedException If the command is unimplemented.
     *
     * @return The length of the key after the append operation.
     */
    Long append(String key, String value) throws WrongTypeException, NotImplementedException;
    /**
     * Count the number of set bits in a string.
     *
     * @param key The key
     *
     * @throws WrongTypeException If key is not a string
     * @throws NotImplementedException If the command is unimplemented.
     *
     * @return The number of bits in the string set to 1
     */
    Long bitcount(String key, long ... options) throws WrongTypeException, NotImplementedException;
    /**
     * Perform a bitwise operation between multiple keys and store the result in the destination key.
     *
     * @param operation The operation to perform. Valid values are "and", "or", "xor", and "not".
     * @param destkey The destination key to store the operation result in.
     * @param keys The keys to operate against.
     *
     * @throws WrongTypeException If key is not a string
     * @throws NotImplementedException If the command is unimplemented.
     *
     * @return The length of the string stored in destkey 
     */
    Long bitop(String operation, String destkey, String ... keys) throws WrongTypeException, NotImplementedException;
    /**
     * Return the position of the first bit set to 0 or 1 in a string.
     *
     * @param key The key
     * @param bit 0|1
     *
     * @throws WrongTypeException If key is not a string
     * @throws BitArgException If bit is not equal to 0 or 1
     * @throws NotImplementedException If the command is unimplemented.
     *
     * @return The bit position.
     */
    Long bitpos(String key, long bit, long ... options) throws WrongTypeException, BitArgException, NotImplementedException;
    /**
     * Decrement the number stored at key by one.
     *
     * @param key The key
     *
     * @throws WrongTypeException If key is not a string
     * @throws NotIntegerException If key does not hold an integer value
     * @throws NotImplementedException If the command is unimplemented.
     *
     * @return The new value of the string after the decrement operation, i.e., value - 1
     */
    Long decr(String key) throws WrongTypeException, NotIntegerException, NotImplementedException;
    /**
     * Decrement the number stored at key by the given decrement.
     *
     * @param key The key
     * @param decrement The number to decrement by
     *
     * @throws WrongTypeException If key is not a string
     * @throws NotIntegerException If key does not hold an integer value
     * @throws NotImplementedException If the command is unimplemented.
     *
     * @return The new value of the string after the decrement operation, i.e., value - decrement
     */
    Long decrby(String key, long decrement) throws WrongTypeException, NotIntegerException, NotImplementedException;
    /**
     * Get the value of a key.
     *
     * @param key The key
     *
     * @throws WrongTypeException If key is not a string
     * @throws NotImplementedException If the command is unimplemented.
     *
     * @return The value of key
     */
    String get(String key) throws WrongTypeException, NotImplementedException;
    /**
     * Returns the bit value at offset in the string value stored at key.
     *
     * @param key The key
     * @param offset The offset within the string
     *
     * @throws WrongTypeException If key is not a string
     * @throws NotImplementedException If the command is unimplemented.
     *
     * @return The bit value at offset
     */
    Boolean getbit(String key, long offset) throws WrongTypeException, NotImplementedException;
    /**
     * Returns the substring of the string value stored at key, determined by the inclusive offsets
     * start and end.
     *
     * @param key The key
     * @param start The start index of the range
     * @param end The end index of the range
     *
     * @throws WrongTypeException If key is not a string
     * @throws NotImplementedException If the command is unimplemented.
     *
     * @return The range
     */
    String getrange(String key, long start, long end) throws WrongTypeException, NotImplementedException;
    /**
     * Atomically set a key and return its previous value.
     *
     * @param key The key
     * @param value The new value
     *
     * @throws WrongTypeException If key is not a string
     * @throws NotImplementedException If the command is unimplemented.
     *
     * @return The previous value
     */
    String getset(String key, String value) throws WrongTypeException, NotImplementedException;
    /**
     * Increment the number stored at key by one.
     *
     * @param key The key
     *
     * @throws WrongTypeException If key is not a string
     * @throws NotIntegerException If key does not hold an integer value
     * @throws NotImplementedException If the command is unimplemented.
     *
     * @return The new value of the string after the decrement operation, i.e., value + 1
     */
    Long incr(String key) throws WrongTypeException, NotIntegerException, NotImplementedException;
    /**
     * Increment the number stored at key by the given increment.
     *
     * @param key The key
     *
     * @throws WrongTypeException If key is not a string
     * @throws NotIntegerException If key does not hold an integer value
     * @throws NotImplementedException If the command is unimplemented.
     *
     * @return The new value of the string after the decrement operation, i.e., value + increment
     */
    Long incrby(String key, long increment) throws WrongTypeException, NotIntegerException, NotImplementedException;
    /**
     * Increment the number stored at key by the given increment.
     *
     * @param key The key
     *
     * @throws WrongTypeException If key is not a string
     * @throws NotFloatException If key does not hold a floating point value
     * @throws NotImplementedException If the command is unimplemented.
     *
     * @return The new value of the string after the decrement operation, i.e., value + increment, as a string.
     */
    String incrbyfloat(String key, double increment) throws WrongTypeException, NotFloatException, NotImplementedException;
    /**
     * Returns the values of all specified keys. If a given key does not hold a string value,
     * or does not exist, null is returned for that key in the returned array.
     *
     * @throws NotImplementedException If the command is unimplemented.
     *
     * @return List of values at the specified keys
     */
    String[] mget(String ... keys) throws NotImplementedException;
    /**
     * Sets the given keys to their respective values. Existing keys will be overwritten.
     *
     * @throws ArgException If the given arguments are not valid
     * @throws NotImplementedException If the command is unimplemented.
     *
     * @return "OK"
     */
    String mset(String ... keyvalues) throws ArgException, NotImplementedException;
    /**
     * Sets the given keys to their respective values. If at least one of the keys already
     * exists, none of the keys are set.
     *
     * @throws ArgException If the given arguments are not valid
     * @throws NotImplementedException If the command is unimplemented.
     *
     * @return 1 if all of the keys were set, or 0 if at least 1 key already existed and
     *         no operation was performed.
     */
    Boolean msetnx(String ... keyvalues) throws ArgException, NotImplementedException;
    /**
     * Set and expire a key in milliseconds.
     *
     * @param key The key
     * @param milliseconds The expiry time in milliseconds
     * @param value The value
     *
     * @throws NotImplementedException If the command is unimplemented.
     *
     * @return "OK"
     */
    String psetex(String key, long milliseconds, String value) throws NotImplementedException;
    /**
     * Set key to hold the string value.
     *
     * @param key The key
     * @param value The value
     *
     * @throws NotImplementedException If the command is unimplemented.
     * @throws SyntaxErrorException If the given options were not valid.
     *
     * @return "OK" if the key was set, or null if the operation failed.
     */
    String set(String key, String value, String ... options) throws NotImplementedException, SyntaxErrorException;
    /**
     * Sets or clears the bit at offset in the string value stored at key.
     *
     * @param key The key
     * @param offset The offset
     * @param value Whether to set or clear the bit (set = true, clear = false)
     *
     * @throws WrongTypeException If key is not a string
     * @throws NotImplementedException If the command is unimplemented.
     *
     * @return The original bit value at offset
     */
    Long setbit(String key, long offset, boolean value) throws WrongTypeException, NotImplementedException;
    /**
     * Set and expire a key in seconds.
     *
     * @param key The key
     * @param seconds The expiry time in seconds
     * @param value The value
     *
     * @throws NotImplementedException If the command is unimplemented.
     *
     * @return "OK"
     */
    String setex(String key, int seconds, String value) throws NotImplementedException;
    /**
     * Set a key only if it does not already exist.
     *
     * @param key The key
     * @param value The value
     *
     * @throws NotImplementedException If the command is unimplemented.
     *
     * @return 1 if the key was set; 0 if it already existed and no operation was performed.
     */
    Long setnx(String key, String value) throws NotImplementedException;
    /**
     * Ovewrite part of the string starting at offset with value.
     *
     * @param key The key
     * @param offset Where to start. If greater than the length of the string,
     *               the string is padded out to the offset.
     * @param value The value
     *
     * @throws WrongTypeException If key is not a string
     * @throws NotImplementedException If the command is unimplemented.
     *
     * @return The length of the string after this operation
     */
    Long setrange(String key, long offset, String value) throws WrongTypeException, NotImplementedException;
    /**
     * Return the length of the string at key.
     *
     * @param key The key
     *
     * @throws WrongTypeException If key is not a string
     * @throws NotImplementedException If the command is unimplemented.
     *
     * @return The length of the string
     */
    Long strlen(String key) throws WrongTypeException, NotImplementedException;
}
