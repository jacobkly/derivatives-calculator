/**
 * ExpressionTreeNode - Derivates Calculator
 */

package structures;

/**
 * ExpressionTreeNode represents a node in an expression tree with a left and right child. Can
 * be easily implemented to apply generic types by replacing "String" with "T" and adding the
 * generic type "T" to the class name.
 *
 * @author Jacob Klymenko
 * @version 1.0
 */
public class ExpressionTreeNode {

	/** The value referred to by this node. */
	private final String myValue;

	/** A reference to the left child of this node. */
	private ExpressionTreeNode myLeft;

	/** A reference to the right child of this node. */
	private ExpressionTreeNode myRight;

	/**
	 * Creates a new tree node with the specified data.
	 *
	 * @param theValue the value that will become a part of the new tree node
	 */
	protected ExpressionTreeNode(final String theValue) {
		myValue = theValue;
		myLeft = null;
		myRight = null;
	}

	/**
	 * Creates a new tree node with the specified data.
	 *
	 * @param theValue 	the value that will become a part of the new tree node
	 * @param theLeft	the root node of the left subtree of this node
	 * @param theRight	the root node of the right subtree of this node
	 */
	protected ExpressionTreeNode(final String theValue, final ExpressionTreeNode theLeft,
	    final ExpressionTreeNode theRight) {
		myValue = theValue;
		myLeft = theLeft;
		myRight = theRight;
	}

	/**
	 * Return the value stored at this node.
	 *
	 * @return the value stored at this node
	 */
	protected String getValue() {
		return myValue;
	}

	/**
	 * Sets the left child of this node.
	 *
	 * @param theNode the left child of this node
	 */
	protected void setLeft(final ExpressionTreeNode theNode) {
		myLeft = theNode;
	}

	/**
	 * Return the left child of this node.
	 *
	 * @return the left child of this node
	 */
	protected ExpressionTreeNode getLeft() {
		return myLeft;
	}

	/**
	 * Sets the right child of this node.
	 *
	 * @param theNode the right child of this node.
	 */
	protected void setRight(final ExpressionTreeNode theNode) {
		myRight = theNode;
	}

	/**
	 * Return the right child of this node.
	 *
	 * @return the right child of this node
	 */
	protected ExpressionTreeNode getRight() {
		return myRight;
	}

}
