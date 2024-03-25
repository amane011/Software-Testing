import java.io.*;
import java.util.HashMap;
import java.util.Objects;

public class TestChecker {
    public static void main(String[] args) throws IOException {
        int count=0;
            int[] correctValue={2,1,0,0,0,0,0,0,0,0,57};
        int i=0;

        File Mainfolders = new File("/Users/ankitmane/Documents/SoftwareTesting/Project/src/main/resources/test");
        for (File f : Objects.requireNonNull(Mainfolders.listFiles())){
            HashMap<Integer,Integer> map=new HashMap<>();
            BufferedReader reader = new BufferedReader(new FileReader(f.getAbsolutePath()));
            String line;
            int a= Integer.parseInt(f.getAbsolutePath().replaceAll("\\D+", ""));
            while ((line = reader.readLine()) != null){
                if (line.matches("\\d+")){
                   if(map.containsKey(Integer.parseInt(line))){
                       map.replace(Integer.parseInt(line),map.get(Integer.parseInt(line))+1);
                   }else{
                       map.put(Integer.parseInt(line),1);
                   }
                }
            }
            System.out.println(map.toString() + " "+f.getName());
            i++;
        }
        System.out.println(count);
    }
}
