package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SubTaskTest {
    @Test
    public void checkingForComplianceWithEpicAndSabtask() { //проверка что подзадача не является эпиком
        SubTask subTask = new SubTask("test", null, "test");
        Epic epic = new Epic("test", null, "test");

        assertNotEquals(subTask, epic);
    }
}