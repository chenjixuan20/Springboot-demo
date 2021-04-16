package leveretconey.chino;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import leveretconey.chino.dataStructures.DataFrame;
import leveretconey.chino.dataStructures.ODCandidate;
import leveretconey.chino.dataStructures.ODTree;
import leveretconey.chino.discoverer.ChinoPlus;
import leveretconey.chino.sampler.OneLevelCheckingSampler;
import leveretconey.chino.util.Timer;
import leveretconey.chino.util.Util;
import leveretconey.chino.validator.ODPrefixBasedIncrementalValidator;
import leveretconey.chino.validator.ODPrefixBasedIncrementalValidatorFDMinimal;

class Main{
    public static void main(String[] args) throws IOException {

//        DataFrame dataFrame=DataFrame.fromCsv("integer datasets/DBTESMA 250000 30.csv");
//        DataFrame dataFrame=DataFrame.fromCsv("integer datasets/horse 300 28 v2.csv");
        DataFrame dataFrame=DataFrame.fromCsv("integer datasets/test.csv");
//        DataFrame dataFrame=DataFrame.fromCsv("integer datasets/letter 20000 17.csv");
//        DataFrame dataFrame=DataFrame.fromCsv("integer datasets/ncvoter 1000 19.csv");
//        DataFrame dataFrame=DataFrame.fromCsv("integer datasets/ncvoter 938071 22.csv");
//        DataFrame dataFrame=DataFrame.fromCsv("integer datasets/test.csv");
//        DataFrame dataFrame=DataFrame.fromCsv("integer datasets/flights 500000 17.csv");
//        DataFrame dataFrame=DataFrame.fromCsv("integer datasets/fd15-end.csv");
//        DataFrame dataFrame=DataFrame.fromCsv("integer datasets/letter-1.csv");
//        DataFrame dataFrame=DataFrame.fromCsv("integer datasets/letter.csv");
//        DataFrame dataFrame=DataFrame.fromCsv("integer datasets/echocardiogram done.csv");
        System.gc();
        Timer timer=new Timer();
        Util.out("原始版本");
        ODTree discover = new ChinoPlus(
                new OneLevelCheckingSampler(),
                new ODPrefixBasedIncrementalValidator(),
                true).discover(dataFrame);

//            File csv = new File("simtext.csv");//CSV文件
//            BufferedWriter bw = new BufferedWriter(new FileWriter(csv, true));
//            Integer i= (int) timer.getTimeUsed();
//            //新增一行数据
//            bw.newLine();
//            bw.write(i.toString());
//            bw.close();
        timer.outTimeAndReset();
        List<ODCandidate> ods = discover.getAllOdsOrderByBFS();
        Util.out("od.size:");
        Util.out(ods.size());
        Util.out("");


        System.gc();
        timer=new Timer();
        Util.out("用FD检查minimal list的版本");
        discover = new ChinoPlus(
                new OneLevelCheckingSampler(),
                new ODPrefixBasedIncrementalValidatorFDMinimal(),
                true).discover(dataFrame);
        timer.outTimeAndReset();
        ods = discover.getAllOdsOrderByBFS();
        Util.out("od.size");
        Util.out(ods.size());
        System.gc();
    }
}
