import java.util.Scanner;

public class mutiple_alignment {

    private String[] target_seq;  //two string to be aligned
    private String[][] scoring_scheme;  //scoring scheme
    private float penalty;  //empty space penalty
    private float[][] scoring_dataSheet;  //scoring matrix
    private String[] answer;  //answer string array
    //private int count=-1;  //records of length


    public static mutiple_alignment Setmutiple_alignment(String type){
        switch(type){
            case "default":{
                mutiple_alignment temp=new mutiple_alignment();
                return temp;
            }
            case "input":{
                Scanner s=new Scanner(System.in);
                mutiple_alignment temp=new mutiple_alignment(s);
                return temp;
            }
            default:{
                return null;
            }
        }
    }
    public mutiple_alignment(){

    }
    public mutiple_alignment(Scanner a){
        try(){
            System.out.println("Please indicate the size of the database: ");
            target_seq=new String[a.nextInt()];
            System.out.println("Please input target sequences (seperate with \\n): ");
            for(int i=0;i<target_seq.length;i++)
                target_seq[i]=a.nextLine();
        }catch(Exception e){
            switch(e.getMessage()){
                case "NullPointerException": System.out.println("");
                case "IndexOutOfBondException": System.out.println("Illegal size of target sequence data base, or ");
            }
        }

    }

    /**
     * the 1st main multiple seq alignment method
     *      using extended smith waterman model,
     *      comparing all the possible combination to obtain
     *      maximum value in all align positions
     */
    public void dynamicProgramming_alibnment(){

    }

    /**
     * the 2nd main mutiple seq alignment method
     *      using CLASTALW model,
     *      comparing all the pair-wise alignments,
     *      build the phylogenetic tree
     *      align the seq defined by tree (bottom up)
     */
    public void phylogeneticTree_alignment(){

    }
}
