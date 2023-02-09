package trees;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import image.GrayScaleImage;
import structs.Node;
import structs.Pair;
import utils.AdjacencyRelation;
import utils.ImageBuilder;

public class ComponentGraph {
	private GrayScaleImage f;
	private int size;
	private int[] parent;
	private int[] prevL;
	private int[] l;
	private int n;
	private int numNodes;
	private AdjacencyRelation adj;
	private Node[] mapPixelToNode;
	private LinkedList<Integer> parentChanges;
	private LinkedList<Integer> descChanges;
	private LinkedList<Pair> oddPq;
	private LinkedList<Pair> evenPq;
	private Node root;
	private GrayScaleImage rec;
	int count;

	public ComponentGraph(GrayScaleImage f, int n){
		this.f = f;
		this.n = n;
		this.numNodes = 0;
		this.size = f.getSize();
		this.parent = new int[f.getSize()];
		this.adj = AdjacencyRelation.getAssymetricalBox(2,2);
		this.mapPixelToNode = new Node[size];
		this.prevL = new int[size];
		this.l = new int[size];
		this.parentChanges = new LinkedList<Integer>();
		this.descChanges = new LinkedList<Integer>();
		this.oddPq = new LinkedList<Pair>();
		this.evenPq = new LinkedList<Pair>();
	}

	public Node getRoot(){
		return root;
	}

	public int getN(){
		return n;
	}

	public int getNumNodes(){
		return numNodes;
	}

	public void markSubdagAsVisited(Node current, int iMax){
		for(Node child: current.children){
			if(!child.visited && child.i <= iMax){
				child.visited = true;
				markSubdagAsVisited(child, iMax);
			}
		}
		for(Node prev: current.previous){
			if(!prev.visited && prev.i <= iMax){
				prev.visited = true;
				markSubdagAsVisited(prev, iMax);
			}
		}
	}

	public void resetVisited(Node current, int iMax){
		for(Node child: current.children){
			if(child.visited && child.i <= iMax){
				child.visited = false;
				resetVisited(child, iMax);
			}
		}
		for(Node prev: current.previous){
			if(prev.visited){
				prev.visited = false;
				resetVisited(prev, iMax);
			}
		}
	}

	public void reconstructNode(Node current){
		//		System.out.print(node.id + "/" + node.i + " ");
		for(Node child: current.children){
			if(!child.visited){
				child.visited = true;
				reconstructNode(child);
			}
		}
		for(Node prev: current.previous){
			if(!prev.visited){
				prev.visited = true;
				reconstructNode(prev);
			}
		}
		for(Integer p: current.pixels){
			rec.setPixel(p, 255);
		}
	}

	public GrayScaleImage reconstructNode(Node current, int width, int height){
		//		System.out.print("Node " + node.id + "/" + node.i + ": ");
		rec = new GrayScaleImage(width, height);
		count = 0;
		reconstructNode(current);
		//		System.out.println();

		resetVisited(current, n);
		return rec;
	}

	public Node nodeInPosition(int p){
		return mapPixelToNode[p];
	}

	public void makeSet(){
		for(int i=0;i<size;i++){
			parent[i] = i;
		}
	}

	public int find(int p){
		if(parent[p] != p && f.getPixel(p) == f.getPixel(parent[p])){
			parent[p] = find(parent[p]);
			return parent[p];
		}
		return p;
	}

	public void connect(int p, int q, boolean first, boolean changed, int spacing){

		//		for(int s=0;s<spacing;s++){
		//			System.out.print("  ");
		//		}
		//				System.out.println("connecting " + p + " with " + q);
		int rP = find(p);
		int rQ = find(q);
		if(f.getPixel(p) < f.getPixel(q)){
			//			System.out.println( " trocou");
			connect(rQ, rP, first, changed, spacing + 1);
		}
		else {
			if(!first && changed && !mapPixelToNode[rP].newChildren){
				mapPixelToNode[rP].newChildren = true;
				descChanges.add(rP);
				//												System.out.println("Add " + rP);
			}
			int parentP = find(parent[p]);
			if(rP != rQ && parentP != rQ){
				//				if(!first && changed && !mapPixelToNode[rQ].newParent){
				//					mapPixelToNode[rP].newParent = true;
				//					parentChanges.add(rP);
				////													System.out.println("Add " + rP);
				//				}

				if(f.getPixel(parentP) >= f.getPixel(q) && parentP != p){
					connect(parentP, rQ, first, changed, spacing + 1);
				}
				else{
					//										System.out.println(" Parent of " + rP + " is now " + rQ);
					parent[rP] = rQ;
					if(!first && !mapPixelToNode[rP].newParent){
						mapPixelToNode[rP].newParent = true;
						parentChanges.add(rP);
					}

					if(parentP != rP){
						connect(rQ, parentP, first, true, spacing+1);
					}
				}
			}
		}

	}

	public void updateParent(int i){
		prevL = l.clone(); 

		LinkedList<Pair> pq;
		LinkedList<Pair> nextPq;
		if(i % 2 == 0){
			pq = evenPq;
			nextPq = oddPq;
		}
		else{
			pq = oddPq;
			nextPq = evenPq;
		}

		while(!pq.isEmpty()){
			Pair pair = pq.remove();
			int p = pair.id;
			//			System.out.println(p);
			for(Integer q: adj.getAdjPixelsVectorDesc(f, p)){
				//									System.out.println(" " + q);
				connect(l[q], prevL[p], false, false, 0);
				if(f.getPixel(l[q]) < f.getPixel(prevL[p])){
					l[q] = prevL[p];
					nextPq.add(new Pair(q, f.getPixel(l[q])));
					//					System.out.println(" L of " + q + " is now " + p );
				}
			}
		}
	}

	public void firstParent(){
		makeSet();

		for(int p=0;p<size;p++){
			oddPq.add(new Pair(p, f.getPixel(p)));
			prevL[p] = p;
			l[p] = p;
		}
		while(!oddPq.isEmpty()){
			Pair pair = oddPq.remove();
			int p = pair.id;
			//						System.out.println(p);
			for(Integer q: adj.getAdjPixelsVectorDesc(f, p)){

				//				System.out.println(" " + q);
				connect(find(prevL[p]), find(l[q]), true, false, 0);
				if(f.getPixel(l[q]) < f.getPixel(prevL[p])){
					l[q] = prevL[p];
					evenPq.add(new Pair(q, f.getPixel(l[q])));
					//					System.out.println(" L of " + q + " is now " + p );
				}
			}
		}
	}

	public void computeFirstAttributes(Node current){
		current.intAttributes[Node.INT_AREA] = current.pixels.size();

		for(Integer p: current.pixels){
			int x = p%f.getWidth();
			int y = p/f.getWidth();
			if(x < current.intAttributes[Node.INT_MIN_X]){
				current.intAttributes[Node.INT_MIN_X] = x;
			}
			if(x > current.intAttributes[Node.INT_MAX_X]){
				current.intAttributes[Node.INT_MAX_X] = x;
			}
			if(y < current.intAttributes[Node.INT_MIN_Y]){
				current.intAttributes[Node.INT_MIN_Y] = y;
			}
			if(y > current.intAttributes[Node.INT_MAX_Y]){
				current.intAttributes[Node.INT_MAX_Y] = y;
			}
		}

		for(Node child: current.children){
			computeFirstAttributes(child);
			current.intAttributes[Node.INT_AREA] += child.intAttributes[Node.INT_AREA];
			if(child.intAttributes[Node.INT_MIN_X] < current.intAttributes[Node.INT_MIN_X]){
				current.intAttributes[Node.INT_MIN_X] = child.intAttributes[Node.INT_MIN_X];
			}
			if(child.intAttributes[Node.INT_MAX_X] > current.intAttributes[Node.INT_MAX_X]){
				current.intAttributes[Node.INT_MAX_X] = child.intAttributes[Node.INT_MAX_X];
			}
			if(child.intAttributes[Node.INT_MIN_Y] < current.intAttributes[Node.INT_MIN_Y]){
				current.intAttributes[Node.INT_MIN_Y] = child.intAttributes[Node.INT_MIN_Y];
			}
			if(child.intAttributes[Node.INT_MAX_Y] > current.intAttributes[Node.INT_MAX_Y]){
				current.intAttributes[Node.INT_MAX_Y] = child.intAttributes[Node.INT_MAX_Y];
			}
		}
	}

	public void computeFirstAttributes(){
		computeFirstAttributes(root);
	}

	public void updateAttributes(Node toUpdate, Node current){
		if(toUpdate.intAttributes[Node.INT_MIN_X] > current.intAttributes[Node.INT_MIN_X]){
			toUpdate.intAttributes[Node.INT_MIN_X] = current.intAttributes[Node.INT_MIN_X];
		}
		if(toUpdate.intAttributes[Node.INT_MAX_X] < current.intAttributes[Node.INT_MAX_X]){
			toUpdate.intAttributes[Node.INT_MAX_X] = current.intAttributes[Node.INT_MAX_X];
		}
		if(toUpdate.intAttributes[Node.INT_MIN_Y] > current.intAttributes[Node.INT_MIN_Y]){
			toUpdate.intAttributes[Node.INT_MIN_Y] = current.intAttributes[Node.INT_MIN_Y];
		}
		if(toUpdate.intAttributes[Node.INT_MAX_Y] < current.intAttributes[Node.INT_MAX_Y]){
			toUpdate.intAttributes[Node.INT_MAX_Y] = current.intAttributes[Node.INT_MAX_Y];
		}
		if(current.i == 1){
			toUpdate.intAttributes[Node.INT_AREA] += current.pixels.size();
		}
		else{
			toUpdate.intAttributes[Node.INT_AREA] += current.intAttributes[Node.INT_AREA];
		}
	}

	public void computeNextAttributes(Node toUpdate, Node current){
		if(current.i == 1){
			updateAttributes(toUpdate, current);
		}
		else{
			for(Node prev: current.previous){
				computeNextAttributes(toUpdate, prev);
			}
		}
	}

	public void computeNextAttributes(Node current){

		if(current.intAttributes[Node.INT_AREA] == 0){
			for(Node child: current.children){
				if(child.intAttributes[Node.INT_AREA] == 0){
					computeNextAttributes(child);
				}
				updateAttributes(current, child);
			}
			for(Node prev: current.previous){
				computeNextAttributes(current, prev);
			}
		}
	}

	public void updateGraph(int i){
		ArrayList<Node> newNodes = new ArrayList<Node>();
		while(!descChanges.isEmpty()){
			int p = descChanges.removeFirst();
			//			System.out.println((p+1) + "/" + i);
			Node nP = mapPixelToNode[p]; // current node that p points to
			nP.newChildren = false;
			int rP = find(p);

			if(nP.id == rP &&  nP.i < i){
				Node n = new Node(rP, f.getPixel(rP), i);
				//				System.out.println("Novo no " + n.id + "/" + n.i);
				numNodes++;
				mapPixelToNode[rP] = n;
				newNodes.add(n);
				nP.next = n;
				n.previous.add(nP);
				//				System.out.println("   Next de " + nP.id + "/" + nP.i + " = " + n.id + "/" + n.i);

			}
		}
		while(!parentChanges.isEmpty()){
			int p = parentChanges.removeFirst();
			//			System.out.println("   @#$ " + p);
			Node nP = mapPixelToNode[p];
			nP.newParent = false;
			int parentP = find(parent[p]);
			Node parentNode = mapPixelToNode[parentP]; 

			if(nP.level > parentNode.level){
				nP.iParents.add(parentNode);
				parentNode.children.add(nP);
			}
			else{
				mapPixelToNode[p] = parentNode;
				nP.next = parentNode;
				parentNode.previous.add(nP);
			}
		}
		for(Node nP: newNodes){ //updating parent relations for new nodes
			int parentP = find(parent[nP.id]);
			Node parentNode = mapPixelToNode[parentP];
			parentNode.children.add(nP);
			nP.iParents.add(parentNode);
		}
		
		Comparator<Node> nodeComparator = new Comparator<Node>() {
		    @Override
		    public int compare(Node n1, Node n2) {
		        return n2.id - n1.id; // use your logic
		    }
		};
		Collections.sort(newNodes, nodeComparator);
		
		for(Node nP: newNodes){ //adding missing parent and next relations to new nodes 
			for(Node prev: nP.previous){
				for(Node childPrev: prev.children){
					int repCP = find(parent[childPrev.id]);
					if(repCP == nP.id && childPrev.next == null){
						childPrev.iParents.add(nP);
						nP.children.add(childPrev);
					}
				}
			}
			computeNextAttributes(nP);
		}
		newNodes = new ArrayList<Node>();
	}

	public void buildFirstGraph(){
		for(int p=0;p<size;p++){ //creating nodes
			int rP = find(p);
			if(mapPixelToNode[rP] == null){
				mapPixelToNode[rP] = new Node(rP, f.getPixel(rP), 1);
				numNodes++;
			}
			mapPixelToNode[p] = mapPixelToNode[rP];
		}
		for(int p=0;p<size;p++){
			//updating pixels of each node, updating the parent relationship, finding the root
			int parentP = parent[p];
			Node nP = mapPixelToNode[p];
			nP.pixels.add(p);

			if(f.getPixel(p) == f.getPixel(parentP)){ //not a representative or the root
				if(p == parentP){
					root = mapPixelToNode[p];
				}
			}
			else{ //a representative that is not the root
				Node nParent = mapPixelToNode[parent[p]];
				nP.parent = nParent;
				nParent.children.add(nP);
			}
		}
	}

	public void printF(){
		for(int i=0;i<size;i++){
			if(i % f.getWidth() == 0){
				System.out.println();
			}
			System.out.print(f.getPixel(i) + " ");
		}
	}

	public void printParent(){
		System.out.println();
		for(int i=0; i<size;i++){
			System.out.println("id " + i + ": Parent = " + parent[i]);
		}
	}

	public void printNode(Node node, boolean att){
		System.out.print("Node " + (node.id));
		System.out.print("/" + (node.i));
		if(att){
			System.out.print("(lvl: " + node.level + ",");
			System.out.print(" area: " + node.intAttributes[Node.INT_AREA] + ",");
			System.out.print(" x: " + node.intAttributes[Node.INT_MIN_X] + "-" + node.intAttributes[Node.INT_MAX_X] + ",");
			System.out.print(" y: " + node.intAttributes[Node.INT_MIN_Y] + "-" + node.intAttributes[Node.INT_MAX_Y]);
		}
		System.out.print(")");
	}

	public void printPixels(Node node){
		for(Integer i: node.pixels){
			System.out.print(i + " ");
		}
	}

	public void printGraph(int depth, Node node){
		for(int d=0;d<depth;d++){
			System.out.print("  ");
		}
		printNode(node, true);
		if(node.next != null){
			System.out.print(" -> ");
			printNode(node.next, false);
		}
		//		System.out.println();
		//		printPixels(node);
		System.out.println();

		for(Node child: node.children){
			printGraph(depth + 2, child);
		}
	}

	public void printTreeStatistics(long t0, long tf, GrayScaleImage f, ComponentGraph cg){
		System.out.print(cg.getN() + ":\t" + (tf-t0)/1000.0 + "s");
		System.out.print(", Size = " + f.getSize() + " (" + f.getWidth() + " x " + f.getHeight() + ")");
		System.out.println(", numNodes = " + cg.getNumNodes());
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		File file1 = ImageBuilder.windowOpenFile();
		GrayScaleImage f = (GrayScaleImage) ImageBuilder.openGrayImage(file1);

		//		WindowImages.show(f, "input");
		long t0 = System.currentTimeMillis();
		ComponentGraph cg = new ComponentGraph(f, Math.max(f.getWidth(), f.getHeight()));
		cg.firstParent();
		cg.buildFirstGraph();
		cg.computeFirstAttributes();
		long tf = System.currentTimeMillis();
		System.out.println("1:\t" + (tf-t0)/1000.0 + "s\t" + cg.getNumNodes());
		int mult2 = 2;

		long t1 = tf;

		for(int i=2;i<=cg.getN();i++){
			if(i == mult2){
				t1 = tf;
			}
			cg.updateParent(i);
			cg.updateGraph(i);
			if(i == mult2){
				tf = System.currentTimeMillis();
				System.out.println(i + ":\t" + (tf-t1)/1000.0 + "s\t" + (tf-t0)/1000.0 + "s\t" + cg.getNumNodes());
				mult2 *= 2;
			}
		}

//		cg.printGraph(0, cg.getRoot());
		long tff = System.currentTimeMillis();

		cg.printTreeStatistics(t0, tff, f, cg);
	}
}