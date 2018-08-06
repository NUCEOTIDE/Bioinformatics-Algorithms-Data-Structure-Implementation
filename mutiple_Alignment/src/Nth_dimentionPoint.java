public class Nth_dimentionPoint {
    private int[] coordination;  //point coordination in Nth dimension
    private int dimension;  //num of dimension
    private float score;  //score in point
    private char[] seq; //sequence data at the point

    public Nth_dimentionPoint(int new_dimension,int[] new_coordination,float new_score,String[] target_seq){
        coordination=new int[new_dimension];
        score=new_score;

    }

    public void setCoordination(int[] new_coordination){
        //coordination=new_coordination;
        for(int i=0;i<new_coordination.length;i++)
            coordination[i]=new_coordination[i];
    }
    public void setScore(float new_score){
        score=new_score;
    }
    
    public static int Nth_to_1st_dimension(int[] coordination,int length){
        int position=0;
        for(int k=0;k<coordination.length-1;k++){
            position+=coordination[k]*length;
        }
        position+=coordination[coordination.length];
        return position;
    }

    public static int previousPosition(int[] currentCoordination,int[] direction,int length){
        int[] previousCoordination=new int[currentCoordination.length];
        try{
            for(int i=0;i<currentCoordination.length;i++)
                previousCoordination[i]=currentCoordination[i]-direction[i];
        }catch(IndexOutOfBoundsException e){

        }finally{
            return Nth_to_1st_dimension(previousCoordination,length);
        }
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
}
