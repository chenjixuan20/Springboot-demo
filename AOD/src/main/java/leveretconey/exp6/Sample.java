package leveretconey.exp6;

import leveretconey.ReturnData;
import leveretconey.chino.util.Timer;
import leveretconey.chino.util.Util;
import leveretconey.cocoa.multipleStandard.DFSDiscovererWithMultipleStandard;
import leveretconey.cocoa.sample.SubsetSampleALODDiscoverer;
import leveretconey.dependencyDiscover.Data.DataFrame;
import leveretconey.dependencyDiscover.Dependency.LexicographicalOrderDependency;
import leveretconey.dependencyDiscover.Discoverer.ALODDiscoverer;
import leveretconey.dependencyDiscover.SortedPartition.ImprovedTwoSideSortedPartition;
import leveretconey.dependencyDiscover.Validator.Result.ValidationResultWithAccurateBound;

import java.math.BigDecimal;
import java.util.*;

import static leveretconey.cocoa.multipleStandard.DFSDiscovererWithMultipleStandard.ValidatorType.G1;

public class Sample {

    public static boolean isEqu(DataFrame data, List<AttributeAndDirection> attrList, int first, int second){
        for(int i = 0; i < attrList.size(); i++){
            int col = attrList.get(i).attr - 1;
            if(data.get(first,col) == data.get(second,col)){
                continue;
            }else {
                return false;
            }
        }
        return true;
    }

    public static boolean isLess(DataFrame data, List<AttributeAndDirection> attrList, int first, int second){
        for(int i = 0; i < attrList.size(); i++){
            int col = attrList.get(i).attr - 1;
            int dir = attrList.get(i).dir;
            if(dir == AttributeAndDirection.UP){
                if(data.get(first,col) < data.get(second,col)){
                    return true;
                }else if(data.get(first,col) > data.get(second,col)){
                    return false;
                }else {
                    continue;
                }
            }else {
                if(data.get(first,col) > data.get(second,col)){
                    return true;
                }else if(data.get(first,col) < data.get(second,col)){
                    return false;
                }else {
                    continue;
                }
            }
        }
        return false;
    }

    public static boolean isSplit(DataFrame data, List<AttributeAndDirection> left, List<AttributeAndDirection> right,
                                  int first, int second){
        if(isEqu(data,left,first,second) && !isEqu(data,right,first,second)){
            return true;
        }
        return false;
    }

    public static boolean isSwap(DataFrame data, List<AttributeAndDirection> left, List<AttributeAndDirection> right,
                                 int first, int second){
        if(isLess(data,left,first,second) && isMore(data,right,first,second)){
            return true;
        }
        if(isMore(data,left,first,second) && isLess(data,right,first,second)){
            return true;
        }
        return false;
    }

    public static boolean isMore(DataFrame data, List<AttributeAndDirection> attrList, int first, int second){
        for(int i = 0; i < attrList.size(); i++){
            int col = attrList.get(i).attr - 1;
            int dir = attrList.get(i).dir;
            if(dir == AttributeAndDirection.UP){
                if(data.get(first,col) > data.get(second,col)){
                    return true;
                }else if(data.get(first,col) < data.get(second,col)){
                    return false;
                }else {
                    continue;
                }
            }else {
                if(data.get(first,col) < data.get(second,col)){
                    return true;
                }else if(data.get(first,col) > data.get(second,col)){
                    return false;
                }else {
                    continue;
                }
            }

        }
        return false;
    }

    public static void main(String[] args) {
//        DataFrame data= DataFrame.fromCsv("data/exp3/data/varying_R/14/FLI 300 12.csv");
        DataFrame data= DataFrame.fromCsv("data/exp6/FLI 100K.csv");
//        DataFrame data= DataFrame.fromCsv("data/exp6/test.csv");

        System.out.println("data.size: " + data.getTupleCount());

        Map<MapKey, Double> aodMap = new HashMap<>();
        System.out.println("原始算法： ");
        ALODDiscoverer discoverer =new DFSDiscovererWithMultipleStandard(G1,0.001);
        //对于DFSDiscovererWithMultipleStandard这个类，它实际使用的error rate以上面这行为准，下面这个是没用的（接口太烂）
        Collection<LexicographicalOrderDependency> aods = discoverer.newDiscover(data,0.001).result;
        System.out.println(aods.toString());

        for (LexicographicalOrderDependency aod : aods) {
            LexicographicalOrderDependency od = LexicographicalOrderDependency.fromString(aod.toString());
            ImprovedTwoSideSortedPartition isp = new ImprovedTwoSideSortedPartition(data,od);
            ValidationResultWithAccurateBound er = isp.validateForALODWithG1();
            aodMap.put(new MapKey(aod.left, aod.right), er.errorRate);
        }

        Timer timer = new Timer();
        SubsetSampleALODDiscoverer discoverer1 = new SubsetSampleALODDiscoverer(0.02,0.001,0.1);
        List<List<Integer>> pairs = discoverer1.newDiscoverPlus(data,0.001);
        System.out.println("sample time: "+timer.getTimeUsedAndReset()/1000.0+"s");
//        System.out.println(pairs);

        Set<Set<Integer>> sets = discoverer1.newDiscoverPlusSet(data,0.001);
        System.out.println("sample time set: "+timer.getTimeUsedAndReset()/1000.0+"s");



        int count = 0;

        Set<MapKey> set = aodMap.keySet();
        for(MapKey key : set){
            List<AttributeAndDirection> leftList = key.left;
            List<AttributeAndDirection> rightList = key.right;
            int split  = 0;
            int swap = 0;
//            for(int i = 0; i < pairs.size(); i++){
//                List<Integer> now = pairs.get(i);
//                int first = now.get(0);
//                int second = now.get(1);
////            前等后不等，split
//                if(isSplit(data,leftList,rightList,first,second)){
//                    split++;
//                }
////            前小后大，前大后小，swap
//                if(isSwap(data,leftList,rightList,first,second)){
//                    swap++;
//                }
//            }
            for(Set<Integer> s : sets){
                Iterator<Integer> it = s.iterator();
                int first = it.next();
                int second = it.next();
//            前等后不等，split
                if(isSplit(data,leftList,rightList,first,second)){
                    split++;
                }
//            前小后大，前大后小，swap
                if(isSwap(data,leftList,rightList,first,second)){
                    swap++;
                }
            }

            System.out.println("-----------");
            System.out.println(key.toString());
            System.out.println("split: " + split);
            System.out.println("swap: " + swap);
            double g1Sample =(double) (split + swap) / pairs.size();
            double g1 = aodMap.get(key);
            System.out.println("g1: " + g1);
            System.out.println("g1Sample: " + g1Sample);
            if(Math.abs(g1-g1Sample) <= 0.001) count++;
            System.out.println("count: "+count);
            System.out.println("---------");
        }

        int aod_size = aods.size();

        double rate = (double)count/aod_size;

        System.out.println("time:"+timer.getTimeUsedAndReset()/1000.0+"s");
        System.out.println("rate:" + rate);
    }


//    public static void main(String[] args) {
//        DataFrame data = DataFrame.fromCsv("data/exp6/test.csv");
//        List<Integer> left = new ArrayList<>();
//        List<Integer> right = new ArrayList<>();
//        left.add(1);
//        left.add(2);
//
//        right.add(3);
//        right.add(4);
//        System.out.println("是否前小:");
//        System.out.println(isLess(data,left,0,1));
//        System.out.println("是否后大:");
//        System.out.println(isMore(data,right,0,1));
//        System.out.println("是否SWAP:");
//        System.out.println(isLess(data,left,0,1) && isMore(data,right,0,1));
//
//        System.out.println("是否前等:");
//        System.out.println(isEqu(data,left,0,1));
//        System.out.println("是否后等:");
//        System.out.println(isEqu(data,right,0,1));
//        System.out.println("是否SPLIT:");
//        System.out.println(isEqu(data,left,0,1) && !isEqu(data,right,0,1));
//
//
//
//
//    }
}
