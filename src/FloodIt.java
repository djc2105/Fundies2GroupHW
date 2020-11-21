
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
  Color colorToBeChecked;
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
   *   this.checked ... boolean
   *   this.random ... Random
   * methods: 
   *   this.genColor(Random) ... Color
   *   this.drawCell(WorldScene) ... void
   *   this.getColor() ... Color
   *   this.setColor(Color) ... void
   *   this.floodCell(Color, int, ArrayList<ArrayList<Cell>>) ... void
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
  
  // floods the cells around this cell if they should be flooded
  ArrayList<Cell> floodCell(Color c, int boardSize, ArrayList<ArrayList<Cell>> board) {
    ArrayList<Cell> nextCellsToFlood = new ArrayList<Cell>(0);
    
    if (this.flooded && this.color != c) {
      this.color = c;
    } else if (this.color == c && !this.flooded) {
      this.flooded = true;
    }
    
    if (this.flooded) {
      if (this.x > 0) {
        Cell cellToBeAddedCell = board.get(this.y).get(this.x - 1);
        if (cellToBeAddedCell.color != c || !cellToBeAddedCell.flooded) {  
          nextCellsToFlood.add(cellToBeAddedCell);
        }
      }
      if (this.x < boardSize - 1) {
        Cell cellToBeAddedCell = board.get(this.y).get(this.x + 1);
        if (cellToBeAddedCell.color != c || !cellToBeAddedCell.flooded) {  
          nextCellsToFlood.add(cellToBeAddedCell);
        }
      }
      if (this.y > 0) {
        Cell cellToBeAddedCell = board.get(this.y - 1).get(this.x);
        if (cellToBeAddedCell.color != c || !cellToBeAddedCell.flooded) {  
          nextCellsToFlood.add(cellToBeAddedCell);
        }
      }
      if (this.y < boardSize - 1) {
        Cell cellToBeAddedCell = board.get(this.y + 1).get(this.x);
        if (cellToBeAddedCell.color != c || !cellToBeAddedCell.flooded) {  
          nextCellsToFlood.add(cellToBeAddedCell);
        }
      }
    } 
    for (Cell cell: nextCellsToFlood) {
      cell.colorToBeChecked = c;
    }
    
    return nextCellsToFlood;
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
  int time;
  // We wanted to declare this in a constructor but it breaks the code somehow
  ArrayList<Cell> cellsToFlood = new ArrayList<Cell>(0);
  Boolean worldIsRunning;
  
  // constructor
  FloodItWorld(int boardSize, int scoreMax) {
    this.boardSize = boardSize;
    this.random = new Random();
    this.board = this.createBoard();
    this.floodBoard(this.board.get(0).get(0).color);
    this.score = 0;
    this.scoreMax = scoreMax;
    this.time = 0;
    this.worldIsRunning = true;
  }
  
  // convenience constructor - takes a random
  FloodItWorld(int boardSize, int scoreMax, Random random) {
    this.boardSize = boardSize;
    this.random = random;
    this.board = this.createBoard();
    this.floodBoard(this.board.get(0).get(0).color);
    this.score = 0;
    this.scoreMax = scoreMax;
    this.time = 0;
    this.worldIsRunning = true;
  }
  
  // convenience constructor - takes a board
  FloodItWorld(ArrayList<ArrayList<Cell>> board, int scoreMax, Random random) {
    this.board = board;
    this.boardSize = board.size();
    this.random = random;
    this.score = 0;
    this.scoreMax = scoreMax;
    this.time = 0;
    this.worldIsRunning = true;
  }
  
  /* fields: 
   *   this.board ... ArrayList<ArrayList<Cell>>
   *   this.boardSize ... int
   *   this.random ... Random
   *   this.score ... int
   *   this.scoreMax ... int
   *   this.time ... int
   *   this.cellsToFlood ... ArrayList<Cell>
   *   this.worldIsRunning ... boolean
   * methods: 
   *   this.makeScene() ... WorldScene
   *   this.checkWin() ... Boolean
   *   this.makeEnd(String) ... WorldScene
   *   this.createBoard() ... ArrayList<ArrayList<Cell>>
   *   this.onTick() ... void
   *   this.removeDuplicates(ArrayList<T>) ... ArrayList<T>
   *   this.onMouseClicked(Posn) ... void
   *   this.floodBoard(Color) ... void
   *   this.onKeyEvent(String) ... void
   *   this.resetBoard() ... void
   *   this.lastScene(String) ... WorldScene
   * methods for fields: 
   *   
   */

  // creates the game
  public WorldScene makeScene() {
    WorldScene scene = this.getEmptyScene();
    if (this.checkWin()) {
      scene = this.makeEnd("You Win! Score: " + this.score);
    } else if (this.score > this.scoreMax) {
      scene = this.makeEnd("You Lose ;~;");
    } else {
      int size = this.boardSize * Util.TILE_SIZE;
      scene.placeImageXY(new RectangleImage(size, size + (5 * Util.TILE_SIZE), 
          OutlineMode.SOLID, Color.WHITE), 
          size / 2, size / 2);
      scene.placeImageXY(new TextImage("Score: " + this.score + "/" + this.scoreMax, 
          20, Color.BLACK), size / 4, size + (2 * Util.TILE_SIZE));
      scene.placeImageXY(new TextImage("Time: " + this.time / 10, 
          20, Color.BLACK), 3 * size / 4, size + (2 * Util.TILE_SIZE));
      scene.placeImageXY(new TextImage("Press 'r' to restart or 'escape' to exit", 
          20, Color.BLACK), size / 2 + (Util.TILE_SIZE), size + (4 * Util.TILE_SIZE));
      for (ArrayList<Cell> i : board) {
        for (Cell j : i) {
          j.drawCell(scene);
        }
      }
    }
    return scene;
  }
  
  //returns true if every cell is the same colour
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
  
  // returns the win/loss screen for the game
  WorldScene makeEnd(String s) {
    WorldScene end = this.getEmptyScene();
    end.placeImageXY(new RectangleImage(600, 300, 
        OutlineMode.SOLID, Color.WHITE), 300, 150);
    end.placeImageXY(new TextImage(s, 25, Color.BLACK), 300, 100);
    end.placeImageXY(new TextImage("Press 'r' to restart or 'escape' to exit", 
        25, Color.BLACK), 300, 200);
    return end;
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
  
  // measures the time spent playing
  // EFFECT: adds 1 to time each tick and updates the board flood
  public void onTick() {
    time += 1;
    
    ArrayList<Cell> newCellsToFlood = new ArrayList<Cell>(0);
    
    cellsToFlood = removeDuplicates(cellsToFlood);
    
    for (Cell cell: cellsToFlood) {
      ArrayList<Cell> cellsCellsToFlood = 
          cell.floodCell(cell.colorToBeChecked, this.boardSize, this.board);
      newCellsToFlood.addAll(cellsCellsToFlood);
    }    
    cellsToFlood = newCellsToFlood;
  }
  
  // A method to remove duplicates from a list
  <T> ArrayList<T> removeDuplicates(ArrayList<T> list) {
    ArrayList<T> result = new ArrayList<T>(0);
    
    for (T t: list) {
      if (!result.contains(t)) {
        result.add(t);
      }
    }
    return result;
  }

  // handles mouse clicks
  // EFFECT: Floods the board
  public void onMouseClicked(Posn p) {
    int xPos = (p.x / Util.TILE_SIZE) - 1;
    int yPos = (p.y / Util.TILE_SIZE) - 1;
    if (((xPos >= 0) && (xPos <= boardSize)) 
        && ((yPos >= 0) && (yPos <= boardSize))) {
      Color clickedColor = this.board.get(yPos).get(xPos).getColor();
      Color baseColor = this.board.get(0).get(0).getColor();
      if (clickedColor != baseColor) {
        this.floodBoard(clickedColor);
        score += 1;
      }
    }
  }
  
  // floods the board with the given color
  // EFFECT: changes the color of every cell connected to the top left
  void floodBoard(Color c) {
    ArrayList<Cell> nextCells = this.board.get(0).get(0).floodCell(c, this.boardSize, this.board);
    
    this.cellsToFlood.addAll(nextCells);
  }
  
  // handles key presses
  // EFFECT: resets the board when "r" is pressed
  public void onKeyEvent(String ke) {
    if (ke.equals("r")) {
      this.resetBoard();
      this.score = 0;
      this.time = 0;
    } else if (ke.equals("escape")) {
      this.endOfWorld("Goodbye!");
    }
  }
  
  // restarts the world
  // EFFECT: creates a new board
  void resetBoard() {
    this.board = createBoard();
    this.floodBoard(this.board.get(0).get(0).getColor());
    this.score = 0;
    this.time = 0;
  }
  
  // returns the end scene for the game
  public WorldScene lastScene(String s) {
    this.worldIsRunning = false;
    WorldScene end = this.getEmptyScene();
    end.placeImageXY(new RectangleImage(600, 300, 
        OutlineMode.SOLID, Color.WHITE), 300, 150);
    end.placeImageXY(new TextImage(s, 25, Color.BLACK), 300, 100);
    return end;
  }
  
}

class ExamplesFloodIt {
  
  // example variables
  FloodItWorld w = new FloodItWorld(14, 30);
  Cell tile00 = new Cell(0, 0, new Random(1));
  Cell tile10 = new Cell(1, 0, new Random(2));
  Cell tile01 = new Cell(0, 1, new Random(3));
  Cell tile11 = new Cell(1, 1, new Random(4));
  ArrayList<Cell> row1 = new ArrayList<Cell>(0);
  ArrayList<Cell> row2 = new ArrayList<Cell>(0);
  ArrayList<ArrayList<Cell>> board1 = new ArrayList<ArrayList<Cell>>(0);  
  FloodItWorld testW = new FloodItWorld(this.board1, 40, new Random(5)); 
  FloodItWorld testW2 = new FloodItWorld(2, 40, new Random(5));
  Cell c1 = new Cell(0, 0, new Random(5));
  ArrayList<Integer> a1;
  ArrayList<String> a2;
  
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
    testW = new FloodItWorld(this.board1, 40, new Random(5)); 
    testW2 = new FloodItWorld(2, 40, new Random(5));
    c1 = new Cell(0, 0, new Random(5));
    a1 = new ArrayList<Integer>(Arrays.asList(1,2,3,4,4,5,5,5,5));
    a2 = new ArrayList<String>(Arrays.asList("A","B","C","C"));
  }
  
  // test to play the game
  void testWorld(Tester t) {
    init();
    w.bigBang(1920, 1080, 0.1);
  }
  
  // A method to test the genColor method of Cell
  void testGenColor(Tester t) {
    init();
    t.checkExpect(this.c1.genColor(new Random(5)), Color.pink);
    t.checkExpect(this.c1.genColor(new Random(8)), Color.blue);
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
  
  // test for the getColor and setColor functions
  void testGetSetColor(Tester t) {
    init();
    t.checkExpect(this.tile00.getColor(), Color.GREEN);
    t.checkExpect(this.tile10.getColor(), Color.BLUE);
    t.checkExpect(this.tile01.getColor(), Color.YELLOW);
    t.checkExpect(this.tile11.getColor(), Color.YELLOW);
    this.tile00.setColor(Color.WHITE);
    this.tile10.setColor(Color.WHITE);
    this.tile01.setColor(Color.WHITE);
    this.tile11.setColor(Color.WHITE);
    t.checkExpect(this.tile00.getColor(), Color.WHITE);
    t.checkExpect(this.tile10.getColor(), Color.WHITE);
    t.checkExpect(this.tile01.getColor(), Color.WHITE);
    t.checkExpect(this.tile11.getColor(), Color.WHITE);
  }
  
  // A method to test floodCell
  void testFloodCell(Tester t) {
    init();
    // (Green Flood)  (Blue Not Flood)
    // (Yellow NF)   (Yellow NF)
    t.checkExpect(this.tile00.flooded, true);
    t.checkExpect(this.tile10.flooded, false);
    t.checkExpect(this.tile01.flooded, false);
    t.checkExpect(this.tile11.flooded, false);
    
    this.tile00.floodCell(Color.yellow, 2, this.board1);
    this.tile01.floodCell(Color.yellow, 2, this.board1);
    this.tile10.floodCell(Color.yellow, 2, this.board1);
    this.tile11.floodCell(Color.yellow, 2, this.board1);
    // (Yellow F)  (Blue NF)
    // (Yellow F)  (Yellow F)
    t.checkExpect(this.tile00.flooded, true);
    t.checkExpect(this.tile10.flooded, false);
    t.checkExpect(this.tile01.flooded, true);
    t.checkExpect(this.tile11.flooded, true);
    t.checkExpect(this.tile00.color, Color.yellow);
    t.checkExpect(this.tile10.color, Color.blue);
    t.checkExpect(this.tile01.color, Color.yellow);
    t.checkExpect(this.tile11.color, Color.yellow);
  }
  
  // A method to test the createScene method
  void testMakeScene(Tester t) {
    init();
    WorldScene checkScene = new WorldScene(0, 0);
    checkScene.placeImageXY(
        new RectangleImage(20, 20, OutlineMode.SOLID, Color.WHITE), 10, 10);
    checkScene.placeImageXY(new RectangleImage(10, 10, OutlineMode.SOLID, Color.GREEN)
        .movePinhole(-10, -10), 20, 20);
    checkScene.placeImageXY(new RectangleImage(10, 10, OutlineMode.SOLID, Color.BLUE)
        .movePinhole(-10, -10), 30, 20);
    checkScene.placeImageXY(new RectangleImage(10, 10, OutlineMode.SOLID, Color.YELLOW)
        .movePinhole(-10, -10), 20, 30);
    checkScene.placeImageXY(new RectangleImage(10, 10, OutlineMode.SOLID, Color.YELLOW)
        .movePinhole(-10, -10), 30, 30);
    checkScene.placeImageXY(new RectangleImage(20, 70, OutlineMode.SOLID, Color.WHITE), 
        10, 10);
    checkScene.placeImageXY(new TextImage("Score: 0/40", 20, Color.BLACK), 
        5, 30);
    checkScene.placeImageXY(new TextImage("Time: 0", 20, Color.BLACK),
        15, 30);
    checkScene.placeImageXY(new TextImage("Press 'r' to restart or 'escape' to exit", 
        20, Color.BLACK), 20, 60);
    t.checkExpect(this.testW.makeScene(), checkScene);
  }
  
  // test the checkWin function
  void testCheckWin(Tester t) {
    init();
    t.checkExpect(this.testW.checkWin(), false);
    this.tile00.setColor(Color.WHITE);
    this.tile10.setColor(Color.WHITE);
    this.tile01.setColor(Color.WHITE);
    this.tile11.setColor(Color.WHITE);
    t.checkExpect(this.testW.checkWin(), true);
  }
  
  // test the makeEnd function
  void testMakeEnd(Tester t) {
    init();
    WorldScene checkScene1 = new WorldScene(0, 0);
    checkScene1.placeImageXY(new RectangleImage(600, 300, 
        OutlineMode.SOLID, Color.WHITE), 300, 150);
    checkScene1.placeImageXY(new TextImage("You Win!", 25, Color.BLACK), 300, 100);
    checkScene1.placeImageXY(new TextImage("Press 'r' to restart or 'escape' to exit", 
        25, Color.BLACK), 300, 200);
    WorldScene checkScene2 = new WorldScene(0, 0);
    checkScene2.placeImageXY(new RectangleImage(600, 300, 
        OutlineMode.SOLID, Color.WHITE), 300, 150);
    checkScene2.placeImageXY(new TextImage("You... tie?", 25, Color.BLACK), 300, 100);
    checkScene2.placeImageXY(new TextImage("Press 'r' to restart or 'escape' to exit", 
        25, Color.BLACK), 300, 200);
    t.checkExpect(this.testW.makeEnd("You Win!"), checkScene1);
    t.checkExpect(this.testW.makeEnd("You... tie?"), checkScene2);
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
    testBoard.get(0).get(1).colorToBeChecked = Color.PINK;
    testBoard.get(1).get(0).colorToBeChecked = Color.PINK;
    this.testW2.onTick();
    this.testW2.onTick();
    this.testW2.onTick();
    t.checkExpect(testW2.board, testBoard);
  }
  
  // a test for onTick
  void testOnTick(Tester t) {
    init();
    t.checkExpect(this.testW.time, 0);
    this.testW.onTick();
    t.checkExpect(this.testW.time, 1);
    this.testW.onTick();
    this.testW.onTick();
    this.testW.onTick();
    t.checkExpect(this.testW.time, 4);
  }
  
  // a test for the removeDuplicates method
  void testRemoveDuplicates(Tester t) {
    t.checkExpect(this.testW.removeDuplicates(a1), 
        new ArrayList<Integer>(Arrays.asList(1,2,3,4,5)));
    t.checkExpect(this.testW.removeDuplicates(a2), 
        new ArrayList<String>(Arrays.asList("A","B","C")));
  }
  
  // a test for onMouseClicked
  void testOnMouseClicked(Tester t) {
    init();
    t.checkExpect(this.testW.score, 0);
    t.checkExpect(this.testW.board.get(0).get(0).color, Color.GREEN);
    this.testW.onMouseClicked(new Posn(45, 25));
    this.testW.onTick();
    t.checkExpect(this.testW.score, 1);
    t.checkExpect(this.testW.board.get(0).get(0).color, Color.BLUE);
    this.testW.onMouseClicked(new Posn(35, 45));
    this.testW.onTick();
    t.checkExpect(this.testW.score, 2);
    t.checkExpect(this.testW.board.get(0).get(0).color, Color.YELLOW);
  }
  
  // A method to test the floodBoard method
  void testFloodBoard(Tester t) {
    init();
    // (Green Flood)  (BLue Not Flood)
    // (Yellow NF)   (Yellow NF)
    t.checkExpect(this.tile00.flooded, true);
    t.checkExpect(this.tile10.flooded, false);
    t.checkExpect(this.tile01.flooded, false);
    t.checkExpect(this.tile11.flooded, false);
    t.checkExpect(this.tile00.color, Color.green);
    
    this.testW.floodBoard(Color.yellow);
    this.testW.onTick();
    this.testW.onTick();
    this.testW.onTick();
    // (Yellow F)  (Blue NF)
    // (Yellow F)  (Yellow F)
    t.checkExpect(this.tile00.flooded, true);
    t.checkExpect(this.tile10.flooded, false);
    t.checkExpect(this.tile01.flooded, true);
    t.checkExpect(this.tile11.flooded, true);
    t.checkExpect(this.tile00.color, Color.yellow);
    
    this.testW.floodBoard(Color.blue);
    this.testW.onTick();
    this.testW.onTick();
    this.testW.onTick();
    // (Blue F)  (Blue F)
    // (Blue F)  (Blue F)
    t.checkExpect(this.tile00.flooded, true);
    t.checkExpect(this.tile10.flooded, true);
    t.checkExpect(this.tile01.flooded, true);
    t.checkExpect(this.tile11.flooded, true);
    t.checkExpect(this.tile00.color, Color.blue);
  }
  
  // a test for onKeyEvent
  void testOnKeyEvent(Tester t) {
    init();
    t.checkExpect(this.testW.board.get(0).get(0).color, Color.GREEN);
    this.testW.onKeyEvent("r");
    t.checkExpect(this.testW.board.get(0).get(0).color, Color.PINK);
    this.testW.onKeyEvent("h");
    t.checkExpect(this.testW.board.get(0).get(0).color, Color.PINK);
    t.checkExpect(this.testW.worldIsRunning, true);
    this.testW.onKeyEvent("escape");
    t.checkExpect(this.testW.worldIsRunning, false);
  }
  
  // a test for resetBoard
  void testResetBoard(Tester t) {
    init();
    this.testW.onTick();
    this.testW.onTick();
    this.testW.onTick();
    t.checkExpect(this.testW.board.get(0).get(0).getColor(), Color.GREEN);
    t.checkExpect(this.testW.time, 3);
    this.testW.resetBoard();
    t.checkExpect(this.testW.board.get(0).get(0).getColor(), Color.PINK);
    t.checkExpect(this.testW.time, 0);
  }
  
  // a test for lastScene
  void testLastScene(Tester t) {
    init();
    WorldScene checkWorld1 = new WorldScene(0, 0);
    checkWorld1.placeImageXY(new RectangleImage(600, 300, 
        OutlineMode.SOLID, Color.WHITE), 300, 150);
    checkWorld1.placeImageXY(new TextImage("test", 25, Color.BLACK), 300, 100);
    WorldScene checkWorld2 = new WorldScene(0, 0);
    checkWorld2.placeImageXY(new RectangleImage(600, 300, 
        OutlineMode.SOLID, Color.WHITE), 300, 150);
    checkWorld2.placeImageXY(new TextImage("Goodbye!", 25, Color.BLACK), 300, 100);
    t.checkExpect(this.testW.lastScene("test"), checkWorld1);
    t.checkExpect(this.testW.lastScene("Goodbye!"), checkWorld2);
    
  }
 
}





















