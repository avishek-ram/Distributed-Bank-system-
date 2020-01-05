//Getter and setter mapping  for hybernate to map the class to the database table

package Hibernate;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Accounts implements Serializable {
    @Id
    private int acc_num;
    private int acc_pin;
    private double acc_bal;
    private int acc_status;

    public int getAcc_num() {
        return acc_num;
    }

    public void setAcc_num(int acc_num) {
        this.acc_num = acc_num;
    }

    public int getAcc_pin() {
        return acc_pin;
    }

    public void setAcc_pin(int acc_pin) {
        this.acc_pin = acc_pin;
    }

    public double getAcc_bal() {
        return acc_bal;
    }

    public void setAcc_bal(double acc_bal) {
        this.acc_bal = acc_bal;
    }

    public int getAcc_status() {
        return acc_status;
    }

    public void setAcc_status(int acc_status) {
        this.acc_status = acc_status;
    }
    
}
