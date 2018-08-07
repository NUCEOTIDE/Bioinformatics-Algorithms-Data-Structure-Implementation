import java.util.Scanner;

public class multiple_alignment {

    private String[] target_seq;  //two string to be aligned
    private float[][] scoring_scheme;  //scoring scheme
    private String syllabus;  //syllabus string
    private float penalty;  //empty space penalty
    private int dimension;  //indicate dimension
    private Nth_dimentionPoint[] mutiDimension_matrix;  //multiple dimensional matrix
    private String[] answer;  //answer string array


    public static multiple_alignment Setmultiple_alignment(String type){
        switch(type){
            case "default":{
                multiple_alignment temp=new multiple_alignment();
                return temp;
            }case "input":{
                Scanner s=new Scanner(System.in);
                multiple_alignment temp=new multiple_alignment(s);
                return temp;
            }case "homework":{
                multiple_alignment temp=new multiple_alignment(true);
                return temp;
            }default: return null;
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
    public multiple_alignment(boolean isHomework){

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
            System.out.println("Please input scoring scheme: ");
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
    public void dynamicProgramming_alignment(){
        int[] temp_positionCoord=new int[1];
        for(int i=0;i<target_seq[0].length();i++){
            temp_positionCoord[0]=i;
            Nth_dimensionalMatrix_initial(0,temp_positionCoord);
        }
        for(int j=0;j<target_seq[0].length();j++){
            temp_positionCoord[0]=j;
            Nth_dimensionalMatrix_generate(0,temp_positionCoord);
        }

    }

    /**
     * the 2nd main mutiple seq alignment method
     *      using CLASTALW model, or improved T-coffee model
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
            for(int k=0;k<current_positionCoord.length-1;k++)
                position+=current_positionCoord[k]*target_seq[0].length();
            position+=current_positionCoord[current_positionCoord.length];
            mutiDimension_matrix[position]=new Nth_dimentionPoint(dimension,current_positionCoord,0,target_seq);
            return;
        }else{
            int[] nextCoord=new int[current_positionCoord.length+1];
            for(int i=0;i<target_seq[0].length();i++){
                nextCoord[nextCoord.length-1]=i;
                Nth_dimensionalMatrix_initial(current_dimensionIndex+1,nextCoord);
            }
        }
        return;
    }

    private void Nth_dimensionalMatrix_generate(int current_dimensionIndex,int[] current_positionCoord){
        if(current_dimensionIndex==dimension-1){
            int temp_coordinateSum=0;
            for(int i=0;i<current_positionCoord.length;i++)
                if(current_positionCoord[i]!=0) temp_coordinateSum++;
            if(temp_coordinateSum>1){
                int position=Nth_dimentionPoint.Nth_to_1st_dimension(current_positionCoord,target_seq[0].length());
                mutiDimension_matrix[position].setScore(maximum(current_positionCoord,position));
            }
            return;
        }else{
            int[] nextCoord=new int[current_positionCoord.length+1];
            for(int i=0;i<target_seq[0].length();i++){
                nextCoord[nextCoord.length-1]=i;
                Nth_dimensionalMatrix_generate(current_dimensionIndex+1,nextCoord);
            }
        }
        return;
    }

    private float maximum(int[] coordination,int position){
        float final_score=0;
        int[] binary_direction=new int[coordination.length];
        for(int i=1;i<=Math.pow(2,dimension)-1;i++){
            for(int pos=0;pos<coordination.length;pos++){
                binary_direction[pos]=(int)(Integer.toBinaryString(i).charAt(pos));
            }
            int previousPosition=Nth_dimentionPoint.previousPosition(coordination,binary_direction,target_seq.length);
            float temp_score=mutiDimension_matrix[previousPosition].getScore()+matching(position,binary_direction);
            if(final_score<temp_score)
                final_score=temp_score;
        }
        return final_score;
    }

    private float matching(int position,int[] direction){

        return (float)1.11;
    }

    private void sortTarget_seq(){
        int longestString_index=0;
        for(int i=1;i<target_seq.length;i++)
            if(target_seq[longestString_index].length()<target_seq[i].length())
                longestString_index=i;
        String temp=target_seq[0];
        target_seq[0]=target_seq[longestString_index];
        target_seq[longestString_index]=temp;
    }

    private void Nth_dimensionalMatrix_traceback(Nth_dimentionPoint currentPoint,int[] direction){

    }
}
