package model;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    @Test
    public void testAddSubTaskToEpicSelf() { //проверка на добавление Epic в качестве подзадачи
        Epic epic = new Epic("Epic1", "Description1");

        assertThrows(IllegalArgumentException.class, () -> {
            SubTask subTask = new SubTask("SubTask1", Status.NEW, "Description1", epic);
            epic.addTask(subTask);
        });
    }
}