import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Analysis {

	/**
	 * id,Node
	 */
	public static HashMap<Integer, Node> nodes;
	//public static int min=9999999;
	
	
	/**
	 * src←→tar
	 */
	private Set<String> edges;
 
	public Analysis() {
		nodes = new HashMap<Integer, Node>();
		edges = new HashSet<String>();
		readFile("com-dblp.ungraph.txt");
		//calAverageDegree();
		calAverageClusteringCoefficient();
		//calDiameter();
		//writeJSON("test.json");
		//draw();
		//calRelief();
		
		//System.out.println(nodes.size());
	}

	public void readFile(String path) {

		FileReader fr = null;
		BufferedReader br = null;

		try {

			fr = new FileReader(path);
			br = new BufferedReader(fr);

			/* 前四行資訊 */
			br.readLine();
			br.readLine();
			br.readLine();
			br.readLine();

			while (br.ready()) {

				String line = br.readLine();
				//System.out.println(line);
				String[] split = line.split("	");

				int srcID = Integer.parseInt(split[0]);
				int tarID = Integer.parseInt(split[1]);
				

				initNodes(srcID,tarID);
				initNodes(tarID,srcID);
				edges.add(srcID +"←→"+tarID);
				
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void writeJSON(String path){
		
		FileWriter fw = null;
		try {
			fw = new FileWriter(path, true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedWriter bw = new BufferedWriter(fw);
		
		try {
			bw.write("{");
			bw.newLine();
			bw.write("\"nodes\":[");
			bw.newLine();
			
			for(int node:nodes.keySet()){
				bw.write("{\"name\":\""+node+"\",\"group\":"+1+"},");
				bw.newLine();
			}
			
			
			bw.write("],\"links\":[");
			bw.newLine();
			
			for(int node:nodes.keySet()){
				try{
				for(int link:nodes.get(node).getLinkNodeIDs()){
					bw.write("{\"source\":"+node+",\"target\":"+link+",\"value\":1},");
					bw.newLine();
				}
				}catch(NullPointerException e){
					
				}
			}
			
			bw.write("]}");
			bw.newLine();
				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		try {
			bw.flush();
			bw.close();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void draw(){
		
		HashMap<Integer, Integer> rowdata = new HashMap<Integer, Integer>();
		
		for(int id:nodes.keySet()){
			
			int degree = nodes.get(id).getDegree();
			//System.out.println(degree);
			
			if(rowdata.containsKey(degree))
				rowdata.put(degree, rowdata.get(degree)+1);
			else
				rowdata.put(degree, 1);
			
		}
		System.err.println(rowdata);
		//DegreeDistribution degreeDistribution = new DegreeDistribution(rowdata);
			
	}
	
	 private static class quickCal implements Runnable {
		 private int start;
		 private int end;
		 public static int [] min = {9999999};
		 public quickCal(int start,int end){
			 this.start = start;
			 this.end = end;
		 }
		 
		 public void run() {
			 
			 int maxShortPath = 0;
				
				for(int srcID=start; srcID<=end;srcID++){
					for(int tarID=srcID+1; tarID <=317080 ;tarID++){
						min[0]=9999999;
						nodes.get(0).getShortPath(nodes,tarID,new HashSet<Integer>(),min);
						if(min[0]!=9999999){
							System.out.println(min[0]);
							maxShortPath = Math.max(min[0],maxShortPath);
						}
						System.out.println("==========");
					}
				}

				System.out.println("Diameter: " + maxShortPath);
				
				/*
				nodes.get(0).getShortPath(nodes,3,new HashSet<Integer>());
				System.out.println(min);
				System.out.println("==========");
				*/
			 
		 }
	 
	 }
	
	public void calDiameter(){
		Thread th1 = new Thread(new quickCal(0,317080/4));
		Thread th2 = new Thread(new quickCal(317080/4,317080/2));
		Thread th3 = new Thread(new quickCal(317080/2,317080*3/4));
		Thread th4 = new Thread(new quickCal(317080*3/4,317080));
		
		th1.start();
		th2.start();
		th3.start();
		th4.start();
		
		
	}
	
	public void calRelief(){
		
		//Set<Integer> cluster = new HashSet<Integer>();
		
		for(int node:nodes.keySet()){
			nodes.get(node).maxCluster(nodes,new HashSet<Integer>());
			System.out.println("=======");
		}
			
			
	}

	
	public void calAverageDegree(){
		
		int sum_degree = 0;
		
		for(int id:nodes.keySet())
			sum_degree+=nodes.get(id).getDegree();
		
		//System.out.println(nodes.get(0).getDegree());
		System.out.println("Average Degree: "+(float)sum_degree/317080);
		
		
	}
	
	
	public void calAverageClusteringCoefficient(){
		
		float sum_clusterCoeff = 0;
		long current = System.currentTimeMillis();
				
		for(int id:nodes.keySet()){
			int degree=nodes.get(id).getDegree();
			int linkNum = 0;
			List<Integer> LinkNodeIDs=nodes.get(id).getLinkNodeIDs();
			int size = LinkNodeIDs.size();
			
			for(int i=0 ; i<size ; i++){
				for(int j=i+1 ; j<size ; j++){
					if(isLink(LinkNodeIDs.get(i), LinkNodeIDs.get(j)))
						linkNum=linkNum+1;
				}
			}
			
			/*NaN*/
			if(C(degree)==0)
				sum_clusterCoeff += 0;
			else
				sum_clusterCoeff += linkNum/C(degree);
		}
		
		System.out.println(System.currentTimeMillis()-current);
		
		//System.out.println("Average clustering coefficient: " + sum_clusterCoeff/317080/2);
	}
	
	
	public boolean isLink(int srcID,int tarID){
		
		if(edges.contains(srcID+"←→"+tarID) || edges.contains(tarID+"←→"+srcID))
			return true;
		else 
			return false;
		
	}
	
	/**
	 * c(n,2)
	 * @param n
	 * @return
	 */
	public float C(int n){
		
		return n*(n-1)/2;
	}
	
	public void initNodes(int nodeID,int linkNodeID){
		
		if (!nodes.containsKey(nodeID))
			nodes.put(nodeID, new Node(nodeID,linkNodeID));
		else if(nodes.containsKey(nodeID))
			nodes.get(nodeID).addLinkNodeID(linkNodeID);
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Analysis();
		//System.out.println(0/5.2);
	}

}
