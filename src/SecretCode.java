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
        ArrayList<Character> alphabetToChange = this.alphabet;
        for(int i = 0; i < 26; i++) {
          int index = this.rand.nextInt(26-i);
          resultCode.add(alphabetToChange.remove(index));
        }
        return resultCode;
    }

    // produce an encoded String from the given String
    String encode(String source) {
      String result = "";
      for(int i = 0; i < source.length(); i++) {
        result = result + this.code.get(this.alphabet.indexOf(source.charAt(i)));
      }
      return result;
    }

    // produce a decoded String from the given String
    String decode(String code) {
      String result = "";
      for(int i = 0; i < code.length(); i++) {
        result = result + this.alphabet.get(this.code.indexOf(code.charAt(i)));
      }
      return result;
    }
}

class ExamplesSecretCode {
  ArrayList<Character> rot13code = 
      new ArrayList<Character>(Arrays.asList(
                  'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 
                  'x', 'y', 'z', 'a', 'b', 'c', 'd', 'e', 'f', 
                  'g', 'h', 'i', 'j', 'k', 'l', 'm'));
  
  PermutationCode rot13 = new PermutationCode(this.rot13code);
  
  // Method to test the encode method
  void testEncode(Tester t) {
    t.checkExpect(rot13.encode("hello"), "uryyb");
    t.checkExpect(rot13.encode("howisyourdaygoing"), "ubjvflbheqnltbvat");
  }
}