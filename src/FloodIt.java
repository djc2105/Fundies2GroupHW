
import java.util.ArrayList;
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
  // the four adjacent cells to this one
  Cell left;
  Cell top;
  Cell right;
  Cell bottom;
  
}


class FloodItWorld extends World {
  
  // size of the board
  int BOARD_SIZE = 22;
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
    ArrayList<ArrayList<Cell>> temp = 
        new ArrayList<ArrayList<Cell>>(BOARD_SIZE);
    for (int i = BOARD_SIZE; )
  }
  
}





















