package BankServer;


import BankServerApp.*;
import Hibernate.Accounts;
import Hibernate.HibernateUtil;
import Hibernate.Transactions;
import java.time.LocalDateTime;
import static java.time.LocalDateTime.parse;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;
import static java.util.Objects.isNull;
import org.omg.CORBA.*;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;

public class BankServerObj extends BankServerPOA{
    
    private ORB orb;
 
    public void setORB(ORB orb_val) {
      orb = orb_val; 
    }
    
    org.hibernate.Session session = HibernateUtil.getSessionFactory().openSession();

    @Override
    /**
        * @desc checks if the account number given by the user exists in the database
        * @param int acntNum - the account number to be checked
        * @return bool - account number exists or does not exist
    */
    public boolean checkAcntNum(int acntNum) {
        Accounts account = (Accounts) session.get( Accounts.class, acntNum);
        boolean bool = false;
        if (isNull(account))
            bool = false;
        else
            bool = true;
        return bool;
    }

    @Override
    /**
        * @desc checks the status of the account
        * @param int acntNum - the account number to be checked
        * @return bool - active or locked
    */
    public boolean checkAcntStatus(int acntNum) {
        Accounts account = (Accounts) session.get( Accounts.class, acntNum);
        boolean bool = false;
        int accStatus = 1;
        if (account.getAcc_status() == accStatus)
            bool = true;
        else
            bool = false;
        return bool;
    }

    @Override
    /**
        * @desc verifies the pin number entered by the user for the account number
        * @param int acntNum - the account number
        * @param int acntPin - the pin number
        * @return bool - success or failure
    */
    public boolean verifyAcntPin(int acntNum, int acntPin) {
        Accounts account = (Accounts) session.get( Accounts.class, acntNum);
        boolean bool = false;
        if (account.getAcc_pin() != acntPin)
            bool = false;
        else
            bool = true;
        return bool;
    }

    @Override
    /**
        * @desc sets the account to locked status
        * @param int acntNum - the account number to be locked
        * @return void
    */
    public void lockAcnt(int acntNum) {
        Transaction t = session.beginTransaction();
        Accounts account = (Accounts) session.get( Accounts.class, acntNum);
        int accStatus = 0;
        account.setAcc_status(accStatus);
        session.save(account);
        t.commit();
    }

    @Override
    /**
        * @desc deposits the amount entered by the user into the account number and saves the transaction details
        * @param int acntNum - the user account number
        * @param int amount - the amount to be deposited
        * @return void
    */
    public void makeDeposit(int acntNum, int amount) {
        Transaction t1 = session.beginTransaction();
        try {
            Accounts account = (Accounts) session.get(Accounts.class,acntNum);
            double newBal = account.getAcc_bal() + amount;
            account.setAcc_bal(newBal);
            session.save(account);
            t1.commit();
            
        } catch (HibernateException e) {
            e.printStackTrace();
            t1.rollback();
        } 
        
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String datetime = now.format(formatter);

        Transaction t2 = session.beginTransaction();
        try {
            Transactions trans = new Transactions();
            trans.setAccNum(acntNum);
            trans.setTransDatetime(datetime);
            trans.setTransType("Deposit");
            trans.setTransDetails("Deposited $" + amount);
            session.save(trans);
            t2.commit();
        } catch (HibernateException e) {
            e.printStackTrace();
            t2.rollback();
        }
    }

    @Override
    /**
        * @desc withdraws the amount entered by the user from the bank account and saves the transaction details
        * @param int acntNum - the user account number
        * @param int amount - the amount to be withdrawn
        * @return void
    */
    public void makeWithdrawal(int acntNum, int amount) {
        Transaction t1 = session.beginTransaction();
        try {
            Accounts account = (Accounts) session.get(Accounts.class,acntNum);
            double newBal = account.getAcc_bal() - (int) amount;
            account.setAcc_bal(newBal);
            session.save(account);
            t1.commit();
            } catch (HibernateException e) {
                e.printStackTrace();
                t1.rollback();
            } 
        
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String datetime = now.format(formatter);

        Transaction t2 = session.beginTransaction();
        try {
            Transactions trans = new Transactions();
            trans.setAccNum(acntNum);
            trans.setTransDatetime(datetime);
            trans.setTransType("Withdraw");
            trans.setTransDetails("Withdrawn $" + amount);
            session.save(trans);
            t2.commit();
        } catch (HibernateException e) {
            e.printStackTrace();
            t2.rollback();
        }
    }

    @Override
    /**
        * @desc checks the account balance
        * @param int acntNum - the account number
        * @return Account_Balance - the account balance
    */
    public double checkBalance(int acntNum) {
        Accounts account = (Accounts) session.get( Accounts.class, acntNum);
        double Account_Balance = account.getAcc_bal();
        return Account_Balance;
    }

    @Override
    /**
        * @desc checks if the account is in overdraft
        * @param int acntNum - the account number
        * @param double amount - the amount
        * @return bool - success of failure
    */
    public boolean checkOverdraft(int acntNum, double amount) {
        boolean bool = false;
        if((checkBalance(acntNum) - amount) < 0){
            bool = true;
        }
        else 
            bool = false;
        return bool;
    }

    @Override
    /**
        * @desc gets transactions from a start date to end date from the database and send an email with the list of transactions
        * @param int acntNum - the account number
        * @param int String startDate - the transaction start date
        * @param int String endDate - the transaction end date
        * @param int String emailAddr - the email address for sending the transactions to
        * @return void
    */
    public void getTransactions(int acntNum, String startDate, String endDate, String emailAddr) {
        String strAccount = Integer.toString(acntNum);
        LocalDateTime s_date = parse(startDate.replace(" ", "T"));
        LocalDateTime e_date = parse(endDate.replace(" ", "T"));
        String Results = new String();
        Results += "<table border=\"1\"><tr><th>Transaction ID</th><th>Transaction Time</th><th>Transaction Details</th></tr>";
        List results = session.createQuery("from Transactions where acc_num=" + strAccount).list();
        for (Iterator iter = results.iterator(); iter.hasNext();) {
            Transactions trans = (Transactions) iter.next();
            LocalDateTime t_date = parse(trans.getTransDatetime().replace(" ", "T"));
            if (t_date.isAfter(s_date) && t_date.isBefore(e_date)){
                Results += "<tr><td>" + trans.getTransId()+"</td><td>"+trans.getTransDatetime()+"</td><td>"+ trans.getTransDetails() + "</td></tr>";
            } 
        }
        Results += "</table>";
        
        Properties emailProperties;
	Session mailSession;
	MimeMessage emailMessage;
        
        String emailPort = "587";//gmail's smtp port

        emailProperties = System.getProperties();
        emailProperties.put("mail.smtp.port", emailPort);
        emailProperties.put("mail.smtp.auth", "true");
        emailProperties.put("mail.smtp.starttls.enable", "true");
        
        String[] toEmails = { emailAddr }; // Add your email here
        String emailSubject = "Transaction History";

        mailSession = Session.getDefaultInstance(emailProperties, null);
        emailMessage = new MimeMessage(mailSession);
        
        try {
            for (int i = 0; i < toEmails.length; i++) {
                emailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmails[i]));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        try {
            emailMessage.setSubject(emailSubject);
            emailMessage.setContent(Results, "text/html");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        String emailHost = "smtp.gmail.com";
        String fromUser = "bankapp1234@gmail.com";
        String fromUserEmailPassword = "//bankapp1234";
        try {
            Transport transport = mailSession.getTransport("smtp");

            transport.connect(emailHost, fromUser, fromUserEmailPassword);
            transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
            transport.close();
            System.out.println("Email sent successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
}
