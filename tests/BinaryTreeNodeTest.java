/**
 * BinaryTreeNodeTest - Derivatives Calculator
 */

package tests;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import structures.BinaryTreeNode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the BinaryTreeNode class.
 *
 * @author Jacob Klymenko
 * @version 1.0
 */
class BinaryTreeNodeTest {

	/** A BinaryTreeNode to use in the tests. */
	private BinaryTreeNode<String> mySingleNode;

	/** A BinaryTreeNode to use in the tests. */
	private BinaryTreeNode<String> myNode;

	/**
	 * Initialize the test fixture before each test.
	 */
	@BeforeEach
	void setUp() {
		mySingleNode = new BinaryTreeNode<String>("1");
		myNode = new BinaryTreeNode<String>("2", mySingleNode, null);
	}

	/**
	 * Test method for {@link structures.BinaryTreeNode#BinaryTreeNode(java.lang.Object)}.
	 */
	@Test
	void testBinaryTreeNode() {
		assertNotNull(mySingleNode);
		assertNull(mySingleNode.getLeft());
		assertNull(mySingleNode.getRight());
		assertEquals(0, mySingleNode.numChildren());
		assertEquals("1", mySingleNode.getElement());
	}

	/**
	 * Test method for {@link structures.BinaryTreeNode#BinaryTreeNode(java.lang.Object, structures.BinaryTreeNode, structures.BinaryTreeNode)}.
	 */
	@Test
	void testBinaryTreeNodeOverload() {
		assertNotNull(myNode);
		assertNotNull(myNode.getLeft());
		assertNull(myNode.getRight());
		assertEquals(1, myNode.numChildren());
		assertEquals("2", myNode.getElement());
		assertEquals("1", myNode.getLeft().getElement());
	}

	/**
	 * Test method for {@link structures.BinaryTreeNode#getElement()}.
	 */
	@Test
	void testGetElement() {
		myNode = buildNode();
		final String[] expected = {"4", "2", "6", "1", "3", "5", "7"};
		ArrayList<BinaryTreeNode<String>> listOfNodes = new ArrayList<>();
		listOfNodes.add(myNode);
		// BFS added visited nodes to the list
		Queue<BinaryTreeNode<String>> queue = new LinkedList<>();
		if (myNode.getLeft() != null) {
			queue.add(myNode.getLeft());
			listOfNodes.add(myNode.getLeft());
		}
		if (myNode.getRight() != null) {
			queue.add(myNode.getRight());
			listOfNodes.add(myNode.getRight());
		}
		while (!queue.isEmpty()) {
			BinaryTreeNode<String> node = queue.poll();
			if (node.getLeft() != null) {
				queue.add(node.getLeft());
				listOfNodes.add(node.getLeft());
			}
			if (node.getRight() != null) {
				queue.add(node.getRight());
				listOfNodes.add(node.getRight());
			}
		}
		for (int i = 0; i < listOfNodes.size(); i++) {
			assertEquals(expected[i], listOfNodes.get(i).getElement());
		}
	}

	/**
	 * Test method for {@link structures.BinaryTreeNode#setLeft(structures.BinaryTreeNode)}.
	 */
	@Test
	void testSetLeft() {
		myNode.setLeft(new BinaryTreeNode<String>("test"));
		assertEquals("test", myNode.getLeft().getElement());
		myNode.getLeft().setLeft(new BinaryTreeNode<String>("test2"));
		assertEquals("test2", myNode.getLeft().getLeft().getElement());
		assertEquals(2, myNode.numChildren());

		myNode = buildNode();
		assertEquals(6, myNode.numChildren());
		myNode.setLeft(mySingleNode); // essentially removing two children nodes
		assertEquals(4, myNode.numChildren());
	}

	/**
	 * Test method for {@link structures.BinaryTreeNode#getLeft()}.
	 */
	@Test
	void testGetLeft() {
		final BinaryTreeNode<String> node3 = new BinaryTreeNode<String>("3");
		final BinaryTreeNode<String> node2 = new BinaryTreeNode<String>("2");
		BinaryTreeNode<String> rootNode = new BinaryTreeNode<String>("1", node2, null);
		rootNode.getLeft().setLeft(node3);
		assertEquals(node2, rootNode.getLeft());
		assertEquals(node3, rootNode.getLeft().getLeft());
		assertNull(rootNode.getLeft().getLeft().getLeft());
	}

	/**
	 * Test method for {@link structures.BinaryTreeNode#setRight(structures.BinaryTreeNode)}.
	 */
	@Test
	void testSetRight() {
		myNode.setRight(new BinaryTreeNode<String>("test"));
		assertEquals("test", myNode.getRight().getElement());
		assertEquals(2, myNode.numChildren());
		myNode.getRight().setRight(new BinaryTreeNode<String>("test2"));
		assertEquals("test2", myNode.getRight().getRight().getElement());
		assertEquals(3, myNode.numChildren());

		myNode = buildNode();
		assertEquals(6, myNode.numChildren());
		myNode.setRight(mySingleNode); // essentially removing two children nodes
		assertEquals(4, myNode.numChildren());
	}

	/**
	 * Test method for {@link structures.BinaryTreeNode#getRight()}.
	 */
	@Test
	void testGetRight() {
		final BinaryTreeNode<String> node3 = new BinaryTreeNode<String>("3");
		final BinaryTreeNode<String> node2 = new BinaryTreeNode<String>("2");
		BinaryTreeNode<String> rootNode = new BinaryTreeNode<String>("1", null, node2);
		rootNode.getRight().setRight(node3);
		assertEquals(node2, rootNode.getRight());
		assertEquals(node3, rootNode.getRight().getRight());
		assertNull(rootNode.getRight().getRight().getRight());
	}

	/**
	 * Test method for {@link structures.BinaryTreeNode#numChildren()}.
	 */
	@Test
	void testNumChildren() {
		assertEquals(0, mySingleNode.numChildren());
		assertEquals(1, myNode.numChildren());
		myNode = buildNode();
		assertEquals(6, myNode.numChildren());
	}

	/**
	 * Test method for {@link structures.BinaryTreeNode#contains(java.lang.Object, structures.BinaryTreeNode)}.
	 */
	@Test
	void testContains() {
		assertTrue(mySingleNode.contains("1", mySingleNode));
		assertTrue(myNode.contains("1", myNode));
		assertTrue(myNode.contains("2", myNode));

		myNode = buildNode();
		int numNodes = myNode.numChildren() + 1;
		for (int i = 1; i <= numNodes; i++) {
			assertTrue(myNode.contains(String.valueOf(i), myNode));
		}

		assertFalse(myNode.contains("0", myNode));
		assertFalse(myNode.contains("8", myNode));
	}

	/**
	 * Test method for {@link structures.BinaryTreeNode#findAndReplace(java.lang.Object, structures.BinaryTreeNode, structures.BinaryTreeNode)}.
	 */
	@Test
	void testFindAndReplace() {
		BinaryTreeNode<String> replacement = new BinaryTreeNode<String>("10");
		mySingleNode = mySingleNode.findAndReplace("1", mySingleNode, replacement);
		assertEquals("10", mySingleNode.getElement());

		replacement = new BinaryTreeNode<String>("20");
		myNode = myNode.findAndReplace("2", myNode, replacement);
		assertEquals("20", myNode.getElement());

		myNode = buildNode();
		myNode = myNode.findAndReplace("7", myNode, new BinaryTreeNode<String>("1000"));
		assertTrue(myNode.contains("1000", myNode));
		assertFalse(myNode.contains("7", myNode));

		myNode = buildNode();
		myNode = myNode.findAndReplace("4", myNode, new BinaryTreeNode<String>("new root"));
		assertEquals("new root", myNode.getElement());
		assertFalse(myNode.contains("3", myNode));
		assertFalse(myNode.contains("5", myNode));
	}

	/**
	 * Helper method returning a binary node containing seven nodes.
	 *
	 * @return a binary node containing seven nodes
	 */
	private BinaryTreeNode<String> buildNode() {
		final BinaryTreeNode<String> node1 = new BinaryTreeNode<String>("1");
		final BinaryTreeNode<String> node3 = new BinaryTreeNode<String>("3");
		final BinaryTreeNode<String> node5 = new BinaryTreeNode<String>("5");
		final BinaryTreeNode<String> node7 = new BinaryTreeNode<String>("7");
		final BinaryTreeNode<String> node2 = new BinaryTreeNode<String>("2", node1, node3);
		final BinaryTreeNode<String> node6 = new BinaryTreeNode<String>("6", node5, node7);
		return new BinaryTreeNode<String>("4", node2, node6);
	}
}
