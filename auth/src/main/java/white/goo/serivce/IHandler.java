package white.goo.serivce;


public interface IHandler<T> {
    boolean doValidate(T requestParameter);
}
