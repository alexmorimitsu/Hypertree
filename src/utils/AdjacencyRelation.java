package utils;

import image.IImage;

import java.util.ArrayList;
import java.util.List;

public class AdjacencyRelation {
	int px[];
	int py[];

	public int getSize(){
		return px.length;
	}

	private AdjacencyRelation(int n) {
		px = new int[n];
		py = new int[n];
	}

	public List<Integer> getAdjPixelsVectorDesc(IImage img, int x, int y) {
		ArrayList<Integer> neighbors = new ArrayList<Integer>();        
		for(int i=0; i < px.length; i++){
			if(img.isPixelValid(px[i] + x, py[i] + y))
				neighbors.add( (py[i] + y) * img.getWidth() + (px[i] + x) );
		}
		return neighbors;
	}

	public List<Integer> getAdjPixelsVectorDesc(IImage img, int i) {
		int x = i % img.getWidth();
		int y =i / img.getWidth();
		ArrayList<Integer> neighbors = new ArrayList<Integer>();        
		for(i=0; i < px.length; i++){
			if(img.isPixelValid(px[i] + x, py[i] + y))
				neighbors.add( ((py[i] + y) * img.getWidth()) + (px[i] + x) );
		}
		return neighbors;
	}


	public AdjacencyRelation CloneAdjRel(){
		AdjacencyRelation adj = new AdjacencyRelation(px.length);
		int i;  
		for(i=0; i < this.px.length; i++){
			adj.px[i] = this.px[i];
			adj.py[i] = this.py[i];
		}
		return adj;
	}

	public AdjacencyRelation rightSide(){
		AdjacencyRelation adj=null;
		int i, dx, dy;
		double d;

		/* Let p -> q be an arc represented by the increments dx,dy. Its
     	right side is given by the increments Dx = -dy/d + dx/2 and Dy =
     	dx/d + dy/2, where d=sqrt(dx�+dy�). */

		adj = new AdjacencyRelation(this.px.length);
		for (i=0; i < adj.px.length; i++){
			d = Math.sqrt (this.px[i] * this.px[i] + this.py[i] * this.py[i]);

			if (d != 0){
				dx = (int) Math.round( (this.px[i] / 2.0) - (this.py[i] / d) );
				dy = (int) Math.round( (this.px[i] / d) + (this.py[i] / 2.0) );
				adj.px[i] = dx;
				adj.py[i] = dy;
			}
		}

		return(adj);
	}


	public AdjacencyRelation leftSide(){
		AdjacencyRelation L = null;
		int i, dx, dy;
		double d;

		/* Let p -> q be an arc represented by the increments dx,dy. Its
     	left side is given by the increments Dx = dy/d + dx/2 and Dy =
     	-dx/d + dy/2, where d=sqrt(dx�+dy�). */

		L = new AdjacencyRelation(this.px.length);
		for (i=0; i < L.px.length; i++){
			d = Math.sqrt (this.px[i] * this.px[i] + this.py[i] * this.py[i]);
			if (d != 0){
				dx = (int) Math.round( (this.px[i] / 2.0) + (this.px[i] / d) );
				dy = (int) Math.round( (this.px[i] / 2.0) - (this.px[i] / d));
				L.px[i] =dx;
				L.py[i] =dy; 
			}
		}

		return(L);
	}


	public AdjacencyRelation rightSide2(float r){
		AdjacencyRelation R;
		int i, dx, dy;
		double d;

		/* Let p -> q be an arc represented by the increments dx,dy. Its
     	right side is given by the increments Dx = -dy/d + dx/2 and Dy =
     	dx/d + dy/2, where d=sqrt(dx�+dy�). */

		R = new AdjacencyRelation(this.px.length);
		for (i=0; i < R.px.length; i++){
			d = Math.sqrt (this.px[i] * this.px[i] + this.py[i] * this.py[i]);
			if (d != 0){
				dx = (int) Math.round( (this.px[i] / 2.0) - (this.py[i] / d) * r);
				dy = (int) Math.round( (this.px[i] / d) + (this.py[i] / 2.0) * r);
				R.px[i] = dx;
				R.py[i] = dy; 
			}
		}

		return(R);
	}

	public AdjacencyRelation leftSide2(float r){
		AdjacencyRelation L;
		int i, dx, dy;
		double d;

		/* Let p -> q be an arc represented by the increments dx,dy. Its
     	left side is given by the increments Dx = dy/d + dx/2 and Dy =
     	-dx/d + dy/2, where d=sqrt(dx�+dy�). */

		L = new AdjacencyRelation(this.px.length);
		for (i=0; i < L.px.length; i++){
			d = Math.sqrt (this.px[i] * this.px[i] + this.py[i] * this.py[i]);
			if (d != 0){
				dx = (int) Math.round( (this.px[i] / 2.0) + (this.py[i] / d) * r);
				dy = (int) Math.round( (this.py[i] / 2.0) - (this.px[i] / d) * r);
				L.px[i] = dx;
				L.py[i] = dy;
			}
		}		
		return(L);
	}



	public static AdjacencyRelation getComplAdj(AdjacencyRelation adj1, AdjacencyRelation adj2){
		AdjacencyRelation A;
		int i,j,n;


		if (adj1.px.length > adj2.px.length){
			A  = adj1;
			adj1 = adj2;
			adj2 = A;
		}

		A = null;
		char subset[] = new char[adj2.px.length];
		n = 0;
		for (i=0; i < adj1.px.length; i++)
			for (j=0; j < adj2.px.length; j++)
				if ((adj1.px[i] == adj2.px[i]) && (adj1.py[i] == adj2.py[i])){
					subset[j] = 1;
					n++;
					break;
				}
		n = adj2.px.length - n;

		if (n == 0) /* A1 == A2 */
			return null;

		A = new AdjacencyRelation(n);
		j=0;
		for (i=0; i < adj2.px.length; i++)
			if (subset[i] == 0){
				A.px[j] = adj2.px[i];
				A.py[i] = adj2.py[i];
				j++;
			}

		return(A);
	}


	public static AdjacencyRelation getHorizontal(int r){
		AdjacencyRelation adj;
		int i,n,dx;

		n= 2 * r + 1;
		adj = new AdjacencyRelation(n);
		i=1;
		for(dx=-r; dx<=r; dx++){
			if(dx != 0){
				adj.px[i] = dx;
				adj.py[i] = 0;
				i++;
			}
		}
		/* place the central pixel at first */
		adj.px[0] = 0;
		adj.py[0] = 0;
		return(adj);
	}


	public static AdjacencyRelation getCutBox(int w1, int w2, int h){
		AdjacencyRelation adj;
		int i,dx,dy;

		adj = new AdjacencyRelation(w1*h - w2*h);
		i=0;
		for(dy=-h/2; dy<=h/2; dy++){
			for(dx=-w1/2; dx<=w1/2; dx++){
				if (Math.abs(dx) - w2/2 > 0) {
					adj.px[i] = dx;
					adj.py[i] = dy;
					i++;
				}
			}
		}
		return(adj);
	}
	
	public static AdjacencyRelation getTranspose(AdjacencyRelation adj){
		int size = adj.px.length;
		AdjacencyRelation at = new AdjacencyRelation(size);
		for(int i=0;i<size;i++){
			at.px[i] = -adj.px[i];
			at.py[i] = -adj.py[i];
		}
		return (at);
	}

	/*
	 * com width e height par, centro deslocado para a esquerda/cima
	 */
	public static AdjacencyRelation getAssymetricalBox(int width, int height){
		AdjacencyRelation adj;
		int i,dx,dy;

		adj = new AdjacencyRelation(width*height);
		i=1;
		for(dy=-(height-1)/2; dy<=height/2; dy++){
			for(dx=-(width-1)/2; dx<=width/2; dx++){
				if ((dx != 0)||(dy != 0)){
					adj.px[i] = dx;
					adj.py[i] = dy;
					i++;
				}
			}
		}
		/* place the central pixel at first */
		adj.px[0] = 0;
		adj.py[0] = 0;
		return(adj);
	}
	
	public static AdjacencyRelation getBox(int width, int height){
		AdjacencyRelation adj;
		int i,dx,dy;

		if (width%2 == 0) width++;
		if (height%2 == 0) height++;

		adj = new AdjacencyRelation(width*height);
		i=1;
		for(dy=-height/2; dy<=height/2; dy++){
			for(dx=-width/2; dx<=width/2; dx++){
				if ((dx != 0)||(dy != 0)){
					adj.px[i] = dx;
					adj.py[i] = dy;
					i++;
				}
			}
		}
		/* place the central pixel at first */
		adj.px[0] = 0;
		adj.py[0] = 0;
		return(adj);
	}
	
	public static AdjacencyRelation getLine(int width){
		AdjacencyRelation adj;
		int i,dx,dy;

		if (width%2 == 0) width++;

		adj = new AdjacencyRelation(width+6);
		i=1;
		for(dx=-width/2;dx<=width/2;dx++){
			if (dx != 0){
				adj.px[i] = dx;
				adj.py[i] = 0;
				i++;
			}
		}
		for(dy = -1; dy<=1;dy+=2){
			for(dx=-1;dx<=1;dx++){
				adj.px[i] = dx;
				adj.py[i] = dy;
				i++;
			}
		}
		adj.px[0] = 0;
		adj.py[0] = 0;
		return(adj);	
	}
	
	public static AdjacencyRelation getDiamond(int radius){
		AdjacencyRelation adj;
		int i,dx,dy;

		adj = new AdjacencyRelation(2*radius*(radius+1)+1);
		i=1;
		for(dx=-radius;dx<=radius;dx++){
			for(dy=-radius;dy<=radius;dy++){
				if(Math.abs(dx) + Math.abs(dy) <= radius){
					if(dx != 0 || dy != 0){
						adj.px[i] = dx;
						adj.py[i] = dy;
						i++;
					}
				}
			}
		}
		adj.px[0] = 0;
		adj.py[0] = 0;
		return(adj);
	}
	

	public static AdjacencyRelation getCross(int width, int height){
		AdjacencyRelation adj;
		int i,dx,dy;

		if (width%2 == 0) width++;
		if (height%2 == 0) height++;


		adj = new AdjacencyRelation(width+height-1);
		i=1;
		for(dx=-width/2,dy=0;dx<=width/2;dx++){
			if (dx != 0){
				adj.px[i] = dx;
				adj.py[i] = dy;
				i++;
			}
		}

		for(dy=-height/2,dx=0;dy<=height/2;dy++){
			if (dy != 0){
				adj.px[i] = dx;
				adj.py[i] = dy;
				i++;
			}
		}


		/* place the central pixel at first */
		adj.px[0] = 0;
		adj.py[0] = 0;
		return(adj);	
	}

	public static AdjacencyRelation getVertical(int r){
		AdjacencyRelation adj;
		int i,n,dy;

		n=2*r+1;
		adj = new AdjacencyRelation(n);
		i=1;
		for(dy=-r;dy<=r;dy++){
			if(dy!=0){//if (i != r){
				adj.px[i] = 0;
				adj.py[i] = dy;
				i++;
			}
		}
		/* place the central pixel at first */
		adj.px[0] = 0;
		adj.py[0] = 0;
		return(adj);
	}


	public static AdjacencyRelation getFastCircular(double raio) {

		int i, n, dx, dy, r0, r2, auxX, auxY, i0 = 0;
		n = 0;
		r0 = (int) raio;
		r2 = (int) (raio * raio);
		for (dy = -r0; dy <= r0; dy++) {
			for (dx = -r0; dx <= r0; dx++) {
				if (((dx * dx) + (dy * dy)) <= r2) {
					n++;
				}
			}
		}
		AdjacencyRelation adj = new AdjacencyRelation(n);

		i = 0;
		for (dy = -r0; dy <= r0; dy++) {
			for (dx = -r0; dx <= r0; dx++) {
				if (((dx * dx) + (dy * dy)) <= r2) {
					adj.px[i] =dx;
					adj.py[i] =dy;
					if ((dx == 0) && (dy == 0))
						i0 = i;
					i++;
				}
			}
		}

		/* place central pixel at first */
		auxX = adj.px[i0];
		auxY = adj.py[i0];
		adj.px[i0] = adj.px[0];
		adj.py[i0] = adj.py[0];

		adj.px[0] = auxX;
		adj.py[0] = auxY;

		return (adj);
	}

	public static AdjacencyRelation getCircular(double raio) {

		int i, j, k, n, dx, dy, r0, r2, i0 = 0;
		n = 0;
		r0 = (int) raio;
		r2 = (int) (raio * raio);
		for (dy = -r0; dy <= r0; dy++)
			for (dx = -r0; dx <= r0; dx++)
				if (((dx * dx) + (dy * dy)) <= r2)
					n++;

		AdjacencyRelation adj = new AdjacencyRelation(n);

		i = 0;
		for (dy = -r0; dy <= r0; dy++) {
			for (dx = -r0; dx <= r0; dx++) {
				if (((dx * dx) + (dy * dy)) <= r2) {
					adj.px[i] =dx;
					adj.py[i] =dy;
					if ((dx == 0) && (dy == 0))
						i0 = i;
					i++;
				}
			}
		}

		double aux;
		double da[] = new double[n];
		double dr[] = new double[n];

		/* Set clockwise */
		for (i = 0; i < n; i++) {
			dx = adj.px[i];
			dy = adj.py[i];
			dr[i] = Math.sqrt((dx * dx) + (dy * dy));
			if (i != i0) {
				da[i] = (Math.atan2(-dy, -dx) * 180.0 / Math.PI);
				if (da[i] < 0.0)
					da[i] += 360.0;
			}
		}
		da[i0] = 0.0;
		dr[i0] = 0.0;

		/* place central pixel at first */
		aux = da[i0];
		da[i0] = da[0];
		da[0] = aux;

		aux = dr[i0];
		dr[i0] = dr[0];
		dr[0] = aux;

		int auxX, auxY;
		auxX = adj.px[i0];
		auxY = adj.py[i0];
		adj.px[i0] = adj.px[0];
		adj.py[i0] = adj.py[0];

		adj.px[0] = auxX;
		adj.py[0] = auxY;


		/* sort by angle */
		for (i = 1; i < n - 1; i++) {
			k = i;
			for (j = i + 1; j < n; j++)
				if (da[j] < da[k]) {
					k = j;
				}
			aux = da[i];
			da[i] = da[k];
			da[k] = aux;
			aux = dr[i];
			dr[i] = dr[k];
			dr[k] = aux;

			auxX = adj.px[i];
			auxY = adj.py[i];
			adj.px[i] = adj.px[k];
			adj.py[i] = adj.py[k];

			adj.px[k] = auxX;
			adj.py[k] = auxY;
		}

		/* sort by radius for each angle */
		for (i = 1; i < n - 1; i++) {
			k = i;
			for (j = i + 1; j < n; j++)
				if ((dr[j] < dr[k]) && (da[j] == da[k])) {
					k = j;
				}
			aux = dr[i];
			dr[i] = dr[k];
			dr[k] = aux;

			auxX = adj.px[i];
			auxY = adj.py[i];
			adj.px[i] = adj.px[k];
			adj.py[i] = adj.py[k];

			adj.px[k] = auxX;
			adj.py[k] = auxY;

		}

		return (adj);
	}


	public static AdjacencyRelation getKAdjacency(){
		AdjacencyRelation A;
		A = new AdjacencyRelation(4);
		A.px[0] = 0; A.py[0] = -1;
		A.px[1] = 1; A.py[1] = -1;
		A.px[2] = 0; A.py[2] = +1;
		A.px[3] = +1; A.py[3] = +1;
		return(A);
	}

	// 
	public static AdjacencyRelation maxtreeAdjacency(int n){
		AdjacencyRelation A = new AdjacencyRelation(n);
		return A;
	}

	public void set(int px, int py, int i){
		this.px[i] = px;
		this.py[i] = py;
	}
}
