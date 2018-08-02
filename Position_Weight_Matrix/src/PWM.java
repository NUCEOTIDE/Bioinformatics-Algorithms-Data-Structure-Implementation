public class PWM {
    private String targetSeq;
    private String[] dataBase;
    private int[][] positionCount_matrix;
    private float[][] positionWeight_matrix;
    private float[] target_score;

    private String syllabus;
    private String output;

    private float e;

    public PWM(){
        targetSeq="ACCCCAGTTTACATTACTACCAGCTAAAA";

        dataBase=new String[8];
        dataBase[0]="ACGTACGTT";
        dataBase[1]="ACTTACGGT";
        dataBase[2]="ACCCAGGGT";
        dataBase[3]="ACCACGTTT";
        dataBase[4]="ACCACCGGT";
        dataBase[5]="ACACACAGT";
        dataBase[6]="ACCAGCAAA";
        dataBase[7]="AGGGTTTAT";

        syllabus ="ACTG";
        output="";

        e=(float)0.001;

        positionCount_matrix=new int[syllabus.length()][dataBase[0].length()];
        positionWeight_matrix=new float[syllabus.length()][dataBase[0].length()];
        target_score=new float[targetSeq.length()];
    }
    public void calculation(){
        positionCount_matrix_generation();
        positionWeight_matrix_generation();
        matching();
        answer_outputting();
    }

    private void positionCount_matrix_generation(){
        for(int i = 0; i< syllabus.length(); i++){
            for(int j=0;j<dataBase[0].length();j++){
                int count=0;
                for(int k=0;k<dataBase.length;k++){
                    char temp_syllabuts=dataBase[k].charAt(j);
                    if(temp_syllabuts== syllabus.charAt(i))
                        count++;
                }
                positionCount_matrix[i][j]=count;
            }
        }

    }
    private void positionWeight_matrix_generation(){
        for(int i=0;i<positionCount_matrix.length;i++)
            for(int j=0;j<positionCount_matrix[0].length;j++)
                //System.out.println(positionCount_matrix[i][j]+"/"+dataBase.length);
                positionWeight_matrix[i][j]=(float)positionCount_matrix[i][j]/(dataBase.length)+e;
    }
    private void matching(){
        for(int i=0;i<targetSeq.length()-9;i++){
            float count=0;
            for(int k=0;k<syllabus.length();k++)
                if(syllabus.charAt(k)==targetSeq.charAt(i)) count+=positionWeight_matrix[k][0];
            for(int j=i+1;;j++){
                if(j<targetSeq.length()&&j<i+dataBase[0].length()){
                    char target_character=targetSeq.charAt(j);
                    for(int k=0;k<syllabus.length();k++)
                        if(syllabus.charAt(k)==target_character) count*=positionWeight_matrix[k][j-i];
                }
                else break;
            }
            target_score[i]=count;
        }
    }

    private void answer_outputting(){
        float temp=target_score[0];
        int index=0;
        for(int i=0;i<targetSeq.length();i++){
            System.out.print(target_score[i]+" ");
            if(target_score[i]>temp){
                index=i;
                temp=target_score[i];
            }
        }
        System.out.println();
        int count=0;
        for(int j=index;j<targetSeq.length();j++){
            if(count==8) break;
            output+=targetSeq.substring(j,j+1);
            count++;
        }
        System.out.println("start point: "+index);
        System.out.println("answer string: "+output);
        System.out.println("score: "+target_score[index]);
    }

    public void getPositonWeight_matrix(){
        for(int i=0;i<positionWeight_matrix.length;i++){
            for(int j=0;j<positionWeight_matrix[0].length;j++)
                System.out.print(positionWeight_matrix[i][j]+" ");
            System.out.println();
        }
    }

    public void getPositionCount_matrix(){
        for(int i=0;i<positionCount_matrix.length;i++){
            for(int j=0;j<positionCount_matrix[0].length;j++)
                System.out.print(positionCount_matrix[i][j]+" ");
            System.out.println();
        }
    }
}
