import java.util.function.BiFunction;
import tester.*;

// A class to represent an Instructor
class Instructor {
  String name;
  IList<Course> courses = new MtList<Course>();
  
  public Instructor(String name) {
    this.name = name;
  }
  
  void addCourse(Course c) {
    this.courses = new ConsList<Course>(c, this.courses);
  }
  
  boolean dejavu(Student s) {
    return this.courses.dejavuH(s, new stuInCourse()) > 1;
  }
}

// A class to represent a course
class Course {
  String name;
  Instructor prof;
  IList<Student> students = new MtList<Student>();
  
  public Course(String name, Instructor prof) {
    this.name = name;
    this.prof = prof;
    this.prof.addCourse(this);
  }
  
  // EFFECT: Adds the given student into the list of students and 
  // adds that course to the students list of courses
  void enroll(Student s) {
    this.students = new ConsList<Student>(s, this.students);
  }

  // Return true if this student is in this courses list of students
  public int containsStudent(Student s) {
    return students.contains(s, new stuInLoStu());
  }
  
}

// A class to represent a student
class Student {
  String name;
  int id;
  IList<Course> courses = new MtList<Course>();
  
  public Student(String name, int id) {
    this.name = name;
    this.id = id;
  }
  
  // EFFECT: Enrolls a student in the given course
  void enroll(Course c) {
    this.courses = new ConsList<Course>(c, this.courses);
    c.enroll(this);
  }
  
  // Returns true if this student is in any of the same classes as the give student
  boolean classmates(Student s) {
    if(this.courses.contains(s, new stuInCourse()) > 0) {
      return true;
    }
    return false;
  }
}

// An interface to represent a list of T
interface IList<T> {
  //Return true if this List contains the given U
  <U> int contains(U stu, BiFunction<T, U, Integer> contains);
  
  // Folds a list of T into a U given a T U -> R Function and a R R -> R Function
  <U> int dejavuH(U u, BiFunction<T, U, Integer> change);
}

// A non-empty list of T
class ConsList<T> implements IList<T> {
  T first;
  IList<T> rest;
  
  public ConsList(T first, IList<T> rest) {
    this.first = first;
    this.rest = rest;
  }

  // Returns true if this list of T contains the given U
  public <U> int contains(U stu, BiFunction<T, U, Integer> func) {
    return func.apply(this.first, stu) + this.rest.contains(stu, func);
  }

  // Returns an int that is the result of summing all the changed items in the lust
  public <U> int dejavuH(U u, BiFunction<T, U, Integer> change) {
    return change.apply(this.first, u) + this.rest.dejavuH(u, change);
  }
}

// An empty list of T
class MtList<T> implements IList<T> {

  // An empty list never contains a given U
  public <U> int contains(U stu, BiFunction<T, U, Integer> contains) {
    return 0;
  }

  // Returns 0 because the sum of nothing is 0
  public <U> int dejavuH(U u, BiFunction<T, U, Integer> change) {
    return 0;
  }
}


// A class to represent an apply function that checks if a student is in a course
class stuInCourse implements BiFunction<Course, Student, Integer> {

  // Returns true if the given student is in the given course
  public Integer apply(Course c, Student s) {
    return c.containsStudent(s);
  } 
}

// A class to represent an apply function that checks if a student in in a list of students
class stuInLoStu implements BiFunction<Student, Student, Integer> {

  // Returns true if this student is the same as that student
  public Integer apply(Student t, Student u) {
    if(t.id == u.id) {
      return 1;
    }
    return 0;
  }
  
}







// A class to test the methods
class ExamplesRegistrar {
  
  Instructor razzaq = new Instructor("Prof. Razzaq");
  Instructor mhetghalchi = new Instructor("Prof. Mhetghalchi");
  
  Course thermo = new Course("Thermodynamics", mhetghalchi);
  Course mechanics = new Course("Mechanics of Materials", mhetghalchi);
  Course ood = new Course("Object Oriented Design", razzaq);
  Course fundies2 = new Course("Fundamental of CS 2", razzaq);
  
  Student daniel = new Student("Daniel", 1318191);
  Student mason = new Student("Mason", 1398877);
  Student shourik = new Student("Shourik", 1333562);
  Student dylan = new Student("Dylan", 1339627);
  Student joe = new Student("Joe", 1364368);  
  
  MtList<Course> mtCourseList = new MtList<Course>();
  MtList<Student> mtStudentList = new MtList<Student>();
  ConsList<Course> courseList1 = new ConsList<Course>(fundies2, mtCourseList);
  ConsList<Course> courseList2 = new ConsList<Course>(ood, courseList1);
  ConsList<Student> studentList1 = new ConsList<Student>(daniel, mtStudentList);
  ConsList<Student> studentList2 = new ConsList<Student>(mason, studentList1);
  
  void init() {
    daniel = new Student("Daniel", 1318191);
    mason = new Student("Mason", 1398877);
    shourik = new Student("Shourik", 1333562);
    dylan = new Student("Dylan", 1339627);
    joe = new Student("Joe", 1364368);
    
    razzaq = new Instructor("Prof. Razzaq");
    mhetghalchi = new Instructor("Prof. Mhetghalchi");
    
    thermo = new Course("Thermodynamics", mhetghalchi);
    mechanics = new Course("Mechanics of Materials", mhetghalchi);
    fundies2 = new Course("Fundamental of CS 2", razzaq);
    ood = new Course("Object Oriented Design", razzaq);
    
    mtCourseList = new MtList<Course>();
    mtStudentList = new MtList<Student>();
    courseList1 = new ConsList<Course>(fundies2, mtCourseList);
    courseList2 = new ConsList<Course>(ood, courseList1);
    studentList1 = new ConsList<Student>(daniel, mtStudentList);
    studentList2 = new ConsList<Student>(mason, studentList1);
  }
  
  // A method to test enroll for a student
  void testEnrollStudent(Tester t) {
    init();
    t.checkExpect(this.daniel.courses, mtCourseList);
    t.checkExpect(this.mason.courses, mtCourseList);
    t.checkExpect(this.fundies2.students, mtStudentList);
    this.daniel.enroll(fundies2);
    t.checkExpect(this.daniel.courses, courseList1);
    t.checkExpect(this.fundies2.students, studentList1);
    this.mason.enroll(fundies2);
    t.checkExpect(this.mason.courses, courseList1);
    t.checkExpect(this.fundies2.students, studentList2);
    this.daniel.enroll(ood);
    t.checkExpect(this.daniel.courses, courseList2);
  }
  
  // A method to test enroll for a course
  void testEnrollCourse(Tester t) {
    init();
    t.checkExpect(this.fundies2.students, mtStudentList);
    this.daniel.enroll(fundies2);
    t.checkExpect(this.fundies2.students, studentList1);
    this.mason.enroll(fundies2);
    t.checkExpect(this.fundies2.students, studentList2);    
  }
  
  // A method to test the classmates method for student
  boolean testClassmates(Tester t) {
    init();
    this.daniel.enroll(this.fundies2);
    this.mason.enroll(this.fundies2);
    return t.checkExpect(this.daniel.classmates(this.mason), true)
        && t.checkExpect(this.daniel.classmates(shourik), false);
  }
  
  // A method to test the apply function in stuInCourse
  
  // A method to test the apply function in stuInLoStu
  
  // A method to test the contains function in ILo
  
  // A method to test Contains student in Course Class
  
  // A method to test dejavu 
  boolean testDejavu(Tester t) {
    init();
    this.daniel.enroll(this.ood);
    this.daniel.enroll(this.fundies2);
    this.mason.enroll(this.fundies2);
    return t.checkExpect(this.razzaq.dejavu(this.daniel), true)
        && t.checkExpect(this.razzaq.dejavu(this.mason), false)
        && t.checkExpect(this.razzaq.dejavu(this.shourik), false);
  }
  
  
  // A method to test dejavuH
  
  // A method to test addCourse for the Instructor Class
  
}