
import java.util.function.*;
import tester.Tester;

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
   *   this.size() ... int
   *   this.addAtHead(T data) ... void
   *   this.addAtTail(T data) ... void
   *   this.removeFromHead() ... T
   *   this.removeFromTail() ... T
   *   this.find(Predicate<T>) ... ANode<T>
   *   this.removeNode(ANode<T>) ... void
   * methods for fields: 
   * 
   */
  
  int size() {
    return 0;
  }
  
  void addAtHead(T data) {
    this.header.next = new Node<T>(data, this.header.next, this.header);
  }
  
  void addAtTail(T data) {
    this.header.prev = new Node<T>(data, this.header, this.header.prev);
  }
  
  T removeFromHead() {
    return null;
  }
  
  T removeFromTail() {
    return null;
  }
  
  ANode<T> find(Predicate<T> pred) {
    return null;
  }
  
  void removeFromList(ANode<T> node) {
    
  }
  
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
  Node(T data, ANode<T> next, ANode<T> prev) {
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
  Deque<String> dequeAddAtHead = new Deque<String>();
  Sentinel<String> sent1 = new Sentinel<String>();
  ANode<String> node1 = new Node<String>("abc");
  ANode<String> node2 = new Node<String>("bcd");
  ANode<String> node3 = new Node<String>("cde");
  ANode<String> node4 = new Node<String>("def");
  ANode<String> nodeAddAtHead = new Node<String>("hi");
  
  
  Sentinel<String> sent2 = new Sentinel<String>();
  ANode<String> node5 = new Node<String>("the");
  ANode<String> node6 = new Node<String>("internets");
  ANode<String> node7 = new Node<String>("busiest");
  ANode<String> node8 = new Node<String>("music");
  ANode<String> node9 = new Node<String>("nerd");
  Deque<String> deque2 = new Deque<String>();
  
  void initData() {
    deque1 = new Deque<String>();
    sent1 = this.deque1.header;
    node1 = new Node<String>("abc", this.sent1, this.sent1);
    node2 = new Node<String>("bcd", this.sent1, this.node1);
    node3 = new Node<String>("cde", this.sent1, this.node2);
    node4 = new Node<String>("def", this.sent1, this.node3);
    nodeAddAtHead = new Node<String>("hi", this.sent1, this.node4);
    
    sent2 = new Sentinel<String>();
    node5 = new Node<String>("the", this.sent2, this.sent2);
    node6 = new Node<String>("internets", this.sent2, this.node5);
    node7 = new Node<String>("busiest", this.sent2, this.node6);
    node8 = new Node<String>("music", this.sent2, this.node7);
    node9 = new Node<String>("nerd", this.sent2, this.node8);
    deque2 = new Deque<String>(this.sent2);
  }
  
  void testDeque(Tester t) {
    this.initData();
    t.checkExpect(this.node2.prev, this.node1);
    t.checkExpect(this.node2.next, this.node3);
    t.checkExpect(this.node4.next, this.deque1.header);
    t.checkExpect(this.deque2.header, this.sent2);
    t.checkExpect(this.deque2.header.next, this.node5);
  }
  
  void testAddAtHead(Tester t) {
    this.initData();
    t.checkExpect(this.deque1.header.next, node1);
    this.deque1.addAtHead("hi");
    t.checkExpect(this.deque1, )
    
  }
  
}

















