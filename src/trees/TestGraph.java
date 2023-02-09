package trees;

import java.io.File;

import image.GrayScaleImage;
import structs.Node;
import utils.ImageBuilder;
import window.WindowImages;

public class TestGraph {
	public static void printTreeStatistics(long t0, long tf, GrayScaleImage f, ComponentGraph cg){
		System.out.print("Total: " + (tf-t0)/1000.0 + "s");
		System.out.print(", Size = " + f.getSize() + " (" + f.getWidth() + " x " + f.getHeight() + ")");
		System.out.println(", numNodes = " + cg.getNumNodes());
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		File file1 = ImageBuilder.windowOpenFile();
		GrayScaleImage f = (GrayScaleImage) ImageBuilder.openGrayImage(file1);

		WindowImages.show(f, "input");
		
		long t0 = System.currentTimeMillis();
		ComponentGraph cg = new ComponentGraph(f, Math.max(f.getWidth(), f.getHeight()));
		cg.firstParent();
		cg.buildFirstGraph();
		cg.computeFirstAttributes();
		long tf = System.currentTimeMillis();
		System.out.println("T 1:\t" + (tf-t0)/1000.0 + "s\t" + cg.getNumNodes());
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
				mult2 *= 2;
				System.out.println(i + ":\t" + (tf-t1)/1000.0 + "s\t" + (tf-t0)/1000.0 + "s\t" + cg.getNumNodes());

			}
		}

//		cg.printGraph(0, cg.getRoot());
		Node current = cg.getRoot();
//		showNodeSubtree(cg, f, current);
		long tff = System.currentTimeMillis();
		System.out.println("Saving images: " + (tff-tf)/1000.0 + "s");

		
		printTreeStatistics(t0, tff, f, cg);
	}
	public static void showNodeSubtree(ComponentGraph cg, GrayScaleImage f, Node node){
		for(Node child: node.children){
			if(!child.marked){
				node.marked = true;
				showNodeSubtree(cg, f, child);
			}
		}
		GrayScaleImage recons = cg.reconstructNode(node, f.getWidth(), f.getHeight()); 
//		WindowImages.show(recons, (1+node.i/100) + "_" + node.id);
//		ImageBuilder.saveImage(recons, new File("out/node" + (1+node.i/50) + "_" + node.id + ".png"));
	}
}
