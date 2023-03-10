import java.lang.Integer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeSet;

class Solution {
    String[] answer = {};
    String[] a={};
    String[] tmpArr;
    ArrayList<String> v;
    static ArrayList<String> ans = new ArrayList<>();
    int[] check;


    public String[] solution(int[] minterm) { // 배열 입력받아

        int bit = minterm[0]; // 몇 비트인지 구하기
        int vars = minterm[1]; // minterm 몇개인지 = answer.length();

        a = new String[vars];

        for(int i=2;i<minterm.length;i++){  // minterm 개수만큼 for문 돌리면서 10진수(int)->2진수(string)로 변환
            a[i-2] = Integer.toBinaryString(minterm[i]); // 한거 a 배열에 넣어주기
        }

        for(int i=0; i<vars;i++){ // 변환한 2진수 비트수에 맞게 조정
            if(bit!=a[i].length()) {
                int cnt0 = bit - a[i].length(); // 더 필요한 0의 개수
                String add0s= "";
                for(int j=0;j<cnt0;j++)
                    add0s += "0";
                a[i] = add0s+ a[i]; //.repeat() 자바8에서 안되서 for 로
            }
        }

        do{
            tmpArr = a;
            a = findPi(a);
        }while(!(Arrays.equals(tmpArr,a))); // 더이상 size 줄어들지 않을 때 까지

            TreeSet<String> ts = new TreeSet<>(ans);
//        answer = ts.toArray(new String[0]);
//
//        for(int i=0;i<answer.length;i++){
//            answer[i] = answer[i].replace("2","-");
//        }

        return ts.stream().map(v -> v.replaceAll("2", "-")).toArray(String[]::new);
    }

    private String[] findPi(String[] arr){
        check = new int[arr.length]; // 이렇게 선언하면 int 타입의 경우 0으로 초기화
        v = new ArrayList<>();
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

        TreeSet<String> ts = new TreeSet<String>(v);
        String[] show = ts.toArray(new String[0]);
        return show;
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
        Solution solution= new Solution();
        System.out.println(solution.solution(new int[]{3, 4, 0, 1, 2, 3}));
        // 3, 6, 0, 1, 2, 5, 6, 7
    }
}