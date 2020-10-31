
// represents a doubly linked list
class Deque<T> {
  
  // variables
  Sentinel<T> header;
  
  // constructors
  Deque() {
    this.header = new Sentinel<T>();
  }
  
  Deque(Sentinel<T> header) {
    this.header = header;
  }
  
  /* fields: 
   *   this.header ... Sentinel<T>
   * methods: 
   * 
   * methods for fields: 
   * 
   */
  
}

// an abstract class to represent a node in Deque
abstract class ANode<T> {
  
  // variables
  ANode<T> next;
  ANode<T> prev;
  
}

// represents the starter for a Deque
class Sentinel<T> extends ANode<T>{
  
  // constructor
  Sentinel() {
    this.next = this;
    this.prev = this;
  }
  
  /* fields: 
   *   this.next ... ANode<T>
   *   this.prev ... ANode<T>
   * methods: 
   * 
   * methods for fields: 
   * 
   */
  
}

// represents an element of the Deque
class Node<T> extends ANode<T>{
  
  // variables
  T data;
  
  // constructors
  // takes a T value and initializes next and prev as null
  Node(T data) {
    this.next = null;
    this.prev = null;
    this.data = data;
  }
  
  // takes two nodes and a T value
  // changes the previous nodes of the given nodes
  Node(ANode<T> next, ANode<T> prev, T data) {
    this.next = next;
    if (next == null) {
      throw new IllegalArgumentException("Cannot take a null ANode");
    } else {
      next.prev = this;
    }
    this.prev = prev;
    if (prev == null) {
      throw new IllegalArgumentException("Cannot take a null ANode");
    } else {
      prev.next = this;
    }
    this.data = data;
  }
  
  /* fields: 
   *   this.next ... ANode<T>
   *   this.prev ... ANode<T>
   *   this.data ... T
   * methods: 
   * 
   * methods for fields: 
   * 
   */
  
}

class ExamplesDeque {
  
  // example variables
  Deque<String> deque1 = new Deque<String>();
  
  Deque<String> deque2 = new Deque<String>();
  ANode<String> node1 = new Node<String>("abc");
  ANode<String> node2 = new Node<String>("bcd");
  ANode<String> node3 = new Node<String>("cde");
  ANode<String> node4 = new Node<String>("def");
  
  
  Deque<String> deque3 = new Deque<String>();
  ANode<String> node5 = new Node<String>("the");
  ANode<String> node6 = new Node<String>("internets");
  ANode<String> node7 = new Node<String>("busiest");
  ANode<String> node8 = new Node<String>("music");
  ANode<String> node9 = new Node<String>("nerd");
  
  
}

















