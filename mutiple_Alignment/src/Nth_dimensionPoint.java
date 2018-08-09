public class Nth_dimensionPoint {
    private int[] coordination;  //point coordination in Nth dimension
    private int dimension;  //num of dimension
    private float score;  //score in point
    private char[] seq; //sequence data at the point
    private char[] tempSeq;  //sequence data at the point considered gaps and directions

    /**
     * the constructor of Nth_dimentionPoint class
     * @param new_dimension the dimension of the point
     * @param new_coordination the coordination of the point
     * @param new_score the score of the point, initially is 0
     * @param target_seq the target sequences inputted
     */
    public Nth_dimensionPoint(int new_dimension,int[] new_coordination,float new_score,String[] target_seq){
        dimension=new_dimension;
        coordination=new int[dimension];
        System.arraycopy(new_coordination,0,coordination,0,coordination.length);
        score=new_score;
        seq=new char[target_seq.length];
        tempSeq=new char[target_seq.length];
        for(int i=0;i<target_seq.length;i++)
            seq[i]=target_seq[i].charAt(coordination[i]);
        System.arraycopy(seq,0,tempSeq,0,seq.length);  //initialize the temp sequence as the sequence
    }

    /**
     * a static method that convert a Nth coordination to a position in 1 dimensional object array multiDimension_matrix
     * @param coordination the coordination of the point
     * @param target_seq the target sequences inputted
     * @return a 1 dimensional position
     */
    public static int Nth_to_1st_dimension(int[] coordination,String[] target_seq,boolean outOfRange){
        int position=0;
        if(!outOfRange){
            for(int k=0;k<coordination.length-1;k++){
                int positionComponent=coordination[k];
                for(int m=k+1;m<coordination.length;m++)
                    positionComponent*=target_seq[m].length();
                position+=positionComponent;
            }
            position+=coordination[coordination.length-1];
        }
        else position=-1;
        return position;
    }

    /**
     * convert a directional variable into a binary dimension in length of dimension
     * @param dimension the dimension of the point
     * @param decimal_direction the decimal direction coordination
     * @return a binary direction
     */
    public static int[] binaryDirection(int dimension,int decimal_direction){
          //might be errors, because of the different length of the string and binary code
        int[] binary_direction=new int[dimension];
        String directionString=Integer.toBinaryString(decimal_direction);
        while(directionString.length()<dimension)
            directionString="0"+directionString;  //may be changed to the string buffer method
            //System.out.println(directionString);
        for(int pos=0;pos<dimension;pos++)
            binary_direction[pos]=(int)directionString.charAt(pos)-48;
        return binary_direction;
    }

    /**
     * obtain the previous position of a given point
     * may be considered to change to an instant method instead of a static method
     * @param currentCoordination the current position coordination of the point
     * @param direction the direction coordination of the point
     * @param target_seq the target sequences
     * @return to call another method to convert the position into 1st dimensional
     */
    public static int previousPosition(int[] currentCoordination,int[] direction,String[] target_seq){
        int[] previousCoordination=new int[currentCoordination.length];
        boolean outOfRange=false;
        try{
            for(int i=0;i<currentCoordination.length;i++){
                previousCoordination[i]=currentCoordination[i]-direction[i];
                if(previousCoordination[i]<0) outOfRange=true;
            }
        }catch(IndexOutOfBoundsException e){
            System.out.println("something goes wrong...");
        }
        return Nth_to_1st_dimension(previousCoordination,target_seq,outOfRange);
    }

    /**
     * calculate the su of all scores at current point
     * @param scoring_scheme the scoring scheme of the seuqences
     * @param syllabus the syllabus
     * @param penalty the gap penalty results
     * @return the sum of scores
     */
    public float alignmentScore_sum(float[][] scoring_scheme,String syllabus,float penalty){  //Exception to be handled
        float sumScore=0;
        for(int i=0;i<tempSeq.length-1;i++)
            for(int j=i+1;j<tempSeq.length;j++){
                boolean isReevaluated1=false,isReevaluated2=false;
                int indexI=0,indexJ=0;
                for(int k=0;k<syllabus.length();k++){
                    if(syllabus.charAt(k)==tempSeq[i]){
                        isReevaluated1=true;
                        indexI=k;
                    }
                    if(syllabus.charAt(k)==tempSeq[j]){
                        isReevaluated2=true;
                        indexJ=k;
                    }
                }
                if(isReevaluated1&&isReevaluated2) sumScore+=scoring_scheme[indexI][indexJ];
                else if(tempSeq[i]=='-'||tempSeq[j]=='-') sumScore+=penalty;  //else, two '-' should not exist
            }
        System.arraycopy(seq,0,tempSeq,0,seq.length);
        return sumScore;
    }

    //get methods
    public float getScore() {
        return this.score;
    }
    public int getDimension(){
        return this.dimension;
    }
    public int[] getCoordination() {
        return this.coordination;
    }
    public char[] getSeq(){
        return this.seq;
    }
    public char[] getTempSeq(){
        return this.tempSeq;
    }

    //set methods
    public void setCoordination(int[] new_coordination){
        //coordination=new_coordination;
        System.arraycopy(new_coordination,0,coordination,0,new_coordination.length);
    }
    public void setScore(float new_score){
        score=new_score;
    }

    /**
     * replace the seq[] with tempSeq[] the difference is the gaps added corresponding to the directions
     * @param direction the binary direction coordination of one pointR
     */
    public void setTempSeq(int[] direction){
        for(int i=0;i<direction.length;i++){
            if(direction[i]==0) tempSeq[i]='-';
        }

    }
}
