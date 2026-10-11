package app.sort;

import java.util.Comparator;
import java.util.List;

public interface SortAlgorithm<T> {

    List<T> sort(List<T> items, Comparator<? super T> comparator);

    String getName();
}