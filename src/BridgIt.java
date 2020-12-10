
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
  Cell above;
  Cell below;
  Cell left;
  Cell right;
  
  // constructor
  public Cell(int x, int y, Color color) {
    this.x = x;
    this.y = y;
    this.color = color;
  }
  
  /* fields: 
   *   this.x ... int
   *   this.y ... int
   *   this.color ... Color
   *   this.above ... Cell
   *   this.below ... Cell
   *   this.left ... Cell
   *   this.right ... Cell
   * methods: 
   *   this.drawCell(WorldScene) ... void
   *   this.assignNeighbors(ArrayList<ArrayList<Cell>>) ... void
   *   this.addNeighbors(ArrayList<Cell>, ArrayList<Cell>) ... void
   * methods for fields: 
   *   this.above.drawCell(WorldScene) ... void
   *   this.above.assignNeighbors(ArrayList<ArrayList<Cell>>) ... void
   *   this.above.addNeighbors(ArrayList<Cell>, ArrayList<Cell>) ... void
   *   this.below.drawCell(WorldScene) ... void
   *   this.below.assignNeighbors(ArrayList<ArrayList<Cell>>) ... void
   *   this.below.addNeighbors(ArrayList<Cell>, ArrayList<Cell>) ... void
   *   this.left.drawCell(WorldScene) ... void
   *   this.left.assignNeighbors(ArrayList<ArrayList<Cell>>) ... void
   *   this.left.addNeighbors(ArrayList<Cell>, ArrayList<Cell>) ... void
   *   this.right.drawCell(WorldScene) ... void
   *   this.right.assignNeighbors(ArrayList<ArrayList<Cell>>) ... void
   *   this.right.addNeighbors(ArrayList<Cell>, ArrayList<Cell>) ... void
   */
  
  // draws a cell onto the given world scene
  // EFFECT: draws the cell onto the given world scene
  void drawCell(WorldScene ws) {
    int s = Util.TILE_SIZE;
    ws.placeImageXY(new RectangleImage(s, s, 
        OutlineMode.SOLID, this.color)
        .movePinhole(-s / 2, -s / 2), 
        this.x * s, 
        this.y * s);
  }
  
  // sets the reference for each of the cells neighbors
  // EFFECT: changes the values of above, below, left, and right
  void assignNeighbors(ArrayList<ArrayList<Cell>> board) {
    if ((this.x - 1) >= 0) {
      left = board.get(this.y).get(this.x - 1);
    }
    if ((this.x + 1) < board.size()) {
      right = board.get(this.y).get(this.x + 1);
    }
    if ((this.y - 1) >= 0) {
      above = board.get(this.y - 1).get(this.x);
    }
    if ((this.y + 1) < board.size()) {
      below = board.get(this.y + 1).get(this.x);
    }
  }
  
  // adds the neighbors of the cell if they have not already been seen and are the name color
  // EFFECT: Adds all the neighboring cell to the work list if they havn't been seen and 
  // they are the right color and then adds this cell to already seen

  void addNeighbors(ArrayList<Cell> workList, ArrayList<Cell> alreadySeen) {
    if(this.left != null && !alreadySeen.contains(this.left) && this.left.color == this.color) {
      workList.add(this.left);
    }
    if(this.right != null && !alreadySeen.contains(this.right) && this.right.color == this.color) {
      workList.add(this.right);
    }
    if(this.above != null && !alreadySeen.contains(this.above) && this.above.color == this.color) {
      workList.add(this.above);
    }
    if(this.below != null && !alreadySeen.contains(this.below) && this.below.color == this.color) {
      workList.add(this.below);
    }
    
    alreadySeen.add(workList.remove(0));
  }
  
}

// represents the game
class BridgItWorld extends World {
 
  // All the cells of the game
  ArrayList<ArrayList<Cell>> board;
  int boardSize;
  Boolean pinkTurn;
  // 0 for running, 1 for pink win, 2 for purple win
  int running = 0;
  ArrayList<Cell> workList = new ArrayList<Cell>(0);
  ArrayList<Cell> alreadySeen = new ArrayList<Cell>(0);
  
  // constructor
  BridgItWorld(int boardSize) {
    this.boardSize = boardSize;
    if ((this.boardSize % 2) == 0) {
      throw new IllegalArgumentException("Size of the board must be odd");
    }
    this.board = this.createBoard();
    this.linkCells();
    this.pinkTurn = true;
  }
  
  
  /* fields: 
   *   this.board ... ArrayList<ArrayList<Cell>>
   *   this.boardSize ... int
   *   this.pinkTurn ... boolean
   *   this.running ... boolean
   *   this.workList ... ArrayList<Cell>
   *   this.alreadySeen ... ArrayList<Cell>
   * methods: 
   *   this.createBoard() ... ArrayList<ArrayList<Cell>>
   *   this.linkCells() ... void
   *   this.makeScene() ... WorldScene
   *   this.checkWin() ... Boolean
   *   this.onMouseClicked(Posn) ... void
   *   this.lastScene(String) ... WorldScene
   * methods for fields: 
   *   
   */
  
  // creates the game board
  ArrayList<ArrayList<Cell>> createBoard() {
    ArrayList<ArrayList<Cell>> temp = new ArrayList<ArrayList<Cell>>(this.boardSize);
    for (int y = 0; y < this.boardSize; y++) {
      ArrayList<Cell> tempRow = new ArrayList<Cell>(this.boardSize);
      for (int x = 0; x < this.boardSize; x++) {
        if ((y % 2) == (x % 2)) {
          tempRow.add(x, new Cell(x, y, Color.WHITE));
        } else if ((y % 2) == 0) {
          tempRow.add(x, new Cell(x, y, Color.PINK));
        } else {
          tempRow.add(x, new Cell(x, y, Color.MAGENTA));
        }
      }
      temp.add(y, tempRow);
    }
    return temp;
  }
  
  // gives each of the cells in the board a reference to it's neighbor
  // EFFECT: changes the values of board's cells' variables
  void linkCells() {
    for (int i = 0; i < this.boardSize; i++) {
      for (int j = 0; j < this.boardSize; j++) {
        this.board.get(j).get(i).assignNeighbors(this.board);
      }
    }
  }

  // creates the game
  public WorldScene makeScene() {
    WorldScene scene = this.getEmptyScene();
    int size = this.boardSize * Util.TILE_SIZE;
    scene.placeImageXY(new RectangleImage(size, size, 
        OutlineMode.SOLID, Color.WHITE), 
        size / 2, size / 2);
    for (ArrayList<Cell> i : board) {
      for (Cell j : i) {
        j.drawCell(scene);
      }
    }
    return scene;
  }
  
  //returns true when one of the players creates a complete bridge
  // EFFECT: Changes the running variable if the game has been won and ends the world
  void checkWin(boolean checkPinkWin) {
    workList = new ArrayList<Cell>(0);
    alreadySeen = new ArrayList<Cell>(0);
    
    if(checkPinkWin) {
      for(int i = 1; i < this.boardSize-1; i+=2) {
        workList.add(this.board.get(0).get(i));
      }
    } else {
      for(int i = 1; i < this.boardSize-1; i+=2) {
        workList.add(this.board.get(i).get(0));
      }
    }
    
    while(workList.size() > 0) {
      ArrayList<Cell> tempWorkList = new ArrayList<Cell>(workList);
      for(Cell c: tempWorkList) {
        if(checkPinkWin) {
          if(c.y == this.boardSize - 1) {
            this.running = 1;
            this.endOfWorld("Pink Wins!!!");
          }
        } else {
          if(c.x == this.boardSize - 1) {
            this.running = 2;
            this.endOfWorld("Purple Wins!!!");
          }
        }
        
        c.addNeighbors(workList, alreadySeen);
      }
    } 
  }

  // handles mouse clicks
  // EFFECT: Changes the color of a cell
  public void onMouseClicked(Posn p) {
    int xPos = p.x / Util.TILE_SIZE;
    int yPos = p.y / Util.TILE_SIZE;
    if (((xPos > 0) && (xPos < boardSize - 1)) 
        && ((yPos > 0) && (yPos < boardSize - 1))) {
      Cell clickedCell = this.board.get(yPos).get(xPos);
      Color clickedColor = clickedCell.color;
      if (this.pinkTurn && (clickedColor == Color.WHITE)) {
        clickedCell.color = Color.PINK;
        this.pinkTurn = false;
        this.checkWin(true);
      } else if (!this.pinkTurn && (clickedColor == Color.WHITE)) {
        clickedCell.color = Color.MAGENTA;
        this.pinkTurn = true;
        this.checkWin(false);
      }
    }
  }
  
  // returns the end scene for the game
  public WorldScene lastScene(String s) {
    WorldScene end = this.makeScene();
    int size = this.boardSize * Util.TILE_SIZE;
    end.placeImageXY(new TextImage(s, this.boardSize * 1.5, Color.BLACK), 
        size / 2, size / 2);
    return end;
  }
  
}

class ExamplesBridgIt {
  
  BridgItWorld w = new BridgItWorld(11);
  BridgItWorld testW = new BridgItWorld(3);
  Cell pinkCell1 = new Cell(1, 0, Color.PINK);
  Cell pinkCell2 = new Cell(1, 2, Color.PINK);
  Cell purpleCell1 = new Cell(0, 1, Color.MAGENTA);
  Cell purpleCell2 = new Cell(2, 1, Color.MAGENTA);
  Cell whiteCell1 = new Cell(0, 0, Color.WHITE);
  Cell whiteCell2 = new Cell(0, 2, Color.WHITE);
  Cell whiteCell3 = new Cell(1, 1, Color.WHITE);
  Cell whiteCell4 = new Cell(2, 0, Color.WHITE);
  Cell whiteCell5 = new Cell(2, 2, Color.WHITE);
  Cell middleCell = new Cell(1, 1, Color.PINK);
  ArrayList<Cell> testRow0 = new ArrayList<Cell>(3);
  ArrayList<Cell> testRow1 = new ArrayList<Cell>(3);
  ArrayList<Cell> testRow2 = new ArrayList<Cell>(3);
  ArrayList<ArrayList<Cell>> testBoard = new ArrayList<ArrayList<Cell>>(3);
  
  // initialize the variables used in testing
  void init() {
    testW = new BridgItWorld(3);
    pinkCell1 = new Cell(1, 0, Color.PINK);
    pinkCell2 = new Cell(1, 2, Color.PINK);
    purpleCell1 = new Cell(0, 1, Color.MAGENTA);
    purpleCell2 = new Cell(2, 1, Color.MAGENTA);
    whiteCell1 = new Cell(0, 0, Color.WHITE);
    whiteCell2 = new Cell(0, 2, Color.WHITE);
    whiteCell3 = new Cell(1, 1, Color.WHITE);
    whiteCell4 = new Cell(2, 0, Color.WHITE);
    whiteCell5 = new Cell(2, 2, Color.WHITE);
    middleCell = new Cell(1, 1, Color.PINK);
    testRow0 = new ArrayList<Cell>(
        Arrays.asList(this.whiteCell1, this.pinkCell1, this.whiteCell4));
    testRow1 = new ArrayList<Cell>(
        Arrays.asList(this.purpleCell1, this.whiteCell3, this.purpleCell2));
    testRow2 = new ArrayList<Cell>(
        Arrays.asList(this.whiteCell2, this.pinkCell2, this.whiteCell5));
    testBoard = new ArrayList<ArrayList<Cell>>(
        Arrays.asList(this.testRow0, this.testRow1, this.testRow2));
  }
  
  // a class to test BridgIt
  void testBridgIt(Tester t) {
    BridgItWorld toTest = w;
    int size = toTest.board.size()
        * Util.TILE_SIZE;
    toTest.bigBang(size, size, 1);
  }
  
  // a test for drawCell in the Cell class
  void testDrawCell(Tester t) {
    // initialize the variables used
    this.init();
    WorldScene testScene = new WorldScene(60, 60);
    testScene.placeImageXY(
        new RectangleImage(60, 60, OutlineMode.SOLID, Color.WHITE), 30, 30);
    WorldScene checkScene = new WorldScene(60, 60);
    checkScene.placeImageXY(
        new RectangleImage(60, 60, OutlineMode.SOLID, Color.WHITE), 30, 30);
    // draw the cell and the expected result
    this.whiteCell1.drawCell(testScene);
    checkScene.placeImageXY(new RectangleImage(20, 20, OutlineMode.SOLID, Color.WHITE)
        .movePinhole(-10, -10), 0, 0);
    // test
    t.checkExpect(testScene, checkScene);
    // draw the cell and the expected result
    this.pinkCell1.drawCell(testScene);
    checkScene.placeImageXY(new RectangleImage(20, 20, OutlineMode.SOLID, Color.PINK) 
        .movePinhole(-10, -10), 20, 0);
    // test
    t.checkExpect(testScene, checkScene);
    // draw the cell and the expected result
    this.purpleCell1.drawCell(testScene);
    checkScene.placeImageXY(new RectangleImage(20, 20, OutlineMode.SOLID, Color.MAGENTA)
        .movePinhole(-10, -10), 0, 20);
    // test
    t.checkExpect(testScene, checkScene);
  }

  // a test for assignNeighbors in the Cell class
  void testAssignNeighbors(Tester t) {
    this.init();
    t.checkExpect(this.whiteCell3.above, null);
    t.checkExpect(this.whiteCell3.below, null);
    t.checkExpect(this.whiteCell3.left, null);
    t.checkExpect(this.whiteCell3.right, null);
    whiteCell3.assignNeighbors(this.testBoard);
    t.checkExpect(whiteCell3.above, this.pinkCell1);
    t.checkExpect(whiteCell3.below, this.pinkCell2);
    t.checkExpect(whiteCell3.left, this.purpleCell1);
    t.checkExpect(whiteCell3.right, this.purpleCell2);
  }
  
  // a test that the constructor errors out when given an even number
  void testBridgItConstruct(Tester t) {
    this.init();
    t.checkConstructorException(
        new IllegalArgumentException("Size of the board must be odd"), 
        "BridgItWorld", 2);
    t.checkConstructorException(
        new IllegalArgumentException("Size of the board must be odd"), 
        "BridgItWorld", 220);
    t.checkConstructorNoException("test", "BridgItWorld", 3);
    t.checkConstructorNoException("test", "BridgItWorld", 317);
  }
  
  // a test for createBoard in the BridgItWorld class
  void testCreateBoard(Tester t) {
    this.init();
    for (ArrayList<Cell> y : this.testBoard) {
      for (Cell x : y) {
        x.assignNeighbors(this.testBoard);
      }
    }
    // because createBoard is ran in the constructor, 
    // the class is checked by checking that the created board
    // is done the correct way
    t.checkExpect(this.testW.board, this.testBoard);
  }
  
  // a test for linkCells in the BridgItWorld class
  void testLinkCells(Tester t) {
    this.init();
    // because linkCells is ran in the constructor, 
    // the class is checked by checking that some of the cells
    // have the correct neighbors
    t.checkExpect(this.testW.board.get(0).get(0).above, null);
    t.checkExpect(this.testW.board.get(1).get(1).below, 
        this.testW.board.get(2).get(1));
    t.checkExpect(this.testW.board.get(2).get(2).left, 
        this.testW.board.get(2).get(1));
    t.checkExpect(this.testW.board.get(0).get(2).right, null);
  }

  // a test for makeScene in the BridgItWorld class
  void testMakeScene(Tester t) {
    this.init();
    WorldScene testScene = new WorldScene(0, 0);
    testScene.placeImageXY(
        new RectangleImage(60, 60, OutlineMode.SOLID, Color.WHITE), 30, 30);
    testScene.placeImageXY(
        new RectangleImage(20, 20, OutlineMode.SOLID, Color.WHITE)
        .movePinhole(-10, -10), 0, 0);
    testScene.placeImageXY(
        new RectangleImage(20, 20, OutlineMode.SOLID, Color.PINK)
        .movePinhole(-10, -10), 20, 0);
    testScene.placeImageXY(
        new RectangleImage(20, 20, OutlineMode.SOLID, Color.WHITE)
        .movePinhole(-10, -10), 40, 0);
    testScene.placeImageXY(
        new RectangleImage(20, 20, OutlineMode.SOLID, Color.MAGENTA)
        .movePinhole(-10, -10), 0, 20);
    testScene.placeImageXY(
        new RectangleImage(20, 20, OutlineMode.SOLID, Color.WHITE)
        .movePinhole(-10, -10), 20, 20);
    testScene.placeImageXY(
        new RectangleImage(20, 20, OutlineMode.SOLID, Color.MAGENTA)
        .movePinhole(-10, -10), 40, 20);
    testScene.placeImageXY(
        new RectangleImage(20, 20, OutlineMode.SOLID, Color.WHITE)
        .movePinhole(-10, -10), 0, 40);
    testScene.placeImageXY(
        new RectangleImage(20, 20, OutlineMode.SOLID, Color.PINK)
        .movePinhole(-10, -10), 20, 40);
    testScene.placeImageXY(
        new RectangleImage(20, 20, OutlineMode.SOLID, Color.WHITE)
        .movePinhole(-10, -10), 40, 40);
    t.checkExpect(this.testW.makeScene(), testScene);
  }
  
  // a test for checkWin in the BridgItWorld class
  void testCheckWin(Tester t) {
    init();
    this.testW.checkWin(true);
    t.checkExpect(this.testW.running, 0);
    this.testW.checkWin(false);
    t.checkExpect(this.testW.running, 0);
    this.testW.onMouseClicked(new Posn(30, 30));
    this.testW.checkWin(true);
    t.checkExpect(this.testW.running, 1);
    init();
    this.testW.checkWin(true);
    t.checkExpect(this.testW.running, 0);
    this.testW.checkWin(false);
    t.checkExpect(this.testW.running, 0);
    this.testW.pinkTurn = false;
    this.testW.onMouseClicked(new Posn(30, 30));
    this.testW.checkWin(false);
    t.checkExpect(this.testW.running, 2);
    
  }
  
  // a test for onMouseClicked in the BridgItWorld class
  void testOnMouseClicked(Tester t) {
    this.init();
    t.checkExpect(this.testW.board.get(1).get(0).color, Color.MAGENTA);
    this.testW.onMouseClicked(new Posn(10, 30));
    t.checkExpect(this.testW.board.get(1).get(0).color, Color.MAGENTA);
    t.checkExpect(this.testW.board.get(1).get(1).color, Color.WHITE);
    this.testW.onMouseClicked(new Posn(30, 30));
    t.checkExpect(this.testW.board.get(1).get(1).color, Color.PINK);
    this.testW.onMouseClicked(new Posn(30, 30));
    t.checkExpect(this.testW.board.get(1).get(1).color, Color.PINK);
  }
  
  // a test for lastScene in the BridgItWorld class
  void testLastScene(Tester t) {
    this.init();
    WorldScene testScene = new WorldScene(0, 0);
    testScene.placeImageXY(
        new RectangleImage(60, 60, OutlineMode.SOLID, Color.WHITE), 30, 30);
    testScene.placeImageXY(
        new RectangleImage(20, 20, OutlineMode.SOLID, Color.WHITE)
        .movePinhole(-10, -10), 0, 0);
    testScene.placeImageXY(
        new RectangleImage(20, 20, OutlineMode.SOLID, Color.PINK)
        .movePinhole(-10, -10), 20, 0);
    testScene.placeImageXY(
        new RectangleImage(20, 20, OutlineMode.SOLID, Color.WHITE)
        .movePinhole(-10, -10), 40, 0);
    testScene.placeImageXY(
        new RectangleImage(20, 20, OutlineMode.SOLID, Color.MAGENTA)
        .movePinhole(-10, -10), 0, 20);
    testScene.placeImageXY(
        new RectangleImage(20, 20, OutlineMode.SOLID, Color.WHITE)
        .movePinhole(-10, -10), 20, 20);
    testScene.placeImageXY(
        new RectangleImage(20, 20, OutlineMode.SOLID, Color.MAGENTA)
        .movePinhole(-10, -10), 40, 20);
    testScene.placeImageXY(
        new RectangleImage(20, 20, OutlineMode.SOLID, Color.WHITE)
        .movePinhole(-10, -10), 0, 40);
    testScene.placeImageXY(
        new RectangleImage(20, 20, OutlineMode.SOLID, Color.PINK)
        .movePinhole(-10, -10), 20, 40);
    testScene.placeImageXY(
        new RectangleImage(20, 20, OutlineMode.SOLID, Color.WHITE)
        .movePinhole(-10, -10), 40, 40);
    testScene.placeImageXY(
        new TextImage("Player One Has Won!", 4.5, Color.BLACK), 30, 30);
    t.checkExpect(this.testW.lastScene("Player One Has Won!"), testScene);
  }
 
  // a test for the addNeighbors in the Cell class
  void testAddNeighbors(Tester t) {
    init();
    Cell testCell = this.testW.board.get(0).get(1);
    t.checkExpect(this.testW.workList, new ArrayList<Cell>(0));
    t.checkExpect(this.testW.alreadySeen, new ArrayList<Cell>(0));
    this.testW.workList.add(testCell);
    testCell.addNeighbors(this.testW.workList, this.testW.alreadySeen);
    t.checkExpect(this.testW.workList, new ArrayList<Cell>(0));
    t.checkExpect(this.testW.alreadySeen.size(), 1);
    this.testW.board.get(1).get(1).color = Color.PINK;
    this.testW.workList.add(testCell);
    testCell.addNeighbors(this.testW.workList, this.testW.alreadySeen);
    t.checkExpect(this.testW.workList.size(), 1);
    t.checkExpect(this.testW.alreadySeen.size(), 2);
  }
}
