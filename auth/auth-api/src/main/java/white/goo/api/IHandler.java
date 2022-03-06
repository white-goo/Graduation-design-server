package white.goo.api;


public interface IHandler<T> {
    boolean doValidate(T requestParameter);
}
