package org.dosgatos.jsonapi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;

import com.google.common.base.Joiner;

public class JsonApiParamsDecorator {

	private List<String> sort = new ArrayList<String>();
	private Map<String,String> params = new HashMap<String,String>();
	//page
	private Set<String> include = new HashSet<String>();
	
	private JsonApiParamsDecorator(){}
	
	public static JsonApiParamsDecorator start(){
		return new JsonApiParamsDecorator();
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
			uriBuilder.replaceQueryParam(SORT_QUERY_PARAM_NAME, sortString());
		}
		if(!include.isEmpty()){
			uriBuilder.replaceQueryParam(INCLUDE_QUERY_PARAM_NAME, includeString());
		}
		if(!params.isEmpty()){
			params.forEach((k, v) -> uriBuilder.replaceQueryParam(k, v));
		}
		return uriBuilder;
	}
	
	public Map<String, List<String>> decorate(Map<String, List<String>> mvm){
		if(!sort.isEmpty()){
			mvm.replace(SORT_QUERY_PARAM_NAME, Arrays.asList(sortString()));
		}
		if(!include.isEmpty()){
			mvm.replace(INCLUDE_QUERY_PARAM_NAME, Arrays.asList(includeString()));
		}
		if(!params.isEmpty()){
			params.forEach((k, v) -> mvm.replace(k, Arrays.asList(v)));
		}		
		return mvm;
	}
	
	public MultivaluedMap<String, String> decorate(MultivaluedMap<String, String> mvm){
		if(!sort.isEmpty()){
			mvm.replace(SORT_QUERY_PARAM_NAME, Arrays.asList(sortString()));
		}
		if(!include.isEmpty()){
			mvm.replace(INCLUDE_QUERY_PARAM_NAME, Arrays.asList(includeString()));
		}
		if(!params.isEmpty()){
			params.forEach((k, v) -> mvm.replace(k, Arrays.asList(v)));
		}		
		return mvm;
	}
	
	public JsonApiParamsDecorator addSort(Stream<String> stream){
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

	
	public JsonApiParamsDecorator addSort(String ... args){
		return addSort(Stream.of(args));
	}
	
	public JsonApiParamsDecorator addSort(List<String> l){
		if(l != null && !l.isEmpty()){
			addSort(l.stream());
		}
		return this;
	}
	
	public JsonApiParamsDecorator addInclude(Stream<String> stream){
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

	
	public JsonApiParamsDecorator addInclude(String ... args){
		return addInclude(Stream.of(args));
	}
	
	public JsonApiParamsDecorator addInclude(List<String> l){
		if(l != null && !l.isEmpty()){
			addInclude(l.stream());
		}
		return this;
	}
	
	private String sortString(){
		return Joiner.on(",").join(sort);
	}
	
	private String includeString(){
		return Joiner.on(",").join(include);
	}
	
	private JsonApiParamsDecorator loadParam(String keyType, String key, String value){
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
	
	public JsonApiParamsDecorator addFields(String key, String value){
		return loadParam(FIELDS_QUERY_PARAM_NAME, key, value);
	}
	
	public JsonApiParamsDecorator addFilter(String key, String value){
		return loadParam(FILTER_QUERY_PARAM_NAME, key, value);
	}
	
	private JsonApiParamsDecorator addPage(String key, String value){
		return loadParam(PAGE_QUERY_PARAM_PREFIX, key, value);
	}
	
	public JsonApiParamsDecorator addPageSize(String value){
		return addPage(PAGE_SIZE_QUERY_PARAM, value);
	}
	
	public JsonApiParamsDecorator addPageNumber(String value){
		return addPage(PAGE_NUMBER_QUERY_PARAM, value);
	}

	public JsonApiParamsDecorator addPageOffset(String value){
		return addPage(PAGE_OFFSET_QUERY_PARAM, value);
	}

	public JsonApiParamsDecorator addPageLimit(String value){
		return addPage(PAGE_LIMIT_QUERY_PARAM, value);
	}

	public JsonApiParamsDecorator addPageCursor(String value){
		return addPage(PAGE_CURSOR_QUERY_PARAM, value);
	}
	
}
