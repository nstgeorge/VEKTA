package vekta.context;

import com.google.common.collect.ImmutableList;
import vekta.KeyBinding;
import vekta.Player;
import vekta.Resources;
import vekta.knowledge.*;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

import static processing.core.PConstants.*;
import static vekta.Vekta.*;

public class KnowledgeContext implements Context, Comparator<Knowledge> {
	private static final int PADDING = 150;
	private static final int SPACING = 30;
	private static final int ITEMS_BEFORE_SCROLL = 5;

	private final Context parent;
	private final Player player;

	private final List<KnowledgeTab> tabs;
	private KnowledgeTab tab;
	private int tabIndex;

	private int cursorIndex;

	public KnowledgeContext(Context parent, Player player) {
		this.parent = parent;
		this.player = player;

		tabs = ImmutableList.of(
				new KnowledgeTab("Planets", TerrestrialKnowledge.class),
				new KnowledgeTab("Settlements", SettlementKnowledge.class),
				new KnowledgeTab("People", PersonKnowledge.class),
				new KnowledgeTab("Ships", ShipKnowledge.class),
				new KnowledgeTab("Owned by " + player.getName(), o -> o instanceof LevelKnowledge && ((LevelKnowledge)o).getLevel() == KnowledgeLevel.OWNED),
				new KnowledgeTab("Everything", o -> true)
		);
		
		player.cleanupKnowledge();
		setTabIndex(0);
	}

	public List<Knowledge> getKnowledgeList() {
		return tab.getKnowledgeList();
	}

	public void setTabIndex(int index) {
		// Clamp to tab index range
		index = max(0, min(tabs.size() - 1, index));

		this.tabIndex = index;
		this.tab = tabs.get(index);
		this.cursorIndex = 0;
	}

	public String getTitleText() {
		return tab.getTitle();
	}

	public String getNoneFoundText() {
		return "No information found. \nLand, dock, or scan objects to collect data. ";
	}

	public Knowledge getCursor() {
		return getKnowledgeList().get(cursorIndex);
	}

	@Override
	public int compare(Knowledge a, Knowledge b) {
		// TODO: add sorting modes/hotkeys

		// Sort space objects by distance
		if(a instanceof SpaceObjectKnowledge && b instanceof SpaceObjectKnowledge) {
			float aSq = player.getShip().relativePosition(((SpaceObjectKnowledge)a).getSpaceObject()).magSq();
			float bSq = player.getShip().relativePosition(((SpaceObjectKnowledge)b).getSpaceObject()).magSq();
			return (int)Math.signum(aSq - bSq);
		}
		// Sort everything else by discovery time
		return 0;
	}

	@Override
	public void render() {
		v.clear();

		v.textAlign(CENTER);
		v.textSize(24);
		v.fill(UI_COLOR);

		// Draw default information if no knowledges are found
		if(getKnowledgeList().isEmpty()) {
			v.fill(100);
			v.text(getNoneFoundText(), v.width / 2F, PADDING);
		}
		else {
			// Draw divider
			v.shapeMode(CORNERS);
			v.stroke(100);
			v.line(v.width / 2F, 100, v.width / 2F, v.height - 50);

			// Draw observation list item
			v.pushMatrix();
			int scrollOffset = cursorIndex > ITEMS_BEFORE_SCROLL ? -((cursorIndex - ITEMS_BEFORE_SCROLL) * SPACING) : 0;
			v.translate(0, scrollOffset);

			List<Knowledge> knowledges = getKnowledgeList();
			for(int i = 0; i < knowledges.size(); i++) {
				Knowledge knowledge = knowledges.get(i);

				v.fill(knowledge.getColor(player));
				v.textAlign(LEFT);

				String name = knowledge.getName();

				if(i == cursorIndex) {
					v.text(">>", PADDING - 100, 110 + (SPACING * i));
				}
				if(name.length() > 20) {
					v.text(name.substring(0, 17) + "...", PADDING, 110 + (SPACING * i));
				}
				else {
					v.text(name, PADDING, 110 + (SPACING * i));
				}
				v.textAlign(RIGHT);
				v.text(knowledge.getSecondaryText(player), v.width / 2F - PADDING, 110 + (SPACING * i));
			}

			v.popMatrix();

			// Rectangles that block overflow
			v.rectMode(CORNERS);
			v.stroke(0);
			v.fill(0);
			v.rect(0, 0, v.width / 2F, 90);
			v.rect(0, v.height, v.width / 2F, v.height - 100);

			Knowledge cursor = getCursor();

			// Define cusor detail rectangle
			float x = v.width / 2F + PADDING;
			float y = PADDING;
			float width = v.width - x - PADDING;
			float height = v.height - y;

			// Draw cursor name
			v.textAlign(CENTER);
			v.fill(cursor.getColor(player));
			v.text(cursor.getName(), x + width / 2, y - 100);

			// Draw cursor selection text
			v.text(cursor.getSelectText(player), v.width / 2F, y + height - 40);

			// Draw cursor details
			v.textAlign(LEFT);
			v.pushMatrix();
			v.translate(x, y);
			cursor.draw(player, width, height);
			v.popMatrix();
		}

		// Draw title
		v.textAlign(CENTER);
		v.textSize(36);
		v.fill(255);
		v.text(getTitleText(), v.width / 2F, 60);
	}

	@Override
	public void focus() {

	}

	@Override
	public void unfocus() {

	}

	@Override
	public void keyPressed(KeyBinding key) {
		switch(key) {
		case SHIP_TARGET_PLANET: // temp: [1]
			setTabIndex(0);
			break;
		case SHIP_TARGET_ASTEROID: // temp: [2]
			setTabIndex(1);
			break;
		case SHIP_TARGET_SHIP: // temp: [3]
			setTabIndex(2);
			break;
		case SHIP_TARGET_OBJECTIVE: // temp: [4]
			setTabIndex(3);
			break;

		case MENU_CLOSE:
		case SHIP_NAVIGATION:
			setContext(parent);
			break;
		case MENU_UP:
			if(cursorIndex > 0) {
				Resources.playSound("change");
				cursorIndex--;
			}
			break;
		case MENU_DOWN:
			if(cursorIndex < getKnowledgeList().size() - 1) {
				Resources.playSound("change");
				cursorIndex++;
			}
			break;
		case MENU_LEFT:
			if(tabIndex > 0) {
				Resources.playSound("change");
				setTabIndex(--tabIndex);
			}
			break;
		case MENU_RIGHT:
			if(tabIndex < tabs.size() - 1) {
				Resources.playSound("change");
				setTabIndex(++tabIndex);
			}
			break;
		case MENU_SELECT:
			if(!getKnowledgeList().isEmpty()) {
				getCursor().onSelect(player);
			}
			break;
		}
	}

	@Override
	public void keyReleased(KeyBinding key) {

	}

	@Override
	public void mouseWheel(int amount) {
		cursorIndex = max(0, min(getKnowledgeList().size() - 1, cursorIndex + amount));
	}

	protected class KnowledgeTab {
		private final String title;
		private final Predicate<Knowledge> predicate;
		private List<Knowledge> knowledges;

		public KnowledgeTab(String title, Class<? extends Knowledge> type) {
			this(title, type::isInstance);
		}

		public KnowledgeTab(String title, Predicate<Knowledge> predicate) {
			this.title = title;
			this.predicate = predicate;
		}

		public String getTitle() {
			return title;
		}

		public List<Knowledge> getKnowledgeList() {
			if(knowledges == null) {
				knowledges = player.findKnowledge(predicate);
				knowledges.sort(KnowledgeContext.this);
			}
			return knowledges;
		}
	}
}
