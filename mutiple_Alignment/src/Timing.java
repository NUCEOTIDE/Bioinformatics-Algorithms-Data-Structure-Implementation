import java.util.*;

public class Timing {
    public static long getTime_interval(){
        Calendar s=Calendar.getInstance();
        long time=s.getTimeInMillis();
        return time;
    }
    public static void interval_calculation(long a,long b){
        System.out.println(b-a);
    }
}
