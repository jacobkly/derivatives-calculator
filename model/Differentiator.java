/**
 * Differentiator - Derivatives Calculator
 */

package model;

import structures.BinaryTreeNode;

/**
 * Differentiator differentiates a binary tree representing a symbolic mathematical expression.
 *
 * @author Jacob Klymenko
 * @version 2.0
 */
public class Differentiator {

	/** A private constructor to inhibit external instantiation. */
	private Differentiator() {
		// do nothing
	}

	/**
	 * Returns a binary tree node representing the derivative of the original root's
	 * equivalent expression.
	 *
	 * @param theRoot		the root node representing the expression segment being derived
	 * @param theVarDiff	the chosen variable of differentiation represented by a node
	 * @return a binary tree node representing the derivative of the root's equivalent expression
	 */
	public static BinaryTreeNode<String> derive(final BinaryTreeNode<String> theRoot,
	    final BinaryTreeNode<String> theVarDiff) {
		// System.out.println("\n" + treeToString(theRoot));
		BinaryTreeNode<String> derivative = null;

		if (theRoot != null) { // first base case here - cannot derive null
			final String rootElement = theRoot.getElement();
			if (isOperator(rootElement)) {
				derivative = deriveOperator(theRoot, theVarDiff);
			} else if (ExpressionParser.isFunction(rootElement)) {
				final String leftNodeElem = theRoot.getLeft().getElement();
				if (ExpressionParser.isFunction(leftNodeElem) || isOperator(leftNodeElem)) {
					derivative = chainRule(theRoot, theVarDiff);
				} else {
					if (rootElement.equals("ln") || rootElement.substring(0, 3).equals("log")) {
						derivative = deriveLog(theRoot, theVarDiff);
					} else {
						derivative = deriveTrig(theRoot, theVarDiff);
					}
				}
			} else { // second base case here - a constant or contains variable
				final String varDiffElement = theVarDiff.getElement();
				if (rootElement.matches(".*" + varDiffElement + ".*")) { // contains a variable
					if (rootElement.length() > 1) { // constant * variable of differentiation
						final char empty = Character.MIN_VALUE; // acts as an empty character
						final char varDiff = varDiffElement.charAt(0);
						final String derivativeString = rootElement.replace(varDiff, empty);
						derivative = new BinaryTreeNode<String>(derivativeString);
					} else { // the root is only the variable of differentiation
						derivative = new BinaryTreeNode<String>("1");
					}
				} else { // only contains a constant
					derivative = new BinaryTreeNode<String>("0");
				}
			}
		}
		return derivative;
	}

	/**
	 * Applies the corresponding operator differentiation rule and returns a binary tree node
	 * representing the derivative of the original root's equivalent expression.
	 *
	 * @param theRoot		the root node representing the expression segment being derived
	 * @param theVarDiff	the chosen variable of differentiation represented by a node
	 * @return a binary tree node representing the derivative of the root's equivalent expression
	 */
	private static BinaryTreeNode<String> deriveOperator(final BinaryTreeNode<String> theRoot,
	    final BinaryTreeNode<String> theVarDiff) {
		final String operator = theRoot.getElement();
		final BinaryTreeNode<String> diffLeftNode = derive(theRoot.getLeft(), theVarDiff);
		final BinaryTreeNode<String> diffRightNode = derive(theRoot.getRight(), theVarDiff);
		final BinaryTreeNode<String> leftProduct =
		    new BinaryTreeNode<String>("*", diffLeftNode, theRoot.getRight());
		final BinaryTreeNode<String> rightProduct =
		    new BinaryTreeNode<String>("*", theRoot.getLeft(), diffRightNode);
		BinaryTreeNode<String> derivative = null;
		switch (operator) {
			case "-":
				derivative = new BinaryTreeNode<String>(operator, diffLeftNode, diffRightNode);
				break;
			case "+":
				derivative = new BinaryTreeNode<String>(operator, diffLeftNode, diffRightNode);
				break;
			case "/":
				final BinaryTreeNode<String> numerator =
				    new BinaryTreeNode<String>("-", leftProduct, rightProduct);
				final BinaryTreeNode<String> two = new BinaryTreeNode<String>("2");
				final BinaryTreeNode<String> denominator =
				    new BinaryTreeNode<String>("^", theRoot.getRight(), two);
				derivative = new BinaryTreeNode<String>("/", numerator, denominator);
				break;
			case "*":
				derivative = new BinaryTreeNode<String>("+", leftProduct, rightProduct);
				break;
			case "^":
				final String varDiff = theVarDiff.getElement();
				// checking if chain/exponent rules need to be applied
				if (theRoot.getRight().contains(varDiff, theRoot.getRight())) {
					// apply non-derivative exponent rule and chain rule
					if (theRoot.getLeft().contains(varDiff, theRoot.getLeft())) {
						derivative = chainRule(theRoot, theVarDiff);
					} else { // apply derivative exponent rule
						derivative = exponentRule(theRoot, theVarDiff);
					}
				} else { // else apply power rule
					System.out.println(2);
					final BinaryTreeNode<String> base =
					    new BinaryTreeNode<String>("*", theRoot.getRight(), theRoot.getLeft());
					final BinaryTreeNode<String> one = new BinaryTreeNode<String>("1");
					final BinaryTreeNode<String> decrementPower =
					    new BinaryTreeNode<String>("-", theRoot.getRight(), one);
					derivative = new BinaryTreeNode<String>("^", base, decrementPower);
				}
				break;
		}
		return derivative;
	}

	/**
	 * Returns a binary tree node representing the derivative of simple logarithmic functions
	 * that require no chain rule.
	 *
	 * @param theRoot		the root node representing the logarithmic function being derived
	 * @param theVarDiff	the chosen variable of differentiation represented by a node
	 * @return a binary tree node representing the derivative of the logarithmic function
	 */
	private static BinaryTreeNode<String> deriveLog(final BinaryTreeNode<String> theRoot,
	    final BinaryTreeNode<String> theVarDiff) {
		final BinaryTreeNode<String> one = new BinaryTreeNode<String>("1");
		BinaryTreeNode<String> derivative = null;

		final String rootElement = theRoot.getElement();
		// specified base value -- 1 / (<theVarDiff> * log(<base>))
		if (rootElement.contains("_")) {
			final BinaryTreeNode<String> base =
			    new BinaryTreeNode<String>(rootElement.substring(4));
			final BinaryTreeNode<String> naturalLog = new BinaryTreeNode<String>("ln", base, null);
			final BinaryTreeNode<String> variableLog =
			    new BinaryTreeNode<String>("*", theVarDiff, naturalLog);
			derivative = new BinaryTreeNode<String>("/", one, variableLog);
		} else if (!rootElement.contains("_")) { // no specified base value -- 1 / <theVarDiff>
			derivative = new BinaryTreeNode<String>("/", one, theVarDiff);
		}
		return derivative;
	}

	/**
	 * Returns a binary tree node representing the derivative of simple trigonometric
	 * functions that require no chain rule.
	 *
	 * @param theRoot		the root node representing the trigonometric function being derived
	 * @param theVarDiff	the chosen variable of differentiation represented by a node
	 * @return a binary tree node representing the derivative of the trigonometric function
	 */
	private static BinaryTreeNode<String> deriveTrig(final BinaryTreeNode<String> theRoot,
	    final BinaryTreeNode<String> theVarDiff) {
		final BinaryTreeNode<String> sine =
		    new BinaryTreeNode<String>("sin", theVarDiff, null);
		final BinaryTreeNode<String> secant =
		    new BinaryTreeNode<String>("sec", theVarDiff, null);
		final BinaryTreeNode<String> zero = new BinaryTreeNode<String>("0");
		final BinaryTreeNode<String> two = new BinaryTreeNode<String>("2");
		BinaryTreeNode<String> derivative = null;

		final String rootElement = theRoot.getElement();
		switch (rootElement) {
			case "sin": // cos(<theVariable>)
				derivative = new BinaryTreeNode<String>("cos", theVarDiff, null);
				break;
			case "cos": // 0 - (<theVariable>)
				derivative = new BinaryTreeNode<String>("-", zero, sine);
				break;
			case "tan": // (sec(<theVariable>)) ^ 2
				derivative = new BinaryTreeNode<String>("^", secant, two);
				break;
			case "sec": // (sec(x)) * (tan(x))
				final BinaryTreeNode<String> tangent =
				    new BinaryTreeNode<String>("tan", theVarDiff, null);
				derivative = new BinaryTreeNode<String>("*", secant, tangent);
				break;
			case "csc": // 0 - ((csc(x)) * (cot(x)))
				final BinaryTreeNode<String> cosecant =
				    new BinaryTreeNode<String>("csc", theVarDiff, null);
				final BinaryTreeNode<String> cotangent =
				    new BinaryTreeNode<String>("cot", theVarDiff, null);
				final BinaryTreeNode<String> cscCot =
				    new BinaryTreeNode<String>("*", cosecant, cotangent);
				derivative = new BinaryTreeNode<String>("-", zero, cscCot);
				break;
			case "cot": // -1 / ((sin(x)) ^ 2)
				final BinaryTreeNode<String> negOne = new BinaryTreeNode<String>("-1");
				final BinaryTreeNode<String> sineSquared =
				    new BinaryTreeNode<String>("^", sine, two);
				derivative = new BinaryTreeNode<String>("/", negOne, sineSquared);
				break;
		}
		return derivative;
	}

	/**
	 * Returns a binary tree node after applying the derivative exponent rule to the specified
	 * expression represented in a binary tree node.
	 *
	 * @param theRoot		the root node representing the expression being derived
	 * @param theVarDiff	the chosen variable of differentiation represented by a node
	 * @return a binary tree node representing the derivative of the expression
	 */
	private static BinaryTreeNode<String> exponentRule(final BinaryTreeNode<String> theRoot,
	    final BinaryTreeNode<String> theVarDiff) {
		final BinaryTreeNode<String> naturalLog =
		    new BinaryTreeNode<String>("ln", theRoot.getLeft(), null);
		final BinaryTreeNode<String> derivative =
		    new BinaryTreeNode<String>("*", theRoot, naturalLog);
		return derivative;
	}

	/**
	 * Returns a binary tree node after applying the derivative chain rule to the specified
	 * expression represented in a binary tree node.
	 *
	 * @param theRoot		the root node representing the expression being derived
	 * @param theVarDiff	the chosen variable of differentiation represented by a node
	 * @return a binary tree node representing the derivative of the expression
	 */
	private static BinaryTreeNode<String> chainRule(final BinaryTreeNode<String> theRoot,
	    final BinaryTreeNode<String> theVarDiff) {
		BinaryTreeNode<String> derivative = null;
		if (theRoot.getElement().equals("^")) {
			// apply non-derivative exponent rule to theRoot parameter and create a new root
			final BinaryTreeNode<String> naturalLog =
			    new BinaryTreeNode<String>("ln", theRoot.getLeft(), null);
			final BinaryTreeNode<String> product =
			    new BinaryTreeNode<String>("*", theRoot.getRight(), naturalLog);
			final BinaryTreeNode<String> eulersNum = new BinaryTreeNode<String>("e");
			final BinaryTreeNode<String> newRoot =
			    new BinaryTreeNode<String>("^", eulersNum, product);
			// apply chain rule to the new root
			final BinaryTreeNode<String> diffRightNode = derive(newRoot.getRight(), theVarDiff);
			derivative = new BinaryTreeNode<String>("*", newRoot, diffRightNode);
		} else if (ExpressionParser.isFunction(theRoot.getElement())) {
			final BinaryTreeNode<String> outerFunc =
			    new BinaryTreeNode<String>(theRoot.getElement(), theVarDiff, null);
			BinaryTreeNode<String> diffRoot = derive(outerFunc, theVarDiff);

			diffRoot = diffRoot.findAndReplace(theVarDiff.getElement(), diffRoot,
			    theRoot.getLeft());
			final BinaryTreeNode<String> diffInner = derive(theRoot.getLeft(), theVarDiff);
			derivative = new BinaryTreeNode<String>("*", diffRoot, diffInner);
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
	 * Returns a String representing the binary tree root node and the root's children as a
	 * mathematical expression, recursively.
	 *
	 * Maintains expression readability by adding parentheses surrounding chunks of the tree
	 * nodes and the nodes children (unless at the direct root node or when the node does not
	 * have non-null children).
	 *
	 * @param theRoot 		the root node of this binary tree
	 * @param theTracker	the method to track the recursive call position in the call stack
	 * @return a String representing the binary tree as an expression
	 */
	public static String treeNodeToString(final BinaryTreeNode<String> theRoot,
	    final int theTracker) {
		String result = "";
		int tracker = theTracker;
		if (theTracker == 0) {
			tracker = 1;
		}
		if (theRoot != null) { // one base case - making sure caller does not include null node
			// checks to see if a left parenthesis is needed
			if (theTracker == 1 && theRoot.numChildren() != 0) {
				result += "(";
			}
			final String rootElement = theRoot.getElement();
			if (isOperator(rootElement)) {
				result += treeNodeToString(theRoot.getLeft(), tracker) + " " +
				    rootElement + " " + treeNodeToString(theRoot.getRight(), tracker);
			} else if (ExpressionParser.isFunction(rootElement)) {
				// WRONG - later implement the first pair of parentheses when inside the subtree
				result += rootElement + "(" + treeNodeToString(theRoot.getLeft(), tracker) + ")";
			} else { // second (real) base case - root is a constant or var of differentiation
				result += rootElement;
			}
			// checks to see if a right parenthesis is needed
			if (theTracker == 1 && theRoot.numChildren() != 0) {
				result += ")";
			}
		}
		return result;
	}
}
