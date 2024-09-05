package com.clean.example.yatspec;

import com.googlecode.yatspec.junit.SpecResultListener;
import com.googlecode.yatspec.junit.SpecRunner;
import com.googlecode.yatspec.junit.WithCustomResultListeners;
import com.googlecode.yatspec.rendering.Renderer;
import com.googlecode.yatspec.rendering.html.HtmlResultRenderer;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import com.googlecode.yatspec.state.givenwhenthen.WithTestState;
import org.apache.commons.lang3.StringUtils;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RunWith(SpecRunner.class)
public abstract class YatspecTest implements WithTestState, WithCustomResultListeners {

    private final TestState testState = new TestState();

    @Override
    public TestState testState() {
        return testState;
    }

    protected <T> void log(String title, T value) {
        testState.log(title, value);
    }

    protected <T> void log(String title, Collection<?> value) {
        testState.log(title, StringUtils.join(value, "<br/>"));
    }

    protected <T> T with(T value) {
        return value;
    }

    protected <T> T and(T value) {
        return value;
    }

    protected <T> T to(T value) {
        return value;
    }

    @Override
    public Iterable<SpecResultListener> getResultListeners() throws Exception {
        List<SpecResultListener> specResultListeners = new ArrayList<>();

        HtmlResultRenderer htmlResultRenderer = new HtmlResultRenderer()
                .withCustomRenderer(String.class, new NewLineAsHtmlBreakRenderer());

        specResultListeners.add(htmlResultRenderer);
        return specResultListeners;
    }

    public class NewLineAsHtmlBreakRenderer implements Renderer<String> {
        @Override
        public String render(String s) throws Exception {
            return s.replaceAll("\\n", "<br/>");
        }
    }

}
