// These will move into their own files once we migrate to Maven

interface MenuItem {
  String getName();
  void select(Menu menu);
}

class CloseItem implements MenuItem {
  @Override
  public String getName() {
    return "Close";
  }
  
  @Override
  void select(Menu menu) {
    closeMenu(menu);
  }
}

class BuyItem implements MenuItem {
  @Override
  public String getName() {
    return "Buy Goods";
  }
  
  @Override
  void select(Menu menu) {
    // TODO: implement
  }
}

class SellItem implements MenuItem {
  @Override
  public String getName() {
    return "Sell Goods";
  }
  
  @Override
  void select(Menu menu) {
    // TODO: implement
  }
}

class InfoItem implements MenuItem {
  @Override
  public String getName() {
    return "Info";
  }
  
  @Override
  void select(Menu menu) {
    // TODO: implement
  }
}

class TakeoffItem implements MenuItem {
  private final LandingSite site;
  
  public TakeoffItem(LandingSite site) {
    this.site = site;
  }
  
  @Override
  public String getName() {
    return "Take Off";
  }
  
  @Override
  void select(Menu menu) {
    site.takeoff();
  }
}

/**
  Default menu renderer implementation; draws buttons and select text
*/
class MenuHandle {
  // Default parameters
  private static final int DEF_SPACING = 100;
  
  private final MenuItem defaultItem;
  private final int spacing;
  
  public MenuHandle() {
    this(new CloseItem());
  }
  
  public MenuHandle(MenuItem defaultItem) {
    this(defaultItem, DEF_SPACING);
  }
  
  public MenuHandle(MenuItem defaultItem, int spacing) {
    this.defaultItem = defaultItem;
    this.spacing = spacing;
  }
  
  public void render(Menu menu) {
    clear(); // TODO: find a way to avoid clearing without creating weird artifacts
    hint(DISABLE_DEPTH_TEST);
    camera();
    noLights();
    //shapeMode(CENTER);
    //shape(logo, width/2, height/4, 339.26, 100);
    textFont(bodyFont);
    
    stroke(0);
    fill(255);
    textAlign(CENTER, CENTER);
    textSize(24);
    for(int i = 0; i < menu.size(); i++) {
      drawButton(menu.getItem(i).getName(), (height / 2) + (i * spacing), menu.getIndex() == i);
    }
    
    //textFont(bodyFont);
    stroke(0);
    fill(255);
    textAlign(CENTER);
    text("X to select", width / 2, (height / 2) + (menu.size() * 100) + 200);
  }
  
  private void drawButton(String name, int yPos, boolean selected) {
    if(selected) stroke(255);
    else stroke(UI_COLOR);
    fill(1);
    rectMode(CENTER);
    rect(width / 2, yPos, 200, 50);
    // Text ----------------------
    //textFont(bodyFont);
    stroke(0);
    fill(UI_COLOR);
    text(name, width / 2, yPos - 3);
  }
  
  public void keyPressed(Menu menu, char key) {
    if(key == ESC) {
      if(defaultItem != null) {
        defaultItem.select(menu);
      }
    }
    else if(key == 'w') {
      if(getSetting("sound") > 0) change.play(); // Play the sound for changing menu selection
      menu.scroll(-1);
    } 
    else if(key == 's') {
      if(getSetting("sound") > 0) change.play(); // Play the sound for changing menu selection
      menu.scroll(1);
    }
    else if(key == 'x') {
      menu.getCursor().select(menu);
    }
  }
}

/**
  Menu renderer for landing on planets
*/
class LandingMenuHandle extends MenuHandle {
  private final LandingSite site;
  
  public LandingMenuHandle(LandingSite site) {
    super(new TakeoffItem(site));
    
    this.site = site;
  }
  
  public void render(Menu menu) {
    super.render(menu);
    
    SpaceObject s = site.getParent();
    textSize(32);
    fill(100);
    text("Welcome to", width/2, height/4);
    textSize(48);
    fill(s.getColor());
    text(s.getName(), width/2, height/4 + 64);
    fill(255);
  }
}

class Menu {
  private final MenuHandle handle;
  
  private final List<MenuItem> items = new ArrayList<MenuItem>();
  
  private int index = 0;
  
  public Menu() {
    this(new MenuHandle());
  }
  
  public Menu(MenuHandle handle) {
    this.handle = handle;
  }
  
  public MenuItem getCursor() {
    return items.get(index);
  }
  
  public int getIndex() {
    return index;
  }
  
  public int size() {
    return items.size();
  }
  
  public MenuItem getItem(int i) {
    return items.get(i);
  }
  
  public void addItem(MenuItem item) {
    items.add(item);
  }
  
  public void scroll(int n) {
    index += n;
    int len = items.size();
    while(index < 0) {
      index += len;
    }
    index %= len;
  }
  
  public void render() {
    handle.render(this);
  }
  
  public void keyPressed(char key) {
    handle.keyPressed(this, key);
  }  
}
