public class recursion {
    public static void main(String args[]){
        recursion a=new recursion();
        System.out.println(a.fab(5));
        String b="-1";
        System.out.println(Integer.parseInt(b));
    }

    private int fab(int index){
        System.out.println("recursion");
        if(index==1 || index==2){
            return 1;
        }else{
            return fab(index-1)+fab(index-2);
        }
    }
}
