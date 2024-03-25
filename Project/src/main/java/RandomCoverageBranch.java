import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class RandomCoverageBranch {
    public static void getTests() throws IOException {
        System.out.println("Test cases for: " + "" +RandomCoverageBranch.class.getName());
        ArrayList<TestCaseBranchDetails> testCases = CustomParser.branchParser(FilePaths.jsonPath);
        ArrayList<TestCaseBranchDetails> universe=CustomParser.branchParser(FilePaths.UniverseJson);
        ArrayList<Branch> universeVisited=universe.get(0).getVisitedBranches();
        ArrayList<Branch> visited=new ArrayList<>();
        ArrayList<TestCaseBranchDetails> generatedTestCases=new ArrayList<>();
        int min=0;
        int max=testCases.size()-1;
        Random random=new Random();
        int total=testCases.get(0).getVisitedBranches().size()+testCases.get(0).getNonVisitedBranches().size();
        generatedTestCases.add(testCases.get( random.nextInt(max - min + 1) + min));
        for(int i=1;i<testCases.size() && !universeVisited.equals(visited);i++){
            boolean flag=false;
            HashSet<Branch> curr=new HashSet<>(testCases.get(i).getVisitedBranches());
            for(int l=0;l<generatedTestCases.size();l++){
                int j=0, k=0;
                HashSet<Branch> prev=new HashSet<>( generatedTestCases.get(l).getVisitedBranches());
                if(prev.containsAll(curr) || visited.containsAll(curr)){
                    flag=true;
                    break;
                }
            }
            if (!flag) {
                generatedTestCases.add(testCases.get(i));
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

        String fileName = FilePaths.inputFile;
        String inputFileName = RandomCoverageBranch.class.getName() + ".txt";
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
