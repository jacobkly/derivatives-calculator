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
			simpExp = shallowSimplify(theRoot);
		} else if (theRoot.numChildren() > 2) {
			simpExp = deepSimplify(theRoot);
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
	private static BinaryTreeNode<String> shallowSimplify(final BinaryTreeNode<String> theRoot) {
		// necessary components to simplify the shallow tree
		final String leftVar = theRoot.getLeft().getElement().replaceAll("[^a-zA-Z]", "");
		final String rightVar = theRoot.getRight().getElement().replaceAll("[^a-zA-Z]", "");
		String leftNumStr = theRoot.getLeft().getElement().replaceAll("[^0-9&&[^.]]", "");
		String rightNumStr = theRoot.getRight().getElement().replaceAll("[^0-9&&[^.]]", "");
		if (leftNumStr.isEmpty()) {
			leftNumStr = "1";
		}
		if (rightNumStr.isEmpty()) {
			rightNumStr = "1";
		}
		final String operator = theRoot.getElement();
		final Double numResult = evaluate(operator, Double.parseDouble(leftNumStr),
		    Double.parseDouble(rightNumStr));
		final String strResult = String.valueOf(numResult);
		BinaryTreeNode<String> simpExp = theRoot;
		// main portion of simplifying
		// if (result is zero or both are constants) and operator is not an exponent
		if (numResult == 0.0 || (leftVar.isEmpty() && rightVar.isEmpty())) {
			if (!operator.equals("^") && !operator.equals("-")) {
				simpExp = new BinaryTreeNode<String>(strResult);
			}
		} else if (operator.equals("-") || operator.equals("+")) {
			if (leftVar.equals(rightVar) && !leftVar.isEmpty()) { // both have same vars
				simpExp = new BinaryTreeNode<String>(strResult + leftVar);
			}
		} else if (operator.equals("/")) {
			if (leftVar.equals(rightVar) && !leftVar.isEmpty()) { // both have same vars
				final BinaryTreeNode<String> leftNode =
				    new BinaryTreeNode<String>(leftNumStr);
				final BinaryTreeNode<String> rightNode =
				    new BinaryTreeNode<String>(rightNumStr);
				final BinaryTreeNode<String> divide =
				    new BinaryTreeNode<String>("/", leftNode, rightNode);
				final BinaryTreeNode<String> var = new BinaryTreeNode<String>(leftVar);
				simpExp = new BinaryTreeNode<String>("*", divide, var);
			}
		} else if (operator.equals("*")) {
			if (leftVar.equals(rightVar) && !leftVar.isEmpty()) { // both have same vars
				final BinaryTreeNode<String> base =
				    new BinaryTreeNode<String>(strResult + leftVar);
				final BinaryTreeNode<String> two = new BinaryTreeNode<String>("2");
				simpExp = new BinaryTreeNode<String>("^", base, two);
			} else { // one side has a variable
				if (leftVar.isEmpty()) {
					simpExp = new BinaryTreeNode<String>(strResult + rightVar);
				}
				if (rightVar.isEmpty()) {
					simpExp = new BinaryTreeNode<String>(strResult + leftVar);
				}
			}
		}
		// else if (operator.equals("^")) { // 10 ^ 2x = 100 ^ x or 10x ^ 2 = 100x
		// final BinaryTreeNode<String> nodeResult = new BinaryTreeNode<String>(strResult);
		// BinaryTreeNode<String> var = null;
		// if (leftVar.isEmpty()) {
		// var = new BinaryTreeNode<String>(rightVar);
		// simpExp = new BinaryTreeNode<String>("^", nodeResult, var);
		// }
		// if (rightVar.isEmpty()) {
		// var = new BinaryTreeNode<String>(leftVar);
		// simpExp = new BinaryTreeNode<String>("^", nodeResult, var);
		// }
		// }
		return simpExp;
	}

	/**
	 *
	 *
	 * @param theOperator
	 * @param theLeftOperand
	 * @param theRightOperand
	 * @return
	 */
	private static Double evaluate(final String theOperator, final Double theLeftOperand,
	    final Double theRightOperand) {
		Double result = 0.0;
		switch (theOperator) {
			case "-":
				result = theLeftOperand - theRightOperand;
				break;
			case "+":
				result = theLeftOperand + theRightOperand;
				break;
			case "/":
				result = theLeftOperand / theRightOperand;
				break;
			case "*":
				result = theLeftOperand * theRightOperand;
				break;
			case "^":
				result = Math.pow(theLeftOperand, theRightOperand);
				break;
		}
		return result;
	}

	private static BinaryTreeNode<String> deepSimplify(final BinaryTreeNode<String> theRoot) {
		// TODO
		return theRoot;
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
