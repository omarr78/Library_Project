package packageName;

public class Book {
    private int id;
    private String title;
    private String author;
    private String topic;
    private int year;
    public Book(int id,String title, String author, String topic, int year) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.topic = topic;
        this.year = year;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getTopic() {
        return topic;
    }

    public int getYear() {
        return year;
    }
}
