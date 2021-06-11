package hu.zza.hyperskill.hypermetro.data;

public class Node<T> {
  private Node<T> parent;
  private final T realObject;

  public Node(T realObject) {
    this.realObject = realObject;
  }

  public Node<T> getParent() {
    return parent;
  }

  public void setParent(Node<T> parent) {
    this.parent = parent;
  }

  public T getRealObject() {
    return realObject;
  }
}
