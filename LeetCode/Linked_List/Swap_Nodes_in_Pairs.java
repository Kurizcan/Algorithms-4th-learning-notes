/**
 * Swap Nodes in Pairs
 *
 *
**/

# Definition for singly-linked list.
# class ListNode:
#     def __init__(self, x):
#         self.val = x
#         self.next = None

class Solution:
    def swapPairs(self, head: ListNode) -> ListNode:
    	pre, pre.next = self, head	
    	# 使用 self 当前对象的 next 属性为头指针，保证头指针不丢失
    	while pre.next and pre.next.next:
    		a = pre.next
    		b = a.next
    		pre.next, a.next, b.next = b, b.next, a
    		pre = a
    	return self.next

/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode(int x) { val = x; }
 * }
 */
class Solution {
    public ListNode swapPairs(ListNode head) {
		ListNode newhead, a, b;
        ListNode prev = new ListNode(0);
        if(head == null || head.next == null)
            return head;		 // 注意特殊情况
        else
        	newhead = head.next; // 先确定头指针
        prev.next = head;
        while(prev.next != null && prev.next.next != null) {
        	a = prev.next;
        	b = a.next;
        	prev.next = b;
        	a.next = b.next;
        	b.next = a;
        	prev = a;
		}
		return newhead;
    }
}

/**
 * 递归写法，空间复杂度较遍历小很多，但是很难想到 O(n)
 */
class Solution {
    public ListNode swapPairs(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode nxt = head.next;
        head.next = swapPairs(nxt.next);
        nxt.next = head;
        return nxt;
    }
}




