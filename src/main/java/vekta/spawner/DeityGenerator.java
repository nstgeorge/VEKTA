package vekta.spawner;

import vekta.deity.Deity;
import vekta.item.ItemType;
import vekta.item.category.ItemTypeCategory;
import vekta.knowledge.DeityKnowledge;
import vekta.player.Player;

import java.util.ArrayList;
import java.util.List;

import static vekta.Vekta.v;

public class DeityGenerator {
	private static final Deity[] DEITIES;

	static {
		List<Deity> deities = new ArrayList<>();

		Deity huchave = new Deity("Hu-Cha Ve", new ItemTypeCategory("Plans", ItemType.RECIPE),
				new String[] {"Power", "Skill", "Dominance", "Thirst", "Innovation"});
		deities.add(huchave);
		Deity hochangu = new Deity("Ho-Chan Gu", new ItemTypeCategory("Sacrifice", ItemType.ECOSYSTEM),
				new String[] {"Survival", "Life", "Hunger", "Nature"});
		deities.add(hochangu);
		Deity gohuioep = new Deity("Go Hui-Oep", new ItemTypeCategory("Weapon", ItemType.DANGEROUS),
				new String[] {"Vengeance", "Wrath", "Conflict"});
		deities.add(gohuioep);
		Deity ahmagehu = new Deity("Ahmva Ge-Hu", new ItemTypeCategory("Trinket", ItemType.MISSION),
				new String[] {"Wit", "Trickery", "Humor"});
		deities.add(ahmagehu);
		Deity sambresoso = new Deity("Sambre So-So", new ItemTypeCategory("Garbage", ItemType.JUNK),
				new String[] {"Forgetfulness", "Sacrelidge"});
		deities.add(sambresoso);
		Deity baoopavun = new Deity("Ba'oop A-Vün", new ItemTypeCategory("Knowledge", ItemType.KNOWLEDGE),
				new String[] {"Decay", "Wisdom", "Understanding"});
		deities.add(baoopavun);
		Deity sharuk = new Deity("Sha-Ruk", new ItemTypeCategory("Wealth", ItemType.ECONOMY),
				new String[] {"Unpredictability", "Spontaneity"});
		deities.add(sharuk);
		Deity garvehau = new Deity("Gar Ve-Hau", new ItemTypeCategory("Freedom", ItemType.MODULE),
				new String[] {"Intimidation", "Insuboordination"});
		deities.add(garvehau);
		Deity uiwovamuske = new Deity("Ui-Wo Vamuske", new ItemTypeCategory("Worthiness", ItemType.LEGENDARY),
				new String[] {"Bravery", "Honor", "Chivalry", "Death"});
		deities.add(uiwovamuske);

		addOpinions(huchave, hochangu, -1);
		addOpinions(gohuioep, baoopavun, -1);
		addOpinions(gohuioep, uiwovamuske, -.5F);
		addOpinions(sharuk, ahmagehu, sambresoso, .5F);
		addOpinions(huchave, sambresoso, garvehau, .2F);
		addOpinions(ahmagehu, uiwovamuske, -.5F);
		addOpinions(uiwovamuske, garvehau, -1);
		addOpinions(baoopavun, uiwovamuske, .5F);

		Deity yeti = new Deity("The Yeti", new ItemTypeCategory("Safety", ItemType.COLONY),
				new String[] {"Hope", "Endurance", "Stealth"});
		for(Deity deity : deities) {
			deity.addOpinion(yeti, -1F);
			yeti.addOpinion(deity, -.2F);
		}
		deities.add(yeti);

		DEITIES = deities.toArray(new Deity[0]);
	}

	private static void addOpinions(Deity a, Deity b, float opinion) {
		a.addOpinion(b, opinion);
		b.addOpinion(a, opinion);
	}

	private static void addOpinions(Deity a, Deity b, Deity c, float opinion) {
		addOpinions(a, b, opinion);
		addOpinions(a, c, opinion);
		addOpinions(b, c, opinion);
	}

	public static Deity randomDeity() {
		return v.random(DEITIES);
	}

	public static DeityKnowledge findKnowledge(Player player, Deity deity) {
		List<DeityKnowledge> knowledgeList = player.findKnowledge(DeityKnowledge.class, k -> k.getDeity() == deity);
		return !knowledgeList.isEmpty() ? knowledgeList.get(0) : null;
	}

	public static float getFavor(Player player, Deity deity) {
		DeityKnowledge knowledge = findKnowledge(player, deity);
		return knowledge != null ? knowledge.getFavor() : 0;
	}

	public static void addFavor(Player player, Deity deity, float favor) {
		DeityKnowledge knowledge = findKnowledge(player, deity);
		if(knowledge == null) {
			knowledge = new DeityKnowledge(deity);
			player.addKnowledge(knowledge);
		}
		for(Deity other : DEITIES) {
			float opinion = other.getOpinion(deity);
			if(opinion != 0) {
				addFavorIfKnown(player, deity, favor * opinion);
			}
		}
	}

	private static void addFavorIfKnown(Player player, Deity deity, float favor) {
		DeityKnowledge knowledge = findKnowledge(player, deity);
		if(knowledge != null) {
			knowledge.addFavor(favor);
		}
	}
}

