# LeetCode  - 二叉树相关题目

## 得到左下角的节点

[513. Find Bottom Left Tree Value (Easy)](https://leetcode.com/problems/find-bottom-left-tree-value/description/)

思路：层次遍历即可，每次取每层的第一个结点的值覆盖保存即可。

```java
class Solution {
    public int findBottomLeftValue(TreeNode root) {
        ArrayDeque<TreeNode> arrayDeque = new ArrayDeque<>();
        arrayDeque.addLast(root);
        int target = -1;
        while (!arrayDeque.isEmpty()) {
            target = arrayDeque.peek().val;
            int size = arrayDeque.size();
            for (int i = 0; i < size; i++) {
                TreeNode node = arrayDeque.poll();
                if (node.left != null) arrayDeque.offer(node.left);
                if (node.right != null) arrayDeque.offer(node.right);
            }
        }
        return target;
    }
}
```

## 非递归实现二叉树的前序遍历

[144. Binary Tree Preorder Traversal(Easy)](https://leetcode.com/problems/binary-tree-preorder-traversal/)

非递归实现：需要保证遍历的顺序是根、左、右，那么使用栈替代递归时就需要保证右子树先入栈，左子树后入栈。

```java
class Solution {
    public List<Integer> preorderTraversal(TreeNode root) {
        List<Integer> list = new ArrayList<>();
        if (root == null)
            return list;
        ArrayDeque<TreeNode> stack = new ArrayDeque<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            TreeNode node = stack.pop();
            list.add(node.val);
            if (node.right != null) stack.push(node.right); // 保证左子树先遍历
            if (node.left != null) stack.push(node.left);
        }
        return list;
    }
}
```

递归实现

```java
class Solution {
    public List<Integer> preorderTraversal(TreeNode root) {
        List<Integer> list = new ArrayList<>();
        preOrder(root, list);
        return list;
    }

    public void preOrder(TreeNode root, List<Integer> list) {
        if (root == null) return;
        list.add(root.val);
        preOrder(root.left, list);
        preOrder(root.right, list);
    }
```

## 非递归实现二叉树的后序遍历

[145. Binary Tree Postorder Traversal (Medium)](https://leetcode.com/problems/binary-tree-postorder-traversal/description/)

递归实现：

```java
class Solution {
    public List<Integer> postorderTraversal(TreeNode root) {
        List<Integer> list = new ArrayList<>();
        postOrder(root, list);
        return list;
    }
    
    public void postOrder(TreeNode root, List<Integer> list) {
        if (root == null) return;
        postOrder(root.left, list);
        postOrder(root.right, list);
        list.add(root.val);
    }
}
```

非递归实现：这个比之前的要难，可以在前序遍历中修改下访问的顺序，前序遍历是 root - left - right，后序遍历是 left - right - root，那么先访问左结点先入栈，后结点后入栈，得到的是一个与后序遍历相反的顺序，在反转即可。

```java
class Solution {
    public List<Integer> postorderTraversal(TreeNode root) {
        List<Integer> list = new ArrayList<>();
        if (root == null)
            return list;
        ArrayDeque<TreeNode> stack = new ArrayDeque<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            TreeNode node = stack.pop();
            list.add(node.val);
            if (node.left != null) stack.push(node.left);
            if (node.right != null) stack.push(node.right);
        }
        Collections.reverse(list);  // 反转回正确的顺序
        return list;
    }
}
```

利用队列的头插法，同样可以在不反转的情况下，解决问题

```java
class Solution {
    public List<Integer> postorderTraversal(TreeNode root) {
        LinkedList<Integer> list = new LinkedList<>();
        ArrayDeque<TreeNode> stack = new ArrayDeque<>();
        TreeNode cur = root;
        while (cur != null || !stack.isEmpty()) {
            if (cur != null) {
                stack.push(cur);
                list.addFirst(cur.val); // 头插法，先插入的排在最后
                cur = cur.right;        // 右结点优先，比左结点更应该排在后边
            }
            else {
                cur = stack.pop();
                cur = cur.left;
            }
        }
        return list;
    }
}
```

## 非递归实现二叉树的中序遍历

[94. Binary Tree Inorder Traversal (Medium)](https://leetcode.com/problems/binary-tree-inorder-traversal/description/)

递归写法

```java
class Solution {
    public List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> list = new ArrayList<>();
        InOrder(root, list);
        return list;
    }
    
    public void InOrder(TreeNode root, List<Integer> list) {
        if (root == null) return;
        InOrder(root.left, list);
        list.add(root.val);
        InOrder(root.right, list);
    }
}
```

非递归写法：栈，需要做的就是左子树需要先全部入栈，右子树后入。

```java
class Solution {
    public List < Integer > inorderTraversal(TreeNode root) {
        List < Integer > res = new ArrayList < > ();
        ArrayDeque<TreeNode> stack = new ArrayDeque<>();
        TreeNode cur = root;
        while (cur != null || !stack.isEmpty()) {
            while (cur != null) {
                stack.push(cur);
                cur = cur.left;  // 左子树需要全部入列先
            }
            cur = stack.pop();
            res.add(cur.val);
            cur = cur.right;
        }
        return res;
    }
}
```

## 找出二叉树中第二小的节点

[671. Second Minimum Node In a Binary Tree (Easy)](https://leetcode.com/problems/second-minimum-node-in-a-binary-tree/submissions/)

思路一：由于要找最小的，且是第二小，我首先想到的是遍历整颗二叉树，将结点存储在一个最小堆的优先队列中，再取出第二小的。空间复杂度 O(n)

```java
class Solution {
    public int findSecondMinimumValue(TreeNode root) {
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>();
        InOrder(root, priorityQueue);
        while (!priorityQueue.isEmpty()) {
            Integer val = priorityQueue.remove();
            Integer nextSmall = priorityQueue.peek();
            if (nextSmall != null && val < nextSmall)
                return nextSmall;
        }
        return -1;
    }

    public void InOrder(TreeNode root, PriorityQueue priorityQueue) {
        if (root == null) return;
        InOrder(root.left, priorityQueue);
        priorityQueue.add(root.val);
        InOrder(root.right, priorityQueue);
    }
}
```

改进版：不使用优先队列，在遍历的过程中比较并记录最小值与次最小值

```java
class Solution {
    int min = Integer.MAX_VALUE;
    long second = Long.MAX_VALUE;

    public int findSecondMinimumValue(TreeNode root) {
        preOrder(root);
        return second < Long.MAX_VALUE? (int) second : -1;
    }

    public void preOrder(TreeNode root) {
        if (root == null) return;
        if (root.val < min) min = root.val;
        if (min < root.val && second > root.val) second = root.val;
        preOrder(root.left);
        preOrder(root.right);
    }

}
```

## 间隔遍历

[337. House Robber III (Medium)](https://leetcode.com/problems/house-robber-iii/)

这道题结合了动态规划与二叉树的遍历。对于小偷而言，房子一共有两个状态，即偷与不偷。

从根结点出发：

若偷根，则最大值会是 root + root.left.left + root.left.right + root.right.left + root.right.right ；

不偷根，则最大值是 root.left + root.right

这只是局部而言，整棵树就可以通过递归遍历后，比较这两个状态下的最大值取较大者。


```java
class Solution {
    public int rob(TreeNode root) {
        if (root == null) return 0;
        int hold = root.val;
        if (root.left != null) hold += rob(root.left.left) + rob(root.left.right);
        if (root.right != null) hold += rob(root.right.left) + rob(root.right.right);
        int unHold = rob(root.left) + rob(root.right);
        return Math.max(hold, unHold);
    }
}
```

但是使用先序遍历的话，从上往下会遍历，重复遍历较多，影响效率，可以使用后序遍历的方法减少重复的遍历进行优化：

后序遍历后的孩子结点可以取，也可以不取，需要使用一个数组存储这两种情况。

```java
        result[0] = Math.max(left[0], left[1]) + Math.max(right[0], right[1]);	// 结点不被取的情况下
        result[1] = left[0] + right[0] + root.val;	// 结点被取的情况下
```

```java
class Solution {
    public int rob(TreeNode root) {
        if(root == null) {
            return 0;
        }
        int[] result = postOrder(root);
        return Math.max(result[0], result[1]);
    }
    /*
    postOrder traversal to get children rob and notRob value
    int[2] int[0] not rob, int[1] rob
    rob = left_nr + right_nr + root.val
    notRob = max(left_r, left_nr) + max(right_r, right_nr)

    */
    private int[] postOrder(TreeNode root) {
        if(root == null) {
            return new int[2];
        }
        int[] left = postOrder(root.left);
        int[] right = postOrder(root.right);
        int[] result = new int[2];
        result[0] = Math.max(left[0], left[1]) + Math.max(right[0], right[1]);
        result[1] = left[0] + right[0] + root.val;
        return result;
    }
} 
```

# LeetCode  - 平衡二叉树 & 二叉搜索树相关题目

## 裁剪平衡二叉树

[669. Trim a Binary Search Tree (Easy)](https://leetcode.com/problems/trim-a-binary-search-tree/)

**思路：** 因为裁剪后，仍然要保持平衡二叉树的状态，并且与修剪之前状态一样，不能是重建的平衡二叉树，那么可以使用递归的思想解决这个问题，当一个结点 Root 在范围 L ~ R 时，它的左子树就是左子树修剪后的左子树，右子树就是右子树修剪后的右子树。当 Root.val > R ，这个时候，应该去裁剪 Root 的左子树，当 Root.val < L，这个时候，应该去裁剪 Root 的右子树。

```java
class Solution {
    public TreeNode trimBST(TreeNode root, int L, int R) {
        if (root == null) return null;
        if (root.val < L) return trimBST(root.right, L, R);
        if (root.val > R) return trimBST(root.left, L, R);
        root.left = trimBST(root.left, L, R);
        root.right = trimBST(root.right, L, R);
        return root;
    }
}
```

时空复杂度都是 O(N) （每个结点之后遍历一次）

## 寻找 BST 的第 k 小的数

[230. Kth Smallest Element in a BST (Middle)](https://leetcode.com/problems/kth-smallest-element-in-a-bst/)

由于中序遍历就可以得到 BST 的从小到大的一个递增序列。可以考虑进行中序遍历，需要设置一个计数器，当第三次遍历更结点的时候记录下来其值。

一般使用一个成员变量存储该值。

```java
 class Solution {
    int result;
    int count = 0;
    public int kthSmallest(TreeNode root, int k) {
        kth(root, k);
        return result;
    }

    public void kth(TreeNode root, int k) {
        if (root == null) return;
        if (root.left != null) kth(root.left, k);
        count++;
        if (count == k) {
            result = root.val;
            return;
        }
        if (root.right != null) kth(root.right, k);
    }
 }
```

> 是否还有值得优化的地方，能否在遍历到第 k 个结点的时候，终止递归。

## 把二叉查找树每个节点的值都加上比它大的所有节点的值

[538. Convert BST to Greater Tree  (Easy)](https://leetcode.com/problems/convert-bst-to-greater-tree/)


```
Input: The root of a Binary Search Tree like this:

              5
            /   \
           2     13

Output: The root of a Greater Tree like this:

             18
            /   \
          20     13

```

根据题意可见，由于二叉搜索树的性质，中序遍历后得到的是一个递增的序列，现在需要做的就是把

2、5、13 变成 20、18、13，可以看出，从后往前开始，13 是最大的，比 5 大的只有 13，所以 5 变成 18，比 2 大的只有 5、13，并且 5 已经变成了 18，2 只需要加上他的后一项就可以了。

很自然会想到按照后，中，前的顺序遍历整棵树，并且需要记录下，每次累加后的值，以便下次使用。

```java
class Solution {
    int sum = 0;
    public TreeNode convertBST(TreeNode root) {
        if (root == null) return null;
        convertBST(root.right);
        root.val += sum;
        sum = root.val;
        convertBST(root.left);
        return root;
    }
}
```

非递归解法：右子树必须先进入栈中

```java
class Solution {
    public TreeNode convertBST(TreeNode root) {
        int sum = 0;
        ArrayDeque<TreeNode> stack = new ArrayDeque<>();
        TreeNode node = root;
        while (!stack.isEmpty() || node != null) {
            
            while (node != null) {
                stack.push(node);
                node = node.right;
            }
            
            node = stack.pop();
            node.val += sum;
            sum = node.val;
            
            node = node.left;
        }
        return root;
    }
}
```

还有一种是空间为 O(1) 的解法：莫里斯遍历

[Morris Traversal方法遍历二叉树（非递归，不用栈，O(1)空间） - AnnieKim - 博客园](https://www.cnblogs.com/AnnieKim/archive/2013/06/15/morristraversal.html)

## 二叉查找树的最近公共祖先

[235. Lowest Common Ancestor of a Binary Search Tree (Easy)](https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-search-tree/)

要充分利用 BST 的特点，右 》 根 》 左，如果 p、q 结点都比根小，应该去左子树查找，都比根大，则应该去右子树查找，若介于两者之间，说明根就是最近公共祖先。

递归解法：O（N）时空

```java
class Solution {
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null) return null;
        if (p.val < root.val && q.val < root.val)
            return lowestCommonAncestor(root.left, p, q);
        else if (p.val > root.val && q.val > root.val)
            return lowestCommonAncestor(root.right, p, q);
        else 
            return root;
    }
}
```

遍历 BST：O（N）时间，O（1）空间

```java
class Solution {
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null) return null;
        TreeNode node = root;
        while (node != null) {
            if (p.val < node.val && q.val < node.val)
                node = node.left;
            else if (p.val > node.val && q.val > node.val)
                node = node.right;
            else
                break;           
        }
        return node;
    }
}
```

## 二叉树的最近公共祖先

[236. Lowest Common Ancestor of a Binary Tree (middle)](https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/description/)

这道题与上一道题有明显的不同，这不是一个 BST，是普通的二叉树。

可以使用深度优先的方法，设置标志位。

```
1 --> 2 --> 4 --> 8
BACKTRACK 8 --> 4
4 --> 9 (ONE NODE FOUND, return True)
BACKTRACK 9 --> 4 --> 2
2 --> 5 --> 10
BACKTRACK 10 --> 5
5 --> 11 (ANOTHER NODE FOUND, return True)
BACKTRACK 11 --> 5 --> 2
```

```java
class Solution {

    TreeNode result;
    
    private boolean recurseTree(TreeNode currentNode, TreeNode p, TreeNode q) {
        if (currentNode == null)
            return false;
        int left = recurseTree(currentNode.left, p, q)? 1 : 0;
        int right = recurseTree(currentNode.right, p , q)? 1 : 0;
        int mid = (currentNode == p || currentNode == q)? 1 : 0;
        if (left + right + mid >= 2)
            result = currentNode;
        return (left + right + mid > 0);
    }

    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        recurseTree(root, p, q);
        return result;
    }
}
```

还有一个暴力的做法是使用哈希表存储所有结点的父结点，最后取出来比较。

## 从有序数组中构造二叉查找树

[108. Convert Sorted Array to Binary Search Tree (Easy)](https://leetcode.com/problems/convert-sorted-array-to-binary-search-tree/description/)

从一个有序数组中构建平衡二叉树，那么，选谁做根了，根比左子树大，比右子树小，毫无疑问，应该是位于中间的元素，那么可以尝试使用**递归 + 二分法**，每次选择中间的元素作为根，其左子树就是中间元素左边递归的结果，右子树就是中间元素右边递归的结果。

```java
class Solution {
    public TreeNode sortedArrayToBST(int[] nums) {
        return sortedBST(nums, 0, nums.length - 1);
    }

    public TreeNode sortedBST(int[] nums, int start, int end) {
        if (start > end)
            return null;
        int mid = start + (end - start) / 2;
        TreeNode root = new TreeNode(nums[mid]);
        root.left = sortedBST(nums, start, mid - 1);
        root.right = sortedBST(nums, mid + 1, end);
        return root;
    }
}
```

> 考查递归、二分法（数组有序）

## 根据有序链表构造平衡的二叉查找树

[109. Convert Sorted List to Binary Search Tree (Middle)](https://leetcode.com/problems/convert-sorted-list-to-binary-search-tree/description/)

如果空间不限制的话，可以将链表转换为数组，那么就变成了与 108 题一样的解法了，但这不是这道题考查的方向，我们应该用链表的方式去解决。

**常规方法**：快慢指针找中间值，断开链表为前后两段，递归完成。

```java
class Solution {
    public TreeNode sortedListToBST(ListNode head) {
        if (head == null)
            return null;
        if (head.next == null) return new TreeNode(head.val);	// 不可缺少，否则栈溢出
        ListNode preMid = FindPreMid(head);
        ListNode mid = preMid.next;
        TreeNode root = new TreeNode(mid.val);
        preMid.next = null; // 断开链接，分成前后两半
        root.left = sortedListToBST(head);
        root.right = sortedListToBST(mid.next);
        return root;
    }

    public ListNode FindPreMid(ListNode head) {
        // 使用快慢指针的方式找到中间节点。
        ListNode fast = head;
        ListNode slow = head;
        ListNode pre = slow;
        while (fast != null && fast.next != null) {
            pre = slow;
            fast = fast.next.next;
            slow = slow.next;
        }
        return pre;
    }
}
```

中序遍历：难，因为 BST 中序遍历就是链表的顺序

```java

class Solution {
    private ListNode node;

    public TreeNode sortedListToBST(ListNode head) {
        if(head == null){
            return null;
        }

        int size = 0;
        ListNode runner = head;
        node = head;

        while(runner != null){
            runner = runner.next;
            size ++;
        }

        return inorderHelper(0, size - 1);
    }

    public TreeNode inorderHelper(int start, int end){
        if(start > end){
            return null;
        }

        int mid = start + (end - start) / 2;
        TreeNode left = inorderHelper(start, mid - 1);

        TreeNode treenode = new TreeNode(node.val);
        treenode.left = left;
        node = node.next;

        TreeNode right = inorderHelper(mid + 1, end);
        treenode.right = right;

        return treenode;
    }
}

```

## 在二叉查找树中寻找两个节点，使它们的和为一个给定值

[653. Two Sum IV - Input is a BST (Easy)](https://leetcode.com/problems/two-sum-iv-input-is-a-bst/submissions/)

这道题由于候选的两个节点可能在根结点的两侧，不建议使用 BST 的性质解决。

我的思路：中序遍历 + Hashset 去重（类似 two sum），但是递归有多余的重复

```java
class Solution {
    
    HashSet<Integer> set = new HashSet<>();
    
    public boolean findTarget(TreeNode root, int k) {
        if (root == null) return false;
        boolean left = findTarget(root.left, k);
        if (set.contains(k - root.val))
            return true;
        else if (!set.contains(root.val))
            set.add(root.val);
        boolean right = findTarget(root.right, k);
        return left || right;
    }
```

第二种思路：层次遍历，循环不递归

```java
class Solution {

    HashSet<Integer> set = new HashSet<>();

    public boolean findTarget(TreeNode root, int k) {
        if (root == null) return false;
        LinkedList<TreeNode> queue = new LinkedList<>();
        queue.addLast(root);
        while (!queue.isEmpty()) {
            TreeNode node = queue.pollFirst();
            if (set.contains(k - node.val))
                return true;
            if (!set.contains(node.val))
                set.add(node.val);
            if (node.left != null) queue.addLast(node.left);
            if (node.right != null) queue.addLast(node.right);
        }
        return false;
    }
}
```

第三种思路：较快，中序遍历得到一个递增的序列存储于数组中，使用双指针遍历该数组，首尾指针比较，只需遍历一半的数组。

```java
class Solution {
    
    ArrayList<Integer> arrayList = new ArrayList<>();
    
    public boolean findTarget(TreeNode root, int k) {
        if (root == null) return false;
        InOrder(root);  // 中序遍历，构建数组
        Integer[] nums = arrayList.toArray(new Integer[arrayList.size()]);
        int i = 0;
        int j = nums.length - 1;
        while (i < j) {
            int compare = nums[i] + nums[j];
            if (compare > k) j--;
            else if (compare < k) i++;
            else 
                return true;
        }
        return false;
    }

    public void InOrder(TreeNode root) {
        if (root == null) return;
        InOrder(root.left);
        arrayList.add(root.val);
        InOrder(root.right);
    }

}
```

## 在二叉查找树中查找两个节点之差的最小绝对值

[530. Minimum Absolute Difference in BST (Easy)](https://leetcode.com/problems/minimum-absolute-difference-in-bst/submissions/)

思路：中序遍历 BST 将得到一个递增的序列，在遍历的过程中记录下相邻的结点之间的绝对值之差即可，取最小值。

```java
class Solution {

    int min = Integer.MAX_VALUE;

    TreeNode pre = null;

    public int getMinimumDifference(TreeNode root) {
        InOrder(root);
        return min;
    }
    
    public void InOrder(TreeNode root) {
        if (root == null) return;
        InOrder(root.left);
        if (pre != null) min = Math.min(min, Math.abs(root.val - pre.val));
        pre = root;
        InOrder(root.right);
    }
}
```

## 寻找二叉查找树中出现次数最多的值

[501. Find Mode in Binary Search Tree (Easy)](https://leetcode.com/problems/find-mode-in-binary-search-tree/)

这道题与上一题类似，但是处理稍微复杂，需要记录中序遍历中记录出现相同次数的元素的最大值，不断的比较，找出最大的。！！！！！

```java
class Solution {
    private int curCnt = 1;
    private int maxCnt = 1;
    private TreeNode preNode = null;

    public int[] findMode(TreeNode root) {
        List<Integer> maxCntNums = new ArrayList<>();
        inOrder(root, maxCntNums);
        int[] ret = new int[maxCntNums.size()];
        int idx = 0;
        for (int num : maxCntNums) {
            ret[idx++] = num;
        }
        return ret;
    }

    private void inOrder(TreeNode node, List<Integer> nums) {
        if (node == null) return;
        inOrder(node.left, nums);
        if (preNode != null) {
            if (preNode.val == node.val) curCnt++;
            else curCnt = 1;
        }
        if (curCnt > maxCnt) {
            maxCnt = curCnt;
            nums.clear();
            nums.add(node.val);
        } else if (curCnt == maxCnt) {
            nums.add(node.val);
        }
        preNode = node;
        inOrder(node.right, nums);
    }
}
```
