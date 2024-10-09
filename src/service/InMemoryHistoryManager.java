package service;

import model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private static class Node {
        Task task;
        Node prev;
        Node next;

        Node(Task task) {
            this.task = task;
        }
    }

    private Node head;
    private Node tail;
    private final Map<Integer, Node> taskMap = new HashMap<>();

    @Override
    public void addAll(Task task) {
        if (taskMap.containsKey(task.getId())) {
            // Если задача уже есть в истории, сначала удаляем её
            removeNode(taskMap.get(task.getId()));
        }

        // Добавляем новую задачу в конец списка
        Node newNode = new Node(task);
        linkLast(newNode);

        // Обновляем HashMap
        taskMap.put(task.getId(), newNode);
    }

    private void linkLast(Node newNode) {
        if (tail == null) {
            // Список был пуст
            head = tail = newNode;
        } else {
            // Добавляем в конец списка
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
    }

    private void removeNode(Node node) {
        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            // Удаляем голову
            head = node.next;
        }

        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            // Удаляем хвост
            tail = node.prev;
        }

        // Удаляем из HashMap
        taskMap.remove(node.task.getId());
    }

    @Override
    public List<Task> getHistory() {
        List<Task> tasks = new ArrayList<>();
        Node current = head;

        while (current != null) {
            tasks.add(current.task);
            current = current.next;
        }

        return tasks;
    }

    @Override
    public void remove(int id) {
        Node nodeToRemove = taskMap.get(id);
        if (nodeToRemove != null) {
            removeNode(nodeToRemove);
        }
    }
}

