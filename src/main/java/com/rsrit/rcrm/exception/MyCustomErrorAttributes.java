package com.rsrit.rcrm.exception;

import java.util.Map;

import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

@Component
public class MyCustomErrorAttributes extends DefaultErrorAttributes {

    // This is used to customize ResponseStatusException json key-value pairs. We remove exception key from output json
    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
        // TODO Auto-generated method stub
        Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, includeStackTrace);
        errorAttributes.remove("trace");
        errorAttributes.remove("error");
        return errorAttributes;
    }

}
