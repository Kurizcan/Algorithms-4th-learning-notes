/**
 *  Linked List Cycle
 *
 */

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

// 龟兔赛跑写法一，空间复杂度相对写法二高，但是速度快
public class Solution {
    public boolean hasCycle(ListNode head) {
   		ListNode runer_one, runer_two;
   		runer_one = runer_two = head;
   		while(runer_one != null && runer_two != null && runer_two.next != null) {
   			// 这个比较条件容易出错，关键需要一步一步的遍历，所以要一步一步的判断，runer_two.next 要注意

   			runer_one = runer_one.next;
   			runer_two = runer_two.next.next;
   			
   			if (runer_one == runer_two) {
   				return true;
   			}
   		}
   		return false;    
    }
}


public class Solution {
    public boolean hasCycle(ListNode head) {
        if(head==null || head.next==null){
            return false;
        }
        ListNode slow = head.next;
        ListNode fast = head.next.next;
        
        while(fast!=null && fast.next!=null){
            if(slow==fast){
                System.out.println(slow.val);
                return true;
            }
            slow = slow.next;
            fast = fast.next.next;
        }
        return false;
    }
}

