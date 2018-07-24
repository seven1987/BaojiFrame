package com.apt;

import javax.annotation.processing.RoundEnvironment;

/**
 * AUTHOR hanshaofeng
 * ACTION 注解接口
 * DATE 2017/12/21
 */

public interface IProcessor {
    void process(RoundEnvironment roundEnv, AnnotationProcessor mAbstractProcessor);
}
