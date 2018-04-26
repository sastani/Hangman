package processor;

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
                //discard one letter entries
                if(word.length() > 1) {
                    writeCharFile(word, word_dir_path);
                }
                word = br.readLine();
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    private static void writeCharFile(String s, String dir_path){
        char first_letter = s.charAt(0);
        first_letter = Character.toUpperCase(first_letter);
        String fpath = dir_path + File.separator + first_letter + ".txt";
        try {
            File f = new File(fpath);
            if(!f.exists()){
                f.createNewFile();
            }
            FileWriter fw = new FileWriter(fpath, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(s + "\n");
            bw.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }

    }

    public static ArrayList<String> createWordList(String file){
        ArrayList<String> word_list = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String word = br.readLine();
            while(word != null){
                word_list.add(word);
                word = br.readLine();
            }
            br.close();
        }
        catch (FileNotFoundException e){
            System.out.println(e);
        }
        catch (IOException i){
            System.out.println(i);
        }
        return word_list;

    }
}
