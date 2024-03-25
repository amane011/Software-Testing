import java.io.*;
import java.util.*;

public class TotalCoverageStatement {
    public static void getTests() throws IOException {
        System.out.println("Test cases for: " + ""  + TotalCoverageBranches.class.getName());
        ArrayList<TestCaseStatementDetails> testCases = CustomParser.LineParser(FilePaths.jsonPath);
        ArrayList<TestCaseStatementDetails> universe=CustomParser.LineParser(FilePaths.UniverseJson);
        ArrayList<Long> universeVisited=universe.get(0).getLinesExecuted();
        ArrayList<Long> visited=new ArrayList<>();
        int total=testCases.get(0).getLinesExecuted().size()+testCases.get(0).getLinesNotExecuted().size();
        Collections.sort(testCases,new SortByTestCoverage());
        ArrayList<TestCaseStatementDetails> generatedTesCases=new ArrayList<>();
        generatedTesCases.add(testCases.get(0));
        for(int i=1;i<testCases.size();i++){
            boolean flag=false;
            boolean changed=true;
            for(int l=0;l<generatedTesCases.size() && !universeVisited.equals(visited);l++){
                int j=0, k=0;
                ArrayList<Long> prev=generatedTesCases.get(l).getLinesExecuted();
                ArrayList<Long> cur=testCases.get(i).getLinesExecuted();
                while (j < prev.size() && k < cur.size()) {
                    if (prev.get(j).equals(cur.get(k))) {
                        k++;
                    }
                    j++;
                }
                if(k==cur.size()){
                    flag=true;
                    break;
                }
            }
            if (!flag) {
                generatedTesCases.add(testCases.get(i));
                for(Long l:testCases.get(i).getLinesExecuted()) {
                    if (!visited.contains(l)) {
                        visited.add(l);
                    }
                }
                String fileName=testCases.get(i).f.getName();
                System.out.println("Line Coverage after adding testCase " +
                        fileName.substring(0,fileName.indexOf(".json")) + " = "+(float)visited.size()/total*100);

            }

        }
        System.out.println();
        System.out.println();

        String fileName = FilePaths.inputFile;
        String inputFileName = TotalCoverageStatement.class.getName() + ".txt";
        BufferedWriter bw = new BufferedWriter(new FileWriter(inputFileName));
        for (int i = 0; i < generatedTesCases.size(); i++) {
            try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
                String line;
                String testCase = generatedTesCases.get(i).getF().getName();
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
class SortByTestCoverage implements Comparator<TestCaseStatementDetails> {

    @Override
    public int compare(TestCaseStatementDetails o1, TestCaseStatementDetails o2) {
        if (o1.testCoverage > o2.testCoverage) {
            return -1;
        } else if (o1.testCoverage < o2.testCoverage) {
            return 1;
        }
        return o2.getLinesExecuted().size()-o1.getLinesExecuted().size();
    }
}


