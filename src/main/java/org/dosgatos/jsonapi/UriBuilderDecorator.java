package org.dosgatos.jsonapi;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import javax.ws.rs.core.UriBuilder;

import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;

public class UriBuilderDecorator {

	private List<String> sort = new ArrayList<String>();
	//fields
	//filter
	//page
	private Set<String> include = new HashSet<String>();
	
	private UriBuilderDecorator(){}
	
	public static UriBuilderDecorator start(){
		return new UriBuilderDecorator();
	}
	
	private static String SORT_QUERY_PARAM_NAME = "sort";
	private static String INCLUDE_QUERY_PARAM_NAME = "include";
	
	public UriBuilder decorate(UriBuilder uriBuilder){
		if(!sort.isEmpty()){
			uriBuilder.replaceQueryParam(SORT_QUERY_PARAM_NAME, Joiner.on(",").join(sort));
		}
		if(!include.isEmpty()){
			uriBuilder.replaceQueryParam(INCLUDE_QUERY_PARAM_NAME, Joiner.on(",").join(include));
		}
		return uriBuilder;
	}
	
	public UriBuilderDecorator addSort(String ... args){
		Stream.of(args).forEach(s -> loadSort(s));
		return this;
	}
	
	private void loadSort(String s){
		s = s != null ? s.trim() : s;
		if(s != null && !s.isEmpty()){
			if(!sort.contains(s)){
				sort.add(s);
			}
		}
	}
	
	public UriBuilderDecorator addSort(List<String> l){
		l.stream().forEach(s -> loadSort(s));
		return this;
	}
	
	public UriBuilderDecorator addInclude(String ... args){
		Stream.of(args).forEach(s -> loadInclude(s));
		return this;
	}
	
	public UriBuilderDecorator addInclude(List<String> l){
		l.stream().forEach(s -> loadInclude(s));
		return this;
	}

	private void loadInclude(String s){
		s = s != null ? s.trim() : s;
		if(s != null && !s.isEmpty()){
			include.add(s);
		}
	}
}
