import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Objects;

class TestCaseStatementDetails {
    ArrayList<Long> linesNotExecuted;
    ArrayList<Long> linesExecuted;
    File f;
    float testCoverage = 0;

    public TestCaseStatementDetails(ArrayList<Long> linesNotExecuted, ArrayList<Long> linesExecuted, File f) {
        this.linesNotExecuted = linesNotExecuted;
        this.linesExecuted = linesExecuted;
        this.f = f;
    }

    public void getTestCoveragePercent() {
        testCoverage = (float) linesExecuted.size() / (linesExecuted.size() + linesNotExecuted.size()) * 1000;
    }

    public ArrayList<Long> getLinesExecuted() {
        return linesExecuted;
    }

    public void setLinesExecuted(ArrayList<Long> linesExecuted) {
        this.linesExecuted = linesExecuted;
    }

    @Override
    public String toString() {
        return "list=" + linesNotExecuted +
                ", f=" + f.getName();
    }

    public ArrayList<Long> getLinesNotExecuted() {
        return linesNotExecuted;
    }

    public void setLinesNotExecuted(ArrayList<Long> linesNotExecuted) {
        this.linesNotExecuted = linesNotExecuted;
    }

    public File getF() {
        return f;
    }

    public void setF(File f) {
        this.f = f;
    }

}
