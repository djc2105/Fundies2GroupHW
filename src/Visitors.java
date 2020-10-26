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

// displays an IArith as a string
class PrintVisitor implements IArithVisitor<String> {

  // prints the constant
  public String visitConst(Const c) {
    return Double.toString(c.num);
  }

  // prints the unary formula
  public String visitUnaryFormula(UnaryFormula uf) {
    return "(" + uf.name + " " + uf.child.accept(this) + ")";
  }

  // prints the binary formula
  public String visitBinaryFormula(BinaryFormula bf) {
    return "(" + bf.name + " " + bf.left.accept(this) + ", "
        + bf.right.accept(this) + ")";
  }
  
}

// returns a new IArith with doubled constants
class DoublerVisitor implements IArithVisitor<IArith> {

  // doubles the constant
  public IArith visitConst(Const c) {
    return new Const(c.num * 2);
  }

  // doubles any constants in the unary formula
  public IArith visitUnaryFormula(UnaryFormula uf) {
    return new UnaryFormula(uf.func, uf.name, uf.child.accept(this));
  }

  // doubles any constants in the binary formula
  public IArith visitBinaryFormula(BinaryFormula bf) {
    return new BinaryFormula(bf.func, bf.name, 
        bf.left.accept(this), bf.right.accept(this));
  }
  
}

// returns true if there are no negative values
class NoNegativeResults implements IArithVisitor<Boolean> {

  // checks if the constant is negative
  public Boolean visitConst(Const c) {
    return c.num >= 0;
  }

  // checks if the number is negated
  public Boolean visitUnaryFormula(UnaryFormula uf) {
    return uf.name.equals("neg")
        && uf.child.accept(this);
  }

  // checks if subtraction results in a negative
  public Boolean visitBinaryFormula(BinaryFormula bf) {
    IArithVisitor<Double> eval = new EvalVisitor();
    return bf.name.equals("sub")
        && bf.left.accept(eval) < bf.right.accept(eval)
        && bf.left.accept(this)
        && bf.right.accept(this);
  }
  
}

class ExamplesVisitors {
  
  Function<Double, Double> eNeg = new Neg();
  Function<Double, Double> eSqr = new Sqr();
  
  BiFunction<Double, Double, Double> ePlus = new Plus();
  BiFunction<Double, Double, Double> eMinus = new Minus();
  BiFunction<Double, Double, Double> eMul = new Mul();
  BiFunction<Double, Double, Double> eDiv = new Div();
  
  IArithVisitor<Double> ev = new EvalVisitor();
  IArithVisitor<String> pv = new PrintVisitor();
  IArithVisitor<IArith> dv = new DoublerVisitor();
  IArithVisitor<Boolean> nnr = new NoNegativeResults();
  
  IArith c1 = new Const(5.0);
  IArith c2 = new Const(0.0);
  IArith c3 = new Const(-5.0);
  IArith c4 = new Const(25.0);
  IArith c5 = new Const(10.0);
  
  IArith u1 = new UnaryFormula(eNeg, "neg", c1);
  IArith u2 = new UnaryFormula(eNeg, "neg", c2);
  IArith u3 = new UnaryFormula(eNeg, "neg", u1);
  IArith u4 = new UnaryFormula(eSqr, "sqr", c1);
  IArith u5 = new UnaryFormula(eSqr, "sqr", c2);
  IArith u6 = new UnaryFormula(eSqr, "sqr", c3);
  
  IArith b1 = new BinaryFormula(ePlus, "plus", c1, c1);
  
}





















