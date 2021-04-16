package leveretconey.chino.minimal;

import java.util.ArrayList;
import java.util.List;

import leveretconey.chino.dataStructures.AttributeAndDirection;
import leveretconey.chino.dataStructures.ODCandidate;
import leveretconey.chino.dataStructures.ODTree;
import leveretconey.chino.util.Timer;


//抽象类
public abstract class ODMinimalChecker {
    public static long minimalCheckTime=0;
    public abstract void insert(ODCandidate candidate);
    protected abstract boolean isListMinimal(List<AttributeAndDirection> list);

    /**
     * 检查该ODCandidate是不是Minimal
     * 存疑
     * @param candidate
     * @return
     */
    public boolean isCandidateMinimal(ODCandidate candidate){
        Timer timer=new Timer();
        boolean result;
        List<AttributeAndDirection> expandSide,otherSide;
        if(candidate.odByODTreeNode.parent.status== ODTree.ODTreeNodeStatus.VALID){
            expandSide=candidate.odByLeftRightAttributeList.right;
            otherSide=candidate.odByLeftRightAttributeList.left;
        }else {
            expandSide=candidate.odByLeftRightAttributeList.left;
            otherSide=candidate.odByLeftRightAttributeList.right;
        }
        //得到expandSide的最后一个属性，即expandAttribute
        int expandAttribute=expandSide.get(expandSide.size()-1).attribute;
        //遍历otherside
        for(AttributeAndDirection x:otherSide){
            //如果otherside中有属性等于expandAttr则不是minimalCandidate
            //为什么？
            if(x.attribute == expandAttribute)
                return false;
        }

        //遍历expandSide
        for (int i = 0; i < expandSide.size()-1; i++) {
            //如果expandAttribute在前面已经出现过，则返回false
            if(expandAttribute==expandSide.get(i).attribute)
                return false;
        }

        result = isListMinimal(expandSide);
        minimalCheckTime+=timer.getTimeUsed();
        return result;
    }


    protected boolean canFindBefore(List<AttributeAndDirection> context,
                                  List<AttributeAndDirection> pattern,int beginPosition){
        int targetAttribute=pattern.get(0).attribute;
        for(int i=beginPosition;i>=0;i--){
            if(context.get(i).attribute==targetAttribute){
                return exactMatch(context,pattern,i);
            }
        }
        return false;
    }


    /**
     *判断pattern是否能匹配context的从beginPosition开始的子集
     * @param context
     * @param pattern
     * @param beginPosition
     * @return
     */
    protected boolean exactMatch(List<AttributeAndDirection> context,
                               List<AttributeAndDirection> pattern,int beginPosition){
        if(beginPosition<0)
            return false;
        //如果起始位置beginPosition加pattern的长度大于context的长度，则pattern一定不在context中
        if(beginPosition+pattern.size()>context.size())
            return false;
        //分别从beginPosition和0开始，遍历两个List,若有不相等的x1，x2，则pattern不在
        for (int i = 0; i < pattern.size(); i++) {
            AttributeAndDirection x1=context.get(beginPosition+i);
            AttributeAndDirection x2=pattern.get(i);
            if(x1!=x2)
                return false;
        }
        return true;
    }
    protected List<AttributeAndDirection> reverseDirection(List<AttributeAndDirection> list){
        List<AttributeAndDirection> result=new ArrayList<>();
        for (AttributeAndDirection attributeAndDirection : list) {
            result.add(AttributeAndDirection.getInstance(attributeAndDirection.attribute
                    ,-attributeAndDirection.direction));
        }
        return result;
    }
}
