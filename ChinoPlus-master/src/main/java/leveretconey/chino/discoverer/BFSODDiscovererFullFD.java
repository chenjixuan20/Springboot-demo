package leveretconey.chino.discoverer;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import leveretconey.chino.dataStructures.DataFrame;
import leveretconey.chino.dataStructures.ODCandidate;
import leveretconey.chino.dataStructures.ODTree;
import leveretconey.chino.dataStructures.ODTree.ODTreeNode;
import leveretconey.chino.dataStructures.ODTreeNodeEquivalenceClasses;
import leveretconey.chino.minimal.ODMinimalCheckTree;
import leveretconey.chino.minimal.ODMinimalChecker;
import leveretconey.chino.util.Timer;
import leveretconey.chino.util.Util;

public class BFSODDiscovererFullFD extends ODDiscoverer{
    @Override
    public ODTree discover(DataFrame data, ODTree reference) {
        Timer timer=new Timer();
        Queue<ODDiscovererNodeSavingInfo> queue = new LinkedList<>();
        ODTree result = new ODTree(data.getColumnCount());
        int attributeCount = data.getColumnCount();
        ODMinimalChecker odMinimalChecker=new ODMinimalCheckTree(data.getColumnCount());

        //note that the direction of all nodes in the second level are always UP
        for (int attribute = 0; attribute < attributeCount; attribute++) {
            if(reference != null) {
                copyConfirmNode(result, result.getRoot().children[attribute]
                        , reference.getRoot().children[attribute]);
            }
            ODTreeNodeEquivalenceClasses odTreeNodeEquivalenceClasses = new ODTreeNodeEquivalenceClasses();
            odTreeNodeEquivalenceClasses.mergeNode(result.getRoot().children[attribute], data);
            queue.offer(new ODDiscovererNodeSavingInfo(result.getRoot().children[attribute]
                    , null, odTreeNodeEquivalenceClasses));
        }

        int preLevel = 2;
        int beginIndex = 0;

        while (!queue.isEmpty()) {
            ODDiscovererNodeSavingInfo info = queue.poll();
            ODTreeNode parent = info.nodeInResultTree;
            ODTreeNodeEquivalenceClasses parentEc = info.odTreeNodeEquivalenceClasses;

            int level = parent.level + 1;

            System.out.println("在处理的层: " + level);
            if(level != preLevel){
                System.out.println("第" + preLevel + "层已经处理完");
                List<ODCandidate> ods = result.getAllOdsOrderByBFS();
                List<ODCandidate> partOds = ods.subList(beginIndex, ods.size());
                for (ODCandidate od : partOds) {
                    Util.out(od);
                }
                beginIndex = ods.size();
            }
            preLevel = level;

            for (int attribute = 0; attribute < attributeCount*2; attribute++) {
                ODTreeNode child;
                if(parent.children[attribute] == null)
                    child = result.new ODTreeNode(parent,result.childrenIndex2AttributeAndDirection(attribute));
                else
                    child = parent.children[attribute];
                ODCandidate childCandidate=new ODCandidate(child);
                child.minimal = odMinimalChecker.isCandidateMinimal(childCandidate);
                if(!child.minimal)
                    continue;
                ODTreeNodeEquivalenceClasses childEc = parentEc.deepClone();
                childEc.mergeNode(child,data);
                child.minimal = !childEc.equals(parentEc);
                if(!child.minimal)
                    continue;
                if(!child.confirm)
                    child.status = childEc.validate(data).status;
                if(child.status == ODTree.ODTreeNodeStatus.VALID){
                    odMinimalChecker.insert(childCandidate);
                }
                if(child.status != ODTree.ODTreeNodeStatus.SWAP){
                    queue.offer(new ODDiscovererNodeSavingInfo(child
                            ,null,childEc));
                }
            }
        }
        return result;
    }

    private void copyConfirmNode(ODTree resultTree,ODTreeNode resultTreeNode,ODTreeNode referenceTreeNode){
        for (ODTreeNode referenceChildNode:referenceTreeNode.children) {
            if (referenceChildNode != null && referenceChildNode.confirm) {
                ODTreeNode resultChildNode = resultTree.new ODTreeNode
                        (resultTreeNode, referenceChildNode.attribute);
                resultChildNode.status = referenceChildNode.status;
                resultChildNode.confirm();
                copyConfirmNode(resultTree, resultChildNode, referenceChildNode);
            }
        }
    }

    public static void main(String[] args) {
        DataFrame dataFrame=DataFrame.fromCsv("integer datasets/test.csv");
        ODTree discover = new BFSODDiscovererFullFD().discover(dataFrame);
//        List<ODCandidate> ods = discover.getAllOdsOrderByBFS();
//        for (ODCandidate od : ods) {
//            Util.out(od);
//        }
//        Util.out("发现ODs的数量："+ods.size());
    }
}
