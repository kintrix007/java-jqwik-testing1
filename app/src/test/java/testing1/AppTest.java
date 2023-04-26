/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package testing1;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.stream.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import net.jqwik.api.*;
import net.jqwik.api.constraints.*;

class AppTest {
    @Property
    boolean propContainsAny(
        @ForAll @Size(max = 100) List<@IntRange(min = -200, max = 200) Integer> xs,
        @ForAll @Size(max = 100) List<@IntRange(min = -200, max = 200) Integer> ys
    ) {
        var testContains = App.containsAny(xs, ys);
        //
        var trueContains = xs.stream()
            .anyMatch(x -> ys.stream().anyMatch(y -> x.equals(y)));
        return testContains == trueContains;
    }

    @Property(tries = 100)
    boolean propLastIndexOfIfContainsThenFound(
        @ForAll @IntRange(min = 0, max = 100) int val,
        @ForAll("intArrays0to100") @Size(min = 20) int[] arr
    ) {
        Assume.that(Arrays.stream(arr).anyMatch(x -> x == val));

        return App.lastIndexOf(arr, val, arr.length-1) != App.INDEX_NOT_FOUND;
    }

    @Property(tries = 10)
    boolean propNullArrayNotFound(@ForAll Integer x, @ForAll Integer y) {
        return App.lastIndexOf(null, x, y) == App.INDEX_NOT_FOUND;
    }

    @Property
    boolean propLastIndexOf(
            @ForAll @IntRange(min = 0, max = 100) int val,
            @ForAll("intArrays0to100") @Size(min = 0, max = 20) int[] arr,
            @ForAll @IntRange(min = -10, max = 100) int lastIndex
    ) {
        var testIndex = App.lastIndexOf(arr, val, lastIndex);
        var clamped = Math.max(0, Math.min(arr.length, lastIndex+1));
        var list = Arrays.stream(arr, 0, clamped).boxed().toList();
        var trueIndex = list.lastIndexOf(val);

        return testIndex == trueIndex;
    }

    @Provide
    Arbitrary<int[]> intArrays0to100() {
        return Arbitraries.integers().between(0, 100)
            .array(int[].class);
    }
}
