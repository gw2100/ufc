package us.galleryw.ufc.backend;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class Vote implements Serializable, Cloneable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private UglyFace uglyFace;
    @ManyToOne
    private User voter;
    private String votingIp;
    private Date votingDate;
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getVotingIp() {
        return votingIp;
    }
    public void setVotingIp(String votingIp) {
        this.votingIp = votingIp;
    }
    public Date getVotingDate() {
        return votingDate;
    }
    public void setVotingDate(Date votingDate) {
        this.votingDate = votingDate;
    }
    public UglyFace getUglyFace() {
        return uglyFace;
    }
    public void setUglyFace(UglyFace uglyFace) {
        this.uglyFace = uglyFace;
    }
    public User getVoter() {
        return voter;
    }
    public void setVoterr(User voter) {
        this.voter = voter;
    }
 

}
