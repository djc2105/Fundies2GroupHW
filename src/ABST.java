
import tester.Tester;
import java.util.Comparator;

// an abstract class to represent a binary tree
abstract class ABST<T> {
  
  // variables
  Comparator<T> order;
  
  // constructor
  ABST(Comparator<T> order) {
    this.order = order;
  }
  
  // insert an item into the correct place in the BST
  abstract ABST<T> insert(T item);
  
  // check if the given item is currently in the BST
  abstract boolean present(T item);
  
  // returns the leftmost item in the BST
  abstract T getLeftMost();
  
  // returns the BST without the leftmost item
  abstract ABST<T> getRight();
  
  // checks if this BST is the same as the given BST
  abstract boolean sameTree(ABST<T> other);
  
  // checks if this BST has the same data as the given BST
  abstract boolean sameData(ABST<T> other);
  
}

// an element of a binary tree with no data
class Leaf<T> extends ABST<T> {
  
  // constructor
  Leaf(Comparator<T> order) {
    super(order);
  }
  
  /* fields: 
   *   super.order ... Comparator<T>
   * methods: 
   *   
   * methods for fields: 
   * 
   */
  
  @Override
  ABST<T> insert(T item) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  boolean present(T item) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  T getLeftMost() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  ABST<T> getRight() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  boolean sameTree(ABST<T> other) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  boolean sameData(ABST<T> other) {
    // TODO Auto-generated method stub
    return false;
  }
  
}

// an element of a binary tree with data
class Node<T> extends ABST<T> {
  
  // variables
  T data;
  ABST<T> left;
  ABST<T> right;
  
  // constructor
  Node(Comparator<T> order, T data, ABST<T> left, ABST<T> right) {
    super(order);
    this.data = data;
    this.left = left;
    this.right = right;
  }
  
  /* fields: 
   *   super.order ... Comparator<T>
   *   this.data ... T
   *   this.left ... ABST<T>
   *   this.right ... ABST<T>
   * methods: 
   *   
   * methods for fields: 
   * 
   */

  @Override
  ABST<T> insert(T item) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  boolean present(T item) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  T getLeftMost() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  ABST<T> getRight() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  boolean sameTree(ABST<T> other) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  boolean sameData(ABST<T> other) {
    // TODO Auto-generated method stub
    return false;
  }
  
}

class Book {
  
  // variables
  String title;
  String author;
  int price;
  
  // constructor
  Book(String title, String author, int price) {
    this.title = title;
    this.author = author;
    this.price = price;
  }
  
  /* fields: 
   *   this.title ... String
   *   this.author ... String
   *   this.price ... int
   * methods: 
   * 
   * methods for fields: 
   * 
   */
  
}

// comparator object to compare the titles of books
class BooksByTitle implements Comparator<Book> {
  
  // returns a posInt if the title of t1 comes after t2 alphabetically
  public int compare(Book t1, Book t2) {
    return t1.title.compareTo(t2.title);
  }
  
}

// comparator object to compare the authors of books
class BooksByAuthor implements Comparator<Book> {
  
  // returns a posInt if the author of t1 comes after t2 alphabetically
  public int compare(Book t1, Book t2) {
    return t1.author.compareTo(t2.author);
  }
  
}

// comparator object to compare the price of books
class BooksByPrice implements Comparator<Book> {
  
  // returns a posInt if the price of t1 is greater than that of t2
  public int compare(Book t1, Book t2) {
    return t1.price - t2.price;
  }
  
}

class ExamplesABST {
  
  // example books
  Book wutheringHeights = new Book("Wuthering Heights", "Emily Bronte", 10);
  Book janeEyre = new Book("Jane Eyre", "Charlotte Bronte", 5);
  Book agnesGrey = new Book("Agnes Grey", "Anne Bronte", 4);
  Book prideAndPrejudice = new Book("Pride and Prejudice", "Jane Austen", 12);
  
  // example function objects
  BooksByTitle bbt = new BooksByTitle();
  BooksByAuthor bba = new BooksByAuthor();
  BooksByPrice bbp = new BooksByPrice();
  
  // valid BST sorting books by Title
  ABST<Book> bbtLeaf = new Leaf<Book>(bbt);
  ABST<Book> bbtNode1 = new Node<Book>(bbt, this.agnesGrey, this.bbtLeaf, this.bbtLeaf);
  ABST<Book> bbtNode2 = new Node<Book>(bbt, this.janeEyre, this.bbtNode1, this.bbtLeaf);
  ABST<Book> bbtNode3 = new Node<Book>(bbt, this.wutheringHeights, this.bbtLeaf, this.bbtLeaf);
  ABST<Book> bbtTree = new Node<Book>(bbt, this.prideAndPrejudice, this.bbtNode2, this.bbtNode3);
  
  // valid BST sorting books by Author
  ABST<Book> bbaLeaf = new Leaf<Book>(bba);
  ABST<Book> bbaNode1 = new Node<Book>(bba, this.agnesGrey, this.bbaLeaf, this.bbaLeaf);
  ABST<Book> bbaNode2 = new Node<Book>(bba, this.wutheringHeights, this.bbaLeaf, this.bbaLeaf);
  ABST<Book> bbaNode3 = new Node<Book>(bba, this.prideAndPrejudice, this.bbaNode2, this.bbaLeaf);
  ABST<Book> bbaTree = new Node<Book>(bba, this.janeEyre, this.bbaNode1, this.bbaNode3);
  
  // valid BST sorting books by Price
  ABST<Book> bbpLeaf = new Leaf<Book>(bbp);
  ABST<Book> bbpNode1 = new Node<Book>(bbp, this.agnesGrey, this.bbpLeaf, this.bbpLeaf);
  ABST<Book> bbpNode2 = new Node<Book>(bbp, this.wutheringHeights, this.bbpLeaf, this.bbpLeaf);
  ABST<Book> bbpNode3 = new Node<Book>(bbp, this.janeEyre, this.bbpNode1, this.bbpNode2);
  ABST<Book> bbpTree = new Node<Book>(bbp, this.prideAndPrejudice, this.bbpNode3, this.bbpLeaf);
  
  
  
}


























