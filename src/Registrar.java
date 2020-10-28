import tester.*;

// A class to represent an Instructor
class Instructor {
  String name;
  IList<Course> courses = new MtLo<Course>();
  
  public Instructor(String name) {
    this.name = name;
  }
  
}

// A class to represent a course
class Course {
  String name;
  Instructor prof;
  IList<Student> students = new MtLo<Student>();
  
  public Course(String name, Instructor prof) {
    this.name = name;
    this.prof = prof;
  }
  
  // EFFECT: Enrolls the given student into the course
  void enroll(Student s) {
    this.students = new ConsLo<Student>(s, this.students);
  }
  
}

// A class to represent a student
class Student {
  String name;
  int id;
  IList<Course> courses = new MtLo<Course>();
  
  public Student(String name, int id) {
    this.name = name;
    this.id = id;
  }
  
  // EFFECT: Enrolls a student in the given course
  void enroll(Course c) {
    this.courses = new ConsLo<Course>(c, this.courses);
    c.enroll(this);
  }
}

// An interface to represent a list of T
interface IList<T> {
  
}

// A non-empty list of T
class ConsLo<T> implements IList<T> {
  T first;
  IList<T> rest;
  
  public ConsLo(T first, IList<T> rest) {
    this.first = first;
    this.rest = rest;
  }
}

// An empty list of T
class MtLo<T> implements IList<T> {
  
}

// A class to test the methods
class ExamplesRegistrar {
  Student daniel = new Student("Daniel", 1318191);
  Instructor razzaq = new Instructor("Prof. Razzaq");
  Course fundies2 = new Course("Fundamental of CS 2", razzaq);
  MtLo<Course> mtList = new MtLo<Course>();
  
  void init() {
    daniel = new Student("Daniel", 1318191);
    razzaq = new Instructor("Prof. Razzaq");
    fundies2 = new Course("Fundamental of CS 2", razzaq);
    mtList = new MtLo<Course>();
  }
  
  // A method to test enroll for a student
  boolean testEnroll(Tester t) {
    init();
    return t.checkExpect(this.daniel.courses, mtList);
  }
}