package vekta;

interface World extends Context {
  /**
    Initializes all required variables
  */
  void init();
  
  /**
    Called when the gamemode restarts
  */
  void restart();
  
  boolean addObject(Object object);
  
  boolean removeObject(Object object);
}  
