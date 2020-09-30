package vekta.menu.option;

import vekta.display.Layout;

import java.io.Serializable;

public interface LayoutAware extends Serializable {
	void onLayout(Layout layout);
}