
import tester.Tester;
import java.util.Comparator;

// an interface to represent a generic list
interface IList<T> {}

// a class to represent an empty element in a generic list
class MtList<T> implements IList<T> {}

// a class to represent a non-empty generic list
class ConsList<T> implements IList<T> {
  
  // variables
  T first;
  IList<T> rest;
  
  // constructor
  ConsList (T first, IList<T> rest) {
    this.first = first;
    this.rest = rest;
  }
  
}

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
  
  //returns the most left item in this BST
   abstract T getLeftMost(T item);
  
  // returns the BST without the leftmost item
  abstract ABST<T> getRight();
  
  // checks if this BST is the same as the given BST
  abstract boolean sameTree(ABST<T> other);
  
  // checks if this BST has the same data as the given BST
  abstract boolean sameData(ABST<T> other);
  
  //creates a list in the same order as this tree
  abstract IList<T> buildList();
  
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
   *   this.insert(T) ... ABST<T>
   *   this.present(T) ... boolean
   *   this.getLeftMost() ... T
   *   this.getLeftMost(T) ... T
   *   this.getRight() ...  ABST<T>
   *   this.sameTree(ABST<T>) ... boolean
   *   this.sameData(ABST<T>) ... boolean
   *   this.buildList() ... IList<T>
   * methods for fields: 
   *   super.order.compare(T, T) ... int
   */
  
  ABST<T> insert(T item) {
    return new Node<T>(super.order, item, 
        new Leaf<T>(super.order), 
        new Leaf<T>(super.order));
  }

  // Return false because nothing is present in a leaf
  boolean present(T item) {
    return false;
  }

  // Returns an error
  T getLeftMost() {
    throw new RuntimeException("No leftmost item of an empty tree");
  }

  // Returns the item because it is the most left
  T getLeftMost(T item) {
    return item;
  }
  
  // returns everything but the leftmost element of the leaf
  ABST<T> getRight() {
    throw new RuntimeException("No right of an empty tree");
  }

  //checks if the leaf is the same as the one given
  boolean sameTree(ABST<T> other) {
    // see the Leaf<T> and Node<T> classes for the possible templates of ABST<T>
    return false;
  }

  // checks if the data in the given tree is the same as in this leaf
  boolean sameData(ABST<T> other) {
    // see the Leaf<T> and Node<T> classes for the possible templates of ABST<T>
    return false;
  }
  
  // creates a list in the same order as this leaf
  IList<T> buildList() {
    return new MtList<T>();
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
   *   this.insert(T) ... ABST<T>
   *   this.present(T) ... boolean
   *   this.getLeftMost() ... T
   *   this.getLeftMost(T) ... T
   *   this.getRight() ...  ABST<T>
   *   this.sameTree(ABST<T>) ... boolean
   *   this.sameData(ABST<T>) ... boolean
   *   this.buildList() ... IList<T>
   * methods for fields: 
   *   super.order.compare(T, T) ... int
   *   this.left.insert(T) ... ABST<T>
   *   this.left.present(T) ... boolean
   *   this.left.getLeftMost() ... T
   *   this.left.getLeftMost(T) ... T
   *   this.left.getRight() ...  ABST<T>
   *   this.left.sameTree(ABST<T>) ... boolean
   *   this.left.sameData(ABST<T>) ... boolean
   *   this.left.buildList() ... IList<T>
   *   this.right.insert(T) ... ABST<T>
   *   this.right.present(T) ... boolean
   *   this.right.getLeftMost() ... T
   *   this.right.getLeftMost(T) ... T
   *   this.right.getRight() ...  ABST<T>
   *   this.right.sameTree(ABST<T>) ... boolean
   *   this.right.sameData(ABST<T>) ... boolean
   *   this.right.buildList() ... IList<T>
   */

  // Inserts item into its comparative position and returns the new ABST<T>
  ABST<T> insert(T item) {
    if(super.order.compare(item, this.data) < 0) {
      return new Node<T>(super.order, this.data, this.left.insert(item), this.right);
    } else if (super.order.compare(item, this.data) > 0) {
      return new Node<T>(super.order, this.data, this.left, this.right.insert(item));
    } else {
      return new Node<T>(super.order, this.data, this.left, 
          new Node<T>(super.order, item, 
              new Leaf<T>(super.order), 
              this.right));
    }
  }

  // 
  boolean present(T item) {
    if (super.order.compare(this.data, item) == 0) {
      return true;
    } else if (super.order.compare(this.data, item) > 0) {
      return this.left.present(item);
    } else {
      return this.right.present(item);
    }
  }

  // Returns the most left item in this node
  T getLeftMost() {
    return this.left.getLeftMost(this.data);
  }
  
  // Returns the most left item in this node
  T getLeftMost(T item) {
    return this.left.getLeftMost(this.data);
  }

  // returns every element of the tree except for the leftmost item
  ABST<T> getRight() {
    if (super.order.compare(this.data, this.getLeftMost()) == 0) {
      return this.right;
    } else {
      return new Node<T>(this.order, this.data, this.left.getRight(), this.right);
    }
  }

  // checks if this tree is the same as the one given
  boolean sameTree(ABST<T> other) {
    // see the Leaf<T> and Node<T> classes for the possible templates of ABST<T>
    return false;
  }

  // checks if this tree holds all the same elements as the given tree
  boolean sameData(ABST<T> other) {
    // see the Leaf<T> and Node<T> classes for the possible templates of ABST<T>
    return (super.order.compare(this.getLeftMost(), other.getLeftMost()) == 0)
        && (this.getRight().sameData(other)); // FIX THIS!!!!!!! IT WILL ERROR OUT
  }
  
  // creates a list in the same order as this non-empty tree
  IList<T> buildList() {
    return new ConsList<T>(this.getLeftMost(), 
        this.getRight().buildList());
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
    // see the Book class for the template for t1 and t2
    return t1.title.compareTo(t2.title);
  }
  
}

// comparator object to compare the authors of books
class BooksByAuthor implements Comparator<Book> {
  
  // returns a posInt if the author of t1 comes after t2 alphabetically
  public int compare(Book t1, Book t2) {
    // see the Book class for the template for t1 and t2
    return t1.author.compareTo(t2.author);
  }
  
}

// comparator object to compare the price of books
class BooksByPrice implements Comparator<Book> {
  
  // returns a posInt if the price of t1 is greater than that of t2
  public int compare(Book t1, Book t2) {
    // see the Book class for the template for t1 and t2
    return t1.price - t2.price;
  }
  
}

class ExamplesABST {
  
  // example books
  Book wutheringHeights = new Book("Wuthering Heights", "Emily Bronte", 10);
  Book janeEyre = new Book("Jane Eyre", "Charlotte Bronte", 5);
  Book agnesGrey = new Book("Agnes Grey", "Anne Bronte", 4);
  Book prideAndPrejudice = new Book("Pride and Prejudice", "Jane Austen", 12);
  Book l984 = new Book("1984", "George Orwell", 10);
  Book dune = new Book("Dune", "Frank Herbert", 9);
  
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
  
  // list of books in title order
  IList<Book> mtBook = new MtList<Book>();
  IList<Book> consBook1 = new ConsList<Book>(this.wutheringHeights, this.mtBook);
  IList<Book> consBook2 = new ConsList<Book>(this.prideAndPrejudice, this.consBook1);
  IList<Book> consBook3 = new ConsList<Book>(this.janeEyre, this.consBook2);
  IList<Book> consBook4 = new ConsList<Book>(this.agnesGrey, this.consBook3);
  
  // list of books in author & price order
  IList<Book> consBook5 = new ConsList<Book>(this.prideAndPrejudice, this.mtBook);
  IList<Book> consBook6 = new ConsList<Book>(this.wutheringHeights, this.consBook5);
  IList<Book> consBook7 = new ConsList<Book>(this.janeEyre, this.consBook6);
  IList<Book> consBook8 = new ConsList<Book>(this.agnesGrey, this.consBook7);

  // A method to test the Insert Method
  boolean testInsert(Tester t) {
    return t.checkExpect(bbtTree.insert(l984), new Node<Book>(bbt, 
        prideAndPrejudice, 
        new Node<Book>(bbt, 
            janeEyre, 
            new Node<Book>(bbt, 
                agnesGrey, 
                new Node<Book>(bbt, 
                    l984, 
                    this.bbtLeaf, 
                    this.bbtLeaf), 
                this.bbtLeaf), 
            this.bbtLeaf), 
        new Node<Book>(bbt, 
            wutheringHeights, 
            this.bbtLeaf,
            this.bbtLeaf)))
        && t.checkExpect(bbaTree.insert(l984), new Node<Book>(bba, 
            janeEyre, 
            new Node<Book>(bba, 
                agnesGrey, 
                this.bbaLeaf, 
                this.bbaLeaf), 
            new Node<Book>(bba, 
                prideAndPrejudice, 
                new Node<Book>(bba, 
                    wutheringHeights, 
                    this.bbaLeaf, 
                    new Node<Book>(bba, 
                        l984, 
                        this.bbaLeaf, 
                        this.bbaLeaf)),
                this.bbaLeaf)))
        && t.checkExpect(bbpTree.insert(l984), new Node<Book>(bbp, 
            prideAndPrejudice, 
            new Node<Book>(bbp, 
                janeEyre, 
                new Node<Book>(bbp, 
                    agnesGrey, 
                    this.bbpLeaf, 
                    this.bbpLeaf), 
                new Node<Book>(bbp, 
                    wutheringHeights, 
                    this.bbpLeaf, 
                    new Node<Book>(bbp, 
                        l984, 
                        this.bbpLeaf, 
                        this.bbpLeaf))), 
            this.bbpLeaf));
  }
  
  // A method to test the Present method
  boolean testPresent(Tester t) {
    return t.checkExpect(bbtTree.present(agnesGrey), true) 
        && t.checkExpect(bbtTree.present(l984), false)
        && t.checkExpect(bbaTree.present(agnesGrey), true)
        && t.checkExpect(bbaTree.present(l984), false)
        && t.checkExpect(bbpTree.present(agnesGrey), true)
        && t.checkExpect(bbpTree.present(l984), true)
        && t.checkExpect(bbpTree.present(dune), false);
  }
  
  // A method to test the getLeftMost method
  boolean testGetLeftMost(Tester t) {
    return t.checkExpect(bbtTree.getLeftMost(), agnesGrey)
        && t.checkExpect(bbaTree.getLeftMost(), agnesGrey)
        && t.checkExpect(bbpTree.getLeftMost(), agnesGrey)
        && t.checkException(new RuntimeException("No leftmost item of an empty tree"), 
            bbaLeaf, "getLeftMost");
  }
  
  // test for getRight
  boolean testGetRight(Tester t) {
    return t.checkExpect(this.bbtTree.getRight(), 
        new Node<Book>(this.bbt, this.prideAndPrejudice, 
            new Node<Book>(this.bbt, this.janeEyre, this.bbtLeaf, this.bbtLeaf), 
            new Node<Book>(this.bbt, this.wutheringHeights, this.bbtLeaf, this.bbtLeaf)))
        && t.checkExpect(this.bbaNode1.getRight(), this.bbaLeaf)
        && t.checkExpect(this.bbpNode3.getRight(), 
            new Node<Book>(this.bbp, this.janeEyre, this.bbpLeaf, 
                new Node<Book>(this.bbp, this.wutheringHeights, this.bbpLeaf, this.bbpLeaf)))
        && t.checkException(new RuntimeException("No right of an empty tree"), 
            this.bbpLeaf, "getRight");
  }
  
  // test for sameTree
  boolean testSameTree(Tester t) {
    return t.checkExpect(this.bbtTree.sameTree(this.bbtTree), true)
        && t.checkExpect(this.bbaTree.sameTree(this.bbaNode3), false)
        && t.checkExpect(this.bbpLeaf.sameTree(this.bbpLeaf), true)
        && t.checkExpect(this.bbaLeaf.sameTree(this.bbaTree), false)
        && t.checkExpect(this.bbtTree.sameTree(this.bbpLeaf), false)
        && t.checkExpect(this.bbaLeaf.sameTree(this.bbpLeaf), true)
        && t.checkExpect(this.bbpTree.sameTree(this.bbtTree), false);
  }
  
  // test for sameData
  boolean testSameData(Tester t) {
    return t.checkExpect(this.bbtTree.sameData(this.bbtTree), true)
        && t.checkExpect(this.bbaTree.sameData(this.bbaNode3), false)
        && t.checkExpect(this.bbpLeaf.sameData(this.bbpLeaf), true)
        && t.checkExpect(this.bbaLeaf.sameData(this.bbaTree), false)
        && t.checkExpect(this.bbtTree.sameData(this.bbpLeaf), false)
        && t.checkExpect(this.bbaLeaf.sameData(this.bbpLeaf), true)
        && t.checkExpect(this.bbpTree.sameData(this.bbtTree), true)
        && t.checkExpect(this.bbpNode1.sameData(this.bbtNode1), true);
  }
  
  // test for buildList
  boolean testBuildList(Tester t) {
    return t.checkExpect(this.bbtTree.buildList(), this.consBook4)
        && t.checkExpect(this.bbaTree.buildList(), this.consBook8)
        && t.checkExpect(this.bbpLeaf.buildList(), this.mtBook);
  }
  
}


























