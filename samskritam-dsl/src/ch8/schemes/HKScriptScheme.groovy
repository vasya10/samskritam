package ch8.schemes

import java.util.List

/**
 * Harvard-Kyoto implementation for tokenizing varnas, syllablizing words
 * 
 * @author vsrinivasan
 */
class HKScriptScheme implements ScriptScheme {

  static def Definitions = new ConfigSlurper().parse(ch8.config.Definitions)
  static def Tokens = Definitions.Tokens
  static def Varnamala = Definitions.HKScript.varnamala
  
  @Override
  public List tokenize(def text) {
    def varnas = []
    def words = new StringTokenizer(text, Tokens.join(), true)
    words.each { varnas.addAll( (it in Tokens) ? it : tokenizeWord(it)) }
    varnas
  }

  protected List tokenizeWord(String word) {
    def varnas = []
    for (int i=0; i<word.length(); i++) {
      def (current, next) = [word[i], (i<word.length()-1) ? word[i+1] : '']
      if ((current+next) in Varnamala) {
        varnas << (current+next)
        i++
      } else if (current in Varnamala){
        varnas << current
      } else { 
        throw new Exception("[unknown varna ($current) in ($word). check your script.]")
      }
    }
    varnas
  }

  @Override
  public List syllablize(String text) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String toUnicode(String text) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String fromUnicode(Object text) {
    // TODO Auto-generated method stub
    return null;
  }
}
