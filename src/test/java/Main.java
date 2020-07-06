import DB.Managment.HeroManager;
import DB.UserDB;
import DB.components.User;
import DB.components.cards.Deck;
import GUI.Frames.GameFrame;
import Game.CommandAndResponse.GameProcessor;
import Game.GameStructure.Game;
import Game.GameStructure.Player;


import java.io.*;
import java.util.Scanner;


public class Main implements Serializable {



    public static void main(String[] args) throws Exception {
        //*
        User.user = UserDB.getUser("", "");
        GameFrame.launch(new GameProcessor("config1"), 0);
        /*/
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String name = scanner.nextLine();
            name = name.toLowerCase();
            name = name.replace(" ", "");
            name = name.replace("\'", "");
            System.out.println(name);
        }
        //*/
    }
}
