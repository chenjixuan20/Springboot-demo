package leveretconey.exp2;

import leveretconey.cocoa.multipleStandard.DFSDiscovererWithMultipleStandard;
import leveretconey.dependencyDiscover.Data.DataFrame;
import leveretconey.dependencyDiscover.Dependency.LexicographicalOrderDependency;
import leveretconey.dependencyDiscover.Discoverer.ALODDiscoverer;
import leveretconey.dependencyDiscover.SortedPartition.ImprovedTwoSideSortedPartition;
import leveretconey.dependencyDiscover.Validator.Result.ValidationResultWithAccurateBound;
import leveretconey.util.TimeStatistics;
import leveretconey.util.Util;

import java.util.Collection;

import static leveretconey.cocoa.multipleStandard.DFSDiscovererWithMultipleStandard.ValidatorType.G1;
import static leveretconey.cocoa.multipleStandard.DFSDiscovererWithMultipleStandard.ValidatorType.G3;

public class AOD1 {
    public static void main(String[] args) {
        DataFrame data = DataFrame.fromCsv("data/exp3/data/varying_e/FLI 300K 14.csv");
//        DataFrame data = DataFrame.fromCsv("data/exp1/NCV/NCV 18.csv");


        System.gc();
        TimeStatistics.reset();
//         原始的发现算法调用
        ALODDiscoverer discoverer =new DFSDiscovererWithMultipleStandard(G1,0.0030);
//        对于DFSDiscovererWithMultipleStandard这个类，它实际使用的error rate以上面这行为准，下面这个是没用的（接口太烂）
        discoverer.discover(data, 0.001);
        TimeStatistics.printStatistics();


    }
}
