package vekta.menu.handle;

import vekta.KeyBinding;
import vekta.menu.Menu;
import vekta.menu.option.MissionButton;
import vekta.mission.Mission;
import vekta.mission.objective.Objective;
import vekta.mission.reward.Reward;

import java.util.List;

import static vekta.Vekta.v;

/**
 * Mission selector menu handle
 */
public class MissionMenuHandle extends MenuHandle {
//	public MissionMenuHandle() {
//		super(0, v.height / 2 - 200, v.width, v.height / 2 + 200);
//	}

	@Override
	public int getItemWidth() {
		return v.width / 3;
	}

	@Override
	public int getItemX() {
		return v.width / 6 + getItemWidth() / 2;
	}

	@Override
	public int getItemY(int i) {
		return super.getItemY(i) - 100;
	}

	@Override
	public KeyBinding getShortcutKey() {
		return KeyBinding.SHIP_MISSIONS;
	}

	@Override
	public void render() {
		super.render();

		Menu menu = getMenu();

		v.textSize(32);
		v.fill(v.color(100));
		v.textAlign(v.CENTER);
		v.text("Mission:", getItemX(), getItemY(-1));

		if(!(menu.getCursor() instanceof MissionButton)) {
			return;
		}
		Mission mission = ((MissionButton)menu.getCursor()).getMission();

		float missionX = getItemX() + getItemWidth() * .75F;
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
