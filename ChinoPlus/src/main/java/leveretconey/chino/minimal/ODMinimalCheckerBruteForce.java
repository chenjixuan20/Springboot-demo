package leveretconey.chino.minimal;

import java.util.ArrayList;
import java.util.List;

import leveretconey.chino.dataStructures.AttributeAndDirection;
import leveretconey.chino.dataStructures.ODByLeftRightAttributeList;
import leveretconey.chino.dataStructures.ODCandidate;
import leveretconey.chino.util.Util;

public class ODMinimalCheckerBruteForce extends ODMinimalChecker{
    private List<ODCandidate> ods;

    public ODMinimalCheckerBruteForce() {
        ods=new ArrayList<>();
    }

    @Override
    public void insert(ODCandidate candidate) {
        ods.add(candidate);
    }

    /**
     * 原始版本
     * @param list
     * @return
     */
    @Override
    protected boolean isListMinimal(List<AttributeAndDirection> list) {
        //遍历ODCandidate中的每个od
        for (ODCandidate od : ods) {
            List<AttributeAndDirection> left=od.odByLeftRightAttributeList.left;
            List<AttributeAndDirection> right=od.odByLeftRightAttributeList.right;
            //找left和right在list中的位置
            int leftIndex=getIndex(list,left),rightIndex=getIndex(list,right);
            //leftIndex,rightIndex不小于-1，则表明在list中找到了left和right。且当left在right左侧，和left是right相接右侧时改list为非最小list
            if(leftIndex!=-1 && rightIndex != -1 &&
                    (leftIndex<rightIndex ||rightIndex+right.size()==leftIndex)){
                return false;
            }
            //排序方向反向的情况
            list=reverseDirection(list);
            leftIndex=getIndex(list,left);
            rightIndex=getIndex(list,right);
            if(leftIndex!=-1 && rightIndex != -1 &&
                    (leftIndex<rightIndex ||rightIndex+right.size()==leftIndex)){
                return false;
            }
        }
        return true;
    }

    /**
     * 从context中找到pattern的位置，并返回
     * @param context
     * @param pattern
     * @return pattern在context中的位置
     */
    private int getIndex(List<AttributeAndDirection> context,List<AttributeAndDirection> pattern){
        //若context大小小于pattern，则肯定不包含pattern
        if(context.size()<pattern.size())
            return -1;
        //遍历的终点
        int end=context.size()-pattern.size();
        //从context的搜索空间每个位置开始找pattern，若能找到则返回i。
        for (int i = 0; i <= end; i++) {
            if(exactMatch(context,pattern,i))
                return i;
        }
        return -1;
    }
}
