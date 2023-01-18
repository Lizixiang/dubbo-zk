package com.dubbo.core.liability;

/**
 * <pre>
 *
 * </pre>
 *
 * @author lizixiang
 * @date 2023年01月17日
 */
public interface IHandler<T, R> {

    R handle(T t);

}
