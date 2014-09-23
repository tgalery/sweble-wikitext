/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */
package org.sweble.wom3.swcadapter;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;

import org.joda.time.DateTime;
import org.sweble.wikitext.engine.PageTitle;
import org.sweble.wikitext.engine.config.WikiConfig;
import org.sweble.wikitext.engine.nodes.CompleteEngineVisitor;
import org.sweble.wikitext.engine.nodes.EngNowiki;
import org.sweble.wikitext.engine.nodes.EngPage;
import org.sweble.wikitext.engine.nodes.EngProcessedPage;
import org.sweble.wikitext.engine.nodes.EngSoftErrorNode;
import org.sweble.wikitext.engine.nodes.EngineNodeFactory;
import org.sweble.wikitext.engine.utils.EngineAstTextUtils;
import org.sweble.wikitext.parser.WtRtData;
import org.sweble.wikitext.parser.nodes.WtBody;
import org.sweble.wikitext.parser.nodes.WtBold;
import org.sweble.wikitext.parser.nodes.WtContentNode;
import org.sweble.wikitext.parser.nodes.WtDefinitionList;
import org.sweble.wikitext.parser.nodes.WtDefinitionListDef;
import org.sweble.wikitext.parser.nodes.WtDefinitionListTerm;
import org.sweble.wikitext.parser.nodes.WtExternalLink;
import org.sweble.wikitext.parser.nodes.WtHeading;
import org.sweble.wikitext.parser.nodes.WtHorizontalRule;
import org.sweble.wikitext.parser.nodes.WtIgnored;
import org.sweble.wikitext.parser.nodes.WtIllegalCodePoint;
import org.sweble.wikitext.parser.nodes.WtImEndTag;
import org.sweble.wikitext.parser.nodes.WtImStartTag;
import org.sweble.wikitext.parser.nodes.WtImageLink;
import org.sweble.wikitext.parser.nodes.WtImageLink.ImageHorizAlign;
import org.sweble.wikitext.parser.nodes.WtImageLink.ImageVertAlign;
import org.sweble.wikitext.parser.nodes.WtImageLink.ImageViewFormat;
import org.sweble.wikitext.parser.nodes.WtInternalLink;
import org.sweble.wikitext.parser.nodes.WtItalics;
import org.sweble.wikitext.parser.nodes.WtLinkOptionAltText;
import org.sweble.wikitext.parser.nodes.WtLinkOptionGarbage;
import org.sweble.wikitext.parser.nodes.WtLinkOptionKeyword;
import org.sweble.wikitext.parser.nodes.WtLinkOptionLinkTarget;
import org.sweble.wikitext.parser.nodes.WtLinkOptionResize;
import org.sweble.wikitext.parser.nodes.WtLinkOptions;
import org.sweble.wikitext.parser.nodes.WtLinkTarget.LinkTargetType;
import org.sweble.wikitext.parser.nodes.WtLinkTitle;
import org.sweble.wikitext.parser.nodes.WtListItem;
import org.sweble.wikitext.parser.nodes.WtName;
import org.sweble.wikitext.parser.nodes.WtNewline;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtNodeList;
import org.sweble.wikitext.parser.nodes.WtOnlyInclude;
import org.sweble.wikitext.parser.nodes.WtOrderedList;
import org.sweble.wikitext.parser.nodes.WtPageName;
import org.sweble.wikitext.parser.nodes.WtPageSwitch;
import org.sweble.wikitext.parser.nodes.WtParagraph;
import org.sweble.wikitext.parser.nodes.WtParsedWikitextPage;
import org.sweble.wikitext.parser.nodes.WtPreproWikitextPage;
import org.sweble.wikitext.parser.nodes.WtRedirect;
import org.sweble.wikitext.parser.nodes.WtSection;
import org.sweble.wikitext.parser.nodes.WtSemiPre;
import org.sweble.wikitext.parser.nodes.WtSemiPreLine;
import org.sweble.wikitext.parser.nodes.WtSignature;
import org.sweble.wikitext.parser.nodes.WtTable;
import org.sweble.wikitext.parser.nodes.WtTableCaption;
import org.sweble.wikitext.parser.nodes.WtTableCell;
import org.sweble.wikitext.parser.nodes.WtTableHeader;
import org.sweble.wikitext.parser.nodes.WtTableImplicitTableBody;
import org.sweble.wikitext.parser.nodes.WtTableRow;
import org.sweble.wikitext.parser.nodes.WtTagExtension;
import org.sweble.wikitext.parser.nodes.WtTagExtensionBody;
import org.sweble.wikitext.parser.nodes.WtTemplate;
import org.sweble.wikitext.parser.nodes.WtTemplateArgument;
import org.sweble.wikitext.parser.nodes.WtTemplateArguments;
import org.sweble.wikitext.parser.nodes.WtTemplateParameter;
import org.sweble.wikitext.parser.nodes.WtText;
import org.sweble.wikitext.parser.nodes.WtTicks;
import org.sweble.wikitext.parser.nodes.WtUnorderedList;
import org.sweble.wikitext.parser.nodes.WtUrl;
import org.sweble.wikitext.parser.nodes.WtValue;
import org.sweble.wikitext.parser.nodes.WtWhitespace;
import org.sweble.wikitext.parser.nodes.WtXmlAttribute;
import org.sweble.wikitext.parser.nodes.WtXmlAttributeGarbage;
import org.sweble.wikitext.parser.nodes.WtXmlAttributes;
import org.sweble.wikitext.parser.nodes.WtXmlCharRef;
import org.sweble.wikitext.parser.nodes.WtXmlComment;
import org.sweble.wikitext.parser.nodes.WtXmlElement;
import org.sweble.wikitext.parser.nodes.WtXmlEmptyTag;
import org.sweble.wikitext.parser.nodes.WtXmlEndTag;
import org.sweble.wikitext.parser.nodes.WtXmlEntityRef;
import org.sweble.wikitext.parser.nodes.WtXmlStartTag;
import org.sweble.wikitext.parser.parser.LinkTargetException;
import org.sweble.wikitext.parser.postprocessor.WtNodeFlags;
import org.sweble.wikitext.parser.utils.StringConversionException;
import org.sweble.wikitext.parser.utils.WtRtDataPrinter;
import org.sweble.wom3.Wom3Article;
import org.sweble.wom3.Wom3Attribute;
import org.sweble.wom3.Wom3Comment;
import org.sweble.wom3.Wom3Document;
import org.sweble.wom3.Wom3Element;
import org.sweble.wom3.Wom3ElementNode;
import org.sweble.wom3.Wom3ExtLink;
import org.sweble.wom3.Wom3For;
import org.sweble.wom3.Wom3Image;
import org.sweble.wom3.Wom3ImageFormat;
import org.sweble.wom3.Wom3ImageHAlign;
import org.sweble.wom3.Wom3ImageVAlign;
import org.sweble.wom3.Wom3IntLink;
import org.sweble.wom3.Wom3Node;
import org.sweble.wom3.Wom3Nowiki;
import org.sweble.wom3.Wom3Paragraph;
import org.sweble.wom3.Wom3Redirect;
import org.sweble.wom3.Wom3Repl;
import org.sweble.wom3.Wom3Rtd;
import org.sweble.wom3.Wom3Section;
import org.sweble.wom3.Wom3Signature;
import org.sweble.wom3.Wom3SignatureFormat;
import org.sweble.wom3.Wom3Subst;
import org.sweble.wom3.Wom3Table;
import org.sweble.wom3.Wom3TableCaption;
import org.sweble.wom3.Wom3Text;
import org.sweble.wom3.Wom3Title;
import org.sweble.wom3.Wom3XmlText;
import org.sweble.wom3.impl.DocumentImpl;
import org.sweble.wom3.impl.DomImplementationImpl;
import org.sweble.wom3.impl.Toolbox;
import org.sweble.wom3.swcadapter.nodes.SwcNode;
import org.w3c.dom.Document;

import de.fau.cs.osr.ptk.common.AstVisitor;
import de.fau.cs.osr.utils.XmlGrammar;

public class AstToWomConverter
		extends
			AstVisitor<WtNode>
		implements
			CompleteEngineVisitor<Wom3Node>
{
	public static final String MWW_NS_URI = SwcNode.MWW_NS_URI;
	
	public static final String DEFAULT_MWW_NS_PREFIX = SwcNode.DEFAULT_MWW_NS_PREFIX;
	
	// =========================================================================
	
	private final WikiConfig wikiConfig;
	
	private final Map<String, HtmlElement> xhtmlElems;
	
	private final EngineAstTextUtils astTextUtils;
	
	private final EngineNodeFactory nodeFactory;
	
	// =========================================================================
	// Configuration
	
	private boolean preserveRtd = true;
	
	private boolean suppressRtd = false;
	
	private boolean addSubst = true;
	
	// =========================================================================
	// Per AST/document state
	
	private LinkedList<Wom3ElementNode> stack = new LinkedList<Wom3ElementNode>();
	
	private PageTitle pageTitle;
	
	private String author;
	
	private DateTime timestamp;
	
	private Document doc;
	
	private Wom3Article page;
	
	private int displacement = 0;
	
	private boolean inNoTextScope;
	
	// =========================================================================
	
	public AstToWomConverter(WikiConfig config)
	{
		this.wikiConfig = config;
		
		this.xhtmlElems = new HashMap<String, HtmlElement>();
		for (HtmlElement e : HtmlElement.values())
			xhtmlElems.put(e.name().toLowerCase(), e);
		
		this.astTextUtils = wikiConfig.getAstTextUtils();
		this.nodeFactory = wikiConfig.getNodeFactory();
	}
	
	// =========================================================================
	
	public static Wom3Document convert(
			WikiConfig config,
			PageTitle pageTitle,
			String author,
			DateTime timestamp,
			WtNode ast)
	{
		Wom3Document doc = DomImplementationImpl.get().createDocument(
				Wom3ElementNode.WOM_NS_URI, "article", null);
		
		AstToWomConverter converter = new AstToWomConverter(config);
		
		converter.convert(doc, pageTitle, author, timestamp, ast);
		
		return doc;
	}
	
	public Wom3Document convert(
			PageTitle pageTitle,
			String author,
			DateTime timestamp,
			WtNode ast)
	{
		DocumentImpl doc = DomImplementationImpl.get().createDocument(
				Wom3ElementNode.WOM_NS_URI, "article", null);
		
		return convert(doc, pageTitle, author, timestamp, ast);
	}
	
	public Wom3Document convert(
			Wom3Document doc,
			PageTitle pageTitle,
			String author,
			DateTime timestamp,
			WtNode ast)
	{
		this.doc = doc;
		this.pageTitle = pageTitle;
		this.author = author;
		this.timestamp = timestamp;
		
		go(ast);
		
		return doc;
	}
	
	@Override
	public Object go(WtNode node)
	{
		if (this.doc == null)
			throw new UnsupportedOperationException("You must not call go() directly!");
		
		return super.go(node);
	}
	
	@Override
	protected Object after(WtNode node, Object result)
	{
		Wom3ElementNode root = (Wom3ElementNode) result;
		
		root.setAttributeNS(
				"http://www.w3.org/2000/xmlns/",
				"xmlns:" + DEFAULT_MWW_NS_PREFIX,
				MWW_NS_URI);
		
		doc.replaceChild(root, doc.getDocumentElement());
		
		return doc;
	}
	
	// =========================================================================
	
	public WikiConfig getWikiConfig()
	{
		return wikiConfig;
	}
	
	public boolean setPreserveRtd(boolean preserveRtd)
	{
		boolean old = this.preserveRtd;
		this.preserveRtd = preserveRtd;
		return old;
	}
	
	public boolean isPreserveRtd()
	{
		return preserveRtd;
	}
	
	public void setAddSubst(boolean addSubst)
	{
		this.addSubst = addSubst;
	}
	
	public boolean isAddSubst()
	{
		return addSubst;
	}
	
	// == [ Encoding Validator ] ===============================================
	
	@Override
	public Wom3ElementNode visit(WtIllegalCodePoint n)
	{
		// The & will be escaped and therefore the whole thing will be rendered 
		// as plain text, indicating that something unrepresentable was 
		// processed here.
		appendInconvertible(n, false);
		return null;
	}
	
	// == [ Tag extension ] ====================================================
	
	@Override
	public Wom3ElementNode visit(WtTagExtension n)
	{
		Wom3Element tag = genPushMww("tagext");
		{
			tag.setAttribute("name", n.getName());
			
			appendRtd(tag, n, 0);
			
			convertToExplicitMwwAttributes(tag, n.getXmlAttributes());
			
			appendRtd(tag, n, 1);
			
			if (n.hasBody())
				dispatchAppend(tag, n.getBody());
			
			appendRtd(tag, n, 2);
		}
		return pop(tag);
	}
	
	@Override
	public Wom3ElementNode visit(WtTagExtensionBody n)
	{
		Wom3Element body = genPushMww("body");
		{
			appendText(n.getContent());
		}
		return pop(body);
	}
	
	// == [ Template ] =========================================================
	
	@Override
	public Wom3ElementNode visit(WtTemplate n)
	{
		Wom3Element template = genPushMww("transclusion");
		{
			appendRtd(template, n, 0);
			
			dispatchAppend(template, n.getName());
			
			appendRtd(template, n, 1);
			
			dispatchAppend(template, n.getArgs());
			
			appendRtd(template, n, 2);
		}
		return pop(template);
	}
	
	@Override
	public Wom3ElementNode visit(WtTemplateArguments n)
	{
		processContainerNode(n);
		return null;
	}
	
	@Override
	public Wom3ElementNode visit(WtTemplateArgument n)
	{
		Wom3Element arg = genPushMww("arg");
		{
			appendRtd(arg, n, 0);
			
			if (n.hasName())
				dispatchAppend(arg, n.getName());
			
			appendRtd(arg, n, 1);
			
			dispatchAppend(arg, n.getValue());
			
			appendRtd(arg, n, 2);
		}
		return pop(arg);
	}
	
	@Override
	public Wom3ElementNode visit(WtTemplateParameter n)
	{
		Wom3Element param = genPushMww("param");
		{
			appendRtd(param, n, 0);
			
			dispatchAppend(param, n.getName());
			
			appendRtd(param, n, 1);
			
			if (n.hasDefault())
				dispatchAppend(param, n.getDefault());
			
			appendRtd(param, n, 2);
			
			if (!n.getGarbage().isEmpty())
			{
				Wom3Element garbage = genPushMww("garbage");
				{
					dispatchAppend(garbage, n.getGarbage());
				}
				popAppend(param, garbage);
			}
			
			appendRtd(param, n, 3);
		}
		return pop(param);
	}
	
	// == [ XML references ] ===================================================
	
	@Override
	public Wom3ElementNode visit(WtXmlCharRef n)
	{
		if (addSubst)
		{
			Wom3Subst subst = (Wom3Subst) genPushWom("subst");
			{
				Wom3Repl repl = (Wom3Repl) genPushWom("repl");
				{
					convertCharRef(n);
				}
				popAppend(subst, repl);
				
				Wom3For for_ = (Wom3For) genPushWom("for");
				{
					Wom3Element charRef = genPushMww("xml-char-ref");
					{
						charRef.setAttribute("code", String.valueOf(n.getCodePoint()));
						
						appendRtd(charRef, n, 0);
					}
					popAppend(for_, charRef);
				}
				popAppend(subst, for_);
			}
			return pop(subst);
		}
		else
		{
			convertCharRef(n);
			return null;
		}
	}
	
	private Wom3ElementNode convertCharRef(WtXmlCharRef n)
	{
		int codePoint = n.getCodePoint();
		if (!XmlGrammar.isChar(codePoint))
		{
			// The & will be escaped and therefore the whole thing will be 
			// rendered as plain text, indicating that something unrepresentable 
			// was processed here.
			appendInconvertible(n, false);
		}
		else
		{
			appendText(new String(Character.toChars(codePoint)));
		}
		return null;
	}
	
	@Override
	public Wom3ElementNode visit(WtXmlEntityRef n)
	{
		if (addSubst)
		{
			Wom3Subst subst = (Wom3Subst) genPushWom("subst");
			{
				Wom3Repl repl = (Wom3Repl) genPushWom("repl");
				{
					convertEntityRef(n);
				}
				popAppend(subst, repl);
				
				Wom3For for_ = (Wom3For) genPushWom("for");
				{
					Wom3Element entityRef = genPushMww("xml-entity-ref");
					{
						entityRef.setAttribute("name", String.valueOf(n.getName()));
						appendRtd(entityRef, n, 0);
					}
					popAppend(for_, entityRef);
				}
				popAppend(subst, for_);
			}
			return pop(subst);
		}
		else
		{
			convertEntityRef(n);
			return null;
		}
	}
	
	private Wom3ElementNode convertEntityRef(WtXmlEntityRef n)
	{
		String resolved = n.getResolved();
		if (resolved == null)
		{
			// The & will be escaped and therefore the whole thing will be 
			// rendered as plain text, indicating that something unrepresentable 
			// was processed here.
			appendInconvertible(n, false);
		}
		else
		{
			appendText(resolved);
		}
		return null;
	}
	
	// == [ Incomplete XML tags ] ==============================================
	
	@Override
	public Wom3ElementNode visit(WtXmlEmptyTag n)
	{
		appendInconvertible(n, false);
		return null;
	}
	
	@Override
	public Wom3ElementNode visit(WtXmlStartTag n)
	{
		appendInconvertible(n, false);
		return null;
	}
	
	@Override
	public Wom3ElementNode visit(WtXmlEndTag n)
	{
		appendInconvertible(n, false);
		return null;
	}
	
	@Override
	public Wom3ElementNode visit(WtImStartTag n)
	{
		appendInconvertible(n, false);
		return null;
	}
	
	@Override
	public Wom3ElementNode visit(WtImEndTag n)
	{
		appendInconvertible(n, false);
		return null;
	}
	
	// == [ Pure XML Elements ] ================================================
	
	@Override
	public Wom3ElementNode visit(WtXmlElement n)
	{
		String lowercaseName = n.getName().toLowerCase();
		HtmlElement elementType = xhtmlElems.get(lowercaseName);
		if (elementType != null)
		{
			return convertXmlElementToNative(n, elementType);
		}
		else
		{
			return convertGenericXmlElement(n);
		}
	}
	
	private Wom3ElementNode convertXmlElementToNative(
			WtXmlElement n,
			HtmlElement elementType)
	{
		switch (elementType)
		{
		// -- Easy stuff --
		
			case ABBR:
				return womFromXmlElement(n, "abbr");
			case B:
				return womFromXmlElement(n, "b");
			case BIG:
				return womFromXmlElement(n, "big");
			case BLOCKQUOTE:
				return womFromXmlElement(n, "blockquote");
			case BR:
				return womFromXmlElement(n, "br");
			case CENTER:
				return womFromXmlElement(n, "center");
			case CITE:
				return womFromXmlElement(n, "cite");
			case CODE:
				return womFromXmlElement(n, "code");
			case DEL:
				return womFromXmlElement(n, "del");
			case DFN:
				return womFromXmlElement(n, "dfn");
			case DIV:
				return womFromXmlElement(n, "div");
			case EM:
				return womFromXmlElement(n, "em");
			case FONT:
				return womFromXmlElement(n, "font");
			case HR:
				return womFromXmlElement(n, "hr");
			case I:
				return womFromXmlElement(n, "i");
			case INS:
				return womFromXmlElement(n, "ins");
			case KBD:
				return womFromXmlElement(n, "kbd");
			case P:
				return womFromXmlElement(n, "p");
			case S:
				return womFromXmlElement(n, "s");
			case SAMP:
				return womFromXmlElement(n, "samp");
			case SMALL:
				return womFromXmlElement(n, "small");
			case SPAN:
				return womFromXmlElement(n, "span");
			case STRIKE:
				return womFromXmlElement(n, "strike");
			case STRONG:
				return womFromXmlElement(n, "strong");
			case SUB:
				return womFromXmlElement(n, "sub");
			case SUP:
				return womFromXmlElement(n, "sup");
			case TT:
				return womFromXmlElement(n, "tt");
			case U:
				return womFromXmlElement(n, "u");
			case VAR:
				return womFromXmlElement(n, "var");
				
				// -- Lists --
				
			case DD:
				return womFromXmlElement(n, "dd");
			case DL:
				return womFromXmlElement(n, "dl");
			case DT:
				return womFromXmlElement(n, "dt");
			case LI:
				return womFromXmlElement(n, "li");
			case OL:
				return womFromXmlElement(n, "ol");
			case UL:
				return womFromXmlElement(n, "ul");
				
				// -- Tables --
				
			case TABLE:
				return tableFromElement(n);
			case CAPTION:
				return tableCaptionFromElement(n);
			case TBODY:
				return tableBodyFromElement(n);
			case TR:
				return womFromXmlElement(n, "tr");
			case TD:
				return tableCellFromElement(n);
			case TH:
				return tableHeaderFromElement(n);
				
				// -- Misc --
				
			case PRE:
				return preFromElement(n);
		}
		
		// Don't push into default: case. 
		// This way we'll get a warning if we missed a constant.
		throw new IllegalArgumentException("Unknown element type: " + elementType);
	}
	
	private Wom3ElementNode convertGenericXmlElement(WtXmlElement n)
	{
		Wom3ElementNode element = genPushMww("xmlelement");
		{
			element.setAttribute("tag", n.getName());
			
			appendRtd(element, n, 0);
			
			convertToExplicitMwwAttributes(element, n.getXmlAttributes());
			
			appendRtd(element, n, 1);
			
			if (n.hasBody())
				processChildrenPushAppend(n.getBody(), genMww("body"));
			
			appendRtd(element, n, 2);
		}
		return pop(element);
	}
	
	// == [ XML Element to Native WOM ] ========================================
	
	private Wom3ElementNode womFromXmlElement(WtXmlElement n, String womName)
	{
		Wom3ElementNode womElement = genPushWom(womName);
		{
			appendRtd(womElement, n, 0);
			
			dispatchAppend(womElement, n.getXmlAttributes());
			
			appendRtd(womElement, n, 1);
			
			if (n.hasBody())
				processChildrenNoPush(n.getBody(), womElement);
			
			appendRtd(womElement, n, 2);
		}
		return pop(womElement);
	}
	
	private Wom3ElementNode preFromElement(WtXmlElement n)
	{
		Wom3ElementNode pre = genPushWom("pre");
		{
			appendRtd(pre, n, 0);
			
			dispatchAppend(pre, n.getXmlAttributes());
			
			appendRtd(pre, n, 1);
			
			if (n.hasBody())
				processChildrenPushAppend(n.getBody(), genWom("nowiki"));
			
			appendRtd(pre, n, 2);
		}
		return pop(pre);
	}
	
	private Wom3ElementNode tableFromElement(WtXmlElement n)
	{
		Wom3Table table = (Wom3Table) genPushWom("table");
		inNoTextScope = true;
		{
			appendRtd(table, n, 0);
			
			dispatchAppend(table, n.getXmlAttributes());
			
			appendRtd(table, n, 1);
			
			if (n.hasBody())
				processChildrenNoPush(n.getBody(), table);
			
			appendRtd(table, n, 2);
		}
		inNoTextScope = false;
		return pop(table);
	}
	
	private Wom3ElementNode tableBodyFromElement(WtXmlElement n)
	{
		return womFromXmlElement(n, "tbody");
	}
	
	private Wom3ElementNode tableCaptionFromElement(WtXmlElement n)
	{
		Wom3ElementNode scope = getScope();
		if (scope instanceof Wom3Table)
		{
			Wom3Table table = (Wom3Table) scope;
			if (table.getCaption() != null)
			{
				// All but the first caption are ignored.
				appendInconvertible(n, true);
				return null;
			}
		}
		
		return tableXFromX(n, "caption");
	}
	
	private Wom3ElementNode tableHeaderFromElement(WtXmlElement n)
	{
		return tableXFromX(n, "th");
	}
	
	private Wom3ElementNode tableCellFromElement(WtXmlElement n)
	{
		return tableXFromX(n, "td");
	}
	
	private Wom3ElementNode tableXFromX(WtXmlElement n, String womName)
	{
		try
		{
			inNoTextScope = false;
			return womFromXmlElement(n, womName);
		}
		finally
		{
			inNoTextScope = true;
		}
	}
	
	// == [ XML Attributes ] ===================================================
	
	@Override
	public Wom3ElementNode visit(WtXmlAttributes n)
	{
		boolean old = setSuppressRtd(n.getRtd());
		{
			processContainerNode(n);
		}
		setSuppressRtd(old);
		return null;
	}
	
	@Override
	public Wom3Node visit(WtXmlAttribute n)
	{
		boolean failed = true;
		tryBlock: try
		{
			if (!n.getName().isResolved())
				break tryBlock;
			String nameAsString = n.getName().getAsString();
			if (!XmlGrammar.xmlName().matcher(nameAsString).matches())
				break tryBlock;
			if (nameAsString.indexOf(':') != -1)
				break tryBlock;
			
			String valueAsString = stringify(n.getValue());
			
			Wom3ElementNode parent = getScope();
			parent.setAttributeNode(attr(nameAsString, valueAsString));
			
			if (!isRtdSuppressed())
				appendRtd(parent, convertToText(n));
			
			failed = false;
		}
		catch (IllegalArgumentException e)
		{
			// Setting the attribute failed.
		}
		catch (IllegalStateException e)
		{
			// Name conversion failed.
		}
		catch (StringConversionException e)
		{
			// Value conversion failed.
		}
		
		if (failed)
			appendInconvertible(n, true);
		
		return null;
	}
	
	@Override
	public Wom3ElementNode visit(WtXmlAttributeGarbage n)
	{
		appendInconvertible(n, true);
		return null;
	}
	
	// == [ Explicit MWW Attributes ] ==========================================
	
	private void convertToExplicitMwwAttributes(
			Wom3ElementNode womParent,
			WtXmlAttributes xmlAttribs)
	{
		appendRtd(womParent, xmlAttribs, 0);
		
		for (WtNode wtAttr : xmlAttribs)
		{
			if (wtAttr instanceof WtXmlAttribute)
			{
				womParent.appendChild(convertToExplicitMwwAttribute((WtXmlAttribute) wtAttr));
			}
			else
			{
				dispatchAppend(womParent, wtAttr);
			}
		}
		
		appendRtd(womParent, xmlAttribs, 1);
	}
	
	private Wom3Element convertToExplicitMwwAttribute(WtXmlAttribute wtAttr)
	{
		Wom3Element womAttr = genPushMww("attr");
		
		appendRtd(womAttr, wtAttr, 0);
		
		processChildrenPushAppend(wtAttr.getName(), genMww("name"));
		
		appendRtd(womAttr, wtAttr, 1);
		
		if (wtAttr.hasValue())
			processChildrenPushAppend(wtAttr.getValue(), genMww("value"));
		
		appendRtd(womAttr, wtAttr, 2);
		
		return (Wom3Element) pop(womAttr);
	}
	
	// == [ Redirect ] =========================================================
	
	@Override
	public Wom3ElementNode visit(WtRedirect n)
	{
		String targetAsString = getTargetAsString(n.getTarget());
		String normalizedTarget = getNormalizedTargetAsString(targetAsString);
		if ((targetAsString != null) && (normalizedTarget != null))
		{
			// Don't push onto stack!
			Wom3Redirect redirect = (Wom3Redirect) genWom("redirect");
			
			// Insert native part into WOM
			String did = String.valueOf(displacement++);
			redirect.setDisplacementId(did);
			redirect.setTarget(normalizedTarget);
			page.setRedirect(redirect);
			
			// Insert into original spot
			if (addSubst)
			{
				Wom3Subst subst = (Wom3Subst) genPushWom("subst");
				subst.setDisplacementId(did);
				{
					Wom3Repl repl = (Wom3Repl) genPushWom("repl");
					// Keep it empty. It's really attached to the page node.
					popAppend(subst, repl);
					
					Wom3For for_ = (Wom3For) genPushWom("for");
					{
						Wom3Element mwwRedirect = genPushMww("redirect");
						{
							appendRtd(mwwRedirect, n, 0);
							
							dispatchAppend(mwwRedirect, n.getTarget());
							
							appendRtd(mwwRedirect, n, 1);
						}
						popAppend(for_, mwwRedirect);
					}
					popAppend(subst, for_);
				}
				return pop(subst);
			}
			else
			{
				return null;
			}
		}
		else
		{
			appendInconvertible(n, false);
			return null;
		}
	}
	
	@Override
	public Wom3ElementNode visit(WtPageName n)
	{
		try
		{
			appendText(n.getAsString());
			return null;
		}
		catch (IllegalStateException e)
		{
			// This should not happen. The WtPageName node is only visited if
			// the link can be properly converted.
			throw new IllegalArgumentException("WtPageName cannot be rendered as text");
		}
	}
	
	private String getTargetAsString(WtPageName target)
	{
		try
		{
			return target.getAsString();
		}
		catch (IllegalStateException e)
		{
			return null;
		}
	}
	
	private String getNormalizedTargetAsString(String targetAsString)
	{
		try
		{
			PageTitle target = PageTitle.make(wikiConfig, targetAsString);
			
			String normalized = target.getNormalizedFullTitle();
			if (target.getFragment() != null)
				normalized += "#" + target.getFragment();
			
			Toolbox.checkValidTarget(normalized);
			
			return normalized;
		}
		catch (IllegalArgumentException e)
		{
			return null;
		}
		catch (LinkTargetException e)
		{
			return null;
		}
	}
	
	// == [ External Links ] ===================================================
	
	@Override
	public Wom3ElementNode visit(WtUrl n)
	{
		URL url = urlNodeToUrl(n);
		if (url != null)
		{
			Wom3ExtLink extLink = (Wom3ExtLink) genPushWom("extlink");
			{
				extLink.setTarget(url);
				extLink.setPlainUrl(true);
				
				appendRtd(extLink, n, 0);
			}
			return pop(extLink);
		}
		else
		{
			appendInconvertible(n, false);
			return null;
		}
	}
	
	@Override
	public Wom3ElementNode visit(WtExternalLink n)
	{
		URL url = urlNodeToUrl(n.getTarget());
		if (url != null)
		{
			Wom3ExtLink extLink = (Wom3ExtLink) genPushWom("extlink");
			{
				extLink.setTarget(url);
				
				appendRtd(extLink, n, 0);
				
				appendRtd(extLink, n.getTarget(), 0);
				
				appendRtd(extLink, n, 1);
				
				if (n.hasTitle())
					dispatchAppend(extLink, n.getTitle());
				
				appendRtd(extLink, n, 2);
			}
			return pop(extLink);
		}
		else
		{
			appendInconvertible(n, false);
			return null;
		}
	}
	
	private URL urlNodeToUrl(WtUrl urlNode)
	{
		try
		{
			if (urlNode.getProtocol().isEmpty())
				return new URL(urlNode.getPath());
			
			return new URL(urlNode.getProtocol() + ":" + urlNode.getPath());
		}
		catch (MalformedURLException e)
		{
			return null;
		}
	}
	
	// == [ Internal Link ]= ===================================================
	
	@Override
	public Wom3ElementNode visit(WtInternalLink n)
	{
		String originalTarget = getTargetAsString(n.getTarget());
		String normalizedTarget = getNormalizedTargetAsString(originalTarget);
		
		if ((originalTarget == null) || (normalizedTarget == null))
		{
			appendInconvertible(n, false);
			return null;
		}
		
		boolean subst = addSubst &&
				(!n.getPostfix().isEmpty() || !n.getPrefix().isEmpty());
		
		return (subst ?
				substIntLink(n, originalTarget, normalizedTarget) :
				nativeIntLink(n, originalTarget, normalizedTarget));
	}
	
	private Wom3ElementNode nativeIntLink(
			WtInternalLink n,
			String originalTarget,
			String normalizedTarget)
	{
		Wom3IntLink intLink = (Wom3IntLink) genPushWom("intlink");
		{
			intLink.setTarget(normalizedTarget);
			
			appendRtd(intLink, n, 0);
			
			boolean old = setSuppressRtd(n.getTarget().getRtd());
			{
				appendRtd(intLink, originalTarget);
			}
			setSuppressRtd(old);
			
			appendRtd(intLink, n, 1);
			
			if (n.hasTitle())
				dispatchAppend(intLink, n.getTitle());
			
			appendRtd(intLink, n, 2);
			
		}
		return pop(intLink);
	}
	
	private Wom3ElementNode substIntLink(
			WtInternalLink n,
			String originalTarget,
			String normalizedTarget)
	{
		Wom3Subst subst = (Wom3Subst) genPushWom("subst");
		{
			Wom3Repl repl = (Wom3Repl) genPushWom("repl");
			{
				Wom3IntLink intLink = (Wom3IntLink) genPushWom("intlink");
				boolean oldPreserveRtd = setPreserveRtd(false);
				{
					intLink.setTarget(normalizedTarget);
					
					if (n.hasTitle())
					{
						Wom3Title title = (Wom3Title) dispatchAppend(intLink, n.getTitle());
						push(title);
						{
							if (!n.getPrefix().isEmpty())
								prependText(title, n.getPrefix());
							// title goes here ...
							if (!n.getPostfix().isEmpty())
								appendText(title, n.getPostfix());
						}
						pop(title);
					}
					else
					// no title given, create title from target
					{
						Wom3Title title = (Wom3Title) genPushWom("title");
						if (!n.getPrefix().isEmpty())
							appendText(title, n.getPrefix());
						appendText(title, originalTarget);
						if (!n.getPostfix().isEmpty())
							appendText(title, n.getPostfix());
						intLink.setLinkTitle((Wom3Title) pop(title));
					}
				}
				setPreserveRtd(oldPreserveRtd);
				popAppend(repl, intLink);
			}
			popAppend(subst, repl);
			
			Wom3For for_ = (Wom3For) genPushWom("for");
			{
				Wom3ElementNode intLink = genPushMww("intlink");
				{
					intLink.setAttribute("target", originalTarget);
					if (!n.getPostfix().isEmpty())
						intLink.setAttribute("postfix", n.getPostfix());
					if (!n.getPrefix().isEmpty())
						intLink.setAttribute("prefix", n.getPrefix());
					
					appendRtd(intLink, n, 0);
					appendRtd(intLink, originalTarget);
					appendRtd(intLink, n, 1);
					
					if (n.hasTitle())
						dispatchAppend(intLink, n.getTitle());
					
					appendRtd(intLink, n, 2);
				}
				popAppend(for_, intLink);
			}
			popAppend(subst, for_);
		}
		return pop(subst);
	}
	
	// == [ Image Link ] =======================================================
	
	@Override
	public Wom3ElementNode visit(WtImageLink n)
	{
		String targetAsString = getTargetAsString(n.getTarget());
		String normalizedTarget = getNormalizedTargetAsString(targetAsString);
		if ((targetAsString != null) && (normalizedTarget != null))
		{
			Wom3Image img = (Wom3Image) genPushWom("image");
			{
				img.setSource(normalizedTarget);
				img.setFormat(mapImgFormat(n.getFormat()));
				img.setBorder(n.getBorder());
				img.setHAlign(mapImgHAlign(n.getHAlign()));
				img.setVAlign(mapImgVAlign(n.getVAlign()));
				img.setUpright(n.getUpright());
				
				if (n.getWidth() >= 0)
					img.setWidth(n.getWidth());
				if (n.getHeight() >= 0)
					img.setHeight(n.getHeight());
				if (n.hasAlt())
					img.setAlt(convertContentToText(n.getAlt()));
				
				setImgLink(img, n.getLink());
				
				appendRtd(img, n, 0);
				
				appendRtd(img, targetAsString);
				
				appendRtd(img, n, 1);
				
				processChildrenNoPush(n.getOptions(), img);
				
				appendRtd(img, n, 2);
			}
			return pop(img);
		}
		else
		{
			appendInconvertible(n, false);
			return null;
		}
	}
	
	private void setImgLink(Wom3Image img, WtLinkOptionLinkTarget link)
	{
		LinkTargetType linkType = link.getTargetType();
		switch (linkType)
		{
			case DEFAULT:
				// Nothing to do
				return;
				
			case NO_LINK:
				img.setIntLink("");
				return;
				
			case PAGE:
				img.setIntLink(getTargetAsString((WtPageName) link.getTarget()));
				return;
				
			case URL:
				img.setExtLink(urlNodeToUrl((WtUrl) link.getTarget()));
				return;
		}
		
		// Don't push into default: case. 
		// This way we'll get a warning if we missed a constant.
		throw new IllegalArgumentException("Unknown element type: " + linkType);
	}
	
	private Wom3ImageVAlign mapImgVAlign(ImageVertAlign vAlign)
	{
		switch (vAlign)
		{
			case BASELINE:
				return Wom3ImageVAlign.BASELINE;
			case BOTTOM:
				return Wom3ImageVAlign.BOTTOM;
			case MIDDLE:
				return Wom3ImageVAlign.MIDDLE;
			case SUB:
				return Wom3ImageVAlign.SUB;
			case SUPER:
				return Wom3ImageVAlign.SUPER;
			case TEXT_BOTTOM:
				return Wom3ImageVAlign.TEXT_BOTTOM;
			case TEXT_TOP:
				return Wom3ImageVAlign.TEXT_TOP;
			case TOP:
				return Wom3ImageVAlign.TOP;
				
		}
		
		// Don't push into default: case. 
		// This way we'll get a warning if we missed a constant.
		throw new IllegalArgumentException("Unknown image vertical alignment: " + vAlign);
	}
	
	private Wom3ImageHAlign mapImgHAlign(ImageHorizAlign hAlign)
	{
		switch (hAlign)
		{
			case CENTER:
				return Wom3ImageHAlign.CENTER;
			case LEFT:
				return Wom3ImageHAlign.LEFT;
			case NONE:
				return Wom3ImageHAlign.NONE;
			case RIGHT:
				return Wom3ImageHAlign.RIGHT;
			case UNSPECIFIED:
				return Wom3ImageHAlign.DEFAULT;
		}
		
		// Don't push into default: case. 
		// This way we'll get a warning if we missed a constant.
		throw new IllegalArgumentException("Unknown image horizontal alignment: " + hAlign);
	}
	
	private Wom3ImageFormat mapImgFormat(ImageViewFormat format)
	{
		switch (format)
		{
			case FRAME:
				return Wom3ImageFormat.FRAME;
			case FRAMELESS:
				return Wom3ImageFormat.FRAMELESS;
			case THUMBNAIL:
				return Wom3ImageFormat.THUMBNAIL;
			case UNRESTRAINED:
				return Wom3ImageFormat.UNRESTRAINED;
		}
		
		// Don't push into default: case. 
		// This way we'll get a warning if we missed a constant.
		throw new IllegalArgumentException("Unknown image view format: " + format);
	}
	
	@Override
	public Wom3ElementNode visit(WtLinkOptions n)
	{
		boolean old = setSuppressRtd(n.getRtd());
		{
			processContainerNode(n);
		}
		setSuppressRtd(old);
		return null;
	}
	
	@Override
	public Wom3ElementNode visit(WtLinkOptionLinkTarget n)
	{
		appendRtd(convertToText(n));
		return null;
	}
	
	@Override
	public Wom3ElementNode visit(WtLinkOptionKeyword n)
	{
		appendRtd(convertToText(n));
		return null;
	}
	
	@Override
	public Wom3ElementNode visit(WtLinkOptionResize n)
	{
		appendRtd(convertToText(n));
		return null;
	}
	
	@Override
	public Wom3ElementNode visit(WtLinkOptionAltText n)
	{
		appendRtd(convertToText(n));
		return null;
	}
	
	@Override
	public Wom3ElementNode visit(WtLinkOptionGarbage n)
	{
		appendInconvertible(n, true);
		return null;
	}
	
	// == [ Section ] ==========================================================
	
	@Override
	public Wom3ElementNode visit(WtSection n)
	{
		Wom3Section section = (Wom3Section) genPushWom("section");
		{
			section.setLevel(n.getLevel());
			
			appendRtd(section, n, 0);
			
			dispatchAppend(section, n.getHeading());
			
			appendRtd(section, n, 1);
			
			dispatchAppend(section, n.getBody());
			
			appendRtd(section, n, 2);
		}
		return pop(section);
	}
	
	@Override
	public Wom3ElementNode visit(WtHeading n)
	{
		return processChildrenPush(n, genWom("heading"));
	}
	
	// == [ Lists ] ============================================================
	
	@Override
	public Wom3ElementNode visit(WtOrderedList n)
	{
		return processChildrenPush(n, genWom("ol"));
	}
	
	@Override
	public Wom3ElementNode visit(WtUnorderedList n)
	{
		return processChildrenPush(n, genWom("ul"));
	}
	
	@Override
	public Wom3ElementNode visit(WtListItem n)
	{
		return processChildrenPush(n, genWom("li"));
	}
	
	@Override
	public Wom3ElementNode visit(WtDefinitionList n)
	{
		return processChildrenPush(n, genWom("dl"));
	}
	
	@Override
	public Wom3ElementNode visit(WtDefinitionListDef n)
	{
		return processChildrenPush(n, genWom("dd"));
	}
	
	@Override
	public Wom3ElementNode visit(WtDefinitionListTerm n)
	{
		return processChildrenPush(n, genWom("dt"));
	}
	
	// == [ Table ] ============================================================
	
	@Override
	public Wom3ElementNode visit(WtTable n)
	{
		Wom3Table table = (Wom3Table) genPushWom("table");
		inNoTextScope = true;
		{
			appendRtd(table, n, 0);
			
			dispatchAppend(table, n.getXmlAttributes());
			
			appendRtd(table, n, 1);
			
			processChildrenNoPush(n.getBody(), table);
			
			appendRtd(table, n, 2);
		}
		inNoTextScope = false;
		return pop(table);
	}
	
	@Override
	public Wom3ElementNode visit(WtTableCaption n)
	{
		Wom3ElementNode scope = getScope();
		if (scope instanceof Wom3Table)
		{
			Wom3Table table = (Wom3Table) scope;
			if (table.getCaption() != null)
			{
				// All but the first caption are ignored.
				appendInconvertible(n, true);
				return null;
			}
		}
		
		Wom3TableCaption caption = (Wom3TableCaption) genPushWom("caption");
		inNoTextScope = false;
		{
			appendRtd(caption, n, 0);
			
			dispatchAppend(caption, n.getXmlAttributes());
			
			appendRtd(caption, n, 1);
			
			processChildrenNoPush(n.getBody(), caption);
			
			appendRtd(caption, n, 2);
		}
		inNoTextScope = true;
		return pop(caption);
	}
	
	@Override
	public Wom3ElementNode visit(WtTableImplicitTableBody n)
	{
		return processChildrenPush(n.getBody(), genWom("tbody"));
	}
	
	@Override
	public Wom3ElementNode visit(WtTableRow n)
	{
		return buildRowOrCell(n, n.getXmlAttributes(), n.getBody(), "tr");
	}
	
	@Override
	public Wom3ElementNode visit(WtTableCell n)
	{
		inNoTextScope = false;
		Wom3ElementNode cell = buildRowOrCell(n, n.getXmlAttributes(), n.getBody(), "td");
		inNoTextScope = true;
		return cell;
	}
	
	@Override
	public Wom3ElementNode visit(WtTableHeader n)
	{
		inNoTextScope = false;
		Wom3ElementNode header = buildRowOrCell(n, n.getXmlAttributes(), n.getBody(), "th");
		inNoTextScope = true;
		return header;
	}
	
	private Wom3ElementNode buildRowOrCell(
			WtNode n,
			WtXmlAttributes attrs,
			WtBody body,
			String name)
	{
		Wom3ElementNode rowOrCell = genPushWom(name);
		{
			appendRtd(rowOrCell, n, 0);
			
			dispatchAppend(rowOrCell, attrs);
			
			appendRtd(rowOrCell, n, 1);
			
			processChildrenNoPush(body, rowOrCell);
			
			appendRtd(rowOrCell, n, 2);
		}
		return pop(rowOrCell);
	}
	
	// == [ Simple HTML equivalents ] ==========================================
	
	@Override
	public Wom3ElementNode visit(WtHorizontalRule n)
	{
		Wom3ElementNode hr = genPushWom("hr");
		{
			appendRtd(hr, n, 0);
		}
		return pop(hr);
	}
	
	@Override
	public Wom3ElementNode visit(WtItalics n)
	{
		return processChildrenPush(n, genWom("i"));
	}
	
	@Override
	public Wom3ElementNode visit(WtBold n)
	{
		return processChildrenPush(n, genWom("b"));
	}
	
	// == [ Signature ] ========================================================
	
	@Override
	public Wom3ElementNode visit(WtSignature n)
	{
		Wom3Signature sig = (Wom3Signature) genPushWom("signature");
		{
			sig.setSignatureFormat(mapSignatureFormat(n));
			sig.setAuthor(author);
			sig.setTimestamp(timestamp);
			
			appendRtd(sig, n, 0);
		}
		return pop(sig);
	}
	
	private Wom3SignatureFormat mapSignatureFormat(WtSignature n)
	{
		switch (n.getTildeCount())
		{
			case 3:
				return Wom3SignatureFormat.USER;
			case 4:
				return Wom3SignatureFormat.USER_TIMESTAMP;
			case 5:
				return Wom3SignatureFormat.TIMESTAMP;
		}
		
		// Don't push into default: case. 
		// This way we'll get a warning if we missed a constant.
		throw new IllegalArgumentException("Illegal signature tilde count: " + n.getTildeCount());
	}
	
	// == [ Paragraph ] ========================================================
	
	@Override
	public Wom3ElementNode visit(WtParagraph n)
	{
		Wom3Paragraph paragraph = (Wom3Paragraph) genPushWom("p");
		{
			paragraph.setTopGap(countNewlines(n.listIterator(), false));
			paragraph.setBottomGap(countNewlines(n.listIterator(n.size()), true));
			
			processChildrenNoPush(n, paragraph);
			
			if (!paragraph.hasChildNodes())
			{
				pop(paragraph);
				return null;
			}
		}
		return pop(paragraph);
	}
	
	private int countNewlines(ListIterator<?> i, boolean reverse)
	{
		int count = 0;
		outer: while (reverse ? i.hasPrevious() : i.hasNext())
		{
			WtNode n = (WtNode) (reverse ? i.previous() : i.next());
			switch (n.getNodeType())
			{
				case WtNode.NT_NEWLINE:
					++count;
					break;
				
				case WtNode.NT_TEXT:
				{
					String text = ((WtText) n).getContent();
					int len = text.length();
					for (int j = 0; j < len; ++j)
					{
						int ch = text.charAt(reverse ? len - 1 - j : j);
						if (ch == '\n' || ch == '\r')
						{
							if (j + 1 < len)
							{
								int ch2 = text.charAt(reverse ? len - 1 - j : j);
								if ((ch == '\r' && ch2 == '\n') || (ch == '\n' && ch2 == '\r'))
									++j;
							}
							++count;
						}
						else if (!Character.isWhitespace(ch))
						{
							break outer;
						}
					}
					break;
				}
				
				default:
					break outer;
			}
		}
		return count;
	}
	
	// == [ Semi Pre ] =========================================================
	
	@Override
	public Wom3ElementNode visit(WtSemiPre n)
	{
		return processChildrenPush(n, genWom("pre"));
	}
	
	@Override
	public Wom3ElementNode visit(WtSemiPreLine n)
	{
		return processContainerNode(n);
	}
	
	// == [ Containers ] =======================================================
	
	@Override
	public Wom3ElementNode visit(WtNodeList n)
	{
		return processContainerNode(n);
	}
	
	@Override
	public Wom3ElementNode visit(WtBody n)
	{
		return processChildrenPush(n, genWom("body"));
	}
	
	@Override
	public Wom3ElementNode visit(WtLinkTitle n)
	{
		Wom3ElementNode parent = getScope();
		if (parent instanceof Wom3Image)
		{
			return processChildrenPush(n, genWom("imgcaption"));
		}
		else
		{
			return processChildrenPush(n, genWom("title"));
		}
	}
	
	@Override
	public Wom3ElementNode visit(WtName n)
	{
		return processChildrenPush(n, genMww("name"));
	}
	
	@Override
	public Wom3ElementNode visit(WtValue n)
	{
		return processChildrenPush(n, genMww("value"));
	}
	
	@Override
	public Wom3ElementNode visit(WtWhitespace n)
	{
		return processContainerNode(n);
	}
	
	@Override
	public Wom3ElementNode visit(WtOnlyInclude n)
	{
		return processContainerNode(n);
	}
	
	@Override
	public Wom3ElementNode visit(WtIgnored n)
	{
		appendRtd(convertToText(n));
		return null;
	}
	
	// == [ Page Switch ] ======================================================
	
	@Override
	public Wom3ElementNode visit(WtPageSwitch n)
	{
		throw new UnsupportedOperationException("not yet implemented");
		
		// FIXME: Attach page switches to page element first!
		// See categories about how it's done.
		//return new WomPageSwitch(n.getName());
	}
	
	// == [ Page roots ] =======================================================
	
	@Override
	public Wom3ElementNode visit(EngProcessedPage n)
	{
		return (Wom3ElementNode) dispatch(n.getPage());
	}
	
	@Override
	public Wom3ElementNode visit(EngPage n)
	{
		return createRootNode(n);
	}
	
	@Override
	public Wom3ElementNode visit(WtParsedWikitextPage n)
	{
		return createRootNode(n);
	}
	
	@Override
	public Wom3ElementNode visit(WtPreproWikitextPage n)
	{
		return createRootNode(n);
	}
	
	private Wom3ElementNode createRootNode(WtContentNode wtBody)
	{
		String title = pageTitle.getDenormalizedTitle();
		String path = null;
		int i = title.lastIndexOf('/');
		if (i != -1)
		{
			path = title.substring(0, i);
			title = title.substring(i + 1);
		}
		
		this.page = (Wom3Article) genPushWom("article");
		{
			this.page.setNamespace(pageTitle.getNamespaceAlias());
			this.page.setPath(path);
			this.page.setTitle(title);
			
			Wom3ElementNode womBody = genPushWom("body");
			{
				processChildrenNoPush(wtBody, womBody);
			}
			popAppend(this.page, womBody);
		}
		return pop(this.page);
	}
	
	// == [ Intermediate Elements ] ============================================
	
	@Override
	public Wom3ElementNode visit(WtTicks n)
	{
		// These should not be part of a fully processed AST any more...
		appendInconvertible(n, false);
		return null;
	}
	
	// == [ Error Node ] =======================================================
	
	@Override
	public Wom3ElementNode visit(EngSoftErrorNode n)
	{
		return visit((WtXmlElement) n);
	}
	
	// == [ Comment ] ==========================================================
	
	@Override
	public Wom3ElementNode visit(WtXmlComment n)
	{
		WtRtData rtd = n.getRtd();
		if (!rtd.isStringOnly(0))
		{
			appendInconvertible(n, false);
			return null;
		}
		else
		{
			Wom3Comment comment = (Wom3Comment) genPushWom("comment");
			{
				String content = n.getContent();
				String before = null;
				String after = null;
				
				String rtdStr = rtd.toString(0);
				int i = rtdStr.indexOf(content);
				if (i != -1)
				{
					before = rtdStr.substring(0, i);
					after = rtdStr.substring(i + content.length());
				}
				
				appendRtd(comment, before);
				
				appendText(content);
				
				appendRtd(comment, after);
			}
			return pop(comment);
		}
	}
	
	// == [ Nowiki ] ===========================================================
	
	@Override
	public Wom3ElementNode visit(EngNowiki n)
	{
		WtRtData rtd = n.getRtd();
		if (!rtd.isStringOnly(0))
		{
			appendInconvertible(n, false);
			return null;
		}
		else
		{
			Wom3Nowiki nowiki = (Wom3Nowiki) genPushWom("nowiki");
			{
				String content = n.getContent();
				String before = null;
				String after = null;
				
				String rtdStr = rtd.toString(0);
				int i = rtdStr.indexOf(content);
				if (i != -1)
				{
					before = rtdStr.substring(0, i);
					after = rtdStr.substring(i + content.length());
				}
				
				appendRtd(nowiki, before);
				
				appendText(content);
				
				appendRtd(nowiki, after);
			}
			return pop(nowiki);
		}
	}
	
	// == [ Text ] =============================================================
	
	@Override
	public Wom3ElementNode visit(WtText n)
	{
		if (inNoTextScope)
		{
			appendRtd(n.getContent());
		}
		else
		{
			appendText(n.getContent());
		}
		return null;
	}
	
	@Override
	public Wom3ElementNode visit(WtNewline n)
	{
		appendText(n.getContent());
		return null;
	}
	
	// =========================================================================
	
	private Wom3ElementNode getScope()
	{
		return stack.peek();
	}
	
	private <T extends Wom3ElementNode> T push(T element)
	{
		stack.push(element);
		return element;
	}
	
	private Wom3ElementNode pop(Wom3Node expected)
	{
		Wom3ElementNode popped = stack.pop();
		if (popped != expected)
			throw new InternalError();
		return popped;
	}
	
	private Wom3ElementNode genPushWom(final String name)
	{
		return push(genWom(name));
	}
	
	private Wom3Element genPushMww(String name)
	{
		return push(genMww(name));
	}
	
	private Wom3ElementNode popAppend(Wom3ElementNode parent, Wom3Node expected)
	{
		Wom3ElementNode child = pop(expected);
		parent.appendChild(child);
		return child;
	}
	
	// =========================================================================
	
	private <T extends Wom3ElementNode> T processChildrenNoPush(
			WtNode astNode,
			T womNode)
	{
		appendRtd(womNode, astNode, 0);
		
		for (WtNode c : astNode)
			dispatchAppend(womNode, c);
		
		appendRtd(womNode, astNode, 1);
		
		return womNode;
	}
	
	private <T extends Wom3ElementNode> T processChildrenPush(
			WtNode astNode,
			T womNode)
	{
		push(womNode);
		{
			processChildrenNoPush(astNode, womNode);
		}
		pop(womNode);
		return womNode;
	}
	
	private void processChildrenPushAppend(
			WtNode astNode,
			Wom3ElementNode womNode)
	{
		getScope().appendChild(processChildrenPush(astNode, womNode));
	}
	
	private Wom3ElementNode processContainerNode(WtNode n)
	{
		processChildrenNoPush(n, getScope());
		return null;
	}
	
	private Wom3ElementNode dispatchAppend(Wom3ElementNode parent, WtNode child)
	{
		Wom3ElementNode result = (Wom3ElementNode) dispatch(child);
		if (result != null)
			parent.appendChild(result);
		return result;
	}
	
	// =========================================================================
	
	private void appendInconvertible(WtNode n, boolean inNoTextScope)
	{
		if (!WtNodeFlags.isRepairNode(n))
		{
			if (this.inNoTextScope || inNoTextScope)
			{
				appendRtd(convertToText(n));
			}
			else
			{
				appendText(convertToText(n));
			}
		}
	}
	
	/**
	 * Tries to convert the children of a node to their original markup
	 * representation.
	 */
	private String convertToText(WtNode n)
	{
		return WtRtDataPrinter.print(n);
	}
	
	/**
	 * Tries to convert the children of a node to their original markup
	 * representation.
	 */
	private String convertContentToText(WtContentNode n)
	{
		return convertToText(nodeFactory.toList(n));
	}
	
	/**
	 * Tries to convert a content node to a pure text representation. This only
	 * works if the content node only contains text nodes and nodes that
	 * represent text (like entity references).
	 */
	private String stringify(WtContentNode value) throws StringConversionException
	{
		return astTextUtils.astToText(value);
	}
	
	// =========================================================================
	
	private void prependText(Wom3ElementNode parent, String text)
	{
		if ((text != null) && !text.isEmpty())
		{
			Wom3Node first = parent.getFirstChild();
			if (first instanceof Wom3Text)
			{
				if (isRtdSuppressed() == isHideRtd((Wom3Text) first))
				{
					Wom3XmlText tn = (Wom3XmlText) first.getFirstChild();
					tn.insertData(0, text);
				}
				else
				{
					prependTextNode(parent, text, first);
				}
			}
			else
			{
				prependTextNode(parent, text, first);
			}
		}
	}
	
	private void prependTextNode(
			Wom3ElementNode parent,
			String text,
			Wom3Node first)
	{
		Wom3Text tn = createTextNode(text);
		
		if (first == null)
		{
			parent.appendChild(tn);
		}
		else
		{
			parent.insertBefore(tn, first);
		}
	}
	
	private void appendText(String text)
	{
		appendText(getScope(), text);
	}
	
	private void appendText(Wom3ElementNode parent, String text)
	{
		Wom3Node last = parent.getLastChild();
		if (last instanceof Wom3Text)
		{
			if (isRtdSuppressed() == isHideRtd((Wom3Text) last))
			{
				Wom3XmlText xmlText = (Wom3XmlText) last.getLastChild();
				if (xmlText != null)
				{
					xmlText.appendData(text);
				}
				else
				{
					last.setTextContent(text);
				}
			}
			else
			{
				appendTextNode(parent, text);
			}
		}
		else
		{
			appendTextNode(parent, text);
		}
	}
	
	private void appendTextNode(Wom3ElementNode n, String text)
	{
		n.appendChild(createTextNode(text));
	}
	
	private Wom3Text createTextNode(String text)
	{
		Wom3Text tn = text(text);
		if (isRtdSuppressed())
			setHideRtd(tn);
		return tn;
	}
	
	// =========================================================================
	
	private void appendRtd(Wom3ElementNode parent, WtNode n, int index)
	{
		WtRtData rtd = n.getRtd();
		if (!isRtdVisible() && (rtd != null) && !rtd.isSuppress())
		{
			if (rtd.isStringOnly(index))
			{
				appendRtd(parent, rtd.toString(index));
			}
			else
			{
				for (Object x : rtd.getField(index))
				{
					if (x instanceof String)
					{
						appendRtd(parent, (String) x);
					}
					else
					{
						// We expect that RTD only consists of String or WtNode.
						dispatchAppend(parent, (WtNode) x);
					}
				}
			}
		}
	}
	
	private void appendRtd(Wom3ElementNode parent, String rtd)
	{
		if (!isRtdVisible() && (rtd != null) && !rtd.isEmpty())
		{
			Wom3Node last = parent.getLastChild();
			if (last instanceof Wom3Rtd)
			{
				Wom3XmlText xmlText = (Wom3XmlText) last.getLastChild();
				if (xmlText != null)
				{
					xmlText.appendData(rtd);
				}
				else
				{
					last.setTextContent(rtd);
				}
			}
			else
			{
				parent.appendChild(rtd(rtd));
			}
		}
	}
	
	private boolean setSuppressRtd(boolean suppressRtd)
	{
		boolean old = this.isRtdSuppressed();
		this.suppressRtd = suppressRtd;
		return old;
	}
	
	private boolean setSuppressRtd(WtRtData rtd)
	{
		if (isRtdSuppressed())
			// If we're already suppressing we don't change that!
			return isRtdSuppressed();
		return setSuppressRtd((rtd != null) && rtd.isSuppress());
	}
	
	private boolean isRtdSuppressed()
	{
		return suppressRtd;
	}
	
	private boolean isRtdVisible()
	{
		return !preserveRtd || isRtdSuppressed();
	}
	
	private void appendRtd(String rtd)
	{
		appendRtd(getScope(), rtd);
	}
	
	private boolean isHideRtd(Wom3ElementNode node)
	{
		return "true".equalsIgnoreCase(node.getAttributeNS(DEFAULT_MWW_NS_PREFIX, "hideRtd"));
	}
	
	private <T extends Wom3ElementNode> T setHideRtd(T node)
	{
		node.setAttributeNS(DEFAULT_MWW_NS_PREFIX, "hideRtd", "true");
		return node;
	}
	
	// =========================================================================
	
	private Wom3ElementNode genWom(String tag)
	{
		return (Wom3ElementNode) doc.createElementNS(
				Wom3ElementNode.WOM_NS_URI,
				tag);
	}
	
	private Wom3Element genMww(String type)
	{
		return (Wom3Element) doc.createElementNS(
				MWW_NS_URI,
				DEFAULT_MWW_NS_PREFIX + ":" + type);
	}
	
	private Wom3Attribute attr(String name, String value)
	{
		Wom3Attribute attr = (Wom3Attribute) doc.createAttribute(name);
		attr.setValue(value);
		return attr;
	}
	
	private Wom3Text text(String text)
	{
		Wom3ElementNode node = genWom("text");
		node.setTextContent(text);
		return (Wom3Text) node;
	}
	
	private Wom3Rtd rtd(String text)
	{
		Wom3ElementNode node = genWom("rtd");
		node.setTextContent(text);
		return (Wom3Rtd) node;
	}
}