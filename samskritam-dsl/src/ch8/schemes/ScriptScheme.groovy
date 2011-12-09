package ch8.schemes

/**
 * Implement this interface for this program to use the scheme as default scheme
 * Right now, only NativeScript is supported. All other scripts are converted to NativeScript before operations are carried out
 * In order for the whole program to use a different scheme, Samjna.groovy must be modified to not use any hardcoded varnas
 * 
 * @author vsrinivasan
 */
interface ScriptScheme {
	public List tokenize(def text)
  public List syllablize(String text)
  public String toUnicode(String text)
  public String fromUnicode(def text)
}
