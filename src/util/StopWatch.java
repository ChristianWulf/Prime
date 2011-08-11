package util;

public class StopWatch<T> {

	private T result;

	public interface MethodWithResult<T> {
		T run();
	}

	public long measure(MethodWithResult<T> statements) {
		long startTime = System.currentTimeMillis();
		result = statements.run();
		long stopTime = System.currentTimeMillis();
		return stopTime - startTime;
	}

	public T getResult() {
		return result;
	}

}
