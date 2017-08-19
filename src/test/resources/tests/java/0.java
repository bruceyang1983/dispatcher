import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String s = in.nextLine();
        if (s.charAt(0) == '1' && s.charAt(1) == '2' && s.charAt(s.length() - 2) == 'A') {
            System.out.print("00" + s.substring(2, s.length() - 2));
        } else if (s.charAt(0) == '1' && s.charAt(1) == '2' && s.charAt(s.length() - 2) == 'P') {
            System.out.print("12" + s.substring(2, s.length() - 2));
        } else {
            if (s.charAt(s.length() - 2) == 'A') {
                System.out.print(s.substring(0, s.length() - 2));
            } else {
                int h = s.charAt(0) == '0' ? Integer.valueOf(s.substring(1, 2)) : Integer.valueOf(s.substring(0, 2));
                System.out.print(String.valueOf(h + 12) + s.substring(2, s.length() - 2));
            }
        }
    }
}