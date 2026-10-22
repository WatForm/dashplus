package ca.uwaterloo.watform.utils;

@FunctionalInterface
public interface QuadFunction<T, U, V, R, S> {
  S apply(T t, U u, V v, R r);
}
