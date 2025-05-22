package packageName;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

// Deleting for only Manager
public class Deleting {

    public static void deleteBook() {
        int u_id = Main.MANAGER_ID;
        Scanner sc = new Scanner(System.in);
        String ID;
        while (true) {
            System.out.print("Enter ID: ");
            ID = sc.nextLine();
            if (Main.isNumeric(ID)) break;
            else {
                System.out.println("Invalid ID");
            }
        }
        int b_id = Integer.parseInt(ID);
        Book book = Searching.searchById(u_id,b_id);
        if(book == null){
            System.out.println("Book not found");
        }
        else{
            Main.unLinkUB(u_id,b_id);
            try{
                String query = ("delete from books where b_id = ?");
                PreparedStatement ps = DealingWithDatabase.getConnection().prepareStatement(query);
                ps.setInt(1,b_id);
                ps.executeUpdate();
                System.out.println("Book removed from All Books");
            } catch (SQLException e) {
                System.out.println("An error occurred while removing the book.");
                System.out.println(e.getMessage());
                System.exit(0);
            }

        }

    }
}
