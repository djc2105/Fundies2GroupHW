
import java.util.ArrayList;
import java.util.Random;

import tester.*;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;

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
  
}


class FloodItWorld extends World {
  
  // size of the board
  int BOARD_SIZE = 2;
  int TILE_SIZE = 10;
  // All the cells of the game
  ArrayList<ArrayList<Cell>> board;
  
  // constructor
  FloodItWorld() {
    board = this.createBoard();
  }

  // creates the game
  public WorldScene makeScene() {
    return null;
  }
  
  ArrayList<ArrayList<Cell>> createBoard() {
    Random random = new Random();
    ArrayList<ArrayList<Cell>> temp = new ArrayList<ArrayList<Cell>>(BOARD_SIZE);
    
    for (int y = 0; y < BOARD_SIZE; y++) {
      ArrayList<Cell> tempRow = new ArrayList<Cell>(BOARD_SIZE);
      for (int x = 0; x < BOARD_SIZE; x++) {
        tempRow.add(x, new Cell(x, y, random));
      }
      temp.add(y, tempRow);
    }
    
    return temp;
  }
  
  public void onTick() {
    WorldImage background = new RectangleImage((BOARD_SIZE + 2) * TILE_SIZE, 
        (BOARD_SIZE + 2) * TILE_SIZE, OutlineMode.SOLID, Color.white);
    
    this.getEmptyScene().placeImageXY(background, 0, 0);
    
  }
  
}

class ExamplesFloodIt {
  FloodItWorld w = new FloodItWorld();
  
  void testWorld(Tester t) {
    w.bigBang(1000, 1000);
  }
}





















