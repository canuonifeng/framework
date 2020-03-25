package com.codeages.framework.jsonrpc;

import static java.lang.String.format;
import static org.springframework.util.ClassUtils.convertClassNameToResourcePath;
import static org.springframework.util.ResourceUtils.CLASSPATH_URL_PREFIX;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import org.springframework.stereotype.Component;

@Component
public class JsonRpcClientCreator implements BeanFactoryPostProcessor, ApplicationContextAware {

	private static final Logger logger = LoggerFactory.getLogger(JsonRpcClientCreator.class);
	private ApplicationContext applicationContext;

	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
		SimpleMetadataReaderFactory metadataReaderFactory = new SimpleMetadataReaderFactory(applicationContext);
		DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) beanFactory;
		String resolvedPath = resolvePackageToScan();
		logger.debug("Scanning '{}' for JSON-RPC service interfaces.", resolvedPath);
		try {
			for (Resource resource : applicationContext.getResources(resolvedPath)) {
				if (resource.isReadable()) {
					MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
					ClassMetadata classMetadata = metadataReader.getClassMetadata();
					AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
					String jsonRpcPathAnnotation = JsonRpcClient.class.getName();
					if (annotationMetadata.isAnnotated(jsonRpcPathAnnotation)) {
						String className = classMetadata.getClassName();
						logger.debug("Found JSON-RPC service to proxy [{}].", className);
						registerJsonProxyBean(defaultListableBeanFactory, className,
								annotationMetadata.getAnnotationAttributes(jsonRpcPathAnnotation));
					}
				}
			}
		} catch (IOException e) {
			throw new IllegalStateException(format("Cannot scan package '%s' for classes.", resolvedPath), e);
		}
	}

	private String resolvePackageToScan() {
		String rpcPackage = applicationContext.getBean(Environment.class).getProperty("rpc.package");
		return CLASSPATH_URL_PREFIX + convertClassNameToResourcePath(rpcPackage) + "/**/*.class";
	}

	private void registerJsonProxyBean(DefaultListableBeanFactory defaultListableBeanFactory, String className,
			Map<String, Object> annotationAttrs) {

		BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(JsonRpcClientProxy.class)
				.addConstructorArgValue(className).addConstructorArgValue(annotationAttrs);

		defaultListableBeanFactory.registerBeanDefinition(className, beanDefinitionBuilder.getBeanDefinition());
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
}
