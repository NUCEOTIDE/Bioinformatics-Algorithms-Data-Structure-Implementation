import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class multiple_alignment {

    private String[] target_seq;  //two string to be aligned
    private float[][] scoring_scheme;  //scoring scheme
    private String syllabus;  //syllabus string,should contain the gap symbol
    private float penalty;  //empty space penalty
    private int dimension;  //indicate dimension
    private Nth_dimensionPoint[] multiDimension_matrix;  //multiple dimensional matrix
    private String[] answer;  //answer string array

    /**
     * the main constructor_initialize method for multiple alignment class
     * @param type the type of the call
     * @return return a null object, mind the NullPointerException
     */
    public static multiple_alignment setMultiple_alignment(String type){
        switch(type){
            case "default": return new multiple_alignment();
            case "input": {
                Scanner s=new Scanner(System.in);
                return new multiple_alignment(s);
            }
            case "homework": return new multiple_alignment(true);
            default: return new multiple_alignment();
        }
    }

    /**
     * default constructor without parameter
     */
    private multiple_alignment(){
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

    /**
     * special constructor method specialized for this project
     * @param isHomework a boolean variable to determine if the call of this constructor is appropriate
     */
    public multiple_alignment(boolean isHomework){
        dimension=2;
        target_seq=new String[dimension];
        target_seq[0]="ACCGTTAACGTT";
        target_seq[1]="AACGCGTTACAC";
//        target_seq[2]="AAAC";
//        target_seq[3]="AAAG";
        penalty=-2;
        syllabus="ACTG";
        scoring_scheme=new float[syllabus.length()][syllabus.length()];
        for(int i=0;i<syllabus.length();i++)
            for(int j=0;j<syllabus.length();j++){
                if(syllabus.charAt(i)==syllabus.charAt(j)) scoring_scheme[i][j]=1;
                else scoring_scheme[i][j]=-1;
            }
        sortTarget_seq();
        modify_seq();
        //corrected, using matrix scale only as big as sequence length
        int dimension_1_length=1;
        for(int j=0;j<target_seq.length;j++)
            dimension_1_length*=(target_seq[j].length()+1);
        multiDimension_matrix=new Nth_dimensionPoint[dimension_1_length];
        answer=new String[dimension];
        for(int i=0;i<answer.length;i++)
            answer[i]="";
    }

    /**
     * customized constructor requires inputs
     * @param a a scanner object
     */
    private multiple_alignment(Scanner a){
        try{
            System.out.println("Please indicate the size of the database: ");
            dimension=a.nextInt();
            target_seq=new String[dimension];
            System.out.println("Please input target sequences (seperate with \\n): ");
            for(int i=0;i<target_seq.length;i++)
                target_seq[i]=a.nextLine();
            System.out.println("Please input any seq that contains all(include '-') syllabus: "); //wait to be implemented
            syllabus=a.nextLine();
            System.out.println("Please input gap penalty: ");
            penalty=a.nextFloat();
            System.out.println("Please input scoring scheme: ");
            //corrected, using matrix scale only as big as sequence length
            int dimension_1_length=1;
            for(int j=0;j<target_seq.length;j++)
                dimension_1_length*=(target_seq[j].length()+1);
            multiDimension_matrix=new Nth_dimensionPoint[dimension_1_length];
            answer=new String[dimension];
            for(int i=0;i<answer.length;i++)
                answer[i]="";
        }catch(Exception e){
            switch(e.getMessage()){
                case "NullPointerException": System.out.println("drunk and han");
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
        Nth_dimensionalMatrix_initial(0,null);
        Nth_dimensionalMatrix_generate(0,null);

        //finding the maximum point in the matrix
        int[] maximumPoint=maximumScore_position();
        //trace back
        for(int i=0;i<maximumPoint.length;i++)
            Nth_dimensionalMatrix_traceBack(multiDimension_matrix[maximumPoint[i]]);
    }

    /**
     * the 2nd main multiple seq alignment method
     *      using CLASTALW model, or improved T-coffee model
     *      comparing all the pair-wise alignments,
     *      build the phylogenetic tree
     *      align the seq defined by tree (bottom up)
     */
    public void Tcoffee_alignment(){
        //Nth_dimensionalMatrix_initial(dimension);
    }

    /**
     * initialize the scoring matrix
     * @param current_dimensionIndex an index to control the dimensional coordination generation
     * @param current_positionCoord current position coordination of a point in the matrix
     */
    private void Nth_dimensionalMatrix_initial(int current_dimensionIndex,int[] current_positionCoord){
        if(current_dimensionIndex==dimension){
            int position=Nth_dimensionPoint.Nth_to_1st_dimension(current_positionCoord,target_seq);
            multiDimension_matrix[position]=new Nth_dimensionPoint(dimension,current_positionCoord,0,target_seq);
        }else if(current_dimensionIndex==0){
            int[] temp_positionCoord=new int[1];
            for(int i=0;i<target_seq[0].length();i++){
                temp_positionCoord[0]=i;
                Nth_dimensionalMatrix_initial(1,temp_positionCoord);
            }
        }else{
            int[] nextCoord=new int[current_positionCoord.length+1];
            System.arraycopy(current_positionCoord,0,nextCoord,0,current_positionCoord.length);
            for(int i=0;i<target_seq[current_dimensionIndex].length();i++){
                nextCoord[nextCoord.length-1]=i;
                Nth_dimensionalMatrix_initial(current_dimensionIndex+1,nextCoord);
            }
        }
    }

    /**
     * generate the score for all points in the matrix
     * @param current_dimensionIndex an index to control the dimensional coordination generation
     * @param current_positionCoord current position coordination of a point in the matrix
     */
    private void Nth_dimensionalMatrix_generate(int current_dimensionIndex,int[] current_positionCoord){
        if(current_dimensionIndex==dimension){
            int temp_coordinateSum=0;
            for(int i=0;i<current_positionCoord.length;i++)
                if(current_positionCoord[i]!=0) temp_coordinateSum++;
            if(temp_coordinateSum>1){
                int position=Nth_dimensionPoint.Nth_to_1st_dimension(current_positionCoord,target_seq);
                multiDimension_matrix[position].setScore(maximum(current_positionCoord,position));
            }
        }else if(current_dimensionIndex==0){
            int[] temp_positionCoord=new int[1];
            for(int i=0;i<target_seq[0].length();i++){
                temp_positionCoord[0]=i;
                Nth_dimensionalMatrix_generate(1,temp_positionCoord);
            }
        }else{
            int[] nextCoord=new int[current_positionCoord.length+1];
            System.arraycopy(current_positionCoord,0,nextCoord,0,current_positionCoord.length);
            for(int i=0;i<target_seq[current_dimensionIndex].length();i++){
                nextCoord[nextCoord.length-1]=i;
                Nth_dimensionalMatrix_generate(current_dimensionIndex+1,nextCoord);
            }
        }
    }

    /**
     * method to find the maximum score in one position
     * @param coordination the coordiantion of the position
     * @param position the position in 1 dimensional matrix
     * @return the final maximum score of this point
     */
    private float maximum(int[] coordination,int position){
        float final_score=0;
        for(int i=1;i<=Math.pow(2,dimension)-1;i++){
            int[] binary_direction=Nth_dimensionPoint.binaryDirection(dimension,i);
            int previousPosition=Nth_dimensionPoint.previousPosition(coordination,binary_direction,target_seq);
            if(previousPosition>=0){
                float temp_score=multiDimension_matrix[previousPosition].getScore()+matching(position,binary_direction);
                if(final_score<temp_score)
                    final_score=temp_score;
            }
        }
        return final_score;
    }
    private int[] maximumScore_position(){

        List<Integer> maximunIndex=new ArrayList<>();
        maximunIndex.add(0);
        try{
            for(int k=1;k<multiDimension_matrix.length;k++){
                if(multiDimension_matrix[maximunIndex.get(0)].getScore()<multiDimension_matrix[k].getScore()){
                    maximunIndex.clear();
                    maximunIndex.add(k);
                    //System.out.println(multiDimension_matrix[k].getScore());
                }else if(multiDimension_matrix[maximunIndex.get(0)].getScore()==multiDimension_matrix[k].getScore())
                    maximunIndex.add(k);
            }
            for(int m=0;m<maximunIndex.size();m++)
                Nth_dimensionalMatrix_traceBack(multiDimension_matrix[m]);
            for(int n=0;n<answer.length;n++){
                System.out.println(answer[n]);
            }

        }catch (NullPointerException e){
            System.out.println("Null character exist for unknown reason");
        }finally {
            int temp[]=new int[maximunIndex.size()];
            for(int i=0;i<temp.length;i++)
                temp[i]=maximunIndex.get(i);
            return temp;
        }
    }

    /**
     * method to find the score for one given direction
     * @param position the position in 1 dimensional matrix
     * @param direction the direction coordination of the point
     * @return the matching score sum
     */
    private float matching(int position,int[] direction){
        multiDimension_matrix[position].setTempSeq(direction);
        return multiDimension_matrix[position].alignmentScore_sum(scoring_scheme,syllabus,penalty);
    }

    /**
     * method to sort the given target sequences and repalace the 1st position with the longest
     * but doesn't matter any more now
     */
    private void sortTarget_seq(){
        int longestString_index=0;
        for(int i=1;i<target_seq.length;i++)
            if(target_seq[longestString_index].length()<target_seq[i].length())
                longestString_index=i;
        String temp=target_seq[0];
        target_seq[0]=target_seq[longestString_index];
        target_seq[longestString_index]=temp;
    }

    private void modify_seq(){
        for(int i=0;i<target_seq.length;i++)
            target_seq[i]="$"+target_seq[i];
    }

    /**
     * trace back to build the answer string
     * @param currentPoint the current point object
     */
    private void Nth_dimensionalMatrix_traceBack(Nth_dimensionPoint currentPoint){
        if(currentPoint.getScore()!=0)
            //get direction for both answer building and tracing back process
            for(int i=1;i<=Math.pow(2,dimension)-1;i++){
                int[] binary_direction=Nth_dimensionPoint.binaryDirection(dimension,i);
                int previousPosition  //previous position obtaining method can be changed to simplify
                        =Nth_dimensionPoint.previousPosition(currentPoint.getCoordination(),binary_direction,target_seq);
                if(multiDimension_matrix[previousPosition].getScore()  //the score of previous point
                        +matching(Nth_dimensionPoint.Nth_to_1st_dimension(currentPoint.getCoordination(),target_seq),binary_direction)
                        ==currentPoint.getScore()){
                    //answer string array building
                    currentPoint.setTempSeq(binary_direction);
                    for(int n=0;n<answer.length;n++)  //may be changed to the string buffer method
                        answer[n]=String.valueOf(currentPoint.getTempSeq()[n])+answer[n];
                    //continue tracing back to the previous point
                    Nth_dimensionalMatrix_traceBack(multiDimension_matrix[previousPosition]);
                }
            }
        else for(int i=0;i<answer.length;i++) answer[i]="";
    }

    public void get_multiDimensionMatrix(){
        try{
            for(int i=0;i<multiDimension_matrix.length;i++){
                int[] temp_string=multiDimension_matrix[i].getCoordination();
                System.out.print("{");
                for(int j=0;j<temp_string.length;j++)
                    System.out.print(temp_string[j]+",");
                System.out.println("}"+multiDimension_matrix[i].getScore());
            }
        }catch (NullPointerException e){
            System.out.println("null character exist for unknown reason");
        }
    }

    public void getScoring_scheme(){
        System.out.println(syllabus);
        for(int i=0;i<scoring_scheme.length;i++){
            System.out.print(syllabus.charAt(i)+" ");
            for(int j=0;j<scoring_scheme[0].length;j++)
                System.out.print(scoring_scheme[i][j]+" ");
            System.out.println();
        }
    }
}
