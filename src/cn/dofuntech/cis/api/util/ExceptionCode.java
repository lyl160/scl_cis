package cn.dofuntech.cis.api.util;

public enum ExceptionCode {
	
	READ_FILE_FAILED("read.file.failed"),
	READ_CONFIG_FAILED("read.config.failed"),
    READ_PARAMETER_ISNULL("read.parameter.isnull"),
    READ_SHOULD_NULL("read.should.null"),
    COLSE_FILE_FAILED("colse.file.failed"),
    REQUEST_NOT_FOUND("request.not.found"),
    INTERNAL_SERVER_ERROR("internal.server.error"),
    PARAM_FORMAT_ERROR("param.format.error"),
    PARAM_REPEAT_ERROR ("param.repeat.error");
    
    private String exceptionCode;
	
    private ExceptionCode(String exceptionCode) {
    	this.exceptionCode = exceptionCode;
    }

	public String getExceptionCode() {
		return exceptionCode;
	}

	public void setExceptionCode(String exceptionCode) {
		this.exceptionCode = exceptionCode;
	}
    
}

