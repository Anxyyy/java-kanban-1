package service;

import model.Status;
import model.Task;
import model.SubTask;
import model.Epic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    InMemoryTaskManager taskManager;

    @BeforeEach
    public void setup() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    public void testCreateTask() {
        Task task = new Task("Тестовая задача", Status.NEW, "Описание");
        taskManager.createTask(task);

        assertEquals(1, taskManager.getAllTasks().size());
    }

    @Test
    public void testDeleteTaskFromHistory() {
        Task task = new Task("Тестовая задача", Status.NEW, "Описание");
        taskManager.createTask(task);

        taskManager.deleteTask(task.getId());

        assertEquals(0, taskManager.getAllTasks().size());
        assertTrue(taskManager.getHistory().isEmpty());  // История должна быть пустой
    }

    @Test
    public void testEpicWithSubTask() {
        Epic epic = new Epic("Тестовый эпик", "Описание эпика");
        taskManager.createEpic(epic);

        SubTask subTask = new SubTask("Тестовая подзадача", Status.NEW, "Описание подзадачи");
        subTask.setEpic(epic);
        taskManager.createSubTask(subTask);

        assertEquals(1, taskManager.getAllSubTasksForEpic(epic.getId()).size());
    }

    @Test
    public void testDeleteEpicShouldRemoveSubTasks() {
        Epic epic = new Epic("Тестовый эпик", "Описание эпика");
        taskManager.createEpic(epic);

        SubTask subTask = new SubTask("Тестовая подзадача", Status.NEW, "Описание подзадачи");
        subTask.setEpic(epic);
        taskManager.createSubTask(subTask);

        taskManager.deleteEpicById(epic.getId());

        assertEquals(0, taskManager.getAllSubTasksForEpic(epic.getId()).size());
    }
}
