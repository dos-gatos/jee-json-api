package org.dosgatos.jsonapi;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.UriBuilder;

import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;

public class UriBuilderDecorator {

	private List<String> sorts = new ArrayList<String>();
	
	private UriBuilderDecorator(){}
	
	public static UriBuilderDecorator start(){
		return new UriBuilderDecorator();
	}
	
	private static String SORT_QUERY_PARAM_NAME = "sort";
	public UriBuilder decorate(UriBuilder uriBuilder){
		if(!sorts.isEmpty()){
			uriBuilder.replaceQueryParam(SORT_QUERY_PARAM_NAME, Joiner.on(",").join(sorts));
		}
		return uriBuilder;
	}
	
	public UriBuilderDecorator addSort(String ... arg){
		for(String s: arg){
			if(!sorts.contains(s)){
				sorts.add(s);
			}
		}
		return this;
	}
	
	public UriBuilderDecorator addSort(List<String> l){
		addSort(Iterables.toArray(l, String.class));
		return this;
	}
}
