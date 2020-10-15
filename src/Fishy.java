
import tester.Tester;
import javalib.funworld.*;
import javalib.worldimages.*;
import java.awt.Color;
import java.util.Random;

import javax.annotation.processing.Generated;

import com.sun.source.util.TreePathScanner;

/*
 * Sizes range from 1 to 10 for bots
 *    Eat 3 fish to grow
 *    Player starts at 1 and can eat size 1
 * 
 */

// contains the Fishy! game
public class Fishy extends World {
  
  // constants
  int SCREEN_WIDTH = 800;
  int SCREEN_HEIGHT = 600;
  WorldImage BACKGROUND = 
      new RectangleImage(SCREEN_WIDTH, SCREEN_HEIGHT, OutlineMode.SOLID, Color.blue);
  
  // variables 
  PlayerFish player;
  Random rand;
  ILoBot bots;

  // Constructor for playing
  public Fishy(PlayerFish player) {
    this.player = player;
    this.rand = new Random();
    this.bots = this.generateFish(10);
  }

  //Constructor for use in testing
 public Fishy(PlayerFish player, Random rand) {
   this.player = player;
   this.rand = rand;
 }
  
  // Constructor for use in testing
  public Fishy(PlayerFish player, Random rand, ILoBot bots) {
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
  public ILoBot generateFish(int n) {
    if (n == 0) {
      return new MtLoBot();
    }
    
    return new ConsLoBot(new BotFish(this.rand, SCREEN_HEIGHT), generateFish(n-1));
  }

  // draws the Fishy! game
  public WorldScene makeScene() {
    return this
        .getEmptyScene()
        .placeImageXY(this.BACKGROUND, 400, 300)
        .placeImageXY(this.player.drawFish(), 
            this.player.x, this.player.y)
        .placeImageXY(this.bots.drawBots(new EmptyImage()
            .movePinhole(SCREEN_WIDTH/-2, SCREEN_HEIGHT/-2)), 0, 0);
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
  
  // returns the distance from the pinhole
  // to the border of the fish's hitbox
  int giveBoundary() {
    return 0;
  }

}


// the fish controlled by the player in the Fishy! game
class PlayerFish extends AFish {
  
  // variables
  int fishUntilGrow;

  // constructor
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
  
  // checks if a collision will occur
  boolean willCollide(BotFish other) {
    return true;
  }
  
  // determines if the playable fish can eat a BotFish
  boolean canEat(BotFish other) {
    return (super.size >= other.size);
  }

}

// the non-playable fish in the Fishy! game
class BotFish extends AFish {
  
  // constructor
  public BotFish(Random rand, int height) {
    int randHeight = rand.nextInt(height - 20) + 10; // Gets a y height that is between 10-(height-10)
    int rand0o1 = rand.nextInt(2);
    super.size = rand.nextInt(10) + 1; // Generates a size between 1-10
    super.color = Color.black;

    if (rand0o1 == 0) {
      movingRight = true;
      super.x = 0;
      super.y = randHeight;
    }
    else {
      movingRight = false;
      super.x = SCREEN_WIDTH;
      super.y = randHeight;
    }
  }
}

// an interface to represent a list of BotFish
interface ILoBot {
  // Return a world image containing all the botFish
  WorldImage drawBots(WorldImage img);
  
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
  
}

// an empty list of BotFish
class MtLoBot implements ILoBot {

  // Returns the completed image
  public WorldImage drawBots(WorldImage img) {
    return img;
  }
}

class ExamplesFishy {
  
  // example variables
  PlayerFish player1 = new PlayerFish(400, 300, 1, 0, Color.RED, true);
  Fishy w = new Fishy(this.player1);
  
  // a class to test Fishy
  boolean testFishy(Tester t) {
    return w.bigBang(800, 600, 1);
  }
  
}