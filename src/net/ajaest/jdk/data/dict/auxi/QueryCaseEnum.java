package net.ajaest.jdk.data.dict.auxi;

public enum QueryCaseEnum {

	EQUALS, NOT_EQUALS, GREATER_THAN, GREATER_OR_EQUALS_THAN, LESS_THAN, LESS_OR_EQUALS_THAN, NULL,
	/**
	 * if there is a query nested in a query(parenthesis)
	 */
	QUERY;
}
