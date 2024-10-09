package model;

import service.InMemoryTaskManager;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    InMemoryTaskManager manager = new InMemoryTaskManager();

    @Test
    void comparingTasksById() { //сравнение по id задач
        Task task1 = new Task(1, "Задача 1", Status.NEW, "Выполнить задачу 1");
        Task task2 = new Task(1, "Задача 2", Status.IN_PROGRESS, "Выполнить задачу 2");
        assertEquals(task1, task2);
    }

    @Test
    void сomparingTasksByFields() { //сравнение по полям задач
        Task task1 = new Task(1, "Задача 1", Status.NEW, "Выполнить задачу 1");
        Task task2 = new Task(2, "Задача 1", Status.NEW, "Выполнить задачу 1");
        assertEquals(task1, task2);
    }
}