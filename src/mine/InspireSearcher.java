package mine;

import java.io.FileNotFoundException;

public class InspireSearcher extends Dijkstra{

	public InspireSearcher(String fileName) throws FileNotFoundException {
		super(fileName);
	}
	@Override
	public boolean exe(int beginIndex, int endIndex) {
		this.beginIndex = beginIndex;
		this.endIndex = endIndex;
		Node begin = this.nodes.get(this.beginIndex), end = this.nodes
				.get(this.endIndex);
		begin.disFromSource = 0;
		MinHeap<Node> que = new MinHeap<>(this.nodes.size());
		begin.vis = true;
		que.push(begin);
		Node node = null;
		int index;
		while (!que.empty()) {
			node=que.fron();
			node.vis=false;
			if(node==end){
				System.out.println("inspire fine!");
				return true;
			}
			index=-1;
			for(Node ele:node.nexts){
				index++;
				if(ele.disFromSource>node.disFromSource+node.nextDis.get(index)){
					ele.disFromSource=node.disFromSource+node.nextDis.get(index);
					ele.parent=node;
					if(ele.vis){
						que.update(ele);
					}else{
						ele.vis=true;
						que.push(ele);
					}
				}
			}
		}
		return false;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
