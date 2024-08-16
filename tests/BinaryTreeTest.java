/**
 * BinaryTreeTest - Derivatives Calculator
 */

package tests;

import java.util.Iterator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import structures.BinaryTree;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the BinaryTree class.
 *
 * @author Jacob Klymenko
 * @version 1.0
 */
class BinaryTreeTest {

	/** A BinaryTree to use in the tests. */
	private BinaryTree<String> mySingleElementTree;

	/** A BinaryTree to use in the tests. */
	private BinaryTree<String> myTree;

	/**
	 * Initialize the test fixture before each test.
	 */
	@BeforeEach
	void setUp() {
		mySingleElementTree = new BinaryTree<String>("1");
		myTree = new BinaryTree<String>("2", mySingleElementTree, null);
	}

	/**
	 * Test method for {@link structures.BinaryTree#BinaryTree(java.lang.Object)}.
	 */
	@Test
	void testBinaryTree() {
		assertNotNull(mySingleElementTree);
		assertEquals(1, mySingleElementTree.size());
		assertEquals("1", mySingleElementTree.getNodeElement());
	}

	/**
	 * Test method for {@link structures.BinaryTree#BinaryTree(java.lang.Object, structures.BinaryTree, structures.BinaryTree)}.
	 */
	@Test
	void testBinaryTreeOverload() {
		assertNotNull(myTree);
		assertEquals(2, myTree.size());
		assertTrue(myTree.toString().contains("1, 2"));
	}

	/**
	 * Test method for {@link structures.BinaryTree#size()}.
	 */
	@Test
	void testSize() {
		assertEquals(1, mySingleElementTree.size());
		assertEquals(2, myTree.size());
		myTree = buildTree(); // custom tree
		assertEquals(7, myTree.size());
	}

	/**
	 * Test method for {@link structures.BinaryTree#getNode()}.
	 */
	@Test
	void testGetNode() {
		assertEquals("2", myTree.getNode().getElement());
		assertEquals("1", myTree.getNode().getLeft().getElement());
		assertEquals("1", mySingleElementTree.getNode().getElement());
	}

	/**
	 * Test method for {@link structures.BinaryTree#getNodeElement()}.
	 */
	@Test
	void testGetNodeElement() {
		assertEquals("2", myTree.getNodeElement());
		assertEquals("1", mySingleElementTree.getNodeElement());
	}

	/**
	 * Test method for {@link structures.BinaryTree#toString()}.
	 */
	@Test
	void testToString() {
		assertEquals("\nInOrder: [1]\nLevelOrder: [1]", mySingleElementTree.toString());
		assertEquals("\nInOrder: [1, 2]\nLevelOrder: [2, 1]", myTree.toString());
		myTree = buildTree();
		assertEquals("\nInOrder: [1, 2, 3, 4, 5, 6, 7]" +
		    "\nLevelOrder: [4, 2, 6, 1, 3, 5, 7]", myTree.toString());
	}

	/**
	 * Test method for {@link structures.BinaryTree#iterator()}.
	 */
	@Test
	void testIterator() {
		final BinaryTree<String> myTree = buildTree();
		final Iterator<String> iterator = myTree.iterator();
		final String[] expected = {"1", "2", "3", "4", "5", "6", "7"};
		int i = 0;
		while (iterator.hasNext()) {
			assertEquals(expected[i], iterator.next());
			i++;
		}
	}

	/**
	 * Test method for {@link structures.BinaryTree#iteratorInOrder()}.
	 */
	@Test
	void testIteratorInOrder() {
		myTree = buildTree();
		final Iterator<String> iterator = myTree.iteratorInOrder();
		final String[] expected = {"1", "2", "3", "4", "5", "6", "7"};
		int i = 0;
		while (iterator.hasNext()) {
			assertEquals(expected[i], iterator.next());
			i++;
		}
	}

	/**
	 * Test method for {@link structures.BinaryTree#iteratorLevelOrder()}.
	 */
	@Test
	void testIteratorLevelOrder() {
		final BinaryTree<String> myTree = buildTree();
		final Iterator<String> iterator = myTree.iteratorLevelOrder();
		final String[] expected = {"4", "2", "6", "1", "3", "5", "7"};
		int i = 0;
		while (iterator.hasNext()) {
			assertEquals(expected[i], iterator.next());
			i++;
		}
	}

	/**
	 * Helper method returning a binary tree containing seven nodes.
	 *
	 * @return a binary tree containing seven nodes
	 */
	private BinaryTree<String> buildTree() {
		final BinaryTree<String> tree1 = new BinaryTree<String>("1");
		final BinaryTree<String> tree3 = new BinaryTree<String>("3");
		final BinaryTree<String> tree5 = new BinaryTree<String>("5");
		final BinaryTree<String> tree7 = new BinaryTree<String>("7");
		final BinaryTree<String> tree2 = new BinaryTree<String>("2", tree1, tree3);
		final BinaryTree<String> tree6 = new BinaryTree<String>("6", tree5, tree7);
		return new BinaryTree<String>("4", tree2, tree6);
	}
}
