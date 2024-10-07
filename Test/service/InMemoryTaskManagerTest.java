package service;

import model.Status;
import model.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    TaskManager taskManager = new InMemoryTaskManager();
    private Task testTask;

    @Test
    void testInMemoryTaskManager() { //проверка на добавление задач разного типа и поиска по id
        Task task1 = new Task(1, "Задача 1", Status.NEW, "Выполнить задачу 1");
        Task task2 = new Task(2, "Задача 2", Status.IN_PROGRESS, "Выполнить задачу 2");
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        assertEquals(task1, taskManager.getTask(1));
        assertEquals(task2, taskManager.getTask(2));
    }

    @Test
    public void testDifferentIds() { //проверка на конфликтность между задачами с заданными и генерируемыми id
        int givenId = 1;
        Task task1 = new Task("Задача 1", Status.NEW, "Выполнить задачу 1");
        task1.setId(givenId);
        taskManager.createTask(task1);

        Task task2 = new Task("Задача 2", Status.NEW, "Выполнить задачу 2");
        taskManager.createTask(task2);

        Task retrievedTask1 = taskManager.getTask(givenId);
        Task retrievedTask2 = taskManager.getTask(task2.getId());

        assertNotNull(retrievedTask1);
        assertNotNull(retrievedTask2);
        assertEquals(task1, retrievedTask1);
        assertEquals(task2, retrievedTask2);
    }

    @Test
    public void testAddTask() { //проверка на неизменность задачи при добавлении в менеджер
        taskManager = Managers.getDefault();
        testTask = new Task("Тестовая задача", Status.NEW, "Сделать тестовую задачу");

        Task addedTask = taskManager.createTask(testTask);

        assertNotNull(addedTask);
        assertEquals(testTask.getName(), addedTask.getName());
        assertEquals(testTask.getStatus(), addedTask.getStatus());
        assertEquals(testTask.getDescription(), addedTask.getDescription());

        assertEquals(testTask, addedTask);
    }
}