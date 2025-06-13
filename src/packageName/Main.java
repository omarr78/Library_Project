package packageName;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

class Result {
    public int id;
    public String email;

    public Result(int id,String email) {
        this.id = id;
        this.email = email;
    }
}


public class Main {
    static final int ADMIN_ID= 1;
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
                    PreparedStatement ps = Connection_to_db.getConnection().prepareStatement(
                            "SELECT * FROM users WHERE u_email = ?");
                    ps.setString(1, msg[0]);
                    ResultSet rs = ps.executeQuery();

                    if(rs.next()){
                        System.out.println("Account already exists");
                        break;
                    }
                    else{
                        PreparedStatement prs = Connection_to_db.getConnection().prepareStatement(
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
                    PreparedStatement ps = Connection_to_db.getConnection().prepareStatement(
                            "SELECT * FROM users WHERE u_email = ? "
                    );
                    ps.setString(1, msg[0]);
                    ResultSet rs = ps.executeQuery();

                    if(rs.next()){
                        int id = rs.getInt("u_id");
                        String db_email = rs.getString("u_email");
                        String db_password = rs.getString("u_password");
                        if(db_password.equals(msg[1])){
                            return new Result(id,db_email);
                        }
                        else{
                            System.out.println("incorrect Password");
                            i++;
                            if (i == 2) break;
                        }
                    }
                    else{
                        System.out.println("incorrect Email");
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
    public void showBooks(int passedId){
        boolean ch = false;
        try {
            PreparedStatement ps;
            if(passedId == ADMIN_ID)
            {
                 ps = Connection_to_db.getConnection().prepareStatement("select * from books");
            }
            else{
                String query = ("SELECT b.b_id,b_title,b_author,b_topic,b_year,end_date " +
                        "FROM users AS u " +
                        "JOIN users_books h ON u.u_id = h.u_id " +
                        "JOIN books AS b ON h.b_id = b.b_id " +
                        "WHERE u.u_id = ? AND (end_date IS NULL OR end_date > NOW()::DATE)");
                ps = Connection_to_db.getConnection().prepareStatement(query);
                ps.setInt(1, passedId);
            }
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ch = true;
                int id = rs.getInt("b_id");
                String title = rs.getString("b_title");
                String author = rs.getString("b_author");
                String topic = rs.getString("b_topic");
                int year = rs.getInt("b_year");
                if(passedId == ADMIN_ID){
                    printBook(new Book(id,title,author,topic,year));
                }
                else{
                    java.sql.Date end_date = rs.getDate("end_date");
                    printBook(new Book(id,title,author,topic,year),end_date);
                }
            }
        }catch(SQLException | NullPointerException e){
                System.out.println("An error occurred while showing Books.");
                System.out.println(e.getMessage());
        }
        if(!ch){
            System.out.println("No Books Found");
        }
    }
    public void showUsersRecords(){
        boolean ch = false;
        try {
            String query = "select u_email,b_title,start_date,end_date " +
                    "from users as u " +
                    "join users_books h on u.u_id = h.u_id " +
                    "join books as b on h.b_id = b.b_id " +
                    "order by start_date";
            PreparedStatement ps = Connection_to_db.getConnection().prepareStatement(query);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ch = true;
                java.sql.Date end_date = rs.getDate("end_date");
                LocalDateTime start_date = rs.getTimestamp("start_date").toLocalDateTime();
                String title = rs.getString("b_title");
                String email = rs.getString("u_email");

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a");
                String formattedDate = start_date.format(formatter);

                if(end_date == null){
                    System.out.println("(" + formattedDate + ")" + " " + email + " (Bought) \"" +  title + "\"");
                }
                else{
                    System.out.println("(" + formattedDate + ")" + " " + email + " (Borrow) \"" + title + "\"" + " to " + "(" + end_date + ")");
                }
            }
        }catch(SQLException | NullPointerException e){
            System.out.println("An error occurred while showing users records.");
            System.out.println(e.getMessage());
        }
        if(!ch){
            System.out.println("There is No Records Found");
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
    public void showAllUsers(){ // just for admin
        boolean ch = false;
        try {
            String query = ("SELECT u_email FROM users WHERE u_id != 1 ORDER BY u_id");
            PreparedStatement ps = Connection_to_db.getConnection().prepareStatement(query);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ch = true;
                String email = rs.getString("u_email");
                System.out.println(email);
            }
        }catch(SQLException | NullPointerException e){
            System.out.println("An error occurred while showing Books.");
            System.out.println(e.getMessage());
        }
        if(!ch){
            System.out.println("No Users Found");
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
        Book book = Searching.searchById(ADMIN_ID, b_id);
        if (book != null) { // check if the id of book found in all book
            book = Searching.searchById(u_id, b_id);
            if(book == null || Searching.isBookExpired(u_id,b_id)){ // check if book is not exist in myBook
                deleteFromMyBooks(u_id, b_id);
                String choice;
                while(true){
                    PrintMessage.showBuyBorrowMessage();
                    choice = sc.nextLine();
                    if(choice.equals("1")) {
                        buyBook(u_id,b_id);
                        break;
                    }
                    else if(choice.equals("2")){
                        borrowBook(u_id,b_id);
                        break;
                    }
                    else{
                        System.out.println("Invalid input");
                    }
                }
            }
            else{
                System.out.println("Book already exists in My Books");
            }
        } else {
            System.out.println("the ID of Book not found to Add to My Books");
        }
    }
    public static void buyBook(int u_id, int b_id) {
        try{
            LocalDateTime start = LocalDateTime.now();
            Timestamp timestamp = Timestamp.valueOf(start);
            String query = ("insert into users_books(u_id,b_id,start_date) values (?,?,?)");
            PreparedStatement ps = Connection_to_db.getConnection().prepareStatement(query);
            ps.setInt(1,u_id);
            ps.setInt(2,b_id);
            ps.setTimestamp(3,timestamp);

            ps.executeUpdate();
            System.out.println("Book bought");
        }catch (SQLException | NullPointerException e) {
            System.out.println("An error occurred while buying Book.");
            System.out.println(e.getMessage());
        }
    }
    public static void borrowBook(int u_id, int b_id){
        Scanner sc = new Scanner(System.in);
        int addDays;
        String input;

        while(true){
            PrintMessage.showBorrowMessage();
            input = sc.nextLine();
            if(input.equals("1")){
                addDays = 3;
                break;
            }
            else if(input.equals("2")){
                addDays = 7;
                break;
            }
            else if(input.equals("3")){
                addDays = 14;
                break;
            }
            else if(input.equals("4")){
                addDays = 21;
                break;
            }
            else{
                System.out.println("Invalid input");
            }
        }
        LocalDateTime start = LocalDateTime.now();
        LocalDate end = LocalDate.now().plusDays(addDays);

        Timestamp timestampStart = Timestamp.valueOf(start);
        java.sql.Date dateEnd = java.sql.Date.valueOf(end);

        try{
            String query = ("insert into users_books(u_id,b_id,start_date,end_date) values (?,?,?,?)");
            PreparedStatement ps = Connection_to_db.getConnection().prepareStatement(query);
            ps.setInt(1,u_id);
            ps.setInt(2,b_id);
            ps.setTimestamp(3,timestampStart);
            ps.setDate(4,dateEnd);

            ps.executeUpdate();
            System.out.println("Book borrowed until " + end);
        } catch (SQLException | NullPointerException e) {
            System.out.println("An error occurred while borrowing Book.");
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
            unLinkUB(u_id, b_id,"");
        }
    }
    public void deleteFromMyBooks(int u_id, int b_id) {
        Book book = Searching.searchById(u_id, b_id);
        if (book != null)unLinkUB(u_id, b_id,"update");
    }
    public static void unLinkUB(int u_id, int b_id,String op) {
        try{
            String query = (u_id == ADMIN_ID) ? "delete from users_books where b_id = ? "
                    : "delete from users_books where u_id = ? and b_id = ?";
            PreparedStatement ps = Connection_to_db.getConnection().prepareStatement(query);
            if(u_id == ADMIN_ID){
                ps.setInt(1,b_id);
            }
            else{
                ps.setInt(1,u_id);
                ps.setInt(2,b_id);
            }

            ps.executeUpdate();
            if(op.equals("update")){
                System.out.println("You previously borrowed this book, but the borrowing period has expired.");
            }
            else{
                System.out.println(u_id == ADMIN_ID? "The Book is UnLinked From All Users" : "Book removed from My Books");
            }
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
    public static void printBook(Book book,java.sql.Date date) {
        if(date == null){
            System.out.println("         [Purchased]         " +"ID: " + book.getId() + ", " + "Title: " + book.getTitle() + ", "
                    + "Author: " + book.getAuthor() + ", " + "Topic: " + book.getTopic() + ", " + "Year: " + book.getYear());
        }
        else{
            System.out.println("[Borrowed until " + date + "]  " + "ID: " + book.getId() + ", " + "Title: " + book.getTitle() + ", "
                    + "Author: " + book.getAuthor() + ", " + "Topic: " + book.getTopic() + ", " + "Year: " + book.getYear());
        }

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
        Connection_to_db.startConnection();

        Result res = null;

        while (true) {
            while (res == null) {
                String input = main.start();
                if (input.equals("1")) {
                    res = main.login();
                } else if (input.equals("2")) {
                    main.signUp();
                    res = main.login();
                } else {
                    Connection_to_db.closeConnection();
                    return;
                }
            }

            if (res.id == ADMIN_ID) { // Admin Account
                String input;
                while (true) {
                    PrintMessage.showMainMessageAdmin(res.email);
                    input = sc.nextLine();
                    if (input.equals("1")) { // Show All Books
                        main.showBooks(ADMIN_ID);
                    } else if (input.equals("2")) { // Show Users Borrowing and Purchase Records
                        main.showUsersRecords();
                    } else if (input.equals("3")) { // Show All users
                        main.showAllUsers();
                    } else if (input.equals("4")) { // Search Book
                        main.searchBooks(ADMIN_ID);
                    } else if (input.equals("5")) { // Add New Book
                        Adding.createBook();
                    } else if (input.equals("6")) { // Delete Book
                        Deleting.deleteBook();
                    } else if (input.equals("-1")) { // Log out
                        System.out.println("Logging out...");
                        res = null;
                        break;
                    } else {
                        System.out.println("Invalid input");
                    }
                }
            } else { // user Account
                String input;
                while (true) {
                    PrintMessage.showMainMessageUser(res.email);
                    input = sc.nextLine();
                    if (input.equals("1")) { // show Admin books(All books)
                        main.showBooks(ADMIN_ID);
                    } else if (input.equals("2")) { // show user books
                        main.showBooks(res.id);
                    } else if (input.equals("3")) { // search Admin books(All books)
                        main.searchBooks(ADMIN_ID);
                    } else if (input.equals("4")) { // search user books
                        main.searchBooks(res.id);
                    } else if (input.equals("5")) { // Add to my books
                        main.addToMyBooks(res.id);
                    } else if (input.equals("6")) { // delete from my books
                        main.deleteFromMyBooks(res.id);
                    } else if (input.equals("-1")) { // Log out
                        System.out.println("Logging out...");
                        res = null;
                        break;
                    } else {
                        System.out.println("Invalid input");
                    }
                }
            }
        }
    }

}