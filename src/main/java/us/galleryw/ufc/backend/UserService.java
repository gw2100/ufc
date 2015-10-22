package us.galleryw.ufc.backend;

import java.io.Serializable;
import java.util.List;

public interface UserService {
    List<User> findAll(String stringFilter);

    long count();

    void delete(User value);
    void delete(Serializable id);

    void save(User entry);

    User authenticate(String userName, String password);
}
