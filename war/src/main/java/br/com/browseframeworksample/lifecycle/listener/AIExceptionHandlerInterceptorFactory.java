package br.com.browseframeworksample.lifecycle.listener;

import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;

import br.com.browseframework.jsfprimefaces.listener.ExceptionHandlerInterceptorFactory;

public class AIExceptionHandlerInterceptorFactory extends ExceptionHandlerInterceptorFactory {

	public AIExceptionHandlerInterceptorFactory(ExceptionHandlerFactory parent) {
		super(parent);
	}

	/*
	 * (non-Javadoc)
	 * @see javax.faces.context.ExceptionHandlerFactory#getExceptionHandler()
	 */
	@Override
	public ExceptionHandler getExceptionHandler() {
		final ExceptionHandler result = new AIExceptionHandlerInterceptor(getParent().getExceptionHandler());
		return result;
	}
}
