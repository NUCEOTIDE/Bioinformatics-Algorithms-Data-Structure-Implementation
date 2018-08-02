import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Scanner s=new Scanner(System.in);
        //System.out.println("Please input S(a,b) as integers");
        //System.out.println("a=b:");
        smith_waterman homework=new smith_waterman();
        homework.scoring(false);
    }
}
