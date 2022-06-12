package store;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface BaseStore {

    void save(LocalDate date, String deal);
    List<String> selectAll(LocalDate date);

}
