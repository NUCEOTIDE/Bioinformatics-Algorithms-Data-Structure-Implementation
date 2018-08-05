public class Nth_dimentionPoint {
    private int[] coordination;  //点的坐标
    private int dimension;  //坐标维数
    private float data;  //保存每个普通点上的数据

    public Nth_dimentionPoint(int new_dimension,int[] new_coordination, float new_data){
        coordination=new int[new_dimension];
        data=new_data;
    }

    public void setCoordination(int[] new_coordination){
        //coordination=new_coordination;
        for(int i=0;i<new_coordination.length;i++)
            coordination[i]=new_coordination[i];
    }
    public void setData(float new_data){
        data=new_data;
    }

    //get methods
    public float getData() {
        return this.data;
    }
    public int getDimension(){
        return this.dimension;
    }
    public int[] getCoordination() {
        return this.coordination;
    }
}
