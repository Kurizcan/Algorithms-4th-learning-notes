/**
 *
 * Linked List Cycle II
 *
 **/


/**
 * Definition for singly-linked list.
 * class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode(int x) {
 *         val = x;
 *         next = null;
 *     }
 * }
 */
public class Solution {
    public ListNode detectCycle(ListNode head) {
        if (head == null || head.next == null) {
            return null;   // no circle
        }
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
            if (fast == slow) {  // circle detected
                while (head != fast) {
                    fast = fast.next;
                    head = head.next;
                }
                return head;	// 当检测到有圆环的时候，从头在走一遍，一步一步的走直到相遇
            }
        }
        return null; // no circle
    }
}

public ListNode detectCycle(ListNode head) {
    Set<ListNode> set = new HashSet<>();
    ListNode cur = head;
    while(cur != null) {
        if (!set.add(cur)) return cur;
            cur = cur.next;
    }
    return cur;
}	// 使用了 hashset 的不可重复的特性，但是只能应对链表本身没有重复项的情况
