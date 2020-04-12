# 图

以下是有关无向图的需要掌握的内容。

> 不涉及自环（某顶点自己连接自己）与平行边的情形（某两个顶点之间有多条直接相连的边）

## 相关及延伸术语

- 连通图：任意一个顶点都存在一条路径到达另一个任意顶点
- 树：无环连通的典型例子
- 顶点的度数：与该顶点相连接的边的数量或者邻接点的数量
- 森林：互不相连的树组成的集合

## 图的表示

- 邻接表
- 邻接矩阵
- 边的数组

### 邻接矩阵

假设有 V 个顶点、 E 条边，那么我们可以使用 V × V 的布尔矩阵（Graph[][]）进行图的结构的存储。当顶点 V 与顶点 E 之间有一条边的时候，则设置
Graph[V][E] = true（无向边的情况下 Graph[E][V]] = Graph[V][E]），否则 Graph[E][V]] = false。

当顶点的数据量非常大的时候，边还十分少的情况下，这种存储结构在空间上的开销将非常大。

相关操作后续补上！！！

### 邻接表

![](https://lcanmelo.me/upload/upload/2019/03/ssgnmr3cs2g81r0tqab3uoaq2e.png)

利用数组的索引或者其他方式的映射至顶点，数组中存储的是一个链表的数据结构，该链表存储了该顶点所有的邻接顶点，类似于散列表中的拉链表实现。

使用这种方式所需空间与 E + V 成正比，添加边的操作是常数级别的。

邻接表非常适用与**稀疏图**的场景

```java
public class Graph {
    private int V;      // 顶点数目
    private int E;      // 边的数目
    private LinkedList<Integer>[] adj; // 存放邻接表的链表数组

    public Graph(int V) {
        this.V = V;
        this.E = 0;
        adj = (LinkedList<Integer>[]) new LinkedList[V];    // 存放连接表的数组初始化
        for (int v = 0; v < V; v++) {
            adj[v] = new LinkedList<Integer>();
        }
    }

    public Graph(Scanner scanner) {
        this(scanner.nextInt());
        int E = scanner.nextInt();
        for (int i = 0; i < E; i++) {
            // 添加一条边
            int v = scanner.nextInt();
            int w = scanner.nextInt();
            addEdge(v, w);              // 读取两个顶点，相连接
        }
    }

    public int getE() {
        return E;
    }

    public int getV() {
        return V;
    }

    public void addEdge(int v, int w) {
        adj[v].add(w);
        adj[w].add(v);
        E++;
    }

    public void deleteEdge(int v, int w) {
        Iterator<Integer> listV = adj(v);
        Iterator<Integer> listW = adj(w);
        while (listV.hasNext()) {
            Integer temp = listV.next();
            if (temp.equals(w)) {
                listV.remove();
                break;
            }
        }
        while (listW.hasNext()) {
            Integer temp = listW.next();
            if (temp.equals(v)) {
                listW.remove();
                break;
            }
        }
        E--;
    }

    public Iterator<Integer> adj(int v) {
        return adj[v].iterator();
    }

    /**
     * Graph 的字符串表示
     * @return
     */
    public String toString() {
        String s = V + " vertices " + E + " edges\n";
        for (int v = 0; v < V; v++) {
            s += v + ": ";
            for (int w : this.adj[v])
                s += w + " ";
            s += "\n";
        }
        return s;
    }

    public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);
        Graph graph = new Graph(scanner);
        System.out.println(graph.toString());
       // graph.deleteEdge(7, 8);
       // System.out.println(graph.toString());
    }
}
```

## 两种经典的搜索遍历方式

- 深度优先搜索
- 广度优先搜索

### 深度优先搜索

深度遍历，即访问顶点的邻接顶点之后，继续访问该邻接顶点的邻接顶点，直到遍历完所有的顶点。

以下是递归实现：

```java
class DFS {
    private boolean[] marked;     // 标记是否访问过
    private int count;

    public DFS(Graph graph, int s) {
        marked = new boolean[graph.getE()];
        dfs(graph, s);                // 进行递归处理，深度遍历
    }

    private void dfs(Graph G, int v) {
        marked[v] = true;
        count++;
        for (Integer w : G.adj(v)) {
            if (!marked[w])
                dfs(G, w);
        }
    }

    public boolean isMarked(int w) {
        return marked[w];
    }

    public int getCount() {
        return count;
    }
}
```

非递归实现：续上!!!

#### 可以解决的问题

- 判断连通图
- 找出连通子图的数目
- 二分图应用
- 无环图形

**性质一**：在深度优先搜索中，每条边都会被访问两次，并且第二次访问时，总会发现这个顶点已经被标记过了，因此，其搜索有轨迹往往会比边的数目多一倍。

利用这个两次遍历同一条边的特性，我们可以解决许多相关的问题。

##### 判断连通图

利用上边的递归实现，得到的 count 与总的顶点数 V 相比较，如果是连通的，则所有的顶点都应该被访问过，即 count = V

##### 找出连通子图的数目

如果一个子图是连通图，那么在遍历他们的时候，他们属于一次完整的递归，那么有多少个连通子图即有多少个集合，对每个不同的集合的深度搜索都
是一个平行且完整的递归过程，那么我们可以在递归后设置一个数组 id[] 记录他们属于哪一个集合即可。

```java
class CC {
    private boolean[] marked;
    public int[] id;
    public int count;

    public CC(Graph graph) {
        marked = new boolean[graph.getV()];
        id = new int[graph.getV()];
        for (int i = 0; i < graph.getV(); i++) {
            if (!marked[i]) {
                dfs(graph, i);
                count++;           // 完成一次递归后总集合数增加
            }
        }
    }

    public void dfs(Graph graph, int v) {
        marked[v] = true;
        id[v] = count;              // V 顶点的所属集合
        for (int w : graph.adj(v))
            if (!marked[w])
                dfs(graph, w);
    }
}
```

##### 判断无环图

最好进行画图跟踪

```java
public void dfs(Graph graph, int v, int u) {
    marked[v] = true;
    for (int w : graph.adj(v))
        if (!marked[w])
            dfs(graph, w, v);				
        else if (w != u) hasCycle = true;   // 当全部遍历完成后，进行第二次边的遍历的时候 
}
```

##### 二分图应用

什么是二分图？

![](https://zh.wikipedia.org/wiki/%E4%BA%8C%E5%88%86%E5%9B%BE)

比如涂色问题，使用两种颜色将所有顶点着色，并且任意一条边的两个顶点的颜色不同。

同样是性质一的使用，我们在第一次经历完所有的结点，涂完颜色后，再进行第二次该边的遍历的时候，如果发现该边顶点同色，则非二分图

```java
public TwoColor(Graph graph) {
    marked = new boolean[graph.getV()];
    color = new boolean[graph.getV()];
    for (int i = 0; i < graph.getV(); i++) {
        if (!marked[i]) {
            dfs(graph, i);
        }
    }
}

public void dfs(Graph graph, int v) {
    marked[v] = true;
    for (int w : graph.adj(v))
        if (!marked[w]) {
        	color[w] = !color[v];		// 上色，相邻的结点颜色不同
            dfs(graph, w);
        }
        else if (color[w] == color[v])  // 访问过的顶点，检查已经上完色的结点是否出现了与邻接结点相同的颜色
        	isTwoColorable = false;
}
```

### 广度优先搜索

即从顶点开始，先将其所有的邻接点访问完毕，再进行访问该顶点的邻接点的所有的邻接点，直到所有的顶点全部被遍历。

在深度优先搜素中我们使用了递归的方式，实际上涉及到了栈的使用，在广度优先搜索中，我们需要使用队列的结构。

每访问一个顶点，将其所有未访问过的邻接顶点入列，之后出列一个顶点，继续重复的操作，直到队列为空。



#### 可以解决的问题

##### 最短路径

由于每次都是距离顶点一定顺序的邻接顶点进行队列中，那么从顶点 V 进入队列到离开，不可能有比到 V 距离更近的路径。

```java
class BFS {
    private boolean[] marked;
    private int[] edgeTo;              // 从起点到已知路径上的最后一个顶点
    private int start;                 // 起点

    public BFS(Graph graph, int s) {
        marked = new boolean[graph.getV()];
        edgeTo = new int[graph.getV()];
        this.start = s;
        bfs(graph, start);
    }

    private void bfs(Graph graph, int start) {
        Queue<Integer> queue = new ArrayDeque<>();
        marked[start] = true;           // 标记结点，入列
        queue.offer(start);
        while (!queue.isEmpty()) {
            int v = queue.poll();       // 从队列中删除一个顶点，进行遍历该结点的邻接顶点
            for (int w : graph.adj(v)) {
                if (!marked[w]) {
                    edgeTo[w] = v;      // 保存最短路劲的左后一条边上的最后一个顶点
                    marked[w] = true;
                    queue.offer(w);     // 入列
                }
            }
        }
    }

    public boolean hasPathTo(int v) {
        return marked[v];
    }

    public Iterable<Integer> pathTo(int v) {
        if (!hasPathTo(v)) return null;
        Stack<Integer> path = new Stack<>();
        for (int x = v; x != start; x = edgeTo[x])
            path.push(x);
        path.push(start);
        return path;
    }
}
```

> 在使用图来解决问题的时候，需要考虑构造图的模型的开销是否值得，是否有不需要构造图的模型就可以解决的其他的方法

参考：

- [二分图](https://zh.wikipedia.org/wiki/%E4%BA%8C%E5%88%86%E5%9B%BE)
- [算法（第四版）](https://algs4.cs.princeton.edu/41graph/)













