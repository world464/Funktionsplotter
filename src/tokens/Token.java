package tokens;

import java.util.ArrayList;

/**
 * Ein Token ist ein zusammengehoeriges Symbol, das Teil eines mathematischen
 * Ausdrucks ist. Es entspricht ungefaehr einem "Wort"; hier kann es entweder
 * Operatoren oder Operanden von Rechenoperationen darstellen. Eine einzelne
 * Zahl (nicht Ziffer) ist bspw. ein Token, die Variable x ist ein Token, ein
 * Operator ist ein Token, der Name einer Funktion ist ein Token.
 */
public class Token {

	protected TokenType type;

	/**
	 * Es soll kein Token ohne Type konstruiert werden, außer von Unterklassen.
	 */
	protected Token() {
	}

	/**
	 * Erzeugt einen Token mit dem angegebenen Typ.
	 * 
	 * @param type Tokentyp.
	 */
	public Token(TokenType type) {
		if (type == TokenType.NUMBER) {
			throw new IllegalArgumentException("NumberToken muss mit NumberToken(number) erzeugt werden");
		} else if (type == TokenType.FUNCTION) {
			throw new IllegalArgumentException("FunctionToken muss mit FunctionToken(name) erzeugt werden");
		}
		this.type = type;
	}

	/**
	 * Erzeugt einen Token aus dem gegebenen Zeichen, falls es einen gueltigen Token
	 * darstellt.
	 * 
	 * @param ch Zeichen, das zu einem Token gemacht werden soll.
	 */
	public Token(char ch) {
		switch (ch) {
		case '+':
			this.type = TokenType.PLUS;
			break;
		case '-':
			this.type = TokenType.MINUS;
			break;
		case '*':
			this.type = TokenType.TIMES;
			break;
		case '/':
			this.type = TokenType.DIVIDE;
			break;
		case '^':
			this.type = TokenType.POWER;
			break;
		case '(':
			this.type = TokenType.OPENING_PARENTHESIS;
			break;
		case ')':
			this.type = TokenType.CLOSING_PARENTHESIS;
			break;
		default:
			throw new IllegalArgumentException("Nicht bekannter Tokentype.");
		}
	}

	/**
	 * getType gibt den Typ dieses Tokens zurueck.
	 * 
	 * @return Tokentyp.
	 */
	public TokenType getType() {
		return this.type;
	}

	/**
	 * toString gibt den Token in lesbarer Form aus.
	 * 
	 * @return Token als String.
	 */
	public String toString() {
		switch (this.type) {
		case X:
			return "X";
		case PLUS:
			return "+";
		case MINUS:
			return "-";
		case TIMES:
			return "*";
		case DIVIDE:
			return "/";
		case POWER:
			return "^";
		case OPENING_PARENTHESIS:
			return "(";
		case CLOSING_PARENTHESIS:
			return ")";
		default:
			throw new IllegalArgumentException("Kein toString fuer tokenType" + this.type);
		}
	}

	/**
	 * ranking gibt die Prioritaet des Tokens zurueck, falls es sich um einen
	 * binaeren Operator handelt. ranking setzt die Regel "Hoch vor Punkt vor
	 * Strich" um. Diese Information benoetigt der Shunting-Yard-Algorithmus.
	 *
	 * Fuer Tokens, die keine binaere Operation darstellen, ist der Rueckgabewert
	 * unwichtig.
	 *
	 * @return Prioritaet (hoehere Zahl heißt hoehere Prioritaet).
	 */
	public int ranking() {
		switch (this.type) {
		case PLUS:
		case MINUS:
			return 1;
		case TIMES:
		case DIVIDE:
			return 2;
		case POWER:
			return 3;
		default:
			return -1;
		}
	}

	/**
	 * isLeftAssociative gibt true zurueck, wenn es sich um einen binaeren Operator
	 * handelt und er links-assoziativ ist (d. h. die Klammerung fuer a [op] b [op]
	 * c implizit als ((a [op] b) [op] c) gelesen werden soll. Diese Information
	 * benoetigt der Shunting-Yard-Algorithmus.
	 *
	 * Punkt und Strich sind linksassoziativ, Potenzierung ist rechtsassoziativ.
	 *
	 * Fuer Tokens, die keine binaeren Operatoren sind, ist der Rueckgabewert
	 * unwichtig.
	 *
	 * @return true, falls linksassoziativ.
	 */
	public boolean isLeftAssociative() {
		return this.type != TokenType.POWER;
	}

	/**
	 * teilt den String s, der einen mathematischen Ausdruck enthaelt, in ein Array
	 * von Tokens (Sinneinheiten, also z.B. Zahlen, einzelne Klammern, Operatoren,
	 * Funktionsnamen) auf.
	 * 
	 * @param s Mathematischer Ausdruck als String
	 * @return Array von Tokens (einzelne mathematische Symbole) in dem String.
	 */
	public static Token[] tokenize(String s) {
		String lastWord = "";
		ArrayList<Token> result = new ArrayList<>();
		int lastOperatorPosition = -1;
		for (int i = 0; i < s.length(); i++) {
			char b = s.charAt(i);
			switch (b) {
			case '+':
			case '-':
			case '*':
			case '/':
			case '^':
			case '(':
			case ')':
				lastWord = s.substring(lastOperatorPosition + 1, i).trim();
				lastOperatorPosition = i;
				if (!("".equals(lastWord))) {
					result.add(fromString(lastWord));
				}
				result.add(new Token(b));
			}

		}
		lastWord = s.substring(lastOperatorPosition + 1).trim().toLowerCase();
		if (!lastWord.equals("")) {
			result.add(fromString(lastWord));
		}
		return result.toArray(new Token[0]);
	}

	/**
	 * Wandelt ein String in ein Token um.
	 * 
	 * @param lastWord ist der String
	 * @return ein Token zu dem der String passt
	 */
	private static Token fromString(String lastWord) {
		try {
			return new NumberToken(Double.parseDouble(lastWord));
		} catch (NumberFormatException e) {
			switch (lastWord) {
			case "x":
				return new Token(TokenType.X);
			case "e":
				return new NumberToken(Math.E);
			case "pi":
				return new NumberToken(Math.PI);
			default:
				return new FunctionToken(lastWord);
			}
		}
	}
}
