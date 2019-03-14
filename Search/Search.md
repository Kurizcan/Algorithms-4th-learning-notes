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


