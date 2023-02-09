package structs;

import java.util.ArrayList;
import java.util.LinkedList;

public class Node {
	public int id;
	public int level;
	public int i;
	public Node parent;
	public Node next;
	public LinkedList<Node> children;
	public LinkedList<Node> iParents;
	public LinkedList<Node> previous;
	public LinkedList<Integer> pixels;
	public int[] intAttributes;
	public boolean marked;
	public boolean visited;
	public boolean newParent;
	public boolean newChildren;
	
	public static final int INT_AREA = 0;
	public static final int INT_MIN_X = 1;
	public static final int INT_MIN_Y = 2;
	public static final int INT_MAX_X = 3;
	public static final int INT_MAX_Y = 4;
	
	public final int NUM_INT_ATTS = 5;
	
	public Node(int id, int level, int i){
		this.id = id;
		this.level = level;
		this.i = i;
		this.parent = null;
		this.next = null;
		this.marked = false;
		this.visited = false;
		this.newParent = false;
		this.newChildren = false;
		this.children = new LinkedList<Node>();
		this.iParents = new LinkedList<Node>();
		this.previous = new LinkedList<Node>();
		this.pixels = new LinkedList<Integer>();
		this.intAttributes = new int[NUM_INT_ATTS];
		this.intAttributes[INT_AREA] = 0;
		this.intAttributes[INT_MIN_X] = Integer.MAX_VALUE;
		this.intAttributes[INT_MIN_Y] = Integer.MAX_VALUE;
		this.intAttributes[INT_MAX_X] = -1;
		this.intAttributes[INT_MAX_Y] = -1;
	}
}
