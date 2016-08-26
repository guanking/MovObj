package mine;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

public class Node implements Comparable<Node> {
	/**
	 * 自行车站台状态数目
	 */
	public static final int STATUS_NUM = 180;
	Integer id;
	/**
	 * 到终点的距离
	 */
	double disFromTarget;
	/**
	 * 到原点的距离
	 */
	double disFromSource;
	/**
	 * 坐标
	 */
	double x, y;
	/**
	 * 搜索树中用于记录父节点
	 */
	Node parent = null;
	/**
	 * 是否被访问过
	 */
	boolean vis = false;
	/**
	 * 下一个节点，及距离
	 */
	LinkedList<Node> nexts = new LinkedList<Node>();
	LinkedList<Double> nextDis = new LinkedList<Double>();
	/**
	 * 上一个节点，及距离
	 */
	LinkedList<Node> pres = new LinkedList<Node>();
	LinkedList<Double> presDis = new LinkedList<Double>();
	/**
	 * 各个时间点状态的状态<br>
	 * true:可以换乘<br>
	 * false:不可以换乘<br>
	 */
	boolean[] status = new boolean[Node.STATUS_NUM];

	public Node() {
		// TODO Auto-generated constructor stub
		this.disFromTarget = Math.random() * 100;
	}

	public Node(int id) {
		this.id = id;
		this.disFromSource = Double.MAX_VALUE;
	}

	@Override
	public int compareTo(Node o) {
		// TODO Auto-generated method stub
		return this.disFromSource < o.disFromSource ? -1 : 1;// 最短路使用
		// return this.disFromTarget < o.disFromTarget ? -1 : 1;//寻路使用
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.id + "";
	}

	/**
	 * 生成站点状态
	 * 
	 * @param filePath
	 * @param stationsNum
	 * @param rate
	 *            可以换乘的概率
	 */
	public static void generateStationsStatus(String filePath, int stationsNum,
			double rate) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < stationsNum; i++) {
			sb.append(i);
			for (int j = 0; j < Node.STATUS_NUM; j++) {
				if (Math.random() < rate) {
					sb.append(" " + 1);
				} else {
					sb.append(" " + 0);
				}
			}
			sb.append("\n");
		}
		FileOperator.writeToFile(sb.toString(), filePath);
	}

	/**
	 * 生成站点状态
	 * 
	 * @param filePath
	 * @param gra
	 * @param rate
	 *            可以换乘的概率
	 */
	public static void generateStationsStatus(String filePath, Dijkstra gra,
			double rate) {
		StringBuffer sb = new StringBuffer();
		for (Node ele : gra.nodes.values()) {
			sb.append(ele.id);
			for (int j = 0; j < Node.STATUS_NUM; j++) {
				if (Math.random() < rate) {
					sb.append(" " + 1);
				} else {
					sb.append(" " + 0);
				}
			}
			sb.append("\n");
		}
		FileOperator.writeToFile(sb.toString(), filePath);
	}

	/**
	 * 初始化站台状态
	 * 
	 * @param fileName
	 * @param dij
	 *            图
	 * @throws FileNotFoundException 
	 */
	public static void initStationsStatus(String fileName, Dijkstra dij) throws FileNotFoundException {
		Scanner sc = new Scanner(new File(fileName));
		int id;
		Node node;
		while (sc.hasNextInt()) {
			id = sc.nextInt();
			node = dij.nodes.get(id);
			for (int i = 0; i < Node.STATUS_NUM; i++) {
				node.status[i] = (sc.nextInt() == 1);
			}
		}
		sc.close();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LinkedList<Node> nodes = new LinkedList<Node>();
	}
}
