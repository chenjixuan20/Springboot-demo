package leveretconey.chino.dataStructures;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import leveretconey.chino.sampler.DataAndIndex;

public class EquivalenceClass{
    /**
     * 实际的sort partition为[[1,2],[3,4],[5,6]]
     * indexes = [1,2,3,4,5,6]
     * begins = [0,2,4]
     */
    public int[] indexes;
    //记录每个簇的起始位置
    public List<Integer> begins;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ODTreeNodeEquivalenceClasses)) return false;
        EquivalenceClass that = (EquivalenceClass) o;
        return this.begins == that.begins || this.begins.size() == that.begins.size();
    }

    private final static Comparator<DataAndIndex> reverseComparator=(i1, i2)->i2.data-i1.data;
    private final static Comparator<DataAndIndex> normalComparator=(i1,i2)->i1.data-i2.data;

    public EquivalenceClass() {
    }

    //是否已经初始化，indexes 不为空时，返回1，表示已经初始化
    public boolean initialized(){
        return indexes!=null;
    }

    //初始化indexes[],begins
    private void initialize(DataFrame data){
        int countRow=data.getRowCount();
        indexes=new int[countRow];
        for (int i = 0; i < countRow; i++) {
            indexes[i]=i;
        }
        //初始化begins
        begins=new ArrayList<>();
        begins.add(0);
        begins.add(countRow);
    }

    //对EquivalenceClass对象，在data数据中，合并attributeAndDirection中属性，得到合并后的sort partition
    public EquivalenceClass merge(DataFrame data,AttributeAndDirection attributeAndDirection){
        //lazy update
        if(!initialized()){
            initialize(data);
        }

        //记录attributeAndDirection中的属性是第几列，因为使用list<list<int>>记录
        int column=attributeAndDirection.attribute;

        Comparator<DataAndIndex> comparator=attributeAndDirection.direction==AttributeAndDirection.UP
                ?normalComparator:reverseComparator;

        /**
         * 若sort patition = [[1],[2],[3],[4],[5]]
         * indexs = [1,2,3,4,5]
         * begins = [0,1,2,3,4,5]
         * 则 begins.size == data.getRowCount()+1，表明没有值相等的情况
         */
        if(begins.size()==data.getRowCount()+1)
            return this;
        List<Integer> newBegins=new ArrayList<>();
        for(int beginPointer=0;beginPointer<begins.size()-1;beginPointer++) {
            //一个簇的开头位置
            int groupBegin = begins.get(beginPointer);
            //一个簇的结尾位置
            int groupEnd = begins.get(beginPointer + 1);
            /**
             * 若簇的大小为1，则不进行操作，直接加入newBegins
             * merge过程只需要对size大于1的簇进行拆分操作
             */
            if(groupBegin == groupEnd-1){
                newBegins.add(groupBegin);
                continue;
            }
            int value=0;
            try {
                //记录对象中，size大于1的簇的第一个元组对于属性column的值
                value=data.get(indexes[groupBegin],column);
            } catch (Exception e) {
                e.printStackTrace();
            }
            boolean same=true;
            List<DataAndIndex> mergeData=new ArrayList<>();
            for(int i=groupBegin;i<groupEnd;i++){
                int row=indexes[i];
                int rowValue=data.get(row,column);
                if(rowValue!=value){
                    same=false;
                }
                //记录按照于对象中顺序排序的元组，对于column的值
                mergeData.add(new DataAndIndex(rowValue,row));
            }
            //如果对象的一个簇中所有元组映射在column属性中的值都相同，则可以将这个簇的加入newBegins
            //记录groupBegin，那么怎么记录groupEnd呢？
            if(same){
                newBegins.add(groupBegin);
                continue;
            }
            mergeData.sort(comparator);
            int fillPointer=groupBegin;
            for (int i = 0; i < mergeData.size(); i++) {
                if(i==0 || mergeData.get(i-1).data!=mergeData.get(i).data){
                    newBegins.add(fillPointer);
                }
                indexes[fillPointer]=mergeData.get(i).index;
                fillPointer++;
            }
        }
        //遍历完后
        begins=newBegins;
        begins.add(data.getRowCount());
        return this;
    }


    @Override
    public String toString() {
        return "EquivalenceClass{" +
                "indexes=" + Arrays.toString(indexes) +
                ", begins=" + begins +
                '}';
    }

    //复制一个对象
    public EquivalenceClass deepClone(){
        EquivalenceClass result=new EquivalenceClass();
        if(initialized()) {
            result.indexes = Arrays.copyOf(indexes, indexes.length);
            result.begins = new ArrayList<>(begins);
        }
        return result;
    }
}
