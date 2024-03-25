import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class RandomCoverageStatement {
    public static void getTests() throws IOException {
        System.out.println("Test cases for: " + ""  + RandomCoverageStatement.class.getName());
        ArrayList<TestCaseStatementDetails> testCases = CustomParser.LineParser(FilePaths.jsonPath);
        ArrayList<TestCaseStatementDetails> universe=CustomParser.LineParser(FilePaths.UniverseJson);
        ArrayList<Long> universeVisited=universe.get(0).getLinesExecuted();
        ArrayList<Long> visited=new ArrayList<>();
        ArrayList<TestCaseStatementDetails> generatedTestCases=new ArrayList<>();
        int total=testCases.get(0).getLinesExecuted().size()+testCases.get(0).getLinesNotExecuted().size();

        int min=0;
        int max=testCases.size()-1;
        Random random=new Random();
        int p=0;
        generatedTestCases.add(testCases.get( random.nextInt(max - min + 1) + min));
        for(int i=1;i<testCases.size() && !universeVisited.equals(visited);i++){
            if(visited.size()==universeVisited.size()){
                break;
            }
            boolean flag=false;
            for(int l=0;l<generatedTestCases.size();l++){
                int j=0, k=0;
                ArrayList<Long> prev=generatedTestCases.get(l).getLinesExecuted();
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
                generatedTestCases.add(testCases.get(i));
                for(Long l:testCases.get(i).getLinesExecuted()) {
                    if (!visited.contains(l)) {
                        visited.add(l);
                    }
                }
//                if(p==visited.size()){
//                    break;
//                }
//                p=visited.size();
                String fileName=testCases.get(i).f.getName();
                System.out.println("Line Coverage after adding testCase " +
                        fileName.substring(0,fileName.indexOf(".json")) + " = "+(float)visited.size()/total*100);
            }

        }
        System.out.println();
        System.out.println();
        String fileName = FilePaths.inputFile;
        String inputFileName =  RandomCoverageStatement.class.getName() + ".txt";
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
