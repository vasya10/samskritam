package ch8.schemes

import java.util.List

/**
 * Harvard-Kyoto implementation for tokenizing
 * Must return a list of varna-s
 * 
 * @author vsrinivasan
 */
class HKScheme implements ScriptScheme {

  @Override
  public List tokenize(String word) {}

  @Override
  public List syllablize(String word) {}

  @Override
  public List toUnicodeList(String word) {}
  
  @Override
  public String toUnicode(String word) {}

}
