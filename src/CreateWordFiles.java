import java.io.*;
import java.util.ArrayList;

public class CreateWordFiles{
    public static void main(String args[]){
        //get current working directory
        String dir = System.getProperty("user.dir");
        try (BufferedReader br = new BufferedReader(new FileReader(dir + File.separator + "words.txt"))){
            String word_dir_path = dir + File.separator + "files";
            File word_dir = new File(word_dir_path);
            word_dir.mkdir();
            String word = br.readLine();
            while(word != null){
                writeCharFile(word, word_dir_path);
                word = br.readLine();
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void writeCharFile(String s, String fpath){
        char first_letter = s.charAt(0);
        first_letter = Character.toUpperCase(first_letter);
        try {
            File f = new File(fpath + File.separator + first_letter + ".txt");
            if(!f.exists()){
                f.createNewFile();
            }
            FileWriter fw = new FileWriter(f.getName(), true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(s + "\n");
            bw.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }

    }
}
