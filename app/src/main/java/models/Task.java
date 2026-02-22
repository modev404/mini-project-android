package models;



import java.io.Serializable;

public class Task implements Serializable {
    private int id;
    private String title;
    private String description;
    private String date;
    private Priority priority;
    private boolean isCompleted;
    private String category;

    public enum Priority {
        HIGH("Haute"),
        MEDIUM("Moyenne"),
        LOW("Basse");

        private String displayName;

        Priority(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    // Constructeurs
    public Task() {}

    public Task(String title, String description, String date, Priority priority, String category) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.priority = priority;
        this.category = category;
        this.isCompleted = false;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public Priority getPriority() { return priority; }
    public void setPriority(Priority priority) { this.priority = priority; }

    public boolean isCompleted() { return isCompleted; }
    public void setCompleted(boolean completed) { isCompleted = completed; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}