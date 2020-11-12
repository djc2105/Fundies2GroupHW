
import java.util.ArrayList;
import java.util.Random;

import tester.*;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;

class Util {
  
  static int BOARD_SIZE = 24;
  static int TILE_SIZE = 20;
  
}

//Represents a single square of the game area
class Cell {
  
  // In logical coordinates, with the origin at the top-left corner of the screen
  int x;
  int y;
  Color color;
  boolean flooded;
  Random random;
  
  public Cell(int x, int y, Random random) {
    
    this.x = x;
    this.y = y;
    this.color = genColor(random);
    this.random = random;
    
    if(this.x == 0 && this.y == 0) {
      this.flooded = true;
    } else {
      this.flooded = false;
    }
  }

  private Color genColor(Random random) {
    ArrayList<Color> colors = new ArrayList<Color>(0);
    colors.add(Color.red);
    colors.add(Color.orange);
    colors.add(Color.yellow);
    colors.add(Color.green);
    colors.add(Color.blue);
    colors.add(Color.pink);
    
    return(colors.get(random.nextInt(6)));
  }
  
  void drawCell(WorldScene ws) {
    int s = Util.TILE_SIZE;
    ws.placeImageXY(new RectangleImage(s, s, 
        OutlineMode.SOLID, this.color)
        .movePinhole(-s / 2, -s / 2), 
        (this.x + 1) * s, 
        (this.y + 1) * s);
  }
  
}


class FloodItWorld extends World {
 
  // All the cells of the game
  ArrayList<ArrayList<Cell>> board;
  
  // constructor
  FloodItWorld() {
    this.board = this.createBoard();
  }
  
  // convenience constructor
  FloodItWorld(ArrayList<ArrayList<Cell>> board) {
    this.board = board;;
  }

  // creates the game
  public WorldScene makeScene() {
    int size = Util.BOARD_SIZE * Util.TILE_SIZE;
    WorldScene scene = this.getEmptyScene();
    scene.placeImageXY(new RectangleImage(size, size, OutlineMode.SOLID, Color.WHITE), 
        size / 2, size / 2);
    for (ArrayList<Cell> i : board) {
      for (Cell j : i) {
        j.drawCell(scene);
      }
    }
    return scene;
  }
  
  ArrayList<ArrayList<Cell>> createBoard() {
    Random random = new Random();
    ArrayList<ArrayList<Cell>> temp = new ArrayList<ArrayList<Cell>>(Util.BOARD_SIZE);
    
    for (int y = 0; y < Util.BOARD_SIZE; y++) {
      ArrayList<Cell> tempRow = new ArrayList<Cell>(Util.BOARD_SIZE);
      for (int x = 0; x < Util.BOARD_SIZE; x++) {
        tempRow.add(x, new Cell(x, y, random));
      }
      temp.add(y, tempRow);
    }
    
    return temp;
  }
  
  public void onTick() {
    WorldImage background = 
        new RectangleImage((Util.BOARD_SIZE + 2) * Util.TILE_SIZE, 
        (Util.BOARD_SIZE + 2) * Util.TILE_SIZE, 
        OutlineMode.SOLID, Color.white);
    
    this.getEmptyScene().placeImageXY(background, 0, 0);
    
  }
  
}

class ExamplesFloodIt {
  FloodItWorld w = new FloodItWorld();
  void testWorld(Tester t) {
    int totalSize = Util.TILE_SIZE * 
        (Util.BOARD_SIZE + 2);
    w.bigBang(totalSize, totalSize, 1);
  }
}





















