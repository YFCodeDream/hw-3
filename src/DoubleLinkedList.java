import java.util.Objects;

/**
 * @author YFCodeDream
 * @version 1.0.0
 * @date 2022/2/23
 * @description 双向链表
 */
@SuppressWarnings("GrazieInspection")
public class DoubleLinkedList<T> implements SimpleList<T> {
    /**
     * 双向链表的大小
     */
    private int size;

    /**
     * 标记头部
     */
    private Node<T> head;

    /**
     * 标记尾部
     */
    private Node<T> tail;

    /**
     * 默认构造方法
     * 初始化head与tail均为null
     * 初始化size为0
     */
    public DoubleLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    /**
     * 返回链表长度
     * @return 链表长度
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * 返回链表是否为空
     * @return 链表是否为空
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * 获取第i个元素
     * @param i the index to be queried
     * @return 第i个元素的值
     */
    @Override
    public T get(int i) {
        if (i < 0 || i >= size) {
            throw new IndexOutOfBoundsException();
        }

        return Objects.requireNonNull(getNode(i)).value;
    }

    /**
     * 设置第i个元素的值
     * @param i the index of the element to be set
     * @param x the element to be inserted at index <code>i</code>
     */
    @Override
    public void set(int i, T x) {
        if (i < 0 || i >= size) {
            throw new IndexOutOfBoundsException();
        }

        Node<T> node = getNode(i);
        Objects.requireNonNull(node).value = x;
    }

    /**
     * 在第i个位置添加值为x的节点
     * @param i the index into which to insert the element <code>x</code>
     * @param x the element to be inserted at index <code>i</code>
     */
    @Override
    public void add(int i, T x) {
        if (i < 0 || i > size) {
            throw new IndexOutOfBoundsException();
        }

        Node<T> addNode = new Node<>(x);

        /*
         * 这里分成两种情况
         * 1. 在头部添加节点，则：
         *      头部节点的先前节点置空
         *      待添加节点的后置节点置为原头节点
         *      将头部节点赋值为待添加节点
         * 2. 在链表中间插入节点，则：
         *      获取待插入位置先前节点
         *      获取待插入位置后继节点
         *      先前节点必不为空，将先前节点的后继节点置为待插入节点，待插入节点的后继节点置为原后继节点
         *      待插入节点的先前节点置为原先前节点，若原后继节点不为空，则将原后继节点的先前节点置为待插入节点
         */
        if (i == 0) {
            addNode.next = head;
            addNode.prev = null;
            head = addNode;
        } else {
            Node<T> aheadNode = getNode(i - 1);
            Node<T> hinderNode = Objects.requireNonNull(aheadNode).next;
            aheadNode.next = addNode;
            addNode.next = hinderNode;
            addNode.prev = aheadNode;
            if (hinderNode != null) {
                hinderNode.prev = addNode;
            }
        }

        // 修改size和tail标记
        size += 1;
        tail = getNode(size - 1);

        // 更新序列号
        renewIndices();
    }

    /**
     * 删除第i个元素
     * @param i the index of the element to be removed
     * @return 已删除元素的值
     */
    @Override
    public T remove(int i) {
        if (i < 0 || i > size) {
            throw new IndexOutOfBoundsException();
        }

        /*
         * 先讨论删除头部的情况，则：
         *      获取头部节点的后继节点
         *      将新头部置为原头部的后继节点
         *      若原头部的后继节点非空，将其先前节点置空
         *      将size减一
         *      tail赋值为size-1处的节点
         *      更新序列号
         */
        if (i == 0) {
            Node<T> hinderNode = getNode(i + 1);
            T removedValue = head.value;

            head = head.next;

            if (hinderNode != null) {
                Objects.requireNonNull(hinderNode).prev = null;
            }

            size -= 1;
            tail = getNode(size - 1);
            renewIndices();

            return removedValue;
        }

        /*
         * 再讨论内部节点，则：
         *      先获取先前节点（此时先前节点必不为空，因为头部节点的情况已经在上面讨论过了）
         *      获取待删除节点
         *      将先前节点的后继节点置为待删除节点的后继节点
         *      如果待删除节点的后继节点非空，则将其先前节点设置为待删除节点的先前节点
         *      将size减一
         *      tail赋值为size-1处的节点
         *      更新序列号
         */
        Node<T> aheadNode = getNode(i - 1);
        Node<T> removedNode = getNode(i);

        assert aheadNode != null;
        assert removedNode != null;

        aheadNode.next = removedNode.next;

        if (removedNode.next != null) {
            removedNode.next.prev = aheadNode;
        }

        size -= 1;
        tail = getNode(size - 1);
        renewIndices();

        return removedNode.value;
    }

    /**
     * 节点私有内部类
     * @param <E> 节点值泛型
     */
    private static class Node<E> {
        /**
         * 标记先前节点
         */
        Node<E> next;

        /**
         * 标记后继节点
         */
        Node<E> prev;

        /**
         * 节点值
         */
        E value;

        /**
         * 当前节点序列号，可无需此属性，该属性便于查看节点信息
         */
        int index;

        public Node(E value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "next=" + (next == null ? "null" : next.index) +
                    ", prev=" + (prev == null ? "null" : prev.index) +
                    ", value=" + value +
                    '}';
        }
    }

    /**
     * 辅助方法，依据索引返回指定节点
     * 其实为返回index == i的节点
     * @param i 索引值
     * @return 指定索引值的节点
     */
    private Node<T> getNode(int i) {
        if (i < 0 || i >= size) return null;

        Node<T> currentNode = head;

        for (int j = 0; j < i; j++) {
            currentNode = currentNode.next;
        }

        return currentNode;
    }

    /**
     * 辅助方法，更新索引值
     */
    private void renewIndices() {
        int index = 0;
        for (Node<T> node = head; node != null; node = node.next) {
            node.index = index;
            index += 1;
        }
    }

    /**
     * 重写的toString方法，便于查看双向链表信息
     * @return 存有双向链表信息的字符串
     */
    @Override
    public String toString() {
        StringBuilder resStr = new StringBuilder();
        resStr.append("LinkedDoubledList: {\n");
        int index = 0;
        for (Node<T> node = head; node != null; node = node.next) {
            resStr.append("\t").append("node ").append(index).append(":\n\t").append(node).append("\n");
            index += 1;
        }
        resStr.append("}");
        return resStr.toString();
    }
}
