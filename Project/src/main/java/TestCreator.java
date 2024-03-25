import java.io.IOException;

public class TestCreator {
    public static void main(String[] args) throws IOException {
        if(args.length==3){
            FilePaths.jsonPath=args[0];
            FilePaths.UniverseJson =args[1];
            FilePaths.inputFile =args[2];
        }
        RandomCoverageStatement.getTests();
        RandomCoverageBranch.getTests();
        TotalCoverageStatement.getTests();
        TotalCoverageBranches.getTests();
        AdditionalCoverageStatement.getTests();
        AdditionalCoverageBranch.getTests();


    }
}
