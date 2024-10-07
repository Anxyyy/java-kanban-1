package service;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {

    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, Epic> epics;
    private final HashMap<Integer, SubTask> subTasks;
    private final HistoryManager historyManager;
    private int counter = 0;

    public InMemoryTaskManager() { //конструктор
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subTasks = new HashMap<>();
        this.historyManager = Managers.getDefaultHistory();
    }

    private int generateId() { //генератор идентификатора
        return ++counter;
    }

    @Override
    public List<Task> getAllTasks() { //вывод всех задач
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<SubTask> getAllSubTasks() { //вывод всех подзадач
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public List<Epic> getAllEpics() { //вывод всех эпиков
        return new ArrayList<>(epics.values());
    }

    @Override
    public void removeAllTasks() { //удаление всех задач
        tasks.clear();
    }

    @Override
    public void removeAllSubTasks() { //удаление всех подзадач
        subTasks.clear();
        for (Epic epic : epics.values()) {
            calculateStatus(epic);
        }
    }

    @Override
    public void removeAllEpics() {//удаление всех эпиков
        subTasks.clear();
        epics.clear();
    }

    @Override
    public Task createTask(Task task) { //создание задачи
        task.setId(generateId());
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public SubTask createSubTask(SubTask subTask) { //создание подзадачи
        Epic epic = epics.get(subTask.getEpic().getId());
        subTask.setId(generateId());
        subTasks.put(subTask.getId(), subTask);

        if (epic != null) {
            epic.addTask(subTask);
            calculateStatus(epic);
        }
        return subTask;
    }

    @Override
    public Epic createEpic(Epic epic) { //создание эпика
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
        return epic;
    }

    @Override
    public Task getTask(int id) { //получение задачи по id
        Task task = tasks.get(id);
        if (task != null) {
            historyManager.addAll(task);
        }
        return task;
    }

    @Override
    public SubTask getSubTask(int id) { //получение подзадачи по id
        SubTask subTask = subTasks.get(id);
        if (subTask != null) {
            historyManager.addAll(subTask);
        }
        return subTask;
    }

    @Override
    public Epic getEpic(int id) { //получение эпика по id
        Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.addAll(epic);
        }
        return epic;
    }

    @Override
    public void updateTask(Task task) { //обновление задачи
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateSubTask(SubTask subTask) { //обновление подзадачи
        Epic epic = epics.get(subTask.getEpic().getId());
        if (epic != null) {

            SubTask saved = subTasks.get(subTask.getId());
            if (saved == null) {
                return;
            }

            saved.setName(subTask.getName());
            saved.setDescription(subTask.getDescription());

            calculateStatus(epic);
        }
    }


    @Override
    public void updateEpic(Epic epic) { //обновление эпиков
        Epic saved = epics.get(epic.getId());
        if(saved == null){
            return;
        }

        saved.setName(epic.getName());
        saved.setDescription(epic.getDescription());
    }

    @Override
    public void deleteTask(int id) { //удаление задачи по id
        tasks.remove(id);
    }

    @Override
    public void deleteSubTask(int id) { //удаление подзадачи по id
        Iterator<SubTask> iterator = subTasks.values().iterator();
        while (iterator.hasNext()) {
            SubTask subTask = iterator.next();
            if (subTask.getId() == id) {
                iterator.remove();

                Epic epic = subTask.getEpic();
                Epic epicSaved = epics.get(epic.getId());

                epicSaved.getSubTask().remove(subTask);
                calculateStatus(epicSaved);

                break;
            }
        }
    }

    private void calculateStatus(Epic epic) { // приватный метод рассчета статуса
        boolean allSubTasksDone = true;
        boolean anySubTaskInProgress = false;

        for (SubTask subTask : epic.getSubTask()) {
            if (subTask.getStatus() != Status.DONE) {
                allSubTasksDone = false;
                if (subTask.getStatus() == Status.IN_PROGRESS) {
                    anySubTaskInProgress = true;
                }
            }
        }

        if (allSubTasksDone) {
            epic.setStatus(Status.DONE);
        } else if (anySubTaskInProgress) {
            epic.setStatus(Status.IN_PROGRESS);
        } else {
            epic.setStatus(Status.NEW);
        }
    }

    @Override
    public void deleteEpicById(int epicId) { //удаление эпика по id
        Iterator<Epic> iterator = epics.values().iterator();
        while (iterator.hasNext()) {
            Epic epic = iterator.next();
            if (epic.getId() == epicId) {
                iterator.remove();

                // Удаление всех подзадач, принадлежащих к удаленному эпику
                for (SubTask subTask : epic.getSubTask()) {
                    subTasks.remove(subTask);
                }

                break;
            }
        }
    }

    @Override
    public List<SubTask> getAllSubTasksForEpic(int epicId) { //получение всех подзадач определенного эпика
        Epic epic = epics.get(epicId);
        if (epic != null) {
            return new ArrayList<>(epic.getSubTask());
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Task> getHistory() { //получение истории задач
        return historyManager.getHistory();
    }
}