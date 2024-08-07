/**
 * BinaryTreeADT - Derivatives Calculator
 */

package structures;

import java.util.Iterator;

/**
 * BinaryTreeADT defines the interface for the binary tree data structure.
 *
 * @author Jacob Klymenko
 * @version 1.0
 *
 * @param <T> the generic type placeholder
 */
public interface BinaryTreeADT<T> {

	/**
	 * Returns the number of nodes in this binary tree.
	 *
	 * @return the number of nodes in this binary tree
	 */
	int size();

	/**
	 * Returns the node of this binary tree's root.
	 *
	 * @return the node of this binary tree's root
	 */
	BinaryTreeNode<T> getNode();

	/**
	 * Returns the element within the root node of this binary tree.
	 *
	 * @return the element within the root node of this binary tree
	 */
	T getNodeElement();

	/**
	 * Returns an iterator over the elements in this binary tree.
	 *
	 * @return an iterator over the elements in this binary tree
	 */
	Iterator<T> iterator();

	/**
	 * Returns an iterator that represents an inorder traversal on this binary tree.
	 *
	 * @return an iterator that represents an inorder traversal on this binary tree.
	 */
	Iterator<T> iteratorInOrder();

	/**
	 * Returns an iterator that represents a levelorder traversal on this binary tree.
	 *
	 * @return an iterator that represents a levelorder traversal on this binary tree.
	 */
	Iterator<T> iteratorLevelOrder();

}
