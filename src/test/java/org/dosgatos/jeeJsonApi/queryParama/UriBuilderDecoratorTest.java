package org.dosgatos.jeeJsonApi.queryParama;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

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
				params.addAll((String)args[0], Arrays.copyOfRange(args, 1, args.length));
				return uriBuilder;
			}});
		
	}

	@Test
	public void testSort1() {	
		UriBuilderDecorator test = UriBuilderDecorator.start(uriBuilder);
		test.addSort("1");
		test.addSort("2","3");
		test.addSort(Arrays.asList("1","3","4"));
		assertEquals(0, params.size());
		test.done();
		assertEquals(1, params.size());
		assertTrue(params.containsKey("sort"));
		List<Object> sortValues = params.get("sort");
		assertEquals(1, sortValues.size());
		assertEquals("1,2,3,4", sortValues.get(0));
	}

	@Test
	public void testSort2() {	
		UriBuilderDecorator test = UriBuilderDecorator.start(uriBuilder);
		test.addSort();
		test.done();
		assertEquals(0, params.size());
	}

}
