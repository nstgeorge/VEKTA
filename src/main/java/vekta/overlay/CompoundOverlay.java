package vekta.overlay;

public abstract class CompoundOverlay implements Overlay {
	private final Overlay[] overlays;

	public CompoundOverlay(Overlay[] overlays) {
		this.overlays = overlays;
	}

	public Overlay[] getOverlays() {
		return overlays;
	}

	@Override
	public void draw() {
		for(Overlay overlay : getOverlays()) {
			overlay.draw();
		}
	}
}
