/* 
 * This file is auto-generated.
 * DO NOT MODIFY MANUALLY!
 * 
 * Generated by AstNodeGenerator.
 * Last generated: 2012-09-26 11:07:49.
 */

package org.sweble.wikitext.parser.nodes;

import de.fau.cs.osr.ptk.common.ast.NodeList;

/**
 * <h1>TableHeader</h1> <h2>Grammar</h2>
 */
public class TableHeader
		extends
			WtInnerNode2

{
	private static final long serialVersionUID = 1L;
	
	// =========================================================================
	
	public TableHeader()
	{
		super(new NodeList(), new NodeList());
		
	}
	
	public TableHeader(NodeList xmlAttributes, NodeList body)
	{
		super(xmlAttributes, body);
		
	}
	
	@Override
	public int getNodeType()
	{
		return org.sweble.wikitext.parser.AstNodeTypes.NT_TABLE_HEADER;
	}
	
	// =========================================================================
	// Properties
	
	// =========================================================================
	// Children
	
	public final void setXmlAttributes(NodeList xmlAttributes)
	{
		set(0, xmlAttributes);
	}
	
	public final NodeList getXmlAttributes()
	{
		return (NodeList) get(0);
	}
	
	public final void setBody(NodeList body)
	{
		set(1, body);
	}
	
	public final NodeList getBody()
	{
		return (NodeList) get(1);
	}
	
	private static final String[] CHILD_NAMES = new String[] { "xmlAttributes", "body" };
	
	public final String[] getChildNames()
	{
		return CHILD_NAMES;
	}
	
	// =========================================================================
	
}