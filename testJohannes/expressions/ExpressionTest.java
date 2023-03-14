package expressions;

import org.junit.jupiter.api.*;

import expressions.*;
import tokens.FunctionToken;
import tokens.NumberToken;
import tokens.Token;
import tokens.TokenType;

public class ExpressionTest {

	@Test
	public void testMethod_shouldNotFail() {
		Assertions.assertTrue(true);
	}

	@Test
	public void testUnaryFunctionException() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			Expression thisdoesnotexist = new UnaryFunctionExpression(new FunctionToken("thisdoesnotexist"),
					new XExpression());
			thisdoesnotexist.eval(0);
		});
	}

	@Test
	public void testExpression() {
		final double epsilon = 1e-15;

		Expression e = new OperatorExpression(
				new Token(TokenType.TIMES), (new OperatorExpression(new Token(TokenType.DIVIDE),
						new NumberExpression(new NumberToken(1)), new NumberExpression(new NumberToken(2)))),
				new XExpression());

		Assertions.assertEquals(e.eval(-2), -1, epsilon);
		Assertions.assertEquals(e.eval(-1.5), -0.75, epsilon);
		Assertions.assertEquals(e.eval(-1), -0.5, epsilon);
		Assertions.assertEquals(e.eval(-0.5), -0.25, epsilon);
		Assertions.assertEquals(e.eval(0), 0, epsilon);
		Assertions.assertEquals(e.eval(0.5), 0.25, epsilon);
		Assertions.assertEquals(e.eval(1), 0.5, epsilon);
		Assertions.assertEquals(e.eval(1.5), 0.75, epsilon);
		Assertions.assertEquals(e.eval(2), 1, epsilon);

		Expression e2 = new UnaryFunctionExpression(new FunctionToken("sin"), new XExpression());
		Assertions.assertEquals(e2.eval(0), 0, epsilon);
		Assertions.assertEquals(e2.eval(Math.PI / 2), 1, epsilon);
		Assertions.assertEquals(e2.eval(Math.PI), 0, epsilon);
		Assertions.assertEquals(e2.eval((3. / 2.) * Math.PI), -1, epsilon);
		Assertions.assertEquals(e2.eval(2 * Math.PI), 0, epsilon);

		Expression e3 = new NumberExpression(new NumberToken(5));
		for (double x = -5; x < 5; x += 0.1) {
			Assertions.assertEquals(e3.eval(x), 5, epsilon);
		}

		Expression e4 = new UnaryFunctionExpression(new FunctionToken("sin"), new OperatorExpression(
				new Token(TokenType.POWER), new XExpression(), new NumberExpression(new NumberToken(2))));
		for (double x = -5; x < 5; x += 0.1) {
			Assertions.assertEquals(e4.eval(x), Math.sin(x * x), epsilon);
		}

		Expression e5 = new UnaryFunctionExpression(new FunctionToken("log"),
				new OperatorExpression(new Token(TokenType.PLUS), new NumberExpression(new NumberToken(3)),
						new OperatorExpression(new Token(TokenType.TIMES), new NumberExpression(new NumberToken(5)),
								new XExpression())));
		for (double x = -5; x < 5; x += 0.1) {
			Assertions.assertEquals(e5.eval(x), Math.log(3 + 5 * x), epsilon);
		}

		Expression e6 = new UnaryFunctionExpression(new FunctionToken("log"),
				new UnaryFunctionExpression(new FunctionToken("sin"), new XExpression()));
		for (double x = -5; x < 5; x += 0.1) {
			Assertions.assertEquals(e6.eval(x), Math.log(Math.sin(x)), epsilon);
		}
	}
}
