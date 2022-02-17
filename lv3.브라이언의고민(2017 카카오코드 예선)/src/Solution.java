import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Solution {
    Set<String> dup = new HashSet<>();
    Map<Character, List<Integer>> signIdxMap;


    class DecrpytException extends RuntimeException {
        public DecrpytException(String msg) {
            super(msg);
        }
    }

    private Map<Character, List<Integer>> findSignIdxMap(char[] chArr) {
        Map<Character, List<Integer>> signIdxMap = new HashMap<>();

        for (int idx = 0; idx < chArr.length; idx++) {
            char ch = chArr[idx];
            if (!signIdxMap.containsKey(ch)) {
                signIdxMap.put(ch, new ArrayList<>());
            }

            signIdxMap.get(ch).add(idx);
        }

        return signIdxMap;
    }

    private boolean isAllUpper(String sentence) {
        return isAllUpper(sentence.toCharArray());
    }

    private boolean isAllUpper(char[] chArr) {
        for (char ch : chArr) {
            if (!Character.isUpperCase(ch)) {
                return false;
            }
        }
        return true;
    }

    private boolean isEvenSeperator(List<Integer> seperatorIndex) {
        // AbAbAbAbAbA
        if (seperatorIndex.size() <= 1)
            return true;

        Integer prev = seperatorIndex.get(0);

        for (int i = 1; i < seperatorIndex.size(); i++) {
            Integer curr = seperatorIndex.get(i);
            if (curr - prev != 2)
                return false;
            prev = curr;
        }

        return true;
    }

    private boolean isCaseFirst(String subSentence) {
        char sp = subSentence.charAt(1);
        List<Integer> spIndex = signIdxMap.get(sp);

        if (!isEvenSeperator(spIndex)) {
            return false;
        }

        Integer stIndex = spIndex.get(0) - 1;
        Integer edIndex = spIndex.get(spIndex.size() - 1) + 1;
        Integer length = (edIndex - stIndex + 1);

        if (length != subSentence.length()) {
            return false;
        }
        return true;

    }

    private boolean hasBlank(char[] chArr) {

        for (char ch: chArr) {
            if (ch == ' ') return true;
        }


        return false;
    }

    public String parse(String sentence, int stIdx) throws DecrpytException, NullPointerException {
        if (stIdx >= sentence.length()) return "";

        char[] chArr = sentence.toCharArray();
        if (hasBlank(chArr)) {
            throw new DecrpytException("blank");
        }
        signIdxMap = findSignIdxMap(chArr);
        List<String> words = new ArrayList<>();

        char fCh = sentence.charAt(stIdx);

        if (Character.isLowerCase(fCh)) {
            List<Integer> signIndex = signIdxMap.get(fCh);
            signIdxMap.remove(fCh);

            if (signIndex.size() == 2) {
                // case 2
                String subSentence = sentence.substring(signIndex.get(0) + 1, signIndex.get(1));
                if (subSentence.length() > 0 && isAllUpper(subSentence)) {
                    // aBBBa => BBB
                    words.add(subSentence);
                } else if (subSentence.length()>= 3) {
                    // aBcBcBa => BcBcB => BBB
                    if (isCaseFirst(subSentence)) {
                        // BaBaB => BBB
                        words.add(subSentence.replaceAll("[a-z]", ""));
                    } else {
                        throw new DecrpytException("aBcca");
                    }
                } else {
                    // aBca
                    throw new DecrpytException("aBca");
                }

            } else {
                // aBBBBaCa
                throw new DecrpytException("aBBBBaCa");
            }

            // aBcBcBaXXXXXX => BBB parse("XXXXXX")
            Integer lastSignIndex = signIndex.get(signIndex.size() - 1);
            words.add(parse(sentence, lastSignIndex + 1));
        } else {
            if (sentence.length() - stIdx == 1) {
                // A
                words.add(sentence.substring(stIdx, stIdx + 1));
            } else {
                char sCh = sentence.charAt(stIdx + 1);

                if (Character.isUpperCase(sCh)) {
                    // AAABaBaB => AAA parse(BaBaB)
                    int i = 1;
                    for (; i + stIdx < chArr.length; i++) {
                        char ch = chArr[stIdx + i];
                        if (!Character.isUpperCase(ch)) {
                            if (signIdxMap.get(ch).size() == 2) {
                                words.add(sentence.substring(stIdx, stIdx + i));
                                words.add(parse(sentence, stIdx + i));
                                break;
                            }
                            words.add(sentence.substring(stIdx, stIdx + i - 1));
                            words.add(parse(sentence, stIdx + i - 1));
                            break;
                        }
                    }

                    if (i + stIdx == chArr.length) {
                        words.add(sentence.substring(stIdx));
                    }
                } else {
                    // AaBaA
                    List<Integer> signIndex = signIdxMap.get(sCh);

                    if (signIndex.size() == 2) {
                        // AaBa => A aBa => A B
                        signIdxMap.remove(sCh);
                        words.add(sentence.substring(stIdx, stIdx + 1));
                        words.add(parse(sentence, stIdx + 1));
                    } else {
                        // AbAbAXXXX => AAA parse(XXXX)
                        Integer endIndex = signIndex.get(signIndex.size() - 1) + 1;
                        String word = sentence.substring(stIdx, endIndex + 1);
                        if (!isCaseFirst(word)) {
                            // AbAbAb
                            throw new DecrpytException("AbAbAb");
                        }
                        signIdxMap.remove(sCh);
                        words.add(word.replaceAll("[a-z]", ""));
                        words.add(parse(sentence, endIndex + 1));
                    }
                }
            }
        }

        // System.out.println(String.join(" ", words).trim());
        return String.join(" ", words).trim();
    }

    public String solution(String sentence) {
        try {
            return parse(sentence, 0);
        } catch (DecrpytException e) {
            System.out.println("error : " + e.getMessage());
            return "invalid";
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
            return "invalid";
        }
    }
}