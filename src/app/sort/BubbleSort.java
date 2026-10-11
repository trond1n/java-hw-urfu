package app.sort;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BubbleSort<T> implements SortAlgorithm<T> {

    @Override
    public List<T> sort(List<T> items, Comparator<? super T> comparator) {
        List<T> result = new ArrayList<>(items);
        int n = result.size();
        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < n - 1 - i; j++) {
                if (comparator.compare(result.get(j), result.get(j + 1)) > 0) {
                    T temp = result.get(j);
                    result.set(j, result.get(j + 1));
                    result.set(j + 1, temp);
                    swapped = true;
                }
            }
            if (!swapped) {
                break;
            }
        }
        return result;
    }

    @Override
    public String getName() {
        return "Сортировка пузырьком";
    }
}
