package service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {
    @Test
    public void testGetDefaultTaskManager() { //Проверка TaskManager на инициализацию
        InMemoryTaskManager taskManager = (InMemoryTaskManager) Managers.getDefault();
        assertNotNull(taskManager, "TaskManager должен быть проинициализирован");
    }

    @Test
    public void testGetDefaultHistoryManager() { //Проверка HistoryManager на инициализацию
        InMemoryHistoryManager historyManager = (InMemoryHistoryManager) Managers.getDefaultHistory();
        assertNotNull(historyManager, "HistoryManager должен быть проинициализирован");
    }
}