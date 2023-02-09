package structs;

public class Pair implements Comparable<Pair>{
	public int id;
	public double cost;
	
	public Pair(){
		this.id = -1;
		this.cost = -1;
	}
	
	public Pair(int id, double cost){
		this.id = id;
		this.cost = cost;
	}
	
	public int compareTo(Pair otherPair) {
		if(otherPair.cost > this.cost)
			return 1;
		else if(otherPair.cost < this.cost)
			return -1;
		else
			return 0;
	}	

}
