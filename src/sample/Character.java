package sample;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;


public class Character {

    public int hp=this.hp;
    public int defense = this.defense;
    public int armour = this.armour;
    public int attack = this.attack;
    public int stamina= this.stamina;


    public Character(int hp, int def, int armour, int attack, int stamina) {

    }

    public void Attack(Character enemy) throws IOException {
        if(this.attack > enemy.defense) {
          //  String prev_hp = enemy.hp;
            //Socket other_player = new Socket("");
            enemy.hp = enemy.hp - (this.attack - enemy.defense);
            FileReader fileReader = new FileReader("character.txt");
            BufferedReader input = new BufferedReader(fileReader);
            String line = null;
            while ((line=input.readLine())!=null){
                if(line=="hp"){
                    Scanner scan = new Scanner(fileReader);
                    String current_hp=scan.next();
                }

            }
            //todo file io to the char info file
        }
        else{//todo add output to UI for dmg dealt
             }
        return;



    }
    public void moves(Character enemy) {
        if (this.attack > enemy.defense) {
            enemy.hp = enemy.hp - (this.attack - enemy.defense);
            //FileWriter write = new FileWriter();
            //todo file io to the char info file
        }
        else {//todo add output to UI for dmg dealt
        }
        return;
    }

}
