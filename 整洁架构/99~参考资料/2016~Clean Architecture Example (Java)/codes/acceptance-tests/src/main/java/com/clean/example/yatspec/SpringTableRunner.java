package com.clean.example.yatspec;

import com.googlecode.yatspec.junit.DecoratingFrameworkMethod;
import com.googlecode.yatspec.junit.Row;
import com.googlecode.yatspec.junit.Table;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public class SpringTableRunner extends SpringJUnit4ClassRunner {

    private org.junit.runner.manipulation.Filter filter;

    public SpringTableRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    protected void validatePublicVoidNoArgMethods(Class<? extends Annotation> annotation, boolean isStatic, List<Throwable> errors) {
        // skip
    }

    @Override
    protected List<FrameworkMethod> getChildren() {

        List<FrameworkMethod> allTestsToBeExecuted = new ArrayList<>();

        for (FrameworkMethod frameworkMethod : computeTestMethods()) {
            final Table annotation = frameworkMethod.getAnnotation(Table.class);
            if (annotation == null) {
                allTestsToBeExecuted.add(frameworkMethod);
            } else {
                Row[] annotationValues = annotation.value();
                for (Row annotationValue : annotationValues) {
                    allTestsToBeExecuted.add(new DecoratingFrameworkMethod(frameworkMethod, annotationValue));
                }
            }
        }

        return allTestsToBeExecuted;
    }

    @Override
    protected List<FrameworkMethod> computeTestMethods() {
        if (filter == null) {
            return super.computeTestMethods();
        }

        List<FrameworkMethod> methodsToBeComputed = new ArrayList<>();

        for (FrameworkMethod frameworkMethod : super.computeTestMethods()) {
            if (filter.shouldRun(describeChild(frameworkMethod))) {
                methodsToBeComputed.add(frameworkMethod);
            }
        }
        return methodsToBeComputed;
    }

    @Override
    public void filter(org.junit.runner.manipulation.Filter filter) throws NoTestsRemainException {
        this.filter = filter;
        if (computeTestMethods().isEmpty()) {
            throw new NoTestsRemainException();
        }
    }

}
