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
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private final InMemoryHistoryManager historyManager = new InMemoryHistoryManager(); // Используем новый менеджер истории
    private int counter = 0;

    public InMemoryTaskManager() {
    }

    private int generateId() {
        return ++counter;
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<SubTask> getAllSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void removeAllTasks() {
        tasks.clear();
    }

    @Override
    public void removeAllSubTasks() {
        subTasks.clear();
        for (Epic epic : epics.values()) {
            calculateStatus(epic);
        }
    }

    @Override
    public void removeAllEpics() {
        subTasks.clear();
        epics.clear();
    }

    @Override
    public Task createTask(Task task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public SubTask createSubTask(SubTask subTask) {
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
    public Epic createEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
        return epic;
    }

    @Override
    public Task getTask(int id) {
        Task task = tasks.get(id);
        if (task != null) {
            historyManager.addAll(task); // Запоминаем в истории
        }
        return task;
    }

    @Override
    public SubTask getSubTask(int id) {
        SubTask subTask = subTasks.get(id);
        if (subTask != null) {
            historyManager.addAll(subTask); // Запоминаем в истории
        }
        return subTask;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.addAll(epic); // Запоминаем в истории
        }
        return epic;
    }

    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateSubTask(SubTask subTask) {
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
    public void updateEpic(Epic epic) {
        Epic saved = epics.get(epic.getId());
        if (saved == null) {
            return;
        }

        saved.setName(epic.getName());
        saved.setDescription(epic.getDescription());
    }

    @Override
    public void deleteTask(int id) { //удаление задачи по id
        tasks.remove(id);
        historyManager.remove(id); // удаляем из истории
    }

    @Override
    public void deleteSubTask(int id) { //удаление подзадачи по id
        Iterator<SubTask> iterator = subTasks.values().iterator();
        while (iterator.hasNext()) {
            SubTask subTask = iterator.next();
            if (subTask.getId() == id) {
                iterator.remove();
                historyManager.remove(subTask.getId()); // удаляем из истории

                Epic epic = subTask.getEpic();
                Epic epicSaved = epics.get(epic.getId());

                epicSaved.getSubTask().remove(subTask);
                calculateStatus(epicSaved);

                break;
            }
        }
    }

    @Override
    public void deleteEpicById(int epicId) { //удаление эпика по id
        Iterator<Epic> iterator = epics.values().iterator();
        while (iterator.hasNext()) {
            Epic epic = iterator.next();
            if (epic.getId() == epicId) {
                iterator.remove();
                historyManager.remove(epicId); // удаляем из истории

                // Удаление всех подзадач, принадлежащих к удаленному эпику
                for (SubTask subTask : epic.getSubTask()) {
                    subTasks.remove(subTask.getId());
                    historyManager.remove(subTask.getId()); // удаляем из истории подзадач
                }

                break;
            }
        }
    }


    @Override
    public List<SubTask> getAllSubTasksForEpic(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic != null) {
            return new ArrayList<>(epic.getSubTask());
        }
        return new ArrayList<>();
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    private void calculateStatus(Epic epic) {
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
}
