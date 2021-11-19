import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArraySet;

public class Node<T> {

    private CopyOnWriteArraySet<Node<T>> children;
    private Node<T> parent;
    private final String url;
    private int nodeSize;

    public Node(String url) {
        this.url = url;
        children = new CopyOnWriteArraySet<>();
        nodeSize = 0;
    }

    public void addChild(Node<T> child) {
        if (!children.contains(child) && child.getUrl().startsWith(url)) {
            this.children.add(child);
            child.setParent(this);

        }
    }

    public CopyOnWriteArraySet<Node<T>> getChildren() {
        return children;
    }

    public void setChildren(CopyOnWriteArraySet<Node<T>> children) {
        this.children = children;
    }

    public Node<T> getParent() {
        return parent;
    }

    public int getNodeSize() {
        return nodeSize;
    }

    public int setNodeSize() {
        if (parent == null) {
            return 0;
        }
        return 1 + parent.getNodeSize();
    }

    public void setParent(Node<T> parent) {
        synchronized (this) {
            this.parent = parent;
            this.nodeSize = setNodeSize();
        }
    }

    public String getUrl() {
        return url;
    }
}