/*
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 * 
 * Copyright 2006 - 2009
 * 	Julien Baudry
 * 	Antoine Dutot
 * 	Yoann Pigné
 * 	Guilhelm Savin
 */

package org.miv.graphstream.io2;

/**
 * Allows to filter the attribute event stream.
 * 
 * <p>
 * The filtering is based on attribute predicates. An attribute predicate is an object that you
 * provide and that only defines one method {@link AttributePredicate#matches(String, Object)}.
 * If the "matches()" method return false, the attribute is discarded from the event stream, else
 * it is passed to the listeners of this filter.
 * </p>
 * 
 * <p>
 * You can setup a predicate from all attributes (graph, node and edge attributes) and specific
 * predicates for graph, node and edge attributes.
 * </p>
 */
public class AttributesFilter extends IdentityFilter
{
	protected AttributePredicate globalPredicate = null;
	
	protected AttributePredicate graphPredicate = null;
	
	protected AttributePredicate nodePredicate = null;
	
	protected AttributePredicate edgePredicate = null;
	
	/**
	 * Set an attribute filter for graph, node and edge attributes. If the filter is null,
	 * attributes will not be filtered globally.
	 * @param filter The filter to use, it can be null to disable global attribute filtering.
	 */
	public void setGlobalAttributeFilter( AttributePredicate filter )
	{
		globalPredicate = filter;
	}
	
	/**
	 * Set an attribute filter for graph attributes only (node an edge attributes are not filtered
	 * by this filter). If the filter is null, graph attributes will not be filtered.
	 * @param filter The filter to use, it can be null to disable graph attribute filtering.
	 */
	public void setGraphAttributeFilter( AttributePredicate filter )
	{
		graphPredicate = filter;
	}
	
	/**
	 * Set an attribute filter for node attributes only (graph an edge attributes are not filtered
	 * by this filter). If the filter is null, node attributes will not be filtered.
	 * @param filter The filter to use, it can be null to disable node attribute filtering.
	 */
	public void setNodeAttributeFilter( AttributePredicate filter )
	{
		nodePredicate = filter;
	}
	
	/**
	 * Set an attribute filter for edge attributes only (graph an node attributes are not filtered
	 * by this filter). If the filter is null, edge attributes will not be filtered.
	 * @param filter The filter to use, it can be null to disable edge attribute filtering.
	 */
	public void setEdgeAttributeFilter( AttributePredicate filter )
	{
		edgePredicate = filter;
	}

	/**
	 * The filter for all graph, node and edge attributes. This filter can be null.
	 * @return The global attribute filter or null if there is no global filter.
	 */
	public AttributePredicate getGlobalAttributeFilter()
	{
		return globalPredicate;
	}
	
	/**
	 * The filter for all graph attributes. This filter can be null.
	 * @return The graph attribute filter or null if there is no graph filter.
	 */
	public AttributePredicate getGraphAttributeFilter()
	{
		return graphPredicate;
	}
	
	/**
	 * The filter for all node attributes. This filter can be null.
	 * @return The node global attribute filter or null if there is no node filter.
	 */
	public AttributePredicate getNodeAttributeFilter()
	{
		return nodePredicate;
	}
	
	/**
	 * The filter for all edge attributes. This filter can be null.
	 * @return The edge attribute filter or null of there is no edge filter.
	 */
	public AttributePredicate getEdgeAttributeFilter()
	{
		return edgePredicate;
	}
	
// GraphListener
	
	@Override
	public void edgeAttributeAdded( String graphId, String edgeId, String attribute, Object value )
    {
		if( ! edgePredicate.matches( attribute, value ) )
		{
			if( ! globalPredicate.matches( attribute, value ) )
			{
				sendEdgeAttributeAdded( graphId, edgeId, attribute, value );
			}
		}
    }

	@Override
	public void edgeAttributeChanged( String graphId, String edgeId, String attribute,
            Object oldValue, Object newValue )
    {
		if( ! edgePredicate.matches( attribute, newValue ) )
		{
			if( ! globalPredicate.matches( attribute, newValue ) )
			{
				sendEdgeAttributeChanged( graphId, edgeId, attribute, oldValue, newValue );
			}
		}
    }

	@Override
	public void edgeAttributeRemoved( String graphId, String edgeId, String attribute )
    {
		if( ! edgePredicate.matches( attribute, null ) )
		{
			if( ! globalPredicate.matches( attribute, null ) )
			{
				sendEdgeAttributeRemoved( graphId, edgeId, attribute );
			}
		}
    }

	@Override
	public void graphAttributeAdded( String graphId, String attribute, Object value )
    {
		if( ! graphPredicate.matches( attribute, value ) )
		{
			if( ! globalPredicate.matches( attribute, value ) )
			{
				sendGraphAttributeAdded( graphId, attribute, value );
			}
		}
    }

	@Override
	public void graphAttributeChanged( String graphId, String attribute, Object oldValue,
            Object newValue )
    {
		if( ! graphPredicate.matches( attribute, newValue ) )
		{
			if( ! globalPredicate.matches( attribute, newValue ) )
			{
				sendGraphAttributeChanged( graphId, attribute, oldValue, newValue );
			}
		}
    }

	@Override
	public void graphAttributeRemoved( String graphId, String attribute )
    {
		if( ! graphPredicate.matches( attribute, null ) )
		{
			if( ! globalPredicate.matches( attribute, null ) )
			{
				sendGraphAttributeRemoved( graphId, attribute );
			}
		}
    }

	@Override
	public void nodeAttributeAdded( String graphId, String nodeId, String attribute, Object value )
    {
		if( ! nodePredicate.matches( attribute, value ) )
		{
			if( ! globalPredicate.matches( attribute, value ) )
			{
				sendNodeAttributeAdded( graphId, nodeId, attribute, value );
			}
		}
    }

	@Override
	public void nodeAttributeChanged( String graphId, String nodeId, String attribute,
            Object oldValue, Object newValue )
    {
		if( ! nodePredicate.matches( attribute, newValue ) )
		{
			if( ! globalPredicate.matches( attribute, newValue ) )
			{
				sendNodeAttributeChanged( graphId, nodeId, attribute, oldValue, newValue );
			}
		}
    }

	@Override
	public void nodeAttributeRemoved( String graphId, String nodeId, String attribute )
    {
		if( ! nodePredicate.matches( attribute, null ) )
		{
			if( ! globalPredicate.matches( attribute, null ) )
			{
				sendNodeAttributeRemoved( graphId, nodeId, attribute );
			}
		}
    }
}