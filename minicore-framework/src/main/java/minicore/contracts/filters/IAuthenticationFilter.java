package minicore.contracts.filters;

import minicore.contracts.HttpContext;

public interface IAuthenticationFilter {

    void onAuthorized(HttpContext httpContext);
}
