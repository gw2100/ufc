package us.galleryw.ufc.backend;

import java.util.List;

public interface UserService {
    List<User> findAll(String stringFilter);

    long count();

    void delete(User value);

    void save(User entry);
}
