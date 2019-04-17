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
