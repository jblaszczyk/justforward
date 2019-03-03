package just.forward.util;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collector.Characteristics;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.ImmutableSortedSet;

public class ImmutableCollectors {

    public static <T> 
    Collector<T, ImmutableSet.Builder<T>, ImmutableSet<T>> toSet() {
    	return new CollectorImpl<>(
			ImmutableSet::builder,
			(c, v) -> c.add(v),
			(c1, c2) -> (ImmutableSet.Builder<T>) c1.addAll(c2.build().iterator()),
			(bl -> (ImmutableSet<T>) bl.build()),
			ImmutableSet.of(Characteristics.CONCURRENT, Characteristics.UNORDERED)
    	);
    }
	
    public static <T> 
    Collector<T, ImmutableList.Builder<T>, ImmutableList<T>> toList() {
    	return new CollectorImpl<>(
			ImmutableList::builder, 
    		(c, v) -> c.add(v), 
    		(c1, c2) -> (ImmutableList.Builder<T>) c1.addAll(c2.build().iterator()), 
    		(bl -> (ImmutableList<T>) bl.build()),
			ImmutableSet.of(Characteristics.CONCURRENT)
    	);
	}

	public static <T extends Comparable<?>> 
	Collector<T, ImmutableSortedSet.Builder<T>, ImmutableSortedSet<T>> toSortedSet() {
    	return new CollectorImpl<>(
    		ImmutableSortedSet::naturalOrder, 
    		(c, v) -> c.add(v), 
    		(c1, c2) -> (ImmutableSortedSet.Builder<T>) c1.addAll(c2.build().iterator()),
    		(bl -> (ImmutableSortedSet<T>) bl.build()),
    		ImmutableSet.of(Characteristics.CONCURRENT)
        );
	}
	
	public static <T, K, U>
    Collector<T, ImmutableMap.Builder<K,U>, ImmutableMap<K,U>> toMap(
   		Function<? super T, ? extends K> keyMapper,
        Function<? super T, ? extends U> valueMapper) {
		
		return new CollectorImpl<>(
			ImmutableMap::builder,
			(c, e) -> c.put(keyMapper.apply(e), valueMapper.apply(e)),
			(c1, c2) -> (ImmutableMap.Builder<K,U>) c1.putAll(c2.build()),
			(bl -> (ImmutableMap<K,U>) bl.build()),
			ImmutableSet.of(Characteristics.CONCURRENT, Characteristics.UNORDERED)
//			ImmutableSet.of(Characteristics.CONCURRENT)
    	);
    }	

	public static <T, K, U>
    Collector<T, ImmutableMultimap.Builder<K,U>, ImmutableMultimap<K,U>> toMultimap(
   		Function<? super T, ? extends K> keyMapper,
        Function<? super T, ? extends U> valueMapper) {
		
		return new CollectorImpl<>(
			ImmutableMultimap::builder,
			(c, e) -> c.put(keyMapper.apply(e), valueMapper.apply(e)),
			(c1, c2) -> (ImmutableMultimap.Builder<K,U>) c1.putAll(c2.build()),
			(bl -> (ImmutableMultimap<K,U>) bl.build()),
			ImmutableSet.of(Characteristics.CONCURRENT, Characteristics.UNORDERED)
//			ImmutableSet.of(Characteristics.CONCURRENT)
    	);
    }	
	
	public static <T, K, U>
    Collector<T, ImmutableSetMultimap.Builder<K,U>, ImmutableSetMultimap<K,U>> toSetMultimap(
   		Function<? super T, ? extends K> keyMapper,
        Function<? super T, ? extends U> valueMapper) {
		
		return new CollectorImpl<>(
			ImmutableSetMultimap::builder,
			(c, e) -> c.put(keyMapper.apply(e), valueMapper.apply(e)),
			(c1, c2) -> (ImmutableSetMultimap.Builder<K,U>) c1.putAll(c2.build()),
			(bl -> (ImmutableSetMultimap<K,U>) bl.build()),
			ImmutableSet.of(Characteristics.CONCURRENT, Characteristics.UNORDERED)
//			ImmutableSet.of(Characteristics.CONCURRENT)
    	);
    }	

    static class CollectorImpl<T, A, R> implements Collector<T, A, R> {
        private final Supplier<A> supplier;
        private final BiConsumer<A, T> accumulator;
        private final BinaryOperator<A> combiner;
        private final Function<A, R> finisher;
        private final Set<Characteristics> characteristics;

        CollectorImpl(Supplier<A> supplier,
                      BiConsumer<A, T> accumulator,
                      BinaryOperator<A> combiner,
                      Function<A,R> finisher,
                      Set<Characteristics> characteristics) {
            this.supplier = supplier;
            this.accumulator = accumulator;
            this.combiner = combiner;
            this.finisher = finisher;
            this.characteristics = characteristics;
        }

        @Override
        public BiConsumer<A, T> accumulator() {
            return accumulator;
        }

        @Override
        public Supplier<A> supplier() {
            return supplier;
        }

        @Override
        public BinaryOperator<A> combiner() {
            return combiner;
        }

        @Override
        public Function<A, R> finisher() {
            return finisher;
        }

        @Override
        public Set<Characteristics> characteristics() {
            return characteristics;
        }
    }    
}
