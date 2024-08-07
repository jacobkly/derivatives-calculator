/**
 * BinaryTree - Derivatives Calculator
 */

package structures;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * BinaryTree implements the BinaryTreeADT interface.
 *
 * @author Jacob Klymenko
 * @version 1.0
 *
 * @param <T> the generic type placeholder
 */
public class BinaryTree<T> implements BinaryTreeADT<T>, Iterable<T> {

	/** The root of this binary tree. */
	protected BinaryTreeNode<T> myRoot;

	/**
	 * Constructs a binary tree with the specified element as its root.
	 *
	 * @param theElement the element that becomes the root of this binary tree
	 */
	public BinaryTree(final T theElement) {
		myRoot = new BinaryTreeNode<T>(theElement);
	}

	/**
	 * Constructs a binary tree with the specified element as its root and the specified two
	 * subtrees as its left and right children.
	 *
	 * @param theElement 	the element that becomes the root of this binary tree
	 * @param theLeft		the left subtree of this binary tree
	 * @param theRight		the right subtree of this binary tree
	 */
	public BinaryTree(final T theElement, final BinaryTree<T> theLeft,
	    final BinaryTree<T> theRight) {
		myRoot = new BinaryTreeNode<T>(theElement);
		if (theLeft == null) {
			myRoot.setLeft(null);
		} else {
			myRoot.setLeft(theLeft.myRoot);
		}

		if (theRight == null) {
			myRoot.setRight(null);
		} else {
			myRoot.setRight(theRight.myRoot);
		}
	}

	@Override
	public int size() {
		int result = 0;
		if (myRoot == null) {
			result = 0;
		} else {
			result = 1 + myRoot.numChildren();
		}
		return result;
	}

	@Override
	public BinaryTreeNode<T> getNode() {
		return myRoot;
	}

	@Override
	public T getNodeElement() {
		return myRoot.getElement();
	}

	/**
	 * Returns an in-order traversal string representation of this binary tree.
	 *
	 * @return an in-order traversal string representation of this binary tree
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();

		Iterator<T> iterator = iterator();
		sb.append("\nInOrder: [");
		while (iterator.hasNext()) {
			sb.append(iterator.next());
			if (iterator.hasNext()) {
				sb.append(", ");
			}
		}
		sb.append("]");

		iterator = iteratorLevelOrder();
		sb.append("\nLevelOrder: [");
		while (iterator.hasNext()) {
			sb.append(iterator.next());
			if (iterator.hasNext()) {
				sb.append(", ");
			}
		}
		sb.append("]");

		return sb.toString();
	}

	@Override
	public Iterator<T> iterator() {
		return iteratorInOrder();
	}

	@Override
	public Iterator<T> iteratorInOrder() {
		final ArrayList<T> tempList = new ArrayList<T>();
		inOrder(myRoot, tempList);
		return new TreeIterator(tempList.iterator());
	}

	/**
	 * Performs a recursive in-order traversal.
	 *
	 * @param theNode		the node to be used as the root for this traversal
	 * @param theTempList	the temporary list for use in this traversal
	 */
	private void inOrder(final BinaryTreeNode<T> theNode, final ArrayList<T> theTempList) {
		if (theNode != null) {
			inOrder(theNode.getLeft(), theTempList);
			theTempList.add(theNode.getElement());
			inOrder(theNode.getRight(), theTempList);
		}
	}

	@Override
	public Iterator<T> iteratorLevelOrder() {
		final ArrayList<BinaryTreeNode<T>> nodes = new ArrayList<BinaryTreeNode<T>>();
		final ArrayList<T> tempList = new ArrayList<T>();
		BinaryTreeNode<T> curr;

		if (myRoot != null) {
			nodes.add(myRoot);
		}
		while (!nodes.isEmpty()) {
			curr = nodes.removeFirst();
			tempList.add(curr.getElement());
			if (curr.getLeft() != null) {
				nodes.add(curr.getLeft());
			}
			if (curr.getRight() != null) {
				nodes.add(curr.getRight());
			}
		}
		return new TreeIterator(tempList.iterator());
	}

	// INNER CLASS ITERATOR

	/**
	 * Inner class to represent an iterator over the elements of this tree.
	 */
	private class TreeIterator implements Iterator<T> {

		/** The backing Iterator. */
		private final Iterator<T> myIter;

		/**
		 * Sets up this iterator using the specified iterator.
		 *
		 * @param theIter the list iterator created by a tree traversal
		 */
		TreeIterator(final Iterator<T> theIter) {
			myIter = theIter;
		}

		/**
		 * Returns true if this iterator has at least one more element to deliver in the
		 * iteration.
		 *
		 * @return true if this iterator has one or more elements to deliver in the iteration
		 */
		@Override
		public boolean hasNext() {
			return myIter.hasNext();
		}

		/**
		 * Returns the next element in the iteration. If there are no more elements in
		 * this iteration, a NoSuchElementException is thrown.
		 *
		 * @return the next element in the iteration
		 * @throws NoSuchElementException if the iterator is empty
		 */
		@Override
		public T next() {
			if (hasNext()) {
				return myIter.next();
			} else {
				throw new NoSuchElementException();
			}
		}
	}

}
