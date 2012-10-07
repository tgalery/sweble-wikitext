package org.sweble.wikitext.parser.nodes;

import org.sweble.wikitext.parser.WtRtData;

import de.fau.cs.osr.ptk.common.ast.AstNode;

/* 
 * @startuml img/WtNode.png
 * 
 * interface WtNode {
 * 	WtRtData setRtd(WtRtData rtd);
 * 	WtRtData setRtd(Object... glue);
 * 	WtRtData setRtd(String... glue);
 * 	WtRtData getRtd();
 * 	void clearRtd();
 * }
 * 
 * @enduml
 */
public interface WtNode
		extends
			AstNode<WtNode>
{
	public WtRtData setRtd(WtRtData rtd);
	
	public WtRtData setRtd(Object... glue);
	
	public WtRtData setRtd(String... glue);
	
	public WtRtData getRtd();
	
	public void clearRtd();
	
	// =========================================================================
	
	public static final int NT_MW_NODE_BITS /* ................. */= NT_CUSTOM_BIT | 0x20000;
	
	public static final int NT_ENCVAL_BITS /* .................. */= NT_MW_NODE_BITS | 0x40000;
	
	public static final int NT_PREPROC_BITS /* ................. */= NT_MW_NODE_BITS | 0x60000;
	
	public static final int NT_PARSER_BITS /* .................. */= NT_MW_NODE_BITS | 0x80000;
	
	// -- General nodes --------------------------------------------------------
	
	public static final int NT_SPACES /* ....................... */= NT_MW_NODE_BITS + 1;
	
	public static final int NT_NEWLINE /* ...................... */= NT_MW_NODE_BITS + 2;
	
	public static final int NT_WHITESPACE /* ................... */= NT_MW_NODE_BITS + 3;
	
	public static final int NT_GARBAGE /* ...................... */= NT_MW_NODE_BITS + 4;
	
	public static final int NT_IGNORED /* ...................... */= NT_MW_NODE_BITS + 5;
	
	public static final int NT_PROTECTED_TEXT /* ............... */= NT_MW_NODE_BITS + 6;
	
	public static final int NT_ERROR /* ........................ */= NT_MW_NODE_BITS + 7;
	
	public static final int NT_NULL_NODE /* .................... */= NT_MW_NODE_BITS + 8;
	
	// -- Preprocessor nodes ---------------------------------------------------
	
	public static final int NT_ILLEGAL_CODE_POINT /* ........... */= NT_ENCVAL_BITS + 1;
	
	// -- Preprocessor nodes ---------------------------------------------------
	
	public static final int NT_PREPRO_WIKITEXT_PAGE /* ......... */= NT_PREPROC_BITS + 1;
	
	public static final int NT_XML_COMMENT /* .................. */= NT_PREPROC_BITS + 2;
	
	public static final int NT_TAG_EXTENSION /* ................ */= NT_PREPROC_BITS + 3;
	
	public static final int NT_TEMPLATE /* ..................... */= NT_PREPROC_BITS + 4;
	
	public static final int NT_TEMPLATE_ARGUMENT /* ............ */= NT_PREPROC_BITS + 5;
	
	public static final int NT_TEMPLATE_PARAMETER /* ........... */= NT_PREPROC_BITS + 6;
	
	public static final int NT_ONLY_INCLUDE /* ................. */= NT_PREPROC_BITS + 7;
	
	public static final int NT_REDIRECT /* ..................... */= NT_PREPROC_BITS + 8;
	
	public static final int NT_TEMPLATE_NAME/* ................. */= NT_PREPROC_BITS + 9;
	
	public static final int NT_TEMPLATE_ARGUMENTS/* ............ */= NT_PREPROC_BITS + 10;
	
	public static final int NT_TEMPLATE_VALUE/* ................ */= NT_PREPROC_BITS + 11;
	
	// -- Parser nodes ---------------------------------------------------------
	
	public static final int NT_PARSED_WIKITEXT_PAGE /* ......... */= NT_PARSER_BITS + 1;
	
	public static final int NT_PARAGRAPH /* .................... */= NT_PARSER_BITS + 2;
	
	public static final int NT_HORIZONTAL_RULE /* .............. */= NT_PARSER_BITS + 3;
	
	public static final int NT_URL /* .......................... */= NT_PARSER_BITS + 4;
	
	public static final int NT_EXTERNAL_LINK /* ................ */= NT_PARSER_BITS + 5;
	
	public static final int NT_INTERNAL_LINK /* ................ */= NT_PARSER_BITS + 6;
	
	public static final int NT_IMAGE_LINK /* ................... */= NT_PARSER_BITS + 7;
	
	public static final int NT_LINK_OPTION_ALT_TEXT /* ......... */= NT_PARSER_BITS + 8;
	
	public static final int NT_LINK_OPTION_GARBAGE /* .......... */= NT_PARSER_BITS + 9;
	
	public static final int NT_LINK_OPTION_KEYWORD /* .......... */= NT_PARSER_BITS + 10;
	
	public static final int NT_LINK_OPTION_LINK_TARGET /* ...... */= NT_PARSER_BITS + 11;
	
	public static final int NT_LINK_OPTION_RESIZE /* ........... */= NT_PARSER_BITS + 12;
	
	public static final int NT_PAGE_NAME /* .................... */= NT_PARSER_BITS + 13;
	
	public static final int NT_LINK_TITLE /* ................... */= NT_PARSER_BITS + 14;
	
	public static final int NT_TABLE /* ........................ */= NT_PARSER_BITS + 15;
	
	public static final int NT_TABLE_CAPTION /* ................ */= NT_PARSER_BITS + 16;
	
	public static final int NT_TABLE_ROW /* .................... */= NT_PARSER_BITS + 17;
	
	public static final int NT_TABLE_CELL /* ................... */= NT_PARSER_BITS + 18;
	
	public static final int NT_TABLE_HEADER /* ................. */= NT_PARSER_BITS + 19;
	
	public static final int NT_TICKS /* ........................ */= NT_PARSER_BITS + 20;
	
	public static final int NT_BOLD /* ......................... */= NT_PARSER_BITS + 21;
	
	public static final int NT_ITALICS /* ...................... */= NT_PARSER_BITS + 22;
	
	public static final int NT_RAW_LIST_ITEM /* ................ */= NT_PARSER_BITS + 23;
	
	public static final int NT_DEFINITION_LIST_DEF /* .......... */= NT_PARSER_BITS + 24;
	
	public static final int NT_DEFINITION_LIST /* .............. */= NT_PARSER_BITS + 25;
	
	public static final int NT_DEFINITION_LIST_TERM /* ......... */= NT_PARSER_BITS + 26;
	
	public static final int NT_ORDERED_LIST /* ................. */= NT_PARSER_BITS + 27;
	
	public static final int NT_UNORDERED_LIST /* ............... */= NT_PARSER_BITS + 29;
	
	public static final int NT_LIST_ITEM /* .................... */= NT_PARSER_BITS + 30;
	
	public static final int NT_SEMI_PRE /* ..................... */= NT_PARSER_BITS + 31;
	
	public static final int NT_SEMI_PRE_LINE /* ................ */= NT_PARSER_BITS + 32;
	
	public static final int NT_XML_ATTRIBUTE /* ................ */= NT_PARSER_BITS + 33;
	
	public static final int NT_XML_ATTRIBUTE_GARBAGE /* ........ */= NT_PARSER_BITS + 34;
	
	public static final int NT_XML_CHAR_REF /* ................. */= NT_PARSER_BITS + 35;
	
	public static final int NT_XML_ELEMENT /* .................. */= NT_PARSER_BITS + 36;
	
	public static final int NT_XML_ENTITY_REF /* ............... */= NT_PARSER_BITS + 37;
	
	public static final int NT_XML_END_TAG /* .................. */= NT_PARSER_BITS + 38;
	
	public static final int NT_XML_EMPTY_TAG /* ................ */= NT_PARSER_BITS + 39;
	
	public static final int NT_XML_START_TAG /* ................ */= NT_PARSER_BITS + 40;
	
	public static final int NT_HEADING /* ...................... */= NT_PARSER_BITS + 41;
	
	public static final int NT_SECTION /* ...................... */= NT_PARSER_BITS + 42;
	
	public static final int NT_PAGE_SWITCH /* .................. */= NT_PARSER_BITS + 43;
	
	public static final int NT_SIGNATURE /* .................... */= NT_PARSER_BITS + 44;
	
	public static final int NT_BODY /* ......................... */= NT_PARSER_BITS + 45;
	
	public static final int NT_LINK_OPTIONS /* ................. */= NT_PARSER_BITS + 46;
	
	public static final int NT_XML_ATTRIBUTES /* ............... */= NT_PARSER_BITS + 47;
}