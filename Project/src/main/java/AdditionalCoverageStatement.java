import java.io.*;
import java.util.*;

public class AdditionalCoverageStatement {
    public static void getTests() throws IOException {
        System.out.println("Test cases for: " + ""  + AdditionalCoverageStatement.class.getName());

        ArrayList<TestCaseStatementDetails> lists = CustomParser.LineParser(FilePaths.jsonPath);
        ArrayList<TestCaseStatementDetails> universe=CustomParser.LineParser(FilePaths.UniverseJson);
        ArrayList<Long> universeVisited=universe.get(0).getLinesExecuted();
        ArrayList<Long> visited=new ArrayList<>();
        int total=lists.get(0).getLinesExecuted().size()+lists.get(0).getLinesNotExecuted().size();
        Collections.sort(lists, new SortByNotExecuted());
        TestCaseStatementDetails topTestCase=lists.get(0);
        ArrayList<TestCaseStatementDetails> generatedTestCases=new ArrayList<>();
        generatedTestCases.add(topTestCase);
        HashMap<TestCaseStatementDetails,Long> map=new HashMap<>();
        //ArrayList<Long> visited=new ArrayList<>();
        visited.addAll(topTestCase.getLinesExecuted());
        boolean changed=true;
        while (!visited.equals(universeVisited)){
            changed=false;
            for(int i=1;i< lists.size();i++){
                Long diff= Long.valueOf(0);
                for(Long l:lists.get(i).getLinesExecuted()){
                    if(!visited.contains(l)){
                        diff++;
                    }
                }
                if(map.containsKey(lists.get(i))){
                    map.replace(lists.get(i),diff);
                }else{
                    map.put(lists.get(i),diff);
                }
            }
            TestCaseStatementDetails toBeAdded = null;
            for (Map.Entry<TestCaseStatementDetails, Long> set :
                    map.entrySet()){
                if(toBeAdded==null && !Objects.equals(set.getValue(), Long.valueOf(0))){
                    changed=true;
                    toBeAdded=set.getKey();
                }else if( toBeAdded!=null && map.get(toBeAdded)<set.getValue()){
                    changed=true;
                    toBeAdded=set.getKey();
                }
            }
            if(!changed){
                break;
            }else {
                for(Long l:toBeAdded.getLinesExecuted()) {
                    if (!visited.contains(l)) {
                        visited.add(l);
                    }
                }
                String fileName=toBeAdded.f.getName();
                System.out.println("Line Coverage after adding testCase " +
                        fileName.substring(0,fileName.indexOf(".json")) + " = "+(float)visited.size()/total*100);
                generatedTestCases.add(toBeAdded);
                map.forEach((key, value) -> map.put(key, Long.valueOf(0)));
                //System.out.println(generatedTestCases.size());
            }
        }

//
//        Collections.sort(visited);
       System.out.println();
        System.out.println();
//        for(TestCaseStatementDetails fileName:generatedTestCases){
//            System.out.println(fileName.f.getName()+""+fileName.testCoverage);
//        }
        String fileName = FilePaths.inputFile;
        String inputFileName = AdditionalCoverageStatement.class.getName() + ".txt";
        BufferedWriter bw = new BufferedWriter(new FileWriter(inputFileName));
        for (int i = 0; i < generatedTestCases.size(); i++) {
            try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
                String line;
                String testCase = generatedTestCases.get(i).getF().getName();
                int lineNumber = 1;
                int lineToRead = Integer.parseInt(testCase.substring(0, testCase.indexOf(".json")));
                while ((line = br.readLine()) != null) {
                    if (lineNumber == lineToRead) {
                        bw.write(line + "\n");
                        break;
                    }
                    lineNumber++;
                }
            } catch (IOException e) {
                System.err.format("IOException: %s%n", e);
            }
        }
        bw.close();

    }
}

class SortByNotExecuted implements Comparator<TestCaseStatementDetails> {

    @Override
    public int compare(TestCaseStatementDetails o1, TestCaseStatementDetails o2) {
        return o1.getLinesNotExecuted().size() - o2.getLinesNotExecuted().size();
    }
}
