package vekta;

import vekta.item.Inventory;
import vekta.item.Item;
import vekta.item.ItemType;
import vekta.item.ModuleItem;
import vekta.object.module.*;

import static vekta.Vekta.*;

public class ItemGenerator {

	// Register module types
	private final Module[] modules = new Module[] {
			new AutopilotModule(),
			new BatteryModule(1),
			new CannonModule(),
			new EngineModule(1),
			new HyperdriveModule(1),
			new RCSModule(1),
			new TargetingModule(),
			new TorpedoModule(1),
			new TractorBeamModule(1),
	};

	public Item randomItem() {
		Vekta v = getInstance();
		float r = v.random(1);
		if(r > .2) {
			ItemType type = randomItemType();
			return new Item(generateItemName(type), type);
		}
		else {
			return new ModuleItem(v.random(modules).getVariant());
		}
	}

	public ItemType randomItemType() {
		float r = getInstance().random(1);
		if(r < .7) {
			return ItemType.COMMON;
		}
		else if(r < .95) {
			return ItemType.RARE;
		}
		else {
			return ItemType.LEGENDARY;
		}
	}

	public void addLoot(Inventory inv, int lootTier) {
		int itemCt = round(getInstance().random(lootTier - 1, lootTier * 2));
		for(int i = 0; i < itemCt; i++) {
			// TODO: occasionally add ModuleItems
			Item item = randomItem();
			inv.add(item);
		}
	}
}  
