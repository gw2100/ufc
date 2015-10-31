package us.galleryw.ufc.backend;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Version;

import org.apache.commons.beanutils.BeanUtils;



@Entity
public class UglyFace implements Serializable, Cloneable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private User owner;
    @OneToMany(cascade=CascadeType.ALL,mappedBy="uglyFace")
    private Set<Vote> votes=new HashSet<Vote>();
    @Column(length = 1000000)
    private byte[] image;
    @Column(length = 500000)
    private byte[] thumbnail;

    private Date uploadDate;
    private String name="";
    private String description="";
    private Set<Comment> comments=new HashSet<Comment>();
    @Version
    private Integer version;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Set<Vote> getVotes() {
        return votes;
    }
    public void setVotes(Set<Vote> votes) {
        this.votes = votes;
    }
    public byte[] getImage() {
        return image;
    }
    public void setImage(byte[] image) {
        this.image = image;
    }
    public Date getUploadDate() {
        return uploadDate;
    }
    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }
    @Override
    public UglyFace clone() throws CloneNotSupportedException {
        try {
            return (UglyFace) BeanUtils.cloneBean(this);
        } catch (Exception ex) {
            throw new CloneNotSupportedException();
        }
    }
    public User getOwner() {
        return owner;
    }
    public void setOwner(User owner) {
        this.owner = owner;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public byte[] getThumbnail() {
        return thumbnail;
    }
    public void setThumbnail(byte[] imageThumbnail) {
        this.thumbnail = imageThumbnail;
    }
    public Vote hasVoteBy(User user){
        for(Vote v:getVotes()){
            if(v.getVoter().equals(user))
                return v;
        }
        return null;
    }
    
    public long getVoteCount(){
        return getVotes().size();
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
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
        UglyFace other = (UglyFace) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "UglyFace [getId()=" + getId() + ", getOwner()=" + getOwner() + ", getName()=" + getName() + ", getVoteCount()="
                + getVoteCount() + "]";
    }
    public Integer getVersion() {
        return version;
    }
    public void setVersion(Integer version) {
        this.version = version;
    }
    public Set<Comment> getComments() {
        return comments;
    }
    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }



}
