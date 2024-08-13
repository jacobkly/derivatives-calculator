/**
 * Differentiator - Derivatives Calculator
 */

package model;

import structures.BinaryTreeNode;

/**
 * Differentiator differentiates a binary tree representing a symbolic mathematical expression.
 *
 * @author Jacob Klymenko
 * @version 2.2
 */
public class Differentiator {

	/** A String representing the variable other than the variable of differentiation. */
	private static String myNonVarDiffElement;

	/** A BinaryTreeNode representing the variable other than the variable of differentiation. */
	private static BinaryTreeNode<String> myNonVarDiffNode = null;

	/**
	 * A BinaryTreeNode representing Leibniz's notation containing the variable other than the
	 * variable of differentiation
	 */
	private static BinaryTreeNode<String> myNonVarDiffLeibniz = null;

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
		myNonVarDiffElement = null;
		myNonVarDiffNode = null;
		myNonVarDiffLeibniz = null;
		setNonVarDiffComponents(theRoot, theVarDiff);
		BinaryTreeNode<String> derivative = null;

		if (theRoot != null) { // first base case - cannot derive null
			final String rootElement = theRoot.getElement();
			if (isOperator(rootElement)) {
				derivative = deriveOperator(theRoot, theVarDiff);
			} else if (ExpressionParser.isFunction(rootElement)) {
				final String leftNodeElem = theRoot.getLeft().getElement();
				if (ExpressionParser.isFunction(leftNodeElem) || isOperator(leftNodeElem)) {
					derivative = chainRule(theRoot, theVarDiff);
				} else if (theRoot.getLeft().getElement().equals(myNonVarDiffElement)) {
					BinaryTreeNode<String> funcDiff = null;
					if (rootElement.equals("ln") || rootElement.substring(0, 3).equals("log")) {
						funcDiff = deriveLog(theRoot, myNonVarDiffNode);
					} else if (rootElement.substring(0, 3).equals("arc")) {
						funcDiff = deriveInverseTrig(theRoot, myNonVarDiffNode);
					} else {
						funcDiff = deriveTrig(theRoot, myNonVarDiffNode);
					}
					derivative = new BinaryTreeNode<String>("*", myNonVarDiffLeibniz, funcDiff);
				} else if (theRoot.getLeft().getElement().equals(theVarDiff.getElement())) {
					if (rootElement.equals("ln") || rootElement.substring(0, 3).equals("log")) {
						derivative = deriveLog(theRoot, theVarDiff);
					} else if (rootElement.substring(0, 3).equals("arc")) {
						derivative = deriveInverseTrig(theRoot, theVarDiff);
					} else {
						derivative = deriveTrig(theRoot, theVarDiff);
					}
				} else {
					derivative = new BinaryTreeNode<String>("0");
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
					derivative = myNonVarDiffLeibniz;
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
				derivative = deriveExponent(theRoot, theVarDiff);
		}
		return derivative;
	}

	/**
	 * Helper method to take the derivative of an expression whose main mathematical operator
	 * is an exponent sign.
	 *
	 * @param theRoot		the root node representing the expression segment being derived
	 * @param theVarDiff	the chosen variable of differentiation represented by a node
	 * @return a binary tree node representing the derivative of the root's equivalent expression
	 */
	private static BinaryTreeNode<String> deriveExponent(final BinaryTreeNode<String> theRoot,
	    final BinaryTreeNode<String> theVarDiff) {
		BinaryTreeNode<String> derivative = null;
		// necessary components to filter out what rules need to be applied
		final String varDiffElement = theVarDiff.getElement();
		final boolean varDiffLeftNode = theRoot.getLeft().contains(varDiffElement,
		    theRoot.getLeft());
		final boolean varDiffRightNode = theRoot.getRight().contains(varDiffElement,
		    theRoot.getRight());
		final boolean nonVarDiffLeftNode = theRoot.getLeft().contains(myNonVarDiffElement,
		    theRoot.getLeft());
		final boolean nonVarDiffRightNode = theRoot.getRight().contains(myNonVarDiffElement,
		    theRoot.getRight());

		if (!varDiffLeftNode && !nonVarDiffLeftNode) { // left side contains only a constant
			if (!varDiffRightNode && !nonVarDiffRightNode) { // both sides are constants\
				System.out.println("second error --- " + myNonVarDiffNode.getElement() + " --- "
				    + nonVarDiffRightNode);
				derivative = new BinaryTreeNode<String>("0");
			} else if (varDiffRightNode && !nonVarDiffRightNode) { // right side contains var diff
				derivative = chainRule(theRoot, theVarDiff);
			} else if (!varDiffRightNode && nonVarDiffRightNode) { // right side contains other var
				final BinaryTreeNode<String> chainRule =
				    chainRule(theRoot, myNonVarDiffNode);
				derivative = new BinaryTreeNode<String>("*", myNonVarDiffLeibniz, chainRule);
			} else { // right side contains two different variables
				derivative = chainRule(theRoot, theVarDiff);
			}
		} else if ((varDiffLeftNode || nonVarDiffLeftNode) &&
		    (varDiffRightNode || nonVarDiffRightNode)) { // both sides contain some variable
			    derivative = chainRule(theRoot, theVarDiff);
		    } else { // left side contains a variable and right side contains a constant
			    if (varDiffLeftNode) {
				    derivative = powerRule(theRoot, theVarDiff);
			    } else {
				    final BinaryTreeNode<String> powerRule = powerRule(theRoot, myNonVarDiffNode);
				    derivative = new BinaryTreeNode<String>("*", myNonVarDiffLeibniz, powerRule);
			    }
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
		// necessary repetitive node components needed to build the new differentiated expression
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
	 * Sets the important global variables representing different forms of a variable in the
	 * specified binary tree node root, other than the specified variable of differentiation.
	 *
	 * @param theRoot		the root node of the expression
	 * @param theVarDiff	the chosen variable of differentiation represented by a node
	 */
	public static void setNonVarDiffComponents(final BinaryTreeNode<String> theRoot,
	    final BinaryTreeNode<String> theVarDiff) {
		if (myNonVarDiffElement == null) {
			if (ExpressionParser.isFunction(theRoot.getElement())) {
				setNonVarDiffComponents(theRoot.getLeft(), theVarDiff);
			} else {
				final String varDiffElem = theVarDiff.getElement();
				myNonVarDiffElement = treeNodeToString(theRoot, 0).replaceAll("[^a-zA-Z&&[^" +
				    varDiffElem + "]]", "");
				// System.out.println(myNonVarDiffElement + " --- hello there");
				if (myNonVarDiffElement.length() > 0) {
					myNonVarDiffElement = Character.toString(myNonVarDiffElement.charAt(0));
				}
				myNonVarDiffNode = new BinaryTreeNode<String>(myNonVarDiffElement);
				myNonVarDiffLeibniz = new BinaryTreeNode<String>("d" + myNonVarDiffElement +
				    "/d" + theVarDiff.getElement());
			}
		}
		// System.out.println(myNonVarDiffElement + "what");
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
