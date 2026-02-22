package helpers;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import java.util.ArrayList;
import java.util.List;

import models.Task;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TaskManager.db";
    private static final int DATABASE_VERSION = 1;

    // Table name
    private static final String TABLE_TASKS = "tasks";

    // Column names
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_PRIORITY = "priority";
    private static final String COLUMN_COMPLETED = "completed";
    private static final String COLUMN_CATEGORY = "category";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_TASKS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_DESCRIPTION + " TEXT,"
                + COLUMN_DATE + " TEXT,"
                + COLUMN_PRIORITY + " TEXT,"
                + COLUMN_COMPLETED + " INTEGER DEFAULT 0,"
                + COLUMN_CATEGORY + " TEXT"
                + ")";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        onCreate(db);
    }

    // Create
    public long addTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, task.getTitle());
        values.put(COLUMN_DESCRIPTION, task.getDescription());
        values.put(COLUMN_DATE, task.getDate());
        values.put(COLUMN_PRIORITY, task.getPriority().name());
        values.put(COLUMN_COMPLETED, task.isCompleted() ? 1 : 0);
        values.put(COLUMN_CATEGORY, task.getCategory());

        long id = db.insert(TABLE_TASKS, null, values);
        db.close();
        return id;
    }

    // Read all
    public List<Task> getAllTasks() {
        List<Task> taskList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_TASKS + " ORDER BY " + COLUMN_DATE + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                task.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
                task.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)));
                task.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
                task.setPriority(Task.Priority.valueOf(cursor.getString(cursor.getColumnIndex(COLUMN_PRIORITY))));
                task.setCompleted(cursor.getInt(cursor.getColumnIndex(COLUMN_COMPLETED)) == 1);
                task.setCategory(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY)));

                taskList.add(task);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return taskList;
    }

    // Update
    public int updateTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, task.getTitle());
        values.put(COLUMN_DESCRIPTION, task.getDescription());
        values.put(COLUMN_DATE, task.getDate());
        values.put(COLUMN_PRIORITY, task.getPriority().name());
        values.put(COLUMN_COMPLETED, task.isCompleted() ? 1 : 0);
        values.put(COLUMN_CATEGORY, task.getCategory());

        return db.update(TABLE_TASKS, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(task.getId())});
    }

    // Delete
    public void deleteTask(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASKS, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    // Search tasks
    public List<Task> searchTasks(String query) {
        List<Task> taskList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String searchQuery = "SELECT * FROM " + TABLE_TASKS + " WHERE "
                + COLUMN_TITLE + " LIKE ? OR "
                + COLUMN_DESCRIPTION + " LIKE ? OR "
                + COLUMN_CATEGORY + " LIKE ?";

        String[] searchArgs = new String[]{"%" + query + "%", "%" + query + "%", "%" + query + "%"};

        Cursor cursor = db.rawQuery(searchQuery, searchArgs);

        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                task.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
                task.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)));
                task.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
                task.setPriority(Task.Priority.valueOf(cursor.getString(cursor.getColumnIndex(COLUMN_PRIORITY))));
                task.setCompleted(cursor.getInt(cursor.getColumnIndex(COLUMN_COMPLETED)) == 1);
                task.setCategory(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY)));

                taskList.add(task);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return taskList;
    }

    // Filter by priority
    public List<Task> getTasksByPriority(Task.Priority priority) {
        List<Task> taskList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_TASKS, null, COLUMN_PRIORITY + " = ?",
                new String[]{priority.name()}, null, null, COLUMN_DATE + " DESC");

        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                task.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
                task.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)));
                task.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
                task.setPriority(Task.Priority.valueOf(cursor.getString(cursor.getColumnIndex(COLUMN_PRIORITY))));
                task.setCompleted(cursor.getInt(cursor.getColumnIndex(COLUMN_COMPLETED)) == 1);
                task.setCategory(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY)));

                taskList.add(task);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return taskList;
    }

    // Get statistics
    public int getTotalTasks() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_TASKS, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        db.close();
        return count;
    }

    public int getCompletedTasks() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_TASKS + " WHERE " + COLUMN_COMPLETED + " = 1", null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        db.close();
        return count;
    }
}