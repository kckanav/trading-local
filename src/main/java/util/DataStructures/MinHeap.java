package util.DataStructures;

import Testing.Util.AtomicResult;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * See cse332/interfaces/worklists/PriorityWorkList.java
 * for method specifications.
 */
public class MinHeap implements Iterable<AtomicResult> {
    /* Do not change the name of this field; the tests rely on it to work correctly. */

    private static final int INITAL_CAPACITY = 10;
    private AtomicResult[] data;
    private int SIZE;

    public MinHeap() {
        this.data = new AtomicResult[INITAL_CAPACITY];
        this.SIZE = -1;
    }

    public boolean hasWork() {
        if (data[0] == null) { //SIZE == 0
            return false;
        } else {
            return true;
        }
    }

    public void add(AtomicResult work) {
        if (SIZE == data.length - 1) {
            AtomicResult[] tempArr = new AtomicResult[SIZE * 2];
            for (int a = 0; a < data.length; a++) {
                tempArr[a] = data[a];
            }
            data = tempArr;
        }
        SIZE++;
        data[SIZE] = work;
        //SIZE++;
        percolateUp(SIZE);
    }

    private void percolateUp(int index) {
        if (index <= 0) {
            return;
        }
        int parent = (index - 1) / 4;
        if (data[parent].compareTo(data[index]) > 0) {
            AtomicResult temp = data[index];
            data[index] = data[parent];
            data[parent] = temp;
            percolateUp(parent);
        }
    }

    public AtomicResult peek() {
        if(data[0] == null) {
            throw new NoSuchElementException();
        }
        return data[0];

        //return data[];
        //throw new NotYetImplementedException();
    }

    public AtomicResult next() {
        peek();
        AtomicResult val = data[0];
        data[0] = data[SIZE];
        data[SIZE] = null;
        SIZE--;
        percolateDown(0);
        return val;
    }

    private void percolateDown(int index) {
        int lowest = index;
        for (int i = 1; i <= 4; i++ ) {
            int child = index * 4 + i;
            if (child <= SIZE && data[lowest].compareTo(data[child]) > 0) {
                lowest = child;
            }
        }
        if (lowest == index) {
            return;
        } else {
            AtomicResult temp = data[index];
            data[index] = data[lowest];
            data[lowest] = temp;
            percolateDown(lowest);
        }
    }

    public int size() {
        return SIZE + 1;
        //throw new NotYetImplementedException();
    }

    public String toString() {
        String s = "";
        for (int i = 0; i < SIZE; i++) {
            s += data[i].toString() + ", ";
        }
        s += data[SIZE];
        return s;
    }

    public Iterator<AtomicResult> iterator() {
        return new MyIterator();
    }

    private class MyIterator implements Iterator<AtomicResult> {

        private int currIndex = 0;
        @Override
        public boolean hasNext() {
            return currIndex <= SIZE;
        }

        @Override
        public AtomicResult next() {
            if (hasNext()) {
                currIndex++;
                return data[currIndex - 1];
            } else {
                throw new IllegalStateException("No more items");
            }
        }
    }
}
