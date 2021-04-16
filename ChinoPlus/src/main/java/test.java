import java.io.*;
import java.util.*;

public class test {
    public static void main(String[] args) throws IOException {
        File csv = new File("bridges.csv");//CSV文件
        BufferedReader br = new BufferedReader(new FileReader(csv));
        String lineDta = "";
        List<List<String>> listList = new ArrayList<>();
//        String attr = br.readLine();

        while ((lineDta = br.readLine())!= null){
            List<String> list = Arrays.asList(lineDta.split(","));
            listList.add(list);
        }

        System.out.println("输入的文件：");
        for (int i = 0; i < listList.size(); i++) {
            System.out.println(listList.get(i));
        }

        System.out.println("属性个数：");
        int len = listList.get(0).size();
        System.out.println(listList.get(0).size());

        for(int i = 0; i < len; i++){
            System.out.println("处理第"+ i + "个元素：");
            int finalI = i;
            Collections.sort(listList,new Comparator<List<String>>() {
                @Override
                public int compare(List<String> o1, List<String> o2) {
                    List<String> list1 = new ArrayList<>(o1);
                    List<String> list2 = new ArrayList<>(o2);
                    return list1.get(finalI).compareTo(list2.get(finalI));
                }
            });

            System.out.println("-----------------------------------");
            System.out.println("排序后：");
            for (int k = 0; k < listList.size(); k++) {
                System.out.println(listList.get(k));
            }

            String pre = null;
            String now;

            for (int j = 0; j < listList.size(); j++) {
                now =listList.get(j).get(i);
                if (j == 0){
                    listList.get(j).set(i, Integer.toString(j));
                }else if(now.compareTo(pre) == 0) {
                    listList.get(j).set(i, listList.get(j-1).get(i));
                }else{
                    listList.get(j).set(i, Integer.toString(j));
                }
                pre = now;
            }

            System.out.println("-----------------------------------");
            System.out.println("替代后：");
            for (int k = 0; k < listList.size(); k++) {
                System.out.println(listList.get(k));
            }

        }






//        Collections.sort(listList, new Comparator<List<String>>() {
//            @Override
//            public int compare(List<String> o1, List<String> o2) {
//                List<String> list1 = new ArrayList<>(o1);
//                List<String> list2 = new ArrayList<>(o2);
//                return list1.get(0).compareTo(list2.get(0));
//            }
//        });





//        String pre = null;
//        String now;
//        for (int i = 0; i < listList.size() ; i++) {
//            now =listList.get(i).get(0);
//            if (i == 0) {
//                listList.get(i).set(i, Integer.toString(i));
//            }else if(now.compareTo(pre) == 0) {
//                listList.get(i).set(0, listList.get(i-1).get(0));
//            }else{
//                listList.get(i).set(0, Integer.toString(i));
//            }
//            pre = now;
//        }


        System.out.println("-----------------------------------");
        System.out.println("最终结果：");
        for (int i = 0; i < listList.size(); i++) {
            System.out.println(listList.get(i));
        }


        System.out.println("----------------------------------");
        System.out.println("写文件：");

        File csv2 = new File("bridges-new2.csv");//CSV文件
        BufferedWriter bw = new BufferedWriter(new FileWriter(csv2,true));


//        bw.newLine();
//        bw.write(attr);

        for(int i = 0; i < listList.size(); i++){
            String s0 = String.join(",",listList.get(i));
            bw.write(s0);
            bw.newLine();
        }
        bw.close();

        System.out.println("写文件结束");




    }
}
