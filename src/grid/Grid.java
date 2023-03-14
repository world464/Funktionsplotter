package grid;

import javax.swing.*;

import expressions.*;
import shuntingyard.ShuntingYard;
import tokens.*;

import java.awt.*;

public class Grid extends JFrame {
	public static int width = 1024;
	public static int height = 786;

	public static double xMax = 10.0;
	public static double xMin = -10.0;
	public static double yMax = 10.0;
	public static double yMin = -10.0;
	public static double steps = 0.1;
	public final static double MID = 0.0;

	/**
	 * Konstruktor aus der Uebung
	 */
	public Grid() {
		super();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(width, height);
		this.setTitle("Plotter");
		this.setVisible(true);
	}

	public static void main(String[] args) {
		new Grid();
	}

	/**
	 * currentWidth konvertiert eine Zahl xValue in eine Pixelkoordinate, indem es
	 * das Intervall zwischen xMin und xMax auf die Pixel width abbildet.
	 * 
	 * @param xValue gegebene x Wert
	 * @return der Pixelwert in x Richtung
	 */
	public static int currentWidth(double xValue) {
		// (xValue - Grid.xMin) verschiebt die x-Achse in den positiven Bereich, um 10
		// nach
		// rechts. xValue + 10
		return (int) ((xValue - Grid.xMin) * Grid.width / (Grid.xMax - Grid.xMin));
	}

	/**
	 * currentHeight konvertiert eine Zahl yValue in eine Pixelkoordinate, indem es
	 * das Intervall zwischen yMin und yMax auf die Pixel height abbildet.
	 * 
	 * @param yValue gegebene y Wert
	 * @return der Pixelwert in y Richtung
	 */
	public static int currentHeight(double yValue) {
		// yValue-minY verschiebt das Koordinatensystem um 10 nach unten; yValue + 10
		return Grid.height - (int) ((yValue - Grid.yMin) * Grid.height / (Grid.yMax - Grid.yMin));
	}

	/**
	 * drawLine nutzt die convert-Funktionen, um einen Koordinatenpunkt zu zeichnen.
	 */
	public void drawLine(Graphics g, double x1, double y1, double x2, double y2) {
		g.drawLine(Grid.currentWidth(x1), Grid.currentHeight(y1), Grid.currentWidth(x2), Grid.currentHeight(y2));
	}

	/**
	 * coordinates zeichnet ein Koordinatenkreuz
	 */
	public void coordinates(Graphics g) {
		g.setColor(Color.RED);
		drawLine(g, Grid.MID, Grid.yMin, Grid.MID, Grid.yMax);// vertical
		drawLine(g, Grid.xMin, Grid.MID, Grid.xMax, Grid.MID);// horizontal
	}

	/**
	 * coordinates zeichnet ein Koordinatensystem
	 */
	public void coordinateSystem(Graphics g) {
		int counter = 0;
		for/* vertical lines */ (double xValue = Grid.MID; xValue <= Grid.xMax; xValue += 0.2, counter++) {
			if (counter % 5 == 0) {
				g.setColor(Color.GRAY);
			} else {
				g.setColor(Color.LIGHT_GRAY);
			}
			drawLine(g, xValue, Grid.yMin, xValue, Grid.yMax);
			drawLine(g, -xValue, Grid.yMin, -xValue, Grid.yMax);
		}
		counter = 0;
		for/* horizontal lines */ (double yValue = Grid.MID; yValue <= Grid.yMax; yValue += 0.2, counter++) {
			if (counter % 5 == 0) {
				g.setColor(Color.GRAY);
			} else {
				g.setColor(Color.LIGHT_GRAY);
			}
			drawLine(g, Grid.xMin, yValue, Grid.xMax, yValue);
			drawLine(g, Grid.xMin, -yValue, Grid.xMax, -yValue);
		}
	}

	/**
	 * labels zeichnet Achsenbeschriftungen bei ganzen Zahlen.
	 */
	public void labels(Graphics g) {// TODO: constant for the shift
		g.setColor(Color.BLACK);
		g.setFont(new Font("Helvetica", Font.PLAIN, 12));
		for/* x-axis */ (int xLabel = (int) Grid.xMin; xLabel <= (int) Grid.xMax; xLabel++) {
			if (xLabel == 0) {
				continue;
			}
			g.drawString(Integer.valueOf(xLabel).toString(), currentWidth(xLabel), currentHeight(-0.4));
		}

		for/* y-axis */ (int yLabel = (int) Grid.yMin; yLabel <= (int) Grid.xMax; yLabel++) {
			if (yLabel == 0) {
				continue;
			}
			g.drawString(Integer.valueOf(yLabel).toString(), currentWidth(-0.4), currentHeight(yLabel));
		}
	}

	/**
	 * plot zeichnet die durch die Expression e angegebene Funktion in der
	 * angegebenen Farbe in g.
	 */
	public void plot(Graphics g, Expression e, Color c) {
		g.setColor(c);
		double lastX, lastY;// lastX, lastY old values
		lastX = Grid.xMin;
		lastY = e.eval(lastX);

		for (double x = Grid.xMin + steps; x <= Grid.xMax; x += Grid.steps) {
			double y = e.eval(x);
			drawLine(g, lastX, lastY, x, y);

			lastX = x;
			lastY = y;
		}

	}

	public static void setMinX(double x) {
		Grid.xMin = x;
	}

	public static void setMinY(double y) {
		Grid.yMin = y;
	}

	public static void setMaxX(double x) {
		Grid.xMax = x;
	}

	public static void setMaxY(double y) {
		Grid.yMax = y;
	}

	/**
	 * 
	 * @param minY
	 * @param maxY
	 * @param minX
	 * @param maxX
	 */
	public static void shows(double minY, double maxY, double minX, double maxX) {
		Grid.setMinX(minX);
		Grid.setMaxX(maxX);
		Grid.setMinY(minY);
		Grid.setMaxY(maxY);
	}

	/**
	 * paint-Methode wie in der Vorlesung
	 */
	public void paint(Graphics g) {
		coordinateSystem(g);
		coordinates(g);
		labels(g);

		// Part1
//			plot(g, new HardcodedLim(), Color.PINK);
//			plot(g, new Hardcodedx2(), Color.PINK);
//			plot(g, new HardcodedExponential(), Color.ORANGE);
//			plot(g, new HardcodedCos(), Color.CYAN);
//			plot(g, new HardcodedSin(), Color.RED);

//			Expression e2 = new OperatorExpression(OperatorType.PLUS, 
//					new OperatorExpression(OperatorType.TIMES, new NumberExpression(5.0), new XExpression()), 
//					new OperatorExpression(OperatorType.PLUS, new UnaryFunctionExpression("log", new XExpression()), 
//							new OperatorExpression(OperatorType.MINUS, new NumberExpression(3.0), 
//								new NumberExpression(5.0))
//							)
//					);
//			plot(g, e1, Color.MAGENTA);
//			plot(g, e2, Color.BLUE);

		// Part 2
		// sin(x)
//			Expression bsp1 = new UnaryFunctionExpression("sin", new XExpression());
//			//log(5 * (3 + x))
//			Expression bsp2 = new UnaryFunctionExpression("log", new OperatorExpression(OperatorType.TIMES, 
//					new NumberExpression(5.0), new OperatorExpression(OperatorType.PLUS, new NumberExpression(3.0), 
//							new XExpression())));
//			//x^2 + 3
//			Expression bsp3 = new OperatorExpression(OperatorType.PLUS, new OperatorExpression(OperatorType.POWER,
//					new XExpression(), new NumberExpression(2.0)), new NumberExpression(3.0));
//			
//			plot(g, bsp1, Color.CYAN);
//			plot(g, bsp2, Color.RED);
//			plot(g, bsp3, Color.GREEN);

		/*
		 * // Part3 Expression e = Expression.parseRPN(new Token[] { new
		 * Token(TokenType.X), new FunctionToken("exp"), new NumberToken(-1), new
		 * Token(TokenType.TIMES) }); try {
		 * 
		 * plot(g, Expression.parseRPN(new Token[] { new NumberToken(1), new
		 * NumberToken(2), new Token(TokenType.DIVIDE), new Token(TokenType.X), new
		 * Token(TokenType.TIMES) }), Color.CYAN); plot(g, Expression.parseRPN(new
		 * Token[] { new Token(TokenType.X), new FunctionToken("sin") }), Color.RED); }
		 * catch (Exception j) { j.printStackTrace(); }
		 */

		/*
		 * Part4 // -1 * exp^(x) Expression e =
		 * Expression.parseRPN(ShuntingYard.convertToRPN(new Token[] { new
		 * NumberToken(-1), new Token(TokenType.TIMES), new FunctionToken("exp"), new
		 * Token(TokenType.OPENING_PARENTHESIS), new Token(TokenType.X), new
		 * Token(TokenType.CLOSING_PARENTHESIS), }));
		 * 
		 * //sin(8 + 3 - 5 * x) Expression f =
		 * Expression.parseRPN(ShuntingYard.convertToRPN(new Token [] { new
		 * FunctionToken("sin"), new Token(TokenType.OPENING_PARENTHESIS), new
		 * NumberToken(8.), new Token(TokenType.PLUS), new NumberToken(3.), new
		 * Token(TokenType.MINUS), new NumberToken(5.), new Token(TokenType.TIMES), new
		 * Token(TokenType.X), new Token(TokenType.CLOSING_PARENTHESIS), }));
		 * 
		 * // sin(x) Expression h = Expression.parseRPN(ShuntingYard.convertToRPN(new
		 * Token [] { new FunctionToken("sin"), new
		 * Token(TokenType.OPENING_PARENTHESIS), new Token(TokenType.X), new
		 * Token(TokenType.CLOSING_PARENTHESIS), }));
		 * 
		 * //log(5 * (3 + x)) Expression i =
		 * Expression.parseRPN(ShuntingYard.convertToRPN(new Token [] { new
		 * FunctionToken("log"), new Token(TokenType.OPENING_PARENTHESIS), new
		 * NumberToken(5.0), new Token(TokenType.TIMES), new
		 * Token(TokenType.OPENING_PARENTHESIS), new NumberToken(3.0), new
		 * Token(TokenType.PLUS), new Token(TokenType.X), new
		 * Token(TokenType.CLOSING_PARENTHESIS), new
		 * Token(TokenType.CLOSING_PARENTHESIS), }));
		 * 
		 * //x^2 + 3 Expression j = Expression.parseRPN(ShuntingYard.convertToRPN(new
		 * Token [] { new Token(TokenType.X), new Token(TokenType.POWER), new
		 * Token(TokenType.OPENING_PARENTHESIS), new NumberToken(2.0), new
		 * Token(TokenType.CLOSING_PARENTHESIS), new Token(TokenType.PLUS), new
		 * NumberToken(3.0), }));
		 */

		// Part5
		Expression e = Expression.parseInfixString("-1");
		Expression f = Expression.parseInfixString("sin(8 + 3 - 5 * x)");
		Expression h = Expression.parseInfixString("sin(x)");
		Expression i = Expression.parseInfixString("log(5 * (3 + x))");
		Expression j = Expression.parseInfixString("x^2 + 3");

		try {
			plot(g, e, Color.RED);
			plot(g, f, Color.GREEN);
			plot(g, h, Color.CYAN);
			plot(g, i, Color.ORANGE);
			plot(g, j, Color.MAGENTA);
		} catch (Exception p) {
			p.printStackTrace();
		}

	}
}
