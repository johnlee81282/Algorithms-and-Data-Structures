import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.StdOut;

public class PointST<Value> {

    // red black tree that stores the points
    private RedBlackBST<Point2D, Value> rbtree;

    // construct an empty symbol table of points
    public PointST() {
        rbtree = new RedBlackBST<Point2D, Value>();
    }

    // is the symbol table empty?
    public boolean isEmpty() {
        return rbtree.size() == 0;
    }

    // number of points
    public int size() {
        return rbtree.size();
    }

    // associate the value val with point p
    public void put(Point2D p, Value val) {
        if (p == null || val == null) {
            throw new IllegalArgumentException("An argument cannot be null");
        }
        rbtree.put(p, val);
    }

    // value associated with point p
    public Value get(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("Invalid coordinates");
        return rbtree.get(p);
    }

    // does the symbol table contain point p?
    public boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("Invalid coordinates");
        return rbtree.get(p) != null;
    }

    // all points in the symbol table
    public Iterable<Point2D> points() {
        return rbtree.keys();
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException("The rectangle cannot be empty");
        // store all the points inside the rectangle as you iterate
        Queue<Point2D> point2DQueue = new Queue<Point2D>();
        for (Point2D key : rbtree.keys()) {
            if (rect.contains(key)) point2DQueue.enqueue(key);
        }
        return point2DQueue;
    }

    // a nearest neighbor of point p; null if the symbol table is empty
    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException(
                    "call nearest() with null argument");

        double minDistance = Double.POSITIVE_INFINITY;
        Point2D champ = null;
        for (Point2D point : rbtree.keys()) {
            double distance = point.distanceSquaredTo(p);
            if (distance < minDistance) {
                minDistance = distance;
                champ = point;
            }
        }
        return champ;
    }

    // unit testing
    public static void main(String[] args) {
        PointST<String> tree = new PointST<String>();
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
    }

}
