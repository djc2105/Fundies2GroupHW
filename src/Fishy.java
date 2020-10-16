import tester.Tester;
import javalib.funworld.*;
import javalib.worldimages.*;
import sun.tools.tree.ThisExpression;

import java.awt.Color;
import java.util.Random;

/*
 * Sizes range from 1 to 10 for bots
 *    Eat 3 fish to grow
 *    Player starts at 1 and can eat size 1
 * 
 */

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
    this.bots = this.generateFish(10);
  }

  //Constructor for use in testing
  Fishy(PlayerFish player, Random rand) {
    this.player = player;
    this.rand = rand;
  }

  // Constructor for use in testing
  Fishy(PlayerFish player, Random rand, ILoBot bots) {
    this.player = player;
    this.rand = rand;
    this.bots = bots;
  }

  /* fields: 
   *   this.player ... PlayerFish
   *   this.rand ... Random
   * methods: 
   *   this.onKeyEvent(String) ... World
   *   this.makeScene() ... WorldScene
   * methods for fields:
   *   this.player.drawFish() ... WorldImage
   *   this.player.moveFish(String) ... PlayerFish
   *   this.player.canEat(BotFish) ... Boolean
   */

  //moves the player when a key is pressed
  public World onKeyEvent(String ke) {
    return new Fishy(player.moveFish(ke), this.rand, this.bots);
  }

  // Generates a ILoFIsh of Botfish of length n
  ILoBot generateFish(int n) {
    if (n == 0) {
      return new MtLoBot();
    }

    return new ConsLoBot(new BotFish(this.rand), generateFish(n-1));
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

  public World onTick() {
    return new Fishy(this.bots.checkCollisions(this.player), 
        this.rand, this.bots.checkDead(this.player).update());
  }
  
  public WorldEnd worldEnds() {
    if (this.player.isEaten()) {
      return new WorldEnd(true, this.lastScene("you got eaten"));
    } else if (this.player.isBigEnough()) {
      return new WorldEnd(true, this.lastScene("you win"));
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
    return (this.checkCollideX(other)
        || other.checkCollideX(this))
        && (this.checkCollideY(other)
        || other.checkCollideY(this));
  }

  // determines if the playable fish can eat a BotFish
  boolean canEat(AFish other) {
    return (this.size >= other.size);
  }

  // checks if this fish's hitbox falls 
  // within another fish's hitbox in the x axis
  boolean checkCollideX(AFish other) {
    return other.withinHitboxX(this.getLeftBox())
        || other.withinHitboxX(this.getRightBox());
  }

  // checks if this fish's hitbox falls 
  // within another fish's hitbox in the y axis
  boolean checkCollideY(AFish other) {
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
    return this.y - (this.size * SCALE);
  }

  // gives the y value of the bottom of the hitbox
  int getBottomBox() {
    return this.y + (this.size * SCALE);
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
  
  public boolean isEaten() {
    return this.fishUntilGrow < 0;
  }
  
  public boolean isBigEnough() {
    return this.size > 10;
  }

}

// the non-playable fish in the Fishy! game
class BotFish extends AFish {
  Random rand;

  // constructor
  public BotFish(Random rand) {
    int randHeight = rand.nextInt(SCREEN_HEIGHT - 20) + 10;
    int rand0o1 = rand.nextInt(2);
    this.rand = rand;
    super.size = rand.nextInt(10) + 1; // Generates a size between 1-10
    super.color = this.randomColor();

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

  public BotFish(Random rand, int x, int y, boolean movingRight, int size, Color c) {
    this.rand = rand;
    super.x = x;
    super.y = y;
    super.movingRight = movingRight;
    super.size = size;
    super.color = c;
  }

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

  public Color randomColor() {
    int rand = this.rand.nextInt(5);
    if(rand == 0) { 
      return Color.green; 
    }
    if(rand == 1) { 
      return Color.yellow; 
    }
    if(rand == 2) { 
      return Color.orange; 
    }
    if(rand == 3) { 
      return Color.gray; 
    }
    if(rand == 4) { 
      return Color.pink; 
    }
    return Color.black;
  }
}

// an interface to represent a list of BotFish
interface ILoBot {
  // Return a world image containing all the botFish
  WorldImage drawBots(WorldImage img);

  ILoBot update();
  
  PlayerFish checkCollisions(PlayerFish p);
  
  ILoBot checkDead(PlayerFish p);

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

  // Adds first fish to the image and then recurs
  public WorldImage drawBots(WorldImage img) {
    return rest.drawBots(((img.movePinhole(this.first.x, this.first.y)).overlayImages(
        this.first.drawFish())).movePinhole(this.first.x * -1, this.first.y * -1));
  }

  // Updates first fishes position
  public ILoBot update() {
    return new ConsLoBot(this.first.update(), this.rest.update());
  }
  
  public PlayerFish checkCollisions(PlayerFish p) {
    return this.rest.checkCollisions(p.collision(this.first));
  }
  
  public ILoBot checkDead(PlayerFish p) {
    if (p.willCollide(this.first) && p.canEat(this.first)) {
      return new ConsLoBot(new BotFish(new Random()), 
          this.rest.checkDead(p));
    } else {
      return new ConsLoBot(this.first, this.rest.checkDead(p));
    }
  }

}

// an empty list of BotFish
class MtLoBot implements ILoBot {

  // Returns the completed image
  public WorldImage drawBots(WorldImage img) {
    return img;
  }

  // Returns an MtLoBot to finish the list
  public ILoBot update() {
    return new MtLoBot();
  }
  
  public PlayerFish checkCollisions(PlayerFish p) {
    return p;
  }
  
  public ILoBot checkDead(PlayerFish p) {
    return this;
  }
  
}

class ExamplesFishy {

  // example variables
  PlayerFish player1 = new PlayerFish();
  Random rand = new Random(5);
  Fishy w = new Fishy(this.player1);
  Fishy testW = new Fishy(this.player1, rand);

  // a class to test Fishy
  boolean testFishy(Tester t) {
    return w.bigBang(800, 600, .05);
  }
  
  // method to testGenerateBots
  //boolean testGenerateBots(Tester t) {
  //  return t.checkExpect(testW.generateFish(0), new MtLoBot())
  //      && t.checkExpect(testW.generateFish(1), new ConsLoBot(new BotFish(rand), new MtLoBot()));
  //}
  
  
}