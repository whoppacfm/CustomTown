package com.mygdx.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.xml.crypto.dsig.keyinfo.KeyValue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.BinaryHeap;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.BinaryHeap.Node;

/*
 	CAstar astar;
	boolean[] map;

	map = new boolean[100 * 100];
	map[5 + 5 * 100] = true;
	map[6 + 5 * 100] = true;
	map[7 + 5 * 100] = true;
	map[7 + 6 * 100] = true;
	map[8 + 6 * 100] = true;

	astar = new CAStar(100, 100) {
		protected boolean isValid (int x, int y) {
			return !map[x + y * 100];
		}
	};

	IntArray path = astar.getPath(startX, startY, targetX, targetY);
	int x = path.get(i);
	int y = path.get(i + 1);
*/


public class CAStar {
	
	private class KV
	{
		public int key;
		public int value;
	}
		
	public Boolean isRoad;
	public Boolean isFootpath;
	public Boolean isAdr;
	
	private final int width, height;
	public final BinaryHeap<PathNode> open;
	public ArrayList<PathNode> drawOpenlist;
	
	//private final PathNode[] nodes;
	public PathNode[] nodes;
	int runID;
	//private final IntArray path = new IntArray();
	private IntArray path;
	private int targetX, targetY;
	private long gameWorldRenderStep;
	private int pathfindingCount;

	public CWorldObject target;
	private List<KV> priorityList;
	
	//public CWorld gameWorld;
	public CTown town;
	
	public void dispose()
	{
		town=null;
		target=null;
	}
	
	public CAStar (int width, int height, CTown t1) {
		isAdr=false;
		isRoad=false;
		isFootpath=false;
		
		drawOpenlist=new ArrayList<CAStar.PathNode>();
		priorityList=new ArrayList<KV>();
		town = t1;
		gameWorldRenderStep=0;
		gameWorldRenderStep=town.gameWorld.renderUniqueNumber;
		this.width = width;
		this.height = height;
		open = new BinaryHeap<PathNode>(width * 4, false);
		nodes = new PathNode[width * height];
	}
	
	/** Returns x,y pairs that are the path from the target to the start. */
	public IntArray getPath (int startX, int startY, int targetX, int targetY, Boolean onlyEmptyTarget, CWorldObject targetObject, CWorldObject sourceObject) {
		
		//Gdx.app.debug("", "start: "+isValid(startX, startY) + "startx: " + startX + ", startY: " + startY);
		//		Gdx.app.debug("", "start x+1: "+isValid(startX+1, startY));
		//		Gdx.app.debug("", "start x+2: "+isValid(startX+2, startY));
		
		target=targetObject;
		drawOpenlist.clear();
		if(town.bDebugLogging)
			Gdx.app.debug("getPath", "1");
		
		//if(targetObject!=null)
		//	Gdx.app.debug("target", "1" + targetObject.theobject.objectName);
				
		//Max pathfindings pro Renderstep
		//		if(gameWorldRenderStep==gameWorld.renderUniqueNumber && pathfindingCount>gameWorld.maxPathfindingsPerStep)
		//		{
		//			if(gameWorld.town.bDebugLogging)
		//				Gdx.app.debug("getPath", "cancel (maxPathfindingsPerStep)");
		//
		//			return null;
		//		}
		//		else if(gameWorldRenderStep!=gameWorld.renderUniqueNumber)
		//		{
		//			gameWorldRenderStep = gameWorld.renderUniqueNumber;
		//			pathfindingCount=0;		
		//		}
		
		
		//Auch bei mehr als 20 Agents soll jeder mal dran kommen
		if(town.bDebugLogging)
		{
			Gdx.app.debug("getPath", "priorityList size: " + priorityList.size());
			if(sourceObject==null)
				Gdx.app.debug("getPath", "sourceobject is null");
		}
		
		if(priorityList.size()>1) //erst ab 2 agents, da logik nur ab 2 agents funktioniert
		{
			if(sourceObject!=null)
			{
				Optional<KV> kv = priorityList.stream().filter(item->item.key==sourceObject.uniqueId).findFirst();
				if(kv.isPresent())
				{
					if(kv.get().value>2)
					{
						kv.get().value=0;
						
						if(town.bDebugLogging)
							Gdx.app.debug("getPath", "goOn (priority)");
					}
					else
					{
						kv.get().value++;
						
						if(town.bDebugLogging)
							Gdx.app.debug("getPath", "cancel (priority)");
						
						return null;
					}
				}
				else
				{
					KV kv1 = new KV();
					kv1.key = sourceObject.uniqueId;
					kv1.value=1;
					priorityList.add(kv1);
				}
			}
		}
		
		//if(targetObject!=null)
		//	Gdx.app.debug("target", "2 "+ targetObject.theobject.objectName);
		
		pathfindingCount++;
		
		this.targetX = targetX;
		this.targetY = targetY;
		
		//Gdx.app.debug("getpath_0", "w: " + width + ", h: " + height + ", targetx: " + targetX + ", targetY: " + targetY);
		
		if(targetX>width || targetY>height || targetX<0 || targetY<0)
		{
			if(town.bDebugLogging)
				Gdx.app.debug("getPath", "cancel (target out of world)");
			
			return null;
		}
		
		//Gdx.app.debug("getpath_0_1", "");
		
		if(onlyEmptyTarget && (!isRoad) && (!isFootpath) && (!isAdr))
		{
			if(isValid(targetX, targetY)==null)
				return null;
		}
		
		//Gdx.app.debug("getpath_0_2", "");
		
		//path.clear();
		path = new IntArray();
		open.clear();
		
		if(targetObject!=null)
			drawOpenlist.clear();
		
		runID++;
		if (runID < 0) 
			runID = 1;
		
		//int index = startY * width + startX;
		int index = (int) (startY * (Math.sqrt(width+height)) + startX); //change für unterschiedliche breite und höhe (pathfinding auf adresse) 
		
		//Gdx.app.debug("getpath_1", "index: "+index);
		
		if(index<1 || index>nodes.length-1) //CFM20150910 Index out of bounds exception
		{
			if(town.bDebugLogging)
				Gdx.app.debug("getPath", "cancel (Index out of bounds)");
			
			return null;
		}
		
		PathNode root = null; //CFM20150910 Index out of bounds exception
		
		root = nodes[index];
		
		if (root == null) {
			root = new PathNode(0);
			root.x = startX;
			root.y = startY;
			nodes[index] = root;
		}
		
		root.parent = null;
		root.pathCost = 0;
		open.add(root, 0);
		if(targetObject!=null)
			drawOpenlist.add(root);
		
		int lastColumn = width - 1, lastRow = height - 1;
		int i = 0;
		int icount=0;
		
		//if(targetObject!=null)
		//	Gdx.app.debug("target", "5 "+ targetObject.theobject.objectName);
		
		//Gdx.app.debug("getpath start", "");
		Boolean targetFound=false;
		while (open.size > 0) {
			icount++;
			
			//Gdx.app.debug("getpath_2", "while");
			
			//if(targetObject!=null)
			//	Gdx.app.debug("target", "6 "+ targetObject.theobject.objectName);
			
//			if(icount>200) //20160123 kostet enorm performance wenn kein weg gefunden wird und artet in ruckelorgien aus
//			{
//				int dist = CHelper.getEuclidianDistance(root.x, root.y, targetX, targetY);
//				
//				if(dist<20)
//				{
//					//Gdx.app.debug("", "dist: " + dist);
//					return null;
//				}
//			}
			
			if(icount>10000) //20160123 kostet enorm performance wenn kein weg gefunden wird und artet in ruckelorgien aus
			{
				int dist = CHelper.getEuclidianDistance(root.x, root.y, targetX, targetY);
				if(dist<10)
					return null;
			}			
			
			//if(icount>100000) //irgendwann abbrechen, deadlock verhindern
			if(icount>2000000) //irgendwann abbrechen, deadlock verhindern
			{
				//if(gameWorld.town.bDebugLogging)
					//Gdx.app.debug("getPath", "cancel (deadlock)");
				
				return null;
			}
			
			PathNode node = open.pop();
			
			//Gdx.app.debug("opensize", String.valueOf(open.size) + ", nodeX: " + node.x + ", nodeY: " + node.y + ", targetY: " + targetX + ", targetY: " + targetY);
						
			if (node.x == targetX && node.y == targetY) {
				//Gdx.app.debug("getpath", "target node found");
				targetFound=true;
				while (node != root) {
					path.add(node.x);
					path.add(node.y);
					node = node.parent;
				}
				break;
			}
			
			//Road: Container kann auch stück neben straße stehen
			if(isRoad || isFootpath || isAdr) // && targetObject!=null && targetObject.theobject.editoraction.contains("container"))
			{
				//Gdx.app.debug("", "abstand_target: " + Math.abs(node.x-targetX) + "abstand_target: " + Math.abs(node.y-targetY));
				
				if (Math.abs(node.x-targetX)<2
						&& Math.abs(node.y-targetY)<2) 
				{
					targetFound=true;
					
					if(isValid(targetX+1, targetY)!=null)
					{
						path.add(targetX+1);
						path.add(targetY);
					}
					else if(isValid(targetX-1, targetY)!=null)
					{
						path.add(targetX-1);
						path.add(targetY);
					}
					else if(isValid(targetX, targetY+1)!=null)
					{
						path.add(targetX);
						path.add(targetY+1);
					}
					else if(isValid(targetX, targetY-1)!=null)
					{
						path.add(targetX);
						path.add(targetY-1);
					}
					
					while (node != root) {
						path.add(node.x);
						path.add(node.y);
						node = node.parent;
					}
					break;
				}
			}
			
			
			//if(targetObject!=null)
			//	Gdx.app.debug("target", "7 "+ targetObject.theobject.objectName);
			
			//Gdx.app.debug("getpath", "runid: " + runID);
			node.closedID = runID;
			int x = node.x;
			int y = node.y;
			if (x < lastColumn) {
				addNode(node, x + 1, y, 10, targetObject);
				if (y < lastRow) addNode(node, x + 1, y + 1, 30, targetObject); // Diagonals cost more, roughly equivalent to sqrt(2).
				if (y > 0) addNode(node, x + 1, y - 1, 30, targetObject);
			}
			if (x > 0) {
				addNode(node, x - 1, y, 10, targetObject);
				if (y < lastRow) addNode(node, x - 1, y + 1, 30, targetObject);
				if (y > 0) addNode(node, x - 1, y - 1, 30, targetObject);
			}
			if (y < lastRow) 
			{
				addNode(node, x, y + 1, 30, targetObject);
			}
			if (y > 0)
			{
				addNode(node, x, y - 1, 30, targetObject);
			}
			i++;
			
			
			
			
			//CFM20150910 Wenn alle Nodes in näherer Umgebung invalid sind muss rausgesprungen werden
			if(icount==1 && open.size==0)
			//if(icount==1 && open.size<2)
			{
				//Gdx.app.debug("opensize is 0", String.valueOf(open.size));
				
				int jump=5;
				addNode(node, x + jump, y+jump, 10, targetObject);
				addNode(node, x - jump, y-jump, 10, targetObject);
								
//				if (x < lastColumn) {
//					addNode(node, x + jump, y, 10, targetObject);
//					if (y < lastRow) addNode(node, x + jump, y + jump, 30, targetObject); // Diagonals cost more, roughly equivalent to sqrt(2).
//					if (y > 0) addNode(node, x + jump, y - jump, 30, targetObject);
//				}
//				if (x > 0) {
//					addNode(node, x - jump, y, 10, targetObject);
//					if (y < lastRow) addNode(node, x - jump, y + jump, 30, targetObject);
//					if (y > 0) addNode(node, x - jump, y - jump, 30, targetObject);
//				}
//				if (y < lastRow) addNode(node, x, y + jump, 30, targetObject);
//				if (y > 0) addNode(node, x, y - jump, 30, targetObject);				
			}
			//Gdx.app.debug("getpath open size", String.valueOf(open.size));
		}
		
		if(!targetFound)
		{
			//if(targetObject!=null)
			//	Gdx.app.debug("target", "not found "+ targetObject.theobject.objectName + ", size: " + drawOpenlist.size());
			
			//Gdx.app.debug("", "target not found");
			return null;
		}
		
		return path;
	}
	
	private void addNode (PathNode parent, int x, int y, int cost, CWorldObject targetObject) {
		//if(nodes.length>1000)
		//	nodes=new ;
		//Gdx.app.debug("getpath", "addNode: x: " + x + ", y: "+y);
		
		//Auskommentiert, da er sonst footpath wege nur findet wenn eine direkte verbindung von adresse1 zu adresse2 besteht
		//lösung über erhöhte kosten für nicht-pfad-knoten
		//if (!isValid(x, y) && (isRoad || isFootpath))
		//if (!isValid(x, y) && (isRoad))
		//{
		//	return;
		//}
		
		
		if(targetX!=x || targetY!=y)
		{
			//if (!isValid(x, y))
			if (isValid(x, y)==null)
			{
				cost+=10000;
				
				//return;
				
				//if(isRoad)
				//{
				//	return;
				//}
				
				//if(isRoad)
				//{
				//	cost+=10000;
				//}
				//else
				//{
				//	if(targetObject!=null) //Bereich anhand Objektgröße ermitteln
				//	{
				//		int betragx = targetX-x;
				//		int betragy = targetY-y;
				//		
				//		int bereichX = targetObject.width/targetObject.gameWorld.nodesize;
				//		int bereichY = targetObject.height/targetObject.gameWorld.nodesize;
						
						//CFM20150910 Problem: wenn Zielobjekt groß ist und zielpunkt in der mitte ist gibt es wegpunkte die not valid sind -> bereich definieren der valid ist
						//todo: Lösung für Problem wenn Objekt für Einwohner nicht erreichbar ist - getpath wird ständig bis zum anschlag ausgeführt  weil kein weg gefunden wird -> siehe code todo file
						//todo: Problem: einwohner rennen in objekt rein und kommen dann nicht mehr weiter weil sie von notvalid umgeben sind
				//		if((Math.abs(betragx) + Math.abs(betragy)) > bereichX+bereichY)
				//			return;
				//	}
				//	else
				//		return;
				//}
					//Gdx.app.debug("getpath not valid", "");
			}
		}
		
		int pathCost = parent.pathCost + cost;
		float score = pathCost + Math.abs(x - targetX) + Math.abs(y - targetY);
		
		int index = y * width + x;
		PathNode node = nodes[index];
		if (node != null && node.runID == runID) 
		{ // Node already encountered for this run.
			if (node.closedID != runID && pathCost < node.pathCost)
			{ // Node isn't closed and new cost is lower.
				// Update the existing node.
				open.setValue(node, score);
				//if(targetObject!=null)
				//	drawOpenlist.add(node);
				node.parent = parent;
				node.pathCost = pathCost;
			}
		} 
		else 
		{
			// Use node from the cache or create a new one.
			if (node == null) 
			{
				node = new PathNode(0);
				node.x = x;
				node.y = y;
				nodes[index] = node;
			}
			open.add(node, score);
			if(targetObject!=null)
				drawOpenlist.add(node);
			node.runID = runID;
			node.parent = parent;
			node.pathCost = pathCost;
		}
	}
	
	//protected boolean isValid (int x, int y) {
	protected CWorldObject isValid (int x, int y) {
		//return true;
		return null;
	}
	
	public int getWidth () {
		return width;
	}
	
	public int getHeight () {
		return height;
	}
	
	static public class PathNode extends Node {
		int runID, closedID, x, y, pathCost;
		PathNode parent;
		
		public PathNode (float value) {
			super(value);
		}
	}
}


