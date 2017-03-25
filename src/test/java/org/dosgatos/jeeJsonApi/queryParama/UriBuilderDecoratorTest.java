package org.dosgatos.jeeJsonApi.queryParama;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.UriBuilder;

import org.junit.Test;
import org.dosgatos.jsonapi.UriBuilderDecorator;
import org.junit.Before;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class UriBuilderDecoratorTest {
	
	private MultivaluedHashMap<String,Object> params;
	private UriBuilder uriBuilder;
	
	@Before
	public void setup(){

		params = new MultivaluedHashMap<String,Object>();
		uriBuilder = mock(UriBuilder.class);
		when(uriBuilder.replaceQueryParam(anyString(), anyVararg())).thenAnswer(new Answer<UriBuilder>(){

			@Override
			public UriBuilder answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				params.remove((String)args[0]);
				params.addAll((String)args[0], Arrays.copyOfRange(args, 1, args.length));
				return uriBuilder;
			}});
		
	}

	@Test
	public void testSort1(){	
		uriBuilder.replaceQueryParam("sort", "bad");
		assertEquals("bad", params.get("sort").get(0));
		UriBuilderDecorator.start().
		addSort("1").
		addSort("2","3").
		addSort(Arrays.asList("1","3","4")).
		decorate(uriBuilder);
		assertEquals(1, params.size());
		assertTrue(params.containsKey("sort"));
		List<Object> sortValues = params.get("sort");
		assertEquals(1, sortValues.size());
		assertEquals("1,2,3,4", sortValues.get(0));
	}

	@Test
	public void testSort2(){	
		UriBuilderDecorator.start().
		addSort().
		decorate(uriBuilder);
		assertEquals(0, params.size());
	}
	
	@Test
	public void testInclude1(){	
		uriBuilder.replaceQueryParam("include", "bad");
		assertEquals("bad", params.get("include").get(0));
		UriBuilderDecorator.start().
		addInclude("1").
		addInclude("2","3").
		addInclude(Arrays.asList("1","3","4")).
		decorate(uriBuilder);
		assertEquals(1, params.size());
		assertTrue(params.containsKey("include"));
		List<Object> sortValues = params.get("include");
		assertEquals(1, sortValues.size());
		List<String> tests = Arrays.asList(sortValues.get(0).toString().split(","));
		assertEquals(4,tests.size());
		assertTrue(tests.containsAll(Arrays.asList("1","2","3","4")));
	}

	@Test
	public void testInclude2(){	
		UriBuilderDecorator.start().
		addInclude().
		decorate(uriBuilder);
		assertEquals(0, params.size());
	}
	
	@Test
	public void testParams(){
		UriBuilderDecorator.start().
		addFields("a", "av1").
		addFields("b", "bv1").
		addFields("a", "av2").
		addFilter("a", "fra").
		addFilter("b", "frb").
		addPageCursor("cur").
		addPageLimit("lim").
		addPageNumber("num").
		addPageOffset("off").
		addPageSize("siz").
		decorate(uriBuilder);
		
		assertEquals(9, params.size());
		assertEquals("lim", params.getFirst("page[limit]"));
		assertEquals("num", params.getFirst("page[number]"));
		assertEquals("off", params.getFirst("page[offset]"));
		assertEquals("fra", params.getFirst("filter[a]"));
		assertEquals("cur", params.getFirst("page[cursor]"));
		assertEquals("av2", params.getFirst("fields[a]"));
		assertEquals("frb", params.getFirst("filter[b]"));
		assertEquals("bv1", params.getFirst("fields[b]"));
		assertEquals("siz", params.getFirst("page[size]"));


	}


}
