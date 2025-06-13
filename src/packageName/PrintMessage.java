package packageName;

import java.util.Scanner;

public class PrintMessage {
    static void startMessage(){
        System.out.println("-- Books Program --");
        System.out.println("1- Log in");
        System.out.println("2- Sign up");
        System.out.println("3- Exit");
    }
    static String[] login(){
        System.out.println("-- Login page --");
        Scanner sc = new Scanner(System.in);
        String [] msg = new String[2];
        System.out.print("Enter your Email _@_.com: ");
        msg[0] = sc.nextLine().trim();
        System.out.print("Enter your Password: ");
        msg[1] = sc.nextLine();
        return msg;
    }
    static String[] signUp(){
        System.out.println("-- Sign up page --");
        Scanner sc = new Scanner(System.in);
        String [] msg = new String[2];
        System.out.print("Enter your Email _@_.com: ");
        msg[0] = sc.nextLine();
        System.out.print("Enter your Password: ");
        msg[1] = sc.nextLine();
        return msg;
    }
    static void showMainMessageAdmin(String email){
        System.out.println("---------------------------------------------------");
        System.out.println("-- "+ email +" --");
        System.out.println("1- Show All Books");
        System.out.println("2- Show Users Borrowing and Purchase Records");
        System.out.println("3- Show All users");
        System.out.println("4- Search Book");
        System.out.println("5- Add New Book");
        System.out.println("6- Delete Book");
        System.out.println("(-1) to Log out");
    }
    static void showMainMessageUser(String email){
        System.out.println("---------------------------------------------------");
        System.out.println("-- "+ email +" --");
        System.out.println("1- Show All Books");
        System.out.println("2- Show my Books");
        System.out.println("3- Search Book in All Books");
        System.out.println("4- Search Book in my Books");
        System.out.println("5- Borrow / Buy Book (By ID)");
        System.out.println("6- Delete from my Books (By ID)");
        System.out.println("(-1) to Log out");
    }
    static void showSearchMessage(){
        System.out.println("1- Search By ID");
        System.out.println("2- Search By Title");
        System.out.println("3- Search By Author");
        System.out.println("4- Search By topic");
        System.out.println("5- Search By Publish Year");
    }
    static void showBuyBorrowMessage(){
        System.out.println("1- Buy Book");
        System.out.println("2- Borrow Book");
    }
    static void showBorrowMessage(){
        System.out.println("1- Borrow Book for 3 days");
        System.out.println("2- Borrow Book for a week");
        System.out.println("3- Borrow Book for two weeks");
        System.out.println("4- Borrow Book for three weeks");
    }
}
