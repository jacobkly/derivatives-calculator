/**
 * SimplifierTest - Derivatives Calculator
 */

package tests;

import java.util.ArrayList;
import model.Differentiator;
import model.ExpressionParser;
import model.Simplifier;
import org.junit.jupiter.api.Test;
import structures.BinaryTree;
import structures.BinaryTreeNode;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the Simplifier class.
 *
 * @author Jacob Klymenko
 * @version 1.0
 */
class SimplifierTest {

	/**
	 * Test method for {@link model.Simplifier#simplify(structures.BinaryTreeNode)}.
	 */
	@Test
	void testSimplify() {
		final String[] expressions = {"0 + 0", "0 + 1", "0 + x", "1 + 0", "x + 0", "x + x",
		    "x + 1", "x * x", "x * 2x", "0 * (1 / 2)", "0 * (1 / x)", "(1 + 0) + (0 + 1)",
		    "((1 + 0) * (5 + x)) + ((x + 5) * (0 + 1))",
		    "5 - 1", "5 - 10", "1 / 5"};
		/*
		 * Since Java does not allow creating a generic array of BinaryTreeNode<String>, it can
		 * be bypassed by first creating an array of BinaryTreeNode<String> with the desired
		 * size. Then initialize each index in the array with the desired element using a loop.
		 * However, an unavoidable type safety warning occurs. Nothing can be done to fix this
		 * expected warning.
		 */
		ArrayList<String> expsList;
		BinaryTree<String>[] expsTree = new BinaryTree[expressions.length];
		for (int i = 0; i < expressions.length; i++) {
			expsList = ExpressionParser.stringToList(expressions[i]);
			expsTree[i] = ExpressionParser.shuntingYardTree(expsList);
		}
		// the simplified form of each expression in order
		final String[] simplifiedExps = {"0.0", "1.0", "0 + x", "1.0", "x + 0", "2.0x",
		    "x + 1", "1.0x ^ 2", "2.0x ^ 2", "0.0", "0 * (1 / x)", "2.0",
		    "(1.0 * (5 + x)) + ((x + 5) * 1.0)",
		    "4.0", "-5.0", "0.2"};
		// unit testing
		BinaryTreeNode<String> currSimpExp;
		for (int i = 0; i < expsTree.length; i++) {
			currSimpExp = Simplifier.simplify(expsTree[i].getNode());
			assertEquals(simplifiedExps[i], Differentiator.treeNodeToString(currSimpExp, 0));
		}
	}

}
