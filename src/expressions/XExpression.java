package expressions;

/**
 * XExpression stellt die Variable x als Expression dar. Diese kann im
 * Syntaxbaum als Blattknoten genutzt werden.
 */
public class XExpression extends Expression {

	/**
	 * eval wertet die XExpression aus. Dies entspricht der unveränderten Rückgabe
	 * des Parameters x.
	 *
	 * @param x Der für x einzusetzende Wert.
	 * @return x.
	 */
	@Override
	public double eval(double x) {
		return x;
	}
}
