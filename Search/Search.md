# 查找算法

## 链表（顺序查找）

## 二分查找

## 二叉树查找

定义：首先是一棵二叉树，其次，每个结点的键都大于其左结点的键，小于右结点的键。

### 二叉查找树的插入与查找效率

- 时间复杂度：O(lgN)	

- 空间复杂度：O(lgN)

> 当顺序或者逆序的情况下，时间空间复杂度将达到最差的级别线性级别。

### 常规操作

- 查找
- 插入
- 删除
- 寻找最大（最小）值

### 实现

```java
package Search;

public class BST_search<Key extends Comparable<Key>, Value> {
    private class Node {
        private Key key;            // 键
        private Value value;        // 值
        private Node left, right;   // 指向左右子树的链接
        private int n;              // 以该结点为根的子树中的结点总数

        public Node(Key key, Value value, int n) {
            this.key = key;
            this.value = value;
            this.n = n;
        }
    }

    private Node root;

    public int size() {
        return size(root);
    }

    private int size(Node root) {
        if (root == null) return 0;
        else return root.n;
    }

    public Value get(Key key) {
        return get(root, key);
    }

    private Value get(Node x, Key key) {
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if      (cmp < 0)   return get(x.left, key);
        else if (cmp > 0)   return get(x.right, key);
        else                return x.value;
    }

    public void put(Key key, Value value) {
        root = put(root, key, value);
    }

    private Node put(Node x, Key key, Value value) {
        if (x == null) x = new Node(key, value, 1);
        int cmp = key.compareTo(x.key);
        if      (cmp < 0) x.left = put(x.left, key, value);
        else if (cmp > 0) x.right = put(x.right, key, value);
        else x.value = value;
        x.n = size(x.left) + size(x.right) + 1;
        return x;
    }

    public Key min() {
        return min(root).key;
    }

    private Node min(Node x) {
        if (x.left == null)
            return x;
        else
            return min(x.left);
    }

    public Node deleteMin() {
        return deleteMin(root);
    }

    private Node deleteMin(Node x) {
        if (x.left == null) return x.right;
        x.left = deleteMin(x.left);
        x.n = size(x.left) + size(x.right) + 1;
        return x;
    }

    public void delete(Key key) {
        root = delete(root, key);
    }

    private Node delete(Node x, Key key) {
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if      (cmp < 0) x.left = delete(x.left, key);
        else if (cmp > 0) x.right = delete(x.right, key);
        else {
            if (x.right == null) return x.left;
            if (x.left == null)  return x.right;
            Node t = x;
            x = min(t.right);               // 被删除结点的替代者为其右子树中最小的结点
            x.right = deleteMin(t.right);   // 替代结点的右子树为删除给结点后的右子树
            x.left = t.left;
        }
        x.n = size(x.left) + size(x.right) + 1;
        return x;
    }

    public static void main(String args[]) {
        BST_search<Integer, String> bst_search = new BST_search();
        bst_search.put(5,"a");
        bst_search.put(6, "c");
        bst_search.put(3,"d");
        bst_search.put(2, "b");
        bst_search.put(4, "g");
        bst_search.put(10, "t");
        System.out.println(bst_search.get(5));
        System.out.println(bst_search.get(6));
        System.out.println(bst_search.min());
        bst_search.delete(6);
        System.out.println(bst_search.get(6));
        bst_search.put(5,"aa");
        System.out.println(bst_search.get(5));

    }
}
```

## 平衡二叉树

插入、查找、删除均达到了对数级别的时间复杂度

完美平衡的 2-3 查找树：空链接到根结点的距离都应该相同

在一棵大小为 N 的 2-3 树中，查找和查询操作访问的结点必然不超过 lgN 个

## 红黑二叉查找树

将 3-结点 表示为由一条红色链接相连的两个 2-结点

- 红链接均为左链接
- 任意一个结点不可能同时与两条红链接相连（重点）
- 任意空链接到根结点的路径上的黑链接数量相同，即完美黑色平衡

> 红黑树既是二叉查找树，也是 2-3 树，可以集二叉查找树中简洁高效的查找方法与 2-3 树中高效的平衡插入算法于一体

P275 

## 实现

需要理解的是如何恢复完美黑色平衡，涉及到的操作有：左旋、右旋、变色（**重点**）

此外，除了插入、删除（较难）涉及到平衡的恢复，颜色的变化，其余操作与二叉查找树基本类似。
```java
public class RedBlackTree <Key extends Comparable<Key>, Value> {
    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private Node root;

    private class Node {
        Key key;
        Value value;
        Node left, right;
        int N;                  // 这棵树中结点总数
        boolean color;          // 由父结点指向它的链接的颜色

        Node(Key key, Value value, int N, boolean color) {
            this.color = color;
            this.key = key;
            this.value = value;
            this.N = N;
        }
    }

    private boolean isRed(Node x) {
        if (x == null)
            return false;
        else
            return x.color == RED;
    }

    public int size() {
        return size(root);
    }

    private int size(Node x) {
        if (x == null)
            return 0;
        else
            return x.N;
    }

    /**
     * 左旋操作：当右链为红色的时候进行左旋恢复平衡
     * @param h
     * @return
     */

    Node rotateLeft(Node h) {
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        x.color = h.color;
        h.color = RED;
        x.N = h.N;
        h.N = 1 + size(h.left) + size(h.right);
        return x;
    }

    /**
     * 右旋操作：当与某个结点相关联的链全为红色的时候，需要右旋恢复平衡
     * @param h
     * @return
     */

    Node rotateRight(Node h) {
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        x.color = h.color;      // 保存原来的颜色
        h.color = RED;
        x.N = h.N;
        h.N = 1 + size(h.left) + size(h.right);
        return x;
    }

    /**
     * 颜色变换，当某个结点的左右链接都为红色链接的时候进行变换为黑色，红色链接向上传递
     * @param h
     */
    void flipColors(Node h) {
        h.color = RED;
        h.left.color = BLACK;
        h.right.color = BLACK;
    }

    public void put(Key key, Value value) {
        // 查找 key，找到就更新其值，否则为它创建一个结点
        root = put(root, key, value);
        root.color = BLACK;
    }

    private Node put(Node h, Key key, Value value) {
        if (h == null) // 标准的插入操作，父结点使用红链相连
            return new Node(key, value, 1, RED);
        // 查找过程
        int cmp = key.compareTo(h.key);
        if      (cmp < 0) h.left = put(h.left, key, value);
        else if (cmp > 0) h.right = put(h.right, key, value);
        else h.value = value;
        // 恢复过程
        if (isRed(h.left) && isRed(h.right))        flipColors(h);
        if (isRed(h.left) && isRed(h.left.left))    h = rotateRight(h);
        if (!isRed(h.left) && isRed(h.right))       h = rotateLeft(h);
        h.N = 1 + size(h.left) + size(h.right);
        return h;
    }

    public Value get(Key key) {
        return get(root, key);
    }

    private Value get(Node x, Key key) {
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if      (cmp < 0) return get(x.left, key);
        else if (cmp > 0) return get(x.right, key);
        else              return x.value;
    }

    public Value min() {
        return min(root).value;
    }

    private Node min(Node x) {
        if (x.left == null) return x;
        return min(x.left);
    }

    public static void main(String args[]) {
        RedBlackTree<Integer, String> redBlackTree = new RedBlackTree();
        redBlackTree.put(5,"a");
        redBlackTree.put(6, "c");
        redBlackTree.put(3,"d");
        redBlackTree.put(2, "b");
        redBlackTree.put(4, "g");
        redBlackTree.put(10, "t");
        System.out.println(redBlackTree.get(5));
        System.out.println(redBlackTree.get(6));
        System.out.println(redBlackTree.min());
        redBlackTree.put(5,"aa");
        System.out.println(redBlackTree.get(5));

    }
}

```

## 散列表查找（哈希表）

使用散列表存储数据，在一般情况下查找和插入可以达到接近常数级别，但在最坏的情况下（聚簇比较长，前半部分为空，后半部分填满）会达到线性级别

> 试图通过计算散列值的方式替换比较的方式

### 散列函数

假设有 N 个键，M 个存储位置，那么经过散列函数的运算后，散列值必须落在 0 ~ M - 1 中。（重点）

优秀的散列函数应满足的条件：

- 一致性
- 高效性
- 均匀性

常用的散列函数：

- 除留余数法
- 浮点数（将键表示为二进制后进行除留余数操作，Java 中是这么操作的）
- 字符串（Java 中 String 的操作类似以下操作，String 对象的 hashcode() 方法使用了**软缓存**方法减少计算量）
```java
int hash = 0;
for(int i = 0; i < s.length; i++) 
	hash = (R * hash + s.charAt(i)) % M;
``` 
- 组合键：以日期举例
```java
int hash = (((day * R + mounth) % M) * R + year) % M;
```

Java 令所有的数据类型都继承了一个能够返回一个 32 bit 整数的 hashcode() 方法。

若需要为自定义的数据类型定义散列函数，需要同时重写 hashcode()、equals()。

> 每一种类型的 hashcode() 方法与 equals() 方法使用结果是一致的

如何将 hashcode() 的返回值转化为一个数组索引

```java
private int hash(Key x) {
	return (x.hashcode() & 0x7fffffff) % M;
}
```
### 碰撞的处理

碰撞指的是同一个散列值对应了多个键，这时候需要进行处理。

拉链法使用了链表的方式，将相同散列值的元素都存储在一个链表中。

线性探测法则是通过不断往下探测数组值为 null 的位置，找到 null 的位置就将元素放入该位置。

### 缺点

不合适进行有序性操作

> 当 (a = N / M) < 0.5 时，可以保持较高的效率  

### 实现

主要包括两个步骤：

- 根据散列函数将被查找的键转换为数组的索引
- 处理碰撞冲突
  - 拉链法	 （链表的方式） 
  - 线性探测法（数组方式）

#### 拉链法

```java
package Search;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;

public class SeparateChainingHashST<Key, Value> {

    class SequentialSearchST<Key, Value> implements Iterable<Key>{

        private Node head;
        private int N;

        private class Node {
            private Key key;
            private Value value;
            Node next;

            private Node(Key key, Value value) {
                this.key = key;
                this.value = value;
                this.next = null;
            }
        }

        public void put(Key key, Value value) {
            // 在头部创建一个元素
            if (head == null) head = new Node(key, value);
            else {
                Node newNode = new Node(key, value);
                newNode.next = head;
                head = newNode;
            }
        }

        public Value get(Key key) {
            Node node = head;
            while (node != null) {
                if (key.equals(node.key))
                    return node.value;
                node = node.next;
            }
            return null;
        }

        public void delete(Key key) {
            Node index = head;
            if (key.equals(index.key))
                head = index.next;
            while (index.next != null) {
                if (index.next.key.equals(key)) {
                    Node node = index.next;
                    index.next = node.next;
                }
            }
        }

        public Iterator<Key> iterator() {
            return new SequentialSearchSTItertor();
        }

        private  class SequentialSearchSTItertor implements Iterator<Key> {
            private Node current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public void remove() {

            }

            @Override
            public Key next() {
                Key key = current.key;
                current = current.next;
                return key;
            }
        }
    }

    private int M;
    private SequentialSearchST<Key, Value>[] st;

    public SeparateChainingHashST(){
        this(997);
    }

    public SeparateChainingHashST(int M){
        this.M = M;
        st = (SequentialSearchST<Key, Value>[])new SequentialSearchST[M];
        for (int i = 0; i < M; i++) {
            st[i] = new SequentialSearchST<>();
        }
    }

    private int hash(Key key){
        return (key.hashCode() & 0x7fffffff) % M;
    }

    private Value get(Key key){
        return (Value) st[hash(key)].get(key);
    }

    public void put(Key key, Value Val){
        st[hash(key)].put(key, Val);
    }

    public void delete(Key key) {
        st[hash(key)].delete(key);
    }

    public Iterable<Key> keys() {
        Queue<Key> queue = new ArrayDeque<>();
        for (SequentialSearchST head : st) {
            Iterator iterator = head.iterator();
            while (iterator.hasNext()) {
                Key key = (Key) iterator.next();
                queue.add(key);
            }
        }
        return queue;
    }

    public static void main(String args[]) {
        SeparateChainingHashST<Integer, String> separateChainingHashST = new SeparateChainingHashST();
        separateChainingHashST.put(1, "abc");
        separateChainingHashST.put(2, "cad");
        separateChainingHashST.put(5,"ddd");
        separateChainingHashST.put(8, "eee");
        separateChainingHashST.put(7, "dasda");
        System.out.println(separateChainingHashST.get(7));
        separateChainingHashST.delete(7);
        System.out.println(separateChainingHashST.get(7));
        Iterable iterable = separateChainingHashST.keys();
        Iterator iterator = iterable.iterator();
        while (iterator.hasNext()) {
            Integer key = (Integer) iterator.next();
            System.out.print(key + " ");
        }
    }
}
```

### 线性探测法

```java
package Search;

public class LinearProbingHashST<Key, Value> {
    private int N;      // 表中的键值对总数
    private int M = 16; // 存储表的长度
    private Key[] keys;
    private Value[] values;

    public LinearProbingHashST() {
        keys = (Key[]) new Object[M];
        values = (Value[]) new Object[M];
    }

    public LinearProbingHashST(int cap) {
        keys = (Key[]) new Object[cap];
        values = (Value[]) new Object[cap];
        M = cap;
    }
    private int hash(Key key) {
        return (key.hashCode() & 0x7fffffff);
    }

    private void resize(int cap) {
        LinearProbingHashST linearProbingHashST = new LinearProbingHashST(cap);
        for (int i = 0; i < keys.length; i++) {
            linearProbingHashST.keys[i] = keys[i];
            linearProbingHashST.values[i] = values[i];
        }
        keys = (Key[]) linearProbingHashST.keys;
        values = (Value[]) linearProbingHashST.values;
        M = linearProbingHashST.M;

    }

    public void put(Key key, Value value) {
        if (N >= M / 2) resize(2 * N);
        int i;
        for (i = hash(key); keys[i] != null; i = (i + 1) % M) {
            if (keys[i].equals(key)) values[i] = value;
        }
        keys[i] = key;
        values[i] = value;
        N++;
    }

    public Value get(Key key) {
        for (int  i = hash(key); keys[i] != null; i = (i + 1) % M)
            if (keys[i].equals(key))
                return values[i];
        return null;
    }

    public void delete(Key key) {
        int i = hash(key);
        int count = 0;
        while (!key.equals(keys[i]) && count < 16) {
            i = (i + 1) % M;
            count++;
        }
        if (count >= M) return;            // 判断该值是否存在
        else {
            // 空表示删除该值
            keys[i] = null;
            values[i] = null;
            // 维护后续集簇
            i = (i + 1) % M;
            while (keys[i] != null) {
                Key oldKey = keys[i];
                Value oldValue = values[i];
                keys[i] = null;
                values[i] = null;
                N--;
                put(oldKey, oldValue);
                i = (i + 1) % M;
            }
            N--;
        }

    }

    public static void main(String args[]) {
        LinearProbingHashST<Integer, String> linearProbingHashST = new LinearProbingHashST();
        linearProbingHashST.put(1, "abc");
        linearProbingHashST.put(2, "cad");
        linearProbingHashST.put(5,"ddd");
        linearProbingHashST.put(8, "eee");
        linearProbingHashST.put(7, "dasda");
        System.out.println(linearProbingHashST.get(7));
        linearProbingHashST.delete(7);
        System.out.println(linearProbingHashST.get(7));
    }
}
```

Java 的 TreeMap 是基于红黑树实现的，HashMap 则属基于拉链法的散列表实现的 ，当以上的操作不涉及键值对，只涉及键的集合的时候，以上操作
可以演变为 set 类的有序与无序集合。

### 应用

根据符号表的不可重复性，可用于过滤器（黑白名单程序，比如实现电子邮件的垃圾邮件处理）

 

