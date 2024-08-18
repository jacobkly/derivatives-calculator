/**
 * OutputLengthTest - Derivatives Calculator
 */

package tests;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;
import model.Differentiator;
import model.ExpressionParser;
import model.Simplifier;
import structures.BinaryTree;
import structures.BinaryTreeNode;

/**
 * This class calculates two average percentages representing the difference in lengths
 * between the outputs of a mathematical expression from the Differentiator class and the
 * Simplifier class. One average is derived from the number of nodes in the output binary tree,
 * and the second average is based on the length of the output String. The goal of this
 * calculation is to show the improved readability achieved through the Simplifier class.
 *
 * @author Jacob Klymenko
 * @version 1.0
 */
public class OutputLengthTest {

	/** The File containing the list of expressions to be differentiated and simplified. */
	private final static File EXPRESSIONS_FILE = new File("src\\expressions.txt");

	/** The hard coded variable of differentiation used for all differentiations. */
	private static final BinaryTreeNode<String> VAR_DIFF = new BinaryTreeNode<String>("x");

	/** An ArrayList to hold each expression from the expressions text file. */
	private static ArrayList<String> myExpressionsList = new ArrayList<>();

	/** The average length of an output String after undergoing the Differentiator. */
	private static Double myDiffStringOutputAverage = 0.0;

	/** The average number of nodes in a binary tree after undergoing the Differentiator. */
	private static Double myDiffNodeOutputAverage = 0.0;

	/** The average length of an output String after undergoing the Simplifier. */
	private static Double mySimpStringOutputAverage = 0.0;

	/** The average number of nodes in a binary tree after undergoing the Simplifier. */
	private static Double mySimpNodeOutputAverage = 0.0;

	/** A private constructor to inhibit external instantiation. */
	private OutputLengthTest() {
		// do nothing
	}

	/**
	 * The start point for the empirical testing program.
	 *
	 * @param theArgs the command line arguments - ignored
	 * @throws FileNotFoundException
	 */
	public static void main(final String[] theArgs) throws FileNotFoundException {
		// populate the global expressions List
		try (Scanner file = new Scanner(EXPRESSIONS_FILE)) {
			while (file.hasNextLine()) {
				myExpressionsList.add(file.nextLine());
			}
		}
		// find number of nodes/length of string and update the averages
		ArrayList<String> expList = null;
		BinaryTree<String> expTree = null;
		BinaryTreeNode<String> diffExpRoot = null;
		BinaryTreeNode<String> simpDiffExpRoot = null;
		for (int i = 0; i < myExpressionsList.size(); i++) {
			expList = ExpressionParser.stringToList(myExpressionsList.get(i));
			expTree = ExpressionParser.shuntingYardTree(expList);

			diffExpRoot = Differentiator.derive(expTree.getNode(), VAR_DIFF);
			updateDiffAverages(diffExpRoot);

			simpDiffExpRoot = Simplifier.simplify(diffExpRoot);
			updateSimpAverages(simpDiffExpRoot);
		}
		displayResults();
	}

	/**
	 * Updates the global average values concerning the String lengths and the number of nodes
	 * represented in the specified binary tree node, after have gone through the
	 * Differentiator.
	 *
	 * @param theDiffExpRoot the node representing the differentiated expression
	 */
	private static void updateDiffAverages(final BinaryTreeNode<String> theDiffExpRoot) {
		final int numNodes = theDiffExpRoot.numChildren() + 1;
		myDiffNodeOutputAverage = (myDiffNodeOutputAverage + numNodes) / 2;

		final int stringLength = Differentiator.treeNodeToString(theDiffExpRoot, 0).length();
		myDiffStringOutputAverage = (myDiffStringOutputAverage + stringLength) / 2;
	}

	/**
	 * Updates the global average values concerning the String lengths and the number of nodes
	 * represented in the specified binary tree node, after have gone through the
	 * Differentiator and Simplifier.
	 *
	 * @param theSimpDiffExpRoot the node representing the simplified differentiated expression
	 */
	private static void updateSimpAverages(final BinaryTreeNode<String> theSimpDiffExpRoot) {
		final int numNodes = theSimpDiffExpRoot.numChildren() + 1;
		mySimpNodeOutputAverage = (mySimpNodeOutputAverage + numNodes) / 2;

		final int stringLength = Differentiator.treeNodeToString(theSimpDiffExpRoot, 0).length();
		mySimpStringOutputAverage = (mySimpStringOutputAverage + stringLength) / 2;
	}

	/**
	 * Displays the final results from the data gathered in the testing.
	 */
	private static void displayResults() {
		System.out.println("\nstatistics on the use of the Simplifier class\n\n" +
		    "-----------------------------------------------------\n");
		// length of String statistics
		System.out.println("diff output avg String length: " +
		    myDiffStringOutputAverage + " chars");
		System.out.println("simp output avg String length: " +
		    mySimpStringOutputAverage + " chars\n");
		final Double stringResult =
		    100 * (1.0 - (mySimpStringOutputAverage / myDiffStringOutputAverage));
		System.out.println("length reduced: " + stringResult + "%");
		System.out.println("\n-----------------------------------------------------\n");
		// number of nodes statistics
		System.out.println("diff output avg num of nodes: " +
		    myDiffNodeOutputAverage + " nodes");
		System.out.println("simp output avg num of nodes: " +
		    mySimpNodeOutputAverage + " nodes\n");
		final Double nodeResult =
		    100 * (1.0 - (mySimpNodeOutputAverage / myDiffNodeOutputAverage));
		System.out.println("nodes reduced: " + nodeResult + "%");
		System.out.println("\n-----------------------------------------------------\n");
		// larger percentage
		final DecimalFormat noDecimal = new DecimalFormat("####0");
		final Double max = Math.max(stringResult, nodeResult);
		System.out.println("larger percentage: " + noDecimal.format(max) + "%");
	}
}
