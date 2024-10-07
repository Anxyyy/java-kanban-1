package model;

public class SubTask extends Task {

    private Epic epic;

    public SubTask(String name, Status status, String description, Epic epic) {
        super(name, status, description);
        this.epic = epic;
    }

    public SubTask(String name, Status status, String description) {
        super(name, status, description);
    }

    @Override
    public Epic getEpic() {
        return epic;
    }

    public void setEpic(Epic epic) {
        this.epic = epic;
    }

    @Override
    public SubTask clone() {
        SubTask clonedSubTask = (SubTask) super.clone();
        clonedSubTask.epic = epic != null ? epic.clone() : null;
        return clonedSubTask;
    }

}