package com.example.ttversion1.login.entity;


import com.example.ttversion1.login.rule.ValidEmail;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

@Entity(name = "help")
@Table
@Data
public class Help {
    public static final int EXPIRATION = 60 ;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID;
    @Column(unique = true)
    @ValidEmail
    private String email;
    @Column
    private String otp;

    @ManyToOne
    @JoinColumn(name = "accountID",foreignKey = @ForeignKey(name = "fk_help_account"))
    private Account account;
    private Date expiryDate;
    public Help() {
        super();
        this.expiryDate = calculateExpiryDate(EXPIRATION);
    }
    public Help(String otp, Account account,String email) {
        this.email=email;
        this.otp = otp;
        this.account = account;
        this.expiryDate = calculateExpiryDate(EXPIRATION);
    }
    private Date calculateExpiryDate(int expiryTimeInMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }

}
