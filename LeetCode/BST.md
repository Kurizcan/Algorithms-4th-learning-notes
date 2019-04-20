# LeetCode  - 平衡二叉树相关题目

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














