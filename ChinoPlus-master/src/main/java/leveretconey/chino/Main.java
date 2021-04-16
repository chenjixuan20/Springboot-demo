package leveretconey.chino;

import java.util.ArrayList;
import java.util.List;

import leveretconey.chino.dataStructures.DataFrame;
import leveretconey.chino.dataStructures.ODCandidate;
import leveretconey.chino.dataStructures.ODTree;
import leveretconey.chino.discoverer.BFSODDiscovererFull;
import leveretconey.chino.discoverer.BFSODDiscovererFullFD;
import leveretconey.chino.discoverer.ChinoPlus;
import leveretconey.chino.sampler.OneLevelCheckingSampler;
import leveretconey.chino.util.Timer;
import leveretconey.chino.util.Util;
import leveretconey.chino.validator.ODPrefixBasedIncrementalValidator;
import leveretconey.chino.validator.ODPrefixBasedIncrementalValidatorFDMinimal;

class Main{
    public static void main(String[] args) {
//        DataFrame dataFrame=DataFrame.fromCsv("integer datasets/fd15-end.csv");
//        DataFrame dataFrame=DataFrame.fromCsv("integer datasets/ncvoter 1000 19.csv");
//        DataFrame dataFrame=DataFrame.fromCsv("integer datasets/DBTESMA 250000 30.csv");
//        DataFrame dataFrame=DataFrame.fromCsv("integer datasets/letter 20000 17.csv");
//        DataFrame dataFrame=DataFrame.fromCsv("integer datasets/flights 500000 17.csv");
//        DataFrame dataFrame=DataFrame.fromCsv("integer datasets/hepatitis-new.csv");
//        DataFrame dataFrame=DataFrame.fromCsv("integer datasets/flight_1k-new.csv");
//        DataFrame dataFrame=DataFrame.fromCsv("integer datasets/horse-new.csv");
        DataFrame dataFrame=DataFrame.fromCsv("integer datasets/test.csv");
//        DataFrame dataFrame=DataFrame.fromCsv("integer datasets/echocardiogram.csv");
        System.gc();
        Timer timer=new Timer();
        Util.out("原始版本");
        ODTree discover = new BFSODDiscovererFull().discover(dataFrame);
//        ODTree discover = new ChinoPlus(
//                new OneLevelCheckingSampler(),
//                new ODPrefixBasedIncrementalValidator(),
//                true).discover(dataFrame);
        List<ODCandidate> ods = discover.getAllOdsOrderByBFS();
        for (ODCandidate od : ods) {
            Util.out(od);
        }
        Util.out("最终时间:"+ timer.getTimeUsedAndReset()/1000.0 + "s");
        Util.out("发现ODs的数量："+ods.size());
        Util.out("--------------------------------------");


        System.gc();
        timer=new Timer();
        Util.out("用FD检查minimal list的版本");
        discover = new BFSODDiscovererFullFD().discover(dataFrame);
//        discover = new ChinoPlus(
//                new OneLevelCheckingSampler(),
//                new ODPrefixBasedIncrementalValidatorFDMinimal(),
//                true).discover(dataFrame);
        Util.out("最终时间"+ timer.getTimeUsedAndReset()/1000.0 + "s");
        ods = discover.getAllOdsOrderByBFS();
        for (ODCandidate od : ods) {
            Util.out(od);
        }
        Util.out("发现ODs的数量："+ods.size());


        discover = new BFSODDiscovererFull().discover(dataFrame);
    }
}
