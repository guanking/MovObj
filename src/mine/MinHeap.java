package mine;

/**
 * 
 * @author GuanjieCao
 * @note 数组下标从1开始，为了使用二叉树的一些性质<br>
 *       这是一个最小堆，堆顶的元素最小
 * @param <T>
 */
public class MinHeap<T extends Comparable<T>> {
	private int lastIndex;
	private T[] nodes;

	/**
	 * 构造函数
	 * 
	 * @param size
	 *            堆的大小(上限)
	 */
	public MinHeap(int size) {
		this.lastIndex = 0;
		this.nodes = (T[]) new Node[size + 1];// 下标从1开始
	}

	/**
	 * 判断是否为空
	 * 
	 * @return
	 */
	public boolean empty() {
		return this.lastIndex == 0;
	}

	/**
	 * 查看堆顶的元素，不弹出
	 * 
	 * @return
	 */
	public T retrieve() {
		return this.nodes[1];
	}

	/**
	 * 取出并弹出堆顶元素，并重新调整最小堆
	 * 
	 * @return
	 */
	public T fron() {
		T t = this.nodes[1];
		this.nodes[1] = this.nodes[this.lastIndex--];
		this.down(1);
		return t;
	}

	/**
	 * 加入一个元素进入最小堆中
	 * 
	 * @param ele
	 */
	public void push(T ele) {
		this.nodes[++this.lastIndex] = ele;
		this.up(this.lastIndex);
	}

	/**
	 * 更新最小堆中的某一个元素
	 * 
	 * @param t
	 * @return 是否更新成功
	 * @warning 这里使用引用判断两个对象是否相同
	 */
	public boolean update(T t) {
		for (int i = 1; i <= this.lastIndex; i++) {
			if (this.nodes[i] == t) {
				this.up(i);
				return true;
			}
		}
		System.out.println("MinHeap.update " + t.toString() + " not find!");
		return false;
	}

	/**
	 * 向下调整最小堆
	 * 
	 * @param index
	 */
	public void down(int index) {
		T t = null;
		while (index << 1 <= this.lastIndex) {
			index <<= 1;
			if (index + 1 <= this.lastIndex
					&& this.nodes[index + 1].compareTo(this.nodes[index]) < 0) {
				index++;
			}
			if (this.nodes[index].compareTo(this.nodes[index >> 1]) < 0) {
				t = this.nodes[index];
				this.nodes[index] = this.nodes[index >> 1];
				this.nodes[index >> 1] = t;
			} else {
				break;
			}
		}
	}

	/**
	 * 向上调整最小堆
	 * 
	 * @param index
	 */
	public void up(int index) {
		T t = null;
		while (index > 1) {
			if (this.nodes[index].compareTo(this.nodes[index >> 1]) < 0) {
				t = this.nodes[index];
				this.nodes[index] = this.nodes[index >> 1];
				this.nodes[index >> 1] = t;
				index >>= 1;
			} else {
				break;
			}
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MinHeap<Node> nodes = new MinHeap<>(50);
		for (int i = 0; i < 50; i++) {
			nodes.push(new Node());
			if (Math.random() < 0.1) {
				System.out.println("pop : " + nodes.fron());
			}
		}
		while (!nodes.empty()) {
			System.out.println(nodes.fron());
		}
	}

}
