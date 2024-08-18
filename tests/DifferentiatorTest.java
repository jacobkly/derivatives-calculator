/**
 * DifferentiatorTest - Derivatives Calculator
 */

package tests;

import java.util.ArrayList;
import model.Differentiator;
import model.ExpressionParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import structures.BinaryTree;
import structures.BinaryTreeNode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the Differentiator class.
 *
 * @author Jacob Klymenko
 * @version 1.0
 */
class DifferentiatorTest {

	/** The hard coded variable of differentiation used for all the tests. */
	private static final BinaryTreeNode<String> VAR_DIFF = new BinaryTreeNode<String>("x");

	/** A constant represented in a binary tree node to use in the tests. */
	private final BinaryTreeNode<String> myConstant = new BinaryTreeNode<String>("5");

	/** A variable represented in a binary tree node to use in the tests. */
	private final BinaryTreeNode<String> myVariable = new BinaryTreeNode<String>("x");

	/** A second variable represented in a binary tree node to use in the tests. */
	private final BinaryTreeNode<String> myNonVarDiff = new BinaryTreeNode<String>("y");

	/** A binary tree node representing an expression with an operator to use in the tests. */
	private BinaryTreeNode<String> myNumOpNum;

	/** A binary tree node representing an expression with an operator to use in the tests. */
	private BinaryTreeNode<String> myNumOpVar;

	/** A binary tree node representing an expression with an operator to use in the tests. */
	private BinaryTreeNode<String> myVarOpNum;

	/** A binary tree node representing an expression with an operator to use in the tests. */
	private BinaryTreeNode<String> myVarOpVar;

	/**
	 * Set the valid functions accepted by this program before each test.
	 */
	@BeforeEach
	void setUp() {
		ExpressionParser.setValidFunctions();
		myNumOpNum = null;
		myNumOpVar = null;
		myVarOpNum = null;
		myVarOpVar = null;
	}

	/**
	 * Test method for {@link model.Differentiator#derive(structures.BinaryTreeNode, structures.BinaryTreeNode)}.
	 */
	@Test
	void testDerive() {
		final BinaryTreeNode<String> diffConstant = Differentiator.derive(myConstant, VAR_DIFF);
		assertEquals("0", diffConstant.getElement());

		final BinaryTreeNode<String> diffVarDiff = Differentiator.derive(myVariable, VAR_DIFF);
		assertEquals("1", diffVarDiff.getElement());

		final BinaryTreeNode<String> diffNonVarDiff = Differentiator.derive(myNonVarDiff, VAR_DIFF);
		assertEquals("dy/dx", diffNonVarDiff.getElement());
	}

	/**
	 * Test method for {@link model.Differentiator#derive(structures.BinaryTreeNode, structures.BinaryTreeNode)}.
	 */
	@Test
	void testDeriveSubtraction() {
		myNumOpNum = new BinaryTreeNode<String>("-", myConstant, myConstant);
		final BinaryTreeNode<String> diffNumOpNum = Differentiator.derive(myNumOpNum, VAR_DIFF);
		assertEquals("0 - 0", Differentiator.treeNodeToString(diffNumOpNum, 0));

		myNumOpVar = new BinaryTreeNode<String>("-", myConstant, myVariable);
		final BinaryTreeNode<String> diffNumOpVar = Differentiator.derive(myNumOpVar, VAR_DIFF);
		assertEquals("0 - 1", Differentiator.treeNodeToString(diffNumOpVar, 0));

		myVarOpNum = new BinaryTreeNode<String>("-", myVariable, myConstant);
		final BinaryTreeNode<String> diffVarOpNum = Differentiator.derive(myVarOpNum, VAR_DIFF);
		assertEquals("1 - 0", Differentiator.treeNodeToString(diffVarOpNum, 0));

		myVarOpVar = new BinaryTreeNode<String>("-", myVariable, myVariable);
		final BinaryTreeNode<String> diffVarOpVar = Differentiator.derive(myVarOpVar, VAR_DIFF);
		assertEquals("1 - 1", Differentiator.treeNodeToString(diffVarOpVar, 0));
	}

	/**
	 * Test method for {@link model.Differentiator#derive(structures.BinaryTreeNode, structures.BinaryTreeNode)}.
	 */
	@Test
	void testDeriveSubtractionNonVarDiff() {
		// the variable in the expressions are NOT the variable of differentiation
		myNumOpVar = new BinaryTreeNode<String>("-", myConstant, myNonVarDiff);
		final BinaryTreeNode<String> diffNumOpNonVar = Differentiator.derive(myNumOpVar, VAR_DIFF);
		assertEquals("0 - dy/dx", Differentiator.treeNodeToString(diffNumOpNonVar, 0));

		myVarOpNum = new BinaryTreeNode<String>("-", myNonVarDiff, myConstant);
		final BinaryTreeNode<String> diffNonVarOpNum = Differentiator.derive(myVarOpNum, VAR_DIFF);
		assertEquals("dy/dx - 0", Differentiator.treeNodeToString(diffNonVarOpNum, 0));

		myVarOpVar = new BinaryTreeNode<String>("-", myNonVarDiff, myNonVarDiff);
		final BinaryTreeNode<String> diffnonVarOpNonVar = Differentiator.derive(myVarOpVar, VAR_DIFF);
		assertEquals("dy/dx - dy/dx", Differentiator.treeNodeToString(diffnonVarOpNonVar, 0));
	}

	/**
	 * Test method for {@link model.Differentiator#derive(structures.BinaryTreeNode, structures.BinaryTreeNode)}.
	 */
	@Test
	void testDeriveAddition() {
		myNumOpNum = new BinaryTreeNode<String>("+", myConstant, myConstant);
		final BinaryTreeNode<String> diffNumOpNum = Differentiator.derive(myNumOpNum, VAR_DIFF);
		assertEquals("0 + 0", Differentiator.treeNodeToString(diffNumOpNum, 0));

		myNumOpVar = new BinaryTreeNode<String>("+", myConstant, myVariable);
		final BinaryTreeNode<String> diffNumOpVar = Differentiator.derive(myNumOpVar, VAR_DIFF);
		assertEquals("0 + 1", Differentiator.treeNodeToString(diffNumOpVar, 0));

		myVarOpNum = new BinaryTreeNode<String>("+", myVariable, myConstant);
		final BinaryTreeNode<String> diffVarOpNum = Differentiator.derive(myVarOpNum, VAR_DIFF);
		assertEquals("1 + 0", Differentiator.treeNodeToString(diffVarOpNum, 0));

		myVarOpVar = new BinaryTreeNode<String>("+", myVariable, myVariable);
		final BinaryTreeNode<String> diffVarOpVar = Differentiator.derive(myVarOpVar, VAR_DIFF);
		assertEquals("1 + 1", Differentiator.treeNodeToString(diffVarOpVar, 0));
	}

	/**
	 * Test method for {@link model.Differentiator#derive(structures.BinaryTreeNode, structures.BinaryTreeNode)}.
	 */
	@Test
	void testDeriveAdditionNonVarDiff() {
		// the variable in the expressions are NOT the variable of differentiation
		myNumOpVar = new BinaryTreeNode<String>("+", myConstant, myNonVarDiff);
		final BinaryTreeNode<String> diffNumOpNonVar = Differentiator.derive(myNumOpVar, VAR_DIFF);
		assertEquals("0 + dy/dx", Differentiator.treeNodeToString(diffNumOpNonVar, 0));

		myVarOpNum = new BinaryTreeNode<String>("+", myNonVarDiff, myConstant);
		final BinaryTreeNode<String> diffNonVarOpNum = Differentiator.derive(myVarOpNum, VAR_DIFF);
		assertEquals("dy/dx + 0", Differentiator.treeNodeToString(diffNonVarOpNum, 0));

		myVarOpVar = new BinaryTreeNode<String>("+", myNonVarDiff, myNonVarDiff);
		final BinaryTreeNode<String> diffnonVarOpNonVar = Differentiator.derive(myVarOpVar, VAR_DIFF);
		assertEquals("dy/dx + dy/dx", Differentiator.treeNodeToString(diffnonVarOpNonVar, 0));
	}

	/**
	 * Test method for {@link model.Differentiator#derive(structures.BinaryTreeNode, structures.BinaryTreeNode)}.
	 */
	@Test
	void testDeriveDivision() {
		myNumOpNum = new BinaryTreeNode<String>("/", myConstant, myConstant);
		final BinaryTreeNode<String> diffNumOpNum = Differentiator.derive(myNumOpNum, VAR_DIFF);
		assertEquals("((0 * 5) - (5 * 0)) / (5 ^ 2)", Differentiator.treeNodeToString(diffNumOpNum, 0));

		myNumOpVar = new BinaryTreeNode<String>("/", myConstant, myVariable);
		final BinaryTreeNode<String> diffNumOpVar = Differentiator.derive(myNumOpVar, VAR_DIFF);
		assertEquals("((0 * x) - (5 * 1)) / (x ^ 2)", Differentiator.treeNodeToString(diffNumOpVar, 0));

		myVarOpNum = new BinaryTreeNode<String>("/", myVariable, myConstant);
		final BinaryTreeNode<String> diffVarOpNum = Differentiator.derive(myVarOpNum, VAR_DIFF);
		assertEquals("((1 * 5) - (x * 0)) / (5 ^ 2)", Differentiator.treeNodeToString(diffVarOpNum, 0));

		myVarOpVar = new BinaryTreeNode<String>("/", myVariable, myVariable);
		final BinaryTreeNode<String> diffVarOpVar = Differentiator.derive(myVarOpVar, VAR_DIFF);
		assertEquals("((1 * x) - (x * 1)) / (x ^ 2)", Differentiator.treeNodeToString(diffVarOpVar, 0));
	}

	/**
	 * Test method for {@link model.Differentiator#derive(structures.BinaryTreeNode, structures.BinaryTreeNode)}.
	 */
	@Test
	void testDeriveDivisionNonVarDiff() {
		// the variable in the expressions are NOT the variable of differentiation
		myNumOpVar = new BinaryTreeNode<String>("/", myConstant, myNonVarDiff);
		final BinaryTreeNode<String> diffNumOpNonVar = Differentiator.derive(myNumOpVar, VAR_DIFF);
		assertEquals("((0 * y) - (5 * dy/dx)) / (y ^ 2)",
		    Differentiator.treeNodeToString(diffNumOpNonVar, 0));

		myVarOpNum = new BinaryTreeNode<String>("/", myNonVarDiff, myConstant);
		final BinaryTreeNode<String> diffNonVarOpNum = Differentiator.derive(myVarOpNum, VAR_DIFF);
		assertEquals("((dy/dx * 5) - (y * 0)) / (5 ^ 2)",
		    Differentiator.treeNodeToString(diffNonVarOpNum, 0));

		myVarOpVar = new BinaryTreeNode<String>("/", myNonVarDiff, myNonVarDiff);
		final BinaryTreeNode<String> diffnonVarOpNonVar = Differentiator.derive(myVarOpVar, VAR_DIFF);
		assertEquals("((dy/dx * y) - (y * dy/dx)) / (y ^ 2)",
		    Differentiator.treeNodeToString(diffnonVarOpNonVar, 0));
	}

	/**
	 * Test method for {@link model.Differentiator#derive(structures.BinaryTreeNode, structures.BinaryTreeNode)}.
	 */
	@Test
	void testDeriveMultiplication() {
		myNumOpNum = new BinaryTreeNode<String>("*", myConstant, myConstant);
		final BinaryTreeNode<String> diffNumOpNum = Differentiator.derive(myNumOpNum, VAR_DIFF);
		assertEquals("(0 * 5) + (5 * 0)", Differentiator.treeNodeToString(diffNumOpNum, 0));

		myNumOpVar = new BinaryTreeNode<String>("*", myConstant, myVariable);
		final BinaryTreeNode<String> diffNumOpVar = Differentiator.derive(myNumOpVar, VAR_DIFF);
		assertEquals("(0 * x) + (5 * 1)", Differentiator.treeNodeToString(diffNumOpVar, 0));

		myVarOpNum = new BinaryTreeNode<String>("*", myVariable, myConstant);
		final BinaryTreeNode<String> diffVarOpNum = Differentiator.derive(myVarOpNum, VAR_DIFF);
		assertEquals("(1 * 5) + (x * 0)", Differentiator.treeNodeToString(diffVarOpNum, 0));

		myVarOpVar = new BinaryTreeNode<String>("*", myVariable, myVariable);
		final BinaryTreeNode<String> diffVarOpVar = Differentiator.derive(myVarOpVar, VAR_DIFF);
		assertEquals("(1 * x) + (x * 1)", Differentiator.treeNodeToString(diffVarOpVar, 0));
	}

	/**
	 * Test method for {@link model.Differentiator#derive(structures.BinaryTreeNode, structures.BinaryTreeNode)}.
	 */
	@Test
	void testDeriveMultiplicationNonVarDiff() {
		// the variables in the expressions are NOT the variable of differentiation
		myNumOpVar = new BinaryTreeNode<String>("*", myConstant, myNonVarDiff);
		final BinaryTreeNode<String> diffNumOpNonVar = Differentiator.derive(myNumOpVar, VAR_DIFF);
		assertEquals("(0 * y) + (5 * dy/dx)",
		    Differentiator.treeNodeToString(diffNumOpNonVar, 0));

		myVarOpNum = new BinaryTreeNode<String>("*", myNonVarDiff, myConstant);
		final BinaryTreeNode<String> diffNonVarOpNum = Differentiator.derive(myVarOpNum, VAR_DIFF);
		assertEquals("(dy/dx * 5) + (y * 0)",
		    Differentiator.treeNodeToString(diffNonVarOpNum, 0));

		myVarOpVar = new BinaryTreeNode<String>("*", myNonVarDiff, myNonVarDiff);
		final BinaryTreeNode<String> diffnonVarOpNonVar = Differentiator.derive(myVarOpVar, VAR_DIFF);
		assertEquals("(dy/dx * y) + (y * dy/dx)",
		    Differentiator.treeNodeToString(diffnonVarOpNonVar, 0));
	}

	/**
	 * Test method for {@link model.Differentiator#derive(structures.BinaryTreeNode, structures.BinaryTreeNode)}.
	 */
	@Test
	void testDeriveExponent() {
		myNumOpNum = new BinaryTreeNode<String>("^", myConstant, myConstant);
		final BinaryTreeNode<String> diffNumOpNum = Differentiator.derive(myNumOpNum, VAR_DIFF);
		assertEquals("0", Differentiator.treeNodeToString(diffNumOpNum, 0));

		myVarOpNum = new BinaryTreeNode<String>("^", myVariable, myConstant);
		final BinaryTreeNode<String> diffVarOpNum = Differentiator.derive(myVarOpNum, VAR_DIFF);
		assertEquals("(5 * x) ^ (5 - 1)", Differentiator.treeNodeToString(diffVarOpNum, 0));

		myNumOpVar = new BinaryTreeNode<String>("^", myConstant, myVariable);
		final BinaryTreeNode<String> diffNumOpVar = Differentiator.derive(myNumOpVar, VAR_DIFF);
		assertEquals("(e ^ (x * ln(5))) * ((1 * ln(5)) + (x * 0))",
		    Differentiator.treeNodeToString(diffNumOpVar, 0));

		myVarOpVar = new BinaryTreeNode<String>("^", myVariable, myVariable);
		final BinaryTreeNode<String> diffVarOpVar = Differentiator.derive(myVarOpVar, VAR_DIFF);
		assertEquals("(e ^ (x * ln(x))) * ((1 * ln(x)) + (x * (1 / x)))",
		    Differentiator.treeNodeToString(diffVarOpVar, 0));
	}

	/**
	 * Test method for {@link model.Differentiator#derive(structures.BinaryTreeNode, structures.BinaryTreeNode)}.
	 *
	 * This method contains both the hard coded variable of differentiation ('x') and the
	 * variable other than the variable of differentiation ('y').
	 */
	@Test
	void testDeriveExponentNonVarDiff() {
		myVarOpNum = new BinaryTreeNode<String>("^", myNonVarDiff, myConstant);
		final BinaryTreeNode<String> diffVarOpNum = Differentiator.derive(myVarOpNum, VAR_DIFF);
		assertEquals("dy/dx * ((5 * y) ^ (5 - 1))", Differentiator.treeNodeToString(diffVarOpNum, 0));

		myNumOpVar = new BinaryTreeNode<String>("^", myConstant, myNonVarDiff);
		final BinaryTreeNode<String> diffNumOpVar = Differentiator.derive(myNumOpVar, VAR_DIFF);
		assertEquals("(e ^ (y * ln(5))) * ((dy/dx * ln(5)) + (y * 0))",
		    Differentiator.treeNodeToString(diffNumOpVar, 0));

		myVarOpVar = new BinaryTreeNode<String>("^", myVariable, myNonVarDiff);
		final BinaryTreeNode<String> diffVarOpNonVar = Differentiator.derive(myVarOpVar, VAR_DIFF);
		assertEquals("(e ^ (y * ln(x))) * ((dy/dx * ln(x)) + (y * (1 / x)))",
		    Differentiator.treeNodeToString(diffVarOpNonVar, 0));

		myVarOpVar = new BinaryTreeNode<String>("^", myNonVarDiff, myVariable);
		final BinaryTreeNode<String> diffNonVarOpVar = Differentiator.derive(myVarOpVar, VAR_DIFF);
		assertEquals("(e ^ (x * ln(y))) * ((1 * ln(y)) + (x * (dy/dx * (1 / y))))",
		    Differentiator.treeNodeToString(diffNonVarOpVar, 0));

		myVarOpVar = new BinaryTreeNode<String>("^", myNonVarDiff, myNonVarDiff);
		final BinaryTreeNode<String> diffNonVarOpNonVar = Differentiator.derive(myVarOpVar, VAR_DIFF);
		assertEquals("(e ^ (y * ln(y))) * ((dy/dx * ln(y)) + (y * (dy/dx * (1 / y))))",
		    Differentiator.treeNodeToString(diffNonVarOpNonVar, 0));
	}

	/**
	 * Test method for {@link model.Differentiator#derive(structures.BinaryTreeNode, structures.BinaryTreeNode)}.
	 */
	@Test
	void testDeriveFunctions() {
		final String[] functions = {"sin", "cos", "tan", "sec", "csc", "cot", "arcsin",
		    "arccos", "arctan", "arcsec", "arccsc", "arccot", "log", "log_10", "log_2", "ln"};
		/*
		 * Since Java does not allow creating a generic array of BinaryTreeNode<String>, it can
		 * be bypassed by first creating an array of BinaryTreeNode<String> with the desired
		 * size. Then initialize each index in the array with the desired element using a loop.
		 * However, an unavoidable type safety warning occurs. Nothing can be done to fix this
		 * expected warning.
		 */
		BinaryTreeNode<String>[] funcsNode = new BinaryTreeNode[functions.length];
		for (int i = 0; i < functions.length; i++) {
			funcsNode[i] = new BinaryTreeNode<String>(functions[i], myVariable, null);
		}
		// the derivative of each function in order
		final String[] diffFuncs = {"cos(x)", "0 - sin(x)", "sec(x) ^ 2",
		    "sec(x) * tan(x)", "0 - (csc(x) * cot(x))", "0 - (csc(x) ^ 2)",
		    "1 / ((1 - (x ^ 2)) ^ (1 / 2))", "0 - (1 / ((1 - (x ^ 2)) ^ (1 / 2)))",
		    "1 / ((x ^ 2) + 1)", "1 / (abs(x) * (((x ^ 2) - 1) ^ (1 / 2)))",
		    "0 - (1 / (abs(x) * (((x ^ 2) - 1) ^ (1 / 2))))", "0 - (1 / ((x ^ 2) + 1))",
		    "1 / x", "1 / (x * ln(10))", "1 / (x * ln(2))", "1 / x"};
		// unit testing
		BinaryTreeNode<String> currDerivative;
		for (int i = 0; i < funcsNode.length; i++) {
			currDerivative = Differentiator.derive(funcsNode[i], VAR_DIFF);
			assertEquals(diffFuncs[i], Differentiator.treeNodeToString(currDerivative, 0));
		}
	}

	/**
	 * Test method for {@link model.Differentiator#derive(structures.BinaryTreeNode, structures.BinaryTreeNode)}.
	 */
	@Test
	void testDeriveFunctionsNonVarDiff() {
		final String[] functions = {"sin", "cos", "tan", "sec", "csc", "cot", "arcsin",
		    "arccos", "arctan", "arcsec", "arccsc", "arccot", "log", "log_10", "log_2", "ln"};
		/*
		 * Since Java does not allow creating a generic array of BinaryTreeNode<String>, it can
		 * be bypassed by first creating an array of BinaryTreeNode<String> with the desired
		 * size. Then initialize each index in the array with the desired element using a loop.
		 * However, an unavoidable type safety warning occurs. Nothing can be done to fix this
		 * expected warning.
		 */
		BinaryTreeNode<String>[] funcsNode = new BinaryTreeNode[functions.length];
		for (int i = 0; i < functions.length; i++) {
			funcsNode[i] = new BinaryTreeNode<String>(functions[i], myNonVarDiff, null);
		}
		// the derivative of each function in order
		final String[] diffFuncs = {"dy/dx * cos(y)", "dy/dx * (0 - sin(y))",
		    "dy/dx * (sec(y) ^ 2)", "dy/dx * (sec(y) * tan(y))",
		    "dy/dx * (0 - (csc(y) * cot(y)))", "dy/dx * (0 - (csc(y) ^ 2))",
		    "dy/dx * (1 / ((1 - (y ^ 2)) ^ (1 / 2)))",
		    "dy/dx * (0 - (1 / ((1 - (y ^ 2)) ^ (1 / 2))))",
		    "dy/dx * (1 / ((y ^ 2) + 1))",
		    "dy/dx * (1 / (abs(y) * (((y ^ 2) - 1) ^ (1 / 2))))",
		    "dy/dx * (0 - (1 / (abs(y) * (((y ^ 2) - 1) ^ (1 / 2)))))",
		    "dy/dx * (0 - (1 / ((y ^ 2) + 1)))",
		    "dy/dx * (1 / y)", "dy/dx * (1 / (y * ln(10)))",
		    "dy/dx * (1 / (y * ln(2)))", "dy/dx * (1 / y)"};
		// unit testing
		BinaryTreeNode<String> currDerivative;
		for (int i = 0; i < funcsNode.length; i++) {
			currDerivative = Differentiator.derive(funcsNode[i], VAR_DIFF);
			assertEquals(diffFuncs[i], Differentiator.treeNodeToString(currDerivative, 0));
		}
	}

	/**
	 * Test method for {@link model.Differentiator#derive(structures.BinaryTreeNode, structures.BinaryTreeNode)}.
	 */
	@Test
	void testDeriveNestedExpressions() {
		// , ""
		final String[] nestedExps = {"(x + 5) + (5 + x)",
		    "(x + 5) * (5 + x)",
		    "sin(sin(x)) * tan(x)",
		    "(x ^ 2) / x",
		    "x ^ (x + 1)",
		    "ln(x) / (x ^ 3)",
		    "x / ((x - 1) ^ (1 / 2))",
		    "arctan(sin(x))",
		    "arccot(5 * x)",
		    "csc(x ^ 2)",
		    "(3 * (x ^ (1 / 2))) / 2",
		    "log_2(5 * x)"};
		/*
		 * Since Java does not allow creating a generic array of BinaryTreeNode<String>, it can
		 * be bypassed by first creating an array of BinaryTreeNode<String> with the desired
		 * size. Then initialize each index in the array with the desired element using a loop.
		 * However, an unavoidable type safety warning occurs. Nothing can be done to fix this
		 * expected warning.
		 */
		ArrayList<String> nestedExpsList;
		BinaryTree<String>[] nestedExpsTree = new BinaryTree[nestedExps.length];
		for (int i = 0; i < nestedExps.length; i++) {
			nestedExpsList = ExpressionParser.stringToList(nestedExps[i]);
			nestedExpsTree[i] = ExpressionParser.shuntingYardTree(nestedExpsList);
		}
		// the derivative of each nested expression in order
		final String[] diffNestedExps = {"(1 + 0) + (0 + 1)",
		    "((1 + 0) * (5 + x)) + ((x + 5) * (0 + 1))",
		    "((cos(sin(x)) * cos(x)) * tan(x)) + (sin(sin(x)) * (sec(x) ^ 2))",
		    "((((2 * x) ^ (2 - 1)) * x) - ((x ^ 2) * 1)) / (x ^ 2)",
		    "(e ^ ((x + 1) * ln(x))) * (((1 + 0) * ln(x)) + ((x + 1) * (1 / x)))",
		    "(((1 / x) * (x ^ 3)) - (ln(x) * ((3 * x) ^ (3 - 1)))) / ((x ^ 3) ^ 2)",
		    "((1 * ((x - 1) ^ (1 / 2))) - (x * (((1 / 2) * (x - 1)) ^ ((1 / 2) - 1)))) / (((x - 1) ^ (1 / 2)) ^ 2)",
		    "(1 / ((sin(x) ^ 2) + 1)) * cos(x)",
		    "(0 - (1 / (((5 * x) ^ 2) + 1))) * ((0 * x) + (5 * 1))",
		    "(0 - (csc((x ^ 2)) * cot((x ^ 2)))) * ((2 * x) ^ (2 - 1))",
		    "((((0 * (x ^ (1 / 2))) + (3 * (((1 / 2) * x) ^ ((1 / 2) - 1)))) * 2) - ((3 * (x ^ (1 / 2))) * 0)) / (2 ^ 2)",
		    "(1 / ((5 * x) * ln(2))) * ((0 * x) + (5 * 1))"};
		// unit testing
		BinaryTreeNode<String> currDerivative;
		for (int i = 0; i < nestedExpsTree.length; i++) {
			currDerivative = Differentiator.derive(nestedExpsTree[i].getNode(), VAR_DIFF);
			assertEquals(diffNestedExps[i], Differentiator.treeNodeToString(currDerivative, 0));
		}
	}

	/**
	 * Test method for {@link model.Differentiator#isOperator(java.lang.String)}.
	 */
	@Test
	void testIsOperator() {
		final String[] validOperators = {"-", "+", "/", "*", "^"};
		for (int i = 0; i < validOperators.length; i++) {
			assertTrue(Differentiator.isOperator(validOperators[i]));
		}

		final String[] invalidOperators = {"!", "@", "#", "$", "%", "&", "(", ")", "[", "]",
		    "{", "}", ",", ".", "<", ">", "?", "_", "=", "~", ";", ":", "\'", "\""};
		for (int i = 0; i < invalidOperators.length; i++) {
			assertFalse(Differentiator.isOperator(invalidOperators[i]));
		}
	}

	/**
	 * Test method for {@link model.Differentiator#treeNodeToString(structures.BinaryTreeNode, int)}.
	 */
	@Test
	void testTreeNodeToString() {
		ExpressionParser.setValidFunctions();
		final ArrayList<String> expList1 =
		    ExpressionParser.stringToList("3.7 * log_10(8.2) - sin(3.14 / 3.5)");
		final BinaryTree<String> expTree1 = ExpressionParser.shuntingYardTree(expList1);
		assertEquals("(3.7 * log_10(8.2)) - sin((3.14 / 3.5))",
		    Differentiator.treeNodeToString(expTree1.getNode(), 0));

		final ArrayList<String> expList2 =
		    ExpressionParser.stringToList("(cos(2.5) + log_2(9.1 + 6.7 ^ 1.2)) * " +
		        "(5.3 + sin(0.75 * pi))");
		final BinaryTree<String> expTree2 = ExpressionParser.shuntingYardTree(expList2);
		assertEquals("(cos(2.5) + log_2((9.1 + (6.7 ^ 1.2)))) * (5.3 + sin((0.75 * pi)))",
		    Differentiator.treeNodeToString(expTree2.getNode(), 0));
	}
}
