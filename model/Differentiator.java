/**
 * ExpressionParser - Derivates Calculator
 */

package model;

import structures.BinaryTreeNode;

/**
 * Differentiator takes a binary tree representing a symbolic mathematical expression and
 * differentiates the expression.
 *
 * @author Jacob Klymenko
 * @version 1.0
 */
public class Differentiator {

	/** A private constructor to inhibit external instantiation. */
	private Differentiator() {
		// do nothing
	}

	/**
	 *
	 *
	 * @param theRoot	the root of the binary tree representing the expression being derived
	 * @param theVar	the chosen independent variable for differentiation
	 * @return
	 */
	public static String derive(final BinaryTreeNode<String> theRoot,
	    final String theVar) {
		String result = "";
		if (theRoot != null) {
			final String rootElement = theRoot.getElement();
			if (isOperator(rootElement)) {
				final String left = theRoot.getLeft().getElement();
				final String difLeft = derive(theRoot.getLeft(), theVar);
				final String right = theRoot.getRight().getElement();
				final String difRight = derive(theRoot.getRight(), theVar);
				switch (rootElement) {
					case "-":
						result = difLeft + " " + rootElement + " " + difRight;
						break;
					case "+":
						result = difLeft + " " + rootElement + " " + difRight;
						break;
					case "/":
						result = "((" + difLeft + ")(" + right + ") - (" + difRight + ")(" +
						    left + ")) / (" + right + ") ^ 2";
						break;
					case "*":
						result = "(" + difLeft + ")(" + right + ") + (" + left + ")(" +
						    difRight + ")";
						break;
					case "^":
						result = right + left + " ^ (" + right + " - 1)";
						break;
				}
			} else if (ExpressionParser.isFunction(rootElement)) {

			} else { // a constant or variable
				if (!rootElement.matches(".*[a-zA-Z].*")) { // a constant
					result = "0";
				} else { // a variable
					if (rootElement.matches("-?\\d+(\\.\\d+)?")) { // constant * variable
						result = rootElement.replaceAll("[^\\d.]", ""); // removes all letters
					} else {
						result = "1";
					}
				}
			}
		}
		return result;
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

	// public static String treeToString(final BinaryTree<String> theOutputTree) {
	// // TODO
	// return null;
	// }
}
