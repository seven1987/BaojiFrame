package com.apt;

import com.google.auto.service.AutoService;

import java.lang.reflect.ParameterizedType;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;

/**
 * AUTHOR hanshaofeng
 * ACTION 注解apt解析
 * DATE 2017/12/21
 */

@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({
        "com.annotation.InstanceFix",
})
public class AnnotationProcessor extends AbstractProcessor {

    /**
     * 文件相关的辅助类
     */
    public Filer mFiler;

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        mFiler = processingEnv.getFiler();
        new InstanceProcessor().process(roundEnv, this);
        return true;
    }

}
