//Getter and setter mapping for hybernate to map the class to the database table
package Hibernate;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Transactions"
)
public class Transactions  implements Serializable {


     private int transId;
     private int accNum;
     private String transDatetime;
     private String transType;
     private String transDetails;

    public Transactions() {
    }

    public Transactions(int accNum, String transDatetime, String transType, String transDetails) {
       this.accNum = accNum;
       this.transDatetime = transDatetime;
       this.transType = transType;
       this.transDetails = transDetails;
    }
   
     @Id @GeneratedValue(strategy=IDENTITY)

    
    @Column(name="trans_id", unique=true, nullable=false)
    public int getTransId() {
        return this.transId;
    }
    
    public void setTransId(int transId) {
        this.transId = transId;
    }

    @Column(name="acc_num", nullable=false)
    public int getAccNum() {
        return this.accNum;
    }
    
    public void setAccNum(int accNum) {
        this.accNum = accNum;
    }

    
    @Column(name="trans_datetime", nullable=false, length=2000000000)
    public String getTransDatetime() {
        return this.transDatetime;
    }
    
    public void setTransDatetime(String transDatetime) {
        this.transDatetime = transDatetime;
    }

    
    @Column(name="trans_type", nullable=false, length=2000000000)
    public String getTransType() {
        return this.transType;
    }
    
    public void setTransType(String transType) {
        this.transType = transType;
    }

    
    @Column(name="trans_details", nullable=false, length=2000000000)
    public String getTransDetails() {
        return this.transDetails;
    }
    
    public void setTransDetails(String transDetails) {
        this.transDetails = transDetails;
    }

}


