
import tester.Tester;
import javalib.funworld.*;
import javalib.worldimages.*;
import testClass.PlayerFish;
import java.awt.Color;
import java.util.Random;

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

  // Constructor for playing
  public Fishy(PlayerFish player) {
    this.player = player;
    this.rand = new Random();
  }

  // Constructor for use in testing
  public Fishy(PlayerFish player, Random rand) {
    this.player = player;
    this.rand = rand;
  }
  
  //moves the player when a key is pressed
  public World onKeyEvent(String ke) {
    return new Fishy(player.moveFish(ke));
  }

  // draws the Fishy! game
  public WorldScene makeScene() {
    return this
        .getEmptyScene()
        .placeImageXY(this.BACKGROUND, 400, 300);
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

  // draws the AFish
  public WorldImage drawFish() {
    return new BesideImage(
        new RotateImage(
            new EquilateralTriangleImage(this.size * SCALE, 
                OutlineMode.SOLID, this.color), 
            90), 
        new EllipseImage(this.size * SCALE * 2, 
            this.size * SCALE, 
            OutlineMode.SOLID, this.color));
  }

}


// the fish controlled by the player in the Fishy! game
class PlayerFish extends AFish {

  // constructor
  public PlayerFish(int x, int y, int size, Color color) {
    super.x = x;
    super.y = y; 
    super.size = size;
    super.color = color;
  }

  // moves the fish based on the key input
  // if the fish leaves the screen, return on the other side
  public PlayerFish moveFish(String ke) {
    if (ke.equals("left")) {
      int newX = super.x - 5;
      if (newX < 0) {
        return new PlayerFish(SCREEN_WIDTH + newX, super.y, super.size, super.color);
      } else {
        return new PlayerFish(newX, super.y, super.size, super.color);
      }
    } else if (ke.equals("right")) {
      int newX = super.x + 5;
      if (newX > SCREEN_WIDTH) {
        return new PlayerFish(newX - SCREEN_WIDTH, super.y, super.size, super.color);
      } else {
        return new PlayerFish(newX, super.y, super.size, super.color);
      }
    } else if (ke.equals("up")) {
      int newY = super.y - 5;
      if (newY < 0) {
        return new PlayerFish(super.x, SCREEN_HEIGHT + newY, super.size, super.color);
      } else {
        return new PlayerFish(super.x, super.y, super.size, super.color);
      }
    } else if (ke.equals("down")) {
      int newY = super.y + 5;
      if (newY > SCREEN_HEIGHT) {
        return new PlayerFish(super.x, newY - SCREEN_HEIGHT, super.size, super.color);
      } else {
        return new PlayerFish(super.x, super.y, super.size, super.color);
      }
    } else {
      return this;
    }
  }

}

// the non-playable fish in the Fishy! game
class BotFish extends AFish {
  
  // variables
  boolean movingRight;
  
  // constructor
  public BotFish(Random rand, int height) {
    int randInt = rand.nextInt(height - 20) + 10; // Gets a y height that is between 10-(height-10)
    super.size = rand.nextInt(10) + 1; // Generates a size between 1-10

    if (randInt < ((height-10)/2)) {
      movingRight = true;
      super.x = 0;
      super.y = randInt;
    }
    else {
      movingRight = false;
      super.x = 800;
      super.y = randInt;
    }
  }
}

// an interface to represent a list of BotFish
interface ILoBot {}

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
  
}

// an empty list of BotFish
class MtLoBot implements ILoBot {}

class TestFishy {
  
  // example variables
  PlayerFish player1 = new PlayerFish(400, 300, 1, Color.RED);
  
  // a class to test Fishy
  boolean testFishy(Tester t) {
    Fishy w = new Fishy(this.player1);
    return w.bigBang(800, 600, 1);
  }
  
}