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
		BinaryTreeNode<String> leftNode = null;
		BinaryTreeNode<String> rightNode = null;
		BinaryTreeNode<String> simpExp = null;

		final String rootElem = theRoot.getElement();
		if (Differentiator.isOperator(rootElem)) {
			leftNode = simplify(theRoot.getLeft());
			rightNode = simplify(theRoot.getRight());
			simpExp = chooseDepth(new BinaryTreeNode<String>(rootElem, leftNode, rightNode));
		} else if (ExpressionParser.isFunction(rootElem)) {
			leftNode = simplify(theRoot.getLeft());
			simpExp = chooseDepth(new BinaryTreeNode<String>(rootElem, leftNode, null));
		} else { // constant and/or variable
			simpExp = theRoot;
		}

		return simpExp;
	}

	private static BinaryTreeNode<String> chooseDepth(final BinaryTreeNode<String> theRoot) {
		BinaryTreeNode<String> simpExp = theRoot;
		if (theRoot.numChildren() == 2) {
			simpExp = shallowSimplify(theRoot);
		} else if (theRoot.numChildren() > 2) {
			simpExp = deepSimplify(theRoot);
		} // else simpExp = theRoot
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
		// necessary components to simplify the shallow tree root
		final String leftVar = theRoot.getLeft().getElement().replaceAll("[^a-zA-Z]", "");
		final String rightVar = theRoot.getRight().getElement().replaceAll("[^a-zA-Z]", "");
		String leftNumStr = theRoot.getLeft().getElement().replaceAll("[^0-9.]", "");
		String rightNumStr = theRoot.getRight().getElement().replaceAll("[^0-9.]", "");
		if (leftNumStr.isEmpty()) {
			leftNumStr = "1";
		}
		if (rightNumStr.isEmpty()) {
			rightNumStr = "1";
		}
		final String operator = theRoot.getElement();
		final Double numResult = applyOperator(operator, Double.parseDouble(leftNumStr),
		    Double.parseDouble(rightNumStr));
		final String strResult = String.valueOf(numResult);
		BinaryTreeNode<String> simpExp = theRoot;
		// main portion of simplifying
		// if (result is zero or both are constants) and not an exponent and not a minus
		if (numResult == 0.0 || (leftVar.isEmpty() && rightVar.isEmpty())) {
			if (!operator.equals("^") || !operator.equals("-")) {
				simpExp = new BinaryTreeNode<String>(strResult);
			}
		} else if (operator.equals("-") || operator.equals("+")) {
			if (leftVar.equals(rightVar) && !leftVar.isEmpty()) { // both have same vars
				simpExp = new BinaryTreeNode<String>(strResult + leftVar);
			}
		} else if (operator.equals("/")) {
			// both have same vars or only left has var
			if ((leftVar.equals(rightVar) || rightVar.isEmpty()) && !leftVar.isEmpty()) {
				if (numResult == 1.0 && !rightVar.isEmpty()) {
					simpExp = new BinaryTreeNode<String>("1.0");
				} else {
					final BinaryTreeNode<String> leftNode =
					    new BinaryTreeNode<String>(leftNumStr);
					final BinaryTreeNode<String> rightNode =
					    new BinaryTreeNode<String>(rightNumStr);
					final BinaryTreeNode<String> divide =
					    new BinaryTreeNode<String>("/", leftNode, rightNode);
					final BinaryTreeNode<String> var = new BinaryTreeNode<String>(leftVar);
					// simpExp = new BinaryTreeNode<String>("*", divide, var);
					simpExp = simplify(new BinaryTreeNode<String>("*", divide, var));
				}
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
		return simpExp;
	}

	private static BinaryTreeNode<String> deepSimplify(final BinaryTreeNode<String> theRoot) {
		BinaryTreeNode<String> simpExp = theRoot;
		final String rootElem = theRoot.getElement();
		if (ExpressionParser.isFunction(rootElem)) {
			simpExp = simplify(theRoot.getLeft());
		} else if (Differentiator.isOperator(rootElem)) {
			final String leftElem = theRoot.getLeft().getElement();
			final String rightElem = theRoot.getRight().getElement();
			boolean isLeftFunc = ExpressionParser.isFunction(leftElem);
			boolean isRightFunc = ExpressionParser.isFunction(rightElem);
			boolean isLeftOp = Differentiator.isOperator(leftElem);
			boolean isRightOp = Differentiator.isOperator(rightElem);

			if (!isLeftFunc && !isLeftOp) {
				final Double leftNum = Double.parseDouble(leftElem);
				if (rootElem.equals("*")) {
					if (leftNum == 0) {
						simpExp = new BinaryTreeNode<String>("0");
					} else if (leftNum == 1) {
						simpExp = theRoot.getRight();
					}
				} else if (rootElem.equals("/")) {
					if (leftNum == 0) {
						simpExp = new BinaryTreeNode<String>("0");
					}
				}
			} else if (!isRightFunc && !isRightOp) {
				final Double rightNum = Double.parseDouble(rightElem);
				if (rootElem.equals("*")) {
					if (rightNum == 0) {
						simpExp = new BinaryTreeNode<String>("0");
					} else if (rightNum == 1) {
						simpExp = theRoot.getRight();
					}
				} else if (rootElem.equals("/")) {
					if (rightNum == 1) {
						simpExp = theRoot.getLeft();
					}
				}
			} else if (isLeftFunc && !isRightFunc && !isRightOp) {
				if (rootElem.equals("*")) {
					simpExp = new BinaryTreeNode<String>(rightElem + leftElem,
					    theRoot.getLeft().getLeft(), null);
				}
			} else if (isRightFunc && !isLeftFunc && !isLeftOp) {
				if (rootElem.equals("*")) {
					simpExp = new BinaryTreeNode<String>(leftElem + rightElem,
					    theRoot.getRight().getLeft(), null);
				}
			}
		}
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
	private static Double applyOperator(final String theOperator, final Double theLeftOperand,
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
}
