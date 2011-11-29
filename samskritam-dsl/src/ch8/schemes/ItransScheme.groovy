package ch8.schemes

import java.util.List

/**
 * ITRANS Tokenizer
 * 
 * @author vsrinivasan
 *
 */
class ItransScheme implements ScriptScheme {

  @Override
  public List tokenize(String word) {}

  @Override
  public List syllablize(String word) {}
  
  @Override
  public List toUnicodeList(String word) {}
  
  @Override
  public String toUnicode(String word) {}

}
