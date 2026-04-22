import javax.swing.DefaultListModel;

/**
 * Manages task and deadline data models.
 * Handles all operations related to adding, removing, and editing tasks.
 */
public class TaskManager {

    private final DefaultListModel<String> taskModel = new DefaultListModel<>();
    private final DefaultListModel<String> deadlineModel = new DefaultListModel<>();

    public DefaultListModel<String> getTaskModel() {
        return taskModel;
    }

    public DefaultListModel<String> getDeadlineModel() {
        return deadlineModel;
    }

    public void addTask(String task, String deadline) {
        taskModel.addElement(task);
        deadlineModel.addElement(deadline);
    }

    public void removeTask(int index) {
        if (index >= 0 && index < taskModel.getSize()) {
            taskModel.remove(index);
        }
        if (index >= 0 && index < deadlineModel.getSize()) {
            deadlineModel.remove(index);
        }
    }

    public void editTask(int index, String newTask, String newDeadline) {
        if (index >= 0 && index < taskModel.getSize()) {
            taskModel.set(index, newTask);
        }
        if (index >= 0 && index < deadlineModel.getSize()) {
            deadlineModel.set(index, newDeadline);
        }
    }

    public String getTask(int index) {
        return taskModel.getElementAt(index);
    }

    public String getDeadline(int index) {
        return deadlineModel.getElementAt(index);
    }

    public int getSize() {
        return taskModel.getSize();
    }
}