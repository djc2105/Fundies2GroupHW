package OldHW;
import tester.*;


public class testClass {
  
  double hahaFunny;
  
  testClass() {
    this.hahaFunny = 69.420;
  }
  
}



class ExamplesTest {
  
  String countDown(int num) {
    String result = "";
    for(int i = num; i > 0; i--) {
      result = result + Integer.toString(i);
    }
    
    return result;
  }
  
  void test(Tester t) {
    t.checkExpect(countDown(10), "10987654321");
  }
}