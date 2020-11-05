
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
  
  // Returns the size of the deque
  int size() {
    return this.header.next.sizeHelper(0);
  }
  
  void addAtHead(T data) {
    this.header.next = new Node<T>(data, this.header.next, this.header);
  }
  
  void addAtTail(T data) {
    this.header.prev = new Node<T>(data, this.header, this.header.prev);
  }
  
  ANode<T> removeFromHead() {
    return this.header.next.removeH();
  }
  
  ANode<T> removeFromTail() {
    return this.header.prev.removeH();
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
  
  // Returns the int once it finds the sentenial is the node
  abstract int sizeHelper(int i);

  // Returns the removed item and removes the head node
  abstract ANode<T> removeH();
  
}

// represents the starter for a Deque
class Sentinel<T> extends ANode<T>{
  
  // constructor
  Sentinel() {
    this.next = this;
    this.prev = this;
  }

  // Returns the int because it has looped
  int sizeHelper(int i) {
    return i;
  }

  // Throws a runtime error
  ANode<T> removeH() {
    throw new RuntimeException("Cannot remove from an empty list");
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

  // Adds one to the int and then calls sizeHelper on next
  int sizeHelper(int i) {
    return this.next.sizeHelper(i + 1);
  }

  // Returns the removed item and removes the head node
  ANode<T> removeH() {
    ANode<T> nextNode = this.next;
    ANode<T> prevNode = this.prev;
    
    nextNode.prev = prevNode;
    prevNode.next = nextNode;
    
    return this;
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
  Sentinel<String> sent1 = new Sentinel<String>();
  ANode<String> node1 = new Node<String>("abc");
  ANode<String> node2 = new Node<String>("bcd");
  ANode<String> node3 = new Node<String>("cde");
  ANode<String> node4 = new Node<String>("def");
  
  
  Sentinel<String> sent2 = new Sentinel<String>();
  ANode<String> node5 = new Node<String>("the");
  ANode<String> node6 = new Node<String>("internets");
  ANode<String> node7 = new Node<String>("busiest");
  ANode<String> node8 = new Node<String>("music");
  ANode<String> node9 = new Node<String>("nerd");
  Deque<String> deque2 = new Deque<String>();
  
  Sentinel<String> sent3 = new Sentinel<String>();
  Deque<String> deque3 = new Deque<String>();
  
  void initData() {
    deque1 = new Deque<String>();
    sent1 = this.deque1.header;
    node1 = new Node<String>("abc", this.sent1, this.sent1);
    node2 = new Node<String>("bcd", this.sent1, this.node1);
    node3 = new Node<String>("cde", this.sent1, this.node2);
    node4 = new Node<String>("def", this.sent1, this.node3);
    
    sent2 = new Sentinel<String>();
    node5 = new Node<String>("the", this.sent2, this.sent2);
    node6 = new Node<String>("internets", this.sent2, this.node5);
    node7 = new Node<String>("busiest", this.sent2, this.node6);
    node8 = new Node<String>("music", this.sent2, this.node7);
    node9 = new Node<String>("nerd", this.sent2, this.node8);
    deque2 = new Deque<String>(this.sent2);
    
    sent3 = new Sentinel<String>();
    deque3 = new Deque<String>(this.sent3);
  }
  
  // Method to test size
  void testSize(Tester t) {
    this.initData();
    t.checkExpect(deque1.size(), 4);
    t.checkExpect(deque2.size(), 5);
  }
  
  // Method to test sizeHelper
  void testSizeHelper(Tester t) {
    this.initData();
    t.checkExpect(sent1.sizeHelper(0), 0);
    t.checkExpect(node9.sizeHelper(2), 3);
  }
  
  void testDeque(Tester t) {
    this.initData();
    t.checkExpect(this.node2.prev, this.node1);
    t.checkExpect(this.node2.next, this.node3);
    t.checkExpect(this.node4.next, this.deque1.header);
    t.checkExpect(this.deque2.header, this.sent2);
    t.checkExpect(this.deque2.header.next, this.node5);
  }
  
  // Method to test removeFromHead
  void testRemoveFromHead(Tester t) {
    this.initData();
    t.checkExpect(deque1.removeFromHead(), node1);
    t.checkExpect(deque2.removeFromHead(), node5);
    t.checkException(new RuntimeException("Cannot remove from an empty list"), deque3, "removeFromHead");
  }

  // Method to test removeFromHead
  void testRemoveFromTail(Tester t) {
    this.initData();
    t.checkExpect(deque1.removeFromTail(), node4);
    t.checkExpect(deque2.removeFromTail(), node9);
    t.checkException(new RuntimeException("Cannot remove from an empty list"), deque3, "removeFromHead");
  }
  
  // Method to test the removeH
  void testRemoveH(Tester t) {
    this.initData();
    t.checkExpect(node1.removeH(), node1);
    t.checkExpect(node5.removeH(), node5);
    t.checkException(new RuntimeException("Cannot remove from an empty list"), sent3, "removeH");
  }
  
//  void testAddAtHead(Tester t) {
//    this.initData();
//    t.checkExpect(this.deque1.header.next, node1);
//    this.deque1.addAtHead("hi");
//    t.checkExpect(this.deque1, )
//    
//  }
  
}

















