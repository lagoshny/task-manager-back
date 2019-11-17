package ru.lagoshny.task.manager.helper;

import org.mockito.invocation.InvocationOnMock;

/**
 * Contains additional useful methods to work with {@link org.mockito.Mockito} framework.
 */
public class MockitoUtils {

    /**
     * Return answer as first passed parameter.
     *
     * @param invocationOnMock object {@link InvocationOnMock}
     * @return object equals first passed parameter
     */
    public static Object returnFirstParam(final InvocationOnMock invocationOnMock) {
        return invocationOnMock.getArgument(0);
    }

}
