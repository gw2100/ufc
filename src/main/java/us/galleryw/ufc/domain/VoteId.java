package us.galleryw.ufc.backend;

import java.io.Serializable;

public class VoteId implements Serializable{

    private Long uglyFaceId;
    private Long voterId;

    public VoteId(){}
    public VoteId(Long uglyFaceId,Long voterId){
        this.uglyFaceId=uglyFaceId;
        this.voterId=voterId;
    }
    
    public static VoteId newInstance(Long uglyFaceId,Long voterId){
        return new VoteId(uglyFaceId,voterId);
    }

    @Override
    public int hashCode() { 
        return toString().hashCode();
    }
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof VoteId){
            VoteId u=(VoteId)obj;
            return getVoterId().equals(u.getVoterId())&&getUglyFaceId().equals(u.getUglyFaceId());
        }
        return false;
    }
    @Override
    public String toString() {
        return "voterId="+getVoterId()+","+"uglyFaceId="+getUglyFaceId();
    }

    public void setUglyFaceId(Long uglyFaceId) {
        this.uglyFaceId = uglyFaceId;
    }
    public void setVoterId(Long voterId) {
        this.voterId = voterId;
    }
    public Long getUglyFaceId() {
        return uglyFaceId;
    }
    public Long getVoterId() {
        return voterId;
    }
    
}