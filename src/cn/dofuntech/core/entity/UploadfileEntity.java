package cn.dofuntech.core.entity;

import java.io.Serializable;

public class UploadfileEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int error;
	private String message;
	private String entity; //文件链接
	private String fileName;
	private boolean success = false;

	public int getError() {
		return this.error;
	}

	public void setError(int error) {
		this.error = error;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isSuccess() {
		return this.success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public static UploadfileEntity getInterance(int error, String message,
			String entity, String fileName) {
		UploadfileEntity vo = new UploadfileEntity();
		vo.setError(error);
		vo.setMessage(message);
		vo.setEntity(entity);
		vo.setFileName(fileName);
		return vo;
	}
	
	public static UploadfileEntity getInterance(int error, String message) {
		UploadfileEntity vo = new UploadfileEntity();
		vo.setError(error);
		vo.setMessage(message);
		return vo;
	}

	public static UploadfileEntity getInterance(int error, String message,
			String entity, String fileName, boolean success) {
		UploadfileEntity vo = new UploadfileEntity();
		vo.setError(error);
		vo.setMessage(message);
		vo.setEntity(entity);
		vo.setFileName(fileName);
		vo.setSuccess(success);
		return vo;
	}
}