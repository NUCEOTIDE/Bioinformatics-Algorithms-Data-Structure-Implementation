public class KMP {
    private int[] next;
    private String pattern,text;
    private int index;

    public KMP(){
        pattern="aabbaaabbb";
        text="aabbaabbaaabbaabbaaabbb";
        next=new int[pattern.length()];
    }

    public void calculation(){
        nextMatrix_generation();
        matching();
        answer_outputting();
    }

    private void nextMatrix_generation(){
        for(int i=0;i<next.length;i++){
            int count=0;
            String subPattern=pattern.substring(0,i+1);
            for(int k=0,j=i;k<pattern.length()-1;k++,j--){
                if(j<0||k==i) break;
                String temp_subPattern=subPattern.substring(j);
                if(temp_subPattern.equals(pattern.substring(0,k+1)))
                    count=temp_subPattern.length();
            }
            next[i]=count;
        }
    }
    private void matching(){
        System.out.println("starting position: ");
        System.out.println(pattern);
        System.out.println(text);
        index=-1;
        int failure_count=0;
        for(int i=0,k=0;i<pattern.length();){
            if(pattern.charAt(i)==text.charAt(k)){
                if(i==pattern.length()-1){
                    index=k-pattern.length();
                    break;
                }else if(k==text.length()) break;
                i++;
                k++;
            }
            else{
                failure_count++;
                System.out.println("failure number:"+failure_count);
                for(int m=0;m<k-i;m++)
                    System.out.print(" ");
                System.out.println(pattern.substring(0,i+1));
                System.out.println(text);
                k++;
                i=next[i]+1;
            }

        }
    }
    private void answer_outputting(){
        System.out.println("answer index: "+index);
        System.out.println("answer: ");
        for(int i=0;i<=index;i++)
            System.out.print(" ");
        System.out.println(pattern);
        System.out.println(text);
    }

    public void getNext(){
        System.out.print("failure function value: ");
        for(int i=0;i<next.length;i++)
            System.out.print(next[i]);
        System.out.println();
    }
}
