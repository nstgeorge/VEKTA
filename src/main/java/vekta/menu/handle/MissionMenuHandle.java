package vekta.menu.handle;

import vekta.menu.Menu;
import vekta.menu.option.MenuOption;
import vekta.menu.option.MissionOption;
import vekta.mission.Mission;
import vekta.mission.Objective;
import vekta.mission.Reward;

import java.util.List;

import static vekta.Vekta.v;

/**
 * Mission selector inject handle
 */
public class MissionMenuHandle extends MenuHandle {
	public MissionMenuHandle(MenuOption defaultOption) {
		super(defaultOption);
	}

	@Override
	public int getButtonWidth() {
		return v.width / 3;
	}

	@Override
	public int getButtonX() {
		return v.width / 6 + getButtonWidth() / 2;
	}

	@Override
	public int getButtonY(int i) {
		return super.getButtonY(i) - 100;
	}

	@Override
	public void render(Menu menu) {
		super.render(menu);

		if(!(menu.getCursor() instanceof MissionOption)) {
			return;
		}
		Mission mission = ((MissionOption)menu.getCursor()).getMission();

		float missionX = getButtonX() + getButtonWidth() * .75F;

		v.textSize(32);
		v.fill(v.color(100));
		v.textAlign(v.CENTER);
		v.text("Missions:", getButtonX(), getButtonY(-1));

		v.textAlign(v.LEFT);
		v.fill(100);
		v.text(mission.getName(), missionX, getButtonY(-1));

		v.textSize(24);
		List<Objective> objectives = mission.getObjectives();
		for(int i = 0; i < objectives.size(); i++) {
			Objective objective = objectives.get(i);
			v.fill(objective.getStatus().getColor());
			v.text(objective.getDisplayText(), missionX + 20, getButtonY(i));
		}
		List<Reward> rewards = mission.getRewards();
		for(int i = 0; i < rewards.size(); i++) {
			Reward reward = rewards.get(i);
			v.fill(reward.getColor());
			v.text(reward.getDisplayText(), missionX + 20, getButtonY(objectives.size() + i));
		}
	}
}
