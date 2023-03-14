package expressions;

import tokens.Token;

/**
 * OperatorExpression stellt die Verknüpfung zweier Expressions mit einem
 * binären Operator (+ - * / ^) als Expression dar. Dies kann genutzt werden, um
 * einen binären Knoten in einem Syntaxbaum darzustellen. Die eval()-Methode
 * ruft die eval()-Methoden der beiden Kind-Expressions auf und verknüpft die
 * Ergebnisse.
 */
public class OperatorExpression extends Expression {
	private Token op;
	private Expression left;
	private Expression right;

	/**
	 * Erstellt eine OperatorExpression.
	 *
	 * Ergänze einen default case um eine IllegalArgumentException mit
	 * aussagekräftiger Fehlermeldung!
	 *
	 * @param op    Art der Verknüpfung (+ - * / ^).
	 * @param left  Expression auf der linken Seite des Operators.
	 * @param right Expression auf der linken Seite des Operators.
	 * @throws IllegalArgumentException wird im Fehlerfall geworfen.
	 */
	public OperatorExpression(Token op, Expression left, Expression right) {
		switch (op.getType()) {
		case PLUS:
		case MINUS:
		case TIMES:
		case DIVIDE:
		case POWER:
			break;
		default:
			throw new IllegalArgumentException("OperatorExpression can only be + - * / ^ ");
		}

		this.op = op;
		this.left = left;
		this.right = right;
	}

	/**
	 * eval wertet die OperatorExpression aus. Hierzu werden erst die
	 * eval()-Methoden von left und right aufgerufen, und dann mit der passenden
	 * Rechenoperation verknüpft.
	 *
	 * @param x Der für x einzusetzende Wert. Der Parameter x muss rekursiv an die
	 *          eval-Methode der Unter-Expressions left und right übergeben werden.
	 * @return Ergebnis der Verknüpfung "left (op) right".
	 */
	@Override
	public double eval(double x) {
		double left = this.left.eval(x);
		double right = this.right.eval(x);

		double ret = 0;
		switch (op.getType()) {
		case PLUS:
			ret = left + right;
			break;
		case MINUS:
			ret = left - right;
			break;
		case TIMES:
			ret = left * right;
			break;
		case DIVIDE:
			ret = left / right;
			break;
		case POWER:
			ret = Math.pow(left, right);
			break;
		default:
			throw new IllegalStateException("OperatorExpression with unkown opertor");
		}
		return ret;
	}
}
