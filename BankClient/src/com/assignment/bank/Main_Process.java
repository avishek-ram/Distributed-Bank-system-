/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.assignment.bank;

import BankServerApp.BankServer;
import BankServerApp.BankServerHelper;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

/**
 *
 * @author Vj
 */
public class Main_Process {
    
    
    static void pros_connect() throws Exception
    {
        try {
            // fire to localhost port 1099
	    org.omg.CORBA.Object objRef = Main_Driver.orb.resolve_initial_references("NameService");
	    NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
	    Main_Driver.bankObj = (BankServer) BankServerHelper.narrow(ncRef.resolve_str("ABC"));
            
        } catch (Exception ex) {
            System.out.println("Bank Client exception: " + ex);
            ex.printStackTrace();
        }
    }
    
    static void pros_acc_num()
    {
        Integer multiplier = 1;
        Main_Driver.acc_num = 0;
        
        try {
            
            try 
            {
                pros_connect();
            } 
            catch (Exception ex) 
            {
                Logger.getLogger(Main_Driver.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            Integer[] in_val = Numpad_Panel.in.toArray(new Integer[Numpad_Panel.in.size()]);
            
            for (int i = Numpad_Panel.in.size()-1; i>=0; i--)
            {
                Main_Driver.acc_num = Main_Driver.acc_num + (in_val[i] * multiplier);
                multiplier = multiplier * 10;
            }
            
            if (Main_Driver.bankObj.checkAcntNum(Main_Driver.acc_num))
            {
                if (Main_Driver.bankObj.checkAcntStatus(Main_Driver.acc_num))
                {
                    Main_Driver.new_num.setVisible(false);
                    Main_Driver.fun_acc_pin();
                }   
                else
                {
                    Numpad_Panel.in_disp = "<html><p style=\"text-align:center;\"> THIS ACCOUNT IS LOCKED <br><br> PLEASE CHECK WITH OUR NEAREST BRANCH ";
                    Main_Driver.new_num.display_message();
                    Main_Driver.new_num.lock_panel();
                }
            }
            else
            {
                Numpad_Panel.in_disp = "<html><p style=\"text-align:center;\"> ACCOUNT DOES NOT EXIST <br><br> TRY AGAIN <br><br>";
                Numpad_Panel.in.clear();
                Main_Driver.new_num.input();
            }
                        
        }
        catch (Exception ex)
        {
        }
    }
    
    static void pros_acc_pin()
    {
        try
        {
            Integer multiplier = 1;
            Integer acc_pin = 0;
            Integer last_atm = 1;
            
            try
            {
                pros_connect();
            }
            catch (Exception ex)
            {
                Logger.getLogger(Main_Driver.class.getName()).log(Level.SEVERE, null, ex);
            }
            Integer[] in_val = Numpad_Panel.in.toArray(new Integer[Numpad_Panel.in.size()]);
            for (int i = Numpad_Panel.in.size()-1; i>=0; i--)
            {
                acc_pin = acc_pin + (in_val[i] * multiplier);
                multiplier = multiplier * 10;
            }
            
            if (Main_Driver.tries > last_atm)
            {
                if (Main_Driver.bankObj.verifyAcntPin(Main_Driver.acc_num,acc_pin))
                {
                    Menu_Panel.menu_disp = "<html><p style=\"text-align:center;\">";
                    new Menu_Panel().setVisible(true);
                    Main_Driver.new_num.setVisible(false);
                    acc_pin = 0;
                }
                else
                {
                    Main_Driver.tries--;
                    Numpad_Panel.in_disp = "<html><p style=\"text-align:center;\"><html> INCORRECT PIN <br><br> ENTER ACCOUNT PIN <br><br>TRIES REMAINING : "+Main_Driver.tries +"<br><br>";
                    Main_Driver.new_num.display_message();
                    Numpad_Panel.in.clear();
                    Main_Driver.new_num.input();  
                }
            }
            else
            {
                Main_Driver.bankObj.lockAcnt(Main_Driver.acc_num);
                Numpad_Panel.in_disp = "<html><p style=\"text-align:center;\"><html> TOO MANY FAILED ATTEMPS <br><br> ACCOUNT IS NOW LOCKED <br><br> PLEASE CHECK WITH OUR NEAREST BRANCH ";
                Main_Driver.new_num.display_message();
                Main_Driver.new_num.lock_panel();
            }
        }
        catch (Exception ex)
        {
            Logger.getLogger(Main_Process.class.getName()).log(Level.SEVERE, null, ex);
        }
            
    }
    
    static void pros_depo()
    {
        
        Integer multiplier = 1;
        Integer dept_val = 0;
        
        try
        {
            try
            {
                pros_connect();
            }
            catch (Exception ex)
            {
                Logger.getLogger(Main_Driver.class.getName()).log(Level.SEVERE, null, ex);
            }
            Integer[] in_val = Numpad_Panel.in.toArray(new Integer[Numpad_Panel.in.size()]);
            for (int i = Numpad_Panel.in.size()-1; i>=0; i--)
            {
                dept_val = dept_val + (in_val[i] * multiplier);
                multiplier = multiplier * 10;
            }
            
            Main_Driver.bankObj.makeDeposit(Main_Driver.acc_num, dept_val);
            Menu_Panel.menu_disp = "<html><p style=\"text-align:center;\"> DEPOSIT SUCCESSFUL <br><br> ";
            new Menu_Panel().setVisible(true);
            
        }
        catch (Exception ex)
        {
            Logger.getLogger(Main_Driver.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
    static void pros_with()
    {
        
        Integer multiplier = 1;
        Integer with_val = 0;
        
        try
        {
            try
            {
                pros_connect();
            }
            catch (Exception ex)
            {
                Logger.getLogger(Main_Driver.class.getName()).log(Level.SEVERE, null, ex);
            }
            Integer[] in_val = Numpad_Panel.in.toArray(new Integer[Numpad_Panel.in.size()]);
            for (int i = Numpad_Panel.in.size()-1; i>=0; i--)
            {
                with_val = with_val + (in_val[i] * multiplier);
                multiplier = multiplier * 10;
            }
            
            if (Main_Driver.bankObj.checkOverdraft(Main_Driver.acc_num, with_val))
            {
                Main_Driver.fun_with();
                Numpad_Panel.in_disp = "<html><p style=\"text-align:center;\"> WITHDRAW AMOUNT EXCEEDS BALANCE <br><br> ENTER NEW AMOUNT <br><br>FJD <html>";
                Main_Driver.new_num.display_message();
            }
            else
            {
                Main_Driver.bankObj.makeWithdrawal(Main_Driver.acc_num, with_val);
                Menu_Panel.menu_disp = "<html><p style=\"text-align:center;\"> WITHDRAWAL SUCCESSFUL <br><br> ";
                new Menu_Panel().setVisible(true);
            }
        }
        catch (Exception ex)
        {
            Logger.getLogger(Main_Driver.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
