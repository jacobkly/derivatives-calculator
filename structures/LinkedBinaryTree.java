/**
 * LinkedBinaryTree - Derivates Calculator
 */

package structures;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * LinkedBinaryTree implements the BinaryTreeADT interface.
 *
 * @author Jacob Klymenko
 * @version 1.0
 *
 * @param <T> the generic type placeholder
 */
public class LinkedBinaryTree<T> implements BinaryTreeADT<T>, Iterable<T> {

	/** The root of this binary tree. */
	protected BinaryTreeNode<T> myRoot;

	/** The number of elements in this binary tree. */
	protected int mySize;

	/**
	 * Constructs a binary tree with the specified element as its root.
	 *
	 * @param theElement the element that becomes the root of this binary tree
	 */
	public LinkedBinaryTree(final T theElement) {
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
	public LinkedBinaryTree(final T theElement, final LinkedBinaryTree<T> theLeft,
	    final LinkedBinaryTree<T> theRight) {
		myRoot = new BinaryTreeNode<T>(theElement);
		myRoot.setLeft(theLeft.myRoot); // gotta test if these two will give any null bugs
		myRoot.setRight(theRight.myRoot);
	}

	@Override
	public int size() {
		int result = 0;
		if (myRoot == null) {
			result = 0;
		} else {
			result = mySize;
		}
		return result;
	}

	@Override
	public int getHeight() {
		int height = 0;
		Queue<BinaryTreeNode<T>> queue = new LinkedList<>();
		queue.add(myRoot);

		while (queue.size() != 0) {
			int count = queue.size();
			height++;
			while (count > 0) {
				BinaryTreeNode<T> node = queue.poll();
				if (node.getLeft() != null) {
					queue.add(node.getLeft());
				}
				if (node.getRight() != null) {
					queue.add(node.getRight());
				}
				count--;
			}
		}
		return height;
	}

	@Override
	public boolean contains(T theTargetElement) {
		if (myRoot.getElement() == theTargetElement) {
			return true;
		}
		boolean result = false;
		Queue<BinaryTreeNode<T>> queue = new LinkedList<>();
		queue.add(myRoot);

		while (!queue.isEmpty()) {
			BinaryTreeNode<T> node = queue.poll();
			if (node.getLeft().getElement() == theTargetElement) {
				result = true;
			} else {
				queue.add(node.getLeft());
			}
			if (node.getRight().getElement() == theTargetElement) {
				result = true;
			} else {
				queue.add(node.getRight());
			}
		}
		return result;
	}

	@Override
	public T find(T theTargetElement) {
		if (myRoot.getElement() == theTargetElement) {
			return myRoot.getElement();
		}
		T result = null;
		Queue<BinaryTreeNode<T>> queue = new LinkedList<>();
		queue.add(myRoot);

		while (!queue.isEmpty()) {
			BinaryTreeNode<T> node = queue.poll();
			if (node.getLeft().getElement() == theTargetElement) {
				result = node.getLeft().getElement();
			} else {
				queue.add(node.getLeft());
			}
			if (node.getRight().getElement() == theTargetElement) {
				result = node.getRight().getElement();
			} else {
				queue.add(node.getRight());
			}
		}
		return result;
	}

	/**
	 * Returns a string representation of this binary tree showing the nodes in an in-order
	 * form.
	 *
	 * @return an in-order string representation of this binary tree
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();

		Iterator<T> iterator = iterator();
		sb.append("InOrder: [");
		while (iterator.hasNext()) {
			sb.append(iterator.next());
			if (iterator.hasNext()) {
				sb.append(", ");
			}
		}
		sb.append("]");

		// iterator = iteratorLevelOrder();
		// sb.append("\nLevelOrder: [");
		// while (iterator.hasNext()) {
		// sb.append(iterator.next());
		// if (iterator.hasNext()) {
		// sb.append(", ");
		// }
		// }
		// sb.append("]");

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
