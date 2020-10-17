import tester.Tester;
import javalib.funworld.*;
import javalib.worldimages.*;
import java.awt.Color;
import java.util.Random;

// contains the Fishy! game
class Fishy extends World {
  
  // constants
  int screenWidth = 800;
  int screenHeight = 600;
  WorldImage background = 
      new RectangleImage(screenWidth, screenHeight, OutlineMode.SOLID, Color.blue);

  // variables 
  PlayerFish player;
  Random rand;
  ILoBot bots;

  // Constructor for playing
  Fishy(PlayerFish player) {
    this.player = player;
    this.rand = new Random();
    this.bots = this.generateFish(10, this.rand);
  }

  //Constructor for use in testing
  Fishy(PlayerFish player, Random rand) {
    this.player = player;
    this.rand = rand;
  }

  // Constructor for use in code
  Fishy(PlayerFish player, Random rand, ILoBot bots) {
    this.player = player;
    this.rand = rand;
    this.bots = bots;
  }

  /* fields: 
   *   this.screenWidth ... int
   *   this.screenHeight ... int
   *   this.background ... WorldImage
   *   this.player ... PlayerFish
   *   this.rand ... Random
   *   this.bots ... ILoBot
   * methods: 
   *   this.onKeyEvent(String) ... World
   *   this.generateFish(int, Random) ... ILoBot
   *   this.makeScene() ... WorldScene
   *   this.onTick() ... World
   *   this.worldEnds() ... WorldEnd
   *   this.lastScene(String) ... WorldImage
   * methods for fields:
   *   this.player.drawFish() ... WorldImage
   *   this.player.willCollide(AFish) ... boolean
   *   this.player.canEat(AFish) ... boolean
   *   this.player.checkCollideX(AFish) ... boolean
   *   this.player.checkCollideY(AFish) ... boolean
   *   this.player.withinHitboxX(int) ... boolean
   *   this.player.withinHitboxY(int) ... boolean
   *   this.player.getLeftBox() ... int
   *   this.player.getRightBox() ... int
   *   this.player.getTopBox() ... int
   *   this.player.getBottomBox() ... int
   *   this.player.moveFish(String) ... PlayerFish
   *   this.player.collision(BotFish) ... PlayerFish
   *   this.player.isEaten() ... boolean
   *   this.player.isBigEnough() ... boolean
   *   this.bots.drawBots(WorldImage) ... WorldImage
   *   this.bots.update() ... ILoBot
   *   this.bots.checkCollisions(PlayerFish) ... PlayerFish
   *   this.bots.checkDead(PlayerFish, Random) ... ILoBot
   */

  //moves the player when a key is pressed
  public World onKeyEvent(String ke) { 
    return new Fishy(player.moveFish(ke), this.rand, this.bots);
  }

  // Generates a ILoFIsh of Botfish of length n
  ILoBot generateFish(int n, Random rand) { 
    if (n == 0) {
      return new MtLoBot();
    }

    return new ConsLoBot(new BotFish(rand), generateFish(n - 1, rand));
  }

  // draws the Fishy! game
  public WorldScene makeScene() {
    return this
        .getEmptyScene()
        .placeImageXY(this.background, 400, 300)
        .placeImageXY(this.player.drawFish(), 
            this.player.x, this.player.y)
        .placeImageXY(this.bots.drawBots(new EmptyImage()
            .movePinhole(screenWidth / -2, screenHeight / -2)), 0, 0);
  }

  // updates the BotFishs' positions and checks for eaten fish every tick
  public World onTick() { 
    return new Fishy(this.bots.checkCollisions(this.player), 
        this.rand, this.bots.checkDead(this.player, rand).update());
  }
  
  // checks if the game is over
  public WorldEnd worldEnds() { 
    if (this.player.isEaten()) {
      return new WorldEnd(true, this.lastScene("You Got Eaten!"));
    } else if (this.player.isBigEnough()) {
      return new WorldEnd(true, this.lastScene("Congratulations, You Win!!!"));
    }
    return new WorldEnd(false, this.makeScene());
  }
  
  //produce the last image of this world by adding text to the image 
  public WorldScene lastScene(String s) { 
    return this.makeScene().placeImageXY(new TextImage(s, Color.red), 100,
        40);
  }

}

// an interface to represent a fish in the Fishy! game
interface IFish {

  // constants
  int SCALE = 10;
  int SCREEN_WIDTH = 800;
  int SCREEN_HEIGHT = 600;

  // draws the IFish
  WorldImage drawFish();

}

// an abstract class to represent a Fish in the Fishy! game
abstract class AFish implements IFish {

  // variables
  int x;
  int y;
  int size;
  Color color;
  boolean movingRight;
 
  // draws the AFish
  public WorldImage drawFish() { 
    if (movingRight) {
      return new BesideImage(
          new RotateImage(
              new EquilateralTriangleImage(this.size * SCALE, 
                  OutlineMode.SOLID, this.color), 
              90), 
          new EllipseImage(this.size * SCALE * 2, 
              this.size * SCALE, 
              OutlineMode.SOLID, this.color))
          .movePinhole(this.size * SCALE * 1.5, 0);
    } else {
      return new BesideImage( 
          new EllipseImage(this.size * SCALE * 2, 
              this.size * SCALE, 
              OutlineMode.SOLID, this.color), 
          new RotateImage(
              new EquilateralTriangleImage(this.size * SCALE, 
                  OutlineMode.SOLID, this.color), 
              270))
          .movePinhole(-this.size * SCALE * 1.5, 0);
    }
  }

  // checks if a collision will occur
  boolean willCollide(AFish other) { 
    /* 
     * see the AFish class for other's template
     */
    return (this.checkCollideX(other)
        || other.checkCollideX(this))
        && (this.checkCollideY(other)
        || other.checkCollideY(this));
  }

  // determines if the playable fish can eat a BotFish
  boolean canEat(AFish other) {
    /* 
     * see the AFish class for other's template
     */
    return (this.size >= other.size);
  }

  // checks if this fish's hitbox falls 
  // within another fish's hitbox in the x axis
  boolean checkCollideX(AFish other) { 
    /* 
     * see the AFish class for other's template
     */
    return other.withinHitboxX(this.getLeftBox())
        || other.withinHitboxX(this.getRightBox());
  }

  // checks if this fish's hitbox falls 
  // within another fish's hitbox in the y axis
  boolean checkCollideY(AFish other) {
    /* 
     * see the AFish class for other's template
     */
    return other.withinHitboxY(this.getTopBox())
        || other.withinHitboxY(this.getBottomBox());
  }

  // returns true if the given x value is within the range of the fish's hitbox
  boolean withinHitboxX(int xVal) { 
    return (xVal >= this.getLeftBox())
        && (xVal <= this.getRightBox());
  }

  // returns true if the given y value is within the range of the fish's hitbox
  boolean withinHitboxY(int yVal) { 
    return (yVal >= this.getTopBox())
        && (yVal <= this.getBottomBox());
  }

  // gives the x value of the left side of the hitbox
  int getLeftBox() { 
    if (this.movingRight) {
      return (this.x - (this.size * SCALE * 3));
    } else {
      return x;
    }
  }

  // gives the x value of the right side of the hitbox
  int getRightBox() { 
    if (this.movingRight) {
      return this.x;
    } else {
      return (this.x + (this.size * SCALE * 3));
    }
  }

  // gives the y value of the top of the hitbox
  int getTopBox() {
    return this.y - (this.size * SCALE / 2);
  }

  // gives the y value of the bottom of the hitbox
  int getBottomBox() {
    return this.y + (this.size * SCALE / 2);
  }

}


// the fish controlled by the player in the Fishy! game
class PlayerFish extends AFish {

  // variables
  int fishUntilGrow;

  // convenience constructor
  public PlayerFish() {
    super.x = SCREEN_WIDTH / 2;
    super.y = SCREEN_HEIGHT / 2;
    super.size = 1;
    this.fishUntilGrow = 0;
    super.color = Color.RED;
    super.movingRight = true;

  }

  // constructor for code use
  public PlayerFish(int x, int y, int size, int fishUntilGrow, 
      Color color, boolean movingRight) {
    super.x = x;
    super.y = y; 
    super.size = size;
    this.fishUntilGrow = fishUntilGrow;
    super.color = color;
    super.movingRight = movingRight;
  }
  
  /* fields:  
   *   super.x ... int
   *   super.y ... int
   *   super.size ... int
   *   this.fishUntilGrow ... int
   *   super.color ... Color
   *   super.movingRight ... boolean
   * methods: 
   *   this.drawFish() ... WorldImage
   *   this.willCollide(AFish) ... boolean
   *   this.canEat(AFish) ... boolean
   *   this.checkCollideX(AFish) ... boolean
   *   this.checkCollideY(AFish) ... boolean
   *   this.withinHitboxX(int) ... boolean
   *   this.withinHitboxY(int) ... boolean
   *   this.getLeftBox() ... int
   *   this.getRightBox() ... int
   *   this.getTopBox() ... int
   *   this.getBottomBox() ... int
   *   this.moveFish(String) ... PlayerFish
   *   this.collision(BotFish) ... PlayerFish
   *   this.isEaten() ... boolean
   *   this.isBigEnough() ... boolean
   * methods for fields: 
   * 
   */

  // moves the fish based on the key input
  // if the fish leaves the screen, return on the other side
  public PlayerFish moveFish(String ke) {
    if (ke.equals("left")) {
      int newX = super.x - 5;
      if (newX < 0) {
        return new PlayerFish(SCREEN_WIDTH + newX, super.y, 
            super.size, this.fishUntilGrow, super.color, false);
      } else {
        return new PlayerFish(newX, super.y, 
            super.size, this.fishUntilGrow, super.color, false);
      }
    } else if (ke.equals("right")) {
      movingRight = true;
      int newX = super.x + 5;
      if (newX > SCREEN_WIDTH) {
        return new PlayerFish(newX - SCREEN_WIDTH, super.y, 
            super.size, this.fishUntilGrow, super.color, true);
      } else {
        return new PlayerFish(newX, super.y, 
            super.size, this.fishUntilGrow, super.color, true);
      }
    } else if (ke.equals("up")) {
      int newY = super.y - 5;
      if (newY < 0) {
        return new PlayerFish(super.x, SCREEN_HEIGHT + newY, 
            super.size, this.fishUntilGrow, super.color, super.movingRight);
      } else {
        return new PlayerFish(super.x, newY, 
            super.size, this.fishUntilGrow, super.color, super.movingRight);
      }
    } else if (ke.equals("down")) {
      int newY = super.y + 5;
      if (newY > SCREEN_HEIGHT) {
        return new PlayerFish(super.x, newY - SCREEN_HEIGHT, 
            super.size, this.fishUntilGrow, super.color, super.movingRight);
      } else {
        return new PlayerFish(super.x, newY, 
            super.size, this.fishUntilGrow, super.color, super.movingRight);
      }
    } else {
      return this;
    }
  }
  
  // if a collision happens, have the fish eat if it can
  public PlayerFish collision(BotFish other) { 
    /* 
     * see the BotFish class for other's template
     */
    if (this.willCollide(other) && this.canEat(other)) {
      this.fishUntilGrow ++;
      if (this.fishUntilGrow == 3) {
        super.size ++;
        this.fishUntilGrow = 0;
      }
      return new PlayerFish(super.x, super.y, super.size, 
          this.fishUntilGrow, super.color, super.movingRight);
    } else if (this.willCollide(other) && !this.canEat(other)) {
      return new PlayerFish(super.x, super.y, super.size, 
          -10, super.color, super.movingRight);
    } 
    return this;
  }
  
  // checks if the player fish has been eaten by a bigger fish
  public boolean isEaten() {
    return this.fishUntilGrow < 0;
  }
  
  // checks if the player fish is big enough to win
  public boolean isBigEnough() {
    return this.size > 10;
  }

}

// the non-playable fish in the Fishy! game
class BotFish extends AFish {
  
  // variables
  Random rand;

  // convenience constructor
  public BotFish(Random rand) {
    int randHeight = rand.nextInt(SCREEN_HEIGHT - 20) + 10;
    int rand0o1 = rand.nextInt(2);
    this.rand = rand;
    super.size = rand.nextInt(10) + 1; // Generates a size between 1-10
    super.color = this.randomColor(this.rand);

    if (rand0o1 == 0) {
      super.movingRight = true;
      super.x = 0;
      super.y = randHeight;
    }
    else {
      super.movingRight = false;
      super.x = SCREEN_WIDTH;
      super.y = randHeight;
    }
  }

  // constructor for code purposes
  public BotFish(Random rand, int x, int y, boolean movingRight, int size, Color c) {
    this.rand = rand;
    super.x = x;
    super.y = y;
    super.movingRight = movingRight;
    super.size = size;
    super.color = c;
  }

  /* fields: 
   *   this.rand ... Random
   *   super.x ... int
   *   super.y ... int
   *   super.movingRight ... Boolean
   *   super.size ... int
   *   super.color ... Color
   * methods: 
   *   this.update() ... BotFish
   *   this.randomColor(Random) ... Color
   * methods for fields: 
   * 
   */
  
  // changes the bots position for 1 tick
  public BotFish update() { 
    int speed;

    if (super.x < -300 || super.x > 1100) {
      return new BotFish(this.rand);
    }

    speed = 6 - ((this.size + 1) / 2);

    int newX;

    if (movingRight) {
      newX = this.x + speed;
    } else {
      newX = this.x - speed;
    }

    return new BotFish(this.rand, newX, this.y, this.movingRight, this.size, this.color);
  }

  // generates a random color based off of the given Random object
  public Color randomColor(Random rand) {
    int randInt = rand.nextInt(5);
    if (randInt == 0) { 
      return Color.green; 
    } else if (randInt == 1) { 
      return Color.yellow; 
    } else if (randInt == 2) { 
      return Color.orange; 
    } else if (randInt == 3) { 
      return Color.gray; 
    } else if (randInt == 4) { 
      return Color.pink; 
    }
    return Color.black;
  }
}

// an interface to represent a list of BotFish
interface ILoBot {
  
  // Return a world image containing all the botFish
  WorldImage drawBots(WorldImage img);

  // redraws each fish with a new position
  ILoBot update();
  
  // checks if the player collides with any fish in the list
  PlayerFish checkCollisions(PlayerFish p);
  
  // checks if the player ate any of the fish in the list
  ILoBot checkDead(PlayerFish p, Random rand); // tested

}

// a non-empty list of BotFish
class ConsLoBot implements ILoBot {

  // variables
  BotFish first;
  ILoBot rest;

  // constructor
  public ConsLoBot(BotFish first, ILoBot rest) {
    this.first = first;
    this.rest = rest;
  }
  
  /* fields: 
   *   this.first ... BotFish
   *   this.rest ... ILoBot
   * methods: 
   *   this.drawBots(WorldImage) ... WorldImage
   *   this.update() ... ILoBot
   *   this.checkCollisions(PlayerFish) ... PlayerFish
   *   this.checkDead(PlayerFish, Random) ... ILoBot
   * methods for fields: 
   *   this.first.update() ... BotFish
   *   this.first.randomColor(Random) ... Color
   *   this.rest.drawBots(WorldImage) ... WorldImage
   *   this.rest.update() ... ILoBot
   *   this.rest.checkCollisions(PlayerFish) ... PlayerFish
   *   this.rest.checkDead(PlayerFish, Random) ... ILoBot
   */

  // Adds first fish to the image and then recurs
  public WorldImage drawBots(WorldImage img) {
    return rest.drawBots(((img.movePinhole(this.first.x, this.first.y)).overlayImages(
        this.first.drawFish())).movePinhole(this.first.x * -1, this.first.y * -1));
  }

  // Updates first fishes position
  public ILoBot update() { 
    return new ConsLoBot(this.first.update(), this.rest.update());
  }
  
  // checks if any of the fish collide with the playerfish
  // returns the playerfish after any collisions
  public PlayerFish checkCollisions(PlayerFish p) { 
    /* 
     * see the PlayerFish class for p's template
     */
    return this.rest.checkCollisions(p.collision(this.first));
  }
  
  // checks if any of the fish collide with the playerfish
  // returns the list of bots after any collisions
  public ILoBot checkDead(PlayerFish p, Random rand) { 
    /* 
     * see the PlayerFish class for p's template
     */
    if (p.willCollide(this.first) && p.canEat(this.first)) {
      return new ConsLoBot(new BotFish(rand), 
          this.rest.checkDead(p, rand));
    } else {
      return new ConsLoBot(this.first, this.rest.checkDead(p, rand));
    }
  }

}

// an empty list of BotFish
class MtLoBot implements ILoBot {
  
  /* fields: 
   * 
   * methods: 
   *   this.drawBots(WorldImage) ... WorldImage
   *   this.update() ... ILoBot
   *   this.checkCollisions(PlayerFish) ... PlayerFish
   *   this.checkDead(PlayerFish, Random) ... ILoBot
   * methods for fields: 
   * 
   */

  // Returns the completed image
  public WorldImage drawBots(WorldImage img) {
    return img;
  }

  // Returns an MtLoBot to finish the list
  public ILoBot update() { 
    return new MtLoBot();
  }
  
  // checks if any of the fish in the empty list collide with the player
  // returns the player after any collisions
  public PlayerFish checkCollisions(PlayerFish p) { 
    /* 
     * see the PlayerFish class for p's template
     */
    return p;
  }
  
  // checks if any fish in the empty list collide with the player
  // returns the empty list after any collisions
  public ILoBot checkDead(PlayerFish p, Random rand) {
    /* 
     * see the PlayerFish class for p's template
     */
    return this;
  }
  
}

class ExamplesFishy {

  // example variables
  PlayerFish player1 = new PlayerFish();
  PlayerFish player2 = new PlayerFish(0, 137, 11, -10, Color.red, false);
  PlayerFish player22 = new PlayerFish(0, 137, 11, -10, Color.red, false);
  PlayerFish player3 = new PlayerFish(800, 600, 1, 0, Color.red, true);
  PlayerFish player4 = new PlayerFish(0, 0, 1, 0, Color.red, true);
  PlayerFish player5 = new PlayerFish(0, 0, 11, 0, Color.red, true);
  PlayerFish player6 = new PlayerFish(0, 0, 1, -10, Color.red, true);
  Random rand = new Random(5);
  Fishy w = new Fishy(this.player1);
  Fishy testW = new Fishy(this.player1, rand);
  Fishy testWwBots = new Fishy(this.player1, rand, testW.generateFish(1, new Random(5)));
  Fishy testWwNoBots = new Fishy(this.player1, rand, testW.generateFish(0, new Random(5)));
  ILoBot testEmptyBot = new MtLoBot();
  BotFish botFish = new BotFish(this.rand, 0, 100, true, 5, Color.yellow);
  BotFish botFishR = new BotFish(this.rand, 0, 100, true, 5, Color.yellow);
  ILoBot testBot = new ConsLoBot(botFish, testEmptyBot);
  BotFish botFishL = new BotFish(rand, 800, 100, false, 1, Color.black);
  WorldImage emptyImg = new EmptyImage().movePinhole(-400, -300);
  Fishy winW = new Fishy(player5);
  Fishy loseW = new Fishy(player6);
  Fishy continueW = new Fishy(player3);

  // a class to test Fishy
  boolean testFishy(Tester t) {
    return w.bigBang(800, 600, .05);
  }
  
  // ~ tests for Fishy class ~
  // test onKeyEvent
  boolean testOnKeyEvent(Tester t) {
    return t.checkExpect(testW.onKeyEvent("left"), 
        new Fishy(this.player1.moveFish("left"), testW.rand, testW.bots))
        && t.checkExpect(testW.onKeyEvent("right"), 
            new Fishy(this.player1.moveFish("right"), testW.rand, testW.bots))
        && t.checkExpect(testW.onKeyEvent("up"), 
            new Fishy(this.player1.moveFish("up"), testW.rand, testW.bots))
        && t.checkExpect(testW.onKeyEvent("down"), 
            new Fishy(this.player1.moveFish("down"), testW.rand, testW.bots))
        && t.checkExpect(testW.onKeyEvent(":("), 
            new Fishy(this.player1.moveFish(":("), testW.rand, testW.bots))
        && t.checkExpect(testW.onKeyEvent("f"), 
            new Fishy(this.player1.moveFish("f"), testW.rand, testW.bots))
        && t.checkExpect(testW.onKeyEvent(";~;"), 
            new Fishy(this.player1.moveFish(";~;"), testW.rand, testW.bots));
  }
  
  // method to testGenerateFish
  boolean testGenerateFish(Tester t) {
    return t.checkExpect(testW.generateFish(0, new Random(5)), new MtLoBot())
        && t.checkExpect(testW.generateFish(1, new Random(5)), 
            new ConsLoBot(new BotFish(new Random(5)), new MtLoBot()));
  }
  
  // test onTick
  boolean testOnTick(Tester t) {
    return t.checkExpect(testWwNoBots.onTick(), testWwNoBots)
        && t.checkExpect(testWwBots.onTick(), new Fishy(this.player1, 
            this.rand, new ConsLoBot(new BotFish(rand, 3, 137, true, 5, Color.pink),
                new MtLoBot())));
  }
  
  // test worldEnds
  boolean testWorldEnds(Tester t) {
    return t.checkExpect(loseW.worldEnds(), new WorldEnd(true, loseW.lastScene("You Got Eaten!")))
        && t.checkExpect(winW.worldEnds(), new WorldEnd(true, 
            winW.lastScene("Congratulations, You Win!!!")))
        && t.checkExpect(continueW.worldEnds(), new WorldEnd(false, continueW.makeScene()));
  }
  
  // test lastScene
  boolean testLastScene(Tester t) {
    return t.checkExpect(winW.lastScene(":("), 
        winW.makeScene().placeImageXY(new TextImage(":(", Color.red), 100,40))
        && t.checkExpect(winW.lastScene(":)"), 
            winW.makeScene().placeImageXY(new TextImage(":)", Color.red), 100,40));
  } 
  
  
  // ~ tests for AFish class ~
  // test drawFish
  boolean testDrawFish(Tester t) {
    return t.checkExpect(botFishR.drawFish(), new BesideImage(
        new RotateImage(
            new EquilateralTriangleImage(50, 
                OutlineMode.SOLID, Color.yellow), 
            90), 
        new EllipseImage(50 * 2, 
            50, 
            OutlineMode.SOLID, Color.yellow))
        .movePinhole(50 * 1.5, 0))
        && t.checkExpect(botFishL.drawFish(), new BesideImage( 
            new EllipseImage(20, 
                10, 
                OutlineMode.SOLID, Color.black), 
            new RotateImage(
                new EquilateralTriangleImage(10, 
                    OutlineMode.SOLID, Color.black), 
                270))
            .movePinhole(-10 * 1.5, 0));
  }
  
  // test willCollide
  boolean testWillCollide(Tester t) {
    return t.checkExpect(this.botFish.willCollide(this.player1), false)
        && t.checkExpect(this.player2.willCollide(this.botFish), true);
  }
  
  // test canEat
  boolean testCanEat(Tester t) {
    return t.checkExpect(this.player1.canEat(this.botFish), false)
        && t.checkExpect(this.player2.canEat(this.botFish), true);
  }
  
  // test checkCollideX and CheckCollideY
  boolean testCheckCollide(Tester t) {
    return t.checkExpect(this.player1.checkCollideX(this.botFish), false)
        && t.checkExpect(this.player1.checkCollideY(this.botFish), false)
        && t.checkExpect(this.botFish.checkCollideX(this.player2), true)
        && t.checkExpect(this.botFish.checkCollideY(this.player2), true);
  }
  
  // test withinHitboxX and withinHitboxY
  boolean testWithinHitbox(Tester t) {
    return t.checkExpect(this.player1.withinHitboxX(400), true)
        && t.checkExpect(this.player1.withinHitboxY(295), true)
        && t.checkExpect(this.player2.withinHitboxX(500), false)
        && t.checkExpect(this.player2.withinHitboxY(600), false);
  }
  
  // test getLeftBox and getRightBox
  boolean testGetXBox(Tester t) { 
    // not to be confused with testGetXBoxSeriesX, 
    // which will be done once it releases
    return t.checkExpect(this.player1.getLeftBox(), 370)
        && t.checkExpect(this.player1.getRightBox(), 400)
        && t.checkExpect(this.player2.getLeftBox(), 0)
        && t.checkExpect(this.player2.getRightBox(), 330);     
  }
  
  // test getTopBox and getBottomBox
  boolean testGetYBox(Tester t) { 
    // not to be confused w - wait I made this joke already
    return t.checkExpect(this.player1.getTopBox(), 295)
        && t.checkExpect(this.player1.getBottomBox(), 305)
        && t.checkExpect(this.player3.getTopBox(), 595)
        && t.checkExpect(this.player3.getBottomBox(), 605);
  }
  
  // ~ tests for PlayerFish class ~
  // test moveFish
  boolean testMoveFish(Tester t) {
    return t.checkExpect(this.player1.moveFish("right"), 
        new PlayerFish(405, 300, 1, 0, Color.red, true))
        && t.checkExpect(this.player3.moveFish("right"), 
            new PlayerFish(5, 600, 1, 0, Color.red, true))
        && t.checkExpect(this.player1.moveFish("left"), 
            new PlayerFish(395, 300, 1, 0, Color.red, false))
        && t.checkExpect(this.player4.moveFish("left"), 
            new PlayerFish(795, 0, 1, 0, Color.red, false))
        && t.checkExpect(this.player1.moveFish("up"), 
            new PlayerFish(400, 295, 1, 0, Color.red, true))
        && t.checkExpect(this.player4.moveFish("up"), 
            new PlayerFish(0, 595, 1, 0, Color.red, true))
        && t.checkExpect(this.player1.moveFish("down"), 
            new PlayerFish(400, 305, 1, 0, Color.red, true))
        && t.checkExpect(this.player3.moveFish("down"), 
            new PlayerFish(800, 5, 1, 0, Color.red, true));
  }
  
  // test collision
  boolean testCollision(Tester t) {
    return t.checkExpect(this.player1.collision(this.botFish), this.player1)
        && t.checkExpect(this.player2.collision(this.botFish), 
            new PlayerFish(0, 137, 11, -9, Color.red, false));
  }
  
  // test isEaten
  boolean testEaten(Tester t) {
    return t.checkExpect(this.player1.isEaten(), false)
        && t.checkExpect(this.player2.isEaten(), true);
  }
  
  // test isBigEnough method 
  boolean testIsBigEnough(Tester t) {
    return t.checkExpect(player1.isBigEnough(), false)
        && t.checkExpect(player2.isBigEnough(), true);
  }
  
  
  // ~ tests for the BotFish class ~
  //method to test the update method for the BotFish
  boolean testUpdateBotFish(Tester t) {
    return t.checkExpect(botFishR.update(), 
        new BotFish(rand, 3, 100, true, 5, Color.yellow))
        && t.checkExpect(botFishL.update(), 
            new BotFish(rand, 795, 100, false, 1, Color.black));
  }
  
  //tests the random color method
  boolean testRandomColor(Tester t) {
    return t.checkExpect(botFishL.randomColor(new Random(1)), Color.green)
        && t.checkExpect(botFishR.randomColor(new Random(2)), Color.gray);
  }
  
  // ~ tests for the ILoBot class ~
  // test for checkDead
  boolean testCheckDead(Tester t) {
    return t.checkExpect(this.testEmptyBot.checkDead(player1, rand), 
        this.testEmptyBot)
        && t.checkExpect(this.testBot.checkDead(player1, rand), 
            this.testBot)
        && t.checkExpect(this.testBot.checkDead(player2, new Random(1)), 
            new ConsLoBot(new BotFish(new Random(1)), testEmptyBot));
  }

  //method to test the update method for the ILoBot
  boolean testUpdateILoBot(Tester t) {
    return t.checkExpect(testEmptyBot.update(), testEmptyBot)
        && t.checkExpect(testBot.update(), new ConsLoBot(botFishR.update(), testEmptyBot));
  }

  // method to test the drawBots method
  boolean testDrawBots(Tester t) {
    return t.checkExpect(testEmptyBot.drawBots(emptyImg), emptyImg)
        && t.checkExpect(testBot.drawBots(emptyImg), ((((emptyImg
            .movePinhole(0, 100))
            .overlayImages(botFish.drawFish())))
            .movePinhole(0, -100)));
  }
  
  // method to test the checkCollisions method
  boolean testCheckCollisions(Tester t) {
    return t.checkExpect(this.testEmptyBot.checkCollisions(this.player2), 
        this.player2)
        && t.checkExpect(this.testBot.checkCollisions(this.player22), 
            new PlayerFish(0, 137, 11, -9, Color.red, false));
  }
  
}
