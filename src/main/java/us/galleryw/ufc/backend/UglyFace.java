package us.galleryw.ufc.backend;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
 public class UglyFace  implements Serializable, Cloneable {
     @Id
     @GeneratedValue(strategy = GenerationType.AUTO)
     private Long id;
     @ManyToOne(targetEntity=User.class)
     private Long userId;
     @OneToMany(targetEntity=Vote.class)
     private Set votes;
     private Byte[] image;
     private Date uploadDate;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public Set getVotes() {
        return votes;
    }
    public void setVotes(Set votes) {
        this.votes = votes;
    }
    public Byte[] getImage() {
        return image;
    }
    public void setImage(Byte[] image) {
        this.image = image;
    }
    public Date getUploadDate() {
        return uploadDate;
    }
    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }
}
