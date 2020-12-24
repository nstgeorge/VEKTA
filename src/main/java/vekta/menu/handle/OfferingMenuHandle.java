package vekta.menu.handle;

import vekta.deity.Deity;

public class OfferingMenuHandle extends SideLayoutMenuHandle {
	private final Deity deity;

	public OfferingMenuHandle(Deity deity) {
		super(false);

		this.deity = deity;
	}

	public Deity getDeity() {
		return deity;
	}

	@Override
	public void render() {
		super.render();

	}
}
