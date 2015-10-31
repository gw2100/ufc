package us.galleryw.ufc.backend;

import java.io.Serializable;

public interface VoteService {
    void delete(Vote value);
    void delete(Serializable id);
    void save(Vote entry);
}
