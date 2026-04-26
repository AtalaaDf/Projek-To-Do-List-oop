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

    // Per-user file names (instance fields)
    private final String TASK_FILE;
    private final String DEADLINE_FILE;
    private final String CATEGORY_FILE;

    private final TaskManager taskManager;

    /**
     * Create a FileManager bound to a specific username. Files will be named
     * using a sanitized username prefix to avoid clashing between users.
     */
    public FileManager(TaskManager taskManager, String username) {
        this.taskManager = taskManager;
        String safe = sanitizeUsername(username);
        this.TASK_FILE = safe + "-tasks.txt";
        this.DEADLINE_FILE = safe + "-deadlines.txt";
        this.CATEGORY_FILE = safe + "-categories.txt";
    }

    /**
     * Backward-compatible constructor that uses a default single-user filename.
     * Prefer the constructor with username when integrating with LoginForm.
     */
    public FileManager(TaskManager taskManager) {
        this(taskManager, "user");
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

    // Method load kategorinye
    public void loadCategories() {
    File file = new File(CATEGORY_FILE);
    if (!file.exists()) {
        for (int i = 0; i < taskManager.getTaskModel().getSize(); i++) {
            taskManager.getCategoryModel().addElement("Umum");
        }
        return;
    }
    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
        String line;
        while ((line = reader.readLine()) != null) {
            taskManager.getCategoryModel().addElement(line);
        }
    } 
    catch (IOException e) {
        showError("Gagal memuat kategori: " + e.getMessage());
    }
    }

    // Method sevv
    public void saveCategories() {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(CATEGORY_FILE))) {
        for (int i = 0; i < taskManager.getCategoryModel().getSize(); i++) {
            writer.write(taskManager.getCategoryModel().getElementAt(i));
            writer.newLine();
        }
    } catch (IOException e) {
        showError("Gagal menyimpan kategori: " + e.getMessage());
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
        saveCategories(); // sev kategori
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private String sanitizeUsername(String username) {
        if (username == null) return "user";
        // replace any non-alphanumeric char with underscore
        return username.trim().replaceAll("[^A-Za-z0-9]", "_");
    }
}