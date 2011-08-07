package math.groups;

public interface TestFunction<T> {

	boolean check(int x);

	T getRecord(int x);
}
