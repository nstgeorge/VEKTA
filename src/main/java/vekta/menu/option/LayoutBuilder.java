package vekta.menu.option;

import vekta.display.Layout;

import java.io.Serializable;

public interface LayoutBuilder extends Serializable {
	void onLayout(Layout layout);
}