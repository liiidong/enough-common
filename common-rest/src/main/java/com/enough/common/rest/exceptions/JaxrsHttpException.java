package com.enough.common.rest.exceptions;

import javax.ws.rs.core.Response.Status;

public class JaxrsHttpException extends RuntimeException {
        private static final long serialVersionUID = -3286032107078353210L;
        private transient Status errorStatus;

        public JaxrsHttpException() {
        }

        public JaxrsHttpException(String errorMsg) {
            super(errorMsg);
        }

        public JaxrsHttpException(Status errorStatus, String errorMsg) {
            super(errorMsg);
            this.errorStatus = errorStatus;
        }

        public JaxrsHttpException(Status errorStatus, String errorMsg, Throwable cause) {
            super(errorMsg, cause);
            this.errorStatus = errorStatus;
        }

        public JaxrsHttpException(int code, String errorMsg) {
            super(errorMsg);
            this.errorStatus = parseStatus(code);
        }

        public JaxrsHttpException(int code, String errorMsg, Throwable cause) {
            super(errorMsg, cause);
            this.errorStatus =parseStatus(code);
        }

        public String getErrorMsg() {
            return this.getMessage();
        }

        public Status getErrorStatus() {
            return this.errorStatus;
        }
        
        public static Status parseStatus(int code ) {
            for (Status e : Status.values()) { 
                if(code == e.getStatusCode()) {
                    return e;
                }
            } 
            return null;
        }
}
