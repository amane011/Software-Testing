import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

public class TotalCoverageBranches {
    public static void getTests() throws IOException {
        System.out.println("Test cases for: " + ""  + TotalCoverageBranches.class.getName());
        ArrayList<TestCaseBranchDetails> testCases = CustomParser.branchParser(FilePaths.jsonPath);
        ArrayList<TestCaseBranchDetails> universe=CustomParser.branchParser(FilePaths.UniverseJson);
        ArrayList<Branch> universeVisited=universe.get(0).getVisitedBranches();
        ArrayList<Branch> visited=new ArrayList<>();
        Collections.sort(testCases,new sortByCoverage());
        ArrayList<TestCaseBranchDetails> generatedTesCases=new ArrayList<>();
        generatedTesCases.add(testCases.get(0));
        int total=testCases.get(0).getVisitedBranches().size()+testCases.get(0).getNonVisitedBranches().size();
        for(int i=1;i<testCases.size() && !universeVisited.equals(visited);i++){
            boolean flag=false;
            HashSet<Branch> curr=new HashSet<>(testCases.get(i).getVisitedBranches());
            for(int l=0;l<generatedTesCases.size();l++){
                int j=0, k=0;
                HashSet<Branch> prev=new HashSet<>( generatedTesCases.get(l).getVisitedBranches());
                if(prev.containsAll(curr) || visited.containsAll(curr)){
                    flag=true;
                    break;
                }
            }
            if (!flag) {
                generatedTesCases.add(testCases.get(i));
                for(Branch l:testCases.get(i).getVisitedBranches()) {
                    if (!visited.contains(l)) {
                        visited.add(l);
                    }
                }
                String fileName=testCases.get(i).file.getName();
                System.out.println("Branch Coverage after adding testCase " +
                        fileName.substring(0,fileName.indexOf(".json")) + " = "+(float)visited.size()/total*100);
            }
        }
        System.out.println();
        System.out.println();
        Collections.sort(visited, new Comparator<Branch>() {
            @Override
            public int compare(Branch b1, Branch b2) {
                if (b1.getLineNumber() == b2.getLineNumber()) {
                    return b1.getBranchNumber() - b2.getBranchNumber();
                } else {
                    return b1.getLineNumber() - b2.getLineNumber();
                }
            }
        });

        String fileName = FilePaths.inputFile;
        String inputFileName = TotalCoverageBranches.class.getName() + ".txt";
        BufferedWriter bw = new BufferedWriter(new FileWriter(inputFileName));
        for (int i = 0; i < generatedTesCases.size(); i++) {
            try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
                String line;
                String testCase = generatedTesCases.get(i).getFile().getName();
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




