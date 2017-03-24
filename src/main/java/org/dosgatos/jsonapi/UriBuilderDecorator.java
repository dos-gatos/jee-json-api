package org.dosgatos.jsonapi;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.UriBuilder;

import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;

public class UriBuilderDecorator {

	private UriBuilder uriBuilder;
	private List<String> sorts = new ArrayList<String>();
	
	private UriBuilderDecorator(UriBuilder ub){
		uriBuilder = ub;
	}
	
	public static UriBuilderDecorator start(UriBuilder ub){
		return new UriBuilderDecorator(ub);
	}
	
	private static String SORT_QUERY_PARAM_NAME = "sort";
	public UriBuilder done(){
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
