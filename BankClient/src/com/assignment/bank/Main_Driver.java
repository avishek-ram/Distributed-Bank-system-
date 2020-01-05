
package com.assignment.bank;

import BankServerApp.BankServer;
import java.awt.Font;
import java.awt.Rectangle;
import java.io.File;
import org.omg.CORBA.ORB;
 
public class Main_Driver {
    
    static String fName = "./src/com/assignment/bank/fonts/display_font.ttf";
    static File font_file = null;
    static Font font = null;
    static final Integer IN_SML = 4;
    static final Integer IN_MED = 6;
    static final Integer IN_LRG = 8;
    static final Integer NEW_TRY = 3;
    static Numpad_Panel new_num;
    static Option_Panel new_opt;
    static Integer acc_num;
    static Integer tries;
    static String opt;
    static String date_srt;
    static String date_end;
    static ORB orb = null;
    static BankServer bankObj = null;
    static Rectangle position = null;
    static Boolean start = true;
    
    static void fun_acc_num()
    {
        opt = "acc_ck";
        Numpad_Panel.in_size = IN_MED;
        Numpad_Panel.in_disp = "<html><p style=\"text-align:center;\"><html> ENTER ACCOUNT NUMBER <br><br>";
        new_num = new Numpad_Panel();
        Numpad_Panel.in.clear();
        new_num.setVisible(true);
        new_num.input();
    }
    
    static void fun_acc_pin()
    {
        opt = "pin_ck";
        tries = NEW_TRY;
        Numpad_Panel.in_size = IN_SML;
        Numpad_Panel.in_disp = "<html><p style=\"text-align:center;\"><html> ENTER ACCOUNT PIN <br><br>TRIES REMAINING : "+tries +"<br><br>";
        new_num = new Numpad_Panel();
        Numpad_Panel.in.clear();
        new_num.setVisible(true);
        new_num.input();
    }
    
    static void fun_depo()
    {
        opt = "depo";
        Numpad_Panel.in_size = IN_MED;
        Numpad_Panel.in_disp = "<html><p style=\"text-align:center;\"> ENTER DEPOSIT AMOUNT <br><br>FJD ";
        new_num = new Numpad_Panel();
        Numpad_Panel.in.clear();
        new_num.setVisible(true);
        new_num.input();
    }
    
    static void fun_with()
    {
        opt = "with";
        Numpad_Panel.in_size = IN_MED;
        Numpad_Panel.in_disp = "<html><p style=\"text-align:center;\"> ENTER WITHDRAW AMOUNT <br><br>FJD ";
        new_num = new Numpad_Panel();
        Numpad_Panel.in.clear();
        new_num.setVisible(true);
        new_num.input();
    }
    
    static void fun_trans()
    {
        opt = "trans";
        new Record_Panel().setVisible(true);
    }
     
    public static void main(String[] args)throws Exception {
        orb = ORB.init(args, null);
        font_file = new File(fName);
        System.out.print(""+font_file.getPath());
        font = Font.createFont(Font.TRUETYPE_FONT, font_file);
        Main_Driver main = new Main_Driver();
        new Home_Panel().setVisible(true);
    }
}
