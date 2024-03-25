import java.io.*;
import java.util.*;

public class AdditionalCoverageBranch {
        public static void getTests() throws IOException {
            System.out.println("Test cases for: " + ""  + AdditionalCoverageBranch.class.getName());
            ArrayList<TestCaseBranchDetails> testCases= CustomParser.branchParser(FilePaths.jsonPath);
            ArrayList<TestCaseBranchDetails> universe=CustomParser.branchParser(FilePaths.UniverseJson);
            ArrayList<Branch> universeVisited=universe.get(0).getVisitedBranches();
            ArrayList<Branch> visited=new ArrayList<>();
            int total=testCases.get(0).getVisitedBranches().size()+testCases.get(0).getNonVisitedBranches().size();
            Collections.sort(testCases, new sortByCoverage());
            TestCaseBranchDetails topTestCase=testCases.get(0);
            ArrayList<TestCaseBranchDetails> generatedTestCases=new ArrayList<>();
            generatedTestCases.add(topTestCase);
            HashMap<TestCaseBranchDetails,Long> map=new HashMap<>();
            //ArrayList<Branch> visited=new ArrayList<>();
            visited.addAll(topTestCase.getVisitedBranches());
            boolean changed=true;
            while (!visited.equals(universeVisited)){
                changed=false;
                for(int i=1;i< testCases.size();i++){
                    Long diff= Long.valueOf(0);
                    for(Branch l:testCases.get(i).getVisitedBranches()){
                        if(!visited.contains(l)){
                            diff++;
                        }
                    }
                    if(map.containsKey(testCases.get(i))){
                        map.replace(testCases.get(i),diff);
                    }else{
                        map.put(testCases.get(i),diff);
                    }
                }
                TestCaseBranchDetails toBeAdded = null;
                for (Map.Entry<TestCaseBranchDetails, Long> set :
                        map.entrySet()){
                    if(toBeAdded==null && set.getValue()!=Long.valueOf(0)){
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
                    for(Branch l:toBeAdded.getVisitedBranches()) {
                        if (!visited.contains(l)) {
                            visited.add(l);
                        }
                    }
                    String fileName=toBeAdded.file.getName();
                    System.out.println("Branch Coverage after adding testCase " +
                            fileName.substring(0,fileName.indexOf(".json")) + " = "+(float)visited.size()/total*100);
                    generatedTestCases.add(toBeAdded);
                    map.forEach((key, value) -> map.put(key, Long.valueOf(0)));
                    //System.out.println(generatedTestCases.size());
                }
            }
           // System.out.println(visited.size());
//            for(TestCaseBranchDetails branchDetails:generatedTestCases){
//                System.out.println(branchDetails.file.getName());
//            }
            String fileName = FilePaths.inputFile;
            String inputFileName = AdditionalCoverageBranch.class.getName() + ".txt";
            BufferedWriter bw = new BufferedWriter(new FileWriter(inputFileName));
            for (int i = 0; i < generatedTestCases.size(); i++) {
                try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
                    String line;
                    String testCase = generatedTestCases.get(i).getFile().getName();
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
