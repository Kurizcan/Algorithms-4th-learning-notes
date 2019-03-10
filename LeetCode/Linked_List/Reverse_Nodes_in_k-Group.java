/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode(int x) { val = x; }
 * }
 */

// 思路：行进间判断 k，k 个一组就要进行 k - 1 次两两交换

public class Solution {
        public ListNode reverseKGroup(ListNode head, int k) {
            if (k <= 1 || head == null || head.next == null)
                return head;					// 判断特殊情况
            ListNode newHead = new ListNode(0); 
            newHead.next = head;
            ListNode prev, start, then, tail;
            tail = prev = newHead;
            start = prev.next;
            while (true) {
                // check if there's k nodes left-out
                for (int i = 0; i < k; i++) {
                    tail = tail.next;
                    if (tail == null)
                        return newHead.next;
                }
                // reverse k nodes
                for (int i = 0; i < k - 1; i++) {
                    then = start.next;
                    start.next = then.next;
                    then.next = prev.next;
                    prev.next = then;
                }
                tail = prev = start;
                start = prev.next;
            }
        }
    }
