package lapr4;

import eapli.framework.util.Utility;

@Utility
public class Application {

	public static final String VERSION = "v1.0 - Shodrone System";
	public static final String COPYRIGHT = "(C) 2025, ShoDrone";

	private static final AppSettings SETTINGS = new AppSettings();

	public static AppSettings settings() {
		return SETTINGS;
	}

	private Application() {
		// private visibility to ensure singleton & utility
	}
}
