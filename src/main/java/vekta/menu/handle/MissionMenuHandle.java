package vekta.menu.handle;

import vekta.KeyBinding;
import vekta.menu.Menu;
import vekta.menu.option.MissionOption;
import vekta.mission.Mission;
import vekta.mission.objective.Objective;
import vekta.mission.reward.Reward;

import java.util.List;

import static vekta.Vekta.v;

/**
 * Mission selector inject handle
 */
public class MissionMenuHandle extends MenuHandle {
	public MissionMenuHandle() {
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
	public KeyBinding getShortcutKey() {
		return KeyBinding.SHIP_MISSIONS;
	}

	@Override
	public void render(Menu menu) {
		super.render(menu);

		v.textSize(32);
		v.fill(v.color(100));
		v.textAlign(v.CENTER);
		v.text("Mission:", getButtonX(), getButtonY(-1));

		if(!(menu.getCursor() instanceof MissionOption)) {
			return;
		}
		Mission mission = ((MissionOption)menu.getCursor()).getMission();

		float missionX = getButtonX() + getButtonWidth() * .75F;
		float missionY = v.height / 4F;

		v.textAlign(v.LEFT);
		v.fill(100);
		v.text(mission.getName(), missionX, missionY);

		v.textSize(24);
		List<Objective> objectives = mission.getObjectives();
		for(int i = 0; i < objectives.size(); i++) {
			Objective objective = objectives.get(i);
			v.fill(objective.getStatus().getColor());
			v.text(objective.getDisplayText(), missionX + 20, missionY + getMissionOffset(i + 1));
		}
		List<Reward> rewards = mission.getRewards();
		float rewardBasis = objectives.size() + 1;
		for(int i = 0; i < rewards.size(); i++) {
			Reward reward = rewards.get(i);
			v.fill(reward.getColor());
			v.text(reward.getDisplayText(), missionX + 30, missionY + getMissionOffset(rewardBasis + i * .75F));
		}
	}

	private float getMissionOffset(float position) {
		return position * getSpacing() * .75F;
	}
}
