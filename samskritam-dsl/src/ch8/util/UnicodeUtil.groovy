package ch8.util

class UnicodeUtil {
  
  // Returns hex String representation of byte b
  static String byteToHex(byte b) {
    char[] hexDigit = [ '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' ];
    char[] array = [ hexDigit[(b >> 4) & 0x0f], hexDigit[b & 0x0f] ];
    return new String(array);
  }
  
  // Returns hex String representation of char c
  static String charToHex(char c) {
    byte hi = (byte) (c >>> 8);
    byte lo = (byte) (c & 0xff);
    return byteToHex(hi) + byteToHex(lo);
  }
  
  static String stringToHex(String text) {
    String result = ''
    String prefix = '\\' + 'u'
    text = text - '\ufeff'
    for (int i=0;i<text.length();i++) {
      result += prefix + UnicodeUtil.charToHex(text.charAt(i))
    }
  }
  
  static void writeToFile(def f, def text) {
    if (f instanceof String) {
      f = new File(f)
    }
    f.withWriter("UTF-8")  { writer-> writer.write(text) }
  }
}
