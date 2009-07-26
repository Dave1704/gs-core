/*
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 */

package org.miv.graphstream.ui2.graphicGraph;

import java.util.Iterator;

import org.miv.graphstream.graph.GraphAttributesListener;
import org.miv.graphstream.graph.Node;
import org.miv.graphstream.ui2.graphicGraph.stylesheet.Selector;
import org.miv.graphstream.ui2.graphicGraph.stylesheet.Style;
import org.miv.graphstream.ui2.graphicGraph.stylesheet.Values;

/**
 * A small gentle sprite.
 */
public class GraphicSprite extends GraphicElement
{
// Attributes
	
	/**
	 * The node this sprite is attached to.
	 */
	protected GraphicNode node;
	
	/**
	 * The edge this sprite is attached to.
	 */
	protected GraphicEdge edge;

	/**
	 * Sprite position.
	 */
	public Values position = new Values( Style.Units.GU, 0, 0, 0 );
	
	/**
	 * Bounds of the sprite (for nodeSelection). These are set by the renderer according to the node
	 * shape. THis is in graph units.
	 */
	public float boundsX, boundsY, boundsZ, boundsW, boundsH, boundsD;
	
// Constructors
	
	/**
	 * New sprite.
	 * @param id The sprite unique identifier.
	 * @param graph The graph containing this sprite.
	 */
	public GraphicSprite( String id, GraphicGraph graph )
	{
		super( id, graph );
		
		// Get the position of a random node.
		
		if( graph.getNodeCount() > 0 )
		{
			Iterator<? extends Node> nodes = graph.getNodeIterator();
		
			GraphicNode node = (GraphicNode) nodes.next();

			position.setValue( 0, node.x );
			position.setValue( 1, node.y );
			position.setValue( 2, node.z );
		}
		
		String myPrefix = String.format( "ui.sprite.%s", id );
		
		if( mygraph.getAttribute( myPrefix ) == null )
			mygraph.addAttribute( myPrefix, position );
	}
	
// Access
	
	/**
	 * The node this sprite is attached to or null if not attached to an edge.
	 * @return A graphic node.
	 */
	public GraphicNode getNodeAttachment()
	{
		return node;
	}
	
	/**
	 * The edge this sprite is attached to or null if not attached to an edge.
	 * @return A graphic edge.
	 */
	public GraphicEdge getEdgeAttachment()
	{
		return edge;
	}
	
	/**
	 * Return the graphic object this sprite is attached to or null if not attached.
	 * @return A graphic object or null if no attachment.
	 */
	public GraphicElement getAttachment()
	{
		GraphicNode n = getNodeAttachment();
		
		if( n != null )
			return n;
		
		return getEdgeAttachment();
	}

	@Override
    protected Selector.Type getSelectorType()
    {
	    return Selector.Type.SPRITE;
    }
	
	@Override
	public float getX()
	{
		return position.get( 0 ); 
	}

	@Override
	public float getY()
	{
		return position.get( 1 );
	}
	
	@Override
	public float getZ()
	{
		return position.get( 2 );
	}
	
	public Style.Units getUnits()
	{
		return position.getUnits();
	}
	
	@Override
	public boolean contains( float x, float y, float z )
	{
		return( x > boundsX && y > boundsY && x < ( boundsX + boundsW ) && y < ( boundsY + boundsH ) );
	}
	
// Commands

	@Override
    public void move( float x, float y, float z )
    {
		setPosition( x, y, z, Style.Units.GU );
    }
	
	@Override
	public void setBounds( float x, float y, float w, float h )
	{
		boundsX = x;
		boundsY = y;
		boundsW = w;
		boundsH = h;
	}
    
	/**
	 * Attach this sprite to the given node.
	 * @param node A graphic node.
	 */
	public void attachToNode( GraphicNode node )
	{
		this.edge = null;
		this.node = node;
		
		String prefix = String.format( "ui.sprite.%s", getId() );
		
		if( this.node.getAttribute( prefix ) == null )
			this.node.addAttribute( prefix );
		
		mygraph.graphChanged = true;
	}
	
	/**
	 * Attach this sprite to the given edge.
	 * @param edge A graphic edge.
	 */
	public void attachToEdge( GraphicEdge edge )
	{
		this.node = null;
		this.edge = edge;
		
		String prefix = String.format( "ui.sprite.%s", getId() );
		
		if( this.edge.getAttribute( prefix ) == null )
			this.edge.addAttribute( prefix );
		
		mygraph.graphChanged = true;
	}
	
	/**
	 * Detach this sprite from the edge or node it was attached to.
	 */
	public void detach()
	{
		String prefix = String.format( "ui.sprite.%s", getId() );
		
		if( this.node != null )
			this.node.removeAttribute( prefix );
		else if( this.edge != null )
			this.edge.removeAttribute( prefix );
		
		this.edge = null;
		this.node = null;
		mygraph.graphChanged = true;
	}
	
	/**
	 * Reposition this sprite.
	 * @param value The coordinate.
	 */
	public void setPosition( float value )
	{
		setPosition( value, 0, 0, getUnits() );
	}
	
	/**
	 * Reposition this sprite.
	 * @param x First coordinate.
	 * @param y Second coordinate.
	 * @param z Third coordinate.
	 * @param units The units to use for lengths and radii, null means "unchanged".
	 */
	public void setPosition( float x, float y, float z, Style.Units units )
	{
		if( node != null )
		{
			y = checkAngle( y );
			z = checkAngle( z );
		}
		else if( edge != null )
		{
			if( x < 0 ) x = 0;
			else if( x > 1 ) x = 1;
		}

		boolean changed = false;
		
		if( getX()     != x     ) { changed = true; position.setValue( 0, x ); }
		if( getY()     != y     ) { changed = true; position.setValue( 1, y ); }
		if( getZ()     != z     ) { changed = true; position.setValue( 2, z ); }
		if( getUnits() != units ) { changed = true; position.setUnits( units ); }

		if( changed )
		{
			mygraph.graphChanged = true;
			String prefix = String.format( "ui.sprite.%s", getId() );
			
			mygraph.setAttribute( prefix, position );
		}
	}
	
	public void setPosition( Values values )
	{
		float x = 0;
		float y = 0;
		float z = 0;
		
		if( values.getValueCount() > 0 ) x = values.get( 0 ); 
		if( values.getValueCount() > 1 ) y = values.get( 1 ); 
		if( values.getValueCount() > 2 ) z = values.get( 2 ); 
		
//System.err.printf( "setting %s position x=%f y=%f z=%f units=%s (value in=%s)%n", getId(), x, y, z, values.units, values );
		if( x == 1 && y == 1 && z == 1 )
			throw new RuntimeException( "WTF !!!" );
		setPosition( x, y, z, values.units );
	}
	
	protected float checkAngle( double angle )
	{
		if( angle > Math.PI*2 )
			angle = angle % (Math.PI*2);
		else if( angle < 0 )
			angle = (Math.PI*2) - ( angle % (Math.PI*2) );
		
		return (float) angle;
	}
	
	@Override
    protected void attributeChanged( String attribute, AttributeChangeEvent event, Object oldValue, Object newValue )
    {
		super.attributeChanged( attribute, event, oldValue, newValue );

		String completeAttr = String.format( "ui.sprite.%s.%s", getId(), attribute );
//System.err.printf( "GSprite add attribute %s %s (old=%s) (new=%s)%n", event, attribute, oldValue, newValue );
		if( event == AttributeChangeEvent.ADD )		// ADD
		{
			Object o = mygraph.getAttribute( completeAttr );
			
			if( o == null || ( ! o.equals( newValue ) ) )
			{
				for( GraphAttributesListener listener: mygraph.attrListeners )
					listener.graphAttributeAdded( mygraph.getId(), completeAttr, newValue );
			}
		}
		else if( event == AttributeChangeEvent.REMOVE )	// REMOVE
		{
			for( GraphAttributesListener listener: mygraph.attrListeners )
				listener.graphAttributeRemoved( mygraph.getId(), completeAttr );			
		}
		else						// CHANGE
		{
			Object o = mygraph.getAttribute( completeAttr );

			if( o == null || ( ! o.equals( newValue ) ) )
			{
				for( GraphAttributesListener listener: mygraph.attrListeners )
					listener.graphAttributeChanged( mygraph.getId(), completeAttr, oldValue, newValue );
			}
		}
    }

	@Override
    protected void removed()
    {
    }
}