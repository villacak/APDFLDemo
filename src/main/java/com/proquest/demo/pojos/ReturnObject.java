package com.proquest.demo.pojos;

import com.google.gson.Gson;
import com.proquest.demo.enums.HttpCode;

import java.util.List;

/**
 * Object to be returned once the task has run
 *
 */
public class ReturnObject {

	private HttpCode responseCode;
	private String message;
	private List<Object> objects;

	public HttpCode getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(final HttpCode responseCode) {
		this.responseCode = responseCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(final String message) {
		this.message = message;
	}

	public List<Object> getObjects() {
		return objects;
	}

	public void setObjects(final List<Object> objects) {
		this.objects = objects;
	}


	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("ReturnObject{");
		sb.append("responseCode=").append(responseCode);
		sb.append(", message='").append(message).append('\'');
		sb.append(", objects=").append(objects);
		sb.append('}');
		return sb.toString();
	}

	/**
	 * Method toJsonString.
	 * @return String
	 */
	public String toJsonString() {
		Gson gson = new Gson();
		String json = gson.toJson(this);
		return json;
	}
}
