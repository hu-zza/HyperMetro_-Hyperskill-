package hu.zza.hyperskill.hypermetro.data;

public class PriorityNode<T> extends Node<T> implements Comparable<PriorityNode<T>> {
  private int priority;

  public PriorityNode(T realObject) {
    this(realObject, Integer.MAX_VALUE);
  }

  public PriorityNode(T realObject, int priority) {
    super(realObject);
    this.priority = priority;
  }

  public int getPriority() {
    return priority;
  }

  public void setPriority(int priority) {
    this.priority = priority;
  }

  @Override
  public int compareTo(PriorityNode<T> o) {
    return priority - o.priority;
  }
}
