package vekta;

import vekta.item.Inventory;
import vekta.item.Item;
import vekta.knowledge.Knowledge;
import vekta.mission.Mission;
import vekta.object.ship.ModularShip;
import vekta.overlay.singleplayer.Notification;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class Player extends Syncable<Player> {
	private /*@Sync */ Faction faction;
	private /*@Sync */ ModularShip currentShip;

	private final List<PlayerListener> listeners = new ArrayList<>();
	private final Set<String> attributes = new HashSet<>();

	//	private final HashMap<SpaceObject, KnowledgeLevel> observedObjectList = new HashMap<>();
	//	private final HashMap<SpaceObject, List<String>> observedObjectFeatureList = new HashMap<>();
	//	private final HashMap<SpaceObject, List<Settlement>> observedObjectSettlementList = new HashMap<>();

	private final List<Knowledge> knowledgeList = new ArrayList<>();

	private final List<Mission> missions = new ArrayList<>();
	private Mission currentMission;

	private final Map<Item, Integer> buyPrices = new HashMap<>();

	public Player(PlayerFaction faction) {
		setFaction(faction);
		faction.setPlayer(this);

		addListener(new PlayerListener() {
			@Override
			public void onChangeShip(ModularShip ship) {
				if(ship == null) {
					throw new RuntimeException("Player ship cannot be null");
				}
				if(currentShip != null) {
					currentShip.setController(null);
				}
				currentShip = ship;
				ship.setController(Player.this);
				ship.setColor(getColor());
			}

			@Override
			public void onMissionStatus(Mission mission) {
				switch(mission.getStatus()) {
				case READY:
				case STARTED:
					if(!missions.contains(mission)) {
						missions.add(0, mission);
						setCurrentMission(mission);
					}
					break;
				case CANCELLED:
				case COMPLETED:
					missions.remove(mission);
					if(getCurrentMission() == mission) {
						setCurrentMission(null);
					}
					break;
				}
			}

			@Override
			public void onKeyPress(KeyBinding key) {
				if(key == KeyBinding.OBJECTIVE_CYCLE && getCurrentMission() != null) {
					getCurrentMission().cycleObjective();
				}
				if(key == KeyBinding.MISSION_CYCLE && missions.size() > 0) {
					int index = missions.indexOf(getCurrentMission()) + 1;
					setCurrentMission(index == getMissions().size() ? null : missions.get(index % missions.size()));
				}
			}

			@Override
			public void onAddItem(Item item) {
				item.onAdd(Player.this);
			}

			@Override
			public void onRemoveItem(Item item) {
				buyPrices.remove(item);
				item.onRemove(Player.this);
			}
		});
	}

	public List<Knowledge> getKnowledgeList() {
		return knowledgeList;
	}

	public List<Knowledge> findKnowledge(Predicate<Knowledge> filter) {
		return getKnowledgeList().stream().filter(filter).collect(Collectors.toList());
	}

	@SuppressWarnings("unchecked")
	public <T extends Knowledge> List<T> findKnowledge(Class<T> type) {
		return (List<T>)findKnowledge(type::isInstance);
	}

	public void addKnowledge(Knowledge knowledge) {
		for(int i = 0; i < knowledgeList.size(); i++) {
			Knowledge prev = knowledgeList.get(i);
			switch(knowledge.getDelta(prev)) {
			case BETTER:
				knowledgeList.set(i, knowledge);
				return; // Swap out instead of adding new knowledge
			case SAME:
			case WORSE:
				return; // Don't add if an equivalent or better knowledge exists
			}
		}
		knowledgeList.add(0, knowledge); // Add to beginning of list
	}

	public void cleanupKnowledge() {
		//		// Clean up invalid knowledge
		//		for(Knowledge k : new ArrayList<>(getKnowledgeList())) {
		//			if(!k.isValid(this)) {
		//				knowledgeList.remove(k);
		//			}
		//		}
		// Disabled for testing (evaluate how to handle destroyed/despawned/dead entries)
	}

	public Faction getFaction() {
		return faction;
	}

	public void setFaction(Faction faction) {
		if(faction == null) {
			throw new RuntimeException("Player faction cannot be null");
		}
		this.faction = faction;
		syncChanges();
	}

	public String getName() {
		return getFaction().getName();
	}

	public int getColor() {
		return getFaction().getColor();
	}

	public ModularShip getShip() {
		return currentShip;
	}

	public Inventory getInventory() {
		return getShip().getInventory();
	}

	public List<Mission> getMissions() {
		return missions;
	}

	public Mission getCurrentMission() {
		return currentMission;
	}

	public void setCurrentMission(Mission currentMission) {
		this.currentMission = currentMission;
		syncChanges();
	}

	public void addListener(PlayerListener listener) {
		this.listeners.add(listener);
	}

	public void removeListener(PlayerListener listener) {
		this.listeners.remove(listener);
	}

	public void removeListeners(Class<? extends PlayerListener> type) {
		this.listeners.removeIf(type::isInstance);
	}

	public boolean hasAttribute(Class attribute) {
		return attributes.contains(attribute.getSimpleName());
	}

	public void addAttribute(Class attribute) {
		attributes.add(attribute.getSimpleName());
		syncChanges();
	}

	public void removeAttribute(Class attribute) {
		attributes.remove(attribute.getSimpleName());
		syncChanges();
	}

	public void emit(PlayerEvent event, Object data) {
		for(PlayerListener listener : new ArrayList<>(listeners)) {
			event.handle(listener, data);
		}
	}

	public Notification send(String notification) {
		return send(new Notification(notification));
	}

	public Notification send(Notification notification) {
		emit(PlayerEvent.NOTIFICATION, notification);
		return notification;
	}

	public int getBuyPrice(Item item) {
		return buyPrices.getOrDefault(item, 0);
	}

	public void setBuyPrice(Item item, int price) {
		if(price > 0) {
			buyPrices.put(item, price);
		}
		else {
			buyPrices.remove(item);
		}
	}

	@Override
	public void onSync(Player data) {
		if(isRemote()) {
			// Prevent local player from syncing remote changes
			super.onSync(data);
		}
	}
}
