/**
 * BinaryTreeNode - Derivatives Calculator
 */

package structures;

import java.util.LinkedList;
import java.util.Queue;

/**
 * BinaryTreeNode represents a node in an binary tree with a left and right child.
 *
 * @author Jacob Klymenko
 * @version 1.2
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
	public BinaryTreeNode(final T theElement) {
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
	public BinaryTreeNode(final T theElement, final BinaryTreeNode<T> theLeft,
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
	public T getElement() {
		return myElement;
	}

	/**
	 * Sets the left child of this node.
	 *
	 * @param theNode the left child of this node
	 */
	public void setLeft(final BinaryTreeNode<T> theNode) {
		myLeft = theNode;
	}

	/**
	 * Return the left child of this node.
	 *
	 * @return the left child of this node
	 */
	public BinaryTreeNode<T> getLeft() {
		return myLeft;
	}

	/**
	 * Sets the right child of this node.
	 *
	 * @param theNode the right child of this node.
	 */
	public void setRight(final BinaryTreeNode<T> theNode) {
		myRight = theNode;
	}

	/**
	 * Return the right child of this node.
	 *
	 * @return the right child of this node
	 */
	public BinaryTreeNode<T> getRight() {
		return myRight;
	}

	/**
	 * Returns the number of non-null children of this node.
	 *
	 * @return the number of children of this node
	 */
	public int numChildren() {
		int children = 0;

		// BFS approach
		Queue<BinaryTreeNode<T>> queue = new LinkedList<>();
		if (myLeft != null) {
			queue.add(myLeft);
		}
		if (myRight != null) {
			queue.add(myRight);
		}
		while (!queue.isEmpty()) {
			children++;
			BinaryTreeNode<T> node = queue.poll();
			if (node.getLeft() != null) {
				queue.add(node.getLeft());
			}
			if (node.getRight() != null) {
				queue.add(node.getRight());
			}
		}

		// recursive approach
		// if (myLeft != null) {
		// children = 1 + myLeft.numChildren();
		// }
		// if (myRight != null) {
		// children = 1 + children + myRight.numChildren();
		// }

		return children;
	}

	/**
	 * Returns true if the target element is in the root node or its children; otherwise false.
	 *
	 * @param theTargetElement 	the target element being searched for in this binary tree
	 * @param theRoot 			the root node defining the binary tree which may or may not
	 * 							contain the target element
	 * @return true if the target element is in the root node or its children; otherwise false.
	 */
	public boolean contains(final T theTargetElement, final BinaryTreeNode<T> theRoot) {
		boolean result = false;
		if (theRoot != null) {
			if (theRoot.getElement().equals(theTargetElement)) {
				result = true;
			} else {
				if (myLeft != null) {
					result = myLeft.contains(theTargetElement, myLeft);
				}
				if (result == false) {
					if (myRight != null) {
						result = myRight.contains(theTargetElement, myRight);
					}
				}
			}
		}
		return result;
	}

	/**
	 * Returns a new binary tree node after finding and replacing the node containing the
	 * target element in the root node or its children with the specified replacing node.
	 *
	 * @param theTargetElement	the target element being searched for in this binary tree node
	 * @param theRoot			the root node in which the target element is searched for
	 * @param theReplaceNode	the node that will replace the node containing the target element
	 * @return a binary tree node with the newly replaced node
	 */
	public BinaryTreeNode<T> findAndReplace(final T theTargetElement,
	    final BinaryTreeNode<T> theRoot, final BinaryTreeNode<T> theReplaceNode) {
		BinaryTreeNode<T> root = theRoot;
		if (theRoot != null) {
			if (theRoot.getElement() == theTargetElement) {
				return theReplaceNode;
			} else {
				root.myLeft = findAndReplace(theTargetElement, root.myLeft, theReplaceNode);
				root.myRight = findAndReplace(theTargetElement, root.myRight, theReplaceNode);
			}
		}
		return root;
	}

	// unneeded height method for now

	// /**
	// * Returns the number of levels of children this node is linked to.
	// *
	// * @return the number of levels of children this node is linked to
	// */
	// public int height() {
	// if (myLeft == null && myRight == null) {
	// return 0;
	// }
	// return Math.max(heightNode(myLeft), heightNode(myRight));
	// }
	//
	// /**
	// * Private recursive helper method returning the number of levels of children this node
	// * is linked to.
	// *
	// * @param theRoot the root to find the height of
	// * @return the number of levels of children this node is linked to
	// */
	// private int heightNode(final BinaryTreeNode<T> theRoot) {
	// if (theRoot == null) {
	// return 0;
	// }
	// return Math.max(heightNode(theRoot.getLeft()), heightNode(theRoot.getRight())) + 1;
	// }
}
