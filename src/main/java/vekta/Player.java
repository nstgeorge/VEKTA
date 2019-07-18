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
	private static final float OVERALL_SKILL_RATE = 1.5F;

	private /*@Sync */ Faction faction;
	private /*@Sync */ ModularShip currentShip;

	private final List<PlayerListener> listeners = new ArrayList<>();
	private final Collection<String> attributes = new ArrayList<>();

	private final List<Mission> missions = new ArrayList<>();
	private Mission currentMission;

	private final List<Knowledge> knowledge = new ArrayList<>();

	private final Map<Knowledge, Integer> knowledgePrices = new HashMap<>();
	private final Map<Item, Integer> buyPrices = new HashMap<>();

	private final Map<SkillType, Float> skills = new EnumMap<>(SkillType.class);

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

	public List<Knowledge> getKnowledge() {
		return knowledge;
	}

	public Map<Knowledge, Integer> getKnowledgePrices() {
		return knowledgePrices;
	}

	public List<Knowledge> findKnowledge(Predicate<Knowledge> filter) {
		return getKnowledge().stream().filter(filter).collect(Collectors.toList());
	}

	@SuppressWarnings("unchecked")
	public <T extends Knowledge> List<T> findKnowledge(Class<T> type) {
		return (List<T>)findKnowledge(type::isInstance);
	}

	@SuppressWarnings("unchecked")
	public <T extends Knowledge> List<T> findKnowledge(Class<T> type, Predicate<T> filter) {
		return (List<T>)findKnowledge(t -> type.isInstance(t) && filter.test((T)t));
	}

	public <T extends Knowledge> boolean hasKnowledge(Class<T> type, Predicate<T> filter) {
		return findKnowledge(type, filter).isEmpty();
	}

	public void addKnowledge(Knowledge knowledge) {
		if(!knowledge.isValid(this)) {
			return;
		}
		boolean added = false;
		int value = knowledge.getArchiveValue();
		for(int i = 0; i < this.knowledge.size(); i++) {
			Knowledge prev = this.knowledge.get(i);
			switch(knowledge.getDelta(prev)) {
			case SAME:
			case WORSE:
				return; // Don't add if equivalent or better knowledge exists
			case BETTER:
				this.knowledge.set(i, knowledge);
				added = true;
				if(knowledgePrices.containsKey(prev)) {
					knowledgePrices.remove(prev);
				}
				else {
					value -= prev.getArchiveValue(); // Subtract previously collected value
				}
				break; // Swap out instead of adding new knowledge
			}
		}
		if(!added) {
			this.knowledge.add(0, knowledge); // Add to beginning of list
		}
		if(value > 0) {
			knowledgePrices.put(knowledge, value);
		}
	}

	public void cleanupKnowledge() {
		// Clean up invalid knowledge
		for(Knowledge k : new ArrayList<>(getKnowledge())) {
			if(!k.isValid(this)) {
				knowledge.remove(k);
			}
		}
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
		if(!hasAttribute(attribute)) {
			attributes.add(attribute.getSimpleName());
			syncChanges();
		}
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

	public float getSkill(SkillType type) {
		return skills.getOrDefault(type, 0F);
	}

	public void addSkill(SkillType type, float amount) {
		skills.put(type, getSkill(type) + amount);
		if(type != SkillType.OVERALL) {
			addSkill(SkillType.OVERALL, amount / SkillType.values().length * OVERALL_SKILL_RATE);
		}
	}

	public SkillLevel getSkillLevel(SkillType type) {
		return SkillLevel.fromSkill(getSkill(type));
	}

	@Override
	public void onSync(Player data) {
		if(isRemote()) {
			// Prevent local player from syncing remote changes
			super.onSync(data);
		}
	}
}
