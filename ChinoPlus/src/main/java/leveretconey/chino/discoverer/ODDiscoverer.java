package leveretconey.chino.discoverer;

import leveretconey.chino.dataStructures.AttributeAndDirection;
import leveretconey.chino.dataStructures.DataFrame;
import leveretconey.chino.dataStructures.ODByLeftRightAttributeList;
import leveretconey.chino.dataStructures.ODCandidate;
import leveretconey.chino.dataStructures.ODTree;
import leveretconey.chino.dataStructures.ODTree.ODTreeNode;
import leveretconey.chino.util.Timer;
import leveretconey.chino.util.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


//由于抽象类不能实例化对象，所以抽象类必须被继承，才能被使用
//抽象类中的抽象方法只是声明，不包含方法体，就是不给出方法的具体实现也就是方法的具体功能
//构造方法，类方法（用 static 修饰的方法）不能声明为抽象方法
public abstract class ODDiscoverer {

    public abstract ODTree discover(DataFrame data, ODTree reference);

    //final修饰类不允许被继承
    //final修饰方法不允许被子类重写，但是可以被子类继承，不能修饰构造方法
    //final修饰变量表示不允许修改
    public final ODTree discover(DataFrame data)  {
        return discover(data,null);
    }
}
