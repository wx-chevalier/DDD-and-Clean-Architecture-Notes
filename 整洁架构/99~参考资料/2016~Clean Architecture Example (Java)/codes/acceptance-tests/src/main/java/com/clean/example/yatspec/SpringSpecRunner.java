package com.clean.example.yatspec;

import com.googlecode.yatspec.junit.DefaultResultListeners;
import com.googlecode.yatspec.junit.SpecResultListener;
import com.googlecode.yatspec.junit.WithCustomResultListeners;
import com.googlecode.yatspec.state.Result;
import com.googlecode.yatspec.state.Scenario;
import com.googlecode.yatspec.state.TestResult;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import com.googlecode.yatspec.state.givenwhenthen.WithTestState;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SpringSpecRunner extends SpringTableRunner {
    public static final String OUTPUT_DIR = "yatspec.output.dir";
    private final Result testResult;
    private Scenario currentScenario;
    private WithCustomResultListeners listeners = new DefaultResultListeners();

    public SpringSpecRunner(Class<?> klass) throws org.junit.runners.model.InitializationError {
        super(klass);
        testResult = new TestResult(klass);
    }

    @Override
    protected List<FrameworkMethod> computeTestMethods() {
        List<FrameworkMethod> methodsToBeComputed = new ArrayList<>();

        for (FrameworkMethod frameworkMethod : super.computeTestMethods()) {
            if(isNotEvaluateMethod(frameworkMethod)){
                methodsToBeComputed.add(frameworkMethod);
            }
        }
        return methodsToBeComputed;
    }

    private boolean isNotEvaluateMethod(FrameworkMethod frameworkMethod) {
        return !frameworkMethod.getName().equals("evaluate");
    }

    @Override
    protected Object createTest() throws Exception {
        Object instance = super.createTest();
        if (instance instanceof WithCustomResultListeners) {
            listeners = (WithCustomResultListeners) instance;
        } else {
            listeners = new DefaultResultListeners();
        }
        return instance;
    }

    @Override
    public void run(RunNotifier notifier) {
        final SpecListener listener = new SpecListener();
        notifier.addListener(listener);
        super.run(notifier);
        notifier.removeListener(listener);
        try {
            for (SpecResultListener resultListener : listeners.getResultListeners()) {
                resultListener.complete(getOuputDirectory(), testResult);
            }
        } catch (Exception e) {
            System.out.println("Error while writing yatspec output");
            e.printStackTrace(System.out);
        }
    }

    @Override
    protected Statement methodInvoker(final FrameworkMethod method, final Object test) {
        final Statement statement = super.methodInvoker(method, test);
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                currentScenario = testResult.getScenario(method.getName());

                if (test instanceof WithTestState) {
                    TestState testState = ((WithTestState) test).testState();
                    currentScenario.setTestState(testState);
                }
                statement.evaluate();
            }
        };
    }

    public File getOuputDirectory() {
        return new File(System.getProperty(OUTPUT_DIR, System.getProperty("java.io.tmpdir")));
    }

    private boolean isInTest() {
        return currentScenario != null;
    }

    private final class SpecListener extends RunListener {

        @Override
        public void testFailure(Failure failure) throws Exception {
            if (isInTest()) {
                currentScenario.setException(failure.getException());
            }
        }
    }
}

