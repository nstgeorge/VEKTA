package vekta.menu.handle;

import processing.event.KeyEvent;
import vekta.menu.Menu;

public class DebugMenuHandle extends MenuHandle {
	@Override
	public void keyPressed(Menu menu, KeyEvent event) {
		super.keyPressed(menu, event);

		if(event.getKey() == '`') {
			menu.close();
		}
	}
}
