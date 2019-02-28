package vekta;

class Inventory implements Iterable<Item> {
  private final List<Item> items = new ArrayList<Item>();
  private int money;
  
  public Inventory() {
    
  }
  
  public Inventory(int money) {
    add(money);
  }
  
  public int getMoney() {
    return money;
  }
  
  public boolean has(int amount) {
    return amount <= money;
  }
  
  public void add(int amount) {
    money += amount;
  }
  
  public boolean remove(int amount) {
    if(!has(amount)) {
      return false;
    }
    money -= amount;
    return true;
  }
  
  public List<Item> getItems() {
    return items;
  }
  
  public boolean has(Item item) {
    return items.contains(item);
  }
  
  public void add(Item item) {
    items.add(item);
    Collections.sort(items);
  }
  
  public boolean remove(Item item) {
    return items.remove(item);
  }
  
  @Override
  public Iterator<Item> iterator() {
    return getItems().iterator();
  }
}

class Item implements Comparable<Item> {
  private final String name;
  private final ItemType type;
  
  public Item(String name, ItemType type) {
    this.name = name;
    this.type = type;
  }
  
  public String getName() {
    return name;
  }
  
  public ItemType getType() {
    return type;
  }
  
  @Override
  public int compareTo(Item other) {
    return this.getName().compareTo(other.getName());
  }
}

enum ItemType {
  COMMON(0xFFFFFF55),
  RARE(0xFF55FF55),
  LEGENDARY(0xFFFF55FF);
  
  private final color c;
  
  private ItemType(color c) {
    this.c = c;
  }
  
  public color getColor() {
    return c;
  }
}
