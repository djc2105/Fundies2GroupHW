import tester.Tester;
import javalib.funworld.*;
import javalib.worldimages.*;
import java.awt.Color;
import java.util.Random;

/*
 * Sizes range from 1 to 10 for bots
 *    Eat 3 fish to grow
 *    Player starts at 1 and can eat size 1
 * 
 */

public class Fishy extends World{
  int width = 800;
  int height = 600;
  Random rand;
  WorldImage backGround = new RectangleImage(width, height, OutlineMode.SOLID, Color.blue);
  
  // Constructor for playing
  public Fishy() {
    this.rand = new Random();
  }
  
  // Constructor for use in testing
  public Fishy(Random rand) {
    this.rand = rand;
  }
  
  public WorldScene makeScene() {
    return this
        .getEmptyScene()
        .placeImageXY(this.backGround, 400, 300);
  }
}
  
interface IFish {
  WorldImage draw(Color c);
}

abstract class AFish implements IFish{
  int x;
  int y;
  int size;
  
  public WorldImage draw(Color c) { 
    return new EllipseImage(this.size * 20, this.size * 10, OutlineMode.SOLID, c);
  }
  
}


class PlayerFish extends AFish {
  
}

class BotFish extends AFish {
  boolean movingRight;
  
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

interface ILoBot {
  
}

class ConsLoBot implements ILoBot {
  BotFish first;
  ILoBot rest;
  
  public ConsLoBot(BotFish first, ILoBot rest) {
    this.first = first;
    this.rest = rest;
  }
}

class MtLoBot implements ILoBot {
  
}

class TestFishy {
  boolean testFishy(Tester t) {
    Fishy w = new Fishy();
    return w.bigBang(800, 600, 1);
  }
}