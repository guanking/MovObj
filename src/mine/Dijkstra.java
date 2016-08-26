package mine;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Scanner;

import test.YenTopKShortestPathsAlgTest;

public class Dijkstra {
	public HashMap<Integer, Node> nodes = new HashMap<Integer, Node>();
	/**
	 * 开始点和终点
	 */
	public int beginIndex, endIndex;

	/**
	 * 构造函数
	 * 
	 * @param fileName
	 *            文件名（有向图）
	 * @throws FileNotFoundException
	 */
	public Dijkstra(String fileName) throws FileNotFoundException {
		Scanner sc = new Scanner(new File(FileOperator.DATA_DIR, fileName));
		int id1, id2;
		double dis;
		Node node1, node2;
		while (sc.hasNextInt()) {
			id1 = sc.nextInt();
			id2 = sc.nextInt();
			dis = sc.nextDouble();
			if (!this.nodes.containsKey(id1)) {
				node1 = new Node(id1);
				this.nodes.put(id1, node1);
			} else {
				node1 = this.nodes.get(id1);
			}
			if (!this.nodes.containsKey(id2)) {
				node2 = new Node(id2);
				this.nodes.put(id2, node2);
			} else {
				node2 = this.nodes.get(id2);
			}
			node1.nexts.add(node2);
			node1.nextDis.add(dis);
		}
	}

	/**
	 * 运行最短路
	 * 
	 * @param beginIndex
	 *            开始节点的id
	 * @param endIndex
	 *            终止节点的id
	 * @return 
	 */
	public boolean exe(int beginIndex, int endIndex) {
		this.beginIndex = beginIndex;
		this.endIndex = endIndex;
		Node begin = this.nodes.get(this.beginIndex), end = this.nodes
				.get(this.endIndex);
		Node node = null;
		begin.disFromSource = 0;
		MinHeap<Node> que = new MinHeap<>(this.nodes.size());
		begin.vis = true;
		que.push(begin);
		int index;
		while (!que.empty()) {
			node = que.fron();
			if (node == end) {
				System.out.println("fine!");
				return true;
			}
			// System.out.println(node);
			// node.vis=false;
			index = -1;
			for (Node ele : node.nexts) {
				index++;
				if (ele.disFromSource > node.disFromSource
						+ node.nextDis.get(index)) {
					ele.disFromSource = node.disFromSource
							+ node.nextDis.get(index);
					ele.parent = node;
					if (ele.vis) {
						que.update(ele);
					} else {
						ele.vis = true;
						que.push(ele);
					}
				}
			}
		}
		return false;
	}

	/**
	 * 最短路算法之后根据终止节点的parent获取路径并输出
	 */
	public void showPath() {
		Node n = this.nodes.get(this.endIndex);
		Node endNode=n;
		LinkedList<Node> tns = new LinkedList<Node>();
		while (n != null) {
			tns.addFirst(n);
			n = n.parent;
		}
		boolean first = true;
		for (Node ele : tns) {
			if (first) {
				System.out.print("[" + ele.id);
				first = false;
			} else {
				System.out.print("," + ele.id);
			}
		}
		System.out.println("]"+endNode.disFromSource);
	}

	/**
	 * 获取最大id，用于初始化Yen算法中的节点个数
	 * 
	 * @return 当前图中的最大ID
	 */
	public int getMaxID() {
		int id = -1;
		for (Node ele : this.nodes.values()) {
			id = id > ele.id ? id : ele.id;
		}
		return id;
	}

	public Hashtable<Integer, Integer> getIDsMap() {
		Hashtable<Integer, Integer> map = new Hashtable<Integer, Integer>();
		int index = 0;
		for (Node ele : this.nodes.values()) {
			if (!map.contains(ele.id)) {
				map.put(ele.id, index++);
			}
		}
		return map;
	}

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		long begin,end;
		Dijkstra dijk = new Dijkstra("bsDetails.txt");
		System.out.println(dijk.getMaxID());
		begin=System.currentTimeMillis();
		dijk.exe(271, 74);
		end=System.currentTimeMillis();
		begin=end-begin;
		System.out.println("Our Djkstra's time : "+begin+"ms");
		dijk.showPath();
		YenTopKShortestPathsAlgTest test=new YenTopKShortestPathsAlgTest();
		test.testDijkstraShortestPathAlg();
		// for(int i=0;i<10;i++)
		// System.out.println((i*100)<=Double.MAX_VALUE);
	}

}
