import java.util.function.BiFunction;
import tester.*;

// A class to represent an Instructor
class Instructor {
  
  // Variables
  String name;
  IList<Course> courses = new MtList<Course>();
  
  public Instructor(String name) {
    this.name = name;
  }
  
  /* fields: 
   * this.name ... String
   * this.courses ... IList<Course>
   * 
   * methods: 
   * this.addCourse(Course) ... Void
   * this.dejavu(Student) ... Boolean
   * 
   * methods for fields: 
   * this.courses.contains(U, BiFunction<T, U, Integer>) ... Integer
   * this.courses.dejavuH(U, BiFunction<T, U, Integer>) ... Integer
   * 
   */
  
  // EFFECT: Adds the given course to this list of courses
  void addCourse(Course c) {
    this.courses = new ConsList<Course>(c, this.courses);
  }
  
  // Returns true if the given student is in two or more of the instructors classes
  boolean dejavu(Student s) {
    return this.courses.dejavuH(s, new StuInCourse()) > 1;
  }

}

// A class to represent a course
class Course {
  
  // Variables
  String name;
  Instructor prof;
  IList<Student> students = new MtList<Student>();
  
  public Course(String name, Instructor prof) {
    this.name = name;
    this.prof = prof;
    this.prof.addCourse(this);
  }
  
  /* fields: 
   * this.name ... String
   * this.prof ... Instructor
   * this.students ... IList<Student>
   * 
   * methods: 
   * this.enroll(Student) ... Void
   * this.containsStudent(Student) ... Integer
   * 
   * methods for fields: 
   * this.students.contains(U, BiFunction<T, U, Integer>) ... Integer
   * this.students.dejavuH(U, BiFunction<T, U, Integer>) ... Integer
   * this.prof.addCourse(Course) ... Void
   * this.prof.dejavu(Student) ... Boolean
   */
  
  // EFFECT: Adds the given student into the list of students and 
  // adds that course to the students list of courses
  void enroll(Student s) {
    this.students = new ConsList<Student>(s, this.students);
  }

  // Return true if this student is in this courses list of students
  public int containsStudent(Student s) {
    return students.contains(s, new StuIsStu());
  }
  
}

// A class to represent a student
class Student {
  
  // Variables
  String name;
  int id;
  IList<Course> courses = new MtList<Course>();
  
  public Student(String name, int id) {
    this.name = name;
    this.id = id;
  }
  
  /* fields: 
   * this.name ... String
   * this.id ... Integer
   * this.courses ... IList<Course>
   * 
   * methods: 
   * this.enroll(Course) ... Void
   * this.classmates(Student) ... Boolean
   * 
   * methods for fields: 
   * this.courses.contains(U, BiFunction<T, U, Integer>) ... Integer
   * this.courses.dejavuH(U, BiFunction<T, U, Integer>) ... Integer
   */
  
  // EFFECT: Enrolls a student in the given course
  void enroll(Course c) {
    this.courses = new ConsList<Course>(c, this.courses);
    c.enroll(this);
  }
  
  // Returns true if this student is in any of the same classes as the give student
  boolean classmates(Student s) {
    return this.courses.contains(s, new StuInCourse()) > 0;
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
  
  // Variables
  T first;
  IList<T> rest;
  
  /* fields: 
   * this.first ... T
   * this.rest ... IList<T>
   * 
   * methods: 
   * this.contains(U, BiFunction<T, U, Integer>) ... Integer
   * this.dejavuH(U, BiFunction<T, U, Integer>) ... Integer
   * 
   * methods for fields: 
   * this.rest.contains(U, BiFunction<T, U, Integer>) ... Integer
   * this.rest.dejavuH(U, BiFunction<T, U, Integer>) ... Integer
   * 
   */
  
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

  /* fields: 
   * 
   * methods: 
   * this.contains(U, BiFunction<T, U, Integer>) ... Integer
   * this.dejavuH(U, BiFunction<T, U, Integer>) ... Integer
   * 
   * methods for fields: 
   * 
   */
  
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
class StuInCourse implements BiFunction<Course, Student, Integer> {

  /* fields: 
   * 
   * methods: 
   * this.apply(Course, Student) ... Integer
   * 
   * methods for fields: 
   * 
   */
  
  // Returns true if the given student is in the given course
  public Integer apply(Course c, Student s) {
    return c.containsStudent(s);
  } 

}

// A class to represent an apply function that checks if a student in in a list of students
class StuIsStu implements BiFunction<Student, Student, Integer> {

  /* fields: 
   * 
   * methods: 
   * this.apply(Student, Student) ... Integer
   * 
   * methods for fields: 
   * 
   */
  
  // Returns 1 if this student has the same ID as that student else returns 0
  public Integer apply(Student t, Student u) {
    if (t.id == u.id) {
      return 1;
    }
    return 0;
  }
  
}

// A class to test the methods
class ExamplesRegistrar {
  
  Instructor razzaq = new Instructor("Prof. Razzaq");
  Instructor mhetghalchi = new Instructor("Prof. Mhetghalchi");
  
  Course thermo = new Course("Thermodynamics", this.mhetghalchi);
  Course mechanics = new Course("Mechanics of Materials", this.mhetghalchi);
  Course fundies2 = new Course("Fundamental of CS 2", this.razzaq);
  Course ood = new Course("Object Oriented Design", this.razzaq);
  
  Student daniel = new Student("Daniel", 1318191);
  Student mason = new Student("Mason", 1398877);
  Student shourik = new Student("Shourik", 1333562);
  Student dylan = new Student("Dylan", 1339627);
  Student joe = new Student("Joe", 1364368);  
  
  MtList<Course> mtCourseList = new MtList<Course>();
  MtList<Student> mtStudentList = new MtList<Student>();
  ConsList<Course> courseList1 = new ConsList<Course>(this.fundies2, this.mtCourseList);
  ConsList<Course> courseList2 = new ConsList<Course>(this.ood, this.courseList1);
  ConsList<Course> courseListM1 = new ConsList<Course>(this.thermo, this.mtCourseList);
  ConsList<Course> courseListM2 = new ConsList<Course>(this.mechanics, this.courseList1);
  ConsList<Student> studentList1 = new ConsList<Student>(this.daniel, this.mtStudentList);
  ConsList<Student> studentList2 = new ConsList<Student>(this.mason, this.studentList1);
  
  StuInCourse stuInCourse = new StuInCourse();
  StuIsStu stuInLoStu = new StuIsStu();
  
  void init() {
    daniel = new Student("Daniel", 1318191);
    mason = new Student("Mason", 1398877);
    shourik = new Student("Shourik", 1333562);
    dylan = new Student("Dylan", 1339627);
    joe = new Student("Joe", 1364368);
    
    razzaq = new Instructor("Prof. Razzaq");
    mhetghalchi = new Instructor("Prof. Mhetghalchi");
    
    thermo = new Course("Thermodynamics", this.mhetghalchi);
    mechanics = new Course("Mechanics of Materials", this.mhetghalchi);
    fundies2 = new Course("Fundamental of CS 2", this.razzaq);
    ood = new Course("Object Oriented Design", this.razzaq);
    
    mtCourseList = new MtList<Course>();
    mtStudentList = new MtList<Student>();
    courseList1 = new ConsList<Course>(this.fundies2, this.mtCourseList);
    courseList2 = new ConsList<Course>(this.ood, this.courseList1);
    courseListM1 = new ConsList<Course>(this.thermo, this.mtCourseList);
    courseListM2 = new ConsList<Course>(this.mechanics, this.courseListM1);
    studentList1 = new ConsList<Student>(this.daniel, this.mtStudentList);
    studentList2 = new ConsList<Student>(this.mason, this.studentList1);
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
    this.fundies2.enroll(this.daniel);
    t.checkExpect(this.fundies2.students, studentList1);
    this.fundies2.enroll(this.mason);
    t.checkExpect(this.fundies2.students, studentList2);    
  }
  
  // A method to test the classmates method for student
  boolean testClassmates(Tester t) {
    init();
    this.daniel.enroll(this.fundies2);
    this.mason.enroll(this.fundies2);
    this.shourik.enroll(this.thermo);
    this.joe.enroll(this.thermo);
    this.joe.enroll(this.fundies2);
    return t.checkExpect(this.daniel.classmates(this.mason), true)
        && t.checkExpect(this.daniel.classmates(this.shourik), false)
        && t.checkExpect(this.daniel.classmates(this.joe), true)
        && t.checkExpect(this.joe.classmates(this.shourik), true)
        && t.checkExpect(this.shourik.classmates(this.mason), false);
  }
  
  // A method to test the apply function in stuInCourse
  boolean testStuInCourse(Tester t) {
    init();
    this.shourik.enroll(thermo);
    this.mason.enroll(fundies2);
    this.mason.enroll(thermo);
    this.joe.enroll(mechanics);    
    
    return t.checkExpect(this.stuInCourse.apply(this.thermo, this.shourik), 1)
        && t.checkExpect(this.stuInCourse.apply(this.thermo, this.mason), 1)
        && t.checkExpect(this.stuInCourse.apply(this.fundies2, this.mason), 1)
        && t.checkExpect(this.stuInCourse.apply(this.mechanics, this.joe), 1)
        && t.checkExpect(this.stuInCourse.apply(this.mechanics, this.shourik), 0)
        && t.checkExpect(this.stuInCourse.apply(this.fundies2, this.shourik), 0)
        && t.checkExpect(this.stuInCourse.apply(this.thermo, this.joe), 0);
  }
  
  // A method to test the apply function in StuIsStu
  boolean testStuIsStu(Tester t) {
    init();
    
    return t.checkExpect(this.stuInLoStu.apply(this.shourik, this.shourik), 1)
        && t.checkExpect(this.stuInLoStu.apply(this.shourik, this.mason), 0)
        && t.checkExpect(this.stuInLoStu.apply(this.shourik, this.mason), 0)
        && t.checkExpect(this.stuInLoStu.apply(this.shourik, this.joe), 0)
        && t.checkExpect(this.stuInLoStu.apply(this.daniel, this.daniel), 1);
    
  }
  
  // A method to test the contains function in ILo
  boolean testContains(Tester t) {
    init();
    this.shourik.enroll(this.thermo);
    this.mason.enroll(this.fundies2);
    this.mason.enroll(this.thermo);
    this.joe.enroll(this.mechanics);    
    
    return t.checkExpect(this.shourik.courses.contains(this.shourik, this.stuInCourse), 1)
        && t.checkExpect(this.mason.courses.contains(this.shourik, this.stuInCourse), 1)
        && t.checkExpect(this.joe.courses.contains(this.shourik, this.stuInCourse), 0)
        && t.checkExpect(this.thermo.students.contains(this.shourik, this.stuInLoStu), 1)
        && t.checkExpect(this.fundies2.students.contains(this.shourik, this.stuInLoStu), 0)
        && t.checkExpect(this.mechanics.students.contains(this.joe, this.stuInLoStu), 1)
        && t.checkExpect(this.ood.students.contains(this.shourik, stuInLoStu), 0)
        && t.checkExpect(this.daniel.courses.contains(this.daniel, this.stuInCourse), 0);
  }
  
  // A method to test Contains student in Course Class
  boolean testContainsStudent(Tester t) {
    init();
    this.shourik.enroll(this.thermo);
    this.mason.enroll(this.fundies2);
    this.mason.enroll(this.thermo);
    this.joe.enroll(this.mechanics);    
    
    return t.checkExpect(this.thermo.containsStudent(this.shourik), 1)
        && t.checkExpect(this.fundies2.containsStudent(this.shourik), 0)
        && t.checkExpect(this.mechanics.containsStudent(this.joe), 1);
  }
  
  // A method to test dejavu 
  boolean testDejavu(Tester t) {
    init();
    this.daniel.enroll(this.ood);
    this.daniel.enroll(this.fundies2);
    this.mason.enroll(this.fundies2);
    this.mason.enroll(this.thermo);
    this.mason.enroll(this.mechanics);
    this.shourik.enroll(mechanics);
    Course fundies1 = new Course("Fundamentals of CS 1", razzaq);
    this.joe.enroll(fundies1);
    this.joe.enroll(this.fundies2);
    this.joe.enroll(this.ood);
    
    return t.checkExpect(this.razzaq.dejavu(this.daniel), true) // Twice
        && t.checkExpect(this.razzaq.dejavu(this.joe), true) // Three
        && t.checkExpect(this.razzaq.dejavu(this.mason), false) // Once
        && t.checkExpect(this.razzaq.dejavu(this.shourik), false) // Zero
        && t.checkExpect(this.mhetghalchi.dejavu(this.joe), false) // Zero
        && t.checkExpect(this.mhetghalchi.dejavu(this.mason), true) // Twice
        && t.checkExpect(this.mhetghalchi.dejavu(this.shourik), false); // Once
  }
  
  // A method to test dejavuH
  boolean testDejavuH(Tester t) {
    init();
    this.shourik.enroll(this.thermo);
    this.shourik.enroll(this.mechanics);
    this.shourik.enroll(this.ood);
    this.shourik.enroll(this.fundies2);
    this.mason.enroll(this.fundies2);
    this.mason.enroll(this.thermo);
    this.daniel.enroll(this.fundies2);
    this.daniel.enroll(this.ood);
    this.joe.enroll(this.mechanics);   
    
    return t.checkExpect(this.razzaq.courses.dejavuH(this.shourik, this.stuInCourse), 2)
        && t.checkExpect(this.razzaq.courses.dejavuH(this.mason, this.stuInCourse), 1)
        && t.checkExpect(this.razzaq.courses.dejavuH(this.daniel, this.stuInCourse), 2)
        && t.checkExpect(this.razzaq.courses.dejavuH(this.joe, this.stuInCourse), 0)
        && t.checkExpect(this.mhetghalchi.courses.dejavuH(this.shourik, this.stuInCourse), 2)
        && t.checkExpect(this.mhetghalchi.courses.dejavuH(this.mason, this.stuInCourse), 1)
        && t.checkExpect(this.mhetghalchi.courses.dejavuH(this.daniel, this.stuInCourse), 0)
        && t.checkExpect(this.mhetghalchi.courses.dejavuH(this.joe, this.stuInCourse), 1)
        && t.checkExpect(this.mtCourseList.dejavuH(this.shourik, this.stuInCourse), 0);
    
  }
  
  // A method to test addCourse for the Instructor Class
  void testAddCourse(Tester t) {
    init();
    t.checkExpect(this.razzaq.courses, courseList2);
    t.checkExpect(this.mhetghalchi.courses, courseListM2);
    razzaq.addCourse(this.thermo);
    mhetghalchi.addCourse(this.fundies2);
    ConsList<Course> courseList3 = new ConsList<Course>(this.thermo, this.courseList2);
    ConsList<Course> courseListM3 = new ConsList<Course>(this.fundies2, this.courseListM2);
    t.checkExpect(this.mhetghalchi.courses, courseListM3);
    t.checkExpect(this.razzaq.courses, courseList3);  
    
  }
  
}