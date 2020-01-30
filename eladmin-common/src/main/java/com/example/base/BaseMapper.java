package com.example.base;

/**
 * Created with IntelliJ IDEA.
 * User: liaozhenglong
 * Date: 2020/1/28
 * Time: 14:59
 * Description: No Description
 */
public interface BaseMapper<D,E> {
    /**
     * Entity转Dto*/
    D toDto(E entity);
}
