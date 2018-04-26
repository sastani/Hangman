package models;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import processor.CreateWordFiles;

/**
 * Created by sinaastani on 4/24/18.
 */
public class Game {
    public static String chooseWord(){
        String chosen_word = "";
        String dir = System.getProperty("user.dir");
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        //generate a random number between 0 and 25
        Random r = new Random(System.currentTimeMillis());
        int ran_int = r.nextInt(26);
        //use number to choose a character
        char letter = alphabet.charAt(ran_int);
        System.out.println(letter);
        String fpath = dir + File.separator + "files" + File.separator + letter + ".txt";
        System.out.println(fpath);
        int num_lines = 0;

        try {
            BufferedReader br = new BufferedReader(new FileReader(fpath));
            while(br.readLine() != null){
                num_lines++;
            }
            br.close();
            //generate random line number
            ran_int = r.nextInt(num_lines);
            //get word at random line
            br = new BufferedReader(new FileReader(fpath));
            for(int i=0; i < (num_lines-1); i++){
                br.readLine();
            }
            chosen_word = br.readLine();
            br.close();
        }
        catch (FileNotFoundException e){
            System.out.println(e);
        }
        catch (IOException i){
            System.out.println(i);
        }

        return chosen_word;
    }
    public static void main(String[] args){
        //System.out.println(chooseWord());
        String dir = System.getProperty("user.dir");
        String fpath = dir + File.separator + "bogwords.txt";
        CreateWordFiles wf = new CreateWordFiles();
        ArrayList<String> wlist = wf.createWordList(fpath);

    }
}
