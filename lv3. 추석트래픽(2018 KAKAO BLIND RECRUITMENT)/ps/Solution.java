import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Solution {

  List<Task> logs = new ArrayList<>();

  public class Task {
    Timestamp start;
    Timestamp end;

    public Task(Timestamp start, Timestamp end) {
      this.start = start;
      this.end = end;
    }
  }

  private void initialLogs( String[] lines ) {
    for (String line : lines) {
      String[] query = line.split(" ");

      String strEnd = query[0] + " " + query[1];
      String strDuration = query[2];

      Timestamp end = Timestamp.valueOf(strEnd);

      strDuration = strDuration.substring(0, strDuration.length() - 1);
      long duration = (new Double(Double.parseDouble(strDuration) * 1000 - 1)).longValue();
      // System.out.println(duration);
      Timestamp start = new Timestamp(end.getTime() - duration);

      logs.add(new Task(start, end));
    }
  }

  private void sort() {
    Collections.sort(logs, new Comparator<Task>() {
      public int compare(Task t1, Task t2) {
        Long t1End = t1.end.getTime();
        Long t2End = t2.end.getTime();

        Long t1Start = t1.start.getTime();
        Long t2Start = t2.start.getTime();

        if (t1End == t2End) {
          return t1Start.compareTo(t2Start);
        }

        return t1End.compareTo(t2End);
      }
    });
  }

  public int solution(String[] lines) {
    int answer = 0;
    initialLogs(lines);

    // sort();

    for (int i = 0; i < logs.size(); i++) {
      int numTask = 1;
      Task logi = logs.get(i);

      for (int j = i + 1; j < logs.size(); j++) {
        Task logj = logs.get(j);
        if (logi.end.getTime() >= logj.start.getTime() - 1000) {
          numTask++;
        }
      }
      answer = Math.max(answer, numTask);
    }

    return answer;
  }
}