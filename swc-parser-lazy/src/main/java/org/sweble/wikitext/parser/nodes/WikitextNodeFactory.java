/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.sweble.wikitext.parser.nodes;

import java.util.Collection;

import org.sweble.wikitext.parser.WtEntityMap;
import org.sweble.wikitext.parser.nodes.WtIllegalCodePoint.IllegalCodePointType;
import org.sweble.wikitext.parser.nodes.WtImageLink.ImageHorizAlign;
import org.sweble.wikitext.parser.nodes.WtImageLink.ImageLinkTarget;
import org.sweble.wikitext.parser.nodes.WtImageLink.ImageVertAlign;
import org.sweble.wikitext.parser.nodes.WtImageLink.ImageViewFormat;
import org.sweble.wikitext.parser.nodes.WtOnlyInclude.XmlElementType;
import org.sweble.wikitext.parser.postprocessor.IntermediateTags;

import xtc.util.Pair;
import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.AstNodeList;

public interface WikitextNodeFactory
{
	// -- Inner Node 1 ---------------------------------------------------------
	
	WtLinkOptionLinkTarget loLinkTarget(ImageLinkTarget type);
	
	WtLinkOptionLinkTarget loNoLinkTarget();
	
	WtRedirect redirect(WtPageName target);
	
	WtXmlAttribute attr(String name);
	
	WtXmlAttribute attr(String name, WtValue value);
	
	WtXmlEmptyTag emptyTag(String name, WtXmlAttributes xmlAttributes);
	
	WtXmlStartTag startTag(String name, WtXmlAttributes xmlAttributes);
	
	WtImStartTag imStartTag(IntermediateTags type, boolean synthetic);
	
	// -- Inner Node 2 ---------------------------------------------------------
	
	WtExternalLink extLink(WtUrl target);
	
	WtExternalLink extLink(WtUrl target, WtLinkTitle title);
	
	WtInternalLink intLink(String prefix, WtPageName target, String postfix);
	
	WtSection section(int level, WtHeading heading, WtBody body);
	
	WtTable table(WtXmlAttributes xmlAttributes);
	
	WtTable table(WtXmlAttributes xmlAttributes, WtBody body);
	
	WtTableCaption caption(WtXmlAttributes xmlAttributes, WtBody body);
	
	WtTableCell td(WtXmlAttributes xmlAttributes, WtBody body);
	
	WtTableHeader th(WtXmlAttributes xmlAttributes, WtBody body);
	
	WtTableRow tr(WtXmlAttributes xmlAttributes, WtBody body);
	
	WtTagExtension tagExt(String name, WtXmlAttributes xmlAttributes);
	
	WtTagExtension tagExt(
			String name,
			WtXmlAttributes xmlAttributes,
			WtTagExtensionBody body);
	
	WtTemplate tmpl(WtName name, WtTemplateArguments args);
	
	WtTemplateArgument tmplArg(WtValue value);
	
	WtTemplateArgument tmplArg(WtName name, WtValue value);
	
	WtXmlElement elem(String name, WtXmlAttributes xmlAttributes);
	
	WtXmlElement elem(String name, WtXmlAttributes xmlAttributes, WtBody body);
	
	// -- Inner Node 3 ---------------------------------------------------------
	
	WtImageLink img(WtPageName target);
	
	WtImageLink img(WtPageName target, WtLinkOptions options);
	
	WtImageLink img(WtPageName target, WtLinkOptions options, WtLinkTitle title);
	
	WtImageLink img(
			WtPageName target,
			WtLinkOptions options,
			ImageViewFormat format,
			boolean border,
			ImageHorizAlign hAlign,
			ImageVertAlign vAlign,
			int width,
			int height,
			boolean upright,
			ImageLinkTarget link,
			WtLinkOptionAltText alt);
	
	WtTemplateParameter tmplParam(WtName name);
	
	WtTemplateParameter tmplParam(WtName name, WtValue defaultValue);
	
	WtTemplateParameter tmplParam(
			WtName name,
			WtValue defaultValue,
			WtTemplateArguments garbage);
	
	// -- Leaf Node ------------------------------------------------------------
	
	WtHorizontalRule hr();
	
	WtIllegalCodePoint illegalCp(String codePoint, IllegalCodePointType type);
	
	WtLinkOptionKeyword loKeyword(String keyword);
	
	WtLinkOptionResize loResize(int width, int height);
	
	WtPageSwitch pageSwitch(String name);
	
	WtSignature sig(int tildeCount);
	
	WtTicks ticks(int tickCount);
	
	WtUrl url(String protocol, String path);
	
	WtXmlCharRef charRef(int codePoint);
	
	WtXmlEndTag endTag(String name);
	
	WtImEndTag imEndTag(IntermediateTags type, boolean synthetic);
	
	WtXmlEntityRef entityRef(String name, String resolved);
	
	// -- Node List ------------------------------------------------------------
	
	WtNodeList list();
	
	WtNodeList list(Collection<? extends WtNode> list);
	
	WtNodeList list(Pair<? extends WtNode> list);
	
	WtNodeList list(WtNode child);
	
	WtNodeList list(Object... content);
	
	WtNodeList emptyList();
	
	WtNodeList toList(AstNode<WtNode> n);
	
	WtNodeList unwrap(AstNodeList<WtNode> n);
	
	// -- Content Node ---------------------------------------------------------
	
	WtBody body(WtNodeList content);
	
	WtBody emptyBody();
	
	WtBody noBody();
	
	WtBold b(WtNodeList content);
	
	WtDefinitionList dl(WtNodeList content);
	
	WtDefinitionListDef dd(WtNodeList content);
	
	WtDefinitionListTerm dt(WtNodeList content);
	
	WtHeading heading(WtNodeList content);
	
	WtItalics i(WtNodeList content);
	
	WtLinkOptionAltText loAlt(WtNodeList content);
	
	WtLinkOptionAltText noLoAlt();
	
	WtLinkOptions linkOpts(WtNodeList content);
	
	WtLinkOptions emptyLinkOpts();
	
	WtLinkTitle linkTitle(WtNodeList content);
	
	WtLinkTitle noLinkTitle();
	
	WtListItem li(WtNodeList content);
	
	WtName name(WtNodeList content);
	
	WtName noName();
	
	WtOnlyInclude onlyInc(WtNodeList content, XmlElementType elementType);
	
	WtOrderedList ol(WtNodeList content);
	
	WtParsedWikitextPage parsedPage(WtNodeList content);
	
	WtParsedWikitextPage parsedPage(
			WtNodeList content,
			WtEntityMap entityMap);
	
	WtPreproWikitextPage preproPage(WtNodeList content);
	
	WtPreproWikitextPage preproPage(
			WtNodeList content,
			WtEntityMap entityMap);
	
	WtParagraph p(WtNodeList content);
	
	WtSemiPre semiPre(WtNodeList content);
	
	WtSemiPreLine semiPreLine(WtNodeList content);
	
	WtTemplateArguments tmplArgs(WtNodeList content);
	
	WtTemplateArguments emptyTmplArgs();
	
	WtUnorderedList ul(WtNodeList content);
	
	WtValue value(WtNodeList content);
	
	WtValue noValue();
	
	WtWhitespace ws(WtNodeList content, boolean hasNewline);
	
	WtXmlAttributes attrs(WtNodeList content);
	
	WtXmlAttributes emptyAttrs();
	
	// -- String Node ----------------------------------------------------------
	
	WtIgnored ignored(String content);
	
	WtLinkOptionGarbage loGarbage(String content);
	
	WtNewline newline(String content);
	
	WtPageName pageName(String content);
	
	WtTagExtensionBody tagExtBody(String content);
	
	WtTagExtensionBody noTagExtBody();
	
	WtXmlAttributeGarbage attrGarbage(String content);
	
	WtXmlComment comment(String content);
	
	WtXmlComment comment(String prefix, String content, String suffix);
	
	// -- Text -----------------------------------------------------------------
	
	WtText text(String content);
}