/**
 * ExpressionParser - Derivatives Calculator
 */

package model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import structures.BinaryTree;

/**
 * ExpressionParser implements the shunting yard algorithm by Edsger Dijkstra to parse
 * symbolic expressions.
 *
 * @author Jacob Klymenko
 * @version 3.0
 */
public class ExpressionParser {

	/** Keeps track if the input expression is valid. */
	private static boolean myIsValid = true;

	/** A set containing the accepted mathematical functions. */
	private static HashSet<String> myFunctions = new HashSet<String>();

	/** A private constructor to inhibit external instantiation. */
	private ExpressionParser() {
		// do nothing
	}

	/**
	 * Sets all the valid mathematical functions this calculator accepts from the user.
	 */
	public static void setValidFunctions() {
		myFunctions.add("abs");
		myFunctions.add("sin");
		myFunctions.add("cos");
		myFunctions.add("tan");
		myFunctions.add("sec");
		myFunctions.add("csc");
		myFunctions.add("cot");
		myFunctions.add("arcsin");
		myFunctions.add("arccos");
		myFunctions.add("arctan");
		myFunctions.add("arcsec");
		myFunctions.add("arccsc");
		myFunctions.add("arccot");
		myFunctions.add("log");
		myFunctions.add("ln");
	}

	/**
	 * Parses the user inputed expression and adds each individual parenthesis and substring
	 * blocks between the space character (and before/after parentheses) into a list.
	 *
	 * @param theUserInput the user inputed symbolic expression
	 * @return a list with the user expression separated in infix notation order
	 */
	public static ArrayList<String> stringToList(final String theUserInput) {
		final String userInput = theUserInput.replace("/^\\s+|\\s+$/g", "");
		ArrayList<String> result = new ArrayList<>();
		Map<Integer, String> spaces = new HashMap<Integer, String>();
		// keeps track of all the space characters from the input string in a map
		for (int i = 0; i < userInput.length(); i++) {
			if (userInput.charAt(i) == ' ') {
				spaces.put(i, " ");
			}
		}
		int track = 0;
		for (int j = 0; j < userInput.length(); j++) {
			char curr = userInput.charAt(j);
			String sub = userInput.substring(track, j);
			if (curr == '(' || curr == ')') {
				// adds the corresponding substring before/after the parenthesis to the list
				if (track != j) {
					result.add(sub);
				}
				// adds the parenthesis to the list
				result.add(Character.toString(userInput.charAt(j)));
				track = j + 1;
				// adds the substring block between the space characters to the list
			} else if (spaces.containsKey(j)) {
				if (userInput.charAt(track) != ' ') {
					result.add(sub);
				}
				track = j + 1;
			}
		}
		if (track != userInput.length()) {
			result.add(userInput.substring(track));
		}
		System.out.println(result);
		return result;
	}

	/**
	 * This implementation of the shunting yard algorithm parses a list containing the user
	 * expression in infix notation, and converts it to a binary tree.
	 *
	 * @param theInfixList the infix notation expression displayed as a List
	 * @return the infix notation expression represented in a binary tree
	 */
	public static BinaryTree<String> shuntingYardTree(final ArrayList<String> theInfixList) {
		Deque<BinaryTree<String>> operandStack = new ArrayDeque<BinaryTree<String>>();
		Deque<String> operatorStack = new ArrayDeque<String>();
		myIsValid = true;

		for (String s : theInfixList) {
			if (s.matches("-?\\d+(\\.\\d+)?") ||
			    (!isFunction(s) && s.matches(".*[a-zA-Z].*"))) {
				operandStack.push(new BinaryTree<String>(s));
			} else if (isFunction(s)) {
				operatorStack.push(s);
			} else if (s.charAt(0) == '(') {
				operatorStack.push(s);
			} else if (s.charAt(0) == ')') {
				try {
					while (!operatorStack.peek().equals("(") && !operatorStack.isEmpty() &&
					    !isFunction(operatorStack.peek())) {
						// operator becomes the root of two binary trees
						mergeTrees(operandStack, operatorStack.pop());
					}
					if (operatorStack.peek().equals("(")) {
						operatorStack.pop(); // discard this element
					}
					if (operatorStack.peek() != null && isFunction(operatorStack.peek())) {
						// function becomes the root of one binary tree
						mergeTrees(operandStack, operatorStack.pop());
					}
				} catch (final Exception theError) {
					setIsValid(false);
					break;
				}
			} else { // the string s is an operator
				// handles the order to adding operators into the tree
				while (!operatorStack.isEmpty() && getPrecedence(s) > 0 &&
				    (getPrecedence(operatorStack.peek()) > getPrecedence(s) ||
				        (getPrecedence(operatorStack.peek()) == getPrecedence(s) &&
				            isLeftAssociative(s)))) {
					mergeTrees(operandStack, operatorStack.pop());
				}
				operatorStack.push(s);
			}
		}
		// adds/merges the rest of the operators into the tree as roots unless there is a
		// misplaced parenthesis in the infix list (the user inputed expression)
		while (!operatorStack.isEmpty() && !operandStack.isEmpty()) {
			String top = operatorStack.peek();
			if (top.equals("(")) {
				setIsValid(false);
				break;
			} else if (getPrecedence(top) > 0 || isFunction(top)) {
				mergeTrees(operandStack, operatorStack.pop());
			}
		}
		System.out.println(operandStack.peek().toString());
		return operandStack.peek();
	}

	/**
	 * Returns true if the string is a function, otherwise false.
	 *
	 * @param theString the string being examined as a function
	 * @return true if the string is a function; otherwise false
	 */
	public static boolean isFunction(final String theString) {
		boolean result = false;
		final int length = theString.length();
		if (length == 2 && myFunctions.contains(theString)) { // ln
			result = true;
		} else if (length == 3 && myFunctions.contains(theString)) { // all normal trig and log
			result = true;
		} else if (length > 4) {
			if (myFunctions.contains(theString.substring(0, 3))) { // log_<base>
				result = true;
			} else if (myFunctions.contains(theString)) { // inverse trig
				result = true;
			}
		} else {
			result = false;
		}
		return result;
	}

	/**
	 * Merges the one or two last binary trees in the operand stack as children to the new
	 * root, the operator or function.
	 *
	 * @param theOperandStack	the operand stack containing binary trees
	 * @param theString			the function or operator becoming the new root in the tree
	 */
	private static void mergeTrees(final Deque<BinaryTree<String>> theOperandStack,
	    final String theString) {
		// System.out.println(theOperandStack.peek().getNodeElement());
		if (isFunction(theString)) {
			final BinaryTree<String> leftSubTree = theOperandStack.pop();
			theOperandStack.push(new BinaryTree<String>(theString, leftSubTree, null));
		} else { // else theString is an operator
			final BinaryTree<String> rightSubTree = theOperandStack.pop();
			final BinaryTree<String> leftSubTree = theOperandStack.pop();
			theOperandStack.push(new BinaryTree<String>(theString, leftSubTree, rightSubTree));
		}
	}

	/**
	 * Sets the class boolean myIsValid to either true or false.
	 *
	 * @param theBoolean the boolean value
	 */
	private static void setIsValid(final boolean theBoolean) {
		myIsValid = theBoolean;
	}

	/**
	 * Returns the value of class boolean myIsValid.
	 *
	 * @return the value of class boolean myIsValid
	 */
	public static boolean getIsValid() {
		return myIsValid;
	}

	/**
	 * Returns the precedence of the operator as an integer. Precedence; meaning the order in
	 * which operators are evaluated in an expression. The greater the integer, the higher the
	 * precedence of the operator. If the string parameter is not an operator, return -1.
	 *
	 * @param theOperator the operator
	 * @return the precedence of the operator as an integer
	 */
	private static int getPrecedence(final String theOperator) {
		int precedence = -1;
		switch (theOperator) {
			case "-":
				precedence = 2;
				break;
			case "+":
				precedence = 2;
				break;
			case "/":
				precedence = 3;
				break;
			case "*":
				precedence = 3;
				break;
			case "^":
				precedence = 4;
				break;
		}
		return precedence;
	}

	/**
	 * Returns true if the operator is left associative; otherwise false. Left associative
	 * operators; meaning the operators of the same precedence are evaluated in the order from
	 * left to right.
	 *
	 * @param theOperator the operator
	 * @return true if the operator is left associative; otherwise false
	 */
	private static boolean isLeftAssociative(final String theOperator) {
		boolean result;
		if (theOperator == "-" || theOperator == "+" ||
		    theOperator == "/" || theOperator == "*") {
			result = true;
		} else {
			result = false;
		}
		return result;
	}
}
