package web;

import processor.CreateWordArray;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by sinaastani on 4/26/18.
 */
final class GamesInit {

    private ArrayList<String> word_list;

    public GamesInit() {
        String dir = System.getProperty("user.dir");
        String fpath = dir + File.separator + "bogwords.txt";
        ArrayList<String> wlist = CreateWordArray.createWordList(fpath);
        this.word_list = wlist;
    }

    public ArrayList<String> getWordList(){
        return this.word_list;
    }

}
