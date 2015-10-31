package us.galleryw.ufc.backend;

import java.io.Serializable;
import java.util.List;

public interface UglyFaceService {
    List<UglyFace> findAll(String stringFilter);
    UglyFace findById(Serializable id);
    long count();

    void delete(UglyFace value);
    void delete(Serializable id);

    Serializable save(UglyFace entry);
     void persist(UglyFace entry);
     UglyFace merge(UglyFace entry);
}
