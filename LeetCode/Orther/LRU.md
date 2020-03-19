# LRU cache

使用一个 Hashmap 存储结点，值为 Node，一个双向链存储访问的次序，最新访问的结点在末尾。通过 HashMap，可以快速定位 Node 在双向链表中的位置，在更新访问结点的时候不需要从头遍历双向链表。

```java
class LRUCache {
  
  HashMap<Integer, Node> map = new HashMap<>();
  Node head;
  Node tail;
  int size;
  
  class Node {
    Node pre;
    Node next;
    int key;
    int val;
    public Node(int key, int val) {
      this.key = key;
      this.val = val;
    }
  }
  
  public void moveToTail(Node node) {
    // 如果是恰好是尾节点，不做任何处理
    if (tail == node) {
      return;
    }
    // 头结点的处理
    if (head == node) {
      head = node.next;
      head.pre = null;
    } else {
      // 中间节点的处理
      node.pre.next = node.next;
      node.next.pre = node.pre;
    }
    tail.next = node;
    node.pre = tail;
    node.next = null;
    tail = node;
  }
  
  // 删除首元素
  public Node remove() {
    // 首元素是否为空
    if (head == null) {
      return null;
    }
    // 是否只有一个元素
    Node res = head;
    if (head == tail) {
      head = null;
      tail = null;
    } else {
      head = res.next;
      head.pre = null;
      res.next = null;
    }
    return res;
  } 
  
  public void add(Node node) {
    if (node == null) {
      return;
    }
    if (head == null) {
      head = node;
      tail = node;
      return;
    }
    tail.next = node;
    node.pre = tail;
    tail = node;
  }
  
  public LRUCache(int capacity) {
    this.size = capacity;
  }
  
  public int get(int key) {
    Node node = map.get(key);
    if (node == null) {
      return -1;
    }
    moveToTail(node);
    return node.val;
  }
  
  public void put(int key, int value) {
    Node node = map.get(key);
    if (node == null) {
      node = new Node(key, value);
      if (map.size() == size) {
        Node del = remove();
        map.remove(del.key);
      }
      map.put(key, node);
      add(node);
    } else {
      node.val = value;
      moveToTail(node);
    }
  }
}

/**
 * Your LRUCache object will be instantiated and called as such:
 * LRUCache obj = new LRUCache(capacity);
 * int param_1 = obj.get(key);
 * obj.put(key,value);
 */
```
