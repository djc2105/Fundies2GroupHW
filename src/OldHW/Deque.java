package OldHW;

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
   *   this.header.sizeHelper(int) ... int
   *   this.header.addH(T) ... void
   *   this.header.removeH() ... T
   *   this.header.findH(Predicate<T>) ... ANode<T>
   */
  
  // Returns the size of the deque
  int size() {
    return this.header.next.sizeHelper(0);
  }
  
  // adds a new node at the front of the deque
  // EFFECT: makes a new node in the header's next position
  void addAtHead(T data) {
    this.header.addH(data);
  }
  
  // adds a new node at the back of the deque
  // EFFECT: makes a new node in the header's prev position
  void addAtTail(T data) {
    this.header.prev.addH(data);
  }
  
  // removes the element at the headers next position
  T removeFromHead() {
    return this.header.next.removeH();
  }
  
  // removes the element at the headers prev position
  T removeFromTail() {
    return this.header.prev.removeH();
  }
  
  // finds the given node within the deque
  ANode<T> find(Predicate<T> pred) {
    return this.header.next.findH(pred);
  }
  
  // removes the given node from the deque
  void removeNode(ANode<T> node) {
    node.removeH();
  }
  
}

// an abstract class to represent a node in Deque
abstract class ANode<T> {
  
  // variables
  ANode<T> next;
  ANode<T> prev;
  
  // Returns the int once it finds the sentenial is the node
  abstract int sizeHelper(int i);

  // inserts a new node into the next of this node
  void addH(T data) {
    this.next = new Node<T>(data, this.next, this);
  }
  
  // Returns the removed item and removes the head node
  abstract T removeH();
  
  // helps to find the node with data matching the given predicate
  abstract ANode<T> findH(Predicate<T> pred);
  
}

// represents the starter for a Deque
class Sentinel<T> extends ANode<T> {
  
  // constructor
  Sentinel() {
    this.next = this;
    this.prev = this;
  }
  
  /* fields: 
   *   this.next ... ANode<T>
   *   this.prev ... ANode<T>
   * methods: 
   *   this.sizeHelper(int) ... int
   *   this.addH(T) ... void
   *   this.removeH() ... T
   *   this.findH(Predicate<T>) ... ANode<T>
   * methods for fields: 
   *   this.next.sizeHelper(int) ... int
   *   this.next.addH(T) ... void
   *   this.next.removeH() ... T
   *   this.next.findH(Predicate<T>) ... ANode<T>
   *   this.prev.sizeHelper(int) ... int
   *   this.prev.addH(T) ... void
   *   this.prev.removeH() ... T
   *   this.prev.findH(Predicate<T>) ... ANode<T>
   */

  // Returns the int because it has looped
  int sizeHelper(int i) {
    return i;
  }

  // Throws a runtime error
  T removeH() {
    throw new RuntimeException("Cannot remove from an empty list");
  }
  
  // returns the sentinel
  ANode<T> findH(Predicate<T> pred) {
    return this;
  }
  
}

// represents an element of the Deque
class Node<T> extends ANode<T> {
  
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
   *   this.sizeHelper(int) ... int
   *   this.addH(T) ... void
   *   this.removeH() ... T
   *   this.findH(Predicate<T>) ... ANode<T>
   * methods for fields: 
   *   this.next.sizeHelper(int) ... int
   *   this.next.addH(T) ... void
   *   this.next.removeH() ... T
   *   this.next.findH(Predicate<T>) ... ANode<T>
   *   this.prev.sizeHelper(int) ... int
   *   this.prev.addH(T) ... void
   *   this.prev.removeH() ... T
   *   this.prev.findH(Predicate<T>) ... ANode<T>
   */

  // Adds one to the int and then calls sizeHelper on next
  int sizeHelper(int i) {
    return this.next.sizeHelper(i + 1);
  }

  // Returns the removed item and removes the head node
  T removeH() {
    ANode<T> nextNode = this.next;
    ANode<T> prevNode = this.prev;
    
    nextNode.prev = prevNode;
    prevNode.next = nextNode;
    
    return this.data;
  }

  // checks if this node's data fulfills the given predicate
  ANode<T> findH(Predicate<T> pred) {
    if (pred.test(this.data)) {
      return this;
    } else {
      return this.next.findH(pred);
    }
  }
  
}

class ExamplesDeque {
  
  // example variables
  Deque<String> deque1 = new Deque<String>();
  Sentinel<String> sent1 = new Sentinel<String>();
  ANode<String> node1 = new Node<String>("abc");
  ANode<String> node2 = new Node<String>("bcd");
  ANode<String> node3 = new Node<String>("cde");
  ANode<String> node4 = new Node<String>("def");
  
  Deque<String> deque1addH = new Deque<String>();
  Sentinel<String> sent1addH = new Sentinel<String>();
  ANode<String> newAddH = new Node<String>("efg");
  ANode<String> node1addH = new Node<String>("abc");
  ANode<String> node2addH = new Node<String>("bcd");
  ANode<String> node3addH = new Node<String>("cde");
  ANode<String> node4addH = new Node<String>("def");
  
  Deque<String> deque1addT = new Deque<String>();
  Sentinel<String> sent1addT = new Sentinel<String>();
  ANode<String> node1addT = new Node<String>("abc");
  ANode<String> node2addT = new Node<String>("bcd");
  ANode<String> node3addT = new Node<String>("cde");
  ANode<String> node4addT = new Node<String>("def");
  ANode<String> newAddT = new Node<String>("efg");
  
  
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
    
    deque1addH = new Deque<String>();
    sent1addH = this.deque1addH.header;
    newAddH = new Node<String>("efg", this.sent1addH, this.sent1addH);
    node1addH = new Node<String>("abc", this.sent1addH, this.newAddH);
    node2addH = new Node<String>("bcd", this.sent1addH, this.node1addH);
    node3addH = new Node<String>("cde", this.sent1addH, this.node2addH);
    node4addH = new Node<String>("def", this.sent1addH, this.node3addH);
    
    deque1addT = new Deque<String>();
    sent1addT = this.deque1addT.header;
    node1addT = new Node<String>("abc", this.sent1addT, this.sent1addT);
    node2addT = new Node<String>("bcd", this.sent1addT, this.node1addT);
    node3addT = new Node<String>("cde", this.sent1addT, this.node2addT);
    node4addT = new Node<String>("def", this.sent1addT, this.node3addT);
    newAddT = new Node<String>("efg", this.sent1addT, this.node4addT);
    
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
  
  // test that Deques are initialized correctly
  void testDeque(Tester t) {
    this.initData();
    t.checkExpect(this.node2.prev, this.node1);
    t.checkExpect(this.node2.next, this.node3);
    t.checkExpect(this.node4.next, this.deque1.header);
    t.checkExpect(this.deque2.header, this.sent2);
    t.checkExpect(this.deque2.header.next, this.node5);
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
  
  // test addAtHead
  void testAddAtHead(Tester t) {
    this.initData();
    t.checkExpect(this.deque1.header, this.sent1);
    t.checkExpect(this.deque1.header.next, this.node1);
    this.deque1.addAtHead("efg");
    t.checkExpect(this.deque1.header, this.sent1addH);
    t.checkExpect(this.deque1.header.next, this.newAddH);
  }
  
  // test addAtTail
  void testAddAtTail(Tester t) {
    this.initData();
    t.checkExpect(this.deque1.header, this.sent1);
    t.checkExpect(this.deque1.header.next, this.node1);
    this.deque1.addAtTail("efg");
    t.checkExpect(this.deque1.header, this.sent1addT);
    t.checkExpect(this.deque1.header.prev, this.newAddT);
  }
  
  // test addH
  void testAddH(Tester t) {
    this.initData();
    t.checkExpect(this.deque1.header, this.sent1);
    t.checkExpect(this.deque1.header.next, this.node1);
    this.deque1.header.addH("efg");
    t.checkExpect(this.deque1.header, this.sent1addH);
    t.checkExpect(this.deque1.header.next, this.newAddH);
  }
  
  // Method to test removeFromHead
  void testRemoveFromHead(Tester t) {
    this.initData();
    t.checkExpect(deque1.removeFromHead(), "abc");
    t.checkExpect(deque2.removeFromHead(), "the");
    t.checkException(new RuntimeException("Cannot remove from an empty list"), 
        deque3, "removeFromHead");
  }

  // Method to test removeFromHead
  void testRemoveFromTail(Tester t) {
    this.initData();
    t.checkExpect(deque1.removeFromTail(), "def");
    t.checkExpect(deque2.removeFromTail(), "nerd");
    t.checkException(new RuntimeException("Cannot remove from an empty list"), 
        deque3, "removeFromHead");
  }
  
  // Method to test the removeH
  void testRemoveH(Tester t) {
    this.initData();
    t.checkExpect(node1.removeH(), "abc");
    t.checkExpect(node5.removeH(), "the");
    t.checkException(new RuntimeException("Cannot remove from an empty list"), 
        sent3, "removeH");
  }
  
  // test for find
  void testFind(Tester t) {
    this.initData();
    t.checkExpect(deque1.find((s) -> s.equals("bcd")), node2);
    t.checkExpect(deque1.find((s) -> s.equals("xyz")), sent1);
    t.checkExpect(deque2.find((s) -> s.length() == 7), node7);
    t.checkExpect(deque2.find((s) -> s.length() > 12), sent2);
  }
  
  // test for removeNode
  void testRemoveNode(Tester t) {
    this.initData();
    t.checkExpect(this.deque1addH.header, this.sent1addH);
    t.checkExpect(this.deque1addH.header.next, this.newAddH);
    this.deque1addH.removeNode(newAddH);
    t.checkExpect(this.deque1addH.header, this.sent1);
    t.checkExpect(this.deque1addH.header.next, this.node1);
  }
  
}

















