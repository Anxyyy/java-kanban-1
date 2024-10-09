package model;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<SubTask> subTask = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, null, description);
    }

    public Epic(String name, Status status, String description) {
        super(name, status, description);
    }

    public List<SubTask> getSubTask() {
        return subTask;
    }

    public void addTask(SubTask subTask) {
        this.subTask.add(subTask);
    }

    public void removeTask(SubTask subTask) {
        this.subTask.remove(subTask);
    }

    public void calculateStatus() {
        status = Status.NEW;
    }

    @Override
    public Epic clone() {
        Epic clonedEpic = (Epic) super.clone();
        clonedEpic.subTask = new ArrayList<>(subTask.size());
        for (SubTask subTask : subTask) {
            clonedEpic.subTask.add(subTask.clone());
        }
        return clonedEpic;
    }
}