package org.dosgatos.jeeJsonApi.queryParama.zoth;

public interface SortQueryParam {
	public enum Order {ASC, DESC}
	
	public Order order();
	public String field();
}
