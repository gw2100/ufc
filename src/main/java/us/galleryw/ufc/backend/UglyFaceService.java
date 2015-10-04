package us.galleryw.ufc.backend;

import java.io.Serializable;
import java.util.List;

public interface UglyFaceService {
    List<UglyFace> findAll(String stringFilter);

    long count();

    void delete(UglyFace value);
    void delete(Serializable id);

    void save(UglyFace entry);
}
