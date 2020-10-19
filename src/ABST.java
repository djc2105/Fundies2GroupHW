
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
