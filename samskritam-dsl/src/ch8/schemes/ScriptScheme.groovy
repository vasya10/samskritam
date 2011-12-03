package ch8.schemes

interface ScriptScheme {
	public List tokenize(String word)
  public List syllablize(String word)
  public String unicodize(String word)
}
