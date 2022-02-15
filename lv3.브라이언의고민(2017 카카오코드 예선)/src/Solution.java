import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Solution {
  Set<String> dup = new HashSet<>();

  public String findBracket(String sentence) {
    Map<String, Integer> hist = new HashMap<>();

    char[] chArr = sentence.toCharArray();

    for (char ch : chArr) {
      if (Character.isUpperCase(ch))
        continue;

      String key = "" + ch;
      if (hist.containsKey(key)) {
        hist.put(key, hist.get(key) + 1);
      } else {
        hist.put(key, 1);
      }
    }

    StringBuilder sb = new StringBuilder();

    for (String key : hist.keySet()) {
      if (hist.get(key) == 2) {
        sb.append(key);
      }
    }

    return sb.toString();
  }

  public String case1(String word) throws Exception {
    char[] chArr = word.toCharArray();
    char sp = '@';

    if (!Character.isUpperCase(chArr[0]))
      throw new Exception();

    if (chArr.length == 1) {
      return "" + chArr[0];
    }

    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < chArr.length; i++) {

    }

    return sb.toString();
  }

  public String parse(String sentence) throws Exception {
    String brackets = findBracket(sentence);
    String[] words = sentence.split("[" + brackets + "]");

    List<String> answer = new ArrayList<>();

    for (String word : words) {
      if (word.length() == 0)
        continue;

      answer.add(case1(word));
    }

    return String.join(" ", answer);
  }

  public String solution(String sentence) {
    try {
      return parse(sentence);
    } catch (Exception e) {
      return "invalid";
    }
  }
}