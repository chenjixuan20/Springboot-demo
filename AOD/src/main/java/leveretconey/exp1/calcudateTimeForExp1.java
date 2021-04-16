package leveretconey.exp1;

import leveretconey.cocoa.multipleStandard.DFSDiscovererWithMultipleStandard;
import leveretconey.cocoa.sample.SubsetSampleALODDiscoverer;
import leveretconey.dependencyDiscover.Data.DataFrame;
import leveretconey.dependencyDiscover.Dependency.LexicographicalOrderDependency;
import leveretconey.dependencyDiscover.Discoverer.ALODDiscoverer;


import java.util.Collection;

import static leveretconey.cocoa.multipleStandard.DFSDiscovererWithMultipleStandard.ValidatorType.*;


public class calcudateTimeForExp1 {
    public static void main(String[] args) {
//        DataFrame data = DataFrame.fromCsv("data/exp1/ATOM/Atom 10.csv");
//        DataFrame data = DataFrame.fromCsv("data/exp1/DB/DB 13.csv");
//        DataFrame data = DataFrame.fromCsv("data/exp1/ATOM/Atom 24.csv");
//        DataFrame data = DataFrame.fromCsv("data/exp1/FLI/FLI 14.csv");
//        DataFrame data = DataFrame.fromCsv("data/exp1/NCV/NCV 15.csv");
        DataFrame data = DataFrame.fromCsv("data/exp1/fd 30.csv");

        // 原始的发现算法调用
        //G1
        System.gc();
        ALODDiscoverer discoverer =new DFSDiscovererWithMultipleStandard(G1,0.001);
        //对于DFSDiscovererWithMultipleStandard这个类，它实际使用的error rate以上面这行为准，下面这个是没用的（接口太烂）
        Collection<LexicographicalOrderDependency> c1 = discoverer.discover(data, 0.001);
//        System.out.println(c1.toString());

        //G3
        System.gc();
        discoverer =new DFSDiscovererWithMultipleStandard(G3,0.01);
        c1 = discoverer.discover(data, 0.01);
//        System.out.println(c1.toString());
    }


}
