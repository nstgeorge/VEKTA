package vekta.story.part;

import vekta.spawner.StoryGenerator;
import vekta.story.Story;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static vekta.Vekta.v;

public class StoryPart implements Serializable {
	private final String text;
	private final List<String> paths = new ArrayList<>();

	public StoryPart(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public List<String> getPaths() {
		return paths;
	}

	public void addPath(String path) {
		paths.add(path);
	}

	public StoryPart chooseNext(Story story) {
		if(getPaths().isEmpty()) {
			return null;
		}
		return StoryGenerator.createPart(story, v.random(getPaths()));
	}
}
