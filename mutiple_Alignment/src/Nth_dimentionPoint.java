public class Nth_dimentionPoint {
    private int[] coordination;  //point coordination in Nth dimension
    private int dimension;  //num of dimension
    private float score;  //score in point
    private char[] seq; //sequence data at the point
    private char[] tempSeq;  //sequence data at the point considered gaps and directions

    public Nth_dimentionPoint(int new_dimension,int[] new_coordination,float new_score,String[] target_seq){
        coordination=new int[new_dimension];
        for(int i=0;i<dimension;i++)
            coordination[i]=new_coordination[i];
        dimension=new_dimension;
        score=new_score;
        for(int i=0;i<dimension;i++){
            seq[i]=target_seq[i].charAt(coordination[i]);
        }
        System.arraycopy(seq,0,tempSeq,0,seq.length);
    }


    public static int Nth_to_1st_dimension(int[] coordination,String[] target_seq){
        int position=0;
        for(int k=0;k<coordination.length-1;k++){
            position+=coordination[k]*target_seq[k].length();
        }
        position+=coordination[coordination.length-1];
        return position;
    }

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
    public void setTempSeq(int[] direction){
        for(int i=0;i<direction.length;i++)
            if(direction[i]==0) tempSeq[i]='-';
    }
}
