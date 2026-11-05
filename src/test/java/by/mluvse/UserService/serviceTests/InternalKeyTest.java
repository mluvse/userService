package by.mluvse.UserService.serviceTests;

import by.mluvse.UserService.web.jwt.InternalKeyFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class InternalKeyTest {

    private InternalKeyFilter filter;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain chain;

    @BeforeEach
    void setUp() {
        filter = new InternalKeyFilter();
        ReflectionTestUtils.setField(filter, "internalKey", "test-internal-key");
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        chain = mock(FilterChain.class);
        SecurityContextHolder.clearContext();

        when(request.getRequestURI()).thenReturn("/auth/credentials");
    }

    @Test
    void doFilter_validKey_setsAuthentication() throws Exception {
        when(request.getHeader("X-Internal-Key")).thenReturn("test-internal-key");

        filter.doFilter(request, response, chain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals("gateway", SecurityContextHolder.getContext().getAuthentication().getName());
        verify(chain).doFilter(request, response);
    }

    @Test
    void doFilter_invalidKey_noAuthentication() throws Exception {
        when(request.getHeader("X-Internal-Key")).thenReturn("wrong-key");

        filter.doFilter(request, response, chain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(chain).doFilter(request, response);
    }

    @Test
    void doFilter_missingKey_noAuthentication() throws Exception {
        when(request.getHeader("X-Internal-Key")).thenReturn(null);

        filter.doFilter(request, response, chain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(chain).doFilter(request, response);
    }

    @Test
    void shouldNotFilter_otherUri_returnsTrue() {
        when(request.getRequestURI()).thenReturn("/api/users");

        assertTrue(filter.shouldNotFilter(request));
    }

    @Test
    void shouldNotFilter_authCredentials_returnsFalse() {
        when(request.getRequestURI()).thenReturn("/auth/credentials");

        assertFalse(filter.shouldNotFilter(request));
    }

}
