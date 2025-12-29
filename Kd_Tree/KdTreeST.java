import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdOut;

public class KdTreeST<Value> {

    // the size of the symbol table
    private int size;
    // the root of the symbol table
    private Node root;

    // construct an empty symbol table of points
    public KdTreeST() {
        root = null;
        size = 0;
    }

    private class Node {
        private Point2D point; // the point
        private Value value; // the value
        // the axis-aligned rectangle corresponding to this node
        private RectHV rect;
        private Node left; // the left/bottom subtree
        private Node right; // the right/top subtree

        // constructor, the subtrees are initialized to null
        private Node(Point2D point, Value value, RectHV rect) {
            this.point = point;
            this.value = value;
            this.left = null;
            this.right = null;
            this.rect = rect;
        }

    }

    // helper function that compares two points appropriately
    private int compare(Point2D key1, Point2D key2, int level) {
        // compare based on x coordinate
        if (level % 2 == 0) {
            double x1 = key1.x();
            double x2 = key2.x();
            if (x1 - x2 < 0) return -1;
            else if (x1 - x2 > 0) return 1;
            else return 0;
        }
        // compare based on y coordinate
        else {
            double y1 = key1.y();
            double y2 = key2.y();
            if (y1 - y2 < 0) return -1;
            else if (y1 - y2 > 0) return 1;
            else return 0;
        }
    }

    // is the symbol table empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // number of points
    public int size() {
        return size;
    }

    // helper function for put() that recursively put the node
    private Node insert(Node x, Point2D key, Value val, RectHV rect, int level) {
        if (x == null) {
            size++;
            return new Node(key, val, rect);
        }

        int cmp = compare(key, x.point, level);
        double currentX = x.point.x();
        double currentY = x.point.y();
        double xMin = rect.xmin();
        double xMax = rect.xmax();
        double yMin = rect.ymin();
        double yMax = rect.ymax();

        // a new rectangle that corresponds to the new node
        RectHV newRect;
        if (cmp < 0) { // left or bottom
            if (x.left != null) newRect = x.left.rect;
                // only when we are adding a new node
            else {
                if (level % 2 == 0)
                    newRect = new RectHV(xMin, yMin, currentX, yMax);
                else
                    newRect = new RectHV(xMin, yMin, xMax, currentY);
            }
            x.left = insert(x.left, key, val, newRect, level + 1);
        }
        else if (cmp > 0) { // right or top
            if (x.right != null) newRect = x.right.rect;
            else {
                if (level % 2 == 0)
                    newRect = new RectHV(currentX, yMin, xMax, yMax);
                else
                    newRect = new RectHV(xMin, currentY, xMax, yMax);
            }
            x.right = insert(x.right, key, val, newRect, level + 1);
        }
        else { // right or top
            // when the point already exists
            if (x.point.equals(key)) {
                x.value = val;
            }
            else {
                if (x.right != null) newRect = x.right.rect;
                else {
                    if (level % 2 == 0)
                        newRect = new RectHV(currentX, yMin, xMax, yMax);
                    else
                        newRect = new RectHV(xMin, currentY, xMax, yMax);
                }
                x.right = insert(x.right, key, val, newRect, level + 1);
            }
        }

        return x;
    }

    // associate the value val with point p
    public void put(Point2D p, Value val) {
        if (p == null || val == null)
            throw new IllegalArgumentException("call put() with null argument");

        double min = Double.NEGATIVE_INFINITY;
        double max = Double.POSITIVE_INFINITY;

        RectHV initialRect = new RectHV(min, min, max, max);

        root = insert(root, p, val, initialRect, 0);
    }

    // helper function for get() that recursively search for the point
    private Value search(Node x, Point2D key, int level) {
        if (x == null) return null;

        int cmp = compare(key, x.point, level);

        if (cmp < 0) {
            return search(x.left, key, level + 1);
        }
        else if (cmp > 0) {
            return search(x.right, key, level + 1);
        }
        else {
            // point found
            if (x.point.equals(key)) {
                return x.value;
            }
            else {
                return search(x.right, key, level + 1);
            }
        }
    }

    // value associated with point p
    public Value get(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("call get() with null argument");

        return search(root, p, 0);
    }

    // does the symbol table contain point p?
    public boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException(
                    "call contains() with null argument");

        return get(p) != null;
    }

    // all points in the symbol table
    public Iterable<Point2D> points() {
        Queue<Point2D> queue = new Queue<Point2D>();
        // points that will be searched in the future
        Queue<Node> nextLevel = new Queue<Node>();
        if (root != null)
            nextLevel.enqueue(root);

        while (!nextLevel.isEmpty()) {
            Node current = nextLevel.dequeue();
            queue.enqueue(current.point);
            if (current.left != null)
                nextLevel.enqueue(current.left);
            if (current.right != null)
                nextLevel.enqueue(current.right);
        }

        return queue;
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException(
                    "call range() with null argument");

        Queue<Point2D> queue = new Queue<Point2D>();
        // nodes that will be examined in the future
        Queue<Node> nodes = new Queue<Node>();

        if (root != null)
            nodes.enqueue(root);
        while (!nodes.isEmpty()) {
            Node current = nodes.dequeue();
            // avoid the null pointer exception by checking if it's null
            if (current.left != null && current.left.rect.intersects(rect))
                nodes.enqueue(current.left);
            if (current.right != null && current.right.rect.intersects(rect))
                nodes.enqueue(current.right);

            if (rect.contains(current.point))
                queue.enqueue(current.point);
        }

        return queue;
    }

    // a nearest neighbor of point p; null if the symbol table is empty
    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException(
                    "call nearest() with null argument");

        if (root == null) return null;

        return nearer(root, p, root.point);
    }

    // helper function that finds the closest point to the point in the subtree
    private Point2D nearer(Node node, Point2D p, Point2D closest) {
        if (node == null) return closest;

        double distance = p.distanceSquaredTo(closest);
        Point2D champ = closest;
        if (p.distanceSquaredTo(node.point) < distance) {
            champ = node.point;
        }

        Node leftSubtree = node.left;
        Node rightSubtree = node.right;

        // is left subtree closer than right subtree
        // used when neither tree contains the point
        boolean leftCloser = (leftSubtree != null && rightSubtree != null &&
                leftSubtree.rect.distanceSquaredTo(p) <
                        rightSubtree.rect.distanceSquaredTo(p));

        // search the subtree that is closer to p first
        // avoid the null pointer exception by checking if it's null
        if (leftCloser) {

            if (leftSubtree.rect.distanceSquaredTo(p) <
                    champ.distanceSquaredTo(p))
                champ = nearer(leftSubtree, p, champ);

            if (rightSubtree.rect.distanceSquaredTo(p) <
                    champ.distanceSquaredTo(p)) {

                champ = nearer(rightSubtree, p, champ);
            }
        }
        else {

            if (rightSubtree != null &&
                    rightSubtree.rect.distanceSquaredTo(p) <
                            champ.distanceSquaredTo(p))
                champ = nearer(rightSubtree, p, champ);

            if (leftSubtree != null &&
                    leftSubtree.rect.distanceSquaredTo(p) <
                            champ.distanceSquaredTo(p)) {

                champ = nearer(leftSubtree, p, champ);
            }
        }

        return champ;
    }

    // unit testing
    public static void main(String[] args) {
        KdTreeST<String> tree = new KdTreeST<String>();
        Point2D p1 = new Point2D(1, 1);
        Point2D p2 = new Point2D(2, 2);
        Point2D p3 = new Point2D(4, -1);
        Point2D p4 = new Point2D(3, 3);
        RectHV rect = new RectHV(1, 2, 3, 4);


        tree.put(p1, "Hello");
        tree.put(p2, "next");
        tree.put(p3, "soccer");
        StdOut.println(tree.get(p3));
        for (Point2D x : tree.points()) {
            StdOut.println(x);
        }
        StdOut.println(tree.size());
        StdOut.println(tree.isEmpty());
        StdOut.println(tree.contains(p4));
        StdOut.println(tree.nearest(p4));
        StdOut.println(tree.range(rect));

        StdOut.println();
        KdTreeST<String> tree2 = new KdTreeST<String>();

        Point2D test1 = new Point2D(0.7, 0.2);
        Point2D test2 = new Point2D(0.5, 0.4);
        Point2D test3 = new Point2D(0.2, 0.3);
        Point2D test4 = new Point2D(0.4, 0.7);
        Point2D test5 = new Point2D(0.9, 0.6);
        Point2D p = new Point2D(0.81, 0.17);
        tree2.put(test1, "");
        tree2.put(test2, "");
        tree2.put(test3, "");
        tree2.put(test4, "");
        tree2.put(test5, "");
        StdOut.println(tree2.nearest(p));

    }

}
