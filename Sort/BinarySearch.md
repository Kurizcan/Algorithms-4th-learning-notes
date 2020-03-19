# 二分查找

## 使用范围

二分法一般使用在有序数组当中的查找。

## 查找方式

先比较数组中的中间位置的值，如果待比较值比该值小，则在左子数组中继续查找，反之，在右子数组中继续查找。

按照这个规则不断的查找直到能判定是否能找到待查找值。

> 由于每次比较后，都会将比较的范围缩小一半，大大的减少了常规顺序查询的比较次数。

## 实现

### 递归实现

```java
public int BinarySearch(int key, int left, int right, int[] a) {
      if (left > right) return -1;
      int mid = left + (right - left) / 2;
      if (key < a[mid]) 
        return BinarySearch(key, left, mid - 1, a);
      else if (key > a[mid]) 
        return BinarySearch(key, mid + 1, right, a);
      else
        return mid;
}
```

### 非递归实现
```java
public int BinarySearch(int key, int left, int right, int[] a) {
      while(left <= right) {
        int mid = left + (right - left) / 2;
        if (key < a[mid]) 
          right = mid - 1;
        else if(key > a[mid])
          left = mid + 1;
        else 
          return mid;
      }	
      return -1;
}
```
## 性能分析

由递归实现，可以确定比较次数的上限。

在含有 N 个元素的有序数组中进行二分查找最多需要比较 lgN + 1 次。

二分查找的效率可以保持在对数级别（lgN）。
