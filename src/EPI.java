import java.lang.Integer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeSet;
import static java.util.stream.Collectors.toCollection;

class SolutionEPI {
    //ArrayList<String> PI;
    ArrayList<String> PIs = new ArrayList<>(), EPIs = new ArrayList<>(); // 정답용 pi랑 epi 모은 arraylist
    ArrayList<String> ans = new ArrayList<>(); String[] EPI; // 정답'제출'용
    String[] toB={}; String[] b={};
    String[] tmp;
    int bit, vars;


    public String[] solution(int[] minterm) {

        // bit vars 띄고 only minterms 만 binary 로 변환
        toB = toBinary(minterm);

        b = toB.clone(); // 원본은 나중에 쓸일 있어서 복사. * b=toB 는 swallow copy 임
        do{
            tmp = b.clone();
            b = findPI(b).clone();
        }while(!Arrays.equals(tmp,b));

        TreeSet<String> ts = new TreeSet<>(PIs);
//        PI = ts.stream().map(v -> v.replaceAll("2", "-")).collect(toCollection(ArrayList::new));
//
//        findEPI(toB, PI);
//
//        TreeSet<String> epi = EPIs.stream().map(v -> v.replaceAll("-", "2")).collect(toCollection(TreeSet::new));
//        EPIs = epi.stream().map(v -> v.replaceAll("2", "-")).collect(toCollection(ArrayList::new));
//
//        ans.addAll(PI); ans.add("EPI"); ans.addAll(EPIs); // 초기화 안해주면 .add 할 때 nullpointer exception 남
//        EPI = ans.toArray(new String[0]);
        PIs = ts.stream().map(v -> v.replaceAll("2", "-")).collect(toCollection(ArrayList::new)); // 길이 변화가 없어서 가능했었던거 일듯

        findEPI(toB, PIs);

        TreeSet<String> epi = EPIs.stream().map(v -> v.replaceAll("-", "2")).collect(toCollection(TreeSet::new));
        EPIs = epi.stream().map(v -> v.replaceAll("2", "-")).collect(toCollection(ArrayList::new));

        ans.addAll(PIs); ans.add("EPI"); ans.addAll(EPIs); // 초기화 안해주면 .add 할 때 nullpointer exception 남
        EPI = ans.toArray(new String[0]);

        return EPI;
    }

    private void findEPI(String[] binaries, ArrayList<String> pis){
        String[] a = pis.toArray(new String[0]);
        int[] check2; int ch;
        Loop1 :
        for(int i=0;i<binaries.length;i++){ // minterm
            ch =0; check2 = new int[a.length];
            Loop2 :
            for(int j=0;j<a.length;j++) { // PIs
                for (int k = 0; k < bit; k++) { // bit 대조
                    if (binaries[i].charAt(k) != a[j].charAt(k)) { // 각 비트가 다를때
                        if (Character.isDigit(binaries[i].charAt(k)) && Character.isDigit(a[j].charAt(k)))
                             continue Loop2;
                    }
                }
                check2[j] = 1; ++ch;
                if(ch>1) continue Loop1;
            }
            if(ch==1){
                int getIdx = 0;
                getIdx = Arrays.binarySearch(check2,1);
                EPIs.add(a[getIdx]);
            }
        }
    }

    private String[] findPI(String[] arr){
        int[] check = new int[arr.length];
        ArrayList<String> v = new ArrayList<>();
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
                PIs.add(arr[i]);
            }
        }

        TreeSet<String> ts = new TreeSet<>(v);
        //String[] show = ts.toArray(new String[0]);
        return ts.stream().toArray(String[]::new);
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

    private String[] toBinary(int[] arr){
        bit = arr[0]; // 몇 비트인지 구하기
        vars = arr[1]; // minterm 몇개인지 = answer.length();

        String[] a = new String[vars];
        for(int i=2;i<arr.length;i++){  // minterm 개수만큼 for문 돌리면서 10진수(int)->2진수(string)로 변환
            a[i-2] = Integer.toBinaryString(arr[i]); // 한거 toB 배열에 넣어주기
        }

        for(int i=0; i<vars;i++){ // 변환한 2진수-> 비트수에 맞게 조정
            if(bit!=a[i].length()) {
                int cnt0 = bit - a[i].length(); // 더 필요한 0의 개수

                String add0s= "";
                for(int j=0;j<cnt0;j++)
                    add0s += "0";
                a[i] = add0s+ a[i]; // .repeat() <-- 자바8에서 안되서 for 로 돌려야댐;;
            }
        }
        return a;
    }
}

public class EPI {
    public static void main(String[] args) {
        SolutionEPI solution= new SolutionEPI();
        System.out.println(Arrays.toString(solution.solution(new int[]{4, 8, 0, 4, 8, 10, 11, 13, 15})));
        // 3, 6, 0, 1, 2, 5, 6, 7
        // 3, 4, 0, 1, 2, 3
        // 4, 8, 0, 4, 8, 10, 11, 13, 15
        // 5, 9, 0,2,5,6,7,8,9,13,20
        // 5, 17, 0,2,4,5,6,7,8,10,11,14,17,18,20,21,22,29,31
    }
}
