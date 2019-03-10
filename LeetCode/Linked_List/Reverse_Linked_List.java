/**
 * Reverse Linked List
 *
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode(int x) { val = x; }
 * }
 */

class Solution {
    public ListNode reverseList(ListNode head) {
        ListNode cur = head;
        ListNode pre = null;
        while(cur != null) {
        	cur.next = pre;			// 这样写是错误的，缺少中间值过渡，使得指向有误
        	pre = cur;
        	cur = pre.next;
        }
        return pre;
    }
}

正确写法：

class Solution {
    public ListNode reverseList(ListNode head) {
        ListNode cur = head;
        ListNode pre = null;
        while(cur != null) {
        	ListNode node = cur.next;	// 需要建立一个临时变量进行存储下一个结点
        	cur.next = pre;	
        	pre = cur;
        	cur = node;
        }
        return pre;
    }
}


递归写法

class Solution {
    public ListNode reverseList(ListNode head) {
        if (head == null || head.next == null) return head;
        ListNode p = reverseList(head.next);
        head.next.next = head;
        head.next = null;
        return p;
    }
}

# python3 遍历写法

# Definition for singly-linked list.
# class ListNode:
#     def __init__(self, x):
#         self.val = x
#         self.next = None

class Solution:
    def reverseList(self, head: ListNode) -> ListNode:
        cur, pre = head, None
        while cur:
            cur.next, pre, cur = pre, cur, cur.next
        return pre

# python3 递归写法

# Definition for singly-linked list.
# class ListNode:
#     def __init__(self, x):
#         self.val = x
#         self.next = None

class Solution:
    def reverseList(self, head):
        return self._reverse(head)

    def _reverse(self, node, prev=None):
        if not node:
            return prev
        n = node.next
        node.next = prev
        return self._reverse(n, node)

