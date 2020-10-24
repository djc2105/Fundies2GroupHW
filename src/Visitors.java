import java.util.function.*;
import tester.Tester;

// an interface to represent an arithmatic expression
interface IArith {
  
  // applies the IArith to the visitor
  <R> R accept(IArithVisitor<R> visitor);
  
}

// a class that represents a constant
class Const implements IArith {

  // variables
  double num;

  // constructor
  Const(double num) {
    this.num = num;
  }

  /* fields: 
   *   this.num ... double
   * methods: 
   *   this.accept(IArithVisitor<R>) ... R
   * methods for fields: 
   * 
   */
  
  // applies the constant to the visitor
  public <R> R accept(IArithVisitor<R> visitor) {
    // see the IArithVisitor<R> class for visitor's template
    return visitor.visitConst(this);
  }

}

// represents a unary formula
class UnaryFormula implements IArith {

  // variables
  Function<Double, Double> func;
  String name;
  IArith child;

  // constructor
  UnaryFormula(Function<Double, Double> func, String name, IArith child) {
    this.func = func;
    this.name = name;
    this.child = child;
  }

  /* fields: 
   *   this.func ... Function<Double, Double>
   *   this.name ... String
   *   this.child ... IArith
   * methods: 
   *   this.accept(IArithVisitor<R>) ... R
   * methods for fields: 
   *   this.child.accept(IArithVisitor<R>) ... R
   */
  
  //applies the unary formula to the visitor
  public <R> R accept(IArithVisitor<R> visitor) {
    // see the IArithVisitor<R> class for visitor's template
    return visitor.visitUnaryFormula(this);
  }

}

// represents a binary formula
class BinaryFormula implements IArith {

  // variables
  BiFunction<Double, Double, Double> func;
  String name;
  IArith left;
  IArith right;

  // constructor
  BinaryFormula(BiFunction<Double, Double, Double> func, 
      String name, IArith left, IArith right) {
    this.func = func;
    this.name = name;
    this.left = left;
    this.right = right;
  }

  /* fields: 
   *   this.func ... BiFunction<Double, Double, Double>
   *   this.name ... String
   *   this.left ... IArith
   *   this.right ... IArith
   * methods: 
   *   this.accept(IArithVisitor<R>) ... R
   * methods for fields: 
   *   this.left.accept(IArithVisitor<R>) ... R
   *   this.right.accept(IArithVisitor<R>) ... R
   */
  
  //applies the constant to the visitor
  public <R> R accept(IArithVisitor<R> visitor) {
    // see the IArithVisitor<R> class for visitor's template
    return visitor.visitBinaryFormula(this);
  }

}

// a function object to perform addition
class Plus implements BiFunction<Double, Double, Double> {

  // adds two doubles
  public Double apply(Double t, Double u) {
    return t + u;
  }

}

// a function object to perform subtraction
class Minus implements BiFunction<Double, Double, Double> {

  // subtracts two doubles
  public Double apply(Double t, Double u) {
    return t - u;
  }

}

// a function object to perform multiplication
class Mul implements BiFunction<Double, Double, Double> {

  // multiplies two doubles
  public Double apply(Double t, Double u) {
    return t * u;
  }

}

// a function object to perform division
class Div implements BiFunction<Double, Double, Double> {

  // divides two doubles
  public Double apply(Double t, Double u) {
    return t / u;
  }

}

// a function object to negate the given double
class Neg implements Function<Double, Double> {

  // negates the given double
  public Double apply(Double t) {
    return -t;
  }

}

// a function object to negate the given double
class Sqr implements Function<Double, Double> {

  // negates the given double
  public Double apply(Double t) {
    return t * t;
  }

}

// an interface to represent a visitor to an IArith
interface IArithVisitor<R> {
  
  // visits the Const class
  R visitConst(Const c);
  
  // visits the UnaryFormula class
  R visitUnaryFormula(UnaryFormula uf);
  
  // visits the BinaryFormula class
  R visitBinaryFormula(BinaryFormula bf);
  
}

// evaluates an IArith to a double
class EvalVisitor implements IArithVisitor<Double> {

  // evaluates the constant
  public Double visitConst(Const c) {
    return c.num;
  }

  // evaluates the Unary Formula
  public Double visitUnaryFormula(UnaryFormula uf) {
    return uf.func.apply(uf.child.accept(this));
  }

  // evaluates the Binary Formula
  public Double visitBinaryFormula(BinaryFormula bf) {
    return bf.func.apply(bf.left.accept(this), 
        bf.right.accept(this));
  }
  
}





















