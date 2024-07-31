/**
 * BinaryTreeADT - Derivates Calculator
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
	 * Returns the number of elements in this binary tree.
	 *
	 * @return the number of elements in this binary tree
	 */
	int size();

	/**
	 * Returns the height of this binary tree.
	 *
	 * @return the height of this binary tree
	 */
	int getHeight();

	/**
	 * Returns true if the target element is in the binary tree; otherwise false.
	 *
	 * @param theTargetElement the target element being searched for in this binary tree
	 * @return true if the target element is in the binary tree; otherwise false
	 */
	boolean contains(T theTargetElement);

	/**
	 * Returns a reference to the target element if it is found in this binary tree. Throws an
	 * ElementNotFoundException if the specified target element is not found in this binary tree.
	 *
	 * @param theTargetElement the target element being searched for in this binary tree
	 * @return a reference to the target element
	 * @throws ElementNotFoundException if the element is not in this binary tree
	 */
	T find(T theTargetElement);

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
