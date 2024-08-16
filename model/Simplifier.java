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
 * @version 2.1
 */
public class Simplifier {

	/** A private constructor to inhibit external instantiation. */
	private Simplifier() {
		// do nothing
	}

	/**
	 * Returns a binary tree node representing the most simplified form, of the mathematical
	 * expression from the specified root, possible by this Simplifier.
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
			simpExp = simplifyOperator(new BinaryTreeNode<String>(rootElem, leftNode, rightNode));
		} else if (ExpressionParser.isFunction(rootElem)) {
			simpExp = new BinaryTreeNode<String>(rootElem, simplify(theRoot.getLeft()), null);
		} else { // constant and/or variable
			simpExp = theRoot;
		}

		return simpExp;
	}

	/**
	 * Returns a binary tree node representing a simplified form of the mathematical
	 * expression of the specified root's equivalent expression. The specified root contains
	 * an operator, and the left and right nodes will be simplified according to the contents.
	 *
	 * @param theRoot the root node representing the expression segment being simplified
	 * @return a binary tree node representing the simplified expression of the specified root
	 */
	private static BinaryTreeNode<String> simplifyOperator(final BinaryTreeNode<String> theRoot) {

		// check if either sides are functions
		final String leftElem = theRoot.getLeft().getElement();
		final String rightElem = theRoot.getRight().getElement();
		if (ExpressionParser.isFunction(leftElem) || ExpressionParser.isFunction(rightElem)) {
			return simplifyOpWithFunc(theRoot);
		}

		// if condition stands, no further simplifying is possible by this Simplifier
		if (Differentiator.isOperator(leftElem) || Differentiator.isOperator(rightElem)) {
			return theRoot;
		}

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

	/**
	 * Return a Double value which occurs when applying the specified mathematical operator on
	 * the specified operands, respecting associativity rules.
	 *
	 * @param theOperator		the mathematical operator represented as a String
	 * @param theLeftOperand	the left operand represented as a Double
	 * @param theRightOperand	the right operand represented as a Double
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

	/**
	 * Returns a binary tree node representing a simplified form of the mathematical
	 * expression of the specified root's equivalent expression. The specified root contains
	 * an operator, and either the left or right node contains a function. Only the simple
	 * simplifying methods are done here.
	 *
	 * @param theRoot the root node representing the expression segment being simplified
	 * @return a binary tree node representing the simplified expression of the specified root
	 */
	private static BinaryTreeNode<String> simplifyOpWithFunc(final BinaryTreeNode<String> theRoot) {
		BinaryTreeNode<String> simpExp = theRoot;
		final String operator = theRoot.getElement();
		final String leftElem = theRoot.getLeft().getElement();
		final String rightElem = theRoot.getRight().getElement();
		boolean isLeftFunc = ExpressionParser.isFunction(leftElem);
		boolean isRightFunc = ExpressionParser.isFunction(rightElem);
		boolean isLeftOp = Differentiator.isOperator(leftElem);
		boolean isRightOp = Differentiator.isOperator(rightElem);
		boolean isLeftVar = leftElem.replaceAll("[0-9.]", "").length() != 0;
		boolean isRightVar = rightElem.replaceAll("[0-9.]", "").length() != 0;

		final boolean isLeftNumVar = !isLeftFunc && !isLeftOp;
		final boolean isRightNumVar = !isRightFunc && !isRightOp;
		if (operator.equals("*")) {
			// opposite must be a function
			if (isLeftNumVar) { // left is constant and/or variable
				if (!isLeftVar) {
					final double leftNum = Double.parseDouble(leftElem);
					if (leftNum == 0) {
						simpExp = new BinaryTreeNode<String>("0");
					} else if (leftNum == 1) {
						simpExp = simplify(theRoot.getRight());
					}
				} else {
					simpExp = new BinaryTreeNode<String>(leftElem + rightElem,
					    simplify(theRoot.getRight().getLeft()), null);
				}
			} else if (isRightNumVar) { // right is constant and/or variable
				if (!isRightVar) {
					final double rightNum = Double.parseDouble(rightElem);
					if (rightNum == 0) {
						simpExp = new BinaryTreeNode<String>("0");
					} else if (rightNum == 1) {
						simpExp = simplify(theRoot.getLeft());
					}
				} else {
					simpExp = new BinaryTreeNode<String>(rightElem + leftElem,
					    simplify(theRoot.getLeft().getLeft()), null);
				}
			}
		} else if (operator.equals("/")) {
			if (isLeftNumVar && !isLeftVar) { // left is a constant
				final double leftNum = Double.parseDouble(leftElem);
				if (leftNum == 0) {
					simpExp = new BinaryTreeNode<String>("0");
				}
			} else if (isRightNumVar && !isRightVar) { // right is a constant
				final double rightNum = Double.parseDouble(rightElem);
				if (rightNum == 1) {
					simpExp = simplify(theRoot.getLeft());
				}
			}
		} else if (operator.equals("+")) {
			if (isLeftNumVar && !isLeftVar) { // left is a constant
				final double leftNum = Double.parseDouble(leftElem);
				if (leftNum == 0) {
					simpExp = simplify(theRoot.getRight());
				}
			} else if (isRightNumVar && !isRightVar) { // right is a constant
				final double rightNum = Double.parseDouble(rightElem);
				if (rightNum == 0) {
					simpExp = simplify(theRoot.getLeft());
				}
			}
		} else if (operator.equals("-")) {
			if (isRightNumVar && !isRightVar) { // right is a constant
				final double rightNum = Double.parseDouble(rightElem);
				if (rightNum == 0) {
					simpExp = simplify(theRoot.getLeft());
				}
			}
		} else if (operator.equals("^")) {
			if (isLeftNumVar && !isLeftVar) { // left is a constant
				final double leftNum = Double.parseDouble(leftElem);
				if (leftNum == 0) {
					simpExp = new BinaryTreeNode<String>("0");
				} else if (leftNum == 1) {
					simpExp = new BinaryTreeNode<String>("1");
				}
			} else if (isRightNumVar && !isRightVar) { // right is a constant
				final double rightNum = Double.parseDouble(rightElem);
				if (rightNum == 0) {
					simpExp = new BinaryTreeNode<String>("1");
				} else if (rightNum == 1) {
					simpExp = simplify(theRoot.getLeft());
				}
			}
		}
		return simpExp;
	}

}
