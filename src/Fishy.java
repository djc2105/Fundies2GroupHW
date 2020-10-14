import tester.Tester;
import javalib.funworld.*;
import javalib.worldimages.*;
import java.awt.Color;
import java.util.Random;


public class Fishy extends World{
  int width = 800;
  int height = 600;
  
  
  @Override
  public WorldScene makeScene() {
    // TODO Auto-generated method stub
    return null;
  }
}
  
interface IFish {
  
}

abstract class AFish implements IFish{
  int x;
  int y;
  int size;
  
}


class PlayerFish extends AFish {
  
}

class BotFish extends AFish {
  boolean movingLeft;
  
  public BotFish() {
    double randValue = Math.random();
    if (randValue < 0.5) {
      movingLeft = true;
      super.x = 800;
      super.y = (int) (300 * randValue + 1);
    }
  }
}

interface ILoBot {
  
}

class ConsLoBot implements ILoBot {
  
}

class MtLoBot implements ILoBot {
  
}