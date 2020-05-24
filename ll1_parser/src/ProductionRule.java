public class ProductionRule {
    public final int num;
    String [] rule;

    public ProductionRule(int num){

        this.num = num;
        this.rule = new String[2];
    }


    @Override
    public String toString(){ return ""+num; }

}
