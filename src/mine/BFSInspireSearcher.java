package mine;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeSet;

import edu.asu.emit.algorithm.graph.Path;
import edu.asu.emit.algorithm.graph.Vertex;
import edu.asu.emit.algorithm.graph.abstraction.BaseVertex;
import edu.asu.emit.algorithm.graph.shortestpaths.DijkstraShortestPathAlg;
import edu.asu.emit.algorithm.graph.shortestpaths.YenTopKShortestPathsAlg;
import test.YenTopKShortestPathsAlgTest;

public class BFSInspireSearcher extends Dijkstra {
	/**
	 * 租车时间上限
	 */
	public static final double MAX_TIME = 3;
	/**
	 * 自行车速度
	 */
	public static double SPEED = 0.002;
	/**
	 * 用户所能容忍的最远距离
	 */
	public static double DETOUR = 10;
	/**
	 * 最短距离乘以DETOUR_PERCENT就是DETOUR
	 */
	public static  double DETOUR_PERCENT = 1.2;
	/**
	 * 自行车状态可以换乘的概率
	 */
	public static final double CHANGE_RATE = 0.5;
	public QNode endQNode;
	public static final double beginTime = 0;

	public BFSInspireSearcher(String fileName) throws FileNotFoundException {
		super(fileName);
	}

	public static class QNode implements Comparable<QNode> {
		/**
		 * 和Node的id一样
		 */
		int id;
		/**
		 * 指向父节点
		 */
		QNode parent;
		/**
		 * 到起始点的距离
		 */
		double disFromSrc;
		/**
		 * 免费还可以持续的时间
		 */
		double remainTime;
		/**
		 * 当前时间
		 */
		double currentTime;

		/**
		 * 用于构造起始节点
		 * 
		 * @param id
		 * @param currentTime
		 */
		QNode(int id, double currentTime) {
			this.id = id;
			this.parent = null;
			this.disFromSrc = 0;
			this.remainTime = BFSInspireSearcher.MAX_TIME;
			this.currentTime = currentTime;
		}

		/**
		 * 用于构造遍历过程中新产生的节点
		 * 
		 * @param parent
		 * @param id
		 * @param dis
		 * @param currentTime
		 */
		public QNode(QNode parent, Integer id, double dis, double currentTime) {
			this.id = id;
			this.parent = parent;
			this.disFromSrc = dis;
			this.currentTime = currentTime;
		}

		/**
		 * 定义距离起始点近的点小
		 */
		@Override
		public int compareTo(QNode o) {
			return this.disFromSrc < o.disFromSrc ? -1 : 1;
		}

		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return "{id:" + this.id + ",disFromSrc:" + this.disFromSrc
					+ ",currentTime:" + this.currentTime + ",remainTime:"
					+ this.remainTime + "}";
		}
	}

	/**
	 * 启发式广度优先遍历
	 * 
	 * @param beginIndex
	 *            开始节点的id
	 * @param endIndex
	 *            终止节点的id
	 * @return
	 * @warning 结束方式二选一
	 */
	@Override
	public boolean exe(int beginIndex, int endIndex) {
		Node beginNode = this.nodes.get(beginIndex), endNode = this.nodes
				.get(endIndex);
		if (beginNode == null || endNode == null) {
			System.out.println("Index of Begin or End is Wrong!");
		}
		PriorityQueue<QNode> que = new PriorityQueue<BFSInspireSearcher.QNode>();
		que.add(new QNode(beginNode.id, this.beginTime));
		Node node;
		QNode curQNode, qRet;
		int index;
		double distance, timeCost;
		Set<Integer> parents = new TreeSet<Integer>();
		while (!que.isEmpty()) {
			curQNode = que.poll();
			// if(curQNode.id==endIndex){//初队列时判断
			// this.endQNode=curQNode;
			// return true;
			// }
			node = this.nodes.get(curQNode.id);
			index = -1;
			qRet = curQNode;
			do {
				parents.add(qRet.id);
				qRet = qRet.parent;
			} while (qRet != null);
			for (Node ele : node.nexts) {
				index++;
				if (parents.contains(ele.id)) {// 已经在路径上
					continue;
				}
				/**
				 * @扩展条件 <br>
				 *       路径总长度不超过DETOUR(剪枝)<br>
				 *       在允许时间内可以到达(确保解是正确的)<br>
				 * @附加操作 <br>
				 *       计算当前时间(用于判断站台的状态)<br>
				 *       计算剩余时间(remainTime)<br>
				 *       能换乘就换乘（更新remainTime）<br>
				 *       计算距离(用于启发选择)<br>
				 */
				distance = node.nextDis.get(index);// 获取这条路的长度
				if (curQNode.disFromSrc + distance > BFSInspireSearcher.DETOUR) {
					continue;
				}
				timeCost = distance / BFSInspireSearcher.SPEED;
				if (curQNode.remainTime - timeCost < 0) {
					continue;
				}
				qRet = new QNode(curQNode, ele.id, curQNode.disFromSrc
						+ distance, curQNode.currentTime + timeCost);
				if (qRet.id == endIndex) {// 在进入队列之前判断，比在出队列时快
					this.endQNode = qRet;
					return true;
				}
				if (node.status[(int) Math.round(curQNode.currentTime
						+ timeCost)]) {
					qRet.remainTime = BFSInspireSearcher.MAX_TIME;
				} else {
					qRet.remainTime = curQNode.remainTime - timeCost;
				}
				que.add(qRet);
			}
			parents.clear();
		}
		return false;
	}

	/**
	 * 显示路径
	 */
	@Override
	public void showPath() {
		// TODO Auto-generated method stub
		if (this.endQNode == null) {
			System.out.println("Find path failed!");
			return;
		}
		LinkedList<Node> nodes = new LinkedList<Node>();
		QNode node = this.endQNode;
		do {
			nodes.addFirst(this.nodes.get(node.id));
			node = node.parent;
		} while (node != null);
		boolean first = true;
		for (Node ele : nodes) {
			if (first) {
				System.out.print("[" + ele.id);
				first = false;
			} else {
				System.out.print(", " + ele.id);
			}
		}
		System.out.println("]" + this.endQNode.disFromSrc);
	}

	public boolean judge(Path authorPath, double beginTime) {
		List<BaseVertex> lists = authorPath.getVertexList();
		Vertex vertex = null;
		QNode front = null, back = null;
		Node node;
		boolean first = true;
		for (BaseVertex ele : lists) {
			vertex = (Vertex) ele;
			if (first) {
				first = false;
				front = new QNode(vertex.getId(), beginTime);
			} else {
				node = this.nodes.get(front.id);
				int index = -1;
				double distance = -1;
				for (Node tn : node.nexts) {
					index++;
					if (tn.id == vertex.getId()) {
						distance = node.nextDis.get(index);// 获取这条路的长度
					}
				}
				if (distance < 0) {
					System.out.println("error");
				}
				if (front.disFromSrc + distance > BFSInspireSearcher.DETOUR) {// 超过用户需求
					return false;
				}
				double timeCost = distance / BFSInspireSearcher.SPEED;
				if (front.remainTime - timeCost < 0) {// 骑不到
					return false;
				}
				back = new QNode(front, vertex.getId(), front.disFromSrc
						+ distance, front.currentTime + timeCost);
				if (node.status[(int) Math.round(front.currentTime + timeCost)]) {
					back.remainTime = BFSInspireSearcher.MAX_TIME;
				} else {
					back.remainTime = front.remainTime - timeCost;
				}
				front = back;
			}
		}
		return true;
	}

	public static void main1(String[] args) throws FileNotFoundException {
		int beginIndex = 271;// 开始节点
		int endIndex = 74;// 终止节点
		String statusFilePath = FileOperator.DATA_DIR + File.separator
				+ "stationsChangeStatus.txt";
		YenTopKShortestPathsAlgTest test = new YenTopKShortestPathsAlgTest();
		BFSInspireSearcher bfs = new BFSInspireSearcher("bsDetails.txt");
		DijkstraShortestPathAlg alg = new DijkstraShortestPathAlg(test.graph);
		Path path = alg.getShortestPath(test.graph.getVertex(beginIndex),
				test.graph.getVertex(endIndex));
		BFSInspireSearcher.DETOUR = path.getWeight() * 1.2;
		Node.generateStationsStatus(statusFilePath, bfs,
				BFSInspireSearcher.CHANGE_RATE);
		Node.initStationsStatus(statusFilePath, bfs);
		long begin, end;
		/**
		 * Yen
		 */
		YenTopKShortestPathsAlg yenAlg = new YenTopKShortestPathsAlg(
				test.graph, test.graph.getVertex(beginIndex),
				test.graph.getVertex(endIndex));
		boolean find = false;
		int i = 0;
		begin = System.currentTimeMillis();
		while (yenAlg.hasNext()) {
			i++;
			path = yenAlg.next();
			if (bfs.judge(path, bfs.beginTime)) {
				find = true;
				break;
			}
		}
		begin = System.currentTimeMillis() - begin;
		System.out.println("Yen " + i + "-shortest time : " + begin + "ms");
		if (find) {
			System.out.println(path);
		}
		/**
		 * mine
		 */
		begin = System.currentTimeMillis();
		bfs.exe(beginIndex, endIndex);
		end = System.currentTimeMillis();
		begin = end - begin;
		System.out.println("Our DFSInspire time : " + begin + "ms");
		bfs.showPath();
	}
}
