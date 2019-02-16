
interface Gamemode {
  /**
    Initializes all required variables
  */
  void init();
  
  /**
    Draws all the things
  */
  void render();
  
  /**
    What to do when a key is pressed
  */
  void keyPressed(char key);
  
  /**
    What to do when a key is released
  */
  void keyReleased(char key);
}  
