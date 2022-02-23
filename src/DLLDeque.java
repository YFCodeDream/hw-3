/**
 * @author YFCodeDream
 * @version 1.0.0
 * @date 2022/2/23
 * @description 以双向链表为底层数据结构的双端队列
 */
public class DLLDeque<T> implements SimpleDeque<T> {
    private final DoubleLinkedList<T> elements;

    public DLLDeque() {
        elements = new DoubleLinkedList<>();
    }

    @Override
    public int size() {
        return elements.size();
    }

    @Override
    public boolean isEmpty() {
        return elements.isEmpty();
    }

    @Override
    public void addFirst(T x) {
        elements.add(0, x);
    }

    @Override
    public T removeFirst() {
        return elements.remove(0);
    }

    @Override
    public T peekFirst() {
        return elements.get(0);
    }

    @Override
    public void addLast(T x) {
        elements.add(elements.size(), x);
    }

    @Override
    public T removeLast() {
        return elements.remove(elements.size() - 1);
    }

    @Override
    public T peekLast() {
        return elements.get(elements.size() - 1);
    }
}
