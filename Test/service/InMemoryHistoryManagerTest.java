package service;

import model.Status;
import model.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    HistoryManager historyManager = new InMemoryHistoryManager();

    @Test
    public void testTaskHistoryWithoutDuplicates() {
        Task task = new Task("Тестовая задача", Status.NEW, "Проверка задачи");

        historyManager.addAll(task);
        historyManager.addAll(task);  // Дубль

        assertEquals(1, historyManager.getHistory().size());
    }
}
