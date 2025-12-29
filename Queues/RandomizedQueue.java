import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    // number of elements in the randomized queue list
    private int randomQueueSize;
    // randomized queue array
    private Item[] randomQueue;

    // construct an empty randomized queue
    public RandomizedQueue() {
        randomQueueSize = 0;
        // I chose initial capacity to be 2;
        randomQueue = (Item[]) new Object[2];
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

    // is the randomized queue empty?
    public boolean isEmpty() {
        return randomQueueSize == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return randomQueueSize;
    }

    // resizes the array
    private void resize(int capacity) {
        Item[] randomQueueTemp = (Item[]) new Object[capacity];
        for (int i = 0; i < size(); i++) {
            randomQueueTemp[i] = randomQueue[i];
        }
        randomQueue = randomQueueTemp;
    }

    // add the item
    public void enqueue(Item item) {
        isArgumentNull(item);
        if (size() == randomQueue.length) {
            resize(randomQueue.length * 2);
        }

        randomQueue[size()] = item;
        randomQueueSize += 1;
    }

    // remove and return a random item
    public Item dequeue() {
        isCollectionEmpty();
        if (3 * size() <= randomQueue.length) {
            resize(randomQueue.length / 2);
        }
        int randomItem = StdRandom.uniformInt(size());
        Item item = randomQueue[randomItem];
        randomQueue[randomItem] = randomQueue[size() - 1];
        randomQueue[size() - 1] = null;
        randomQueueSize -= 1;
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        isCollectionEmpty();
        int randomItem = StdRandom.uniformInt(randomQueueSize);
        return randomQueue[randomItem];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomOrderIterator();
    }

    private class RandomOrderIterator implements Iterator<Item> {
        // index of next item to return
        private int index = randomQueueSize - 1;
        // shuffled array
        private int[] shuffled = new int[randomQueueSize];

        // constructor
        private RandomOrderIterator() {
            for (int j = 0; j < randomQueueSize; j++) {
                shuffled[j] = j;
            }
            StdRandom.shuffle(shuffled);
        }

        public boolean hasNext() {
            return index >= 0;
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Item item = randomQueue[shuffled[index]];
            index -= 1;
            return item;
        }

    }

    // unit testing (optional)
    public static void main(String[] args) {
        RandomizedQueue<Integer> test = new RandomizedQueue<>();
        System.out.println(test.isEmpty());
        test.enqueue(1);
        test.enqueue(2);
        test.enqueue(3);
        test.enqueue(4);
        System.out.println(test.size());
        System.out.println(test.dequeue());
        System.out.println(test.sample());
        for (int element : test) {
            System.out.print(element + " ");
        }
        System.out.println();
    }

}
