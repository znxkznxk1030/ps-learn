package ps;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class Solution {

  List<Node> list = new ArrayList<>();
  Stack<Node> trace = new Stack<>();
  Node curNode = null;
  
  public class Node {
    int index;
    Node prev = null;
    Node next = null;
    
    public Node(int index) {
      this.index = index;
    }

    public Node(int index, Node prev, Node next) {
      this.index = index;
      this.prev = prev;
      this.next = next;
    }
  }

  private void initializeList(int n) {
    for (int i = 0; i < n; i++) {
      list.add(new Node(i));
    }

    list.get(0).next = list.get(1);
    list.get(list.size() - 1).prev = list.get(list.size() - 2);

    for (int i = 1; i < n - 1; i++) {
      list.get(i).prev = list.get(i - 1);
      list.get(i).next = list.get(i + 1);
    }
  }

  private void selectUp(int n) {
    while(n-- > 0){
      curNode = curNode.prev;
    }
  }

  private void selectDown(int n) {
    while (n-- > 0) {
      curNode = curNode.next;
    }
  }

  private void delete() {
    trace.push(curNode);
    Node prevNode = curNode.prev;
    Node nextNode = curNode.next;
    if (nextNode != null && prevNode != null) {
      prevNode.next = nextNode;
      nextNode.prev = prevNode;
      curNode = nextNode;
    } else if (nextNode == null && prevNode != null) {
      prevNode.next = nextNode;
      curNode = prevNode;
    } else if (nextNode != null && prevNode == null) {
      nextNode.prev = prevNode;
      curNode = nextNode;
    }
  }

  private void undo() {
    Node undoNode = trace.pop();

    Node prevNode = undoNode.prev;
    Node nextNode = undoNode.next;

    if (prevNode != null) prevNode.next = undoNode;
    if (nextNode != null) nextNode.prev = undoNode;
  }

  public String solution(int n, int k, String[] cmds) {
    initializeList(n);

    curNode = list.get(k);

    for (String cmd: cmds) {
      String[] keywords = cmd.split(" ");
      int num = -1;
      
      switch (keywords[0]) {
        case "U":
        num = Integer.parseInt(keywords[1]);
        selectUp(num);
        break;
        case "D":
        num = Integer.parseInt(keywords[1]);
        selectDown(num);
        break;
        case "C":
        delete();
        break;
        case "Z":
        undo();
        break;
      }
    }


    StringBuilder answer = new StringBuilder("O".repeat(n));

    while(!trace.empty()) {
      Node node = trace.pop();
      answer.setCharAt(node.index, 'X');
    }

    return answer.toString();
  }
}