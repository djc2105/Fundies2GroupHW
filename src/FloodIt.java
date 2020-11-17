
import java.util.*;
import tester.*;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;

// constants used through the code
class Util {

  // size of the tiles
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
  
  // constructor
  public Cell(int x, int y, Random random) {
    
    this.x = x;
    this.y = y;
    this.color = genColor(random);
    this.random = random;
    
    if (this.x == 0 && this.y == 0) {
      this.flooded = true;
    } else {
      this.flooded = false;
    }
  }
  
  /* fields: 
   *   this.x ... int
   *   this.y ... int
   *   this.color ... Color
   *   this.flooded ... boolean
   *   this.random ... Random
   * methods: 
   *   this.genColor(Random) ... Color
   *   this.drawCell(WorldScene) ... void
   * methods for fields: 
   * 
   */

  // returns a random color based off of a random seed
  Color genColor(Random random) {
    ArrayList<Color> colors = new ArrayList<Color>(0);
    colors.add(Color.red);
    colors.add(Color.orange);
    colors.add(Color.yellow);
    colors.add(Color.green);
    colors.add(Color.blue);
    colors.add(Color.pink);
    
    return (colors.get(random.nextInt(6)));
  }
  
  // draws a cell onto the given world scene
  // EFFECT: draws the cell onto the given world scene
  void drawCell(WorldScene ws) {
    int s = Util.TILE_SIZE;
    ws.placeImageXY(new RectangleImage(s, s, 
        OutlineMode.SOLID, this.color)
        .movePinhole(-s / 2, -s / 2), 
        (this.x + 1) * s, 
        (this.y + 1) * s);
  }
  
  // returns the color of the cell
  Color getColor() {
    return this.color;
  }
  
  // changes the color of the cell
  // EFFECT: sets the color to the given color
  void setColor(Color color) {
    this.color = color;
  }
  
}

// represents the game
class FloodItWorld extends World {
 
  // All the cells of the game
  ArrayList<ArrayList<Cell>> board;
  int boardSize;
  int score;
  int scoreMax;
  Random random;
  
  // constructor
  FloodItWorld(int boardSize, int scoreMax) {
    this.boardSize = boardSize;
    this.random = new Random();
    this.board = this.createBoard();
    this.score = 0;
    this.scoreMax = scoreMax;
  }
  
  // convenience constructor - takes a random
  FloodItWorld(int boardSize, int scoreMax, Random random) {
    this.boardSize = boardSize;
    this.random = random;
    this.board = this.createBoard();
    this.score = 0;
    this.scoreMax = scoreMax;
  }
  
  // convenience constructor - takes a board
  FloodItWorld(ArrayList<ArrayList<Cell>> board, int scoreMax) {
    this.board = board;
    this.boardSize = board.size();
    this.random = new Random();
    this.score = 0;
    this.scoreMax = scoreMax;
  }
  
  /* fields: 
   *   this.board ... ArrayList<ArrayList<Cell>>
   *   this.boardSize ... int
   *   this.random ... Random
   * methods: 
   *   this.makeScene() ... WorldScene
   *   this.createBoard() ... ArrayList<ArrayList<Cell>>
   *   this.onMouseClicked(Posn) ... void
   *   this.onKeyEvent(String) ... void
   *   this.lastScene(String) ... WorldScene
   * methods for fields: 
   *   
   */

  // creates the game
  public WorldScene makeScene() {
    WorldScene scene = this.getEmptyScene();
    if (this.checkWin()) {
      scene = this.makeEnd("You Win!");
    } else if (this.score >= this.scoreMax) {
      scene = this.makeEnd("You Lose ;~;");
    } else {
      int size = this.boardSize * Util.TILE_SIZE;
      scene.placeImageXY(new RectangleImage(size, size + (3 * Util.TILE_SIZE), 
          OutlineMode.SOLID, Color.WHITE), 
          size / 2, size / 2);
      scene.placeImageXY(new TextImage("Score: " + score, 20, Color.BLACK), 
          size / 2, size + (2 * Util.TILE_SIZE));
      for (ArrayList<Cell> i : board) {
        for (Cell j : i) {
          j.drawCell(scene);
        }
      }
    }
    return scene;
  }
  
  // creates a random game board
  ArrayList<ArrayList<Cell>> createBoard() {
    ArrayList<ArrayList<Cell>> temp = new ArrayList<ArrayList<Cell>>(this.boardSize);
    for (int y = 0; y < this.boardSize; y++) {
      ArrayList<Cell> tempRow = new ArrayList<Cell>(this.boardSize);
      for (int x = 0; x < this.boardSize; x++) {
        tempRow.add(x, new Cell(x, y, random));
      }
      temp.add(y, tempRow);
    }
    return temp;
  }
  
  // handles mouse clicks
  // EFFECT: Floods the board
  public void onMouseClicked(Posn p) {
    int xPos = (p.x / Util.TILE_SIZE) - 1;
    int yPos = (p.y / Util.TILE_SIZE) - 1;
    if (((xPos >= 0) && (xPos <= boardSize))
      && ((yPos >= 0) && (yPos <= boardSize))) {
      score += 1;
      Color clickedColor = this.board.get(yPos).get(xPos).getColor();
      this.floodBoard(clickedColor);
      
    }
  }
  
  // floods the board with the given color
  // EFFECT: changes the color of every cell connected to the top left
  void floodBoard(Color c) {
    board.get(0).get(0).setColor(c);
  }
  
  // returns true if every cell is the same colour
  boolean checkWin() {
    Color curColor = this.board.get(0).get(0).getColor();
    for (ArrayList<Cell> i : board) {
      for (Cell j : i) {
        if (j.getColor() != curColor) {
          return false;
        }
      }
    }
    return true;
  }
  
  // handles key presses
  // EFFECT: resets the board when "r" is pressed
  public void onKeyEvent(String ke) {
    if (ke.equals("r")) {
      this.resetBoard();
      this.score = 0;
    }
  }
  
  // restarts the world
  // EFFECT: creates a new board
  void resetBoard() {
    this.board = createBoard();
  }
  
  // renders the last scene of the game
  WorldScene makeEnd(String s) {
    WorldScene end = this.getEmptyScene();
    end.placeImageXY(new RectangleImage(600, 300, OutlineMode.SOLID, Color.WHITE), 300, 150);
    end.placeImageXY(new TextImage(s, 25, Color.BLACK), 300, 150);
    return end;
  }
  
}

class ExamplesFloodIt {
  
  // example variables
  FloodItWorld w = new FloodItWorld(24, 25);
  Cell tile00 = new Cell(0, 0, new Random(1));
  Cell tile10 = new Cell(1, 0, new Random(2));
  Cell tile01 = new Cell(0, 1, new Random(3));
  Cell tile11 = new Cell(1, 1, new Random(4));
  ArrayList<Cell> row1 = new ArrayList<Cell>(0);
  ArrayList<Cell> row2 = new ArrayList<Cell>(0);
  ArrayList<ArrayList<Cell>> board1 = new ArrayList<ArrayList<Cell>>(0);  
  FloodItWorld testW = new FloodItWorld(this.board1, 25); 
  FloodItWorld testW2 = new FloodItWorld(2, 25, new Random(5));
  Cell c1 = new Cell(0, 0, new Random(5));
  
  // initialize the data
  void init() {
    tile00 = new Cell(0, 0, new Random(1));
    tile10 = new Cell(1, 0, new Random(2));
    tile01 = new Cell(0, 1, new Random(3));
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
    testW = new FloodItWorld(this.board1, 25); 
    testW2 = new FloodItWorld(2, 25, new Random(5));
    c1 = new Cell(0, 0, new Random(5));
  }
  
  // test to play the game
  void testWorld(Tester t) {
    init();
    // int totalSize = Util.TILE_SIZE 
    //    * (24 + 2);
    w.bigBang(1920, 1080, 1);
  }
  
  // A method to test the drawCell method of Cell
  void testDrawCell(Tester t) {
    // initialize the variables used
    init();
    WorldScene testScene = new WorldScene(20, 20);
    testScene.placeImageXY(
        new RectangleImage(20, 20, OutlineMode.SOLID, Color.WHITE), 10, 10);
    WorldScene checkScene = new WorldScene(20, 20);
    checkScene.placeImageXY(
        new RectangleImage(20, 20, OutlineMode.SOLID, Color.WHITE), 10, 10);
    // draw the cell and the expected result
    this.tile00.drawCell(testScene);
    checkScene.placeImageXY(new RectangleImage(10, 10, OutlineMode.SOLID, Color.BLUE)
        .movePinhole(-10, -10), 20, 20);
    // test
    t.checkExpect(testScene, checkScene);
    // draw the cell and the expected result
    this.tile01.drawCell(testScene);
    checkScene.placeImageXY(new RectangleImage(10, 10, OutlineMode.SOLID, Color.GREEN)
        .movePinhole(-10, -10), 30, 20);
    // test
    t.checkExpect(testScene, checkScene);
    // draw the cell and the expected result
    this.tile10.drawCell(testScene);
    checkScene.placeImageXY(new RectangleImage(10, 10, OutlineMode.SOLID, Color.YELLOW)
        .movePinhole(-10, -10), 20, 30);
    // test
    t.checkExpect(testScene, checkScene);
    // draw the cell and the expected result
    this.tile11.drawCell(testScene);
    checkScene.placeImageXY(new RectangleImage(10, 10, OutlineMode.SOLID, Color.YELLOW)
        .movePinhole(-10, -10), 30, 30);
    // test
    t.checkExpect(testScene, checkScene);
  }
  
  // A method to test the createBoard method
  void testCreateBoard(Tester t) {
    init();
    // check that testW2's board creates a board using the given random seed
    Random testRand = new Random(5);
    ArrayList<ArrayList<Cell>> testBoard =
        new ArrayList<ArrayList<Cell>>(Arrays.asList(
            new ArrayList<Cell>(Arrays.asList(
                new Cell(0, 0, testRand), 
                new Cell(1, 0, testRand))), 
            new ArrayList<Cell>(Arrays.asList(
                new Cell(0, 1, testRand), 
                new Cell(1, 1, testRand)))));
    t.checkExpect(testW2.board, testBoard);
  }
  
  // A method to test the createScene method
  void testMakeScene(Tester t) {
    init();
    WorldScene checkScene = new WorldScene(0, 0);
    checkScene.placeImageXY(
        new RectangleImage(20, 20, OutlineMode.SOLID, Color.WHITE), 10, 10);
    checkScene.placeImageXY(new RectangleImage(10, 10, OutlineMode.SOLID, Color.BLUE)
        .movePinhole(-10, -10), 20, 20);
    checkScene.placeImageXY(new RectangleImage(10, 10, OutlineMode.SOLID, Color.GREEN)
        .movePinhole(-10, -10), 30, 20);
    checkScene.placeImageXY(new RectangleImage(10, 10, OutlineMode.SOLID, Color.YELLOW)
        .movePinhole(-10, -10), 20, 30);
    checkScene.placeImageXY(new RectangleImage(10, 10, OutlineMode.SOLID, Color.YELLOW)
        .movePinhole(-10, -10), 30, 30);
    t.checkExpect(this.testW.makeScene(), checkScene);
  }
  
  // A method to test the genColor method of Cell
  void testGenColor(Tester t) {
    init();
    t.checkExpect(this.c1.genColor(new Random(5)), Color.pink);
    t.checkExpect(this.c1.genColor(new Random(8)), Color.blue);
  }
  
}





















