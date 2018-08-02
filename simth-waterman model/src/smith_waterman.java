import java.lang.*;


public class smith_waterman{
    private String A,B;  /**two string to be aligned*/
    private String[][] scoring_scheme;  /**scoring scheme*/
    private float penalty;  /**empty space penalty*/
    private float[][] scoring_dataSheet;  /**scoring matrix*/
    private String[] answer;  /**answer string*/
    private int count=-1;  /**records of length*/

    public smith_waterman(String types){
        switch (types){
            //case "default": new smith_waterman();
            //case "Settings from files": new smith_waterman();
            case "homework": new smith_waterman();
        }
    }

    /**
     * default object constructor
     *
     */
    public smith_waterman(){

        /**input String A and B*/
        A="ACCGTTAACGTT";
        B="AACGCGTTACAC";
        System.out.println(A);
        System.out.println(B);

        /**construct scoring scheme*/
        String tempScore_string="ACTG";
        scoring_scheme=new String[4+1][4+1];
        scoring_scheme[0][0]="0";
        for(int i=1;i<scoring_scheme.length;i++)
            for(int j=1;j<scoring_scheme[0].length;j++){
                scoring_scheme[i][0]=String.valueOf(tempScore_string.charAt(i-1));
                scoring_scheme[0][j]=String.valueOf(tempScore_string.charAt(j-1));
                if(i==j) scoring_scheme[i][j]="1";
                else scoring_scheme[i][j]="-1";
            }
        for(int i=0;i<scoring_scheme.length;i++){
            for(int j=0;j<scoring_scheme[0].length;j++)
                System.out.print(scoring_scheme[i][j]);
            System.out.println();
        }

        /**construct penalty*/
        penalty=-2;
        int aLen=A.length()+1,bLen=B.length()+1;
        scoring_dataSheet=new float[aLen][bLen];
        answer=new String[2];
        answer[0]="";
        answer[1]="";
    }

    /**
     * main scoring method, do its job by call a number of sub-methods
     * also out put results on terminal
     * @param isGlobal
     */
    public void scoring(boolean isGlobal){
        scoring_dataSheet_initial();
        scoring_calculation(isGlobal);
        for(int i=0;i<scoring_dataSheet.length;i++){
            for(int j=0;j<scoring_dataSheet[0].length;j++)
                System.out.print((int)scoring_dataSheet[i][j]+" ");
            System.out.println();
        }

        if(isGlobal)traceBack_global(scoring_dataSheet.length,scoring_dataSheet[0].length,"start point");
        //else traceBack_local(0,0,"start point");
        else traceBack_local_initial(0,0);
        System.out.println(answer[0]);
        System.out.println(answer[1]);
    }

    /**
     * initialize the scoring_dataSheet by giving all elements in sheet a value of zero
     * the value of first line and first roll will be used for further matrix building
     * @return
     */
    private boolean scoring_dataSheet_initial(){
        try{
            scoring_dataSheet[0][0]=0;
            for(int i=1;i<scoring_dataSheet[0].length;i++){
                scoring_dataSheet[0][i]=0;
            }
            for(int j=1;j<scoring_dataSheet.length;j++){
                scoring_dataSheet[j][0]=0;
            }

        }catch(NullPointerException e){
            return false;
        }
        return true;
    }

    /**
     * go through every element in matrix, fill in the maximum number
     * @param isGlobal
     */
    private void scoring_calculation(boolean isGlobal){
        for(int i=1;i<scoring_dataSheet.length;i++){
            for(int j=1;j<scoring_dataSheet[0].length;j++){
                scoring_dataSheet[i][j]=maximum(isGlobal,i,j);
            }
        }
    }

    /**
     * global trace back method
     * @param i
     * @param j
     * @param traceBack_direction
     */
    private void traceBack_global(int i,int j,String traceBack_direction){
        try{
            count++;
            answerConstructing(i,j,traceBack_direction);
            if(traceBack_direction.equals("start point")){
                /**
                 * global trace back initialization
                 */
                if(scoring_dataSheet[i-1][j]==scoring_dataSheet[i][j]+matching(i,j,"downward"))
                    traceBack_global(i,j,"upward");
                if(scoring_dataSheet[i][j-1]==scoring_dataSheet[i][j]+matching(i,j,"rightward"))
                    traceBack_global(i,j,"leftward");
                if(scoring_dataSheet[i-1][j-1]==scoring_dataSheet[i][j]+matching(i,j,"diagonal"))
                    traceBack_global(i,j,"reverse_diagonal");
            }
            if(i==1&&j==1){
                answerOutputing();
                return;
            }else if((i>1||j>1)&&(i<scoring_dataSheet.length&&j<scoring_dataSheet[0].length)){
                if(scoring_dataSheet[i-1][j]==scoring_dataSheet[i][j]+matching(i,j,"downward"))
                    traceBack_global(i-1,j,"upward");
                if(scoring_dataSheet[i][j-1]==scoring_dataSheet[i][j]+matching(i,j,"rightward"))
                    traceBack_global(i,j-1,"leftward");
                if(scoring_dataSheet[i-1][j-1]==scoring_dataSheet[i][j]+matching(i,j,"diagonal"))
                    traceBack_global(i-1,j-1,"reverse_diagonal");
            }
        }catch(Exception e){
            if(e.getMessage().equals("cannot analyze direction"))
                System.out.println("cannot trace back because of unknown direction during tracing");
            if(i<=0||j<=0||i>=scoring_dataSheet.length||j>=scoring_dataSheet[0].length)
                System.out.println("Index out of bond for unknown reason (At trace back-global)");
        }finally{
            count=-1;
            return;
        }
    }

    /**
     * local trace back method
     * 1. find the largest value in the matrix and record the position as starting position of trace back
     * 2. start recursion and call itself until the value stored in current position reach zero
     * 3. every time the method call itself, examine the conditional statement
     *  (1) traceBack_direction's content = "start point" reveals that it's the first time the method is called
     *  (2) when the recursion is in its progress, every time add strings in to the string "answer"
     *  (3) when the recursion is ended (in one certain path), output the string, reinitialize variables and return to
     *  outer layers
     * @param i
     * @param j
     * @param traceBack_direction
     */
    private void traceBack_local(int i,int j,String traceBack_direction){
        try{
            count++;  /**counting every time to obtain the length of alignment*/
            System.out.println("recursion in progress...");
            if(scoring_dataSheet[i][j]!=0){  /**if the recursion is in progress*/
                answerConstructing(i,j,traceBack_direction);  /**construct answer string*/
                System.out.println("answer constructed"+i+" "+j);
                if((i>=1||j>=1)&&(i<scoring_dataSheet.length&&j<scoring_dataSheet[0].length)){
                    if(scoring_dataSheet[i-1][j]==(scoring_dataSheet[i][j]+matching(i,j,"downward")))
                        traceBack_local(i-1,j,"upward");
                    if(scoring_dataSheet[i][j-1]==(scoring_dataSheet[i][j]+matching(i,j,"rightward")))
                        traceBack_local(i,j-1,"leftward");
                    if(scoring_dataSheet[i-1][j-1]==(scoring_dataSheet[i][j]+matching(i,j,"diagonal")))
                        traceBack_local(i-1,j-1,"reverse_diagonal");
                }
            }else{
                answerOutputing();
                return;
            }
        }catch(Exception e){  /**catching exceptions*/
            if(e.getMessage().equals("cannot analyze direction"))
                System.out.println("cannot trace back because of unknown direction during tracing");
            if(i<=0||j<=0||i>=scoring_dataSheet.length||j>=scoring_dataSheet[0].length)
                System.out.println("Index out of bond for unknown reason (At trace back-local)");
        }finally{
            count=-1;
            return;
        }
    }


    private void traceBack_local_initial(int i,int j){
        try{
            float temp=0;
            int maxPos_i=0,maxPos_j=0;
            for(int k=1;k<scoring_dataSheet.length;k++)
                for(int m=1;m<scoring_dataSheet[0].length;m++)
                    if(scoring_dataSheet[k][m]>scoring_dataSheet[maxPos_i][maxPos_j]){
                        maxPos_i=k;
                        maxPos_j=m;
                    }
            System.out.println(maxPos_i+" "+maxPos_j);
            temp=scoring_dataSheet[maxPos_i][maxPos_j];
            /**local trace back initialization*/
            if(scoring_dataSheet[maxPos_i-1][maxPos_j]==temp+matching(maxPos_i,maxPos_j,"downward"))
                traceBack_local(maxPos_i,maxPos_j,"upward");
            if(scoring_dataSheet[maxPos_i][maxPos_j-1]==temp+matching(maxPos_i,maxPos_j,"rightward"))
                traceBack_local(maxPos_i,maxPos_j,"leftward");
            if(scoring_dataSheet[maxPos_i-1][maxPos_j-1]==temp+matching(maxPos_i,maxPos_j,"diagonal"))
                traceBack_local(maxPos_i,maxPos_j,"reverse_diagonal");
        }catch(Exception e){
            if(e.getMessage().equals("cannot analyze direction"))
                System.out.println("cannot trace back because of unknown direction during tracing");
            if(i<=0||j<=0||i>=scoring_dataSheet.length||j>=scoring_dataSheet[0].length)
                System.out.println("Index out of bond for unknown reason (At trace back-local)");
        }

    }

    /**
     * finding maximun score path between nodes in matrix
     * @param isGlobal
     * @param i
     * @param j
     * @return
     */
    private float maximum(boolean isGlobal,int i,int j){
        float temp_score=0;
        try{
            if(isGlobal){
                temp_score=scoring_dataSheet[i-1][j-1]+matching(i,j,"diagonal");
                if(scoring_dataSheet[i-1][j]+matching(i,j,"downward")>temp_score)
                    temp_score=scoring_dataSheet[i-1][j]+matching(i,j,"downward");
                if(scoring_dataSheet[i][j-1]+matching(i,j,"rightward")>temp_score)
                    temp_score=scoring_dataSheet[i][j-1]+matching(i,j,"rightward");
            }else if(!isGlobal){
                if(scoring_dataSheet[i-1][j-1]+matching(i,j,"diagonal")>temp_score)
                    temp_score=scoring_dataSheet[i-1][j-1]+matching(i,j,"diagonal");
                if(scoring_dataSheet[i-1][j]+matching(i,j,"downward")>temp_score)
                    temp_score=scoring_dataSheet[i-1][j]+matching(i,j,"downward");
                if(scoring_dataSheet[i][j-1]+matching(i,j,"rightward")>temp_score)
                    temp_score=scoring_dataSheet[i][j-1]+matching(i,j,"rightward");
            }
        }catch (Exception e){
            if(e.getMessage().equals("cannot find character")) System.out.println("cannot find character in arrays");
            if(e.getMessage().equals("cannot analyze direction")) System.out.println("cannot analyze direction");
        }
        finally{
            return temp_score;
        }
    }

    /**
     * match the direction with coordinate score
     * @param i
     * @param j
     * @param direction
     * @return
     * @throws Exception
     */
    private float matching(int i,int j,String direction)throws Exception{
        int a_pos=0,b_pos=0;
            switch (direction) {
                case "diagonal": {
                    try {
                    char a = A.charAt(i - 1), b = B.charAt(j - 1);
                    for (int k = 0; k < scoring_scheme.length; k++) if (a == scoring_scheme[k][0].charAt(0)) a_pos = k;
                    for (int m = 0; m < scoring_scheme[0].length; m++)
                        if (b == scoring_scheme[0][m].charAt(0)) b_pos = m;
                    if (a_pos == 0 || b_pos == 0) throw new Exception("cannot find character");
                    }finally {
                        return Integer.parseInt(scoring_scheme[a_pos][b_pos]);
                    }
                }
                case "rightward": return penalty;
                case "downward": return penalty;
                default: {
                    throw new Exception("cannot analyze direction");
                }
            }
    }

    /**
     * answer output (to terminator) method
     * @throws Exception
     */
    private void answerOutputing()throws Exception{
        System.out.println("the length of the matched seq:"+count);
        System.out.println(answer[0]);
        System.out.println(answer[1]);
        answer[0]="";
        answer[1]="";
        count=0;
    }

    /**
     * answer processing method
     * @param i
     * @param j
     * @param traceBack_direction
     */
    private void answerConstructing(int i,int j,String traceBack_direction)/*throws Exception*/{
        if(traceBack_direction.equals("reverse_diagonal")){
            answer[0]+=String.valueOf(A.charAt(i));
            answer[1]+=String.valueOf(B.charAt(j));
        }else if(traceBack_direction.equals("leftward")){
            answer[0]+="-";
            answer[1]+=String.valueOf(B.charAt(j));
        }else if(traceBack_direction.equals("upward")){
            answer[0]+=String.valueOf(A.charAt(i));
            answer[1]+="-";
        }
    }


    /**
     * get methods
     */
    private String getA(){
        return this.A;
    }
    private String getB(){
        return this.B;
    }
    private String[] getAnswer(){
        return this.answer;
    }
    private float getPenalty(){
        return this.penalty;
    }
    private String[][] getScoring_scheme(){
        return this.scoring_scheme;
    }
    private float[][] getScoring_dataSheet(){
        return this.scoring_dataSheet;
    }

    /**
     * set methods
     */
    private void setA(String A){
        this.A=A;
    }
    private void setB(String B){
        this.B=B;
    }
    private void setScoring_scheme(String[][] scoring_scheme){
        this.scoring_scheme=scoring_scheme;
    }
    private void setPenalty(float penalty){
        this.penalty=penalty;
    }
    private void setScoring_dataSheet(float[][] scoring_dataSheet){
        this.scoring_dataSheet=scoring_dataSheet;
    }

}
