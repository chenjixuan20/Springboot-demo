package leveretconey.chino.dataStructures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ODCandidate implements Comparable<ODCandidate>{
    public ODByLeftRightAttributeList odByLeftRightAttributeList;
    public List<AttributeAndDirection> odByPath;
    public ODTree.ODTreeNode odByODTreeNode;

    public ODCandidate(ODTree.ODTreeNode odByODTreeNode) {
        //this can be null if im debugging
        if(odByODTreeNode==null){
            return;
        }
        this.odByODTreeNode = odByODTreeNode;

        odByPath = new ArrayList<>();
        odByLeftRightAttributeList = new ODByLeftRightAttributeList();
        ODTree.ODTreeNode node = odByODTreeNode;
        //根据之前的规则
        //valid则将attribute加入ODCandidate右侧
        //split则将attribute加入ODCandidate左侧
        while (!node.isRoot()){
            odByPath.add(node.attribute);
            if(node.parent.status == ODTree.ODTreeNodeStatus.VALID)
                odByLeftRightAttributeList.right.add(node.attribute);
            else if(node.parent.status == ODTree.ODTreeNodeStatus.SPLIT)
                odByLeftRightAttributeList.left.add(node.attribute);
            //向上找，前缀树
            //这样的话结点的顺序是反的
            //LHS的顺序是反的
            //RHS的顺序也是反的
            node=node.parent;
        }
        //由于上面方法是从结点开始向root反向找，所以需要进行反转操作
        Collections.reverse(odByPath);
        Collections.reverse(odByLeftRightAttributeList.left);
        Collections.reverse(odByLeftRightAttributeList.right);
    }

    @Override
    public int compareTo(ODCandidate o) {
        return odByPath.size()-o.odByPath.size();

    }
    @Override
    public String toString(){
        return odByLeftRightAttributeList.toString();
    }

    public boolean isSingleDirection(){
        for(AttributeAndDirection attributeAndDirection:odByPath){
            if(attributeAndDirection.direction!=AttributeAndDirection.UP){
                return false;
            }
        }
        return true;
    }
}
