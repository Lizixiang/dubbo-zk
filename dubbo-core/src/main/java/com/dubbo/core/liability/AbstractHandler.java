package com.dubbo.core.liability;

/**
 * <pre>
 *
 * </pre>
 *
 * @author lizixiang
 * @date 2023年01月17日
 */
public abstract class AbstractHandler<T, R> implements IHandler<T, R> {

    @Override
    public R handle(T t) {
        return doHandle(t);
    }

    protected abstract R doHandle(T t);

}
