package org.dosgatos.jeeJsonApi.queryParama.zoth;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.HashSet;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import org.junit.Ignore;
import org.junit.Test;

public class ParamsReadTest {

	@Ignore
	@Test
	public void test() {
		fail("Not yet implemented");
	}
	
	private UriInfo uriInfo(){
		  MultivaluedMap<String,String> mm = (MultivaluedMap<String,String>) mock(MultivaluedMap.class);
		  
		  when(mm.keySet()).thenReturn(new HashSet<String>(
		      Arrays.asList("sort", "include",
		          "fields[articles]", "fields[people]",
		          "page[number]", "page[size]", "page[offset]", "page[limit]",
		          "filter[post]", "filter[author]")));
		  
		  when(mm.get("sort")).thenReturn(Arrays.asList("-created,title"));
		  when(mm.get("include")).thenReturn(Arrays.asList("author,comments.author"));
		  
		  when(mm.get("fields[articles]")).thenReturn(Arrays.asList("title,body"));
		  when(mm.get("fields[people]")).thenReturn(Arrays.asList("name"));
		  
		  when(mm.get("page[number]")).thenReturn(Arrays.asList("7"));
		  when(mm.get("page[size]")).thenReturn(Arrays.asList("20"));
		  when(mm.get("page[offset]")).thenReturn(Arrays.asList("12"));
		  when(mm.get("page[limit]")).thenReturn(Arrays.asList("30"));
		  
		  when(mm.get("filter[post]")).thenReturn(Arrays.asList("1,2"));
		  when(mm.get("filter[author]")).thenReturn(Arrays.asList("12"));		
		  
		  UriInfo uriInfo = mock(UriInfo.class);
		  when(uriInfo.getQueryParameters()).thenReturn(mm);
		  
		  return uriInfo;

		}

}
