# 2021-카카오 인턴쉽 | 표편집 풀이

## 문제

[2021 카카오 채용연계형 인턴십 > 
표 편집
](https://programmers.co.kr/learn/courses/30/lessons/81303?language=java)

## 해설

![table_1](./figures/table_1.png)

이번 문제는 자료구조 중에서 Array 와 List의 차이를 알고 사용할 수 있는지 묻는 문제 였습니다.
그 중에서도 List (LinkedList)의 특징을 이용해서 최적화를 할수있는지 물어보는 문제였습니다.

### Array와 List의 차이

Array는 임의 조회에 유리하고, List는 순차적 조회에서 삭제와 삽입에 유리합니다.

예를 들어, 2번째에 있는 어피치에 있는 정보를 가지고 오는것은 Array에 경우 O(1)의 속도로 조회할 수 있습니다. 하지만 List의 경우 O(N)의 속도가 걸려 조회가 가능합니다.

#### Array에서의 조회

```java
Node apeach = array[2];
```

#### List에서의 조회

```java
Node apeach = linkedlist;

while(linkedlist != null) {
  if (apeach.name == "어피치" ) break;
  apeach = linkedlist.next;
}
```

조회한 어피치에 정보를 삭제하는 것은 Array의 경우 O(N)의 속도로 삭제가 가능한 반면 List의 경우 O(1) 의 속도로 삭제가 가능합니다.

#### Array에서의 삭제

```java
for (int i = 2 ; i < array.size(); i++) {
  array[i] = array[i + 1];
}
```

#### List에서의 삭제

```java
Node prevNode = apeach.prev;
Node nextNode = apeach.next;

prevNode.next = nextNode;
nextNode.prev = prevNode;
```

### 왜 List (LinkedList) 일까요?

앞서 말한 것과 같이 Array는 임의 조회에 유리하고, List는 삭제와 삽입에 유리합니다. 이 점을 염두하고 문제의 제약 사항을 살펴 봅시다

```text
- "U X": 현재 선택된 행에서 X칸 위에 있는 행을 선택합니다.
- "D X": 현재 선택된 행에서 X칸 아래에 있는 행을 선택합니다.
- "C" : 현재 선택된 행을 삭제한 후, 바로 아래 행을 선택합니다. 단, 삭제된 행이 가장 마지막 행인 경우 바로 윗 행을 선택합니다.
- "Z" : 가장 최근에 삭제된 행을 원래대로 복구합니다. 단, 현재 선택된 행은 바뀌지 않습니다.
```

U, D, C, Z 네가지 명령어로만 표에 대한 접근이 가능합니다. 그리고 이 네가지 경우 모두 임의 조회가 필요하지 않고, 삭제와 복구 (삽입)에 대한 요구가 있습니다. 이런 정보를 토대로 해당 문제는 Array보다 LinkedList를 이용하는 것이 더 유리한 것을 알수 있었습니다.

또한 삭제된 노드를 복구시키는 것을 Array로 구현할 경우 정합성에 문제가 생길 수 있으나,
LinkedList로 구현 할 경우 노드 삭제시 해당 노드의 전/후 노드를 기억시킨다면 복구시에도 정합성에 문제 없이 복구가능하다는 점도 있었습니다.

## 입력

해설에서 설명한 바와 같이 LinkedList를 이용합니다. LinkedList에 데이터를 담기위해 리스트의 요소가 될 Node를 먼저 정의합니다. Node에는 이전 Node, 이후 Node그리고 처음 자리잡을때 번호를 기억할 index로 구성합니다. ( 출력에 이름과 관련된 요구사항이 없으므로 이름 필드는 생략합니다. )

```java
public class Node {
  int index;
  Node prev = null;
  Node next = null;

  public Node(int index) {
    this.index = index;
  }
}
```

문제 풀이에 사용할 list를 선언하고 값을 초기화할 함수를 선언하고 작성합니다. 노드의 개수 n개만큼 초기화하고 나면 전후 노드를 연결하도록 합니다.

```java
List<Node> list = new ArrayList<>();

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
```

## 풀이

현재 선택된 행을 저장할 노드인 curNode를 정의하고 k번째 노드로 초기화 합니다.

```java
Node curNode = null;

curNode = list.get(k);
```

명령어를 파싱하여, 각 라인에 있는 명령을 수행합니다.

### 파싱

```java
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
```

### U | X칸 위에 있는 행 선택하기

```java
  private void selectUp(int n) {
    while(n-- > 0){
      curNode = curNode.prev;
    }
  }
```

### D | X칸 아래에 있는 행 선택하기

```java
  private void selectDown(int n) {
    while (n-- > 0) {
      curNode = curNode.next;
    }
  }
```

### C | 현재행 삭제하기

- 삭제, 복구는 마지막에 있는 원소만 사용하니 Stack구조로 정의합니다..
- 삭제시 첫번째와 마지막에 있는 노드는 따로 처리하였습니다.

```java
Stack<Node> trace = new Stack<>();

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
```

### Z | 최근에 삭제된 행을 원래대로 복구하기

```java
  private void undo() {
    Node undoNode = trace.pop();

    Node prevNode = undoNode.prev;
    Node nextNode = undoNode.next;

    if (prevNode != null) prevNode.next = undoNode;
    if (nextNode != null) nextNode.prev = undoNode;
  }
```

## 출력

전체 노드중에서 삭제된 노드의 위치에만 X표시를 해두면 되니, "O"로 이루어진 String에 삭제된 노드들의 index에 "X"표시를 해두어서 출력하였습니다.

```java
    StringBuilder answer = new StringBuilder("O".repeat(n));

    while(!trace.empty()) {
      Node node = trace.pop();
      answer.setCharAt(node.index, 'X');
    }
```

## 전체 코드

```java
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
```
