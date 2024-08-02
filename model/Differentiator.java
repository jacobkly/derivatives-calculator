/**
 * ExpressionParser - Derivatives Calculator
 */

package model;

import structures.BinaryTreeNode;

/**
 * Differentiator differentiates a binary tree representing a symbolic mathematical expression.
 *
 * @author Jacob Klymenko
 * @version 1.0
 */
public class Differentiator {

	/** The default base value for logarithms without specified bases. */
	private final static String DEFAULT_BASE = "10";

	/** A private constructor to inhibit external instantiation. */
	private Differentiator() {
		// do nothing
	}

	/**
	 * Returns a String representing the derivative of the binary tree node root.
	 *
	 * @param theRoot		the root node representing the expression segment being derived
	 * @param theVariable	the chosen independent variable for differentiation
	 * @return a String representing the derivative of the binary tree node root
	 */
	public static String derive(final BinaryTreeNode<String> theRoot,
	    final Character theVariable) {
		// System.out.println("\n" + treeToString(theRoot));
		String derivative = "";
		if (theRoot != null) { // first base case here
			final String rootElement = theRoot.getElement();
			if (isOperator(rootElement)) {
				final String left = treeToString(theRoot.getLeft());
				final String right = treeToString(theRoot.getRight());
				// differentiated left subtree
				final String diffLeft = derive(theRoot.getLeft(), theVariable);
				// differentiated right subtree
				final String diffRight = derive(theRoot.getRight(), theVariable);
				derivative = deriveOperator(left, right, diffLeft, diffRight, rootElement);
			} else if (ExpressionParser.isFunction(rootElement)) {
				if (rootElement.equals("ln") || rootElement.substring(0, 3).equals("log")) {
					derivative = deriveLog(theRoot, theVariable);
				} else {
					derivative = deriveTrig(theRoot, theVariable);
				}
			} else { // second base case here - a constant or variable
				if (rootElement.matches(".*" + theVariable + ".*")) { // contains a variable
					if (rootElement.length() > 1) { // constant * variable of differentiation
						char ch = Character.MIN_VALUE;
						derivative = rootElement.replace(theVariable, ch);
					} else { // the variable of differentiation is alone
						derivative = "1";
					}
				} else { // only contains a constant
					derivative = "0";
				}
			}
		}
		return derivative;
	}

	/**
	 * Applies the corresponding operator differentiation rule and returns a String
	 * representing differentiated expression.
	 *
	 * @param theLeft 		a String representing the left subtree of the expression
	 * @param theRight		a String representing the right subtree of the expression
	 * @param theDiffLeft	a String representing the differentiated left subtree
	 * @param theDiffRight 	a String representing the differentiated left subtree
	 * @param theOperator	the operator determining the differentiation rule to apply
	 * @return a String representing differentiated expression
	 */
	private static String deriveOperator(final String theLeft, final String theRight,
	    final String theDiffLeft, final String theDiffRight, final String theOperator) {
		String derivative = "";
		switch (theOperator) {
			case "-":
				derivative = theDiffLeft + " " + theOperator + " " + theDiffRight;
				break;
			case "+":
				derivative = theDiffLeft + " " + theOperator + " " + theDiffRight;
				break;
			case "/":
				derivative = "((" + theDiffLeft + ")(" + theRight + ") - (" + theDiffRight +
				    ")(" + theLeft + ")) / (" + theRight + ") ^ 2";
				break;
			case "*":
				derivative = "(" + theDiffLeft + ")(" + theRight + ") + (" + theLeft + ")(" +
				    theDiffRight + ")";
				break;
			case "^": // ERROR - implement handling var of diff in the exponent
				derivative = theRight + theLeft + " ^ (" + theRight + " - 1)";
				break;
		}
		return derivative;
	}

	private static String deriveLog(final BinaryTreeNode<String> theRoot,
	    final Character theVariable) {
		String derivative = "";
		final String rootElement = theRoot.getElement();
		if (rootElement.equals("ln")) {
			derivative = "(1 / ln(" + theVariable + "))";
		} else { // regular logarithm
			if (rootElement.contains("_")) {
				final String baseString = rootElement.substring(4);
				derivative = "(1 / " + theVariable + " * ln(" + baseString + "))";
			} else {
				derivative = "(1 / " + theVariable + " * ln(" + DEFAULT_BASE + "))";
			}
		}
		return derivative;
	}

	private static String deriveTrig(final BinaryTreeNode<String> theRoot,
	    final Character theVariable) {
		String derivative = "";
		final String rootElement = theRoot.getElement();
		switch (rootElement) {
			case "sin":
				derivative = "cos(" + theVariable + ")";
				break;
			case "cos":
				derivative = "-sin(" + theVariable + ")";
				break;
			case "tan":
				derivative = "(sec(" + theVariable + ") ^ 2)";
				break;
			case "sec":
				derivative = "(-csc(" + theVariable + ")cot(" + theVariable + "))";
				break;
			case "csc":
				derivative = "(sec(" + theVariable + ")tan(" + theVariable + "))";
				break;
			case "cot":
				derivative = "(-(sec(" + theVariable + ")) ^ 2)";
				break;
		}
		return derivative;
	}

	/**
	 * Returns true if the String is an operator; otherwise false.
	 *
	 * @param theString the String being examined
	 * @return true if the String is an operator; otherwise false
	 */
	private static boolean isOperator(final String theString) {
		boolean result = false;
		if (theString.equals("-") || theString.equals("+") || theString.equals("/") ||
		    theString.equals("*") || theString.equals("^")) {
			result = true;
		}
		return result;
	}

	/**
	 * Returns a String representing the binary tree as a mathematical expression recursively.
	 *
	 * @param theRoot the root node of this binary tree
	 * @return a String representing the binary tree as an expression
	 */
	private static String treeToString(final BinaryTreeNode<String> theRoot) {
		String result = "";
		if (isOperator(theRoot.getElement())) {
			/*
			 * Improves final expression readability by adding parentheses surrounding chunks
			 * of the tree. However, the bigger the tree, the messier the String becomes. Best
			 * thing to do in the future is to use apply parentheses around two operands and
			 * an operator. The operands could be other chunks of expressions, like "(x ^ 2)".
			 *
			 * Operator Example: "(<operand> <operator> <operand>)"
			 * Function Example: "(<function>(<operand>))"
			 */
			if (theRoot.numChildren() == 2 || theRoot.numChildren() == 6) {
				// result = "(" + treeToString(theRoot.getLeft()) + " " +
				// theRoot.getElement() + " " + treeToString(theRoot.getRight()) + ")";
				// } else {
				result = treeToString(theRoot.getLeft()) + " " +
				    theRoot.getElement() + " " + treeToString(theRoot.getRight());
			}
		} else { // the base case is when the root is a constant or variable
			result = theRoot.getElement();
		}
		return result;
	}
}
