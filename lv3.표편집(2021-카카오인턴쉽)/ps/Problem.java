package ps;


public class Problem {
  public static void main(String[] args) {
    Solution s = new Solution();

    String[] cmds = {"D 2","C","U 3","C","D 4","C","U 2","Z","Z"};

    System.out.println(s.solution(8, 2, cmds));
  }
}