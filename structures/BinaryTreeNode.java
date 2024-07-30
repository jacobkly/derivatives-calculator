/**
 * BinaryTreeNode - Derivates Calculator
 */

package structures;

/**
 * BinaryTreeNode represents a node in an binary tree with a left and right child.
 *
 * @author Jacob Klymenko
 * @version 1.0
 *
 * @param <T> the generic type placeholder
 */
public class BinaryTreeNode<T> {

	/** The element referred to by this node. */
	private final T myElement;

	/** A reference to the left child of this node. */
	private BinaryTreeNode<T> myLeft;

	/** A reference to the right child of this node. */
	private BinaryTreeNode<T> myRight;

	/**
	 * Creates a new tree node with the specified data.
	 *
	 * @param theElement the element that will become a part of the new tree node
	 */
	protected BinaryTreeNode(final T theElement) {
		myElement = theElement;
		myLeft = null;
		myRight = null;
	}

	/**
	 * Creates a new tree node with the specified data.
	 *
	 * @param theElement 	the element that will become a part of the new tree node
	 * @param theLeft		the root node of the left subtree of this node
	 * @param theRight		the root node of the right subtree of this node
	 */
	protected BinaryTreeNode(final T theElement, final BinaryTreeNode<T> theLeft,
	    final BinaryTreeNode<T> theRight) {
		myElement = theElement;
		myLeft = theLeft;
		myRight = theRight;
	}

	/**
	 * Return the element stored at this node.
	 *
	 * @return the element stored at this node
	 */
	protected T getElement() {
		return myElement;
	}

	/**
	 * Sets the left child of this node.
	 *
	 * @param theNode the left child of this node
	 */
	protected void setLeft(final BinaryTreeNode<T> theNode) {
		myLeft = theNode;
	}

	/**
	 * Return the left child of this node.
	 *
	 * @return the left child of this node
	 */
	protected BinaryTreeNode<T> getLeft() {
		return myLeft;
	}

	/**
	 * Sets the right child of this node.
	 *
	 * @param theNode the right child of this node.
	 */
	protected void setRight(final BinaryTreeNode<T> theNode) {
		myRight = theNode;
	}

	/**
	 * Return the right child of this node.
	 *
	 * @return the right child of this node
	 */
	protected BinaryTreeNode<T> getRight() {
		return myRight;
	}

}
