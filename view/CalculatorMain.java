/**
 * CalculatorMain - Derivates Calculator
 */

package view;

import java.util.ArrayList;
import java.util.Scanner;
import model.Differentiator;
import model.ExpressionParser;
import structures.BinaryTree;

/**
 * A console-based program to perform symbolic differentiation on mathematical expressions.
 *
 * @author Jacob Klymenko
 * @version 3.0
 */
public class CalculatorMain {

	/** The user inputed infix notation arithmetic expression. */
	private static String myUserInput = "";

	private static boolean myUserQuitOption = false;

	/** A private constructor to inhibit external instantiation. */
	private CalculatorMain() {
		// do nothing
	}

	/**
	 * The start point for this program.
	 *
	 * @param theArgs command line arguments - ignored
	 */
	public static void main(String[] args) {
		System.out.println("derivates calculator... STAAARRT!!!");
		ExpressionParser.setValidFunctions();
		try (Scanner console = new Scanner(System.in)) {
			for (;;) {
				start(console);
				if (myUserQuitOption) {
					break;
				}
			}
		}
		System.out.println("\nthank you for trying out this calculator!");
	}

	/**
	 * Starts the first round of asking for and differentiating a symbolic expression. Both
	 * methods of parsing and differentiating are shown to the user.
	 *
	 * @param theConsole a Scanner used to gather user input
	 */
	private static void start(final Scanner theConsole) {
		final String prompt = "\nenter an expression (or \"q\" to quit): ";
		System.out.print(prompt);
		myUserInput = theConsole.nextLine();
		final BinaryTree<String> tree = getTree(theConsole, prompt);
		if (myUserQuitOption) {
			return;
		} else {
			try {
				final String outputTree = Differentiator.derive(tree.getNode(), "x");
				// final String output = Differentiator.treeToString(outputTree);
				System.out.println("\n" + myUserInput + " = " + outputTree);
			} catch (final Exception error) {
				System.out.println("evaluation error has occured!");
			}
		}
	}

	/**
	 * Repeatedly waits for an expression in infix notation to be entered by the user. It
	 * runs the first input and after some parsing, feeds it to the shunting yard algorithm.
	 *
	 * This method also handles misplaced parentheses by asking the user to input once more.
	 *
	 * @param theConsole	a Scanner used to gather user input
	 * @param thePrompt		the prompt to display to the user repeatedly
	 * @return the user inputed infix notation expression represented in a binary tree
	 */
	private static BinaryTree<String> getTree(final Scanner theConsole,
	    final String thePrompt) {
		BinaryTree<String> quit = null;
		if (isUserQuitting(myUserInput)) {
			return quit;
		}
		ArrayList<String> infixList = ExpressionParser.stringToList(myUserInput);
		BinaryTree<String> expTree = ExpressionParser.shuntingYardTree(infixList);
		while (!ExpressionParser.getIsValid()) {
			System.out.println("not a valid arithmetic expression. \nyour input may " +
			    "contain misplaced parentheses. \n\nplease try again.");
			System.out.print(thePrompt);
			myUserInput = theConsole.nextLine();
			if (isUserQuitting(myUserInput)) {
				return quit;
			}
			infixList = ExpressionParser.stringToList(myUserInput);
			expTree = ExpressionParser.shuntingYardTree(infixList);
		}
		return expTree;
	}

	/**
	 * Return true if the user input is an upper or lower case 'q'. Otherwise return false.
	 *
	 * @param theUserInput the user's input
	 * @return true if the user input is an upper or lower case 'q'; otherwise false
	 */
	private static boolean isUserQuitting(final String theUserInput) {
		if (myUserInput.equals("Q") || myUserInput.equals("q")) {
			myUserQuitOption = true;
			return true;
		} else {
			myUserQuitOption = false;
			return false;
		}
	}
}
