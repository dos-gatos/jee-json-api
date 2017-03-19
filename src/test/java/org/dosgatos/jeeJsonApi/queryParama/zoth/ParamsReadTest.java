package org.dosgatos.jeeJsonApi.queryParama.zoth;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import org.dosgatos.jeeJsonApi.queryParama.zoth.SortQueryParam.Order;
import org.junit.Test;

public class ParamsReadTest {

	@Test
	public void test() {
		JsonApiQuery jsonApiQuery = new JsonApiQuery(uriInfo());
		
		List<SortQueryParam> sort = jsonApiQuery.sort();
		assertEquals(2, sort.size());
		assertEquals("created", sort.get(0).field());
		assertEquals(Order.ASC, sort.get(0).order());
		assertEquals("title", sort.get(1).field());
		assertEquals(Order.DESC, sort.get(1).order());
		
		MultivaluedMap<String, String> filterMm = jsonApiQuery.filter();
		assertEquals(2, filterMm.size());
		List<String> filter = filterMm.get("post");
		assertEquals(2, filter.size());
		assertEquals("1", filter.get(0));
		assertEquals("2", filter.get(1));
		filter = filterMm.get("author");
		assertEquals(1, filter.size());
		assertEquals("12", filter.get(0));
		
		MultivaluedMap<String, String> fieldsMm = jsonApiQuery.fields();
		assertEquals(2, fieldsMm.size());
		List<String> fields = fieldsMm.get("articles");
		assertEquals(2, fields.size());
		assertEquals("title", fields.get(0));
		assertEquals("body", fields.get(1));
		fields = fieldsMm.get("people");
		assertEquals(1, fields.size());
		assertEquals("name", fields.get(0));
		
		PageQueryParams pageQP = jsonApiQuery.page();
		assertEquals("7", pageQP.getNumber());
		assertEquals("20", pageQP.getSize());
		assertEquals("12", pageQP.getOffset());
		assertEquals("30", pageQP.getLimit());
	}

	private UriInfo uriInfo() {

		MultivaluedHashMap<String, String> mm = new MultivaluedHashMap<String, String>();
		mm.addAll("sort", Arrays.asList("-created,title"));
		mm.addAll("include", Arrays.asList("author,comments.author"));
		mm.addAll("fields[articles]", Arrays.asList("title,body"));
		mm.addAll("fields[people]", Arrays.asList("name"));

		mm.addAll("page[number]", Arrays.asList("7"));
		mm.addAll("page[size]", Arrays.asList("20"));
		mm.addAll("page[offset]", Arrays.asList("12"));
		mm.addAll("page[limit]", Arrays.asList("30"));

		mm.addAll("filter[post]", Arrays.asList("1,2"));
		mm.addAll("filter[author]", Arrays.asList("12"));

		UriInfo uriInfo = mock(UriInfo.class);
		when(uriInfo.getQueryParameters()).thenReturn(mm);

		return uriInfo;

	}

}
