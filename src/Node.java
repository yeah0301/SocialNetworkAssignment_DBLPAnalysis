import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;


public class Node {
	
	private int id;
	public int degree;
	private LinkedList<Integer> linkNodeIDs = new LinkedList<Integer>();
	private HashMap<Integer, Integer> ShortPathtoTarID = new HashMap<Integer, Integer>();
	
	public Node(int id){
		this.id = id;
		this.degree=1;
	}
	
	public Node(int id,int linkNodeID){
		this.id = id;
		addLinkNodeID(linkNodeID);
	}
	
	public void addDegree(){
		this.degree++;
	}
	
	public int getID(){
		return this.id;
	}
	
	public int getDegree(){
		return this.degree;
	}
	
	public LinkedList<Integer> getLinkNodeIDs(){
		return this.linkNodeIDs;
	}
	
	public void addLinkNodeID(int id){
		addDegree();
		this.linkNodeIDs.add(id);
	}
	
	public int getShortPathtoTarID(int tarID){
		return this.ShortPathtoTarID.get(tarID);
	}
	
	public void getShortPath(HashMap<Integer, Node> nodes,int tarID,Set<Integer> visits,int[] min){
		
		/*Does list contain tarID by checking list of srcID link tarID*/
		if(linkNodeIDs.contains(tarID)){
			//System.out.println(tarID);
			int size = visits.size()+1;
			//System.out.println("size:"+size);
			min[0]=Math.min(min[0],size);
		}
		/*not contain*/
		else{
			for(int linkID:this.linkNodeIDs){
				if(!visits.contains(linkID)){
					visits.add(linkID);
					//System.out.print(linkID+"->");
					nodes.get(linkID).getShortPath(nodes, tarID,visits,min);
				}
			}
		}
		
		
	}
	
	public void maxCluster(HashMap<Integer, Node> nodes,Set<Integer> visit){
		
		if(linkNodeIDs.isEmpty())
			System.out.println(visit.size());
		
		else{
			for(int linkid:linkNodeIDs){
				visit.add(linkid);
				nodes.get(linkid).maxCluster(nodes, visit);
			}
		}
	}
}
