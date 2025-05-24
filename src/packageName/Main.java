package packageName;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

class Result {
    public int id;
    public String email;

    public Result(int id,String email) {
        this.id = id;
        this.email = email;
    }
}


public class Main {
    static final int MANAGER_ID = 8;
    Scanner sc = new Scanner(System.in);


    public String start() {
        String input;
        sc = new Scanner(System.in);
        while (true) {
            PrintMessage.startMessage();
            input = sc.nextLine();
            if (input.equals("1") || input.equals("2") || input.equals("3")) {
                return input;
            } else {
                System.out.println("Invalid input");
            }
        }
    }
    public void signUp(){
        String[] msg;
        while (true) {
            msg = PrintMessage.signUp();
            if (validateEmailAndPassword(msg[0], msg[1])) {
                try{
                    PreparedStatement ps = DealingWithDatabase.getConnection().prepareStatement(
                            "SELECT * FROM users WHERE u_email = ?");
                    ps.setString(1, msg[0]);
                    ResultSet rs = ps.executeQuery();

                    if(rs.next()){
                        System.out.println("Account already exists");
                        break;
                    }
                    else{
                        PreparedStatement prs = DealingWithDatabase.getConnection().prepareStatement(
                                "INSERT INTO users(u_email, u_password) VALUES (?, ?)"
                        );
                        prs.setString(1, msg[0]);
                        prs.setString(2, msg[1]);
                        prs.executeUpdate();
                        System.out.println("Account successfully created");
                        break;
                    }
                }catch (SQLException | NullPointerException e) {
                    System.out.println("An error occurred while signing up.");
                    System.out.println(e.getMessage());
                }
            } else {
                System.out.println("invalid Email or Password");
            }
        }
    }
    public Result login() {
        String[] msg;
        int i = 0;
        while (true) {
            msg = PrintMessage.login();
            if (validateEmailAndPassword(msg[0], msg[1])) {
                try{
                    PreparedStatement ps = DealingWithDatabase.getConnection().prepareStatement(
                            "SELECT * FROM users WHERE u_email = ? "
                    );
                    ps.setString(1, msg[0]);
                    ResultSet rs = ps.executeQuery();

                    if(rs.next()){
                        int id = rs.getInt("u_id");
                        String db_email = rs.getString("u_email");
                        String db_password = rs.getString("u_password");
                        if(db_email.equals(msg[0]) && db_password.equals(msg[1])){
                            return new Result(id,db_email);
                        }
                    }
                    else{
                        System.out.println("incorrect Email or Password");
                        i++;
                        if (i == 2) break;
                    }
                }catch (SQLException | NullPointerException e) {
                    System.out.println("An error occurred while login.");
                    System.out.println(e.getMessage());
                }
            }
            else{
                System.out.println("invalid Email or Password");
            }
        }
        return null;
    }
    public void showBooks(int id){
        boolean ch = false;
        try {
            String query = ("select b.b_id,b_title,b_author,b_topic,b_year " +
                    "from users as u " +
                    "join u_have_b h on u.u_id = h.u_id " +
                    "join books as b on h.b_id = b.b_id " +
                    "where u.u_id = ?");
            PreparedStatement ps;
            if(id == MANAGER_ID)
            {
                 ps = DealingWithDatabase.getConnection().prepareStatement("select * from books");
            }
            else{
                ps = DealingWithDatabase.getConnection().prepareStatement(query);
                ps.setInt(1, id);
            }
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ch = true;
                id = rs.getInt("b_id");
                String title = rs.getString("b_title");
                String author = rs.getString("b_author");
                String topic = rs.getString("b_topic");
                int year = rs.getInt("b_year");
                printBook(new Book(id,title,author,topic,year));
            }
        }catch(SQLException | NullPointerException e){
                System.out.println("An error occurred while showing Books.");
                System.out.println(e.getMessage());
        }
        if(!ch){
            System.out.println("Books Not Found");
        }
    }
    public void searchBooks(int u_id){
        String input;
        while (true) {
            PrintMessage.showSearchMessage();
            input = sc.nextLine();
            if (input.equals("1")) {
                Searching.handleSearchingById(u_id);
            }
            else if (input.equals("2")) {
                Searching.handleSearchingByTitle(u_id);
            }
            else if (input.equals("3")) {
                Searching.handleSearchingByAuthor(u_id);
            }
            else if (input.equals("4")) {
                Searching.handleSearchingByTopic(u_id);
            }
            else if (input.equals("5")) {
                Searching.handleSearchingByYear(u_id);
            } else {
                System.out.println("Invalid input");
                continue;
            }
            break;
        }
    }

    public void addToMyBooks(int u_id){
        String id;
        while (true) {
            System.out.print("Enter ID: ");
            id = sc.nextLine();
            if (isNumeric(id)) break;
            else {
                System.out.println("Invalid ID");
            }
        }
        int b_id = Integer.parseInt(id);
        Book book = Searching.searchById(MANAGER_ID, b_id);
        if (book != null) {
            book = Searching.searchById(u_id, b_id);
            if(book == null){
                linkUB(u_id,b_id);
            }
            else{
                System.out.println("Book already exists in My Books");
            }
        } else {
            System.out.println("the ID of Book not found to Add to My Books");
        }
    }
    public static void linkUB(int u_id, int b_id) {
        try{
            String query = ("insert into u_have_b(u_id,b_id) values (?,?)");
            PreparedStatement ps = DealingWithDatabase.getConnection().prepareStatement(query);
            ps.setInt(1,u_id);
            ps.setInt(2,b_id);
            ps.executeUpdate();
            System.out.println("Book added to My Books");
        }catch (SQLException | NullPointerException e) {
            System.out.println("An error occurred while LinkUB.");
            System.out.println(e.getMessage());
        }
    }

    public void deleteFromMyBooks(int u_id) {
        String id;
        while (true) {
            System.out.print("Enter ID: ");
            id = sc.nextLine();
            if (isNumeric(id)) break;
            else {
                System.out.println("Invalid ID");
            }
        }
        int b_id = Integer.parseInt(id);
        Book book = Searching.searchById(u_id, b_id);
        if (book == null) {
            System.out.println("Book not found");
        } else {
            unLinkUB(u_id, b_id);
        }
    }
    public static void unLinkUB(int u_id, int b_id) {
        try{
            String query = (u_id == MANAGER_ID) ? "delete from u_have_b where b_id = ? "
                    : "delete from u_have_b where u_id = ? and b_id = ?";
            PreparedStatement ps = DealingWithDatabase.getConnection().prepareStatement(query);
            if(u_id == MANAGER_ID){
                ps.setInt(1,b_id);
            }
            else{
                ps.setInt(1,u_id);
                ps.setInt(2,b_id);
            }

            ps.executeUpdate();
            System.out.println(u_id == MANAGER_ID ? "The Book is UnLinked From All Users" : "Book removed from My Books");
        }catch (SQLException | NullPointerException e) {
            System.out.println("An error occurred while UnLinkUB.");
            System.out.println(e.getMessage());
        }
    }

    public Boolean validateEmailAndPassword(String email, String password) {
        email = email.trim();
        password = password.trim();
        boolean valid = email.contains("@") && email.contains(".") && (email.length() <= 30);
        if (password.length() < 3 || password.length() > 15) {
            valid = false;
        }
        return valid;
    }
    public static void printBook(Book book) {
        System.out.println("ID: " + book.getId() + ", " + "Title: " + book.getTitle() + ", "
                + "Author: " + book.getAuthor() + ", " + "Topic: " + book.getTopic() + ", " + "Year: " + book.getYear());
    }
    public static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }




    public static void main(String[] args)  {

        Main main = new Main();
        Scanner sc = new Scanner(System.in);
        DealingWithDatabase.startConnection();

        Result res;

        while (true) {
            String input = main.start();
            if (input.equals("1")) {
                res= main.login();
            } else if (input.equals("2")) {
                main.signUp();
                res = main.login();
            } else {
                DealingWithDatabase.closeConnection();
                return;
            }
            if (res != null) break;
        }
        if (res.id == MANAGER_ID) { // Manager Account
            String input;
            while (true) {
                PrintMessage.showMainMessageManager(res.email);
                input = sc.nextLine();
                if (input.equals("1")) { // show all books
                    main.showBooks(MANAGER_ID);
                } else if (input.equals("2")) { // search book
                    main.searchBooks(MANAGER_ID);
                }
                else if (input.equals("3")) { // add new book
                    Adding.createBook();
                }
                else if (input.equals("4")) { // delete book
                    Deleting.deleteBook();
                }
                else if(input.equals("-1")) { // Exit
                    break;
                }
                else {
                    System.out.println("Invalid input");
                }
            }
        }
        else{ // user Account
            String input;
            while (true) {
                PrintMessage.showMainMessageUser(res.email);
                input = sc.nextLine();
                if (input.equals("1")) { // show manager books(All books)
                    main.showBooks(MANAGER_ID);
                }
                else if (input.equals("2")) { // show user books
                    main.showBooks(res.id);
                }
                else if (input.equals("3")) { // search manager books(All books)
                    main.searchBooks(MANAGER_ID);
                }
                else if (input.equals("4")) { // search user books
                    main.searchBooks(res.id);
                }
                else if(input.equals("5")) { // Add to my books
                    main.addToMyBooks(res.id);
                }
                else if (input.equals("6")) { // delete from my books
                    main.deleteFromMyBooks(res.id);
                }
                else if(input.equals("-1")) { // Exit
                    break;
                }
                else{
                    System.out.println("Invalid input");
                }
            }
        }
        DealingWithDatabase.closeConnection();
    }

}