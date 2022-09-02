import java.lang.Integer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.TreeSet;

class SolutionPI {
    String[] a={};
    String[] tmpArr;
    HashSet<String> v;
    static TreeSet<String> ans = new TreeSet<>();
    int[] check;


    public String[] solution(int[] minterm) {

        int bit = minterm[0]; // CHECK bits
        int vars = minterm[1]; // CHECK minterms

        a = new String[vars];

        for(int i=0; i< vars; i++){ // minterm 개수만큼 for문 돌리면서 10진수(int)->2진수(string)로 변환
            String intToBinary = Integer.toBinaryString(minterm[i+2]);

            if(bit == intToBinary.length()){
                a[i] = intToBinary;
            }else{
                int zeros = bit - intToBinary.length(); // 더 필요한 0의 개수
                String add_zeros= "";
                for(int j=0; j<zeros; j++)
                    add_zeros += "0";
                a[i] = add_zeros+ intToBinary; //.repeat() 자바8에서 안되서 for 로;
            }
        }

        do{
            tmpArr = a.clone();
            a = findPi(a);
        }while(!(Arrays.equals(tmpArr,a))); // 더이상 size 줄어들지 않을 때 까지

        return ans.stream().map(v -> v.replaceAll("2", "-")).toArray(String[]::new);
    }

    private String[] findPi(String[] arr){
        check = new int[arr.length]; // 이렇게 선언하면 int 타입의 경우 0으로 초기화
        v = new HashSet<>();

        for(int i=0;i<arr.length-1;i++){
            for(int j=i+1;j<arr.length;j++){
                String c = combine(arr[i],arr[j]);
                if (c != null){
                    v.add(c);
                    check[i] = 1; check[j] =1;
                }
            }
        }

        for(int i=0;i<arr.length;i++){
            if(check[i] == 0){
                ans.add(arr[i]);
            }
        }

        return v.toArray(new String[0]);
    }

    private String combine(String a, String b){
        int l = a.length();
        String c = "";
        int hd = 0;
        for(int i=0;i<l;i++){
            if(a.charAt(i) == b.charAt(i)){
                c += a.charAt(i);
            }else{
                c += "2";
                hd++;
            }
        }
        if(hd>1){
            return null;
        }else{
            return c;
        }
    }

}

public class PI {
    public static void main(String[] args) {
        SolutionPI solution= new SolutionPI();
        System.out.println(Arrays.toString(solution.solution(new int[]{3, 6, 0, 1, 2, 5, 6, 7})));
        // 3, 6, 0, 1, 2, 5, 6, 7 -> [00-, 0-0, 11-, 1-1, -01, -10]
        // 3, 4, 0, 1, 2, 3
        // 4,16,0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15 -> ----
        // 4, 8, 0, 4, 8, 10, 11, 12, 13, 15 -> 101-, 10-0, 110-, 11-1, 1-11, --00
    }
}
