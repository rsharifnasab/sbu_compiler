
public class Main {

    private static String getFileName(String[] args){
        if(args.length == 1) return args[0];
        System.err.println("please enter input filename");
        return new java.util.Scanner(System.in).next();
    }

    public static void main(String[] args) {
        System.err.println("welcome to this program!");
        String add = getFileName(args);
        System.out.println("your file was:" + add);
    } 
}
