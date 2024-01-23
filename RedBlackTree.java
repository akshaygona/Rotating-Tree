import java.util.LinkedList;
import java.util.Stack;

/**
 * Red-Black Tree implementation with a Node inner class for representing
 * the nodes of the tree. Currently, this implements a Binary Search Tree that
 * we will turn into a red black tree by modifying the insert functionality.
 * In this activity, we will start with implementing rotations for the binary
 * search tree insert algorithm.
 */
public class RedBlackTree<T extends Comparable<T>> implements SortedCollectionInterface<T> {

    protected Node<T> root; // reference to root node of tree, null when empty
    protected int size = 0; // the number of values in the tree

    public static boolean test1() {
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        tree.insert(40);
        tree.insert(20);
        tree.insert(60);
        tree.insert(50);
        tree.insert(10);
        tree.insert(30);
        tree.insert(80);

        try {
            tree.rotate(tree.findNodeWithData(60), tree.findNodeWithData(40));
        } catch (Exception e) {
            System.out.println("how tf you get a random exception");
            return false;
        }

        if (tree.size() != 7) {
            System.out.println("how did yo size change bruh what");
            return false;
        }

        String string = "level order: [ 60, 40, 80, 80, 20, 10, 30 ]\n" +
            "in order: [ 80, 40, 10, 20, 30, 60, 80 ]";
        if (!tree.toString().equals(string)) {
            System.out.println("toString be busted");
            return false;
        }

        if (tree.root != tree.findNodeWithData(60)) {
            System.out.println("how you mess up root");
            return false;
        }
        return true;
    }

    /**
     * This tester method verifies the functionality of the rotate method when an unbalanced
     * BST with 3 levels is being rotated at the root of the tree. This tester method also tests
     * out illegal rotations and makes sure those are disallowed.
     * @return false if any bugs were detected, true otherwise
     */
    public static boolean test2() {
        // create a bst
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        tree .insert(60);
        tree .insert(90);
        tree .insert(105);
        tree .insert(75);

        {
            try {
                tree.rotate(tree.findNodeWithData(75), tree.findNodeWithData(60));
                System.out.println("exception was supposed to be thrown with two unrelated nodes");
                return false;
            } catch (Exception e) {
                if (!(e instanceof IllegalArgumentException)) {
                    System.out.println("how did you get this random exception");
                    return false;
                }
            }
            try {
                tree.rotate(tree.findNodeWithData(75), tree.findNodeWithData(105));
                System.out.println("exception was supposed to be thrown with two unrelated nodes");
                return false;
            } catch (Exception e) {
                if (!(e instanceof IllegalArgumentException)) {
                    System.out.println("how did you get his random exception 1");
                    return false;
                }
            }
        }

        try {
            tree.rotate(tree.findNodeWithData(90), tree.findNodeWithData(60));
        } catch (Exception e) {
            System.out.println("An incorrect exception was thrown");
            return false;
        }

        if (tree.size() != 4) {
            System.out.println("The size was incorrectly altered");
            return false;
        }

        if (tree.root != tree.findNodeWithData(90)) {
            System.out.println("how the hell u get the wrong root");
            return false;
        }

        String string = "level order: [ 90, 60, 105, 105 ]\n"
            + "in order: [ 105, 60, 90, 105 ]";
        if (!tree.toString().equals(string)) {
            System.out.println("toString() busted");
            return false;
        }
        return true;
    }

    /**
     * This tester method verifies the functionality of the rotate method when only a subtree of
     * a BST is being rotated
     * @return false if any bugs were detected, true otherwise
     */
    public static boolean test3() {
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        tree.insert(100);
        tree.insert(150);
        tree.insert(50);
        tree.insert(25);
        tree.insert(75);
        tree.insert(15);
        tree.insert(35);

        try {
            tree.rotate(tree.findNodeWithData(25), tree.findNodeWithData(50));
        } catch (Exception e) {
            System.out.println("how tf you get a random exception");
            return false;
        }

        if (tree.size() != 7) {
            System.out.println("how did the size change bruh");
            return false;
        }


        if (tree.root != tree.findNodeWithData(100)) {
            System.out.println("how tf did your root change");
            return false;
        }


        String string = "level order: [ 100, 50, 25, 35, 75, 15, 50, 35, 75 ]\n"
            + "in order: [ 35, 50, 75, 100, 15, 25, 35, 50, 75 ]";
        if (!tree.toString().equals(string)) {
            System.out.println("toString busted");
            return false;
        }
        return true;
    }

    /**
     * Main method to run tests. Comment out the lines for each test
     * to run them.
     *
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("Test 1 passed: " + test1());
        System.out.println("Test 2 passed: " + test2());
        System.out.println("Test 3 passed: " + test3());
    }

    /**
     * Performs a naive insertion into a binary search tree: adding the input
     * data value to a new node in a leaf position within the tree. After
     * this insertion, no attempt is made to restructure or balance the tree.
     * This tree will not hold null references, nor duplicate data values.
     *
     * @param data to be added into this binary search tree
     * @return true if the value was inserted, false if not
     * @throws NullPointerException     when the provided data argument is null
     * @throws IllegalArgumentException when data is already contained in the tree
     */
    public boolean insert(T data) throws NullPointerException, IllegalArgumentException {
        // null references cannot be stored within this tree
        if (data == null)
            throw new NullPointerException("This RedBlackTree cannot store null references.");

        Node<T> newNode = new Node<>(data);
        if (this.root == null) {
            // add first node to an empty tree
            root = newNode;
            size++;
            return true;
        } else {
            // insert into subtree
            Node<T> current = this.root;
            while (true) {
                int compare = newNode.data.compareTo(current.data);
                if (compare == 0) {
                    throw new IllegalArgumentException(
                        "This RedBlackTree already contains value " + data);
                } else if (compare < 0) {
                    // insert in left subtree
                    if (current.context[1] == null) {
                        // empty space to insert into
                        current.context[1] = newNode;
                        newNode.context[0] = current;
                        this.size++;
                        return true;
                    } else {
                        // no empty space, keep moving down the tree
                        current = current.context[1];
                    }
                } else {
                    // insert in right subtree
                    if (current.context[2] == null) {
                        // empty space to insert into
                        current.context[2] = newNode;
                        newNode.context[0] = current;
                        this.size++;
                        return true;
                    } else {
                        // no empty space, keep moving down the tree
                        current = current.context[2];
                    }
                }
            }
        }
    }

    /**
     * Performs the rotation operation on the provided nodes within this tree.
     * When the provided child is a left child of the provided parent, this
     * method will perform a right rotation. When the provided child is a
     * right child of the provided parent, this method will perform a left rotation.
     * When the provided nodes are not related in one of these ways, this method
     * will throw an IllegalArgumentException.
     *
     * @param child  is the node being rotated from child to parent position
     *               (between these two node arguments)
     * @param parent is the node being rotated from parent to child position
     *               (between these two node arguments)
     * @throws IllegalArgumentException when the provided child and parent
     *                                  node references are not initially (pre-rotation) related that way
     */
    private void rotate(Node<T> child, Node<T> parent) throws IllegalArgumentException {
        if (parent.context[1] != child && parent.context[2] != child) {
            throw new IllegalArgumentException("child and parent are not initially related");
        }

        //left rotation
        if (parent.context[2] == child) {
            Node<T> temp = parent.context[1];
            parent.context[1] = child.context[2];
            child.context[1] = parent;
            parent.context[2] = temp;

            if (parent.context[0] != null){
                parent.context[0].context[1] = child;
            }
            child.context[0] = parent.context[0];
            parent.context[0] = child;
            if(temp != null){
                temp.context[0] = parent;
            }
        }

        //right rotation
        else if(parent.context[1] == child){
            Node<T> temp = child.context[2];
            child.context[2] = parent;
            parent.context[1] = temp;

            if (parent.context[0] != null){
                parent.context[0].context[2] = child;
            }
            child.context[0] = parent.context[0];
            parent.context[0] = child;
            if(temp != null){
                temp.context[0] = parent;
            }
        }
        if (parent.equals(root)){
            root = child;
        }
    }

    /**
     * Get the size of the tree (its number of nodes).
     *
     * @return the number of nodes in the tree
     */
    public int size() {
        return size;
    }

    /**
     * Method to check if the tree is empty (does not contain any node).
     *
     * @return true of this.size() return 0, false if this.size() > 0
     */
    public boolean isEmpty() {
        return this.size() == 0;
    }

    /**
     * Removes the value data from the tree if the tree contains the value.
     * This method will not attempt to rebalance the tree after the removal and
     * should be updated once the tree uses Red-Black Tree insertion.
     *
     * @return true if the value was remove, false if it didn't exist
     * @throws NullPointerException     when the provided data argument is null
     * @throws IllegalArgumentException when data is not stored in the tree
     */
    public boolean remove(T data) throws NullPointerException, IllegalArgumentException {
        // null references will not be stored within this tree
        if (data == null) {
            throw new NullPointerException("This RedBlackTree cannot store null references.");
        } else {
            Node<T> nodeWithData = this.findNodeWithData(data);
            // throw exception if node with data does not exist
            if (nodeWithData == null) {
                throw new IllegalArgumentException(
                    "The following value is not in the tree and cannot be deleted: " + data);
            }
            boolean hasRightChild = (nodeWithData.context[2] != null);
            boolean hasLeftChild = (nodeWithData.context[1] != null);
            if (hasRightChild && hasLeftChild) {
                // has 2 children
                Node<T> successorNode = this.findMinOfRightSubtree(nodeWithData);
                // replace value of node with value of successor node
                nodeWithData.data = successorNode.data;
                // remove successor node
                if (successorNode.context[2] == null) {
                    // successor has no children, replace with null
                    this.replaceNode(successorNode, null);
                } else {
                    // successor has a right child, replace successor with its child
                    this.replaceNode(successorNode, successorNode.context[2]);
                }
            } else if (hasRightChild) {
                // only right child, replace with right child
                this.replaceNode(nodeWithData, nodeWithData.context[2]);
            } else if (hasLeftChild) {
                // only left child, replace with left child
                this.replaceNode(nodeWithData, nodeWithData.context[1]);
            } else {
                // no children, replace node with a null node
                this.replaceNode(nodeWithData, null);
            }
            this.size--;
            return true;
        }
    }

    /**
     * Checks whether the tree contains the value *data*.
     *
     * @param data the data value to test for
     * @return true if *data* is in the tree, false if it is not in the tree
     */
    public boolean contains(T data) {
        // null references will not be stored within this tree
        if (data == null) {
            throw new NullPointerException("This RedBlackTree cannot store null references.");
        } else {
            Node<T> nodeWithData = this.findNodeWithData(data);
            // return false if the node is null, true otherwise
            return (nodeWithData != null);
        }
    }

    /**
     * Helper method that will replace a node with a replacement node. The replacement
     * node may be null to remove the node from the tree.
     *
     * @param nodeToReplace   the node to replace
     * @param replacementNode the replacement for the node (may be null)
     */
    protected void replaceNode(Node<T> nodeToReplace, Node<T> replacementNode) {
        if (nodeToReplace == null) {
            throw new NullPointerException("Cannot replace null node.");
        }
        if (nodeToReplace.context[0] == null) {
            // we are replacing the root
            if (replacementNode != null)
                replacementNode.context[0] = null;
            this.root = replacementNode;
        } else {
            // set the parent of the replacement node
            if (replacementNode != null)
                replacementNode.context[0] = nodeToReplace.context[0];
            // do we have to attach a new left or right child to our parent?
            if (nodeToReplace.isRightChild()) {
                nodeToReplace.context[0].context[2] = replacementNode;
            } else {
                nodeToReplace.context[0].context[1] = replacementNode;
            }
        }
    }

    /**
     * Helper method that will return the inorder successor of a node with two children.
     *
     * @param node the node to find the successor for
     * @return the node that is the inorder successor of node
     */
    protected Node<T> findMinOfRightSubtree(Node<T> node) {
        if (node.context[1] == null && node.context[2] == null) {
            throw new IllegalArgumentException("Node must have two children");
        }
        // take a steop to the right
        Node<T> current = node.context[2];
        while (true) {
            // then go left as often as possible to find the successor
            if (current.context[1] == null) {
                // we found the successor
                return current;
            } else {
                current = current.context[1];
            }
        }
    }

    /**
     * Helper method that will return the node in the tree that contains a specific
     * value. Returns null if there is no node that contains the value.
     *
     * @return the node that contains the data, or null of no such node exists
     */
    protected Node<T> findNodeWithData(T data) {
        Node<T> current = this.root;
        while (current != null) {
            int compare = data.compareTo(current.data);
            if (compare == 0) {
                // we found our value
                return current;
            } else if (compare < 0) {
                // keep looking in the left subtree
                current = current.context[1];
            } else {
                // keep looking in the right subtree
                current = current.context[2];
            }
        }
        // we're at a null node and did not find data, so it's not in the tree
        return null;
    }


    // Implement at least 3 boolean test methods by using the method signatures below,
    // removing the comments around them and addind your testing code to them. You can
    // use your notes from lecture for ideas on concrete examples of rotation to test for.
    // Make sure to include rotations within and at the root of a tree in your test cases.
    // Give each of the methods a meaningful header comment that describes what is being
    // tested and make sure your test hafe inline comments to help developers read through them.
    // If you are adding additional tests, then name the method similar to the ones given below.
    // Eg: public static boolean test4() {}
    // Do not change the method name or return type of the existing tests.
    // You can run your tests by commenting in the calls to the test methods 

    /**
     * This method performs an inorder traversal of the tree. The string
     * representations of each data value within this tree are assembled into a
     * comma separated string within brackets (similar to many implementations
     * of java.util.Collection, like java.util.ArrayList, LinkedList, etc).
     *
     * @return string containing the ordered values of this tree (in-order traversal)
     */
    public String toInOrderString() {
        // generate a string of all values of the tree in (ordered) in-order
        // traversal sequence
        StringBuffer sb = new StringBuffer();
        sb.append("[ ");
        if (this.root != null) {
            Stack<Node<T>> nodeStack = new Stack<>();
            Node<T> current = this.root;
            while (!nodeStack.isEmpty() || current != null) {
                if (current == null) {
                    Node<T> popped = nodeStack.pop();
                    sb.append(popped.data.toString());
                    if (!nodeStack.isEmpty() || popped.context[2] != null)
                        sb.append(", ");
                    current = popped.context[2];
                } else {
                    nodeStack.add(current);
                    current = current.context[1];
                }
            }
        }
        sb.append(" ]");
        return sb.toString();
    }

    /**
     * This method performs a level order traversal of the tree. The string
     * representations of each data value
     * within this tree are assembled into a comma separated string within
     * brackets (similar to many implementations of java.util.Collection).
     * This method will be helpful as a helper for the debugging and testing
     * of your rotation implementation.
     *
     * @return string containing the values of this tree in level order
     */
    public String toLevelOrderString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[ ");
        if (this.root != null) {
            LinkedList<Node<T>> q = new LinkedList<>();
            q.add(this.root);
            while (!q.isEmpty()) {
                Node<T> next = q.removeFirst();
                if (next.context[1] != null)
                    q.add(next.context[1]);
                if (next.context[2] != null)
                    q.add(next.context[2]);
                sb.append(next.data.toString());
                if (!q.isEmpty())
                    sb.append(", ");
            }
        }
        sb.append(" ]");
        return sb.toString();
    }

    public String toString() {
        return "level order: " + this.toLevelOrderString() + "\nin order: "
            + this.toInOrderString();
    }


    /**
     * This class represents a node holding a single value within a binary tree.
     */
    protected static class Node<T> {
        public T data;
        // The context array stores the context of the node in the tree:
        // - context[0] is the parent reference of the node,
        // - context[1] is the left child reference of the node,
        // - context[2] is the right child reference of the node.
        // The @SupressWarning("unchecked") annotation is used to supress an unchecked
        // cast warning. Java only allows us to instantiate arrays without generic
        // type parameters, so we use this cast here to avoid future casts of the
        // node type's data field.
        @SuppressWarnings("unchecked") public Node<T>[] context = (Node<T>[]) new Node[3];

        public Node(T data) {
            this.data = data;
        }

        /**
         * @return true when this node has a parent and is the right child of
         * that parent, otherwise return false
         */
        public boolean isRightChild() {
            return context[0] != null && context[0].context[2] == this;
        }

    }

}
