# 六大排序算法

## 选择排序

### 思路与分析

#### 思路

以按照从小到大排序的方式为例，每一轮确定一个最小的元素并与其该存在的位置的元素进行交换，以后此轮最小的元素将不再被访问。

因此需要进行 N 次交换，确定 N 个位置。

#### 分析

对于长度为 N 的数组，选择排序大约需要 N^2 次的比较以及 N 次交换

### 实现

```java
	public static void Select_sort(Comparable[] a) {
		for (int i = 0; i < a.length; i++) {
			int min = i;	// 最小元素索引
			for (int j = i + 1; j < a.length; j++) {
				if (a[j].CompareTo(a[min]) < 0) {
					min = j;
				}
			}
			int temp = a[i];
			a[i] = a[min];
			a[min] = temp;
		}
	}

```

缺点：当所有的元素都相等，甚至是已经有序的部分都要进行与元素随机排列时一样多次的比较。


## 插入排序（稳定）

### 思路与分析

#### 思路

相对于选择排序的进行了一定的改进，每一轮将可以确保这一轮起始位置的前面的元素是有序的，只需要将该轮的元素插入到此前排序好的
序列中合适位置即可。

#### 分析

平均情况下：比较次数（N^2 / 4） 交换 （N^2 / 4）

最好情况下：完全或者接近有序 比较次数（N - 1） 交换 （0）

最坏情况下：完全逆序 比较次数（N^2 / 2） 交换 （N^2 / 2）

> 插入排序的交换操作与数组中倒置的数量相同。（每一次交换都会调整一组倒置的元素的位置）
> 倒置数量 <= 需要比较的次数 <= 倒置数量 + 数组大小 - 1

### 实现

```java
public static void Insert_sort(Comparable[] a) {
	for (int i = 1; i < a.length; i++) {
		for (int j = i; j > 0; j--) {
			// 将 a[i] 插入到已经排好序的 a[0]....a[i - 1] 中
			if (a[j].CompareTo(a[j -1]) < 0) {
				Comparable temp = a[j - 1];
				a[j - 1] = a[j];
				a[j] = temp;
			}
			else
				break;
		}
	}
}
```

### 适合的场景

- 数组中每个元素距离它的最终位置不远
- 一个有序数组接一个小数组
- 数组中只有几个元素的位置不正确

### 如何提高效率

在内循环中将较大的元素都向右移动而不总是交换两个元素，使得访问数组的次数减半

```java
public static void sort(Comparable[] a){
    int n = a.length;
    for(int i = 1; i < n; i ++){
        Comparable temp = arr[i];
        int j;
        for(j = i; j > 0 && arr[j-1].compareTo(temp) > 0; j --){
            a[j] = a[j-1];
        }
        a[j] = temp;
    }
}
```

可以考虑查找的时候使用二分查找提高效率

```java
public static void Insert_sort(Comparable[] a) {
    int N = a.length;
    for(int i = 1; i < N; i++){
        int left = 0;
        int right = i - 1;
        Comparable temp = a[i];
        int j;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (temp.CompareTo(a[mid]) < 0)
                right = mid - 1;
            else
                left = mid + 1;
        }		// 二分查找应该插入的位置
        // 最佳位置往后移
        for (j = i - 1; j >= left; j--) {
            a[j + 1] = a[j];
        }
        a[j + 1] = temp;
    }
}


```

> 插入排序不会移动比被插入元素更小的元素，其所需的平均比较次数只有选择排序的一半

## 希尔排序

### 思路与分析

在插入排序的基础上进了该进，对于乱序的数组，由于只会交换相邻的元素，插入排序的效果并不好，希尔排序选择了跨越度更大的插入方法，
以相隔 h 个数组的元素有序为一组进行跨越的插入。

比一般的选择排序、插入排序要快，但是如何选择递增的间隔 h 的序列是个挑战

### 实现

```java
public static void Shell_sort(Comparable[] a){
    int n = a.length;
    int h = 1;
    while (h < n / 3) h = 3*h + 1;          // 1, 4, 13,.......
    while (h >= 1) {
        for(int i = h; i < n; i ++){
            Comparable temp = a[i];
            int j;
            for( j = i ; j >= h && temp.CompareTo(a[j - h]) < 0; j = j - h){
                a[j] = a[j - h];
            }
            a[j] = temp;
        }
        h = h / 3;
    }
}
```

