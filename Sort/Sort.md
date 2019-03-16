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
public static void insert_sort(Comparable[] a) {
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
public static void insert_sort(Comparable[] a) {
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
public static void shell_sort(Comparable[] a){
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

## 归并排序

### 思路与分析

使用分冶的思想，将两个有序的数组归并为一个更大的有序数组，可以使用递归的自顶向下的方式实现，也可以使用非递归的自底向上的方式实现。

对于任意长度为 N 的数组排序所需的时间复杂度： O(NlogN) 级别，空间复杂度：O（N）

> 基于比较的排序算法可以展开成类似二叉树的形式分析，对于层数为 N 的比较树，至少有 N！ 个叶子结点，最多有 2^N 个叶子结点，比较的次
数介于 lgN! ~ NlgN

### 实现

关键分成两部分：部分排序分割，归并

#### 归并（稳定）

```java
public static void merge(Comparable[] a, int left, int mid, int right) {
	// 将 a[left....mid] 与 a[mid + 1....right] 归并
	int i = left;
	int j  = mid + 1;
	int[] aux = new int[a.length]; 		 	// 辅助数组
	for (int k = left; k <= right; k++) {
		aux[k] = a[k];
	}
	for (int k = left; k <= right; k++) {	// 利用辅助数组将原数组进行归并
		if(i > mid) 			a[k] = aux[j++];
		else if (j > right)  	a[k] = aux[i++];
		else if (aux[j].CompareTo(aux[i]) < 0) 
			a[k] = aux[j++];
		else
			a[k] = aux[i++];
	}
}

```

#### 分割

##### 自顶向下（递归）

```java
public static void merge_sort(CompareTo[] a) {
	merge_sort(a, 0, a.length - 1);
}

public static void merge_sort(CompareTo[] a, int left, int right) {
	// 将 a[left...right] 排序
	if(left >= right) return;
	int mid = left + (right - left) / 2;
	sort(a, left, mid);				// 对左半部分排序
	sort(a, mid + 1, right);		// 对右半部分排序
	if (a[mid] < a[mid + 1])
		return;						// 判断是否已经有序
	else 
		merge(a, left, mid, right); // 归并左右部分
}
```

1. 对于长度为 N 的任意数组，自顶向下的归并排序需要 1/2NlgN - NlgN 次比较
2. 对于长度为 N 的任意数组，自顶向下的归并排序最多需要访问数组 6NlgN 次（2N 次用来复制、2N 次用来将排好序的元素移动回来、另外最多比较 2N 次）

##### 自底向上

```java
public static void merge_sort_BU(Comparable[] a) {
	int n = a.length;
	for(int size = 1; size < n; size = size + size) {		// size 指子数组的大小
		for(int left = 0; left < n - size; left += size + size)
			merge(a, left, left + size - 1, Math.min(left + size + size - 1, n - 1));
	}
}
```

> 自底向上的归并比较适合用链表组织的数据

### 改进

1. 当数组长度小到一定规模的时候，插入排序或者选择排序的速度可能会比归并排序的更快，我们可以给予一个阙值，当数组长度小到阙值时，就是用插入排序来解决小规模子数组的排序问题。
2. 判断数组已经有序：左半边的最大值小于或等于右半边的最小值，则跳过归并，此时任意有序的子数组算法的运行时间可以达到线性级别。
3. 为了节省将元素复制到辅助数组作用的时间，可在递归调用的每个层次交换原始数组与辅助数组的角色。

```java 
public static void merge(Comparable[] a, int left, int mid, int right) {
	for (int k = left; k <= mid; k++) {
	    aux[k] = a[k];
	}
	for (int k = mid + 1;k <= right; k++) {
	    aux[k] = a[hi - k + mid + 1];
	}
	int i = left, j = right;      //从两端往中间
	for (int k = left; k <= right; k++)
	    if (aux[i] <= aux[j]) a[k] = aux[i++];
	    else a[k] = aux[j--];
}
```

[参考文章](https://blog.csdn.net/tinyDolphin/article/details/78457343)

> 对于存在大量重复元素的数组排序，归并排序无法保证其性能

## 快速排序

### 思路与分析

快速排序也是一种使用了分冶思想的算法，将一个数组分成两个子数组与一个介于两个子数组之间的确定位置的元素，
	
	子数组1 < 确定位置的元素 < 子数组2

当这两个子数组有序时，整个数组也就自然有序了

> 与同样使用了分冶思想的归并排序不同的是，快速排序的递归调用发生在处理整个数组（切分）之后。

### 分析

时间复杂度：O(NlgN) 

空间复杂度：O(lgN) 使用了递归

> 当数组非随机顺序排列时，快速排序的性能有可能大幅度降低为 O(N^2) 级别，因此，在使用快速排序之前最好打乱数组顺序。

### 实现

关键在于切分与切分之后的处理

切分的结果是：

- 对于某个 j，a[j] 已经确定
- a[left ... j - 1] 中的元素都小于 a[j]
- a[j + 1 ... right] 中的元素都大于 a[j]

```java
public static void quick_sort(Comparable[] a) {

		// 随机打乱数组顺序

		quick_sort(a, 0, a.length - 1);
}

public static void quick_sort(Comparable[] a, int left, int right) {
	if(right <= left) return;
	int j = partition(a, left, right);		// 切分数组
	quick_sort(a, left, j - 1);				// 递归排序 a[left ... j - 1]
	quick_sort(a, j + 1, right);			// 递归排序 a[j + 1 ... right]
}
```

切分部分：

```java
private static int partition(Comparable[] a, int left, int right) {
	int value = a[left];
    int i = left;
    int j = right + 1;
    while(true) {
        while(a[++i].CompareTo(value) < 0) if(i == right) break;
        while(a[--j].CompareTo(value) > 0) if(j == left) break;
        if(i >= j) break;
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }
    a[left] = a[j];
    a[j] = value;
    return j;
}
```


### 改进

1. 对于小数组，快速排序比插入排序慢，可以设置一个阀值，采用不同的排序方法（针对递归的排序算法，都可以这么改进）
2. 对于存在大量重复元素的数组排序，可以考虑使用**三取样切分**    

三取样方法可以应对存在大量重复数组的排序问题上，甚至能优化达到 O(N) 级别的时间复杂度，关键在于切分成一个这样的序列：

a[left ... lt - 1] < v = a[lt ... gt] < a[gt + 1 .. right]

```java
public static void quick_sort(Comparable[] a, int left, int right) {
	if(left >= right) return;
	int lt = left;
	int i = left + 1;
	int gt = right;
	int value = a[left];
	while(i <= gt) {
		int cmp = a[i].CompareTo(value);
		if(cmp < 0) {
			Comparable temp = a[i];
			a[i] = a[lt];
			a[lt] = temp;
			i++;
			lt++;
		}
		else if(cmp > 0) {
			Comparable temp = a[i];
			a[i] = a[gt];
			a[gt] = temp;
			gt--;			
		}
		else
			i++;
	}
	quick_sort(a, left, lt - 1);
	quick_sort(a, gt + 1, right);
}
```

> 归并排序与希尔排序一般都比快速排序慢的原因是，在内循环中移动数据
> 快速排序的速度优势在于它的比较次数很少，效率的关键更多取决于切分数组的效果。 

## 堆排序

基于优先队列实现的堆排序。

### 堆的定义

当一颗二叉树的每个结点都大于等于它的两个子结点时，称为堆有序

根结点是堆有序的二叉树中最大（最小）的结点

可以使用数组按层存储，**一般不使用数组的第一个位置**

使用数组（堆）实现的完全二叉树可以实现优先队列**对数级别**的插入与删除

> 一颗大小为 N 的完全二叉树的高度 lgN（取小）
> 完全二叉树中位置 k 的父节点：k / 2（取小）、子结点：2k、 2k + 1

### 优缺点

优点：可以保证对数级别的时间复杂度，适用于需要快速获取最值的情形。

缺点：数组元素很少和相邻的其他元素进行比较，若利用缓存，缓存被命中的概率十分低。

### 优先队列

有关优先队列的普通实现方式，请参考：

- [无序数组](https://algs4.cs.princeton.edu/24pq/UnorderedArrayMaxPQ.java.html)

- [有序数组](https://algs4.cs.princeton.edu/24pq/OrderedArrayMaxPQ.java.html)

- 链表表示（类似数组的操作，在插入或者弹出做修改）

## 实现

以升序排列为例，我们的操作是，构造最大堆，每次将首位的元素与最后的第 i 个元素交换，并重新对前 n - i 个元素进行结构的恢复

堆排序的实现主要有两个过**程堆的构造**与**恢复**

```java
package Sort;

public class MaxPQ <Key extends Comparable<Key>>{
    private Key[] pq;
    private int n;

    public MaxPQ(int capacity) {
        pq = (Key[]) (new Comparable[capacity + 1]);    // 元素存储在 a[1....n] 中
        n = 0;
    }

    public MaxPQ(Key[] a) {
        pq = a;
        n = a.length - 1;
    }

    public boolean isEmpty() {
        return n == 0;
    }

    public int size() {return n;}

    public void insert(Key key) {
        pq[++n] = key;
        swim(1, n);            // 插入一个元素在末尾，需要上浮维护二叉堆的结构
    }

    public Key delMax() {
        Key max = pq[1];      // 根结点几是最大的元素
        exch(1, n--);         // 将最大的元素与末尾元素交换，并缩小树的范围
        pq[n + 1] = null;     // 防止对象游离
        sink(1, n);           // 恢复堆的结构，因为最小的元素于最顶端，所以需要下沉维护堆的结构
        return max;
    }

    public void exch(int i, int j) {
        Key temp = pq[i];
        pq[i] = pq[j];
        pq[j] = temp;
    }

    public void swim(int k, int n) {
        while (k > 1 && pq[k / 2].compareTo(pq[k]) < 0) {
            exch(k / 2, k);
            k = k / 2;
        }
    }

    public void sink(int k, int n) {
        while (2 * k < n) {
            int j = 2 * k;
            if (j + 1 < n && pq[j].compareTo(pq[j + 1]) < 0) j++;
            if (pq[j].compareTo(pq[k]) < 0) break;
            exch(j, k);
            k = j;
        }
    }

    public void sort() {
        for (int k = n / 2; k >= 1; k--)
            sink(k, n);          // 堆的构造
        while (n > 1) {
            exch(1, n--);      // 与第一个元素交换
            sink(1, n);        // 恢复堆的结构
        }
    }

    public static void main(String args[]) {
        Integer[] a = {null, 5, 8, 6, 3, 5, 10, 9};
        MaxPQ<Integer> maxPQ = new MaxPQ<Integer>(a);
        maxPQ.sort();
        for (Integer number: a) {
            System.out.print(number + " ");
        }
    }
}
```

## 总结

稳定性：指排序算法可以保留数组中重复元素的相对位置

Java 系统的库函数 Arrays.sort():

- 实现了 Comparable 接口的数据类型的排序方法
- 实现了比较器 Comparator 接口的数据类型的排序方法
- 对于原始数据类型使用（三向切分）快速排序
- 对于引用类型使用归并排序

常考笔试题类型：

- 寻找重复元素
- 两个数组的逆序对数（插入排序或其他）
- 寻找中位数或者第 k 个数据 (快速排序思想，可达到线性级别)
-  

