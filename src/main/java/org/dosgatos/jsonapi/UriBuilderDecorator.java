package org.dosgatos.jsonapi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import javax.ws.rs.core.UriBuilder;

import com.google.common.base.Joiner;

public class UriBuilderDecorator {

	private List<String> sort = new ArrayList<String>();
	private Map<String,String> params = new HashMap<String,String>();
	//page
	private Set<String> include = new HashSet<String>();
	
	private UriBuilderDecorator(){}
	
	public static UriBuilderDecorator start(){
		return new UriBuilderDecorator();
	}
	
	private static String SORT_QUERY_PARAM_NAME = "sort";
	private static String INCLUDE_QUERY_PARAM_NAME = "include";
	private static String FILTER_QUERY_PARAM_NAME = "filter";
	private static String FIELDS_QUERY_PARAM_NAME = "fields";
	private static String PAGE_QUERY_PARAM_PREFIX = "page";
	private static String PAGE_SIZE_QUERY_PARAM = "size";
	private static String PAGE_NUMBER_QUERY_PARAM = "number";
	private static String PAGE_OFFSET_QUERY_PARAM = "offset";
	private static String PAGE_LIMIT_QUERY_PARAM = "limit";
	private static String PAGE_CURSOR_QUERY_PARAM = "cursor";
	
	public UriBuilder decorate(UriBuilder uriBuilder){
		if(!sort.isEmpty()){
			uriBuilder.replaceQueryParam(SORT_QUERY_PARAM_NAME, Joiner.on(",").join(sort));
		}
		if(!include.isEmpty()){
			uriBuilder.replaceQueryParam(INCLUDE_QUERY_PARAM_NAME, Joiner.on(",").join(include));
		}
		if(!params.isEmpty()){
			params.forEach((k, v) -> uriBuilder.replaceQueryParam(k, v));
		}
		return uriBuilder;
	}
	
	public UriBuilderDecorator addSort(Stream<String> stream){
		if(stream != null){
			stream.forEach(s -> {
				s = s != null ? s.trim() : s;
				if(s != null && !s.isEmpty()){
					if(!sort.contains(s)){
						sort.add(s);
					}
				}
			});
		}
		return this;
	}

	
	public UriBuilderDecorator addSort(String ... args){
		return addSort(Stream.of(args));
	}
	
	public UriBuilderDecorator addSort(List<String> l){
		if(l != null && !l.isEmpty()){
			addSort(l.stream());
		}
		return this;
	}
	
	public UriBuilderDecorator addInclude(Stream<String> stream){
		if(stream != null){
			stream.forEach(s -> {
				s = s != null ? s.trim() : s;
				if(s != null && !s.isEmpty()){
					include.add(s);
				}
			});
		}
		return this;
	}

	
	public UriBuilderDecorator addInclude(String ... args){
		return addInclude(Stream.of(args));
	}
	
	public UriBuilderDecorator addInclude(List<String> l){
		if(l != null && !l.isEmpty()){
			addInclude(l.stream());
		}
		return this;
	}
	
	private UriBuilderDecorator loadParam(String keyType, String key, String value){
		value = value != null ? value.trim() : value;
		if(value != null && !value.isEmpty()){
			key = key != null ? key.trim() : key;
			if(key != null && !key.isEmpty()){
				String fullKey = String.format("%s[%s]", keyType, key);
				params.put(fullKey, value);
			}
		}
		return this;
	}
	
	public UriBuilderDecorator addFields(String key, String value){
		return loadParam(FIELDS_QUERY_PARAM_NAME, key, value);
	}
	
	public UriBuilderDecorator addFilter(String key, String value){
		return loadParam(FILTER_QUERY_PARAM_NAME, key, value);
	}
	
	private UriBuilderDecorator addPage(String key, String value){
		return loadParam(PAGE_QUERY_PARAM_PREFIX, key, value);
	}
	
	public UriBuilderDecorator addPageSize(String value){
		return addPage(PAGE_SIZE_QUERY_PARAM, value);
	}
	
	public UriBuilderDecorator addPageNumber(String value){
		return addPage(PAGE_NUMBER_QUERY_PARAM, value);
	}

	public UriBuilderDecorator addPageOffset(String value){
		return addPage(PAGE_OFFSET_QUERY_PARAM, value);
	}

	public UriBuilderDecorator addPageLimit(String value){
		return addPage(PAGE_LIMIT_QUERY_PARAM, value);
	}

	public UriBuilderDecorator addPageCursor(String value){
		return addPage(PAGE_CURSOR_QUERY_PARAM, value);
	}
	
}
