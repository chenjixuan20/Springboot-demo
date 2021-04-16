package leveretconey.chino.discoverer;

import leveretconey.chino.dataStructures.DataFrame;
import leveretconey.chino.dataStructures.ODCandidate;
import leveretconey.chino.dataStructures.ODTree;
import leveretconey.chino.dataStructures.ODTree.ODTreeNode;
import leveretconey.chino.dataStructures.ODTreeNodeEquivalenceClasses;
import leveretconey.chino.minimal.ODMinimalCheckTree;
import leveretconey.chino.minimal.ODMinimalChecker;
import leveretconey.chino.minimal.ODMinimalCheckerBruteForce;
import leveretconey.chino.util.Util;

import java.util.LinkedList;
import java.util.Queue;

public class BFSODDiscovererForIteration extends ODDiscoverer{

    public static final int INITIAL_RETURN_THRESHOLD =1000;
    private boolean complete=true;
    private Queue<ODDiscovererNodeSavingInfo> queue;
    private ODMinimalChecker odMinimalChecker;
    //因为实验中发现，一次广度优先发现od太多了，所以在发现到一定的阈值则返回进行验证操作
    private int returnThreshold;


    public boolean isComplete() {
        return complete;
    }

    public ODTree restartDiscovering(DataFrame data,ODTree reference){
        odMinimalChecker=new ODMinimalCheckTree(data.getColumnCount());
//        odMinimalChecker=new ODMinimalCheckerBruteForce();
        queue=new LinkedList<>();
        ODTree result=new ODTree(data.getColumnCount());

        returnThreshold= INITIAL_RETURN_THRESHOLD;
        int attributeCount=data.getColumnCount();

        for (int attribute = 0; attribute < attributeCount; attribute++){
            if(reference!=null) {
                copyConfirmNode(result, result.getRoot().children[attribute]
                        , reference.getRoot().children[attribute]);
            }
            ODTreeNodeEquivalenceClasses odTreeNodeEquivalenceClasses = new ODTreeNodeEquivalenceClasses();
            odTreeNodeEquivalenceClasses.mergeNode(result.getRoot().children[attribute], data);
            //add()和offer()都是向队列中添加一个元素
            // 一些队列有大小限制，因此如果想在一个满的队列中加入一个新项
            // 调用 add() 方法就会抛出一个 unchecked 异常，而调用 offer() 方法会返回 false
            queue.offer(new ODDiscovererNodeSavingInfo(result.getRoot().children[attribute]
                    , null, odTreeNodeEquivalenceClasses));
        }
        BFSTraversing(data,result);
        return result;
    }

    public ODTree continueDiscovering(DataFrame data,ODTree tree){
        returnThreshold*=2;
        BFSTraversing(data, tree);
        return tree;
    }

    //重写父类的抽象方法
    @Override
    public ODTree discover(DataFrame data, ODTree reference) {
        if (complete)
            return restartDiscovering(data,reference);
        else
            return continueDiscovering(data,reference);
    }

    private void BFSTraversing(DataFrame data,ODTree result){
        int attributeCount=data.getColumnCount();
        int newFoundOdCount=0;
        while (!queue.isEmpty()) {

            ODDiscovererNodeSavingInfo info=queue.poll();
            ODTreeNode parent=info.nodeInResultTree;

            //attributrCount*2 为什么？
            for (int attribute = 0; attribute < attributeCount*2; attribute++) {
                ODTreeNode child;
                //没子节点进行什么操作？
                if(parent.children[attribute]==null)
                    child=result.new ODTreeNode(parent,result.childrenIndex2AttributeAndDirection(attribute));
                else
                    child=parent.children[attribute];
                //得到ODTree中从root到child结点的ODCandidate
                ODCandidate childCandidate=new ODCandidate(child);
                //检查child是否为minimal list
                child.minimal=odMinimalChecker.isCandidateMinimal(childCandidate);
                if(!child.minimal)
                    continue;
                ODTreeNodeEquivalenceClasses odTreeNodeEquivalenceClasses =
                        info.odTreeNodeEquivalenceClasses.deepClone();
                odTreeNodeEquivalenceClasses.mergeNode(child,data);
                if(!child.confirm)
                    child.status=odTreeNodeEquivalenceClasses.validate(data).status;
                if(child.status!= ODTree.ODTreeNodeStatus.SWAP){
                    queue.offer(new ODDiscovererNodeSavingInfo(child
                            ,null,odTreeNodeEquivalenceClasses));
                }
                if(child.status== ODTree.ODTreeNodeStatus.VALID){
                    odMinimalChecker.insert(childCandidate);
                    if(!child.confirm){
                        newFoundOdCount++;
                    }
                }
            }
            if(newFoundOdCount>=returnThreshold){
                complete=false;
                return;
            }
        }
        complete=true;
    }

    private void copyConfirmNode(ODTree resultTree,ODTreeNode resultTreeNode,ODTreeNode referenceTreeNode){
        for (ODTreeNode referenceChildNode:referenceTreeNode.children) {
            if(referenceChildNode!=null && referenceChildNode.confirm){
                ODTreeNode resultChildNode =resultTree.new ODTreeNode
                        (resultTreeNode,referenceChildNode.attribute);
                resultChildNode.status=referenceChildNode.status;
                resultChildNode.confirm();
                copyConfirmNode(resultTree,resultChildNode,referenceChildNode);
            }
        }
    }
}
