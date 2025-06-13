package packageName;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

// Adding for only Admin
public class Adding {
    private static String getTitle(){
        Scanner sc = new Scanner(System.in);
        String title;
        while(true){
            System.out.print("Enter Title: ");
            title = sc.nextLine();
            if(title.isEmpty()){
                System.out.println("Title is empty");
            }
            else{
                break;
            }
        }
        return title;
    }
    private static String getAuthor(){
        Scanner sc = new Scanner(System.in);
        String author;
        while(true){
            System.out.print("Enter Author: ");
            author = sc.nextLine();
            if(author.isEmpty()){
                System.out.println("Author is empty");
            }
            else{
                break;
            }
        }
        return author;
    }
    private static String getTopic(){
        Scanner sc = new Scanner(System.in);
        String topic;
        while(true){
            System.out.print("Enter Topic: ");
            topic = sc.nextLine();
            if(topic.isEmpty()){
                System.out.println("Topic is empty");
            }
            else{
                break;
            }
        }
        return topic;
    }
    private static int getYear(){
        Scanner sc = new Scanner(System.in);
        String strYear;
        while (true) {
            System.out.print("Enter Year: ");
            strYear = sc.nextLine();
            if (Main.isNumeric(strYear) && Integer.parseInt(strYear) > 1900
                    && Integer.parseInt(strYear) <= 2025) {
                break;
            }
        }
        return Integer.parseInt(strYear);
    }
    public static void createBook(){
        Book book = new Book(-1,getTitle(),getAuthor(),getTopic(),getYear());
        try{
            // first check if book is already exist
            PreparedStatement ps = Connection_to_db.getConnection().prepareStatement(
                    "SELECT * FROM books WHERE b_title = ?");
            ps.setString(1,book.getTitle());
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                System.out.println("Books already exist");
            }
            else{
                PreparedStatement prs = Connection_to_db.getConnection().prepareStatement(
                        "insert into books(b_title,b_author,b_topic,b_year) values (?,?,?,?)");

                prs.setString(1,book.getTitle());
                prs.setString(2,book.getAuthor());
                prs.setString(3,book.getTopic());
                prs.setInt(4,book.getYear());
                prs.executeUpdate();
                System.out.println("Book added to All Books");
            }
        } catch (SQLException | NullPointerException e) {
            System.out.println("An error occurred while creating the book.");
            System.out.println(e.getMessage());
        }

    }
}
