import java.util.*;
import tester.Tester;

/**
 * A class that defines a new permutation code, as well as methods for encoding
 * and decoding of the messages that use this code.
 */
class PermutationCode {
  // The original list of characters to be encoded
  ArrayList<Character> alphabet = 
      new ArrayList<Character>(Arrays.asList(
          'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 
          'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 
          't', 'u', 'v', 'w', 'x', 'y', 'z'));

  ArrayList<Character> code = new ArrayList<Character>(26);

  // A random number generator
  Random rand = new Random();

  // Create a new instance of the encoder/decoder with a new permutation code 
  PermutationCode() {
    this.code = this.initEncoder();
  }

  // Create a new instance of the encoder/decoder with the given code 
  PermutationCode(ArrayList<Character> code) {
    this.code = code;
  }

  // Initialize the encoding permutation of the characters
  ArrayList<Character> initEncoder() {
    ArrayList<Character> resultCode = new ArrayList<Character>(26);
    ArrayList<Character> alphabetToChange = 
        new ArrayList<Character>(Arrays.asList(
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 
            't', 'u', 'v', 'w', 'x', 'y', 'z'));
    for (int i = 0; i < 26; i++) {
      int index = this.rand.nextInt(26 - i);
      resultCode.add(alphabetToChange.remove(index));
    }
    return resultCode;
  }

  // produce an encoded String from the given String
  String encode(String source) {
    String result = "";
    for (int i = 0; i < source.length(); i++) {
      result = result + this.code.get(this.alphabet.indexOf(source.charAt(i)));
    }
    return result;
  }

  // produce a decoded String from the given String
  String decode(String code) {
    String result = "";
    for (int i = 0; i < code.length(); i++) {
      result = result + this.alphabet.get(this.code.indexOf(code.charAt(i)));
    }
    return result;
  }
}

class ExamplesSecretCode {
  
  // shift all letters 13 forward
  ArrayList<Character> rot13code = 
      new ArrayList<Character>(Arrays.asList(
          'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 
          'x', 'y', 'z', 'a', 'b', 'c', 'd', 'e', 'f', 
          'g', 'h', 'i', 'j', 'k', 'l', 'm'));
  // shift vowels two forward, consonants one back
  ArrayList<Character> v2cm1code = 
      new ArrayList<Character>(Arrays.asList(
          'i', 'c', 'd', 'f', 'o', 'g', 'h', 'j', 'u', 'k', 
          'l', 'm', 'n', 'p', 'a', 'q', 'r', 's', 't', 
          'v', 'e', 'w', 'x', 'y', 'z', 'b'));
  // the unaltered alphabet
  ArrayList<Character> alphabet = 
      new ArrayList<Character>(Arrays.asList(
          'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 
          'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 
          't', 'u', 'v', 'w', 'x', 'y', 'z'));
  // a list that is NOT a permutation of the alphabet
  ArrayList<Character> notAlphabet = 
      new ArrayList<Character>(Arrays.asList(
          'a', 'b', 'c', 'd', 'e', 'f', 'g', 'g', 'g', 'g', 
          'g', 'g', 'g', 'g', 'o', 'p', 'q', 'r', 's', 
          't', 'u', 'v', 'w', 'x', 'y', 'z'));
  //a list that is even less of a permutation of the alphabet
  ArrayList<Character> extraNotAlphabet = 
      new ArrayList<Character>(Arrays.asList('a'));

  PermutationCode randPerm1 = new PermutationCode();
  PermutationCode randPerm2 = new PermutationCode();
  PermutationCode rot13 = new PermutationCode(this.rot13code);
  PermutationCode v2cm1 = new PermutationCode(this.v2cm1code);
  
  // checks if a list of characters is a permutation of the alphabet
  boolean hasAlphabet(ArrayList<Character> chars) {
    if (chars.size() == 26) {
      ArrayList<Character> alphabet = 
          new ArrayList<Character>(Arrays.asList(
              'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 
              'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 
              't', 'u', 'v', 'w', 'x', 'y', 'z'));
      int count = 0;
      int curIndex = 0;
      for (Character a : alphabet) {
        while (curIndex < 26) {
          if (chars.get(curIndex) == a) {
            count += 1;
            curIndex = 26;
          } else {
            curIndex += 1;
          }
        }
        curIndex = 0;
      }
      if (count == 26) {
        return true;
      }
    }
    return false;
  }
  
  // checks if two lists are the same list
  <T> boolean listEquals(ArrayList<T> list1, ArrayList<T> list2, Comparator<T> comp) {
    if (list1.size() == list2.size()) {
      int count = 0;
      for (int i = 0; i < list1.size(); i++) {
        if (comp.compare(list1.get(i), list2.get(i)) == 0) {
          count += 1;
        }
      }
      if (count == list1.size()) {
        return true;
      }
    }
    return false;
  }

  // test for initEncode
  /* does so by checking that two instances of initEncode generate 
   * lists that are each permutations of the alphabet, but are not equal
   */
  void testInitEncode(Tester t) {
    t.checkExpect(this.hasAlphabet(this.randPerm1.code), true);
    t.checkExpect(this.hasAlphabet(this.randPerm2.code), true);
    t.checkExpect(this.listEquals(this.randPerm1.code, this.randPerm2.code, 
        (c1, c2) -> c1.compareTo(c2)), false);
  }
  
  // test hasAlphabet
  void testHasAlphabet(Tester t) {
    t.checkExpect(this.hasAlphabet(this.alphabet), true);
    t.checkExpect(this.hasAlphabet(this.rot13.code), true);
    t.checkExpect(this.hasAlphabet(this.notAlphabet), false);
    t.checkExpect(this.hasAlphabet(this.extraNotAlphabet), false);
  }
  
  // test listEquals
  void testListEquals(Tester t) {
    Comparator<Character> compChar = (c1, c2) -> c1.compareTo(c2);
    t.checkExpect(this.listEquals(this.alphabet, this.alphabet, compChar), true);
    t.checkExpect(this.listEquals(this.alphabet, this.notAlphabet, compChar), false);
    t.checkExpect(this.listEquals(this.alphabet, this.extraNotAlphabet, compChar), false);
  }
  
  // Method to test the encode method
  void testEncode(Tester t) {
    t.checkExpect(this.rot13.encode("hello"), "uryyb");
    t.checkExpect(this.rot13.encode("howisyourdaygoing"), "ubjvflbheqnltbvat");
    t.checkExpect(this.rot13.encode(""), "");
    t.checkExpect(this.v2cm1.encode("goldenexperience"), "hamfopoyqosuopdo");
    t.checkExpect(this.v2cm1.encode("aeioubcd"), "iouaecdf");
  }
  
  //Method to test the encode method
  void testDecode(Tester t) {
    t.checkExpect(this.rot13.decode("uryyb"), "hello");
    t.checkExpect(this.rot13.decode("ubjvflbheqnltbvat"), "howisyourdaygoing");
    t.checkExpect(this.v2cm1.decode("hamfopoyqosuopdo"), "goldenexperience");
    t.checkExpect(this.v2cm1.decode("iouaecdf"), "aeioubcd");
    t.checkExpect(this.v2cm1.decode(""), "");
  }
  
}





















