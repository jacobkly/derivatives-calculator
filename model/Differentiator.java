/**
 * Differentiator - Derivatives Calculator
 */

package model;

import structures.BinaryTreeNode;

/**
 * Differentiator differentiates a binary tree representing a symbolic mathematical expression.
 *
 * @author Jacob Klymenko
 * @version 2.1
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

		if (theRoot != null) { // first base case - cannot derive null
			final String rootElement = theRoot.getElement();
			if (isOperator(rootElement)) {
				derivative = deriveOperator(theRoot, theVarDiff);
			} else if (ExpressionParser.isFunction(rootElement)) {
				final String leftNodeElem = theRoot.getLeft().getElement();
				final String nonVarDiffElement = getNonVarDiffElement(theRoot, theVarDiff);
				System.out.println(nonVarDiffElement);
				final BinaryTreeNode<String> nonVarDiffNode =
				    new BinaryTreeNode<String>(nonVarDiffElement);
				if (ExpressionParser.isFunction(leftNodeElem) || isOperator(leftNodeElem)) {
					derivative = chainRule(theRoot, theVarDiff);
				} else if (theRoot.getLeft().getElement().equals(nonVarDiffElement)) {
					BinaryTreeNode<String> funcDiff = null;
					if (rootElement.equals("ln") || rootElement.substring(0, 3).equals("log")) {
						funcDiff = deriveLog(theRoot, nonVarDiffNode);
					} else if (rootElement.substring(0, 3).equals("arc")) {
						funcDiff = deriveInverseTrig(theRoot, nonVarDiffNode);
					} else {
						funcDiff = deriveTrig(theRoot, nonVarDiffNode);
					}
					final BinaryTreeNode<String> nonVarDiffLeibniz =
					    new BinaryTreeNode<String>("d" + nonVarDiffElement + "/d" +
					        theVarDiff.getElement());
					derivative = new BinaryTreeNode<String>("*", nonVarDiffLeibniz, funcDiff);
				} else {
					if (rootElement.equals("ln") || rootElement.substring(0, 3).equals("log")) {
						derivative = deriveLog(theRoot, theVarDiff);
					} else if (rootElement.substring(0, 3).equals("arc")) {
						derivative = deriveInverseTrig(theRoot, theVarDiff);
					} else {
						derivative = deriveTrig(theRoot, theVarDiff);
					}
				}
			} else { // second (real) base case - a constant or contains variable
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
					// there is another var other than the var of diff
				} else if (rootElement.matches(".*[a-zA-Z&&[^" + varDiffElement + "]].*")) {
					derivative = new BinaryTreeNode<String>("dy/dx");
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
				final String varDiffElement = theVarDiff.getElement();
				// filtering for the second variable in the expression.
				final String nonVarDiffElement = getNonVarDiffElement(theRoot, theVarDiff);
				// checking if chain/exponent rules need to be applied
				if (theRoot.getRight().contains(varDiffElement, theRoot.getRight())) {
					// apply non-derivative exponent rule and chain rule
					if (theRoot.getLeft().contains(varDiffElement, theRoot.getLeft())) {
						derivative = chainRule(theRoot, theVarDiff);
					} else { // apply derivative exponent rule
						derivative = exponentRule(theRoot, theVarDiff);
					}
					// there is another variable other than the var of diff
				} else if (theRoot.contains(nonVarDiffElement, theRoot)) {
					final BinaryTreeNode<String> dydx = new BinaryTreeNode<String>("dy/dx");
					final BinaryTreeNode<String> notVarDiff =
					    new BinaryTreeNode<String>(nonVarDiffElement);
					final boolean left = theRoot.getLeft().contains(nonVarDiffElement,
					    theRoot.getLeft());
					final boolean right = theRoot.getRight().contains(nonVarDiffElement,
					    theRoot.getRight());
					if (left && right) { // chain rule is needed
						derivative =
						    new BinaryTreeNode<String>("*", dydx,
						        chainRule(theRoot, notVarDiff));
					} else if (!left && right) { // derivative exponent rule is needed
						derivative =
						    new BinaryTreeNode<String>("*", dydx,
						        exponentRule(theRoot, notVarDiff));
					} else { // power rule is needed
						final BinaryTreeNode<String> powerRule = powerRule(theRoot, notVarDiff);
						derivative = new BinaryTreeNode<String>("*", dydx, powerRule);
					}
				} else { // else apply simple power rule
					derivative = powerRule(theRoot, theVarDiff);
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
		final BinaryTreeNode<String> cosecant =
		    new BinaryTreeNode<String>("csc", theVarDiff, null);
		final BinaryTreeNode<String> zero = new BinaryTreeNode<String>("0");
		final BinaryTreeNode<String> two = new BinaryTreeNode<String>("2");
		BinaryTreeNode<String> derivative = null;

		final String rootElement = theRoot.getElement();
		switch (rootElement) {
			case "sin": // cos(<theVariable>)
				derivative = new BinaryTreeNode<String>("cos", theVarDiff, null);
				break;
			case "cos": // 0 - sin(<theVariable>)
				derivative = new BinaryTreeNode<String>("-", zero, sine);
				break;
			case "tan": // sec(<theVariable>) ^ 2
				derivative = new BinaryTreeNode<String>("^", secant, two);
				break;
			case "sec": // (sec(x)) * (tan(x))
				final BinaryTreeNode<String> tangent =
				    new BinaryTreeNode<String>("tan", theVarDiff, null);
				derivative = new BinaryTreeNode<String>("*", secant, tangent);
				break;
			case "csc": // 0 - ((csc(x)) * (cot(x)))
				final BinaryTreeNode<String> cotangent =
				    new BinaryTreeNode<String>("cot", theVarDiff, null);
				final BinaryTreeNode<String> cscCot =
				    new BinaryTreeNode<String>("*", cosecant, cotangent);
				derivative = new BinaryTreeNode<String>("-", zero, cscCot);
				break;
			case "cot": // 0 - (csc(x) ^ 2)

				final BinaryTreeNode<String> exponent =
				    new BinaryTreeNode<String>("^", cosecant, two);
				derivative = new BinaryTreeNode<String>("-", zero, exponent);
				break;
		}
		return derivative;
	}

	/**
	 * Returns a binary tree node representing the derivative of simple inverse trigonometric
	 * functions that require no chain rule.
	 *
	 * @param theRoot		the root node representing the inverse trigonometric function
	 * 						being derived
	 * @param theVarDiff	the chosen variable of differentiation represented by a node
	 * @return a binary tree node representing the derivative of the inverse trigonometric function
	 */
	private static BinaryTreeNode<String> deriveInverseTrig(
	    final BinaryTreeNode<String> theRoot, final BinaryTreeNode<String> theVarDiff) {
		final BinaryTreeNode<String> zero = new BinaryTreeNode<String>("0");
		final BinaryTreeNode<String> one = new BinaryTreeNode<String>("1");
		final BinaryTreeNode<String> two = new BinaryTreeNode<String>("2");
		final BinaryTreeNode<String> half =
		    new BinaryTreeNode<String>("/", one, two);
		final BinaryTreeNode<String> varSquared =
		    new BinaryTreeNode<String>("^", theVarDiff, two);
		final BinaryTreeNode<String> oneMinusVar =
		    new BinaryTreeNode<String>("-", one, varSquared);
		final BinaryTreeNode<String> varPlusOne =
		    new BinaryTreeNode<String>("+", varSquared, one);
		final BinaryTreeNode<String> varMinusOne =
		    new BinaryTreeNode<String>("-", varSquared, one);
		final BinaryTreeNode<String> sqrtOneMinusVar =
		    new BinaryTreeNode<String>("^", oneMinusVar, half);
		final BinaryTreeNode<String> sqrtVarMinusOne =
		    new BinaryTreeNode<String>("^", varMinusOne, half);
		final BinaryTreeNode<String> absoluteVar =
		    new BinaryTreeNode<String>("abs", theVarDiff, null);
		final BinaryTreeNode<String> absoVarProduct =
		    new BinaryTreeNode<String>("*", absoluteVar, sqrtVarMinusOne);
		BinaryTreeNode<String> derivative = null;

		final String rootElement = theRoot.getElement();
		switch (rootElement) {
			case "arcsin": // 1 / ((1 - (x ^ 2)) ^ (1 / 2))
				derivative = new BinaryTreeNode<String>("/", one, sqrtOneMinusVar);
				break;
			case "arccos": // 0 - (1 / ((1 - (x ^ 2)) ^ (1 / 2)))
				final BinaryTreeNode<String> arcsinDiff =
				    new BinaryTreeNode<String>("/", one, sqrtOneMinusVar);
				derivative = new BinaryTreeNode<String>("-", zero, arcsinDiff);
				break;
			case "arctan": // 1 / ((x ^ 2) + 1)
				derivative = new BinaryTreeNode<String>("/", one, varPlusOne);
				break;
			case "arcsec": // 1 / (abs(x) * (((x ^ 2) - 1) ^ (1 / 2))
				derivative = new BinaryTreeNode<String>("/", one, absoVarProduct);
				break;
			case "arccsc": // 0 - (1 / (abs(x) * (((x ^ 2) - 1) ^ (1 / 2)))
				final BinaryTreeNode<String> arcsecDiff =
				    new BinaryTreeNode<String>("/", one, absoVarProduct);
				derivative = new BinaryTreeNode<String>("-", zero, arcsecDiff);
				break;
			case "arccot": // 0 - (1 / ((x ^ 2) + 1))
				final BinaryTreeNode<String> arctanDiff =
				    new BinaryTreeNode<String>("/", one, varPlusOne);
				derivative = new BinaryTreeNode<String>("-", zero, arctanDiff);
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
	 * Returns a binary tree node after applying the derivative power rule to the specified
	 * expressions represented in a binary tree node.
	 *
	 * @param theRoot		the root node representing the expression being derived
	 * @param theVarDiff	the chosen variable of differentiation represented by a node
	 * @return a binary tree node representing the derivative of the expression
	 */
	private static BinaryTreeNode<String> powerRule(final BinaryTreeNode<String> theRoot,
	    final BinaryTreeNode<String> theVarDiff) {
		final BinaryTreeNode<String> base =
		    new BinaryTreeNode<String>("*", theRoot.getRight(), theRoot.getLeft());
		final BinaryTreeNode<String> one = new BinaryTreeNode<String>("1");
		final BinaryTreeNode<String> decrement =
		    new BinaryTreeNode<String>("-", theRoot.getRight(), one);
		return new BinaryTreeNode<String>("^", base, decrement);
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
	 * Returns a String representing the first variable in the specified binary tree node root
	 * other than the specified variable of differentiation.
	 *
	 * @param theRoot		the root node of the expression
	 * @param theVarDiff	the chosen variable of differentiation represented by a node
	 * @return a String representing a variable other than the variable of differentiation
	 */
	private static String getNonVarDiffElement(final BinaryTreeNode<String> theRoot,
	    final BinaryTreeNode<String> theVarDiff) {
		String result = "";

		if (ExpressionParser.isFunction(theRoot.getElement())) {
			return getNonVarDiffElement(theRoot.getLeft(), theVarDiff);
		}
		final String varDiffElem = theVarDiff.getElement();
		result = treeNodeToString(theRoot, 0).replaceAll("[^a-zA-Z&&[^" +
		    varDiffElem + "]]", "");
		if (result.length() > 0) {
			result = Character.toString(result.charAt(0));
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
			if (theTracker == 1 && theRoot.numChildren() > 1) {
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
			if (theTracker == 1 && theRoot.numChildren() > 1) {
				result += ")";
			}
		}
		return result;
	}
}
