package expressions;

import tokens.FunctionToken;

/**
 * UnaryFunctionExpression stellt eine unäre Funktion dar. Die Funktion wird mit
 * ihrem Namen identifiziert. Siehe switch-case in eval() für eine Liste der
 * darstellbaren Funktionen. Die Funktion enthält die Unter-Expression argument
 * als Funktionsargument und stellt somit die Funktion name(argument) dar.
 */
public class UnaryFunctionExpression extends Expression {
	private final String name;
	private final Expression argument;

	/**
	 * Initialisiert die UnaryFunctionExpression.
	 * 
	 * @param f        Name der darzustellenden Funktion (z. B. sin, cos, sqrt) als
	 *                 Token. Siehe eval() für unterstützte Funktionen.
	 * @param argument Ausdruck, der als Funktionsargument genutzt wird.
	 */
	public UnaryFunctionExpression(FunctionToken funToken, Expression argument) {
		this.name = funToken.getName();
		this.argument = argument;
	}

	/**
	 * Wertet die FunctionExpression aus. Hierbei wird zunächst die eval-Methode auf
	 * das argument aufgerufen und dann in die angegeben Funktion eingesetzt.
	 *
	 * Nicht vergessen: default case mit einer IllegalargumentException mit
	 * aussagekräftiger Fehlermeldung!
	 *
	 * @param x Der für x einzusetzende Wert. Der Parameter x muss rekursiv an
	 *          eval-Methode der Unter-Expression argument weitergegeben werden.
	 * @return Der Wert der Funktion, ausgewertet an der Stelle, die dem
	 *         ausgewerteten Wert von argument entspricht.
	 * @throws IllegalargumentException wird im Fehlerfall geworfen.
	 */
	@Override
	public double eval(double x) {
		double ret = this.argument.eval(x);
		switch (this.name) {
		case "sin":
			ret = Math.sin(ret);
			break;
		case "cos":
			ret = Math.cos(ret);
			break;
		case "tan":
			ret = Math.tan(ret);
			break;
		case "log":
			ret = Math.log(ret);
			break;
		case "sqrt":
			ret = Math.sqrt(ret);
			break;
		case "asin":
			ret = Math.asin(ret);
			break;
		case "acos":
			ret = Math.acos(ret);
			break;
		case "atan":
			ret = Math.atan(ret);
			break;
		case "abs":
			ret = Math.abs(ret);
			break;
		case "exp":
			ret = Math.exp(ret);
			break;
		case "minus":
			ret = -ret;
			break;
		default:
			throw new IllegalArgumentException("Unkown function " + this.name);
		}
		return ret;
	}
}
