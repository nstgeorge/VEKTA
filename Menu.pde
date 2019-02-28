// These will move into their own files once we migrate to Maven

interface MenuItem {
  String getName();
  void select(LandingMenu menu);
}

class PlaceholderItem implements MenuItem {
  @Override
  public String getName() {
    return "Placeholder";
  }
  
  @Override
  void select(LandingMenu menu) {}
}

class TakeoffItem implements MenuItem {
  private final LandingSite site;
  
  public TakeoffItem(LandingSite site) {
    this.site = site;
  }
  
  @Override
  public String getName() {
    return "Takeoff";
  }
  
  @Override
  void select(LandingMenu menu) {
    site.takeoff();
  }
}

class LandingMenu {
  private static final int ITEM_SPACING = 100;
  
  private final LandingSite site;
  
  private final List<MenuItem> items = new ArrayList<MenuItem>();
  
  private int cursor = 0;
  
  public LandingMenu(LandingSite site) {
    this.site = site;
    
    addItem(new PlaceholderItem());
    addItem(new TakeoffItem(site));
  }
  
  public int size() {
    return items.size();
  }
  
  public MenuItem getCursor() {
    return items.get(cursor);
  }
  
  public void addItem(MenuItem item) {
    items.add(item);
  }
  
  public void render() {
    clear(); // TODO: find a way to avoid clearing without creating weird artifacts
    hint(DISABLE_DEPTH_TEST);
    camera();
    noLights();
    shapeMode(CENTER);
    shape(logo, width/2, height/4, 339.26, 100);
    for(int i = 0; i < items.size(); i++) {
      drawButton(items.get(i).getName(), (height / 2) + (i * ITEM_SPACING), cursor == i);
    }
    
    textFont(bodyFont);
    stroke(0);
    fill(255);
    textAlign(CENTER);
    text("X to select", width / 2, (height / 2) + (size() * 100) + 200);
  }
  
  private void drawButton(String name, int yPos, boolean selected) {
    if(selected) stroke(255);
    else stroke(UI_COLOR);
    fill(1);
    rectMode(CENTER);
    rect(width / 2, yPos, 200, 50);
    // Text ----------------------
    textFont(bodyFont);
    stroke(0);
    fill(UI_COLOR);
    textAlign(CENTER, CENTER);
    text(name, width / 2, yPos - 3);
  }
  
  public void keyPressed(char key) {
    if(key == 'w') {
      if(getSetting("sound") > 0) change.play(); // Play the sound for changing menu selection
      cursor = (cursor - 1 + items.size()) % items.size(); // Cycle cursor left
    } 
    else if(key == 's') {
      if(getSetting("sound") > 0) change.play(); // Play the sound for changing menu selection
      cursor = (cursor + 1) % items.size(); // Cycle cursor right
    }
    else if(key == 'x') {
      getCursor().select(this);
    }
  }  
}  
