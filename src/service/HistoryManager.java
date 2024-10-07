package service;

import model.Task;
import java.util.List;

public interface HistoryManager {
    void addAll(Task task);

    List<Task> getHistory();
}