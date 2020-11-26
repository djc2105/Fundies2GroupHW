package OldHW;
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
  
  /* fields: 
   * 
   * methods: 
   *   this.apply(Double, Double) ... Double
   * methods for fields: 
   * 
   */

  // adds two doubles
  public Double apply(Double t, Double u) {
    return t + u;
  }

}

// a function object to perform subtraction
class Minus implements BiFunction<Double, Double, Double> {
  
  /* fields: 
   * 
   * methods: 
   *   this.apply(Double, Double) ... Double
   * methods for fields: 
   * 
   */

  // subtracts two doubles
  public Double apply(Double t, Double u) {
    return t - u;
  }

}

// a function object to perform multiplication
class Mul implements BiFunction<Double, Double, Double> {
  
  /* fields: 
   * 
   * methods: 
   *   this.apply(Double, Double) ... Double
   * methods for fields: 
   * 
   */

  // multiplies two doubles
  public Double apply(Double t, Double u) {
    return t * u;
  }

}

// a function object to perform division
class Div implements BiFunction<Double, Double, Double> {

  /* fields: 
   * 
   * methods: 
   *   this.apply(Double, Double) ... Double
   * methods for fields: 
   * 
   */
  
  // divides two doubles
  public Double apply(Double t, Double u) {
    if (Math.abs(u) <= 0.0000001) {
      throw new ArithmeticException("Cannot divide by 0");
    }
    return t / u;
  }

}

// a function object to negate the given double
class Neg implements Function<Double, Double> {
  
  /* fields: 
   * 
   * methods: 
   *   this.apply(Double) ... Double
   * methods for fields: 
   * 
   */

  // negates the given double
  public Double apply(Double t) {
    return -t;
  }

}

// a function object to negate the given double
class Sqr implements Function<Double, Double> {
  
  /* fields: 
   * 
   * methods: 
   *   this.apply(Double) ... Double
   * methods for fields: 
   * 
   */

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
  
  // passes this IArithVisitor to the IArith
  R apply(IArith a);
  
}

// evaluates an IArith to a double
class EvalVisitor implements IArithVisitor<Double> {
  
  /* fields: 
   * 
   * methods: 
   *   this.visitConst(Const) ... Double
   *   this.visitUnaryFormula(UnaryFormula) ... Double
   *   this.visitBinaryFormula(BinaryFormula) ... Double
   *   this.apply(IArith) ... Double
   * methods for fields: 
   * 
   */

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
  
  // passes this EvalVisitor to the IArith
  public Double apply(IArith a) {
    return a.accept(this);
  }
  
}

// displays an IArith as a string
class PrintVisitor implements IArithVisitor<String> {
  
  /* fields: 
   * 
   * methods: 
   *   this.visitConst(Const) ... String
   *   this.visitUnaryFormula(UnaryFormula) ... String
   *   this.visitBinaryFormula(BinaryFormula) ... String
   *   this.apply(IArith) ... String
   * methods for fields: 
   * 
   */
  
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
    return "(" + bf.name + " " + bf.left.accept(this) + " "
        + bf.right.accept(this) + ")";
  }
  
  //passes this EvalVisitor to the IArith
  public String apply(IArith a) {
    return a.accept(this);
  }
  
}

// returns a new IArith with doubled constants
class DoublerVisitor implements IArithVisitor<IArith> {
  
  /* fields: 
   * 
   * methods: 
   *   this.visitConst(Const) ... IArith
   *   this.visitUnaryFormula(UnaryFormula) ... IArith
   *   this.visitBinaryFormula(BinaryFormula) ... IArith
   *   this.apply(IArith) ... IArith
   * methods for fields: 
   * 
   */
  
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
  
  // passes this DoublerVisitor to the IArith
  public IArith apply(IArith a) {
    return a.accept(this);
  }
  
}

// returns true if there are no negative values
class NoNegativeResults implements IArithVisitor<Boolean> {
  
  /* fields: 
   * 
   * methods: 
   *   this.visitConst(Const) ... Boolean
   *   this.visitUnaryFormula(UnaryFormula) ... Boolean
   *   this.visitBinaryFormula(BinaryFormula) ... Boolean
   *   this.apply(IArith) ... Boolean
   * methods for fields: 
   * 
   */
  
  // checks if the constant is negative
  public Boolean visitConst(Const c) {
    return c.num >= 0;
  }

  // checks if the number is negated
  public Boolean visitUnaryFormula(UnaryFormula uf) {
    return !uf.name.equals("neg")
        && uf.child.accept(this);
  }

  // checks if subtraction results in a negative
  public Boolean visitBinaryFormula(BinaryFormula bf) {
    IArithVisitor<Double> eval = new EvalVisitor();
    if (bf.accept(eval) < 0) {
      return false;
    }
    return bf.left.accept(this)
        && bf.right.accept(this);
  }
  
  // passes this NoNegativeResults to the IArith
  public Boolean apply(IArith a) {
    return a.accept(this);
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
  
  IArith u1 = new UnaryFormula(eNeg, "neg", c1); // -5
  IArith u2 = new UnaryFormula(eNeg, "neg", c2); // 0
  IArith u3 = new UnaryFormula(eNeg, "neg", u1); // 5
  IArith u4 = new UnaryFormula(eSqr, "sqr", c1); // 25
  IArith u5 = new UnaryFormula(eSqr, "sqr", c2); // 0
  IArith u6 = new UnaryFormula(eSqr, "sqr", c3); // 25
  
  IArith b1 = new BinaryFormula(ePlus, "plus", c1, c1); // 10
  IArith b2 = new BinaryFormula(ePlus, "plus", c1, c3); // 0
  IArith b3 = new BinaryFormula(ePlus, "plus", u4, u6); // 50
  IArith b4 = new BinaryFormula(eMinus, "minus", c2, c1); // -5
  IArith b5 = new BinaryFormula(eMinus, "minus", c5, c1); // 5
  IArith b6 = new BinaryFormula(eMinus, "minus", u4, c5); // 15
  IArith b7 = new BinaryFormula(eMul, "mul", c1, c1); // 25
  IArith b8 = new BinaryFormula(eMul, "mul", c4, c2); // 0
  IArith b9 = new BinaryFormula(eMul, "mul", u1, c5); // -50
  IArith b10 = new BinaryFormula(eDiv, "div", u4, c1); // 5
  IArith b11 = new BinaryFormula(eDiv, "div", c2, c1); // 0
  IArith b12 = new BinaryFormula(eDiv, "div", c5, c2); // error - cannot divide by 0
  
  Const con1 = new Const(5.0);
  Const con2 = new Const(0.0);
  Const con3 = new Const(-5.0);
  Const con4 = new Const(25.0);
  Const con5 = new Const(10.0);
  
  UnaryFormula uf1 = new UnaryFormula(eNeg, "neg", c1); // -5
  UnaryFormula uf2 = new UnaryFormula(eNeg, "neg", c2); // 0
  UnaryFormula uf3 = new UnaryFormula(eSqr, "sqr", c1); // 25
  UnaryFormula uf4 = new UnaryFormula(eSqr, "sqr", c2); // 0
  
  BinaryFormula bf1 = new BinaryFormula(ePlus, "plus", c1, c1); // 10
  BinaryFormula bf2 = new BinaryFormula(ePlus, "plus", c5, c3); // 5
  BinaryFormula bf3 = new BinaryFormula(eMinus, "minus", c1, c1); // 0
  BinaryFormula bf4 = new BinaryFormula(eMinus, "minus", c5, c3); // 15
  BinaryFormula bf5 = new BinaryFormula(eMul, "mul", c1, c1); // 25
  BinaryFormula bf6 = new BinaryFormula(eMul, "mul", c5, c3); // -50
  BinaryFormula bf7 = new BinaryFormula(eDiv, "div", c1, c1); // 1
  BinaryFormula bf8 = new BinaryFormula(eDiv, "div", c5, c3); // -2
  BinaryFormula bf9 = new BinaryFormula(eDiv, "div", c1, c2); // error - cannot divide by 0
  
  // test for accepting EvalVisitor
  void testAcceptEval(Tester t) {
    t.checkInexact(this.c1.accept(this.ev), 5.0, 0.001);
    t.checkInexact(this.u1.accept(this.ev), -5.0, 0.001);
    t.checkInexact(this.u4.accept(this.ev), 25.0, 0.001);
    t.checkInexact(this.b3.accept(this.ev), 50.0, 0.001);
    t.checkInexact(this.b6.accept(this.ev), 15.0, 0.001);
    t.checkInexact(this.b8.accept(this.ev), 0.0, 0.001);
    t.checkInexact(this.b10.accept(this.ev), 5.0, 0.001);
    t.checkException(new ArithmeticException("Cannot divide by 0"), this.b12, "accept", this.ev);
  }
  
  // test for accepting PrintVisitor
  void testAcceptPrint(Tester t) {
    t.checkExpect(this.c3.accept(this.pv), "-5.0");
    t.checkExpect(this.u3.accept(this.pv), "(neg (neg 5.0))");
    t.checkExpect(this.u6.accept(this.pv), "(sqr -5.0)");
    t.checkExpect(this.b1.accept(this.pv), "(plus 5.0 5.0)");
    t.checkExpect(this.b5.accept(this.pv), "(minus 10.0 5.0)");
    t.checkExpect(this.b9.accept(this.pv), "(mul (neg 5.0) 10.0)");
    t.checkExpect(this.b12.accept(this.pv), "(div 10.0 0.0)");
  }
  
  // test for accepting DoublerVisitor
  void testAcceptDoubler(Tester t) {
    t.checkExpect(this.c1.accept(this.dv), this.c5);
    t.checkExpect(this.u2.accept(this.dv), this.u2);
    t.checkExpect(this.u4.accept(this.dv), 
        new UnaryFormula(this.eSqr, "sqr", this.c5));
    t.checkExpect(this.b1.accept(this.dv), 
        new BinaryFormula(this.ePlus, "plus", this.c5, this.c5));
    t.checkExpect(this.b5.accept(this.dv), 
        new BinaryFormula(this.eMinus, "minus", new Const(20.0), this.c5));
    t.checkExpect(this.b7.accept(this.dv), 
        new BinaryFormula(this.eMul, "mul", this.c5, this.c5));
    t.checkExpect(this.b11.accept(this.dv), 
        new BinaryFormula(this.eDiv, "div", this.c2, this.c5));
  }
  
  // test for accepting NoNegativeResults
  void testNoNegatives(Tester t) {
    t.checkExpect(this.c1.accept(this.nnr), true);
    t.checkExpect(this.c2.accept(this.nnr), true);
    t.checkExpect(this.c3.accept(this.nnr), false);
    t.checkExpect(this.u1.accept(this.nnr), false);
    t.checkExpect(this.u2.accept(this.nnr), false);
    t.checkExpect(this.u4.accept(this.nnr), true);
    t.checkExpect(this.u6.accept(this.nnr), false);
    t.checkExpect(this.b1.accept(this.nnr), true);
    t.checkExpect(this.b2.accept(this.nnr), false);
    t.checkExpect(this.b4.accept(this.nnr), false);
    t.checkExpect(this.b5.accept(this.nnr), true);
    t.checkExpect(this.b8.accept(this.nnr), true);
    t.checkExpect(this.b9.accept(this.nnr), false);
    t.checkExpect(this.b10.accept(this.nnr), true);
    t.checkExpect(this.b11.accept(this.nnr), true);
  }
  
  // test for apply (Function Objects)
  void testApplyFO(Tester t) {
    // test Neg
    t.checkInexact(this.eNeg.apply(5.0), -5.0, 0.001);
    t.checkInexact(this.eNeg.apply(-5.0), 5.0, 0.001);
    t.checkInexact(this.eNeg.apply(0.0), 0.0, 0.001);
    // test Sqr
    t.checkInexact(this.eSqr.apply(5.0), 25.0, 0.001);
    t.checkInexact(this.eSqr.apply(-5.0), 25.0, 0.001);
    t.checkInexact(this.eSqr.apply(0.0), 0.0, 0.001);
    // test Plus
    t.checkInexact(this.ePlus.apply(5.0, 5.0), 10.0, 0.001);
    t.checkInexact(this.ePlus.apply(0.0, 5.0), 5.0, 0.001);
    t.checkInexact(this.ePlus.apply(5.0, -5.0), 0.0, 0.001);
    // test Minus
    t.checkInexact(this.eMinus.apply(5.0, 5.0), 0.0, 0.001);
    t.checkInexact(this.eMinus.apply(0.0, 5.0), -5.0, 0.001);
    t.checkInexact(this.eMinus.apply(5.0, -5.0), 10.0, 0.001);
    // test Mul
    t.checkInexact(this.eMul.apply(30.0, 1.0), 30.0, 0.001);
    t.checkInexact(this.eMul.apply(15.0, -2.0), -30.0, 0.001);
    t.checkInexact(this.eMul.apply(30.0, 0.0), 0.0, 0.001);
    // test Div
    t.checkInexact(this.eDiv.apply(30.0, 1.0), 30.0, 0.001);
    t.checkInexact(this.eDiv.apply(30.0, -3.0), -10.0, 0.001);
    t.checkException(new ArithmeticException("Cannot divide by 0"), this.eDiv, "apply", 25.0, 0.0);
  }
  
  // test for visitConst
  void testVisitConst(Tester t) {
    // EvalVisitor
    t.checkInexact(this.ev.visitConst(this.con1), 5.0, 0.001);
    t.checkInexact(this.ev.visitConst(con2), 0.0, 0.001);
    // PrintVisitor
    t.checkExpect(this.pv.visitConst(this.con1), "5.0");
    t.checkExpect(this.pv.visitConst(this.con5), "10.0");
    // DoublerVisitor
    t.checkExpect(this.dv.visitConst(this.con1), this.con5);
    t.checkExpect(this.dv.visitConst(this.con2), this.con2);
    // NoNegativeResults
    t.checkExpect(this.nnr.visitConst(this.con1), true);
    t.checkExpect(this.nnr.visitConst(this.con3), false);
  }
  
  // test for visitUnaryFormula
  void testVisitUnary(Tester t) {
    // EvalVisitor
    t.checkInexact(this.ev.visitUnaryFormula(this.uf1), -5.0, 0.001);
    t.checkInexact(this.ev.visitUnaryFormula(this.uf3), 25.0, 0.001);
    // PrintVisitor
    t.checkExpect(this.pv.visitUnaryFormula(this.uf2), "(neg 0.0)");
    t.checkExpect(this.pv.visitUnaryFormula(this.uf3), "(sqr 5.0)");
    // DoublerVisitor
    t.checkExpect(this.dv.visitUnaryFormula(this.uf1), 
        new UnaryFormula(this.eNeg, "neg", this.c5));
    t.checkExpect(this.dv.visitUnaryFormula(this.uf3), 
        new UnaryFormula(this.eSqr, "sqr", this.c5));
    // NoNegativeResults
    t.checkExpect(this.nnr.visitUnaryFormula(this.uf1), false);
    t.checkExpect(this.nnr.visitUnaryFormula(this.uf3), true);
  }
  
  // test for visitBinaryFormula
  void testVisitBinaryFormula(Tester t) {
    // EvalVisitor
    t.checkInexact(this.ev.visitBinaryFormula(this.bf1), 10.0, 0.001);
    t.checkInexact(this.ev.visitBinaryFormula(this.bf4), 15.0, 0.001);
    t.checkInexact(this.ev.visitBinaryFormula(this.bf6), -50.0, 0.001);
    t.checkInexact(this.ev.visitBinaryFormula(this.bf8), -2.0, 0.001);
    // PrintVisitor
    t.checkExpect(this.pv.visitBinaryFormula(this.bf2), "(plus 10.0 -5.0)");
    t.checkExpect(this.pv.visitBinaryFormula(this.bf4), "(minus 10.0 -5.0)");
    t.checkExpect(this.pv.visitBinaryFormula(this.bf6), "(mul 10.0 -5.0)");
    t.checkExpect(this.pv.visitBinaryFormula(this.bf8), "(div 10.0 -5.0)");
    // DoublerVisitor
    t.checkExpect(this.dv.visitBinaryFormula(this.bf1), 
        new BinaryFormula(this.ePlus, "plus", this.c5, this.c5));
    t.checkExpect(this.dv.visitBinaryFormula(this.bf3), 
        new BinaryFormula(this.eMinus, "minus", this.c5, this.c5));
    t.checkExpect(this.dv.visitBinaryFormula(this.bf5), 
        new BinaryFormula(this.eMul, "mul", this.c5, this.c5));
    t.checkExpect(this.dv.visitBinaryFormula(this.bf7), 
        new BinaryFormula(this.eDiv, "div", this.c5, this.c5));
    // NoNegativeResults
    t.checkExpect(this.nnr.visitBinaryFormula(this.bf1), true);
    t.checkExpect(this.nnr.visitBinaryFormula(this.bf4), false);
    t.checkExpect(this.nnr.visitBinaryFormula(this.bf5), true);
    t.checkExpect(this.nnr.visitBinaryFormula(this.bf8), false);
  }
  
  // test for apply (IArithVisitor<R>)
  void testApplyVis(Tester t) {
    // EvalVisitor
    t.checkInexact(this.ev.apply(this.c1), 5.0, 0.001);
    t.checkInexact(this.ev.apply(this.u1), -5.0, 0.001);
    t.checkInexact(this.ev.apply(this.b1), 10.0, 0.001);
    // PrintVisitor
    t.checkExpect(this.pv.apply(this.c1), "5.0");
    t.checkExpect(this.pv.apply(this.u1), "(neg 5.0)");
    t.checkExpect(this.pv.apply(this.b1), "(plus 5.0 5.0)");
    // DoublerVisitor
    t.checkExpect(this.dv.apply(this.c1), this.c5);
    t.checkExpect(this.dv.apply(this.u1), 
        new UnaryFormula(this.eNeg, "neg", this.c5));
    t.checkExpect(this.dv.apply(this.b1), 
        new BinaryFormula(this.ePlus, "plus", this.c5, this.c5));
    // NoNegativeResults
    t.checkExpect(this.nnr.apply(this.c3), false);
    t.checkExpect(this.nnr.apply(this.u1), false);
    t.checkExpect(this.nnr.apply(this.b1), true);
    t.checkExpect(this.nnr.apply(this.b9), false);
  }
  
}





















