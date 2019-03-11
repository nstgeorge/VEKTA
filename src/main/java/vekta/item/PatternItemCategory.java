package vekta.item;

public class PatternItemCategory extends ItemCategory {
	private final String[] patterns;

	public PatternItemCategory(String name, String[] patterns) {
		super(name);
		
		this.patterns = patterns;
	}

	public String[] getPatterns() {
		return patterns;
	}

	@Override
	public boolean isIncluded(Item item) {
		String name = item.getName();
		for(String pattern : getPatterns()) {
			if(name.contains(pattern)) {
				return true;
			}
		}
		return false;
	}
}
