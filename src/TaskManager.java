import javax.swing.DefaultListModel;

/**
 * Manages task and deadline data models.
 * Handles all operations related to adding, removing, and editing tasks.
 */
public class TaskManager {

    private final DefaultListModel<String> taskModel = new DefaultListModel<>();
    private final DefaultListModel<String> deadlineModel = new DefaultListModel<>();
    private final DefaultListModel<String> categoryModel = new DefaultListModel<>(); // Kategori

    public DefaultListModel<String> getCategoryModel() {
        return categoryModel;
    }

    public DefaultListModel<String> getTaskModel() {
        return taskModel;
    }

    public DefaultListModel<String> getDeadlineModel() {
        return deadlineModel;
    }

    public void addTask(String task, String deadline, String category) {
        taskModel.addElement(task);
        deadlineModel.addElement(deadline);
        categoryModel.addElement(category);
    }

    public void removeTask(int index) {
        /*if (index >= 0 && index < taskModel.getSize()) {
            taskModel.remove(index);
        }
        if (index >= 0 && index < deadlineModel.getSize()) {
            deadlineModel.remove(index);
        }*/
        // Jadiin satu ae
        if (index < 0 || index >= taskModel.getSize()) return;
            taskModel.remove(index);
            deadlineModel.remove(index);
            categoryModel.remove(index); 
    }

    public void editTask(int index, String newTask, String newDeadline, String newCategory) {
        /*if (index >= 0 && index < taskModel.getSize()) {
            taskModel.set(index, newTask);
        }
        if (index >= 0 && index < deadlineModel.getSize()) {
            deadlineModel.set(index, newDeadline);
        }*/
        // Buat jadi 1 kondisi ae
        if (index < 0 || index >= taskModel.getSize()) return;
        taskModel.set(index, newTask);
        deadlineModel.set(index, newDeadline);
        categoryModel.set(index, newCategory);
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

    public String getCategory(int index) {
        return categoryModel.getElementAt(index);
    }
}