package Util;

import javax.servlet.http.HttpServletRequest;

public final class HttpServletRequestUtil {
    /**
     * Checks if all request keys exists inside of the request
     * @param request Request to check
     * @param keys String array of key names to check inside the request
     * @return True if all keys exist, otherwise false
     */
    public static boolean requestContainsKeys(HttpServletRequest request, String[] keys) {
        for(String key : keys) {
            if(!request.getParameterMap().containsKey(key))
                return false;
        }
        return true;
    }
}
