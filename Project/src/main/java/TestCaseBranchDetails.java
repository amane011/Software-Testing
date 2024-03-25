import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;

public class TestCaseBranchDetails {
    File file;
    ArrayList<Branch> visitedBranches;
    ArrayList<Branch> nonVisitedBranches;
    float branchCoverage;

    public TestCaseBranchDetails(File file, ArrayList<Branch> visitedBranches, ArrayList<Branch> nonVisitedBranches) {
        this.file = file;
        this.visitedBranches = visitedBranches;
        this.nonVisitedBranches = nonVisitedBranches;
    }

    public void getBranchCoverage() {
        branchCoverage= (float) visitedBranches.size()/(visitedBranches.size()+nonVisitedBranches.size()) *2000;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public ArrayList<Branch> getVisitedBranches() {
        return visitedBranches;
    }

    public void setVisitedBranches(ArrayList<Branch> visitedBranches) {
        this.visitedBranches = visitedBranches;
    }

    public ArrayList<Branch> getNonVisitedBranches() {
        return nonVisitedBranches;
    }

    public void setNonVisitedBranches(ArrayList<Branch> nonVisitedBranches) {
        this.nonVisitedBranches = nonVisitedBranches;
    }
}
class Branch{
    int lineNumber;
    int branchNumber;

    public Branch(int lineNumber, int branchNumber) {
        this.lineNumber = lineNumber;
        this.branchNumber = branchNumber;
    }
    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public int getBranchNumber() {
        return branchNumber;
    }

    public void setBranchNumber(int branchNumber) {
        this.branchNumber = branchNumber;
    }

    @Override
    public String toString() {
        return "Branch{" +
                "lineNumber=" + lineNumber +
                ", branchNumber=" + branchNumber +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Branch branch = (Branch) o;
        return lineNumber == branch.lineNumber && branchNumber == branch.branchNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(lineNumber, branchNumber);
    }
}

class sortByCoverage implements Comparator<TestCaseBranchDetails>{

    @Override
    public int compare(TestCaseBranchDetails o1, TestCaseBranchDetails o2) {
        if (o1.branchCoverage > o2.branchCoverage) {
            return -1;
        } else if (o1.branchCoverage < o2.branchCoverage) {
            return 1;
        }
        return o2.getVisitedBranches().size()-o1.getVisitedBranches().size();
    }
}
