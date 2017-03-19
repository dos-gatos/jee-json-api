package org.dosgatos.jeeJsonApi.queryParama.zoth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import org.dosgatos.jeeJsonApi.queryParama.zoth.SortQueryParam.Order;

public class JsonApiQuery {
	private MultivaluedMap<String,String> mm;
	
	public JsonApiQuery(UriInfo uriInfo){
		mm = uriInfo.getQueryParameters();
	}
	
	private static String SORT = "sort";
	private List<SortQueryParam> sort = null;
	
	public List<SortQueryParam> sort(){
		if(sort == null){
			sort = new ArrayList<SortQueryParam>();
			List<String> list = readCommaList(mm.getFirst(SORT));
			for(String field: list){
				Order order = Order.DESC;
				if(field.startsWith("+")){
					field = field.substring(1);
				}else if(field.startsWith("-")){
					order = Order.ASC;
					field = field.substring(1);
				}
				sort.add(sortQueryParam(order, field));
			}
		}
		return sort;
	}
	
	private SortQueryParam sortQueryParam(Order order, String field){
		return new SortQueryParam(){

			@Override
			public Order order() {
				return order;
			}

			@Override
			public String field() {
				return field;
			}
			
		};
	}
	
	private static String INCLUDE = "include";
	private List<String> include = null;
	
	public List<String> include(){
		if(include == null){
			include = readCommaList(mm.getFirst(INCLUDE));
		}
		return include;
	}
	
	private static String PAGE_PERFIX = "page[";
	private static String PAGE_NUMBER = "number";
	private static String PAGE_SIZE = "size";
	private static String PAGE_OFFSET = "offset";
	private static String PAGE_LIMIT = "limit";
	
	private PageQueryParams page = null;
	
	public PageQueryParams page(){
		if(page == null){
			MultivaluedMap<String,String> pmm = read(PAGE_PERFIX);
			page = new PageQueryParams(){

				@Override
				public String getNumber() {
					return pmm.getFirst(PAGE_NUMBER);
				}

				@Override
				public String getSize() {
					return pmm.getFirst(PAGE_SIZE);
				}

				@Override
				public String getOffset() {
					return pmm.getFirst(PAGE_OFFSET);
				}

				@Override
				public String getLimit() {
					return pmm.getFirst(PAGE_LIMIT);
				}
			};
			
		}
		return page;
	}

	private static String FIELDS_PERFIX = "fields[";	
	private MultivaluedMap<String,String> fields = null;
	
	public MultivaluedMap<String,String> fields(){
		if(fields == null){
			fields = read(FIELDS_PERFIX);
		}
		return fields;
	}

	private static String FILTER_PERFIX = "filter[";	
	private MultivaluedMap<String,String> filter = null;
	
	public MultivaluedMap<String,String> filter(){
		if(filter == null){
			filter = read(FILTER_PERFIX);
		}
		return filter;
	}
	
	private static String SUFFIX = "]";
	
	private MultivaluedMap<String,String> read(String prefix){
		MultivaluedHashMap<String, String> result = new MultivaluedHashMap<String, String>();
		int startPos = prefix.length();
		for(String mmkey: mm.keySet()){
			if(mmkey.startsWith(prefix) && mmkey.endsWith(SUFFIX)){
				String key = mmkey.substring(startPos, mmkey.length() - 1);
				result.addAll(key, readCommaList(mm.getFirst(mmkey)));	
			}
		}		
		return result;
	}
	
	private List<String> readCommaList(String s){
		if(s == null || s.isEmpty()){
			return new ArrayList<String>();
		}
		return Arrays.asList(s.split(","));
	}
	
}
