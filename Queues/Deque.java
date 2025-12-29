import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    // Node at the front
    private Node first;
    // Node at the back
    private Node last;
    // number of elements in the deque list
    private int dequeSize;

    private class Node {
        // item of the Node
        private Item item;
        // link to the next Node
        private Node next;
        // link to the previous Node
        private Node prev;
    }

    // construct an empty deque
    public Deque() {
        dequeSize = 0;
    }

    // throws an illegal argument exception if the argument item is null
    private void isArgumentNull(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Error: item is null");
        }
    }

    // throws a no such element expcetion if the list is empty
    private void isCollectionEmpty() {
        if (isEmpty()) {
            throw new NoSuchElementException("Error: Collection is empty");
        }
    }

    // is the deque empty?
    public boolean isEmpty() {
        return dequeSize == 0;
    }

    // return the number of items on the deque
    public int size() {
        return dequeSize;
    }

    // add the item to the front
    public void addFirst(Item item) {
        isArgumentNull(item);
        Node oldFirst = first;
        first = new Node();
        first.item = item;
        if (isEmpty()) {
            last = first;
        }
        else {
            oldFirst.prev = first;
            first.next = oldFirst;
        }
        dequeSize += 1;

    }

    // add the item to the back
    public void addLast(Item item) {
        isArgumentNull(item);
        Node oldLast = last;
        last = new Node();
        last.item = item;
        if (isEmpty()) {
            first = last;
        }
        else {
            last.prev = oldLast;
            oldLast.next = last;
        }
        dequeSize += 1;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        isCollectionEmpty();
        Item item = first.item;
        first = first.next;
        dequeSize -= 1;
        if (isEmpty()) {
            last = first;
        }
        else {
            first.prev = null;
        }
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        isCollectionEmpty();
        Item item = last.item;
        last = last.prev;
        dequeSize -= 1;
        if (isEmpty()) {
            first = last;
        }
        else {
            last.next = null;
        }
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new LinkedIterator();
    }

    // class that allows foreach loop to work
    private class LinkedIterator implements Iterator<Item> {
        // the iterator starts at the front of the list
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        // test 1
        Deque<Integer> testDeque = new Deque<>();
        StringBuilder storage = new StringBuilder();

        System.out.println("Empty?: " + testDeque.isEmpty());
        for (int i = 0; i < 10; i++) {
            testDeque.addFirst(i + 1);
        }

        System.out.println("Size for testDeque: " + testDeque.size());
        System.out.print("Test with addFirst() and removeLast(): ");
        while (!testDeque.isEmpty()) {
            String printingItem = testDeque.removeLast() + " ";
            System.out.print(printingItem);
            storage.append(printingItem);
        }
        System.out.println();
        if (!storage.equals("1 2 3 4 5 6 7 8 9 10 ")) {
            System.out.println("Error");
        }

        // test 2
        Deque<Integer> testDeque2 = new Deque<>();
        StringBuilder storage2 = new StringBuilder();

        System.out.println("Empty?: " + testDeque.isEmpty());
        for (int i = 0; i < 10; i++) {
            testDeque2.addLast(i + 1);
        }
        System.out.println("Size for testDeque2: " + testDeque2.size());
        System.out.print("Test with addLast() and removeFirst(): ");
        while (!testDeque2.isEmpty()) {
            String printingItem = testDeque2.removeFirst() + " ";
            System.out.print(printingItem);
            storage2.append(printingItem);
        }
        System.out.println();
        if (!storage2.equals("1 2 3 4 5 6 7 8 9 10 ")) {
            System.out.println("Error");
        }

        // test 3
        Deque<Integer> testDeque3 = new Deque<>();
        System.out.println("Empty?: " + testDeque3.isEmpty());
        for (int i = 0; i < 10; i++) {
            testDeque3.addFirst(i + 1);
        }
        System.out.println("Size for testDeque 3: " + testDeque3.size());
        System.out.print("Test for printing the "
                                 + "contents of the deque after addFirst(): ");
        for (int element : testDeque3) {
            System.out.print(element + " ");
        }
        System.out.println();

    }

}
