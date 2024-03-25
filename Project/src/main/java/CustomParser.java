import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

public class CustomParser {

    public static ArrayList<TestCaseStatementDetails> LineParser(String path) {
        File Mainfolders = new File(path);
        ArrayList<TestCaseStatementDetails> lists = new ArrayList<>();
        for (File f : Objects.requireNonNull(Mainfolders.listFiles())) {
            JSONParser parser = new JSONParser();
            try {
                Object obj = parser.parse(new FileReader(f.getAbsolutePath()));
                JSONObject jsonObject = (JSONObject) obj;
                JSONArray files = (JSONArray) jsonObject.get("files");
                JSONObject file = (JSONObject) files.get(0);
                JSONArray lines = (JSONArray) file.get("lines");
                ArrayList<Long> lineNotExec = new ArrayList<>();
                ArrayList<Long> lineExec = new ArrayList<>();
                for (int i = 0; i < lines.size(); i++) {
                    JSONObject line = (JSONObject) lines.get(i);
                    Long count = (Long) line.get("count");
                    if (count == 0) {
                        lineNotExec.add((Long) line.get("line_number"));
                    }else{
                        lineExec.add((Long) line.get("line_number"));
                    }
                }
                TestCaseStatementDetails fileDetails = new TestCaseStatementDetails(lineNotExec,lineExec,f);
                fileDetails.getTestCoveragePercent();
                lists.add(fileDetails);
            } catch (Exception e) {

            }
        }
        return lists;
    }

    public static ArrayList<TestCaseBranchDetails> branchParser(String path){
        File folders = new File(path);
        ArrayList<TestCaseBranchDetails> testCases = new ArrayList<>();
        for (File f : Objects.requireNonNull(folders.listFiles())) {
            JSONParser parser = new JSONParser();
            try {
                Object obj = parser.parse(new FileReader(f.getAbsolutePath()));
                JSONObject jsonObject = (JSONObject) obj;
                JSONArray files = (JSONArray) jsonObject.get("files");
                JSONObject file = (JSONObject) files.get(0);
                JSONArray lines = (JSONArray) file.get("lines");
                ArrayList<Branch> branchNotExec = new ArrayList<>();
                ArrayList<Branch> branchExec = new ArrayList<>();
                for (int i = 0; i < lines.size(); i++) {
                    JSONObject line = (JSONObject) lines.get(i);
                    Long count = (Long) line.get("count");
                    JSONArray branches=(JSONArray) line.get("branches");
                    if(!branches.isEmpty()){
                        if(count==0){
                            for(int j=0;j<branches.size();j++){
                                //JSONObject branch=(JSONObject) branches.get(j);
                                branchNotExec.add(new Branch(i,j));
                            }
                        }else{
                            for (int j=0;j<branches.size();j++){
                                JSONObject branch=(JSONObject) branches.get(j);
                                Long branchCount=(Long) branch.get("count");
                                if(branchCount==0){
                                    branchNotExec.add(new Branch(i,j));
                                }else{
                                    branchExec.add(new Branch(i,j));
                                }

                            }
                        }
                    }
                }
                Collections.sort(branchExec, new Comparator<Branch>() {
                    @Override
                    public int compare(Branch b1, Branch b2) {
                        if (b1.getLineNumber() == b2.getLineNumber()) {
                            return b1.getBranchNumber() - b2.getBranchNumber();
                        } else {
                            return b1.getLineNumber() - b2.getLineNumber();
                        }
                    }
                });
                TestCaseBranchDetails fileDetails = new TestCaseBranchDetails(f,branchExec,branchNotExec);
                fileDetails.getBranchCoverage();

                testCases.add(fileDetails);
            } catch (Exception e) {

            }
        }
        return testCases;
    }
}
