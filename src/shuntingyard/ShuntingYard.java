package shuntingyard;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Stack;

import tokens.Token;
import tokens.TokenType;

/**
 * ShuntingYard stellt die Methode convertToRPN() bereit, die ein Array von
 * Tokens in Infix-Notation mithilfe des Shunting-Yard-Algorithmus von Dijkstra
 * in ein Array von Tokens in umgekehrter polnischer Notation konvertiert.
 */
public class ShuntingYard {
	private static final ArrayList<Token> result = new ArrayList<>();
	private static final Stack<Token> stack = new Stack<>();

	/**
	 * convertToRPN wandelt das Array tokens, das einen mathematischen Ausdruck in
	 * Infix-Schreibweise darstellt, in ein Array von Tokens um, das denselben
	 * Ausdruck in umgekehrter polnischer Notation darstellt.
	 *
	 * Hierfuer wird der Shunting-Yard-Algorithmus genutzt, siehe
	 * https://en.m.wikipedia.org/wiki/Shunting-yard_algorithm
	 *
	 * @param tokens Tokens in Infix-Schreibweise.
	 * @return Tokens in umgekehrter polnischer Notation.
	 */
	public static Token[] convertToRPN(Token[] tokens) {
		result.clear();
		stack.clear();

		for (Token token : tokens) {
			switch (token.getType()) {
			case X:
			case NUMBER:
				result.add(token);
				break;
			case PLUS:
			case MINUS:
			case TIMES:
			case DIVIDE:
			case POWER:
				evaluateOperator(token);
				break;
			case FUNCTION:
			case OPENING_PARENTHESIS:
				stack.push(token);
				break;
			case CLOSING_PARENTHESIS:
				evaluateClosingParenthesis();
				break;
			}
		}
		sortToRPN();

		// Hier wird die result Liste in ein Array vom Typ Token umgewandelt
		return result.toArray(new Token[0]);
	}

	/**
	 * Für die Operatoren +, -, *, /, und ^ verschiebt die Funktion unter gewissen
	 * Bedingungen die Zeichen vom Stack auf dem result Array.
	 * 
	 * @param token Das aktuelle Token, welches ausgewählt wurde.
	 */
	private static void evaluateOperator(Token token) {
		while (!stack.empty() && (stack.peek().getType() != TokenType.OPENING_PARENTHESIS
				&& (stack.peek().ranking() > token.ranking())
				|| ((stack.peek().ranking() == token.ranking()) && token.isLeftAssociative()))) {
			result.add(stack.pop());
		}
		stack.push(token);
	}

	/**
	 * Wertet den Ausdruck aus bei einer geschlossenen Klammer aus.
	 */
	private static void evaluateClosingParenthesis() {
		Token t;
		do {
			try {
				t = stack.pop();
			} catch (EmptyStackException e) {
				throw new IllegalStateException("Mismatched parentheses", e);
			}
			if (t.getType() != TokenType.OPENING_PARENTHESIS) {
				result.add(t);
			}
		} while (t.getType() != TokenType.OPENING_PARENTHESIS);
		if (stack.peek().getType() == TokenType.FUNCTION) {
			result.add(stack.pop());
		}
	}

	/*
	 * Falls noch Zeichen auf dem Stack übrig sind, werden in dem result Array
	 * abgelegt.
	 */
	private static void sortToRPN() {
		while (true) {
			Token t;
			try {
				t = stack.pop();
			} catch (EmptyStackException e) {
				break;
			}
			if (t.getType() == TokenType.OPENING_PARENTHESIS) {
				throw new IllegalStateException("Mismatched parentheses");
			}
			result.add(t);
		}
	}
}
