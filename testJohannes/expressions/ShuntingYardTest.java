package expressions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import expressions.*;
import shuntingyard.ShuntingYard;
import tokens.FunctionToken;
import tokens.NumberToken;
import tokens.Token;
import tokens.TokenType;

public class ShuntingYardTest {

	@Test
	public void testMethod_shouldNotFail() {
		Assertions.assertTrue(true);
	}

	@Test
	public void testShuntingYard() {
		final double epsilon = 1e-15;

		Expression e = Expression
				.parseRPN(ShuntingYard.convertToRPN(new Token[] { new NumberToken(1.), new Token(TokenType.DIVIDE),
						new NumberToken(2.), new Token(TokenType.TIMES), new Token(TokenType.X), }));

		Assertions.assertEquals(e.eval(-2), -1, epsilon);
		Assertions.assertEquals(e.eval(-1.5), -0.75, epsilon);
		Assertions.assertEquals(e.eval(-1), -0.5, epsilon);
		Assertions.assertEquals(e.eval(-0.5), -0.25, epsilon);
		Assertions.assertEquals(e.eval(0), 0, epsilon);
		Assertions.assertEquals(e.eval(0.5), 0.25, epsilon);
		Assertions.assertEquals(e.eval(1), 0.5, epsilon);
		Assertions.assertEquals(e.eval(1.5), 0.75, epsilon);
		Assertions.assertEquals(e.eval(2), 1, epsilon);

		Expression e2 = Expression.parseRPN(ShuntingYard
				.convertToRPN(new Token[] { new FunctionToken("sin"), new Token(TokenType.OPENING_PARENTHESIS),
						new Token(TokenType.X), new Token(TokenType.CLOSING_PARENTHESIS), }));
		Assertions.assertEquals(e2.eval(0), 0, epsilon);
		Assertions.assertEquals(e2.eval(Math.PI / 2), 1, epsilon);
		Assertions.assertEquals(e2.eval(Math.PI), 0, epsilon);
		Assertions.assertEquals(e2.eval((3. / 2.) * Math.PI), -1, epsilon);
		Assertions.assertEquals(e2.eval(2 * Math.PI), 0, epsilon);

		Expression e3 = Expression.parseRPN(ShuntingYard.convertToRPN(new Token[] { new NumberToken(5) }));
		for (double x = -5; x < 5; x += 0.1) {
			Assertions.assertEquals(e3.eval(x), 5, epsilon);
		}

		Expression e4 = Expression.parseRPN(ShuntingYard.convertToRPN(new Token[] { new FunctionToken("sin"),
				new Token(TokenType.OPENING_PARENTHESIS), new Token(TokenType.X), new Token(TokenType.POWER),
				new NumberToken(2), new Token(TokenType.CLOSING_PARENTHESIS), }));
		for (double x = -5; x < 5; x += 0.1) {
			Assertions.assertEquals(e4.eval(x), Math.sin(x * x), epsilon);
		}

		Expression e5 = Expression.parseRPN(ShuntingYard
				.convertToRPN(new Token[] { new FunctionToken("log"), new Token(TokenType.OPENING_PARENTHESIS),
						new NumberToken(3), new Token(TokenType.PLUS), new NumberToken(5), new Token(TokenType.TIMES),
						new Token(TokenType.X), new Token(TokenType.CLOSING_PARENTHESIS), }));
		for (double x = -5; x < 5; x += 0.1) {
			Assertions.assertEquals(e5.eval(x), Math.log(3 + 5 * x), epsilon);
		}

		Expression e6 = Expression.parseRPN(ShuntingYard
				.convertToRPN(new Token[] { new FunctionToken("log"), new Token(TokenType.OPENING_PARENTHESIS),
						new FunctionToken("sin"), new Token(TokenType.OPENING_PARENTHESIS), new Token(TokenType.X),
						new Token(TokenType.CLOSING_PARENTHESIS), new Token(TokenType.CLOSING_PARENTHESIS), }));
		for (double x = -5; x < 5; x += 0.1) {
			Assertions.assertEquals(e6.eval(x), Math.log(Math.sin(x)), epsilon);
		}
	}
}
