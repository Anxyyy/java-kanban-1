import model.Status;
import model.Task;
import model.Epic;
import model.SubTask;
import service.InMemoryTaskManager;
import service.TaskManager;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new InMemoryTaskManager();

        //работа с задачами
        Task task1 = taskManager.createTask(new Task("Сдать проект", Status.NEW, "сдать проект ревьюеру"));
        System.out.println("Созданная задача 1: " + task1);

        Task task2 = taskManager.createTask(new Task("Тестирование", Status.DONE, "Проверить программу"));
        System.out.println("Созданная задача 2: " + task2);

        Task taskFromManager1 = taskManager.getTask(task1.getId());
        System.out.println("Задача 1: " + taskFromManager1);

        taskFromManager1.setName("Сдать проект побыстрее");
        taskFromManager1.setDescription("Сдать проект ревьюеру еще быстрее");
        taskManager.updateTask(taskFromManager1);
        System.out.println("Обновленная задача 1: " + taskFromManager1);

        taskManager.deleteTask(taskFromManager1.getId());
        System.out.println("Задача 1: " + task1 + " удалена!");

        System.out.println("Задачи после удаления: " + taskManager.getAllTasks());

        //работа с эпиком1 и двумя подзадачами
        Epic epic1 = taskManager.createEpic(new Epic("Важные дела", "сделать все важные дела"));
        System.out.println("Созданный эпик 1: " + epic1);

        SubTask subTask1 = taskManager.createSubTask(new SubTask("Важное дело 1", Status.NEW, "Сделать важное дело 1", epic1));
        SubTask subTask2 = taskManager.createSubTask(new SubTask("Важное дело 2", Status.IN_PROGRESS, "Сделать важное дело 2", epic1));
        System.out.println("Созданная подзадача 1: " + subTask1);
        System.out.println("Созданная подзадача 2: " + subTask2);

        System.out.println("Подзадачи эпика 1: " + taskManager.getAllSubTasksForEpic(3));

        taskManager.deleteSubTask(4);

        System.out.println("Подзадачи эпика 1 после удаления одной из подзадач: " + taskManager.getAllSubTasksForEpic(3));

        //работа с эпиком2
        Epic epic2 = taskManager.createEpic(new Epic("Очень важные дела", "сделать все очень важные дела"));
        System.out.println("Созданный эпик 2: " + epic2);

        SubTask subTask = taskManager.createSubTask(new SubTask("Очень важное дело", Status.DONE, "Сделать очень важное дело", epic2));
        System.out.println("Созданная подзадача: " + subTask);

        System.out.println("Все эпики: " + taskManager.getAllEpics());

        SubTask taskFromManager2 = taskManager.getSubTask(subTask.getId());
        System.out.println("Подзадача эпика 2: " + taskFromManager2);
        taskFromManager2.setName("Очень-очень важное дело");
        taskFromManager2.setDescription("Сделать дело через 5 дней");
        taskFromManager2.setStatus(Status.NEW);
        taskManager.updateSubTask(taskFromManager2);
        System.out.println("Обновленная подзадача эпика 2: " + taskFromManager2);

        System.out.println("Все эпики: " + taskManager.getAllEpics());

        taskManager.deleteEpicById(3);
        System.out.println("Все эпики после удаления: " + taskManager.getAllEpics());

        //остальные методы
        System.out.println("Все подзадачи: " + taskManager.getAllSubTasks());

        System.out.println("Эпик с id 3: " + taskManager.getEpic(3));

        Epic taskFromManager3 = taskManager.getEpic(epic2.getId());
        taskFromManager3.setName("Супер важные дела");
        taskManager.updateEpic(taskFromManager3);
        System.out.println("Обновленный эпик 2: " + taskFromManager3);

        Task taskFromManager = taskManager.getTask(task1.getId());
        System.out.println("Задача 1: " + taskFromManager);

        System.out.println("Задачи после удаления: " + taskManager.getAllTasks());

        taskManager.removeAllEpics();
        System.out.println("Эпики после удаления: " + taskManager.getAllEpics());

        System.out.println("Подзадачи после удаления: " + taskManager.getAllSubTasks());
    }
}