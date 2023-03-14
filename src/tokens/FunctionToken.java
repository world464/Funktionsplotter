package tokens;

/**
 * FunctionToken beschreibt den Namen einer Funktion.
 */
public class FunctionToken extends Token {
	private final String name;

	/**
	 * Erzeugt eine Funktion mit dem angegebenen Namen.
	 * 
	 * @param name Name der Funktion, z. B. sin, sqrt, ...
	 */
	public FunctionToken(String name) {
		this.type = TokenType.FUNCTION;
		this.name = name;
	}

	/**
	 * getName gibt den Namen der Funktion zurück.
	 * 
	 * @return Name der Funktion.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * toString gibt den Namen der Funktion zurueck.
	 * 
	 * @return Name der Funktion.
	 */
	public String toString() {
		return name;
	}
}
