package jpm.mimacom.tasks;

public abstract class TestWrapper {
	final StringBuilder s;
	final String key;

	public TestWrapper(final StringBuilder s, final String key) {
		this.s = s;
		this.key = key;
	}

	public abstract void execute() throws Exception;

	public void run() {
		try {
			execute();
			System.out.println(key + ": TEST OK!");
		} catch (AssertionError a) {
			s.append("\n" + key + ": " + a.getMessage());
		} catch (Throwable t) {
			t.printStackTrace(System.err);
			s.append("\n" + key + ": " + ": " + t.getClass().getName() + ": "
					+ t.getMessage());
		}
	}
}