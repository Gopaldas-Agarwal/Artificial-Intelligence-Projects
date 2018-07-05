
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Engine {

    public static void main(String[] args) {
        Data mydata = new Data();
        ReadFile readInput = new ReadFile();
        mydata = readInput.getInputFromFile();
        InferenceEngine FOL = new InferenceEngine(mydata);
        FOL.startResolution();

    }

    public static void readAll(int k) {
        for (int i = 1; i <= k; ++i) {
            System.out.println("\nINPUT" + i);
            Data mydata = new Data();
            ReadFile readInput = new ReadFile();
            readInput.setPath(i);
            mydata = readInput.getInputFromFile();
            InferenceEngine FOL = new InferenceEngine(mydata);
            FOL.startResolution();
        }
    }

}

class ReadFile {

    String inputPath;
    ArrayList<Predicate> query;
    ArrayList<Sentence> kb;
    Data tempData;

    ReadFile() {
        query = new ArrayList<>();
        kb = new ArrayList<>();
        inputPath = "";
        tempData = new Data();
    }

    void setPath(int k) {
        inputPath = "";
    }

    Data getInputFromFile() {
        String inputString = readInputFile();
        parseInput(inputString);
        return tempData;
    }

    void parseInput(String inputString) {
        String lines[] = inputString.split("\n");
        tempData.totalQueries = Integer.parseInt(lines[0].trim());
        for (int i = 1; i <= tempData.totalQueries; ++i) {
            String str = lines[i].trim();
            tempData.query.add(parseTerm(str, i));
        }

        tempData.totalSentences = Integer.parseInt(lines[tempData.totalQueries + 1].trim());
        for (int i = tempData.totalQueries + 2; i < tempData.totalQueries + tempData.totalSentences + 2; ++i) {
            String str = lines[i].trim();
            String split1[] = str.split("\\|");
            tempData.kb.add(new Sentence());
            for (int j = 0; j < split1.length; ++j) {
                Predicate p = parseTerm(split1[j], i - tempData.totalQueries - 2);
                updateHashMap(p.name, i - tempData.totalQueries - 2);
                tempData.kb.get(i - tempData.totalQueries - 2).mysentence.add(p);
            }
        }
    }

    void updateHashMap(String predicateName, int row) {
        if (tempData.predicateToKb.containsKey(predicateName)) {
            if (!tempData.predicateToKb.get(predicateName).contains(row)) {
                tempData.predicateToKb.get(predicateName).add(row);
            }
        } else {
            ArrayList<Integer> temp = new ArrayList<>();
            temp.add(row);
            tempData.predicateToKb.put(predicateName, temp);
        }
    }

    Predicate parseTerm(String str, int num) {
        str = str.trim();
        Predicate tempTerm = new Predicate();
        if ((str.charAt(0)) == '~') {
            tempTerm.negated = true;
        }

        String split[] = str.split("\\(|\\,|\\)");
        tempTerm.name = split[0].trim();
        for (int i = 1; i < split.length; ++i) {
            split[i] = split[i].trim();
            if (Character.isUpperCase(split[i].charAt(0))) {
                tempTerm.constants.add(split[i]);
                tempTerm.arguments.add(split[i]);
            } else {
                split[i] += Integer.toString(num);
                tempTerm.variables.add(split[i]);
                tempTerm.arguments.add(split[i]);
            }

        }
        return (new Predicate(tempTerm));
    }

    String readInputFile() {
        FileReader inputFile = null;
        String inputString = "";
        short i = 0;
        try {
            inputFile = new FileReader(inputPath);
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
        try {
            BufferedReader br = new BufferedReader(inputFile);
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            inputString = sb.toString();

        } catch (IOException e) {
            System.out.println("Error reading file:" + e);
        } finally {
            try {
                inputFile.close();
            } catch (IOException e) {
                System.out.println("Error Closing File");
            }
        }
        return inputString;
    }
}

class InferenceEngine {

    Data myData;
    Data mainData;
    String outputPath;
    ArrayList<Integer> count;
    HashMap<Integer, Integer> usedCount2;

    InferenceEngine(Data d) {
        mainData = d;
        outputPath = "";
    }

    void startResolution() {
        boolean[] solution = new boolean[mainData.query.size()];

        usedCount2 = new HashMap<>();
        for (int i = 0; i < mainData.kb.size(); ++i) {
            usedCount2.put(i, 0);
        }

        count = new ArrayList<>();
        for (int i = 0; i < mainData.query.size(); ++i) {
            count.add(0);
            System.out.print("**" + i + "**\t");
            Sentence negatedQuery = new Sentence();
            negatedQuery.mysentence.add(negateQuery(new Predicate(mainData.query.get(i))));

            String predname = negatedQuery.mysentence.get(0).name;
            myData = new Data(mainData);
            myData.kb.add(new Sentence(negatedQuery));
            if (myData.predicateToKb.containsKey(predname)) {
                myData.predicateToKb.get(predname).add(myData.totalSentences);
            } else {
                ArrayList<Integer> temp = new ArrayList<>();
                temp.add(myData.totalSentences);
                myData.predicateToKb.put(predname, temp);
            }
            myData.totalSentences++;
            for (int j = 0; j < myData.kb.size(); ++j) {
                usedCount2.put(j, 0);
            }
            solution[i] = resolveSentence(negatedQuery, intHashMapCopy(usedCount2));
            System.out.print("COUNTS:");
            for (int j = 0; j < usedCount2.size(); ++j) {
                if (usedCount2.get(j) > 0) {
                    System.out.print(j + "=" + usedCount2.get(j) + "\t");
                }
            }
            System.out.println();
        }
        writeToFile(solution);
    }

    boolean resolveSentence(Sentence sentenceToResolve, HashMap<Integer, Integer> usedCount) {
        count.set(count.size() - 1, count.get(count.size() - 1) + 1);
        sysout("\n\t\t\t\t\t----------------------------------------------\n\t\t\t\t\t\t\tNEW CALL TO RESOLUTION");
        sysout("\t\t\t\t\t\t" + sentenceToResolve.mysentence);
        if (sentenceToResolve.mysentence.size() == 0) {
            return true;
        }
        for (int i = 0; i < sentenceToResolve.mysentence.size(); ++i) {
            Sentence negatedSTR = new Sentence();
            negatedSTR.mysentence.add(negateQuery(sentenceToResolve.mysentence.get(i)));

            ArrayList<Integer> negatedSTRinKBindex = myData.predicateToKb.get(negatedSTR.mysentence.get(0).name);

            if (negatedSTRinKBindex == null) {
                return false;
            }
            for (int j = 0; j < negatedSTRinKBindex.size(); ++j) {
                sysout("\t\t\t\tSTR=" + sentenceToResolve.mysentence);
                sysout("----->>>NegatedSTR=" + negatedSTR.mysentence);
                sysout("USING A NEW OPTION\nParent MAP=");
                sysout("Possible with:" + negatedSTRinKBindex.toString() + "=" + myData.kb.get(negatedSTRinKBindex.get(j)).mysentence);
                if (usedCount.get(negatedSTRinKBindex.get(j)) > 5) {
                    sysout("TOO MANY USES");
                    continue;
                }
                sysout("RESOLVE WITH=" + negatedSTRinKBindex.get(j) + " :" + myData.kb.get(negatedSTRinKBindex.get(j)).mysentence);
                Sentence mergeWith = new Sentence(myData.kb.get(negatedSTRinKBindex.get(j)));

                HashMap<String, String> substitutionMapCopy = unifySentences(sentenceToResolve, mergeWith, i, new HashMap<String, String>());
                if (substitutionMapCopy != null) {
                    usedCount.put(negatedSTRinKBindex.get(j), usedCount.get(negatedSTRinKBindex.get(j)) + 1);
                    usedCount2.put(negatedSTRinKBindex.get(j), usedCount2.get(negatedSTRinKBindex.get(j)) + 1);

                    Sentence s3 = mergeSentences(new Sentence(sentenceToResolve), new Sentence(mergeWith), substitutionMapCopy);
                    sysout("Unified to -> " + s3.mysentence);
                    if (s3.mysentence.size() == 0) {
                        return true;
                    }
                    if (resolveSentence(new Sentence(s3), intHashMapCopy(usedCount))) {
                        return true;
                    }

                } else {
                }
            }
        }
        sysout("\n\n\n");
        return false;
    }

    void reuseSentenceFromKB(Sentence s, int count) {
        for (int i = 0; i < s.mysentence.size(); ++i) {
            for (int j = 0; j < s.mysentence.get(i).variables.size(); ++j) {
                String temp = s.mysentence.get(i).variables.get(j);
                int index = s.mysentence.get(i).arguments.indexOf(temp);
                temp = temp + ":" + count;
                s.mysentence.get(i).variables.set(j, temp);
                s.mysentence.get(i).arguments.set(index, temp);
            }
        }

    }

    HashMap<String, String> hashMapCopy(HashMap<String, String> mymap) {
        HashMap<String, String> newMap = new HashMap<>();
        for (Map.Entry<String, String> entry : mymap.entrySet()) {

            newMap.put(entry.getKey(), entry.getValue());

        }
        return newMap;
    }

    HashMap<Integer, Integer> intHashMapCopy(HashMap<Integer, Integer> mymap) {
        HashMap<Integer, Integer> newMap = new HashMap<>();
        for (Map.Entry<Integer, Integer> entry : mymap.entrySet()) {

            newMap.put(entry.getKey(), entry.getValue());

        }
        return newMap;
    }

    boolean isVariable(String str) {
        if (Character.isUpperCase(str.charAt(0))) {
            return false;
        } else {
            return true;
        }
    }

    Sentence mergeSentences(Sentence s1, Sentence s2, HashMap<String, String> substitutionMap) {
        Sentence s3 = new Sentence();
        sysout(s1.mysentence.toString() + "&" + s2.mysentence.toString());
        sysout("Mapping--->" + substitutionMap.toString());
        for (int i = 0; i < s1.mysentence.size(); ++i) {
            for (int j = 0; j < s1.mysentence.get(i).arguments.size(); ++j) {
                String predarg = s1.mysentence.get(i).arguments.get(j);
                if (s1.mysentence.get(i).variables.contains(predarg)) {
                    if (substitutionMap.containsKey(predarg)) {
                        if (!isVariable(substitutionMap.get(predarg))) {
                            s1.mysentence.get(i).variables.remove(predarg);
                            s1.mysentence.get(i).constants.add(substitutionMap.get(predarg));
                            s1.mysentence.get(i).arguments.set(j, substitutionMap.get(predarg));
                        } else if (!predarg.equals(substitutionMap.get(predarg))) {
                            s1.mysentence.get(i).arguments.set(j, substitutionMap.get(predarg));
                            s1.mysentence.get(i).variables.remove(predarg);
                            s1.mysentence.get(i).variables.add(substitutionMap.get(predarg));
                            j--;
                        }
                        sysout(j + ":" + predarg + "<-->" + substitutionMap.get(predarg) + "--->" + s1.mysentence.toString());

                    }
                }
            }
        }
        for (int i = 0; i < s2.mysentence.size(); ++i) {
            for (int j = 0; j < s2.mysentence.get(i).arguments.size(); ++j) {
                String arg = s2.mysentence.get(i).arguments.get(j);
                if (s2.mysentence.get(i).variables.contains(arg)) {
                    if (substitutionMap.containsKey(arg)) {
                        if (!isVariable(substitutionMap.get(arg))) {
                            s2.mysentence.get(i).variables.remove(arg);
                            s2.mysentence.get(i).constants.add(substitutionMap.get(arg));
                            s2.mysentence.get(i).arguments.set(j, substitutionMap.get(arg));
                        } else if (!arg.equals(substitutionMap.get(arg))) {
                            s2.mysentence.get(i).arguments.set(j, substitutionMap.get(arg));
                            s2.mysentence.get(i).variables.remove(arg);
                            s2.mysentence.get(i).variables.add(substitutionMap.get(arg));
                            j--;
                        }
                    }
                }
            }
        }

        for (int i = 0; i < s1.mysentence.size(); ++i) {
            s3.mysentence.add(s1.mysentence.get(i));

        }
        for (int j = 0; j < s2.mysentence.size(); ++j) {
            s3.mysentence.add(s2.mysentence.get(j));
        }
        int size = s3.mysentence.size();
        sysout("MERGEEEEEEEEEEEEEEEEEEEEEEEEE");
        sysout(s1.mysentence.toString() + "&" + s2.mysentence.toString() + "->" + s3.mysentence.toString());
        sysout("Size=" + size);
        for (int i = 0; i < size; ++i) {
            boolean flag = false;
            for (int j = i + 1; j < size; ++j) {
                if (i != j && checkEqualPredicate(s3.mysentence.get(i), s3.mysentence.get(j), substitutionMap)) {
                    sysout(s3.mysentence + " -> remove " + i + "&" + j);
                    s3.mysentence.remove(j);
                    s3.mysentence.remove(i);
                    size -= 2;
                    flag = true;
                    break;
                }
            }
            if (flag) {
                i = -1;
            }

        }
        sysout("-------------MERGEEEEEEEEEEEEEEEEEEEEEEEEE--------------------");
        return s3;
    }

    boolean checkEqualPredicate(Predicate p1, Predicate p2, HashMap<String, String> substitutionMap) {

        if (checkNegatedPredicates(p1.name, p2.name) && (p1.arguments.size() == p2.arguments.size())) {
            for (int i = 0; i < p1.arguments.size(); ++i) {
                if (!compareArgument(p1, p2, i, substitutionMap)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    boolean checkNegatedPredicates(String s1, String s2) {
        if (s1.equals("~" + s2)) {
            return true;
        }
        if (s2.equals("~" + s1)) {
            return true;
        }
        return false;
    }

    boolean checkEqualPredicates(String s1, String s2) {
        if (s1.equals("~" + s2)) {
            return true;
        }
        if (s2.equals("~" + s1)) {
            return true;
        }
        if (s1.equals(s2)) {
            return true;
        }
        return false;
    }

    boolean compareArgument(Predicate p1, Predicate p2, int n, HashMap<String, String> substitutionMap) {
        String arg1 = p1.arguments.get(n);
        String arg2 = p2.arguments.get(n);

        if (arg1.equals(arg2)) {
            return true;
        }
        if (p2.variables.contains(arg2)) {
            if (substitutionMap.containsKey(arg2) && arg1.equals(substitutionMap.get(arg2))) {
                return true;
            }
        }
        if (p1.variables.contains(arg1)) {
            if (substitutionMap.containsKey(arg1) && arg2.equals(substitutionMap.get(arg1))) {
                return true;
            }
        }
        if (substitutionMap.containsKey(arg1) && substitutionMap.containsKey(arg2)) {
            if (substitutionMap.get(arg1).equals(substitutionMap.get(arg2))) {
                return true;
            }
        }
        return false;
    }

    HashMap<String, String> unifySentences(Sentence s1, Sentence s2, int num, HashMap<String, String> substitutionMap) {
        sysout("--------- UNIFY SENTENCES");
        sysout(s1.mysentence + " &&&&&&& " + s2.mysentence);
        sysout("---------");
        for (int i = 0; i < s1.mysentence.size(); ++i) {
            boolean possible = applySubstitution(s1.mysentence.get(i), s2, substitutionMap);
            if (!possible) {
                sysout("Substitution not possible");
                substitutionMap = null;
                break;
            }
        }

        return substitutionMap;
    }

    boolean applySubstitution(Predicate p, Sentence s2, HashMap<String, String> substitutionMap) {
        ArrayList<Boolean> flagList = new ArrayList<>();
        boolean usedOnce = false;
        for (int j = 0; j < s2.mysentence.size(); ++j) {
            if (checkNegatedPredicates(p.name, s2.mysentence.get(j).name)) {
                sysout(p + "======" + s2.mysentence.get(j));
                flagList.add(true);
                sysout("Start FLAG list=" + flagList.toString());
                usedOnce = true;
                for (int i = 0; i < p.arguments.size(); ++i) {
                    boolean flag = false;
                    if (p.variables.contains(p.arguments.get(i))) {
                        if (s2.mysentence.get(j).constants.contains(s2.mysentence.get(j).arguments.get(i))) {
                            flag = updateArgMap(p.arguments.get(i), s2.mysentence.get(j).arguments.get(i), "vc", substitutionMap);
                        } else {
                            flag = updateArgMap(p.arguments.get(i), s2.mysentence.get(j).arguments.get(i), "vv", substitutionMap);
                        }
                    } else {
                        if (s2.mysentence.get(j).constants.contains(s2.mysentence.get(j).arguments.get(i))) {
                            flag = (updateArgMap(p.arguments.get(i), s2.mysentence.get(j).arguments.get(i), "cc", substitutionMap));

                        } else {
                            flag = updateArgMap(p.arguments.get(i), s2.mysentence.get(j).arguments.get(i), "cv", substitutionMap);

                        }
                    }
                    if (!flag) {
                        flagList.set(flagList.size() - 1, false);

                        break;
                    }

                }
                sysout(j + " FLAG list=" + flagList.toString());
                if (flagList.contains(true)) {
                    return true;
                }

            }

        }

        if (flagList.size() == 0) {
            return true;
        }
        for (int i = 0; i < flagList.size(); ++i) {
            if (flagList.get(i)) {
                return true;
            }
        }

        return false;
    }

    boolean updateArgMap(String arg1, String arg2, String subType, HashMap<String, String> substitutionMap) {

        if (arg1.equals(arg2)) {
            return true;
        }

        if (subType == "cc") {
            if (arg1.equals(arg2)) {
                return true;
            }
            return false;
        } else if (subType == "vc") {
            if (substitutionMap.containsKey(arg1)) {
                if (isVariable(substitutionMap.get(arg1)) && !arg1.equals(substitutionMap.get(arg1))) {
                    boolean flag = updateArgMap(substitutionMap.get(arg1), arg2, "vc", substitutionMap);
                    if (flag) {
                        substitutionMap.put(arg1, arg2);
                        return true;
                    } else {
                        return false;
                    }
                } else if (isVariable(substitutionMap.get(arg1))) {
                    substitutionMap.put(arg1, arg2);
                    return true;
                } else {
                    return updateArgMap(substitutionMap.get(arg1), arg2, "cc", substitutionMap);
                }
            } else {
                substitutionMap.put(arg1, arg2);

                return true;
            }
        } else if (subType == "cv") {
            return updateArgMap(arg2, arg1, "vc", substitutionMap);
        } else if (subType == "vv") {

            if (arg1.equals(arg2)) {
                return true;
            }

            if (substitutionMap.containsKey(arg1)) {
                if (substitutionMap.containsKey(arg2)) {
                    if (isVariable(substitutionMap.get(arg1)) && !arg1.equals(substitutionMap.get(arg1))) {
                        if (isVariable(substitutionMap.get(arg2)) && !arg2.equals(substitutionMap.get(arg2))) {
                            boolean flag = updateArgMap(substitutionMap.get(arg1), substitutionMap.get(arg2), "vv", substitutionMap);
                            if (flag) {
                                substitutionMap.put(arg1, substitutionMap.get(arg2));
                                substitutionMap.put(arg2, substitutionMap.get(arg2));
                                return true;
                            } else {
                                return false;
                            }
                        } else {
                            boolean flag = updateArgMap(substitutionMap.get(arg1), substitutionMap.get(arg2), "vc", substitutionMap);
                            if (flag) {
                                substitutionMap.put(arg1, arg2);
                                return true;
                            } else {
                                return false;
                            }
                        }
                    } else {
                        if (isVariable(substitutionMap.get(arg2))) {
                            boolean flag = updateArgMap(substitutionMap.get(arg2), substitutionMap.get(arg1), "vc", substitutionMap);
                            if (flag) {
                                substitutionMap.put(arg2, arg1);
                                return true;
                            } else {
                                return false;
                            }
                        } else {
                            return updateArgMap(substitutionMap.get(arg1), substitutionMap.get(arg2), "cc", substitutionMap);
                        }
                    }
                } else {
                    if (isVariable(substitutionMap.get(arg1))) {
                        boolean flag = updateArgMap(substitutionMap.get(arg1), arg1, "vv", substitutionMap);
                        if (flag) {
                            substitutionMap.put(arg2, substitutionMap.get(arg1));
                            return true;
                        } else {
                            return false;
                        }

                    } else {
                        substitutionMap.put(arg2, substitutionMap.get(arg1));
                        return true;
                    }
                }
            } else {
                if (substitutionMap.containsKey(arg2)) {
                    if (isVariable(substitutionMap.get(arg2))) {
                        boolean flag = updateArgMap(substitutionMap.get(arg2), arg2, "vv", substitutionMap);
                        if (flag) {
                            substitutionMap.put(arg1, substitutionMap.get(arg2));
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        substitutionMap.put(arg1, substitutionMap.get(arg2));
                        return true;
                    }
                } else {
                    substitutionMap.put(arg1, arg2);
                    substitutionMap.put(arg2, arg2);
                    return true;
                }
            }
        }

        return false;
    }

    Predicate negateQuery(Predicate q) {
        Predicate toNegate = new Predicate(q);
        if (q.name.charAt(0) == '~') {
            toNegate.name = q.name.substring(1, q.name.length());
        } else {
            toNegate.name = '~' + q.name;
        }
        toNegate.negated = !q.negated;
        return toNegate;

    }

    void writeToFile(boolean[] ans) {
        Writer writer = null;
        try {
            File file = new File(outputPath);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            for (short i = 0; i < ans.length; ++i) {
                System.out.println(i + ":" + myData.query.get(i) + "=" + ans[i]);
                if (ans[i]) {
                    bw.write("TRUE");
                } else {
                    bw.write("FALSE");
                }

                bw.write("\n");
                bw.flush();
            }
            bw.close();
        } catch (IOException e) {
            sysout("Exception: " + e);
        } finally {
            try {
                writer.close();
            } catch (Exception ex) {/*ignore*/
            }

        }
    }

    void sysout(String str) {
    }
}

class Sentence {

    ArrayList<Predicate> mysentence;

    Sentence() {
        mysentence = new ArrayList<>();
    }

    Sentence(Predicate p) {
        this.mysentence = new ArrayList<>();
        this.mysentence.add(new Predicate(p));
    }

    Sentence(Sentence s) {
        this.mysentence = new ArrayList<>();
        for (int i = 0; i < s.mysentence.size(); ++i) {

            this.mysentence.add(new Predicate(s.mysentence.get(i)));
        }

    }
}

class Predicate {

    boolean negated;
    String name;
    ArrayList<String> arguments;
    ArrayList<String> constants;
    ArrayList<String> variables;

    Predicate() {
        this.negated = false;
        this.name = "";
        this.arguments = new ArrayList<>();
        this.constants = new ArrayList<String>();
        this.variables = new ArrayList<String>();
    }

    Predicate(Predicate t) {
        this.name = "";
        this.arguments = new ArrayList<>();
        this.constants = new ArrayList<String>();
        this.variables = new ArrayList<String>();
        this.negated = t.negated;
        this.name = new String(t.name);
        for (int i = 0; i < t.arguments.size(); ++i) {
            this.arguments.add(new String(t.arguments.get(i)));
        }
        if (t.constants != null) {
            for (int i = 0; i < t.constants.size(); ++i) {
                this.constants.add(new String(t.constants.get(i)));
            }
        }
        if (t.variables != null) {
            for (int i = 0; i < t.variables.size(); ++i) {
                this.variables.add(new String(t.variables.get(i)));
            }
        }
    }

    public String toString() {
        String str = "";
        str += name + "(";

        for (int i = 0; i < arguments.size(); ++i) {
            str += arguments.get(i) + ",";
        }
        str += ")";
        return str;
    }
}

class Data {

    ArrayList<Predicate> query;
    ArrayList<Sentence> kb;
    HashMap<String, ArrayList<Integer>> predicateToKb;
    int totalQueries;
    int totalSentences;

    Data() {
        query = new ArrayList<>();
        kb = new ArrayList<>();
        predicateToKb = new HashMap<String, ArrayList<Integer>>();
    }

    Data(Data d) {
        this.totalQueries = d.totalQueries;
        this.totalSentences = d.totalSentences;
        this.query = new ArrayList<>();
        this.kb = new ArrayList<>();
        this.predicateToKb = new HashMap<String, ArrayList<Integer>>();

        for (int i = 0; i < d.query.size(); ++i) {
            this.query.add(new Predicate(d.query.get(i)));
        }

        for (int i = 0; i < d.kb.size(); ++i) {
            this.kb.add(new Sentence(d.kb.get(i)));
        }

        for (String key : d.predicateToKb.keySet()) {
            ArrayList<Integer> temp = new ArrayList<>(d.predicateToKb.get(key));
            ArrayList<Integer> temp2 = new ArrayList<>();
            for (int i = 0; i < temp.size(); ++i) {
                int x = temp.get(i);
                temp2.add(x);
            }
            this.predicateToKb.put(key, temp2);
        }
    }

    void displayInfo() {
        System.out.println("Total queries=" + totalQueries);
        for (int i = 0; i < totalQueries; ++i) {
            System.out.println(i + ":" + query.get(i));
        }
        System.out.println("Total sentences=" + totalSentences);
        for (int i = 0; i < totalSentences; ++i) {
            System.out.print("\n" + i + ":");
            for (int k = 0; k < kb.get(i).mysentence.size(); ++k) {
                System.out.print(kb.get(i).mysentence.get(k) + " *** ");
            }
        }
        System.out.println("\n\nHashMap:");
        for (String name : predicateToKb.keySet()) {

            String key = name.toString();
            String value = predicateToKb.get(name).toString();
            System.out.println(key + " " + value);
        }
        System.out.println("**********************");
    }

}
