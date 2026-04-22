import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JOptionPane;

/**
 * Handles all file I/O operations for tasks and deadlines.
 */
public class FileManager {

    private static final String TASK_FILE     = "data-tugas.txt";
    private static final String DEADLINE_FILE = "deadline.txt";

    private final TaskManager taskManager;

    public FileManager(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    /** Load task names from file into the task model. */
    public void loadTasks() {
        File file = new File(TASK_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                taskManager.getTaskModel().addElement(line);
            }
        } catch (IOException e) {
            showError("Gagal memuat data tugas: " + e.getMessage());
        }
    }

    /** Load deadlines from file into the deadline model. */
    public void loadDeadlines() {
        File file = new File(DEADLINE_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                taskManager.getDeadlineModel().addElement(line);
            }
        } catch (IOException e) {
            showError("Gagal memuat deadline: " + e.getMessage());
        }
    }

    /** Save all task names to file. */
    public void saveTasks() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TASK_FILE))) {
            for (int i = 0; i < taskManager.getTaskModel().getSize(); i++) {
                writer.write(taskManager.getTaskModel().getElementAt(i));
                writer.newLine();
            }
        } catch (IOException e) {
            showError("Gagal menyimpan data tugas: " + e.getMessage());
        }
    }

    /** Save all deadlines to file. */
    public void saveDeadlines() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DEADLINE_FILE))) {
            for (int i = 0; i < taskManager.getDeadlineModel().getSize(); i++) {
                writer.write(taskManager.getDeadlineModel().getElementAt(i));
                writer.newLine();
            }
        } catch (IOException e) {
            showError("Gagal menyimpan deadline: " + e.getMessage());
        }
    }

    /** Convenience: save both tasks and deadlines at once. */
    public void saveAll() {
        saveTasks();
        saveDeadlines();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}