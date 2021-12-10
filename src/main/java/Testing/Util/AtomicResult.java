package Testing.Util;

public interface AtomicResult extends Comparable<AtomicResult> {

    public boolean shouldInclude();

    public String symbol();
}
