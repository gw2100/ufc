package us.galleryw.ufc.backend;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class Vote implements Serializable, Cloneable {

    private Long id;
    
    private UglyFace uglyFace;
    private User voter;

    private String votingIp;
    private Date votingDate;

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
    public void setVoter(User voter) {
        this.voter = voter;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

}
