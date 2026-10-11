package app.sort;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class InsertionSort<T> implements SortAlgorithm<T> {

    @Override
    public List<T> sort(List<T> items, Comparator<? super T> comparator) {
        List<T> result = new ArrayList<>(items);
        int n = result.size();
        for (int i = 1; i < n; i++) {
            T current = result.get(i);
            int j = i - 1;
            while (j >= 0 && comparator.compare(result.get(j), current) > 0) {
                result.set(j + 1, result.get(j));
                j--;
            }
            result.set(j + 1, current);
        }
        return result;
    }

    @Override
    public String getName() {
        return "Сортировка вставками";
    }
}
