package expressions;

import java.util.EmptyStackException;
import java.util.Stack;

import shuntingyard.ShuntingYard;
import tokens.FunctionToken;
import tokens.NumberToken;
import tokens.Token;

/**
 * Expression stellt einen Ausdruck dar, der mit der eval()-Methode ausgewertet
 * werden kann. Der Ausdruck kann eine Variable x enthalten, die als Argument an
 * eval übergeben wird.
 */
public abstract class Expression {
	protected Expression() {
	}

	/**
	 * eval wertet den Ausdruck aus. Dabei wird der übergebene Wert für die Variable
	 * x eingesetzt.
	 *
	 * @param x Der für x einzusetzende Wert. Der Parameter x sollte stets übergeben
	 *          werden, muss aber von Tochterklassen nicht benutzt werden, falls der
	 *          dargestellte Ausdruck kein x enthält.
	 * @return Das Ergebnis des Ausdrucks mit dem ggf. eingesetzten x.
	 */
	public abstract double eval(double x);

	/**
	 * parseRPN liest ein Array aus Tokens, die in umgekehrter polnischer Notation
	 * sortiert sein müssen, und konstruiert daraus einen Syntaxbaum aus
	 * Expression-Tochterklassen.
	 *
	 * Die Konstruktion des Syntaxbaumes funktioniert so: * Wenn in tokens eine Zahl
	 * oder ein x angetroffen wird, wird eine NumberExpression/XExpression erzeugt
	 * und auf den Stack gelegt. * Wenn in tokens ein FunctionToken angetroffen
	 * wird, wird eine Expression vom Stack genommen, als Funktionsargument genutzt,
	 * und eine UnaryFunctionExpression mit diesem Argument erzeugt und auf den
	 * Stack gelegt. * Wenn in tokens ein Rechen-Operator angetroffen wird, werden
	 * zwei Expressions vom Stack genommen, als linke und rechte Operanden genutzt
	 * und eine OperatorExpression mit diesen Operanden erzeugt und auf den Stack
	 * gelegt. Durch die Wiederverwendung der Expressions auf dem Stack entsteht auf
	 * diese Weise nach und nach eine Baumstruktur.
	 *
	 * @param tokens Tokens, die einen mathematischen Ausdruck in umgekehrter
	 *               polnischer Notation darstellen.
	 * @return Eine Expression, die rekursiv den kompletten Ausdruck darstellt.
	 * @throws IllegalArgumentException wird im Fehlerfall geworfen.
	 */
	public static Expression parseRPN(Token[] tokens) throws IllegalArgumentException {
		Stack<Expression> stack = new Stack<>();
		for (Token token : tokens) {
			switch (token.getType()) {
			case NUMBER:
				stack.push(new NumberExpression((NumberToken) token));
				break;
			case X:
				stack.push(new XExpression());
				break;
			case FUNCTION:
				try {
					stack.push(new UnaryFunctionExpression((FunctionToken) token, stack.pop()));
				} catch (EmptyStackException e) {
					System.out.println(
							"Du hast" + ((FunctionToken) token).getName() + " eingegeben. Versuch's nochmal :)");
					throw new IllegalArgumentException("Ungueltige Funktion");
				}
				break;
			case DIVIDE:
			case TIMES:
			case MINUS:
			case PLUS:
			case POWER:
				Expression rightExp = stack.pop();// Reihenfolge beachten
				Expression leftExp = stack.pop();
				stack.push(new OperatorExpression(token, leftExp, rightExp));
				break;
			default:
				throw new IllegalArgumentException("Unbekantes Token");
			}
		}
		Expression ret = stack.pop();
		if (!stack.empty()) {
			throw new IllegalArgumentException("Ungueltiger UPN-Ausdruck");
		}
		return ret;

	}

	/**
	 * parseInfixString liest einen String, der einen mathematischen Ausdruck in
	 * Infix-Schreibweise darstellt, und erzeugt einen Syntaxbaum daraus.
	 *
	 * Hierfuer wird der String zunaechst mit tokenize in Tokens zerlegt, dann mit
	 * dem Shunting-Yard-Algorithmus in umgekehrte polnische Notation konvertiert,
	 * und dann mit parseRPN in einen Syntaxbaum umgewandelt.
	 * 
	 * @param s Ausdruck, der gelesen werden soll.
	 * @return Ausdruck als Syntaxbaum.
	 */
	public static Expression parseInfixString(String s) {
		if (s.startsWith("-")) {
			s = s.replaceFirst("-", "0-");
		}
		if (s.endsWith("-")) {
			s = s + "0";
		}
		s = s.replaceAll("\\(-", "(0-");
		s = s.replaceAll("-\\)", "-0)");
		return parseRPN(ShuntingYard.convertToRPN(Token.tokenize(s)));
	}
}