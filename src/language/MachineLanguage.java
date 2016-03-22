package language;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Dilip Arumugam on 3/8/16.
 */
public class MachineLanguage implements LanguageExpression {
    protected final List<String> words;
    protected static final List<String> propFuncs = Arrays.asList("agentInRoom","blockInRoom","isTeal","isTan","isBlue","isRed","isOrange");

    public MachineLanguage(List<String> words){
        this.words = words;
    }

    public static Collection<String> enumerate(int maxLength){
        List<String> ret = new ArrayList<>();
        int count = 1;
        while(count <= maxLength) {
            if(count == 1){
                ret.addAll(propFuncs);
            }
            else{
                int end = ret.size();
                Set<String> toAdd = new HashSet<>();
                for(int i=0;i < end;i++){
                    String elt = ret.get(i);
                    if(elt.split(" ").length > 1){
                        continue;
                    }
                    final int finalCount = count;
                    toAdd.addAll(ret.stream().filter(w -> w.split(" ").length == finalCount -1).map(w -> w + " " + elt).collect(Collectors.toSet()));
                }
                ret.addAll(toAdd);
            }
            count++;
        }

        Set<String> realRet = new HashSet<>();
        realRet.addAll(ret);
        realRet = realRet.stream().filter(c -> c.contains("blockInRoom") || c.contains("agentInRoom")).collect(Collectors.toSet());
        realRet = realRet.stream().filter(c -> {
            String[] words = c.split(" ");
            long agents = Arrays.stream(words).filter(w -> w.equals("agentInRoom")).count();
            return agents == 1;
        }).collect(Collectors.toSet());
        realRet = realRet.stream().filter(c -> {
            String[] words = c.split(" ");
            long agents = Arrays.stream(words).filter(w -> w.contains("is")).count();
            return agents <= 3;
        }).collect(Collectors.toSet());
        realRet = realRet.stream().map(c -> {
            String[] words = c.split(" ");
            Arrays.sort(words);
            StringBuilder sb = new StringBuilder();
            Arrays.stream(words).forEach(w -> sb.append(w + " "));
            return sb.toString().trim();
        }).collect(Collectors.toSet());

        realRet = realRet.stream().filter(c -> c.split(" ").length % 2 == 0).collect(Collectors.toSet());
        System.out.println(realRet.size() + "\n\n\n");
        return realRet;
    }

    @Override
    public List<String> getWords() {
        return this.words;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (String w : this.words) {
            sb.append(w + " ");
        }
        return sb.toString().trim();
    }

    public static void main(String[] args){
        Collection<String> enums = MachineLanguage.enumerate(6);
        System.out.println(enums);
        System.out.println(enums.stream().map(w -> w.split(" ").length).collect(Collectors.toSet()));
    }
}
