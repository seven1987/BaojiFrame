package com.apt;

import com.annotation.InstanceFix;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;

import static com.squareup.javapoet.TypeSpec.classBuilder;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;

/**
 * AUTHOR hanshaofeng
 * ACTION 注解解析
 * DATE 2017/12/21
 */

public class InstanceProcessor implements IProcessor {

    @Override
    public void process(RoundEnvironment roundEnv, AnnotationProcessor mAbstractProcessor) {
        String CLASS_NAME = "InstanceFixs";
        TypeSpec.Builder tb = classBuilder(CLASS_NAME).addModifiers(PUBLIC, FINAL).addJavadoc("@apt生成彩种修复实例集合");
        List<ClassName> mList = new ArrayList<>();

        ClassName speciesFix = ClassName.get("com.sensefun.betting.components.betting.factory", "ISpeciesFix");
        ClassName list = ClassName.get("java.util", "List");
        ClassName arrayList = ClassName.get("java.util", "ArrayList");
        TypeName listFix = ParameterizedTypeName.get(list, speciesFix);

        MethodSpec.Builder methodBuilder1 = MethodSpec.methodBuilder("list")
                .addJavadoc("@此方法由apt自动生成")
                .returns(listFix).addModifiers(PUBLIC, STATIC)
                .addStatement("$T result = new $T<>()", listFix, arrayList);

        try {
            for (TypeElement element : ElementFilter.typesIn(roundEnv.getElementsAnnotatedWith(InstanceFix.class))) {
                ClassName currentType = ClassName.get(element);
                if (mList.contains(currentType)){
                    continue;
                }
                mList.add(currentType);
                //初始化修复器
                methodBuilder1.addStatement("result.add(new $T())", currentType);
            }
            methodBuilder1.addStatement("return result");
            tb.addMethod(methodBuilder1.build());
             //生成源代码
            JavaFile javaFile = JavaFile.builder("com.sensefun.hc.apt", tb.build()).build();
            javaFile.writeTo(mAbstractProcessor.mFiler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
