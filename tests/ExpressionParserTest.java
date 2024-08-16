/**
 * ExpressionParserTest - Derivatives Calculator
 */

package tests;

import java.util.ArrayList;
import model.ExpressionParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import structures.BinaryTree;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the ExpressionParser class.
 *
 * @author Jacob Klymenko
 * @version 3.0
 */
class ExpressionParserTest {

	/** The first valid infix notation expression to be tested. */
	private final String myValidExp1 = "3.7 * log_10(8.2) - sin(pi / 3.5)";

	/** The second valid infix notation expression to be tested. */
	private final String myValidExp2 =
	    "(cos(2.5) + log_2(9.1 + 6.7 ^ 1.2)) * (5.3 + sin(0.75 * pi))";

	/** The first invalid infix notation expression to be tested. */
	private final String myInvalidExp1 = "(((3 + 2) + 1)";

	/** The second invalid infix notation expression to be tested. */
	private final String myInvalidExp2 = "(1 + (2 + 3)))";

	/** A temporary ArrayList to hold expressions in a stringToList() format. */
	private ArrayList<String> myTempList = new ArrayList<String>();

	/**
	 * Clears the myTempList ArrayList before each test.
	 */
	@BeforeEach
	void setUp() {
		myTempList.clear();
	}

	/**
	 * Test method for {@link model.ExpressionParser#setValidFunctions()}.
	 */
	@Test
	void testSetValidFunctions() {
		final String[] validFunctions = {"abs", "sin", "cos", "tan", "sec", "csc", "cot",
		    "arcsin", "arccos", "arctan", "arcsec", "arccsc", "arccot", "log", "ln"};
		ExpressionParser.setValidFunctions();
		for (int i = 0; i < validFunctions.length; i++) {
			assertTrue(ExpressionParser.isFunction(validFunctions[i]));
		}
		assertFalse(ExpressionParser.isFunction("max"));
	}

	/**
	 * Test method for {@link model.ExpressionParser#stringToList(java.lang.String)}.
	 */
	@Test
	void testStringToListFirst() {
		final String[] validExp1 = {"3.7", "*", "log_10", "(", "8.2", ")", "-", "sin", "(",
		    "pi", "/", "3.5", ")"};
		for (int i = 0; i < validExp1.length; i++) {
			myTempList.add(validExp1[i]);
		}
		assertEquals(myTempList, ExpressionParser.stringToList(myValidExp1));
	}

	/**
	 * Test method for {@link model.ExpressionParser#stringToList(java.lang.String)}.
	 */
	@Test
	void testStringToListSecond() {
		final String[] validExp2 = {"(", "cos", "(", "2.5", ")", "+", "log_2", "(", "9.1",
		    "+", "6.7", "^", "1.2", ")", ")", "*", "(", "5.3", "+", "sin", "(", "0.75", "*",
		    "pi", ")", ")"};
		for (int i = 0; i < validExp2.length; i++) {
			myTempList.add(validExp2[i]);
		}
		assertEquals(myTempList, ExpressionParser.stringToList(myValidExp2));
	}

	/**
	 * Test method for {@link model.ExpressionParser#shuntingYardTree(java.util.ArrayList)}.
	 */
	@Test
	void testShuntingYardTree() {
		final ArrayList<String> expList1 = ExpressionParser.stringToList("3.7 * log_10(8.2)");
		final BinaryTree<String> expTree1 = ExpressionParser.shuntingYardTree(expList1);
		assertEquals("\nInOrder: [3.7, *, 8.2, log_10]" +
		    "\nLevelOrder: [*, 3.7, log_10, 8.2]", expTree1.toString());

		final ArrayList<String> expList2 = ExpressionParser.stringToList("log_2(9.1 + 6.7 ^ 1.2))");
		final BinaryTree<String> expTree2 = ExpressionParser.shuntingYardTree(expList2);
		assertEquals("\nInOrder: [9.1, +, 6.7, ^, 1.2, log_2]" +
		    "\nLevelOrder: [log_2, +, 9.1, ^, 6.7, 1.2]", expTree2.toString());
	}

	/**
	 * Test method for {@link model.ExpressionParser#isFunction(java.lang.String)}. Partially
	 * tested in testSetValidFunctions() with the real valid functions this calculator accepts.
	 */
	@Test
	void testIsFunction() {
		final String[] invalidFunctions = {"max", "xsin", "5xsin", "cosine", "5arcsin", "lg"};
		ExpressionParser.setValidFunctions();
		for (int i = 0; i < invalidFunctions.length; i++) {
			assertFalse(ExpressionParser.isFunction(invalidFunctions[i]));
		}
	}

	/**
	 * Test method for {@link model.ExpressionParser#getIsValid()} with a valid expression.
	 */
	@Test
	void testGetIsValidFirstTrue() {
		myTempList = ExpressionParser.stringToList(myValidExp1);
		@SuppressWarnings("unused")
		final BinaryTree<String> test = ExpressionParser.shuntingYardTree(myTempList);
		assertTrue(ExpressionParser.getIsValid());
	}

	/**
	 * Test method for {@link model.ExpressionParser#getIsValid()} with a valid expression.
	 */
	@Test
	void testGetIsValidSecondTrue() {
		myTempList = ExpressionParser.stringToList(myValidExp2);
		@SuppressWarnings("unused")
		final BinaryTree<String> test = ExpressionParser.shuntingYardTree(myTempList);
		assertTrue(ExpressionParser.getIsValid());
	}

	/**
	 * Test method for {@link model.ExpressionParser#getIsValid()} with an invalid expression.
	 */
	@Test
	void testGetIsValidFirstFalse() {
		myTempList = ExpressionParser.stringToList(myInvalidExp1);
		@SuppressWarnings("unused")
		final BinaryTree<String> test = ExpressionParser.shuntingYardTree(myTempList);
		assertFalse(ExpressionParser.getIsValid());
	}

	/**
	 * Test method for {@link model.ExpressionParser#getIsValid()} with an invalid expression.
	 */
	@Test
	void testGetIsValidSecondFalse() {
		myTempList = ExpressionParser.stringToList(myInvalidExp2);
		@SuppressWarnings("unused")
		final BinaryTree<String> test = ExpressionParser.shuntingYardTree(myTempList);
		assertFalse(ExpressionParser.getIsValid());
	}

}
