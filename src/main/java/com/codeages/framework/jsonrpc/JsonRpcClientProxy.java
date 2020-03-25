package com.codeages.framework.jsonrpc;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class JsonRpcClientProxy
		implements MethodInterceptor, InitializingBean, FactoryBean<Object>, ApplicationContextAware {

	private static Logger logger = LoggerFactory.getLogger(JsonRpcClientProxy.class);

	private ApplicationContext applicationContext;
	private Environment environment;
	private Object proxyObject = null;
	private HttpClient client = new HttpClient();
	private Class<?> clazz;
	private Gson gson;
	private String path;
	private String serverName;

	public JsonRpcClientProxy(String className, Map<String, Object> annotationAttrs) throws Throwable {
		this.clazz = Class.forName(className);
		this.path = annotationAttrs.get("name").toString();
		this.serverName = annotationAttrs.get("serverName").toString();
		this.gson = initGson();
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
		environment = this.applicationContext.getBean(Environment.class);
	}

	public Object getObject() throws Exception {
		return proxyObject;
	}

	public Class<?> getObjectType() {
		return this.clazz;
	}

	public boolean isSingleton() {
		return true;
	}

	public Object invoke(MethodInvocation invocation) throws Throwable {
		PostMethod httpMethod = new PostMethod(environment.getProperty(serverName));
		httpMethod.addRequestHeader("Authorization", "Basic " + getBasicAuthorization());
		httpMethod.addRequestHeader("Content-type", "application/json");

		Request request = new Request();
		request.setParams(invocation.getArguments());
		request.setMethod(path + "." + invocation.getMethod().getName());
		String requestJson = this.gson.toJson(request).toString();

		logger.info(request.getMethod() + " json-rpc request: " + requestJson);

		RequestEntity requestEntity = new StringRequestEntity(requestJson, "application/json", "UTF-8");

		httpMethod.setRequestEntity(requestEntity);
		client.executeMethod(httpMethod);
		String responseStr = httpMethod.getResponseBodyAsString();

		logger.info(request.getMethod() + " json-rpc response: " + responseStr);

		Type returnType = invocation.getMethod().getGenericReturnType();

		Type responseType = new TypeToken<Response>() {
			private static final long serialVersionUID = 1L;
		}.getType();
		Response response = this.gson.fromJson(responseStr, responseType);
		return gson.fromJson(this.gson.toJson(response.getResult()), returnType);
	}

	private Gson initGson() {
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(java.util.Date.class, new DateSerializer()).setDateFormat(DateFormat.LONG);
		builder.registerTypeAdapter(java.util.Date.class, new DateDeserializer()).setDateFormat(DateFormat.LONG);
		Gson gson = builder.create();
		return gson;
	}

	private String getBasicAuthorization() {
		String userAndPassword = environment.getProperty(serverName + ".username") + ":"
				+ environment.getProperty(serverName + ".password");
		byte[] auth = Base64.getEncoder().encode(userAndPassword.getBytes());
		return new String(auth);
	}

	public void afterPropertiesSet() {
		proxyObject = ProxyFactory.getProxy(this.clazz, this);
	}

}

class DateSerializer implements JsonSerializer<Date> {
	public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
		return new JsonPrimitive(src.getTime() / 1000);
	}
}

class DateDeserializer implements JsonDeserializer<Date> {
	public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		return new java.util.Date(json.getAsJsonPrimitive().getAsLong() * 1000);
	}
}
