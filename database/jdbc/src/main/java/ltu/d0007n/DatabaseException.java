package ltu.d0007n;

public class DatabaseException extends Exception {
	public DatabaseException(String msg) {
		super(msg);
	}
    public DatabaseException(String msg, Exception e) {
        super(msg, e);
    }
}
