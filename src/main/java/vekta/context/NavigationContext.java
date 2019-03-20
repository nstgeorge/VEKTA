package vekta.context;

import vekta.KeyBinding;
import vekta.Player;
import vekta.PlayerListener;
import vekta.Resources;
import vekta.menu.Menu;
import vekta.menu.handle.SurveyMenuHandle;
import vekta.object.SpaceObject;
import vekta.object.planet.Planet;
import vekta.observation.ObservationLevel;
import vekta.overlay.singleplayer.TelemetryOverlay;
import vekta.terrain.LandingSite;
import vekta.terrain.settlement.Settlement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static processing.core.PConstants.*;
import static vekta.Vekta.*;

public class NavigationContext implements Context, PlayerListener {

	private HashMap<SpaceObject, ObservationLevel> objectList;

	private int selected;

	private static final float ROTATE_SPEED = 1F / 2000;
	private static final float SCAN_SPEED = 1F / 100;
	private static final int PLANET_SIZE = 100;
	private static final int PLANET_RES = 32;

	private static final int PADDING = 150;
	private static final int SPACING = 30;
	private static final int ITEMS_BEFORE_SCROLL = 5;

	private final Context parent;
	private final Player player;

	public NavigationContext(Context parent, Player player) {
		this.parent = parent;
		this.player = player;

		selected = 0;

		player.addListener(this);
	}

	@Override
	public void render() {
		v.clear();

		v.textSize(24);
		v.fill(UI_COLOR);

		objectList = player.getObservedObjectList();

		if(objectList.size() > 0) {

			v.shapeMode(CORNERS);
			v.stroke(100);
			v.line(v.width / 2F, 100, v.width / 2F, v.height - 50);

			SpaceObject[] objects = new SpaceObject[objectList.size()];
			objects = objectList.keySet().toArray(objects);

			v.pushMatrix();
			int scrollOffset = selected > ITEMS_BEFORE_SCROLL ? -((selected - ITEMS_BEFORE_SCROLL) * SPACING) : 0;
			v.translate(0, scrollOffset);

			for(int i = 0; i < objectList.keySet().size(); i++) {

				SpaceObject object = objects[i];
				v.fill(object.getColor());
				v.textAlign(LEFT);

				if(i == selected) {
					v.text(">>", PADDING - 100, 110 + (SPACING * i));
				}
				if(object.getName().length() > 20) {
					v.text(object.getName().trim().substring(0, 17) + "...", PADDING, 110 + (SPACING * i));
				}
				else {
					v.text(object.getName().trim(), PADDING, 110 + (SPACING * i));
				}
				v.textAlign(RIGHT);
				v.text(TelemetryOverlay.getDistanceString(object, player), v.width / 2F - PADDING, 110 + (SPACING * i));
			}

			v.popMatrix();

			// Rectangles that block overflow
			v.rectMode(CORNERS);
			v.stroke(0);
			v.fill(0);
			v.rect(0, 0, v.width / 2F, 90);
			v.rect(0, v.height, v.width / 2F, v.height - 100);

			float rotate = v.frameCount * ROTATE_SPEED;
			float scan = v.frameCount * SCAN_SPEED;

			// Right side
			SpaceObject focus = objects[selected];
			ObservationLevel level = objectList.get(focus);
			int color = focus.getColor();

			v.pushMatrix();
			v.textAlign(CENTER);
			v.translate(3 * v.width / 4F, 200 + PLANET_SIZE / 2F);
			v.fill(color);
			v.text(focus.getName(), 0, -130);
			if(player.getShip().getTargets().contains(focus)) {
				v.text(":: Targeted ::", 0, v.height - (250 + PLANET_SIZE / 2F));
			}
			else {
				v.text("Press SELECT to target", 0, v.height - (250 + PLANET_SIZE / 2F));
			}

			// a e s t h e t i c planet animation
			// TODO: Move this into a function in Planet
			if(focus instanceof Planet && level == ObservationLevel.SCANNED) {
				float perspective = 1;

				v.shapeMode(CENTER);
				v.strokeWeight(2);
				v.noFill();

				// Draw scanner arc
				float scanScale = cos(scan);
				v.stroke(v.lerpColor(0, color, sq(cos(scan / 2 + perspective))));
				v.arc(0, 0, PLANET_SIZE * scanScale, PLANET_SIZE, -HALF_PI, HALF_PI);

				// Draw planet
				for(float r = 0; r < TWO_PI; r += TWO_PI / PLANET_RES) {
					float angle = r + rotate;
					float xScale = cos(angle);
					v.stroke(v.lerpColor(0, color, sq(cos(r / 2 + perspective))));
					v.arc(0, 0, PLANET_SIZE * xScale, PLANET_SIZE, -HALF_PI, HALF_PI);
				}

				v.strokeWeight(1);
			}
			else {
				v.textSize(PLANET_SIZE);
				v.fill(100);
				v.text("?", 0, 0);
				v.textSize(24);
			}

			// Info
			v.fill(focus.getColor());
			v.translate(-(v.width / 4F) + PADDING, PLANET_SIZE + SPACING);
			v.textAlign(LEFT);
			switch(objectList.get(focus)) {
			case LANDED:
				v.text("Mass: " + TelemetryOverlay.getMassString(focus.getMass()), 0, 0);
				v.text("Radius: " + focus.getRadius() + " km", 0, SPACING);
				break;
			case SCANNED:
				List<String> features = player.getObservedFeatures(focus);
				List<Settlement> settlements = player.getObservedSettlements(focus);
				List<String> settlementNames = new ArrayList<>();

				for(Settlement s : settlements) {
					settlementNames.add(s.getName());
				}

				v.text("Mass: " + TelemetryOverlay.getMassString(focus.getMass()), 0, 0);
				v.text("Radius: " + focus.getRadius() + " km", 0, SPACING);
				v.text(buildMultipleLineString(features, "Features"), 0, SPACING * 2);
				v.text(buildMultipleLineString(settlementNames, "Settlements"), 0, SPACING * 2 + (24 * numberOfLines(buildMultipleLineString(features, "Features"))));
				break;
			}

			v.popMatrix();

			v.textAlign(CENTER);
			v.textSize(36);
			v.fill(255);
			v.text("Navigation", v.width / 2F, 60);

		}
		else {
			v.fill(100);
			v.text("No entries found. \nLand on, dock on, or scan a planet or ship for data.", v.width / 2F, 100);
		}
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
		case MENU_CLOSE:
		case SHIP_NAVIGATION:
			setContext(parent);
			break;
		case MENU_UP:
			Resources.playSound("change");
			selected = Math.max(--selected, 0);
			break;
		case MENU_DOWN:
			Resources.playSound("change");
			selected = Math.min(++selected, player.getObservedObjectList().size() - 1);
			break;
		case MENU_SELECT:
			if(objectList.size() > 0) {
				player.getShip().setTargets(objectList.keySet().toArray(new SpaceObject[] {})[selected]);
			}
			break;
		}
	}

	@Override
	public void keyReleased(KeyBinding key) {

	}

	@Override
	public void mouseWheel(int amount) {
		selected = max(0, min(player.getObservedObjectList().size() - 1, selected + amount));
	}

	@Override
	public void onLand(LandingSite site) {
		player.recordSpaceObject(site.getParent(), ObservationLevel.LANDED);

	}

	@Override
	public void onDock(SpaceObject object) {
		player.recordSpaceObject(object, ObservationLevel.LANDED);
	}

	@Override
	public void onMenu(Menu menu) {
		if(menu.getHandle() instanceof SurveyMenuHandle) {
			LandingSite site = ((SurveyMenuHandle)menu.getHandle()).getSite();
			player.recordSpaceObject(site.getParent(), ObservationLevel.SCANNED, site.getTerrain());
		}
	}

	private String buildMultipleLineString(List<String> words, String description) {
		String finalString = "";
		String[] lines = new String[10];
		int currentLineIndex = 0;
		int currentWordIndex = 0;
		for(int i = 0; i < lines.length; i++) {
			lines[i] = "";
		}
		lines[0] = description + ": ";
		for(String word : words) {
			if(v.textWidth(lines[currentLineIndex] + ", " + word) > (v.width / 2F - PADDING * 2)) {
				currentLineIndex++;
			}
			if(currentWordIndex != words.size() - 1) {
				lines[currentLineIndex] = lines[currentLineIndex] + word + ", ";
			}
			else {
				lines[currentLineIndex] = lines[currentLineIndex] + word;
			}

			currentWordIndex++;
		}
		for(String line : lines) {
			if(line == null)
				return finalString;
			finalString = finalString.concat("\n" + line);
		}
		return finalString;
	}

	private int numberOfLines(String in) {
		int count = 0;
		for(char c : in.toCharArray()) {
			if(c == '\n')
				count++;
		}
		return count;
	}
}
