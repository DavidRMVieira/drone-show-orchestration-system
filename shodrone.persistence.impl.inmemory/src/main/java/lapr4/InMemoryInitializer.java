package lapr4;

import lapr4.bootstrapers.ShodroneBootstrapper;
import lapr4.bootstrapers.demo.ShodroneDemoBootstrapper;

final class InMemoryInitializer {

	private static class LazyHolder {
		private static final InMemoryInitializer INSTANCE = new InMemoryInitializer();

		private LazyHolder() {
		}
	}

	private boolean initialized;

	private InMemoryInitializer() {
	}

	private synchronized void initialize() {
		if (!initialized) {
			new ShodroneBootstrapper().execute();
			new ShodroneDemoBootstrapper().execute();
			initialized = true;
		}
	}

	public static void init() {
		LazyHolder.INSTANCE.initialize();
	}
}
