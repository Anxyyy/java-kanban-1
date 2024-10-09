import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import service.InMemoryTaskManager;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();

        // Создаем задачи
        Task task1 = new Task("Задача 1", Status.NEW, "Сделать задачу 1");
        Task task2 = new Task("Задача 2", Status.DONE, "Сделать задачу 2");
        Epic epicWithSubtasks = new Epic("Эпик", Status.IN_PROGRESS, "Сделать подзадачи");

        SubTask subTask1 = new SubTask("Подзадача 1", Status.DONE, "Сделать подзадачу 1", epicWithSubtasks);
        SubTask subTask2 = new SubTask("Подзадача 2", Status.IN_PROGRESS, "Сделать подзадачу 2", epicWithSubtasks);
        SubTask subTask3 = new SubTask("Подзадача 3", Status.NEW, "Сделать подзадачу 3", epicWithSubtasks);

        // Создаем и добавляем задачи в менеджер
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createEpic(epicWithSubtasks);
        taskManager.createSubTask(subTask1);
        taskManager.createSubTask(subTask2);
        taskManager.createSubTask(subTask3);

        // Запросим задачи в разном порядке
        taskManager.getTask(task1.getId());
        taskManager.getSubTask(subTask1.getId());
        taskManager.getEpic(epicWithSubtasks.getId());
        taskManager.getSubTask(subTask2.getId());
        taskManager.getTask(task1.getId());
        taskManager.getSubTask(subTask1.getId());
        taskManager.getEpic(epicWithSubtasks.getId());

        // Выводим историю
        List<Task> history = taskManager.getHistory();
        System.out.println("История после первых запросов: " + history);

        // Удаляем задачу
        taskManager.deleteTask(task1.getId());
        System.out.println("История после удаления задачи 1: " + taskManager.getHistory());

        // Удаляем эпик и подзадачи
        taskManager.deleteEpicById(epicWithSubtasks.getId());
        System.out.println("История после удаления эпика 1: " + taskManager.getHistory());
    }
}

