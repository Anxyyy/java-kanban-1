package service;

import model.Status;
import model.Task;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    HistoryManager historyManager = new InMemoryHistoryManager();

    @Test
    public void testTaskHistoryPreservation() {
        Task originalTask = new Task("Тестовая задача", Status.NEW, "Проверить тестовую задачу");

        historyManager.addAll(originalTask);

        Task retrievedTask = historyManager.getHistory().get(0);

        assertEquals(originalTask.getId(), retrievedTask.getId());
        assertEquals(originalTask.getName(), retrievedTask.getName());
        assertEquals(originalTask.getStatus(), retrievedTask.getStatus());
        assertEquals(originalTask.getDescription(), retrievedTask.getDescription());
    }
}