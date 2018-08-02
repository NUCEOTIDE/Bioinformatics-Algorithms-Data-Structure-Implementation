public class Main {
    public static void main(String[] args){
        Magaret_Dayhoff_socringMatrix homework=new Magaret_Dayhoff_socringMatrix(4);
        homework.scoringMatrix_calculation();
        homework.get("syllabus");
        homework.get("source");
        homework.get("PresFreq");
        homework.get("ExpFreq");
        homework.get("ObsFreq");
        homework.get("likelihood");
        //String a="A B C";
        //System.out.println(a.replaceAll(" ",""));
    }
}
