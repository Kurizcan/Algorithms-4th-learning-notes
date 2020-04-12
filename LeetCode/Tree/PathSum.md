# Path Sum I

```go
/**
 * Definition for a binary tree node.
 * type TreeNode struct {
 *     Val int
 *     Left *TreeNode
 *     Right *TreeNode
 * }
 */
func hasPathSum(root *TreeNode, sum int) bool {
  if root == nil {
    return false
  }  
  if root.Val == sum && isLeaf(root) {
    return true
  }
  target := sum - root.Val
  return hasPathSum(root.Left, target) || hasPathSum(root.Right, target)
}

func isLeaf(node *TreeNode) bool {
  return node.Left == nil && node.Right == nil
}
```

# 113. Path Sum II

```java
/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode(int x) { val = x; }
 * }
 */
class Solution {

  List<List<Integer>> res = new LinkedList<>();

  public List<List<Integer>> pathSum(TreeNode root, int sum) {
    if (root == null) {
      return res;
    }
    dfs(root, sum, 0, new LinkedList<>());
    return res;
  }
  
  public void dfs(TreeNode root, int sum, int cur, LinkedList<Integer> list) {
    if (root == null) {
      return;
    }
    list.addLast(root.val);
    cur += root.val;
    if (root.left == null && root.right == null && cur == sum) {
      res.add(new LinkedList<Integer>(list));
    } else {
      dfs(root.left, sum, cur, list);
      dfs(root.right, sum, cur, list); 
    }
    list.removeLast();
    cur -= root.val;
  }
  
}
```