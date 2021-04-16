package leveretconey.exp6;


public class AttributeAndDirection {
    public static final int UP=1;
    public static final int DOWN=-1;

    public final int attr;
    public final int dir;

    public AttributeAndDirection(int attr, int dir){
        this.attr = attr;
        this.dir = dir;
    }

    @Override
    public String toString() {
        if(dir == UP) return attr + "↑";
        return attr + "↓";
    }
}
