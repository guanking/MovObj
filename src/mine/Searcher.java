package mine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import test.YenTopKShortestPathsAlgTest;
import edu.asu.emit.algorithm.graph.Path;
import edu.asu.emit.algorithm.graph.shortestpaths.DijkstraShortestPathAlg;
import edu.asu.emit.algorithm.graph.shortestpaths.YenTopKShortestPathsAlg;

public abstract class Searcher {
	public static final String statusFilePath = FileOperator.DATA_DIR
			+ File.separator + "stationsChangeStatus.txt";
	public static final String compareResult = FileOperator.OUTPUT_DIR
			+ File.separator + "compareResult.txt";
	public static FileWriter writer = null;

	public static void initWriter(String fileName) throws IOException {
		Searcher.writer = new FileWriter(new File(fileName));
	}

	public static void generateStatus() throws FileNotFoundException {
		BFSInspireSearcher bfs = new BFSInspireSearcher("bsDetails.txt");
		Node.generateStationsStatus(statusFilePath, bfs,
				BFSInspireSearcher.CHANGE_RATE);
	}

	public static void compare(int beginIndex, int endIndex) throws IOException {
		writer.write(beginIndex + " " + endIndex);
		YenTopKShortestPathsAlgTest test = new YenTopKShortestPathsAlgTest();
		BFSInspireSearcher bfs = new BFSInspireSearcher("bsDetails.txt");
		DijkstraShortestPathAlg alg = new DijkstraShortestPathAlg(test.graph);
		Path path = alg.getShortestPath(test.graph.getVertex(beginIndex),
				test.graph.getVertex(endIndex));
		BFSInspireSearcher.DETOUR = path.getWeight()
				* BFSInspireSearcher.DETOUR_PERCENT;
		Node.initStationsStatus(statusFilePath, bfs);
		boolean find = false;
		long begin, end;
		/**
		 * mine
		 */
		begin = System.currentTimeMillis();
		find = bfs.exe(beginIndex, endIndex);
		end = System.currentTimeMillis();
		begin = end - begin;
		System.out.println("Our DFSInspire time : " + begin + "ms");
		if (find) {
			writer.write(" 1 " + begin + " " + bfs.endQNode.disFromSrc);
			bfs.showPath();
		} else {
			writer.write(" 1 " + begin + " -1");
			System.out.println("not find");
		}
		/**
		 * Yen
		 */
		find = false;
		YenTopKShortestPathsAlg yenAlg = new YenTopKShortestPathsAlg(
				test.graph, test.graph.getVertex(beginIndex),
				test.graph.getVertex(endIndex));
		int i = 0;
		begin = System.currentTimeMillis();
		while (yenAlg.hasNext()) {
			i++;
			path = yenAlg.next();
			if (path.getWeight() > BFSInspireSearcher.DETOUR) {
				find = false;
				break;
			}
			if (bfs.judge(path, bfs.beginTime)) {
				find = true;
				break;
			}
		}
		begin = System.currentTimeMillis() - begin;
		System.out.println("Yen " + i + "-shortest time : " + begin + "ms");
		if (find) {
			writer.write(" 0 " + begin + " " + path.getWeight() + "\n");
			System.out.println(path);
		} else {
			writer.write(" 0 " + begin + " -1\n");
			System.out.println("not find");
		}
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		/**
		 * 0: 初始化状态<b>慎用</b> <br>
		 * 1: 算法比较 <br>
		 * 2: 自己的 <br>
		 * 3: Yen的 <br>
		 * 4: 比较特定的起始点<br>
		 * 5: 自己的(运行特定的点集合)<br>
		 * 6: 模拟<br>
		 */
		int choice = 6;
		int data[] = new int[] { 1, 1223 };
		int beginIndex = data[0];
		int endIndex = data[1];
		/**
		 * Yen 要跑好久
		 */
		int addition[][] = new int[][] { { 1, 272 }, { 1, 266 }, { 1, 264 },
				{ 1, 265 }, { 1, 39 }, { 1, 292 }, { 1, 794 }, { 1, 885 },
				{ 1, 83 }, { 1, 531 }, { 1, 621 }, { 1, 58 }, { 1, 91 },
				{ 1, 851 }, { 1, 373 }, { 1, 1490 }, { 1, 1498 }, { 1, 1496 },
				{ 1, 1480 }, { 1, 169 }, { 1, 200 }, { 1, 193 }, { 1, 742 },
				{ 1, 192 }, { 1, 454 }, { 1, 451 }, { 1, 715 }, { 1372, 272 } };
		/**
		 * not find path
		 */
		int notFind[][] = new int[][] { { 1, 1372 }, { 1, 1371 }, { 1, 10 },
				{ 1, 1095 }, { 1, 570 }, { 1, 1404 }, { 1, 1132 }, { 1, 38 },
				{ 1, 47 }, { 1, 773 }, { 1, 1384 }, { 1, 1143 }, { 1, 1137 },
				{ 1, 1033 }, { 1, 1036 }, { 1, 1027 }, { 1, 347 }, { 1, 345 },
				{ 1, 620 }, { 1, 323 }, { 1, 1051 }, { 1, 865 }, { 1, 626 },
				{ 1, 552 }, { 1, 627 }, { 1, 1055 }, { 1, 1281 }, { 1, 579 },
				{ 1, 1071 }, { 1, 859 }, { 1, 1056 }, { 1, 834 }, { 1, 593 },
				{ 1, 1322 }, { 1, 595 }, { 1, 1087 }, { 1, 597 }, { 1, 1075 },
				{ 1, 1073 }, { 1, 685 }, { 1, 685 }, { 1, 400 }, { 1, 401 },
				{ 1, 676 }, { 1, 138 }, { 1, 949 }, { 1, 1499 }, { 1, 1236 },
				{ 1, 703 }, { 1, 145 }, { 1, 691 }, { 1, 1243 }, { 1, 171 },
				{ 1, 908 }, { 1, 906 }, { 1, 658 }, { 1, 745 }, { 1, 470 },
				{ 1, 469 }, { 1, 1166 }, { 1, 461 }, { 1, 216 }, { 1, 210 },
				{ 1, 238 }, { 1, 237 }, { 1, 1190 }, { 1, 233 }, { 1, 1189 },
				{ 1, 704 }, { 1, 1467 }, { 1, 711 }, { 1, 1202 }, { 1, 252 },
				{ 1, 733 }, { 1, 490 }, { 1, 1214 }, { 1, 725 }, { 1, 726 },
				{ 1, 241 }, { 1372, 1 } };
		/**
		 * 我们的好
		 */
		int well[][] = new int[][] { { 1, 566 }, { 1, 562 }, { 1, 561 },
				{ 1, 260 }, { 1, 261 }, { 1, 267 }, { 1, 271 }, { 1, 30 },
				{ 1, 74 }, { 1, 631 }, { 1, 62 }, { 1, 24 }, { 1, 61 },
				{ 1, 101 }, { 1, 108 }, { 1, 403 }, { 1, 385 }, { 1, 442 },
				{ 1, 172 }, { 1, 163 }, { 1, 176 }, { 1, 1430 }, { 1, 474 },
				{ 1, 472 }, { 1, 202 }, { 1, 197 }, { 1, 738 }, { 1, 721 } };
		/**
		 * 不好
		 */
		int bad[][] = new int[][] { { 1, 1223 } };
		int[][] specific = notFind;// 指定测试哪一种
		BFSInspireSearcher bfs = null;
		Path path = null;
		YenTopKShortestPathsAlgTest test = null;
		DijkstraShortestPathAlg alg = null;
		boolean find;
		long begin, end;
		BFSInspireSearcher.DETOUR_PERCENT = 1.2;
		switch (choice) {
		case 0:// 初始化状态
			generateStatus();
			break;
		case 1:// 算法比较
			initWriter(Searcher.compareResult);
			bfs = new BFSInspireSearcher("bsDetails.txt");
			for (Integer index0 : bfs.nodes.keySet()) {
				COMPARE: for (Integer index1 : bfs.nodes.keySet()) {
					if (index0 != index1) {
						for (int[] ele : addition) {
							if (ele[0] == index0 && ele[1] == index1) {
								continue COMPARE;
							}
						}
						for (int[] ele : notFind) {
							if (ele[0] == index0 && ele[1] == index1) {
								continue COMPARE;
							}
						}
						for (int[] ele : well) {
							if (ele[0] == index0 && ele[1] == index1) {
								continue COMPARE;
							}
						}
						for (int[] ele : bad) {
							if (ele[0] == index0 && ele[1] == index1) {
								continue COMPARE;
							}
						}
						System.out.println("{" + index0 + "," + index1 + "}");
						compare(index0, index1);
					}
				}
			}
			writer.close();
			break;
		case 2:// 自己的
			test = new YenTopKShortestPathsAlgTest();
			bfs = new BFSInspireSearcher("bsDetails.txt");
			alg = new DijkstraShortestPathAlg(test.graph);
			path = alg.getShortestPath(test.graph.getVertex(beginIndex),
					test.graph.getVertex(endIndex));
			BFSInspireSearcher.DETOUR = path.getWeight()
					* BFSInspireSearcher.DETOUR_PERCENT;
			Node.initStationsStatus(statusFilePath, bfs);
			begin = System.currentTimeMillis();
			find = bfs.exe(beginIndex, endIndex);
			end = System.currentTimeMillis();
			begin = end - begin;
			System.out.println("Our DFSInspire time : " + begin + "ms");
			if (find) {
				bfs.showPath();
			} else {
				System.out.println("ourPath not find!\n");
			}
			break;
		case 3:// Yen的
			test = new YenTopKShortestPathsAlgTest();
			bfs = new BFSInspireSearcher("bsDetails.txt");
			alg = new DijkstraShortestPathAlg(test.graph);
			path = alg.getShortestPath(test.graph.getVertex(beginIndex),
					test.graph.getVertex(endIndex));
			BFSInspireSearcher.DETOUR = path.getWeight()
					* BFSInspireSearcher.DETOUR_PERCENT;
			Node.initStationsStatus(statusFilePath, bfs);
			YenTopKShortestPathsAlg yenAlg = new YenTopKShortestPathsAlg(
					test.graph, test.graph.getVertex(beginIndex),
					test.graph.getVertex(endIndex));
			find = false;
			int i = 0;
			begin = System.currentTimeMillis();
			while (yenAlg.hasNext()) {
				i++;
				path = yenAlg.next();
				if (path.getWeight() > BFSInspireSearcher.DETOUR) {
					find = false;
					break;
				}
				if (bfs.judge(path, bfs.beginTime)) {
					find = true;
					break;
				} else {
					System.out.println("Yen " + i + " :" + path);
				}
			}
			begin = System.currentTimeMillis() - begin;
			System.out.println("Yen " + i + "-shortest time : " + begin + "ms");
			if (find) {
				System.out.println(path);
			} else {
				System.out.println("Yen not find!");
			}
			break;
		case 4:// 比较特定的起始点
			initWriter(Searcher.compareResult);
			for (int[] ele : specific) {
				System.out.println("{" + ele[0] + "," + ele[1] + "}");
				compare(ele[0], ele[1]);
			}
			writer.close();
			break;
		case 5:// 自己的(运行特定的点集合)
			StringBuffer sb = new StringBuffer();
			for (BFSInspireSearcher.DETOUR_PERCENT = 1.1; BFSInspireSearcher.DETOUR_PERCENT < 1.4; BFSInspireSearcher.DETOUR_PERCENT += 0.05) {
				int count = 0;
				for (int[] ele : specific) {
					beginIndex = ele[0];
					endIndex = ele[1];
					test = new YenTopKShortestPathsAlgTest();
					bfs = new BFSInspireSearcher("bsDetails.txt");
					alg = new DijkstraShortestPathAlg(test.graph);
					path = alg.getShortestPath(
							test.graph.getVertex(beginIndex),
							test.graph.getVertex(endIndex));
					BFSInspireSearcher.DETOUR = path.getWeight()
							* BFSInspireSearcher.DETOUR_PERCENT;
					Node.initStationsStatus(statusFilePath, bfs);
					begin = System.currentTimeMillis();
					find = bfs.exe(beginIndex, endIndex);
					end = System.currentTimeMillis();
					begin = end - begin;
					System.out.println("Our DFSInspire time : " + begin + "ms");
					if (find) {
						bfs.showPath();
					} else {
						System.out.println("ourPath not find!\n");
						count++;
					}
					System.out.println("DetourPercent:"
							+ BFSInspireSearcher.DETOUR_PERCENT);
				}
				sb.append(BFSInspireSearcher.DETOUR_PERCENT + " " + count + " "
						+ specific.length + "\n");
				System.out.println(sb.toString());
			}
			System.out.println(sb.toString());
			break;
		case 6:
			initWriter(Searcher.compareResult);
			specific = new int[notFind.length + well.length][2];
			for (i = 0; i < specific.length; i++) {
				if (i < notFind.length) {
					specific[i] = notFind[i];
				} else {
					specific[i] = well[i - notFind.length];
				}
			}
			i = 100;
			while (i-- > 0) {
				int[] ele = specific[(int) Math.floor(specific.length
						* Math.random())];
				compare(ele[0], ele[1]);
			}
			writer.close();
			break;
		}
		System.out.println("finish!specific's size " + specific.length);
	}

}
