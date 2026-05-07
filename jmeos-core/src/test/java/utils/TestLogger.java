package utils;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

// Logging extension to automate the traceability of each test using the JUnit lifecycle
public class TestLogger implements BeforeEachCallback, AfterEachCallback {
    @Override
    public void beforeEach(ExtensionContext context) {
        String displayName = context.getDisplayName();
        String methodName = context.getRequiredTestMethod().getName();
        System.out.println("test " + methodName + " started with parameters : " + displayName);
    }

    @Override
    public void afterEach(ExtensionContext context) {
        String methodName = context.getRequiredTestMethod().getName();
        System.out.println("test " + methodName + " finished");
        System.out.println("---");
    }
}