package vekta.menu.handle;

import processing.event.KeyEvent;

public class DebugMenuHandle extends MenuHandle {
	@Override
	public void keyPressed(KeyEvent event) {
		super.keyPressed(event);

		if(event.getKey() == '`') {
			getMenu().close();
		}
	}
}
