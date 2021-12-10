package util.DataStructures;

import Testing.Util.AtomicResult;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public class TopKResults<E extends AtomicResult> implements Iterable<AtomicResult> {

    private int k;

    public int errorCounts;

    private MinHeap heap;

    public TopKResults(int k) {
        this.k = k;
        heap = new MinHeap();
    }

    public void add(AtomicResult elem) {
        if (!elem.shouldInclude()) {
            return;
        }
        if (heap.size() < k) {
            heap.add(elem);
        } else {
            heap.add(elem);
            heap.next();
        }
    }

    public AtomicResult getMin() {
        return heap.next();
    }

    public boolean hasNext() {
        return heap.hasWork();
    }

    public TopKResults<E> combine(TopKResults<E> obj) {
        while (obj.heap.hasWork()) {
            add(obj.heap.next());
        }
        this.errorCounts = errorCounts + obj.errorCounts;
        return this;
    }

    public int size() {
        return heap.size();
    }

    // Returns Sorted Decresing order
    public String toString() {
        String s = "Errors: " + errorCounts + "\n";
        MinHeap temp = new MinHeap();
        Stack<AtomicResult> stack = new Stack();
        while (heap.hasWork()) {
            stack.push(heap.next());
            temp.add(stack.peek());
        }
        while (!stack.isEmpty()) {
            s += stack.pop().toString() + " \n";
        }
        heap = temp;
        return s;
    }


    public List<String> getAllSymbols() {
        List<String> l = new ArrayList<>();
        for (AtomicResult r: heap) {
            l.add(r.symbol());
        }
        return l;
    }

    @Override
    public Iterator<AtomicResult> iterator() {
        return heap.iterator();
    }
}
