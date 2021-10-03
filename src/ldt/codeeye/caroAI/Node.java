package ldt.codeeye.caroAI;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Node {
	Point p = null;
	int value;
	Node parent = null;
	List<Node> children = new ArrayList<>();

	public Node() { }
}
