public interface IRedisTransaction {

    public String discard() throws NotImplementedException;

    public Object[] exec() throws NotImplementedException;

    public String multi() throws NotImplementedException;

    public String unwatch() throws NotImplementedException;

    public String watch(String key) throws NotImplementedException;

}
