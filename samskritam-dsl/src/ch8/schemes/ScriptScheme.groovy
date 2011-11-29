package ch8.schemes

interface ScriptScheme {
	public List tokenize(String word)
  public List syllablize(String word)
  public List toUnicodeList(String word)
  public String toUnicode(String word)
}
