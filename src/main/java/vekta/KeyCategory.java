package vekta;

public enum KeyCategory {
	SHIP_CONTROLS,
	SHIP_COMPUTER,
	QUICK_ACTIONS,
	MENU_CONTROLS;

	private final String title;

	KeyCategory() {
		String title = name().replace("_", " ");
		this.title = title.charAt(0) + title.substring(1).toLowerCase();
	}

	public String getTitle() {
		return title;
	}
}
