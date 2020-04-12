# 最小的 K 个数

输入整数数组 arr ，找出其中最小的 k 个数。例如，输入4、5、1、6、2、7、3、8这8个数字，则最小的4个数字是1、2、3、4。

- 堆
```java
class Solution {
  
  public int[] getLeastNumbers(int[] arr, int k) {
    if (arr == null || arr.length == 0 || k <= 0) {
      return new int[0];
    }
    PriorityQueue<Integer> queue = new PriorityQueue<>((o1, o2) -> o2 - o1);
    for (int item : arr) {
      if (queue.size() < k) {
        queue.add(item);
      } else if (queue.peek() > item) {
        queue.remove();
        queue.add(item);
      }
    }
    int[] res = new int[queue.size()];
    int i = 0;
    while (!queue.isEmpty()) {
      res[i++] = queue.remove();
    }
    return res;
  }
}
```

- 快速排序切割法

```java
class Solution {
  
  public int[] getLeastNumbers(int[] arr, int k) {
    if (arr == null || arr.length == 0 || k <= 0) {
      return new int[0];
    }
    if (k == arr.length) {
      return arr;
    }
    int left = 0;
    int right = arr.length - 1;
    int p = partition(arr, left, right);
    while (p != k) {
      if (p > k) {
        right = p - 1;
      } else {
        left = p + 1;
      }
      p = partition(arr, left, right);
    }
    int[] res = new int[k];
    for (int i = 0; i < k; i++) {
        res[i] = arr[i];     
    }
    return res;
  }
  
  // 快速排序分割经典模板
  private int partition(int[] nums, int lo, int hi) {
    int v = nums[lo];
    int i = lo, j = hi + 1;
    while (true) {
      while (++i <= hi && nums[i] < v);
      while (--j >= lo && nums[j] > v);
      if (i >= j) {
        break;
      }
      int t = nums[j];
      nums[j] = nums[i];
      nums[i] = t;
    }
    nums[lo] = nums[j];
    nums[j] = v;
    return j;
  }
}
```