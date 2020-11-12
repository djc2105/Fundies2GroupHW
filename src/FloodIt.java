
import java.util.ArrayList;
import java.util.Random;
import tester.*;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;
import sun.tools.tree.ThisExpression;

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

  Color genColor(Random random) {
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
  Cell tile00 = new Cell(0, 0, new Random(1));
  Cell tile10 = new Cell(1, 0, new Random(2));
  Cell tile01 = new Cell(1, 0, new Random(3));
  Cell tile11 = new Cell(1, 1, new Random(4));
  ArrayList<Cell> row1 = new ArrayList<Cell>(0);
  ArrayList<Cell> row2 = new ArrayList<Cell>(0);
  ArrayList<ArrayList<Cell>> board1 = new ArrayList<ArrayList<Cell>>(0);  
  FloodItWorld testW = new FloodItWorld(this.board1); 
  Cell c1 = new Cell(0, 0, new Random(5));
  
  void init() {
    w = new FloodItWorld();
    tile00 = new Cell(0, 0, new Random(1));
    tile10 = new Cell(1, 0, new Random(2));
    tile01 = new Cell(1, 0, new Random(3));
    tile11 = new Cell(1, 1, new Random(4));
    row1 = new ArrayList<Cell>(0);
    row2 = new ArrayList<Cell>(0);
    board1 = new ArrayList<ArrayList<Cell>>(0);  
    this.row1.add(tile00);
    this.row1.add(tile10);
    this.row2.add(tile01);
    this.row2.add(tile11);
    this.board1.add(this.row1);
    this.board1.add(this.row2);
    testW = new FloodItWorld(this.board1); 
    c1 = new Cell(0, 0, new Random(5));
  }
  
  
  
  void testWorld(Tester t) {
    int totalSize = Util.TILE_SIZE * 
        (Util.BOARD_SIZE + 2);
    w.bigBang(totalSize, totalSize, 1);
  }
  
  // A method to test the drawCell method of Cell
  void testDrawCell(Tester t) {
    
  }
  
  // A method to test the OnTick method of FloodItWorld
  void testOnTick(Tester t) {
    
  }
  
  // A method to test the createBoard method
  void testCreateBoard(Tester t) {
    
  }
  
  // A method to test the createScene method
  void testCreateScene(Tester t) {
    
  }
  
  // A method to test the genColor method of Cell
  void testGenColor(Tester t) {
    init();
    t.checkExpect(this.c1.genColor(new Random(5)), Color.pink);
    t.checkExpect(this.c1.genColor(new Random(8)), Color.blue);
  }
  
}





















