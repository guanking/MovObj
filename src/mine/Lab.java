package mine;

import java.io.IOException;

public class Lab {
	/**
	 * 固定其他因素，改变用户的 detour
	 * 
	 * @param beginIndex
	 * @param endIndex
	 * @throws IOException
	 */
	public static void labDetour(int beginIndex, int endIndex)
			throws IOException {
		double min = 1;// 下限
		double max = 2;// 上限
		double delta = 0.001;
		for (double detourPer = min; detourPer < max; detourPer += delta) {
			BFSInspireSearcher.DETOUR_PERCENT = detourPer;
			Searcher.compare(beginIndex, endIndex);
		}
	}

	/**
	 * 固定其他因素，改变用户的行车速度
	 * 
	 * @param beginIndex
	 * @param endIndex
	 * @throws IOException
	 */
	public static void labSpeed(int beginIndex, int endIndex)
			throws IOException {
		double min = 0.00001;
		double max = 0.004;
		double delta = 0.00001;
		for (double cur = min; cur < max; cur += delta) {
			BFSInspireSearcher.SPEED = cur;
			System.out.print("speed: " + BFSInspireSearcher.SPEED + " ");
			Searcher.compare(beginIndex, endIndex);
		}
	}
	public static void mentImitate() throws IOException{
		Searcher.initWriter(Searcher.compareResult);
		BFSInspireSearcher bfs = new BFSInspireSearcher("bsDetails.txt");
		for (Integer index0 : bfs.nodes.keySet()) {
			COMPARE: for (Integer index1 : bfs.nodes.keySet()) {
				if (index0 != index1) {
					System.out.println("{" + index0 + "," + index1 + "}");
					Searcher.compare(index0, index1);
				}
			}
		}
		Searcher.writer.close();
	}
	public static void main(String[] args) throws IOException {
		int test[] = new int[] { 1, 631 };
		int choice = 0;
		Searcher.initWriter(Searcher.compareResult);
		switch (choice) {
		case 0:// 速度试验
			Lab.labSpeed(test[0], test[1]);
			break;
		case 1:// detour_percent实验（Detour实验）
			Lab.labDetour(test[0], test[1]);
			break;
		}
		Searcher.writer.close();
		System.out.println("finish!");
	}
}
