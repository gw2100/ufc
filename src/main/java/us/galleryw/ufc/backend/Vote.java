package us.galleryw.ufc.backend;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Vote implements Serializable, Cloneable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne(targetEntity = UglyFace.class)
    private Long uglyFaceId;
    @OneToOne(targetEntity = User.class)
    private Long userId;
    private String votingIp;
    private Date votingDate;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getUglyFaceId() {
        return uglyFaceId;
    }
    public void setUglyFaceId(Long uglyFaceId) {
        this.uglyFaceId = uglyFaceId;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
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
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((uglyFaceId == null) ? 0 : uglyFaceId.hashCode());
        result = prime * result + ((userId == null) ? 0 : userId.hashCode());
        result = prime * result + ((votingDate == null) ? 0 : votingDate.hashCode());
        result = prime * result + ((votingIp == null) ? 0 : votingIp.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Vote other = (Vote) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (uglyFaceId == null) {
            if (other.uglyFaceId != null)
                return false;
        } else if (!uglyFaceId.equals(other.uglyFaceId))
            return false;
        if (userId == null) {
            if (other.userId != null)
                return false;
        } else if (!userId.equals(other.userId))
            return false;
        if (votingDate == null) {
            if (other.votingDate != null)
                return false;
        } else if (!votingDate.equals(other.votingDate))
            return false;
        if (votingIp == null) {
            if (other.votingIp != null)
                return false;
        } else if (!votingIp.equals(other.votingIp))
            return false;
        return true;
    }

}
