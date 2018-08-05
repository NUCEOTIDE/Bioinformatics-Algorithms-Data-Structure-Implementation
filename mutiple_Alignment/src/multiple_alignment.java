import java.util.Scanner;

public class multiple_alignment {

    private String[] target_seq;  //two string to be aligned
    private float[][] scoring_scheme;  //scoring scheme
    private String syllabus;
    private float penalty;  //empty space penalty
    private int dimension;
    private Nth_dimentionPoint[] mutiDimension_matrix;  //multiple dimensional matrix
    //private float[] scoring_dataSheet_1d;  //scoring matrix
    private String[] answer;  //answer string array
    //private int count=-1;  //records of length


    public static multiple_alignment Setmutiple_alignment(String type){
        switch(type){
            case "default":{
                multiple_alignment temp=new multiple_alignment();
                return temp;
            }
            case "input":{
                Scanner s=new Scanner(System.in);
                multiple_alignment temp=new multiple_alignment(s);
                return temp;
            }
            default:{
                return null;
            }
        }
    }
    public multiple_alignment(){
        //target_seq default initialization
        target_seq=new String[2];
        for(int i=0;i<target_seq.length;i++)
            target_seq[i]="";

        //scoring_scheme default initialization
        scoring_scheme=new float[4][4];
        for(int i=0;i<scoring_scheme.length;i++)
            for(int j=0;j<scoring_scheme[0].length;j++)
                scoring_scheme[i][j]=0;

        //syllabus default initialization
        syllabus="";

        //penalty default initialization
        penalty=0;

        //answer default initialization
        answer=new String[2];
        for(int i=0;i<answer.length;i++)
            answer[i]="";
    }
    public multiple_alignment(Scanner a){
        try{
            System.out.println("Please indicate the size of the database: ");
            dimension=a.nextInt();
            target_seq=new String[dimension];
            System.out.println("Please input target sequences (seperate with \\n): ");
            for(int i=0;i<target_seq.length;i++)
                target_seq[i]=a.nextLine();
            System.out.println("Please input any seq that contains all/(but just) syllabus: "); //wait to be implemented
            syllabus=a.nextLine();
            System.out.println("Please input gap penalty: ");
            penalty=a.nextFloat();
            mutiDimension_matrix=new Nth_dimentionPoint[(int)Math.pow(target_seq[0].length(),dimension)]; //to be implemented
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
        Nth_dimensionalMatrix_initial();
    }

    /**
     * the 2nd main mutiple seq alignment method
     *      using CLASTALW model,
     *      comparing all the pair-wise alignments,
     *      build the phylogenetic tree
     *      align the seq defined by tree (bottom up)
     */
    public void phylogeneticTree_alignment(){
        //Nth_dimensionalMatrix_initial(dimension);
    }

    private void Nth_dimensionalMatrix_initial(int current_dimensionIndex,int[] current_positionCoord){
        if(current_dimensionIndex==dimension-1){
            int position=0;
            for(int k=0;k<current_positionCoord.length-1;k++){
                position+=current_positionCoord[k]*target_seq.length;
            }
            position+=current_positionCoord[current_positionCoord.length];
            mutiDimension_matrix[position]=new Nth_dimentionPoint(dimension,current_positionCoord,0);
            return;
        }
        else{
            int[] nextCord=new int[current_positionCoord.length+1];
            for(int i=0;i<target_seq[0].length();i++){
                nextCord[nextCord.length-1]=i;
                Nth_dimensionalMatrix_initial(current_dimensionIndex+1,nextCord);
            }

        }
    }

    private  void Nth_dimensionalMatrix_starting(){
        for(int i=0;i<mutiDimension_matrix.length;i++){
            int[] temp_coordination=mutiDimension_matrix[i].getCoordination();
            for(int j=0;j<temp_coordination.length;j++)
                if(temp_coordination[j]==0) mutiDimension_matrix[i].setData(0);
        }
    }

    private void Nth_dimensionalMatrix_generate(){
        for(int i=0;i<mutiDimension_matrix.length;i++){
            mutiDimension_matrix[i]=new Nth_dimentionPoint();
        }
    }

    private void Nth_dimensionalMatrix_traceback(Nth_dimentionPoint currentPoint,int[] direction){

    }
}
