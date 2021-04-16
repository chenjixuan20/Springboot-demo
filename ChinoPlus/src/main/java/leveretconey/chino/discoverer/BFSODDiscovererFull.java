package leveretconey.chino.discoverer;

import java.util.LinkedList;
import java.util.Queue;

import leveretconey.chino.dataStructures.DataFrame;
import leveretconey.chino.dataStructures.ODCandidate;
import leveretconey.chino.minimal.ODMinimalCheckTree;
import leveretconey.chino.dataStructures.ODTree;
import leveretconey.chino.dataStructures.ODTree.ODTreeNode;
import leveretconey.chino.dataStructures.ODTreeNodeEquivalenceClasses;
import leveretconey.chino.minimal.ODMinimalChecker;
import leveretconey.chino.util.Util;

public class BFSODDiscovererFull extends ODDiscoverer{
    /**
     *
     * @param data
     * @param reference
     * @return
     */
    //reference是用来干嘛的？
    @Override
    public ODTree discover(DataFrame data, ODTree reference) {
        int visitNodeCount=0;

        Queue<ODDiscovererNodeSavingInfo> queue = new LinkedList<>();
        ODTree result = new ODTree(data.getColumnCount());
        int attributeCount=data.getColumnCount();
        ODMinimalChecker odMinimalChecker=new ODMinimalCheckTree(data.getColumnCount());


        //note that the direction of all nodes in the second level are always UP
        for (int attribute = 0; attribute < attributeCount; attribute++) {
            //当reference不为空时，表示是多次遍历，也就是使用了抽样——验证技术。
            //这里是整体遍历，所以就使用不上
            if(reference != null) {
                //复制reference到result上
                copyConfirmNode(result, result.getRoot().children[attribute]
                        , reference.getRoot().children[attribute]);
            }
            ODTreeNodeEquivalenceClasses odTreeNodeEquivalenceClasses = new ODTreeNodeEquivalenceClasses();
            //根据ExtendOD规则，决定root的字节点扩展再哪一侧
            //扩展的是第一层，每一次遍历把一个结点加入队列中
            odTreeNodeEquivalenceClasses.mergeNode(result.getRoot().children[attribute], data);
            //将下一层加入队列
            queue.offer(new ODDiscovererNodeSavingInfo(result.getRoot().children[attribute]
                    , null, odTreeNodeEquivalenceClasses));
        }

        //BFS
        while (!queue.isEmpty()) {
            ODDiscovererNodeSavingInfo info=queue.poll();
            ODTreeNode parent=info.nodeInResultTree;
            visitNodeCount++;
            Util.out(String.format("当前访问到第%d个结点%s",visitNodeCount,parent));

            //为什么要遍历attributeCount*2次？因为子结点的个数最多为attributeCount，每个的方向有2种
            for (int attribute = 0; attribute < attributeCount*2; attribute++) {
                ODTreeNode child;
                if(parent.children[attribute] == null)
                    //如果从result中取来的结点还没有子结点，新建一个子结点
                    child = result.new ODTreeNode(parent,result.childrenIndex2AttributeAndDirection(attribute));
                else
                    child = parent.children[attribute];

                //childCandidate是最后扩展属性为child.attribute的ODCandidate
                ODCandidate childCandidate = new ODCandidate(child);
                //
                child.minimal = odMinimalChecker.isCandidateMinimal(childCandidate);
                if(!child.minimal)
                    continue;
                //复刻一个当前结点的等价类对象
                ODTreeNodeEquivalenceClasses odTreeNodeEquivalenceClasses =
                        info.odTreeNodeEquivalenceClasses.deepClone();
                odTreeNodeEquivalenceClasses.mergeNode(child,data);
                if(!child.confirm)
                    child.status = odTreeNodeEquivalenceClasses.validate(data).status;
                //？待看
                if(child.status == ODTree.ODTreeNodeStatus.VALID){
                    odMinimalChecker.insert(childCandidate);
                }

                //如果staus不是swap，则会产生子结点，将child加入队列
                if(child.status!= ODTree.ODTreeNodeStatus.SWAP){
                    queue.offer(new ODDiscovererNodeSavingInfo(child
                            ,null,odTreeNodeEquivalenceClasses));
                }
            }
        }
        return result;
    }

    /**
     * 复制
     * @param resultTree
     * @param resultTreeNode
     * @param referenceTreeNode
     */
    private void copyConfirmNode(ODTree resultTree,ODTreeNode resultTreeNode,ODTreeNode referenceTreeNode){
        //遍历referenceTreeNode的子结点
        for (ODTreeNode referenceChildNode:referenceTreeNode.children) {
            //若子结点不为空，且子结点已经Confirm了
            if(referenceChildNode != null && referenceChildNode.confirm){
                //在resultTree上创建子结点，resultTreeNode是父结点，referenceChildNode.attribute为attibute
                //state为unknow
                ODTreeNode resultChildNode = resultTree.new ODTreeNode
                        (resultTreeNode,referenceChildNode.attribute);
                resultChildNode.status=referenceChildNode.status;
                resultChildNode.confirm();
                //继续复制子结点。
                copyConfirmNode(resultTree,resultChildNode,referenceChildNode);
            }
        }
    }
}
