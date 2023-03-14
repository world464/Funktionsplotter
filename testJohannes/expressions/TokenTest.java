package expressions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import expressions.*;
import tokens.FunctionToken;
import tokens.NumberToken;
import tokens.Token;
import tokens.TokenType;

import java.util.EmptyStackException;

public class TokenTest {

	@Test
	public void testMethod_shouldNotFail() {
		Assertions.assertTrue(true);
	}

	@Test
	public void testParseRPNException() {
		Assertions.assertThrows(EmptyStackException.class, () -> {
			Expression.parseRPN(new Token[] { new NumberToken(1), new NumberToken(2), new Token(TokenType.DIVIDE),
					new Token(TokenType.TIMES) });
		});

		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			Expression.parseRPN(new Token[] { new NumberToken(1), new NumberToken(1), new NumberToken(1),
					new Token(TokenType.DIVIDE), });
		});
	}

	@Test
	public void testToken() {
		final double epsilon = 1e-15;

		Expression e = Expression.parseRPN(new Token[] { new NumberToken(1), new NumberToken(2),
				new Token(TokenType.DIVIDE), new Token(TokenType.X), new Token(TokenType.TIMES) });

		Assertions.assertEquals(e.eval(-2), -1, epsilon);
		Assertions.assertEquals(e.eval(-1.5), -0.75, epsilon);
		Assertions.assertEquals(e.eval(-1), -0.5, epsilon);
		Assertions.assertEquals(e.eval(-0.5), -0.25, epsilon);
		Assertions.assertEquals(e.eval(0), 0, epsilon);
		Assertions.assertEquals(e.eval(0.5), 0.25, epsilon);
		Assertions.assertEquals(e.eval(1), 0.5, epsilon);
		Assertions.assertEquals(e.eval(1.5), 0.75, epsilon);
		Assertions.assertEquals(e.eval(2), 1, epsilon);

		Expression e2 = Expression.parseRPN(new Token[] { new Token(TokenType.X), new FunctionToken("sin") });
		Assertions.assertEquals(e2.eval(0), 0, epsilon);
		Assertions.assertEquals(e2.eval(Math.PI / 2), 1, epsilon);
		Assertions.assertEquals(e2.eval(Math.PI), 0, epsilon);
		Assertions.assertEquals(e2.eval((3. / 2.) * Math.PI), -1, epsilon);
		Assertions.assertEquals(e2.eval(2 * Math.PI), 0, epsilon);

		Expression e3 = Expression.parseRPN(new Token[] { new NumberToken(5) });
		for (double x = -5; x < 5; x += 0.1) {
			Assertions.assertEquals(e3.eval(x), 5, epsilon);
		}

		Expression e4 = Expression.parseRPN(new Token[] { new Token(TokenType.X), new NumberToken(2),
				new Token(TokenType.POWER), new FunctionToken("sin") });
		for (double x = -5; x < 5; x += 0.1) {
			Assertions.assertEquals(e4.eval(x), Math.sin(x * x), epsilon);
		}

		Expression e5 = Expression.parseRPN(new Token[] { new NumberToken(5), new Token(TokenType.X),
				new Token(TokenType.TIMES), new NumberToken(3), new Token(TokenType.PLUS), new FunctionToken("log") });
		for (double x = -5; x < 5; x += 0.1) {
			Assertions.assertEquals(e5.eval(x), Math.log(3 + 5 * x), epsilon);
		}

		Expression e6 = Expression
				.parseRPN(new Token[] { new Token(TokenType.X), new FunctionToken("sin"), new FunctionToken("log") });
		for (double x = -5; x < 5; x += 0.1) {
			Assertions.assertEquals(e6.eval(x), Math.log(Math.sin(x)), epsilon);
		}
	}

	@Test
	public void testTokenize() {
		final double epsilon = 1e-15;

		Expression e = Expression.parseInfixString("1/2 * x");

		Assertions.assertEquals(e.eval(-2), -1, epsilon);
		Assertions.assertEquals(e.eval(-1.5), -0.75, epsilon);
		Assertions.assertEquals(e.eval(-1), -0.5, epsilon);
		Assertions.assertEquals(e.eval(-0.5), -0.25, epsilon);
		Assertions.assertEquals(e.eval(0), 0, epsilon);
		Assertions.assertEquals(e.eval(0.5), 0.25, epsilon);
		Assertions.assertEquals(e.eval(1), 0.5, epsilon);
		Assertions.assertEquals(e.eval(1.5), 0.75, epsilon);
		Assertions.assertEquals(e.eval(2), 1, epsilon);

		Expression e2 = Expression.parseInfixString("sin(x)");
		Assertions.assertEquals(e2.eval(0), 0, epsilon);
		Assertions.assertEquals(e2.eval(Math.PI / 2), 1, epsilon);
		Assertions.assertEquals(e2.eval(Math.PI), 0, epsilon);
		Assertions.assertEquals(e2.eval((3. / 2.) * Math.PI), -1, epsilon);
		Assertions.assertEquals(e2.eval(2 * Math.PI), 0, epsilon);

		Expression e3 = Expression.parseInfixString("5");
		for (double x = -5; x < 5; x += 0.1) {
			Assertions.assertEquals(e3.eval(x), 5, epsilon);
		}

		Expression e4 = Expression.parseInfixString("sin(x^2)");
		for (double x = -5; x < 5; x += 0.1) {
			Assertions.assertEquals(e4.eval(x), Math.sin(x * x), epsilon);
		}

		Expression e5 = Expression.parseInfixString("log(3+5*x)");
		for (double x = -5; x < 5; x += 0.1) {
			Assertions.assertEquals(e5.eval(x), Math.log(3 + 5 * x), epsilon);
		}

		Expression e6 = Expression.parseInfixString("log(sin(x))");
		for (double x = -5; x < 5; x += 0.1) {
			Assertions.assertEquals(e6.eval(x), Math.log(Math.sin(x)), epsilon);
		}
	}

	@Test
	public void testUnaryMinus() {
		final double epsilon = 1e-15;

		Expression e1 = Expression.parseInfixString("log(x)");
		Assertions.assertEquals(e1.eval(1), 0, epsilon);
		Assertions.assertEquals(e1.eval(Math.E), 1, epsilon);
		Assertions.assertEquals(e1.eval(Math.E * Math.E), 2, epsilon);

		Expression e2 = Expression.parseInfixString("log(-1*x)");
		Assertions.assertEquals(e2.eval(-1), 0, epsilon);
		Assertions.assertEquals(e2.eval(-Math.E), 1, epsilon);
		Assertions.assertEquals(e2.eval(-Math.E * Math.E), 2, epsilon);

		Expression e3 = Expression.parseInfixString("-1*log(x)");
		Assertions.assertEquals(e3.eval(1), 0, epsilon);
		Assertions.assertEquals(e3.eval(Math.E), -1, epsilon);
		Assertions.assertEquals(e3.eval(Math.E * Math.E), -2, epsilon);

		Expression e4 = Expression.parseInfixString("-1*log(-1*x)");
		Assertions.assertEquals(e4.eval(-1), 0, epsilon);
		Assertions.assertEquals(e4.eval(-Math.E), -1, epsilon);
		Assertions.assertEquals(e4.eval(-Math.E * Math.E), -2, epsilon);

	}
}
