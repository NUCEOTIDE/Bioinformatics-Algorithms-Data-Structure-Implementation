public class Nth_dimensionPoint {
    private int[] coordination;  //point coordination in Nth dimension
    private int dimension;  //num of dimension
    private float score;  //score in point
    private char[] seq; //sequence data at the point
    private char[] tempSeq;  //sequence data at the point considered gaps and directions

    /**
     * the constructor of Nth_dimentionPoint class
     * @param new_dimension
     * @param new_coordination
     * @param new_score
     * @param target_seq
     */
    public Nth_dimensionPoint(int new_dimension,int[] new_coordination,float new_score,String[] target_seq){
        coordination=new int[new_dimension];
        for(int i=0;i<dimension;i++)
            coordination[i]=new_coordination[i];
        dimension=new_dimension;
        score=new_score;
        for(int i=0;i<dimension;i++){
            seq[i]=target_seq[i].charAt(coordination[i]);
        }
        System.arraycopy(seq,0,tempSeq,0,seq.length);  //initialize the temp sequence as the sequence
    }

    /**
     * a static method that convert a Nth coordination to a position in 1 dimensional object array multiDimension_matrix
     * @param coordination
     * @param target_seq
     * @return
     */
    public static int Nth_to_1st_dimension(int[] coordination,String[] target_seq){
        int position=0;
        for(int k=0;k<coordination.length;k++){
            int positionComponent=coordination[k];
            for(int m=k+1;m<coordination.length-1;m++)
                positionComponent*=target_seq[m].length();
            position+=positionComponent;
        }
        position+=coordination[coordination.length-1];
        return position;
    }

    /**
     * convert a directional variable into a binary dimension in length of dimension
     * @param dimension
     * @param direction
     * @return
     */
    public static int[] binaryDirection(int dimension,int direction){
          //might be errors, because of the different length of the string and binary code
        int[] binary_direction=new int[dimension];
        String directionString=Integer.toBinaryString(direction);
        while(directionString.length()<dimension)
            directionString="0"+directionString;  //may be changed to the string buffer method
        for(int pos=0;pos<dimension;pos++)
            binary_direction[pos]=(int)directionString.charAt(pos);
        return binary_direction;
    }

    /**
     * obtain the previous position of a given point
     * may be considered to change to an instant method instead of a static method
     * @param currentCoordination
     * @param direction
     * @param target_seq
     * @return
     */
    public static int previousPosition(int[] currentCoordination,int[] direction,String[] target_seq){
        int[] previousCoordination=new int[currentCoordination.length];
        try{
            for(int i=0;i<currentCoordination.length;i++)
                previousCoordination[i]=currentCoordination[i]-direction[i];
        }catch(IndexOutOfBoundsException e){
            System.out.println("something goes wrong...");
        }finally{
            return Nth_to_1st_dimension(previousCoordination,target_seq);
        }
    }

    /**
     * calculate the su of all scores at current point
     * @param scoring_scheme
     * @param syllabus
     * @param penalty
     * @return
     */
    public float alignmentScore_sum(float[][] scoring_scheme,String syllabus,float penalty){  //Exception to be handled
        float sumScore=0;
        for(int i=0;i<tempSeq.length;i++)
            for(int j=i+1;j<tempSeq.length;j++){
                boolean isReevaluated1=false,isReevaluated2=false;
                for(int k=0;k<syllabus.length();k++){
                    if(syllabus.charAt(k)==tempSeq[i]) isReevaluated1=true;
                    if(syllabus.charAt(k)==tempSeq[j]) isReevaluated2=true;
                }
                if(isReevaluated1&&isReevaluated2) sumScore+=scoring_scheme[i][j];
                else if(tempSeq[i]=='-'||tempSeq[j]=='-') sumScore+=penalty;  //else, two '-' should not exist
            }
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
     * @param direction
     */
    public void setTempSeq(int[] direction){
        for(int i=0;i<direction.length;i++)
            if(direction[i]==0) tempSeq[i]='-';
    }
}
