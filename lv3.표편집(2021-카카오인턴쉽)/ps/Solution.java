package ps;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class Solution {
  List<User> list = new LinkedList<>();
  Stack<User> trace = new Stack();
  int cursor = -1;

  public class User {
    int orgIndex;
    boolean isDeleted;

    public User(int orgIndex, boolean isDeleted) {
      this.orgIndex = orgIndex;
      this.isDeleted = isDeleted;
    }
  }

  private void selectUp(int u) {
    for (int i = 0; i < u;) {
      User user = list.get(--cursor);
      if (!user.isDeleted) {
        i++;
      }
    }
  }

  private void selectDown(int d) {
    for (int i = 0; i < d;) {
      User user = list.get(++cursor);
      if (!user.isDeleted) {
        i++;
      }
    }
  }

  private void delete() {
    User user = list.get(cursor);
    user.isDeleted = true;
    trace.add(user);

    boolean isMove = false;
    int origin = cursor;

    while (true) {
      if (cursor + 1 >= list.size()) break;
      User nextUser = list.get(++cursor);
      if (!nextUser.isDeleted) {
        isMove = true;
        break;
      }
    }

    if (!isMove) {
      cursor = origin;
      while (true) {
        User nextUser = list.get(--cursor);
        if (!nextUser.isDeleted) {
          isMove = true;
          break;
        }
      }
    }
  }

  private void undo() {
    User user = trace.pop();
    user.isDeleted = false;
  }

  public String solution(int n, int k, String[] cmds) {
    cursor = k;

    for (int i = 0; i < n; i++) {
      list.add(new User(i, false));
    }

    for (String cmd : cmds) {
      String[] keyword = cmd.split(" ");
      String code = keyword[0];
      Integer digit = -1;

      switch (code) {
        case "U":
          digit = Integer.parseInt(keyword[1]);
          selectUp(digit);
          break;
        case "D":
          digit = Integer.parseInt(keyword[1]);
          selectDown(digit);
          break;
        case "C":
          delete();
          break;
        case "Z":
          undo();
          break;
      }
    }

    String answer = "";

    for (User user: list) {
      if (user.isDeleted) {
        answer = answer + "X";
      } else {
        answer = answer + "O";
      }
    }

    return answer;
  }
}