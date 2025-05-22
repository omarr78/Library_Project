package packageName;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;


public class Searching {
    private static final String SUBQUERY =
            "select b.b_id,b_title,b_author,b_topic,b_year " +
                    "from users as u " +
                    "join u_have_b h on u.u_id = h.u_id " +
                    "join books as b on h.b_id = b.b_id ";
    private static ArrayList<Book> resultHandling(ResultSet rs) throws SQLException {
        ArrayList<Book> books = new ArrayList<>();

        while(rs.next()) {
            int id = rs.getInt("b_id");
            String title = rs.getString("b_title");
            String author = rs.getString("b_author");
            String topic = rs.getString("b_topic");
            int year = rs.getInt("b_year");
            books.add(new Book(id,title,author,topic,year));
        }
        return books;
    }
    public static Book searchById(int u_id,int b_id) {
        try{
            String query = SUBQUERY + "where u.u_id = ? and b.b_id = ?";
            PreparedStatement ps = DealingWithDatabase.getConnection().prepareStatement(query);
            ps.setInt(1, u_id);
            ps.setInt(2, b_id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                int id = rs.getInt("b_id");
                String name = rs.getString("b_title");
                String author = rs.getString("b_author");
                String topic = rs.getString("b_topic");
                int year = rs.getInt("b_year");
                return new Book(id, name, author, topic, year);
            }
            else{
                return null;
            }
        } catch (SQLException e) {
            System.out.println("An error occurred while searching for a Book id.");
            e.printStackTrace();
        }
        return null;
    }
    public static void handleSearchingById(int u_id) {
        Scanner sc = new Scanner(System.in);
        String b_id;
        while (true) {
            System.out.print("Enter ID: ");
            b_id = sc.nextLine();
            if (Main.isNumeric(b_id)) break;
            else {
                System.out.println("Invalid ID");
            }
        }
        Book book = Searching.searchById(u_id,Integer.parseInt(b_id));
        if (book == null) {
            System.out.println("Book not found");
        } else {
            Main.printBook(book);
        }
    }
    public static ArrayList<Book> searchByTitle(int u_id,String title) {
        try{
            String query = SUBQUERY + "where u.u_id = ? and lower(b.b_title) LIKE ?";
            PreparedStatement ps = DealingWithDatabase.getConnection().prepareStatement(query);
            ps.setInt(1, u_id);
            ps.setString(2, "%" + title.toLowerCase() + "%");
            ResultSet rs = ps.executeQuery();
            return resultHandling(rs);
        } catch (SQLException e) {
            System.out.println("An error occurred while searching for a Book title.");
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static void handleSearchingByTitle(int u_id) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Title: ");
        String title = sc.nextLine();
        ArrayList<Book> searchedBooks = Searching.searchByTitle(u_id,title);
        if (searchedBooks.isEmpty()) {
            System.out.println("Book not found");
        } else {
            for (Book book : searchedBooks) {
                Main.printBook(book);
            }
        }
    }
    public static ArrayList<Book> searchByAuthor(int u_id,String author) {
        ArrayList<Book> ret = new ArrayList<>();
        try{
            String query = SUBQUERY + "where u.u_id = ? and lower(b.b_author) LIKE ?";
            PreparedStatement ps = DealingWithDatabase.getConnection().prepareStatement(query);
            ps.setInt(1, u_id);
            ps.setString(2, "%" + author.toLowerCase() + "%");
            ResultSet rs = ps.executeQuery();
            return resultHandling(rs);
        } catch (SQLException e) {
            System.out.println("An error occurred while searching for a Book author.");
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
    public static void handleSearchingByAuthor(int u_id) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Author: ");
        String author = sc.nextLine();
        ArrayList<Book> searchedBooks = Searching.searchByAuthor(u_id,author);
        if (searchedBooks.isEmpty()) {
            System.out.println("Books not found");
        } else {
            for (Book book : searchedBooks) {
                Main.printBook(book);
            }
        }
    }
    public static ArrayList<Book> searchByTopic(int u_id,String topic) {
        ArrayList<Book> ret = new ArrayList<>();
        try{
            String query = SUBQUERY + "where u.u_id = ? and lower(b.b_topic) LIKE ?";
            PreparedStatement ps = DealingWithDatabase.getConnection().prepareStatement(query);
            ps.setInt(1, u_id);
            ps.setString(2, "%" + topic.toLowerCase() + "%");
            ResultSet rs = ps.executeQuery();
            return resultHandling(rs);
        } catch (SQLException e) {
            System.out.println("An error occurred while searching for a Book topic.");
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
    public static void handleSearchingByTopic(int u_id) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter topic: ");
        String topic = sc.nextLine();
        ArrayList<Book> searchedBooks = Searching.searchByTopic(u_id,topic);
        if (searchedBooks.isEmpty()) {
            System.out.println("Books not found");
        } else {
            for (Book book : searchedBooks) {
                Main.printBook(book);
            }
        }
    }
    public static ArrayList<Book> searchByYear(int u_id,int year) {
        ArrayList<Book> ret = new ArrayList<>();
        try{
            String query = SUBQUERY + "where u.u_id = ? and b.b_year = ?";
            PreparedStatement ps = DealingWithDatabase.getConnection().prepareStatement(query);
            ps.setInt(1, u_id);
            ps.setInt(2, year);
            ResultSet rs = ps.executeQuery();
            return resultHandling(rs);
        } catch (SQLException e) {
            System.out.println("An error occurred while searching for a Book year.");
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
    public static void handleSearchingByYear(int u_id) {
        Scanner sc = new Scanner(System.in);
        String year;
        while (true) {
            System.out.print("Enter year: ");
            year = sc.nextLine();
            if (Main.isNumeric(year) && Integer.parseInt(year) > 1900 && Integer.parseInt(year) <= 2025)
                break;
            else
                System.out.println("Invalid year");
        }
        ArrayList<Book> searchedBooks = Searching.searchByYear(u_id,Integer.parseInt(year));
        if (searchedBooks.isEmpty()) {
            System.out.println("Books not found");
        } else {
            for (Book book : searchedBooks) {
                Main.printBook(book);
            }
        }
    }
}
