package com.km.milonga.rhino;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.mozilla.javascript.NativeFunction;
import org.springframework.web.servlet.ModelAndView;

public class JsgiStyleCheckPolicy extends ArgumentCheckPolicy {

	public JsgiStyleCheckPolicy(int argumentsCount, Object[] args) {
		super(argumentsCount, args);
	}

	@Override
	public boolean isValidated(NativeFunction atmosHandler) {
		String encodedSource = atmosHandler.getEncodedSource();
		return atmosHandler.getLength() == 2
				&& encodedSource.indexOf("request") < 10
				&& encodedSource.indexOf("response") < 20;
	}

	@Override
	public ModelAndView apply(NativeFunction atmosHandler) {
		ModelAndView mav = new ModelAndView();

		atmosHandler.call(context, scope, scope, args);
		HttpServletRequest request = (HttpServletRequest) args[0];
		// HttpServletResponse response = (HttpServletResponse) args[1];
		Enumeration<String> attributeNames = request.getAttributeNames();
		while (attributeNames.hasMoreElements()) {
			String attributeName = attributeNames.nextElement();
			mav.addObject(attributeName, request.getAttribute(attributeName));
		}

		return mav;
	}

}
