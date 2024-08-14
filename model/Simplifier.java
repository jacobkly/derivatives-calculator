/**
 * Simplifier - Derivatives Calculator
 */

package model;

import structures.BinaryTreeNode;

/**
 * Simplifier traverses through a binary tree representing a mathematical expression and
 * simplifies the expression to its fullest extent.
 *
 * @author Jacob Klymenko
 * @version 1.0
 */
public class Simplifier {

	/** A private constructor to inhibit external instantiation. */
	private Simplifier() {
		// do nothing
	}

	/**
	 * Returns a binary tree node representing the most simplified form of the mathematical
	 * expression of the specified root's equivalent expression.
	 *
	 * @param theRoot the root node representing the expression segment being simplified
	 * @return a binary tree node representing the simplified expression of the specified root
	 */
	public static BinaryTreeNode<String> simplify(final BinaryTreeNode<String> theRoot) {
		BinaryTreeNode<String> simpExp = null;
		if (theRoot != null) {
			final String rootElem = theRoot.getElement();
			if (Differentiator.isOperator(rootElem)) {
				final BinaryTreeNode<String> leftNode = simplify(theRoot.getLeft());
				final BinaryTreeNode<String> rightNode = simplify(theRoot.getRight());
				// simpExpr = simplifyOperator(leftNode, rightNode, rootElem);
				simpExp = simplifyOperator(theRoot);
			} else if (ExpressionParser.isFunction(rootElem)) {
				simpExp = simplify(theRoot.getLeft());
			} else { // cannot be simplified further
				simpExp = theRoot;
			}
		}
		return simpExp;
	}

	private static BinaryTreeNode<String> simplifyOperator(final BinaryTreeNode<String> theRoot) {
		// final String operator = theRoot.getElement();
		// final String leftElem = theRoot.getLeft().getElement();
		// final String rightElem = theRoot.getLeft().getElement();
		BinaryTreeNode<String> simpExp = null;

		if (theRoot.numChildren() == 2) {
			simpExp = shallowSimplification(theRoot);
		} else if (theRoot.numChildren() > 2) {
			simpExp = deepSimplification(theRoot);
		}

		return simpExp;
	}

	// 1 - both don't contain variables
	// 2 - both contain variables
	// 3 - either one contains a variable
	// 4 - either contains an operator
	// 5 - either contains a function
	// else can't be simplified so return theRoot

	/**
	 *
	 *
	 * @param theRoot
	 * @return
	 */
	private static BinaryTreeNode<String> shallowSimplification(
	    final BinaryTreeNode<String> theRoot) {

		final String leftVar = theRoot.getLeft().getElement().replaceAll("[^a-zA-Z]", "");
		final String rightVar = theRoot.getRight().getElement().replaceAll("[^a-zA-Z]", "");
		String leftNum = theRoot.getLeft().getElement().replaceAll("[^0-9&&[^.]]", "");
		String rightNum = theRoot.getRight().getElement().replaceAll("[^0-9&&[^.]]", "");

		if (leftNum.isEmpty()) {
			leftNum = "1";
		}
		if (rightNum.isEmpty()) {
			rightNum = "1";
		}

		Double left = Double.parseDouble(leftNum);
		Double right = Double.parseDouble(rightNum);

		BinaryTreeNode<String> temp = null;
		BinaryTreeNode<String> leftNode = null;
		BinaryTreeNode<String> rightNode = null;
		BinaryTreeNode<String> result = theRoot;

		// test -------------------------------------------------------------------------------
		if (leftVar.equals(rightVar) && !leftVar.isEmpty()) { // both contain same variable
			System.out.println(leftVar + " = " + rightVar + " (" + leftNum + ", " +
			    rightNum + ")");

			// both don't contain variables
		} else if (!containsVariables(theRoot.getLeft().getElement()) &&
		    !containsVariables(theRoot.getRight().getElement())) {
			    System.out.println(leftNum + " and " + rightNum);
		    }
		// test -------------------------------------------------------------------------------

		final String operator = theRoot.getElement();
		switch (operator) {
			case "-":
				Double difference = left - right;
				if (leftVar.equals(rightVar) && !leftVar.isEmpty()) { // both contain variables
					if (difference == 0.0) {
						result = new BinaryTreeNode<String>(String.valueOf(difference));
					} else {
						result = new BinaryTreeNode<String>(String.valueOf(difference) +
						    leftVar);
					}
				} else if (!containsVariables(theRoot.getLeft().getElement()) &&
				    !containsVariables(theRoot.getRight().getElement())) { // only constants
					    result = new BinaryTreeNode<String>(String.valueOf(difference));
				    }
				break;
			case "+":
				break;
			case "/":
				break;
			case "*":
				break;
			case "^":
				if (!containsVariables(theRoot.getLeft().getElement()) &&
				    !containsVariables(theRoot.getRight().getElement())) { // only constants
					Double power = Math.pow(left, right);
					result = new BinaryTreeNode<String>(String.valueOf(power));
				}
				break;
		}
		return result;
	}

	private static BinaryTreeNode<String> deepSimplification(
	    final BinaryTreeNode<String> theRoot) {
		// TODO
		return null;
	}

	/**
	 * Returns true if the specified String contains variables. Otherwise returns false.
	 *
	 * @param theString the string containing potential variables
	 * @return true if the specified String contains variables; otherwise false
	 */
	private static boolean containsVariables(final String theString) {
		final String variables = theString.replaceAll("[^a-zA-Z]", "");
		if (variables.length() == 0) {
			return false;
		}
		return true;
	}

	/**
	 * Returns true if the root representing a mathematical expression contains a valid
	 * function. Otherwise returns false.
	 *
	 * @param theRoot the root representing the expression searched to find a valid function
	 * @return true if the root representing an expression contains a function; otherwise false
	 */
	public static boolean containsFunction(final BinaryTreeNode<String> theRoot) {
		boolean result = false;
		if (theRoot != null) {
			if (ExpressionParser.isFunction(theRoot.getElement())) {
				result = true;
			} else {
				result = containsFunction(theRoot.getLeft()) ||
				    containsFunction(theRoot.getRight());
			}
		}
		return result;
	}
}
