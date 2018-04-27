package processors;

import java.io.*;
import java.util.ArrayList;

public class CreateWordArray{
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
