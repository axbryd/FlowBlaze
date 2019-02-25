// Generated from /home/giacomo/IdeaProjects/xtrac/src/main/java/resources/xtra.g4 by ANTLR 4.7
package it.uniroma2.netprog.xtrac.parser;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class xtraParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		WS=1, COMMENT=2, LINE_COMMENT=3, GLOBAL_MODIFIER=4, INITIAL_MODIFIER=5, 
		REGISTER_DEF_KEYWORD=6, CONSTANT_DEF_KEYWORD=7, STATE_DEF_KEYWORD=8, CUSTOM_DEF_EVENT_KEYWORD=9, 
		MACRO_ACTION_DEF_KEYWORD=10, STAGE_DEF_KEYWORD=11, PACKET_FIELD_KEYWORD=12, 
		CONDITIONS_KEYWORD=13, EVENT_KEYWORD=14, SERIAL_KEYWORD=15, FILE_IMPORT=16, 
		IPV6_ADDR=17, IPV4_ADDR=18, MAC_ADDR=19, HEX_INTEGER=20, DECIMAL_INTEGER=21, 
		ID=22, INDEX=23, FILENAME=24, NOT_ZERO_OCTET=25, OCTET=26, LS32=27, HEX_16=28, 
		COLON=29, DOUBLE_COLON=30, DQUOTES=31, DOT=32, ASSIGNMENT=33, PLUS=34, 
		MINUS=35, MUL=36, DIV=37, MOD=38, MAX=39, MIN=40, COMMA=41, EQ=42, LEQ=43, 
		GEQ=44, NEQ=45, GRET=46, LESS=47, LPARENT=48, RPARENT=49, LBRACE=50, RBRACE=51, 
		LBRACK=52, RBRACK=53, SEMICOLON=54, SPACE=55, HEX_LITERAL=56, HEX_DIGIT=57, 
		LETTER=58, DIGIT=59;
	public static final int
		RULE_program = 0, RULE_stage_program = 1, RULE_state_program = 2, RULE_macro_action_definition = 3, 
		RULE_custom_event_definition = 4, RULE_stage_statement = 5, RULE_stage_body = 6, 
		RULE_state_statement = 7, RULE_state_body = 8, RULE_event_statement = 9, 
		RULE_event_body = 10, RULE_condition_statement = 11, RULE_condition_body = 12, 
		RULE_condition = 13, RULE_body = 14, RULE_serial = 15, RULE_action = 16, 
		RULE_call = 17, RULE_assignment = 18, RULE_expr = 19, RULE_term = 20, 
		RULE_index_access = 21, RULE_field_elem = 22, RULE_identifier = 23, RULE_file_import = 24, 
		RULE_file_name = 25, RULE_packet_field_definition = 26, RULE_packet_field_entry = 27, 
		RULE_register_definition = 28, RULE_constant_definition = 29, RULE_const_assignement = 30, 
		RULE_integer = 31, RULE_addr = 32;
	public static final String[] ruleNames = {
		"program", "stage_program", "state_program", "macro_action_definition", 
		"custom_event_definition", "stage_statement", "stage_body", "state_statement", 
		"state_body", "event_statement", "event_body", "condition_statement", 
		"condition_body", "condition", "body", "serial", "action", "call", "assignment", 
		"expr", "term", "index_access", "field_elem", "identifier", "file_import", 
		"file_name", "packet_field_definition", "packet_field_entry", "register_definition", 
		"constant_definition", "const_assignement", "integer", "addr"
	};

	private static final String[] _LITERAL_NAMES = {
		null, null, null, null, "'global'", "'initial'", "'Register'", "'Constant'", 
		"'State'", "'Event'", "'Action'", "'Stage'", "'PacketField'", "'if'", 
		"'on'", "'serial'", "'import'", null, null, null, null, null, null, null, 
		null, null, null, null, null, "':'", null, "'\"'", "'.'", "'='", "'+'", 
		"'-'", "'*'", "'/'", "'%'", "'max'", "'min'", "','", "'=='", "'<='", "'>='", 
		"'!='", "'>'", "'<'", "'('", "')'", "'{'", "'}'", "'['", "']'", "';'", 
		"' '"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, "WS", "COMMENT", "LINE_COMMENT", "GLOBAL_MODIFIER", "INITIAL_MODIFIER", 
		"REGISTER_DEF_KEYWORD", "CONSTANT_DEF_KEYWORD", "STATE_DEF_KEYWORD", "CUSTOM_DEF_EVENT_KEYWORD", 
		"MACRO_ACTION_DEF_KEYWORD", "STAGE_DEF_KEYWORD", "PACKET_FIELD_KEYWORD", 
		"CONDITIONS_KEYWORD", "EVENT_KEYWORD", "SERIAL_KEYWORD", "FILE_IMPORT", 
		"IPV6_ADDR", "IPV4_ADDR", "MAC_ADDR", "HEX_INTEGER", "DECIMAL_INTEGER", 
		"ID", "INDEX", "FILENAME", "NOT_ZERO_OCTET", "OCTET", "LS32", "HEX_16", 
		"COLON", "DOUBLE_COLON", "DQUOTES", "DOT", "ASSIGNMENT", "PLUS", "MINUS", 
		"MUL", "DIV", "MOD", "MAX", "MIN", "COMMA", "EQ", "LEQ", "GEQ", "NEQ", 
		"GRET", "LESS", "LPARENT", "RPARENT", "LBRACE", "RBRACE", "LBRACK", "RBRACK", 
		"SEMICOLON", "SPACE", "HEX_LITERAL", "HEX_DIGIT", "LETTER", "DIGIT"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "xtra.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public xtraParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class ProgramContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(xtraParser.EOF, 0); }
		public Stage_programContext stage_program() {
			return getRuleContext(Stage_programContext.class,0);
		}
		public State_programContext state_program() {
			return getRuleContext(State_programContext.class,0);
		}
		public List<Packet_field_definitionContext> packet_field_definition() {
			return getRuleContexts(Packet_field_definitionContext.class);
		}
		public Packet_field_definitionContext packet_field_definition(int i) {
			return getRuleContext(Packet_field_definitionContext.class,i);
		}
		public List<File_importContext> file_import() {
			return getRuleContexts(File_importContext.class);
		}
		public File_importContext file_import(int i) {
			return getRuleContext(File_importContext.class,i);
		}
		public List<Constant_definitionContext> constant_definition() {
			return getRuleContexts(Constant_definitionContext.class);
		}
		public Constant_definitionContext constant_definition(int i) {
			return getRuleContext(Constant_definitionContext.class,i);
		}
		public ProgramContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_program; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof xtraListener ) ((xtraListener)listener).enterProgram(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof xtraListener ) ((xtraListener)listener).exitProgram(this);
		}
	}

	public final ProgramContext program() throws RecognitionException {
		ProgramContext _localctx = new ProgramContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_program);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(71);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << CONSTANT_DEF_KEYWORD) | (1L << PACKET_FIELD_KEYWORD) | (1L << FILE_IMPORT))) != 0)) {
				{
				setState(69);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case PACKET_FIELD_KEYWORD:
					{
					setState(66);
					packet_field_definition();
					}
					break;
				case FILE_IMPORT:
					{
					setState(67);
					file_import();
					}
					break;
				case CONSTANT_DEF_KEYWORD:
					{
					setState(68);
					constant_definition();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(73);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(76);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				{
				setState(74);
				stage_program();
				}
				break;
			case 2:
				{
				setState(75);
				state_program();
				}
				break;
			}
			setState(78);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Stage_programContext extends ParserRuleContext {
		public List<Register_definitionContext> register_definition() {
			return getRuleContexts(Register_definitionContext.class);
		}
		public Register_definitionContext register_definition(int i) {
			return getRuleContext(Register_definitionContext.class,i);
		}
		public List<Custom_event_definitionContext> custom_event_definition() {
			return getRuleContexts(Custom_event_definitionContext.class);
		}
		public Custom_event_definitionContext custom_event_definition(int i) {
			return getRuleContext(Custom_event_definitionContext.class,i);
		}
		public List<Macro_action_definitionContext> macro_action_definition() {
			return getRuleContexts(Macro_action_definitionContext.class);
		}
		public Macro_action_definitionContext macro_action_definition(int i) {
			return getRuleContext(Macro_action_definitionContext.class,i);
		}
		public List<Stage_statementContext> stage_statement() {
			return getRuleContexts(Stage_statementContext.class);
		}
		public Stage_statementContext stage_statement(int i) {
			return getRuleContext(Stage_statementContext.class,i);
		}
		public Stage_programContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stage_program; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof xtraListener ) ((xtraListener)listener).enterStage_program(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof xtraListener ) ((xtraListener)listener).exitStage_program(this);
		}
	}

	public final Stage_programContext stage_program() throws RecognitionException {
		Stage_programContext _localctx = new Stage_programContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_stage_program);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(85);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REGISTER_DEF_KEYWORD) | (1L << CUSTOM_DEF_EVENT_KEYWORD) | (1L << MACRO_ACTION_DEF_KEYWORD))) != 0)) {
				{
				setState(83);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case REGISTER_DEF_KEYWORD:
					{
					setState(80);
					register_definition();
					}
					break;
				case CUSTOM_DEF_EVENT_KEYWORD:
					{
					setState(81);
					custom_event_definition();
					}
					break;
				case MACRO_ACTION_DEF_KEYWORD:
					{
					setState(82);
					macro_action_definition();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(87);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(89); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(88);
				stage_statement();
				}
				}
				setState(91); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==STAGE_DEF_KEYWORD );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class State_programContext extends ParserRuleContext {
		public List<Register_definitionContext> register_definition() {
			return getRuleContexts(Register_definitionContext.class);
		}
		public Register_definitionContext register_definition(int i) {
			return getRuleContext(Register_definitionContext.class,i);
		}
		public List<Custom_event_definitionContext> custom_event_definition() {
			return getRuleContexts(Custom_event_definitionContext.class);
		}
		public Custom_event_definitionContext custom_event_definition(int i) {
			return getRuleContext(Custom_event_definitionContext.class,i);
		}
		public List<Macro_action_definitionContext> macro_action_definition() {
			return getRuleContexts(Macro_action_definitionContext.class);
		}
		public Macro_action_definitionContext macro_action_definition(int i) {
			return getRuleContext(Macro_action_definitionContext.class,i);
		}
		public List<State_statementContext> state_statement() {
			return getRuleContexts(State_statementContext.class);
		}
		public State_statementContext state_statement(int i) {
			return getRuleContext(State_statementContext.class,i);
		}
		public State_programContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_state_program; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof xtraListener ) ((xtraListener)listener).enterState_program(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof xtraListener ) ((xtraListener)listener).exitState_program(this);
		}
	}

	public final State_programContext state_program() throws RecognitionException {
		State_programContext _localctx = new State_programContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_state_program);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(98);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REGISTER_DEF_KEYWORD) | (1L << CUSTOM_DEF_EVENT_KEYWORD) | (1L << MACRO_ACTION_DEF_KEYWORD))) != 0)) {
				{
				setState(96);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case REGISTER_DEF_KEYWORD:
					{
					setState(93);
					register_definition();
					}
					break;
				case CUSTOM_DEF_EVENT_KEYWORD:
					{
					setState(94);
					custom_event_definition();
					}
					break;
				case MACRO_ACTION_DEF_KEYWORD:
					{
					setState(95);
					macro_action_definition();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(100);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(102); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(101);
				state_statement();
				}
				}
				setState(104); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==STATE_DEF_KEYWORD );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Macro_action_definitionContext extends ParserRuleContext {
		public TerminalNode MACRO_ACTION_DEF_KEYWORD() { return getToken(xtraParser.MACRO_ACTION_DEF_KEYWORD, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode LBRACE() { return getToken(xtraParser.LBRACE, 0); }
		public BodyContext body() {
			return getRuleContext(BodyContext.class,0);
		}
		public TerminalNode RBRACE() { return getToken(xtraParser.RBRACE, 0); }
		public Macro_action_definitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_macro_action_definition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof xtraListener ) ((xtraListener)listener).enterMacro_action_definition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof xtraListener ) ((xtraListener)listener).exitMacro_action_definition(this);
		}
	}

	public final Macro_action_definitionContext macro_action_definition() throws RecognitionException {
		Macro_action_definitionContext _localctx = new Macro_action_definitionContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_macro_action_definition);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(106);
			match(MACRO_ACTION_DEF_KEYWORD);
			setState(107);
			identifier();
			setState(108);
			match(LBRACE);
			setState(109);
			body();
			setState(110);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Custom_event_definitionContext extends ParserRuleContext {
		public TerminalNode CUSTOM_DEF_EVENT_KEYWORD() { return getToken(xtraParser.CUSTOM_DEF_EVENT_KEYWORD, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode ASSIGNMENT() { return getToken(xtraParser.ASSIGNMENT, 0); }
		public List<ConditionContext> condition() {
			return getRuleContexts(ConditionContext.class);
		}
		public ConditionContext condition(int i) {
			return getRuleContext(ConditionContext.class,i);
		}
		public TerminalNode SEMICOLON() { return getToken(xtraParser.SEMICOLON, 0); }
		public List<TerminalNode> COMMA() { return getTokens(xtraParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(xtraParser.COMMA, i);
		}
		public Custom_event_definitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_custom_event_definition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof xtraListener ) ((xtraListener)listener).enterCustom_event_definition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof xtraListener ) ((xtraListener)listener).exitCustom_event_definition(this);
		}
	}

	public final Custom_event_definitionContext custom_event_definition() throws RecognitionException {
		Custom_event_definitionContext _localctx = new Custom_event_definitionContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_custom_event_definition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(112);
			match(CUSTOM_DEF_EVENT_KEYWORD);
			setState(113);
			identifier();
			setState(114);
			match(ASSIGNMENT);
			setState(115);
			condition();
			setState(120);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(116);
				match(COMMA);
				setState(117);
				condition();
				}
				}
				setState(122);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(123);
			match(SEMICOLON);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Stage_statementContext extends ParserRuleContext {
		public TerminalNode STAGE_DEF_KEYWORD() { return getToken(xtraParser.STAGE_DEF_KEYWORD, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public Stage_bodyContext stage_body() {
			return getRuleContext(Stage_bodyContext.class,0);
		}
		public Stage_statementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stage_statement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof xtraListener ) ((xtraListener)listener).enterStage_statement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof xtraListener ) ((xtraListener)listener).exitStage_statement(this);
		}
	}

	public final Stage_statementContext stage_statement() throws RecognitionException {
		Stage_statementContext _localctx = new Stage_statementContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_stage_statement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(125);
			match(STAGE_DEF_KEYWORD);
			setState(126);
			identifier();
			setState(127);
			stage_body();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Stage_bodyContext extends ParserRuleContext {
		public TerminalNode LBRACE() { return getToken(xtraParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(xtraParser.RBRACE, 0); }
		public List<Register_definitionContext> register_definition() {
			return getRuleContexts(Register_definitionContext.class);
		}
		public Register_definitionContext register_definition(int i) {
			return getRuleContext(Register_definitionContext.class,i);
		}
		public List<Custom_event_definitionContext> custom_event_definition() {
			return getRuleContexts(Custom_event_definitionContext.class);
		}
		public Custom_event_definitionContext custom_event_definition(int i) {
			return getRuleContext(Custom_event_definitionContext.class,i);
		}
		public List<Macro_action_definitionContext> macro_action_definition() {
			return getRuleContexts(Macro_action_definitionContext.class);
		}
		public Macro_action_definitionContext macro_action_definition(int i) {
			return getRuleContext(Macro_action_definitionContext.class,i);
		}
		public BodyContext body() {
			return getRuleContext(BodyContext.class,0);
		}
		public List<State_statementContext> state_statement() {
			return getRuleContexts(State_statementContext.class);
		}
		public State_statementContext state_statement(int i) {
			return getRuleContext(State_statementContext.class,i);
		}
		public Stage_bodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stage_body; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof xtraListener ) ((xtraListener)listener).enterStage_body(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof xtraListener ) ((xtraListener)listener).exitStage_body(this);
		}
	}

	public final Stage_bodyContext stage_body() throws RecognitionException {
		Stage_bodyContext _localctx = new Stage_bodyContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_stage_body);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(129);
			match(LBRACE);
			setState(135);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << REGISTER_DEF_KEYWORD) | (1L << CUSTOM_DEF_EVENT_KEYWORD) | (1L << MACRO_ACTION_DEF_KEYWORD))) != 0)) {
				{
				setState(133);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case REGISTER_DEF_KEYWORD:
					{
					setState(130);
					register_definition();
					}
					break;
				case CUSTOM_DEF_EVENT_KEYWORD:
					{
					setState(131);
					custom_event_definition();
					}
					break;
				case MACRO_ACTION_DEF_KEYWORD:
					{
					setState(132);
					macro_action_definition();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(137);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(139);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,12,_ctx) ) {
			case 1:
				{
				setState(138);
				body();
				}
				break;
			}
			setState(142); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(141);
				state_statement();
				}
				}
				setState(144); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==STATE_DEF_KEYWORD );
			setState(146);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class State_statementContext extends ParserRuleContext {
		public TerminalNode STATE_DEF_KEYWORD() { return getToken(xtraParser.STATE_DEF_KEYWORD, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public State_bodyContext state_body() {
			return getRuleContext(State_bodyContext.class,0);
		}
		public TerminalNode INITIAL_MODIFIER() { return getToken(xtraParser.INITIAL_MODIFIER, 0); }
		public State_statementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_state_statement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof xtraListener ) ((xtraListener)listener).enterState_statement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof xtraListener ) ((xtraListener)listener).exitState_statement(this);
		}
	}

	public final State_statementContext state_statement() throws RecognitionException {
		State_statementContext _localctx = new State_statementContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_state_statement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(148);
			match(STATE_DEF_KEYWORD);
			setState(150);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==INITIAL_MODIFIER) {
				{
				setState(149);
				match(INITIAL_MODIFIER);
				}
			}

			setState(152);
			identifier();
			setState(153);
			state_body();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class State_bodyContext extends ParserRuleContext {
		public TerminalNode LBRACE() { return getToken(xtraParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(xtraParser.RBRACE, 0); }
		public BodyContext body() {
			return getRuleContext(BodyContext.class,0);
		}
		public List<Event_statementContext> event_statement() {
			return getRuleContexts(Event_statementContext.class);
		}
		public Event_statementContext event_statement(int i) {
			return getRuleContext(Event_statementContext.class,i);
		}
		public State_bodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_state_body; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof xtraListener ) ((xtraListener)listener).enterState_body(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof xtraListener ) ((xtraListener)listener).exitState_body(this);
		}
	}

	public final State_bodyContext state_body() throws RecognitionException {
		State_bodyContext _localctx = new State_bodyContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_state_body);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(155);
			match(LBRACE);
			setState(157);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,15,_ctx) ) {
			case 1:
				{
				setState(156);
				body();
				}
				break;
			}
			setState(162);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==EVENT_KEYWORD) {
				{
				{
				setState(159);
				event_statement();
				}
				}
				setState(164);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(165);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Event_statementContext extends ParserRuleContext {
		public TerminalNode EVENT_KEYWORD() { return getToken(xtraParser.EVENT_KEYWORD, 0); }
		public TerminalNode LPARENT() { return getToken(xtraParser.LPARENT, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode RPARENT() { return getToken(xtraParser.RPARENT, 0); }
		public Event_bodyContext event_body() {
			return getRuleContext(Event_bodyContext.class,0);
		}
		public Event_statementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_event_statement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof xtraListener ) ((xtraListener)listener).enterEvent_statement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof xtraListener ) ((xtraListener)listener).exitEvent_statement(this);
		}
	}

	public final Event_statementContext event_statement() throws RecognitionException {
		Event_statementContext _localctx = new Event_statementContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_event_statement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(167);
			match(EVENT_KEYWORD);
			setState(168);
			match(LPARENT);
			setState(169);
			identifier();
			setState(170);
			match(RPARENT);
			setState(171);
			event_body();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Event_bodyContext extends ParserRuleContext {
		public TerminalNode LBRACE() { return getToken(xtraParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(xtraParser.RBRACE, 0); }
		public BodyContext body() {
			return getRuleContext(BodyContext.class,0);
		}
		public List<Condition_statementContext> condition_statement() {
			return getRuleContexts(Condition_statementContext.class);
		}
		public Condition_statementContext condition_statement(int i) {
			return getRuleContext(Condition_statementContext.class,i);
		}
		public Event_bodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_event_body; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof xtraListener ) ((xtraListener)listener).enterEvent_body(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof xtraListener ) ((xtraListener)listener).exitEvent_body(this);
		}
	}

	public final Event_bodyContext event_body() throws RecognitionException {
		Event_bodyContext _localctx = new Event_bodyContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_event_body);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(173);
			match(LBRACE);
			setState(175);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,17,_ctx) ) {
			case 1:
				{
				setState(174);
				body();
				}
				break;
			}
			setState(180);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==CONDITIONS_KEYWORD) {
				{
				{
				setState(177);
				condition_statement();
				}
				}
				setState(182);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(183);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Condition_statementContext extends ParserRuleContext {
		public TerminalNode CONDITIONS_KEYWORD() { return getToken(xtraParser.CONDITIONS_KEYWORD, 0); }
		public TerminalNode LPARENT() { return getToken(xtraParser.LPARENT, 0); }
		public List<ConditionContext> condition() {
			return getRuleContexts(ConditionContext.class);
		}
		public ConditionContext condition(int i) {
			return getRuleContext(ConditionContext.class,i);
		}
		public TerminalNode RPARENT() { return getToken(xtraParser.RPARENT, 0); }
		public Condition_bodyContext condition_body() {
			return getRuleContext(Condition_bodyContext.class,0);
		}
		public List<TerminalNode> COMMA() { return getTokens(xtraParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(xtraParser.COMMA, i);
		}
		public Condition_statementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_condition_statement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof xtraListener ) ((xtraListener)listener).enterCondition_statement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof xtraListener ) ((xtraListener)listener).exitCondition_statement(this);
		}
	}

	public final Condition_statementContext condition_statement() throws RecognitionException {
		Condition_statementContext _localctx = new Condition_statementContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_condition_statement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(185);
			match(CONDITIONS_KEYWORD);
			setState(186);
			match(LPARENT);
			setState(187);
			condition();
			setState(192);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(188);
				match(COMMA);
				setState(189);
				condition();
				}
				}
				setState(194);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(195);
			match(RPARENT);
			setState(196);
			condition_body();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Condition_bodyContext extends ParserRuleContext {
		public TerminalNode LBRACE() { return getToken(xtraParser.LBRACE, 0); }
		public BodyContext body() {
			return getRuleContext(BodyContext.class,0);
		}
		public TerminalNode RBRACE() { return getToken(xtraParser.RBRACE, 0); }
		public Condition_bodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_condition_body; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof xtraListener ) ((xtraListener)listener).enterCondition_body(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof xtraListener ) ((xtraListener)listener).exitCondition_body(this);
		}
	}

	public final Condition_bodyContext condition_body() throws RecognitionException {
		Condition_bodyContext _localctx = new Condition_bodyContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_condition_body);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(198);
			match(LBRACE);
			setState(199);
			body();
			setState(200);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ConditionContext extends ParserRuleContext {
		public List<TermContext> term() {
			return getRuleContexts(TermContext.class);
		}
		public TermContext term(int i) {
			return getRuleContext(TermContext.class,i);
		}
		public TerminalNode EQ() { return getToken(xtraParser.EQ, 0); }
		public TerminalNode LEQ() { return getToken(xtraParser.LEQ, 0); }
		public TerminalNode GEQ() { return getToken(xtraParser.GEQ, 0); }
		public TerminalNode NEQ() { return getToken(xtraParser.NEQ, 0); }
		public TerminalNode GRET() { return getToken(xtraParser.GRET, 0); }
		public TerminalNode LESS() { return getToken(xtraParser.LESS, 0); }
		public ConditionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_condition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof xtraListener ) ((xtraListener)listener).enterCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof xtraListener ) ((xtraListener)listener).exitCondition(this);
		}
	}

	public final ConditionContext condition() throws RecognitionException {
		ConditionContext _localctx = new ConditionContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_condition);
		try {
			setState(226);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,20,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(202);
				term();
				setState(203);
				match(EQ);
				setState(204);
				term();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(206);
				term();
				setState(207);
				match(LEQ);
				setState(208);
				term();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(210);
				term();
				setState(211);
				match(GEQ);
				setState(212);
				term();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(214);
				term();
				setState(215);
				match(NEQ);
				setState(216);
				term();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(218);
				term();
				setState(219);
				match(GRET);
				setState(220);
				term();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(222);
				term();
				setState(223);
				match(LESS);
				setState(224);
				term();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BodyContext extends ParserRuleContext {
		public List<ActionContext> action() {
			return getRuleContexts(ActionContext.class);
		}
		public ActionContext action(int i) {
			return getRuleContext(ActionContext.class,i);
		}
		public List<SerialContext> serial() {
			return getRuleContexts(SerialContext.class);
		}
		public SerialContext serial(int i) {
			return getRuleContext(SerialContext.class,i);
		}
		public BodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_body; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof xtraListener ) ((xtraListener)listener).enterBody(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof xtraListener ) ((xtraListener)listener).exitBody(this);
		}
	}

	public final BodyContext body() throws RecognitionException {
		BodyContext _localctx = new BodyContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_body);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(232);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==SERIAL_KEYWORD || _la==ID) {
				{
				setState(230);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case ID:
					{
					setState(228);
					action();
					}
					break;
				case SERIAL_KEYWORD:
					{
					setState(229);
					serial();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(234);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SerialContext extends ParserRuleContext {
		public TerminalNode SERIAL_KEYWORD() { return getToken(xtraParser.SERIAL_KEYWORD, 0); }
		public TerminalNode LBRACE() { return getToken(xtraParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(xtraParser.RBRACE, 0); }
		public List<ActionContext> action() {
			return getRuleContexts(ActionContext.class);
		}
		public ActionContext action(int i) {
			return getRuleContext(ActionContext.class,i);
		}
		public SerialContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_serial; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof xtraListener ) ((xtraListener)listener).enterSerial(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof xtraListener ) ((xtraListener)listener).exitSerial(this);
		}
	}

	public final SerialContext serial() throws RecognitionException {
		SerialContext _localctx = new SerialContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_serial);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(235);
			match(SERIAL_KEYWORD);
			setState(236);
			match(LBRACE);
			setState(238); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(237);
				action();
				}
				}
				setState(240); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==ID );
			setState(242);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ActionContext extends ParserRuleContext {
		public CallContext call() {
			return getRuleContext(CallContext.class,0);
		}
		public AssignmentContext assignment() {
			return getRuleContext(AssignmentContext.class,0);
		}
		public ActionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_action; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof xtraListener ) ((xtraListener)listener).enterAction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof xtraListener ) ((xtraListener)listener).exitAction(this);
		}
	}

	public final ActionContext action() throws RecognitionException {
		ActionContext _localctx = new ActionContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_action);
		try {
			setState(246);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,24,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(244);
				call();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(245);
				assignment();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CallContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(xtraParser.ID, 0); }
		public TerminalNode LPARENT() { return getToken(xtraParser.LPARENT, 0); }
		public TerminalNode RPARENT() { return getToken(xtraParser.RPARENT, 0); }
		public TerminalNode SEMICOLON() { return getToken(xtraParser.SEMICOLON, 0); }
		public List<TermContext> term() {
			return getRuleContexts(TermContext.class);
		}
		public TermContext term(int i) {
			return getRuleContext(TermContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(xtraParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(xtraParser.COMMA, i);
		}
		public CallContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_call; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof xtraListener ) ((xtraListener)listener).enterCall(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof xtraListener ) ((xtraListener)listener).exitCall(this);
		}
	}

	public final CallContext call() throws RecognitionException {
		CallContext _localctx = new CallContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_call);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(248);
			match(ID);
			setState(249);
			match(LPARENT);
			setState(253);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << IPV6_ADDR) | (1L << IPV4_ADDR) | (1L << MAC_ADDR) | (1L << HEX_INTEGER) | (1L << DECIMAL_INTEGER) | (1L << ID))) != 0)) {
				{
				{
				setState(250);
				term();
				}
				}
				setState(255);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(260);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(256);
				match(COMMA);
				setState(257);
				term();
				}
				}
				setState(262);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(263);
			match(RPARENT);
			setState(264);
			match(SEMICOLON);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AssignmentContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode ASSIGNMENT() { return getToken(xtraParser.ASSIGNMENT, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode SEMICOLON() { return getToken(xtraParser.SEMICOLON, 0); }
		public AssignmentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assignment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof xtraListener ) ((xtraListener)listener).enterAssignment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof xtraListener ) ((xtraListener)listener).exitAssignment(this);
		}
	}

	public final AssignmentContext assignment() throws RecognitionException {
		AssignmentContext _localctx = new AssignmentContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_assignment);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(266);
			identifier();
			setState(267);
			match(ASSIGNMENT);
			setState(268);
			expr();
			setState(269);
			match(SEMICOLON);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExprContext extends ParserRuleContext {
		public List<TermContext> term() {
			return getRuleContexts(TermContext.class);
		}
		public TermContext term(int i) {
			return getRuleContext(TermContext.class,i);
		}
		public TerminalNode MUL() { return getToken(xtraParser.MUL, 0); }
		public TerminalNode DIV() { return getToken(xtraParser.DIV, 0); }
		public TerminalNode PLUS() { return getToken(xtraParser.PLUS, 0); }
		public TerminalNode MINUS() { return getToken(xtraParser.MINUS, 0); }
		public TerminalNode MOD() { return getToken(xtraParser.MOD, 0); }
		public TerminalNode LPARENT() { return getToken(xtraParser.LPARENT, 0); }
		public TerminalNode COMMA() { return getToken(xtraParser.COMMA, 0); }
		public TerminalNode RPARENT() { return getToken(xtraParser.RPARENT, 0); }
		public TerminalNode MAX() { return getToken(xtraParser.MAX, 0); }
		public TerminalNode MIN() { return getToken(xtraParser.MIN, 0); }
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof xtraListener ) ((xtraListener)listener).enterExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof xtraListener ) ((xtraListener)listener).exitExpr(this);
		}
	}

	public final ExprContext expr() throws RecognitionException {
		ExprContext _localctx = new ExprContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_expr);
		int _la;
		try {
			setState(299);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,27,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(271);
				term();
				setState(272);
				match(MUL);
				setState(273);
				term();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(275);
				term();
				setState(276);
				match(DIV);
				setState(277);
				term();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(279);
				term();
				setState(280);
				match(PLUS);
				setState(281);
				term();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(283);
				term();
				setState(284);
				match(MINUS);
				setState(285);
				term();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(287);
				term();
				setState(288);
				match(MOD);
				setState(289);
				term();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(291);
				_la = _input.LA(1);
				if ( !(_la==MAX || _la==MIN) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(292);
				match(LPARENT);
				setState(293);
				term();
				setState(294);
				match(COMMA);
				setState(295);
				term();
				setState(296);
				match(RPARENT);
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(298);
				term();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TermContext extends ParserRuleContext {
		public Field_elemContext field_elem() {
			return getRuleContext(Field_elemContext.class,0);
		}
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public IntegerContext integer() {
			return getRuleContext(IntegerContext.class,0);
		}
		public AddrContext addr() {
			return getRuleContext(AddrContext.class,0);
		}
		public Index_accessContext index_access() {
			return getRuleContext(Index_accessContext.class,0);
		}
		public TermContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_term; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof xtraListener ) ((xtraListener)listener).enterTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof xtraListener ) ((xtraListener)listener).exitTerm(this);
		}
	}

	public final TermContext term() throws RecognitionException {
		TermContext _localctx = new TermContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_term);
		try {
			setState(306);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,28,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(301);
				field_elem();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(302);
				identifier();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(303);
				integer();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(304);
				addr();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(305);
				index_access();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Index_accessContext extends ParserRuleContext {
		public Field_elemContext field_elem() {
			return getRuleContext(Field_elemContext.class,0);
		}
		public TerminalNode INDEX() { return getToken(xtraParser.INDEX, 0); }
		public Index_accessContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_index_access; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof xtraListener ) ((xtraListener)listener).enterIndex_access(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof xtraListener ) ((xtraListener)listener).exitIndex_access(this);
		}
	}

	public final Index_accessContext index_access() throws RecognitionException {
		Index_accessContext _localctx = new Index_accessContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_index_access);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(308);
			field_elem();
			setState(309);
			match(INDEX);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Field_elemContext extends ParserRuleContext {
		public List<TerminalNode> ID() { return getTokens(xtraParser.ID); }
		public TerminalNode ID(int i) {
			return getToken(xtraParser.ID, i);
		}
		public TerminalNode DOT() { return getToken(xtraParser.DOT, 0); }
		public Field_elemContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_field_elem; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof xtraListener ) ((xtraListener)listener).enterField_elem(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof xtraListener ) ((xtraListener)listener).exitField_elem(this);
		}
	}

	public final Field_elemContext field_elem() throws RecognitionException {
		Field_elemContext _localctx = new Field_elemContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_field_elem);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(311);
			match(ID);
			setState(312);
			match(DOT);
			setState(313);
			match(ID);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IdentifierContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(xtraParser.ID, 0); }
		public IdentifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_identifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof xtraListener ) ((xtraListener)listener).enterIdentifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof xtraListener ) ((xtraListener)listener).exitIdentifier(this);
		}
	}

	public final IdentifierContext identifier() throws RecognitionException {
		IdentifierContext _localctx = new IdentifierContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_identifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(315);
			match(ID);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class File_importContext extends ParserRuleContext {
		public TerminalNode FILE_IMPORT() { return getToken(xtraParser.FILE_IMPORT, 0); }
		public List<File_nameContext> file_name() {
			return getRuleContexts(File_nameContext.class);
		}
		public File_nameContext file_name(int i) {
			return getRuleContext(File_nameContext.class,i);
		}
		public TerminalNode SEMICOLON() { return getToken(xtraParser.SEMICOLON, 0); }
		public List<TerminalNode> COMMA() { return getTokens(xtraParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(xtraParser.COMMA, i);
		}
		public File_importContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_file_import; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof xtraListener ) ((xtraListener)listener).enterFile_import(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof xtraListener ) ((xtraListener)listener).exitFile_import(this);
		}
	}

	public final File_importContext file_import() throws RecognitionException {
		File_importContext _localctx = new File_importContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_file_import);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(317);
			match(FILE_IMPORT);
			setState(318);
			file_name();
			setState(323);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(319);
				match(COMMA);
				setState(320);
				file_name();
				}
				}
				setState(325);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(326);
			match(SEMICOLON);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class File_nameContext extends ParserRuleContext {
		public TerminalNode FILENAME() { return getToken(xtraParser.FILENAME, 0); }
		public File_nameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_file_name; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof xtraListener ) ((xtraListener)listener).enterFile_name(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof xtraListener ) ((xtraListener)listener).exitFile_name(this);
		}
	}

	public final File_nameContext file_name() throws RecognitionException {
		File_nameContext _localctx = new File_nameContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_file_name);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(328);
			match(FILENAME);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Packet_field_definitionContext extends ParserRuleContext {
		public TerminalNode PACKET_FIELD_KEYWORD() { return getToken(xtraParser.PACKET_FIELD_KEYWORD, 0); }
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode LBRACE() { return getToken(xtraParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(xtraParser.RBRACE, 0); }
		public List<Packet_field_entryContext> packet_field_entry() {
			return getRuleContexts(Packet_field_entryContext.class);
		}
		public Packet_field_entryContext packet_field_entry(int i) {
			return getRuleContext(Packet_field_entryContext.class,i);
		}
		public Packet_field_definitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_packet_field_definition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof xtraListener ) ((xtraListener)listener).enterPacket_field_definition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof xtraListener ) ((xtraListener)listener).exitPacket_field_definition(this);
		}
	}

	public final Packet_field_definitionContext packet_field_definition() throws RecognitionException {
		Packet_field_definitionContext _localctx = new Packet_field_definitionContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_packet_field_definition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(330);
			match(PACKET_FIELD_KEYWORD);
			setState(331);
			identifier();
			setState(332);
			match(LBRACE);
			setState(334); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(333);
				packet_field_entry();
				}
				}
				setState(336); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==ID );
			setState(338);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Packet_field_entryContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode ASSIGNMENT() { return getToken(xtraParser.ASSIGNMENT, 0); }
		public Index_accessContext index_access() {
			return getRuleContext(Index_accessContext.class,0);
		}
		public TerminalNode SEMICOLON() { return getToken(xtraParser.SEMICOLON, 0); }
		public Packet_field_entryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_packet_field_entry; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof xtraListener ) ((xtraListener)listener).enterPacket_field_entry(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof xtraListener ) ((xtraListener)listener).exitPacket_field_entry(this);
		}
	}

	public final Packet_field_entryContext packet_field_entry() throws RecognitionException {
		Packet_field_entryContext _localctx = new Packet_field_entryContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_packet_field_entry);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(340);
			identifier();
			setState(341);
			match(ASSIGNMENT);
			setState(342);
			index_access();
			setState(343);
			match(SEMICOLON);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Register_definitionContext extends ParserRuleContext {
		public TerminalNode REGISTER_DEF_KEYWORD() { return getToken(xtraParser.REGISTER_DEF_KEYWORD, 0); }
		public List<IdentifierContext> identifier() {
			return getRuleContexts(IdentifierContext.class);
		}
		public IdentifierContext identifier(int i) {
			return getRuleContext(IdentifierContext.class,i);
		}
		public TerminalNode SEMICOLON() { return getToken(xtraParser.SEMICOLON, 0); }
		public TerminalNode GLOBAL_MODIFIER() { return getToken(xtraParser.GLOBAL_MODIFIER, 0); }
		public List<TerminalNode> COMMA() { return getTokens(xtraParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(xtraParser.COMMA, i);
		}
		public Register_definitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_register_definition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof xtraListener ) ((xtraListener)listener).enterRegister_definition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof xtraListener ) ((xtraListener)listener).exitRegister_definition(this);
		}
	}

	public final Register_definitionContext register_definition() throws RecognitionException {
		Register_definitionContext _localctx = new Register_definitionContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_register_definition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(345);
			match(REGISTER_DEF_KEYWORD);
			setState(347);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==GLOBAL_MODIFIER) {
				{
				setState(346);
				match(GLOBAL_MODIFIER);
				}
			}

			setState(349);
			identifier();
			setState(354);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(350);
				match(COMMA);
				setState(351);
				identifier();
				}
				}
				setState(356);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(357);
			match(SEMICOLON);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Constant_definitionContext extends ParserRuleContext {
		public TerminalNode CONSTANT_DEF_KEYWORD() { return getToken(xtraParser.CONSTANT_DEF_KEYWORD, 0); }
		public List<Const_assignementContext> const_assignement() {
			return getRuleContexts(Const_assignementContext.class);
		}
		public Const_assignementContext const_assignement(int i) {
			return getRuleContext(Const_assignementContext.class,i);
		}
		public TerminalNode SEMICOLON() { return getToken(xtraParser.SEMICOLON, 0); }
		public List<TerminalNode> COMMA() { return getTokens(xtraParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(xtraParser.COMMA, i);
		}
		public Constant_definitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constant_definition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof xtraListener ) ((xtraListener)listener).enterConstant_definition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof xtraListener ) ((xtraListener)listener).exitConstant_definition(this);
		}
	}

	public final Constant_definitionContext constant_definition() throws RecognitionException {
		Constant_definitionContext _localctx = new Constant_definitionContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_constant_definition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(359);
			match(CONSTANT_DEF_KEYWORD);
			setState(360);
			const_assignement();
			setState(365);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(361);
				match(COMMA);
				setState(362);
				const_assignement();
				}
				}
				setState(367);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(368);
			match(SEMICOLON);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Const_assignementContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode ASSIGNMENT() { return getToken(xtraParser.ASSIGNMENT, 0); }
		public IntegerContext integer() {
			return getRuleContext(IntegerContext.class,0);
		}
		public AddrContext addr() {
			return getRuleContext(AddrContext.class,0);
		}
		public Const_assignementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_const_assignement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof xtraListener ) ((xtraListener)listener).enterConst_assignement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof xtraListener ) ((xtraListener)listener).exitConst_assignement(this);
		}
	}

	public final Const_assignementContext const_assignement() throws RecognitionException {
		Const_assignementContext _localctx = new Const_assignementContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_const_assignement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(370);
			identifier();
			setState(371);
			match(ASSIGNMENT);
			setState(374);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case HEX_INTEGER:
			case DECIMAL_INTEGER:
				{
				setState(372);
				integer();
				}
				break;
			case IPV6_ADDR:
			case IPV4_ADDR:
			case MAC_ADDR:
				{
				setState(373);
				addr();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IntegerContext extends ParserRuleContext {
		public TerminalNode DECIMAL_INTEGER() { return getToken(xtraParser.DECIMAL_INTEGER, 0); }
		public TerminalNode HEX_INTEGER() { return getToken(xtraParser.HEX_INTEGER, 0); }
		public IntegerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_integer; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof xtraListener ) ((xtraListener)listener).enterInteger(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof xtraListener ) ((xtraListener)listener).exitInteger(this);
		}
	}

	public final IntegerContext integer() throws RecognitionException {
		IntegerContext _localctx = new IntegerContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_integer);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(376);
			_la = _input.LA(1);
			if ( !(_la==HEX_INTEGER || _la==DECIMAL_INTEGER) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AddrContext extends ParserRuleContext {
		public TerminalNode IPV4_ADDR() { return getToken(xtraParser.IPV4_ADDR, 0); }
		public TerminalNode IPV6_ADDR() { return getToken(xtraParser.IPV6_ADDR, 0); }
		public TerminalNode MAC_ADDR() { return getToken(xtraParser.MAC_ADDR, 0); }
		public AddrContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_addr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof xtraListener ) ((xtraListener)listener).enterAddr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof xtraListener ) ((xtraListener)listener).exitAddr(this);
		}
	}

	public final AddrContext addr() throws RecognitionException {
		AddrContext _localctx = new AddrContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_addr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(378);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << IPV6_ADDR) | (1L << IPV4_ADDR) | (1L << MAC_ADDR))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3=\u017f\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\3\2\3\2\3\2\7\2H\n\2\f\2\16\2K\13\2\3\2\3\2\5\2O\n\2\3\2\3"+
		"\2\3\3\3\3\3\3\7\3V\n\3\f\3\16\3Y\13\3\3\3\6\3\\\n\3\r\3\16\3]\3\4\3\4"+
		"\3\4\7\4c\n\4\f\4\16\4f\13\4\3\4\6\4i\n\4\r\4\16\4j\3\5\3\5\3\5\3\5\3"+
		"\5\3\5\3\6\3\6\3\6\3\6\3\6\3\6\7\6y\n\6\f\6\16\6|\13\6\3\6\3\6\3\7\3\7"+
		"\3\7\3\7\3\b\3\b\3\b\3\b\7\b\u0088\n\b\f\b\16\b\u008b\13\b\3\b\5\b\u008e"+
		"\n\b\3\b\6\b\u0091\n\b\r\b\16\b\u0092\3\b\3\b\3\t\3\t\5\t\u0099\n\t\3"+
		"\t\3\t\3\t\3\n\3\n\5\n\u00a0\n\n\3\n\7\n\u00a3\n\n\f\n\16\n\u00a6\13\n"+
		"\3\n\3\n\3\13\3\13\3\13\3\13\3\13\3\13\3\f\3\f\5\f\u00b2\n\f\3\f\7\f\u00b5"+
		"\n\f\f\f\16\f\u00b8\13\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r\7\r\u00c1\n\r\f\r"+
		"\16\r\u00c4\13\r\3\r\3\r\3\r\3\16\3\16\3\16\3\16\3\17\3\17\3\17\3\17\3"+
		"\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3"+
		"\17\3\17\3\17\3\17\3\17\3\17\5\17\u00e5\n\17\3\20\3\20\7\20\u00e9\n\20"+
		"\f\20\16\20\u00ec\13\20\3\21\3\21\3\21\6\21\u00f1\n\21\r\21\16\21\u00f2"+
		"\3\21\3\21\3\22\3\22\5\22\u00f9\n\22\3\23\3\23\3\23\7\23\u00fe\n\23\f"+
		"\23\16\23\u0101\13\23\3\23\3\23\7\23\u0105\n\23\f\23\16\23\u0108\13\23"+
		"\3\23\3\23\3\23\3\24\3\24\3\24\3\24\3\24\3\25\3\25\3\25\3\25\3\25\3\25"+
		"\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25"+
		"\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\5\25\u012e\n\25\3\26\3\26\3\26"+
		"\3\26\3\26\5\26\u0135\n\26\3\27\3\27\3\27\3\30\3\30\3\30\3\30\3\31\3\31"+
		"\3\32\3\32\3\32\3\32\7\32\u0144\n\32\f\32\16\32\u0147\13\32\3\32\3\32"+
		"\3\33\3\33\3\34\3\34\3\34\3\34\6\34\u0151\n\34\r\34\16\34\u0152\3\34\3"+
		"\34\3\35\3\35\3\35\3\35\3\35\3\36\3\36\5\36\u015e\n\36\3\36\3\36\3\36"+
		"\7\36\u0163\n\36\f\36\16\36\u0166\13\36\3\36\3\36\3\37\3\37\3\37\3\37"+
		"\7\37\u016e\n\37\f\37\16\37\u0171\13\37\3\37\3\37\3 \3 \3 \3 \5 \u0179"+
		"\n \3!\3!\3\"\3\"\3\"\2\2#\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$"+
		"&(*,.\60\62\64\668:<>@B\2\5\3\2)*\3\2\26\27\3\2\23\25\2\u0190\2I\3\2\2"+
		"\2\4W\3\2\2\2\6d\3\2\2\2\bl\3\2\2\2\nr\3\2\2\2\f\177\3\2\2\2\16\u0083"+
		"\3\2\2\2\20\u0096\3\2\2\2\22\u009d\3\2\2\2\24\u00a9\3\2\2\2\26\u00af\3"+
		"\2\2\2\30\u00bb\3\2\2\2\32\u00c8\3\2\2\2\34\u00e4\3\2\2\2\36\u00ea\3\2"+
		"\2\2 \u00ed\3\2\2\2\"\u00f8\3\2\2\2$\u00fa\3\2\2\2&\u010c\3\2\2\2(\u012d"+
		"\3\2\2\2*\u0134\3\2\2\2,\u0136\3\2\2\2.\u0139\3\2\2\2\60\u013d\3\2\2\2"+
		"\62\u013f\3\2\2\2\64\u014a\3\2\2\2\66\u014c\3\2\2\28\u0156\3\2\2\2:\u015b"+
		"\3\2\2\2<\u0169\3\2\2\2>\u0174\3\2\2\2@\u017a\3\2\2\2B\u017c\3\2\2\2D"+
		"H\5\66\34\2EH\5\62\32\2FH\5<\37\2GD\3\2\2\2GE\3\2\2\2GF\3\2\2\2HK\3\2"+
		"\2\2IG\3\2\2\2IJ\3\2\2\2JN\3\2\2\2KI\3\2\2\2LO\5\4\3\2MO\5\6\4\2NL\3\2"+
		"\2\2NM\3\2\2\2OP\3\2\2\2PQ\7\2\2\3Q\3\3\2\2\2RV\5:\36\2SV\5\n\6\2TV\5"+
		"\b\5\2UR\3\2\2\2US\3\2\2\2UT\3\2\2\2VY\3\2\2\2WU\3\2\2\2WX\3\2\2\2X[\3"+
		"\2\2\2YW\3\2\2\2Z\\\5\f\7\2[Z\3\2\2\2\\]\3\2\2\2][\3\2\2\2]^\3\2\2\2^"+
		"\5\3\2\2\2_c\5:\36\2`c\5\n\6\2ac\5\b\5\2b_\3\2\2\2b`\3\2\2\2ba\3\2\2\2"+
		"cf\3\2\2\2db\3\2\2\2de\3\2\2\2eh\3\2\2\2fd\3\2\2\2gi\5\20\t\2hg\3\2\2"+
		"\2ij\3\2\2\2jh\3\2\2\2jk\3\2\2\2k\7\3\2\2\2lm\7\f\2\2mn\5\60\31\2no\7"+
		"\64\2\2op\5\36\20\2pq\7\65\2\2q\t\3\2\2\2rs\7\13\2\2st\5\60\31\2tu\7#"+
		"\2\2uz\5\34\17\2vw\7+\2\2wy\5\34\17\2xv\3\2\2\2y|\3\2\2\2zx\3\2\2\2z{"+
		"\3\2\2\2{}\3\2\2\2|z\3\2\2\2}~\78\2\2~\13\3\2\2\2\177\u0080\7\r\2\2\u0080"+
		"\u0081\5\60\31\2\u0081\u0082\5\16\b\2\u0082\r\3\2\2\2\u0083\u0089\7\64"+
		"\2\2\u0084\u0088\5:\36\2\u0085\u0088\5\n\6\2\u0086\u0088\5\b\5\2\u0087"+
		"\u0084\3\2\2\2\u0087\u0085\3\2\2\2\u0087\u0086\3\2\2\2\u0088\u008b\3\2"+
		"\2\2\u0089\u0087\3\2\2\2\u0089\u008a\3\2\2\2\u008a\u008d\3\2\2\2\u008b"+
		"\u0089\3\2\2\2\u008c\u008e\5\36\20\2\u008d\u008c\3\2\2\2\u008d\u008e\3"+
		"\2\2\2\u008e\u0090\3\2\2\2\u008f\u0091\5\20\t\2\u0090\u008f\3\2\2\2\u0091"+
		"\u0092\3\2\2\2\u0092\u0090\3\2\2\2\u0092\u0093\3\2\2\2\u0093\u0094\3\2"+
		"\2\2\u0094\u0095\7\65\2\2\u0095\17\3\2\2\2\u0096\u0098\7\n\2\2\u0097\u0099"+
		"\7\7\2\2\u0098\u0097\3\2\2\2\u0098\u0099\3\2\2\2\u0099\u009a\3\2\2\2\u009a"+
		"\u009b\5\60\31\2\u009b\u009c\5\22\n\2\u009c\21\3\2\2\2\u009d\u009f\7\64"+
		"\2\2\u009e\u00a0\5\36\20\2\u009f\u009e\3\2\2\2\u009f\u00a0\3\2\2\2\u00a0"+
		"\u00a4\3\2\2\2\u00a1\u00a3\5\24\13\2\u00a2\u00a1\3\2\2\2\u00a3\u00a6\3"+
		"\2\2\2\u00a4\u00a2\3\2\2\2\u00a4\u00a5\3\2\2\2\u00a5\u00a7\3\2\2\2\u00a6"+
		"\u00a4\3\2\2\2\u00a7\u00a8\7\65\2\2\u00a8\23\3\2\2\2\u00a9\u00aa\7\20"+
		"\2\2\u00aa\u00ab\7\62\2\2\u00ab\u00ac\5\60\31\2\u00ac\u00ad\7\63\2\2\u00ad"+
		"\u00ae\5\26\f\2\u00ae\25\3\2\2\2\u00af\u00b1\7\64\2\2\u00b0\u00b2\5\36"+
		"\20\2\u00b1\u00b0\3\2\2\2\u00b1\u00b2\3\2\2\2\u00b2\u00b6\3\2\2\2\u00b3"+
		"\u00b5\5\30\r\2\u00b4\u00b3\3\2\2\2\u00b5\u00b8\3\2\2\2\u00b6\u00b4\3"+
		"\2\2\2\u00b6\u00b7\3\2\2\2\u00b7\u00b9\3\2\2\2\u00b8\u00b6\3\2\2\2\u00b9"+
		"\u00ba\7\65\2\2\u00ba\27\3\2\2\2\u00bb\u00bc\7\17\2\2\u00bc\u00bd\7\62"+
		"\2\2\u00bd\u00c2\5\34\17\2\u00be\u00bf\7+\2\2\u00bf\u00c1\5\34\17\2\u00c0"+
		"\u00be\3\2\2\2\u00c1\u00c4\3\2\2\2\u00c2\u00c0\3\2\2\2\u00c2\u00c3\3\2"+
		"\2\2\u00c3\u00c5\3\2\2\2\u00c4\u00c2\3\2\2\2\u00c5\u00c6\7\63\2\2\u00c6"+
		"\u00c7\5\32\16\2\u00c7\31\3\2\2\2\u00c8\u00c9\7\64\2\2\u00c9\u00ca\5\36"+
		"\20\2\u00ca\u00cb\7\65\2\2\u00cb\33\3\2\2\2\u00cc\u00cd\5*\26\2\u00cd"+
		"\u00ce\7,\2\2\u00ce\u00cf\5*\26\2\u00cf\u00e5\3\2\2\2\u00d0\u00d1\5*\26"+
		"\2\u00d1\u00d2\7-\2\2\u00d2\u00d3\5*\26\2\u00d3\u00e5\3\2\2\2\u00d4\u00d5"+
		"\5*\26\2\u00d5\u00d6\7.\2\2\u00d6\u00d7\5*\26\2\u00d7\u00e5\3\2\2\2\u00d8"+
		"\u00d9\5*\26\2\u00d9\u00da\7/\2\2\u00da\u00db\5*\26\2\u00db\u00e5\3\2"+
		"\2\2\u00dc\u00dd\5*\26\2\u00dd\u00de\7\60\2\2\u00de\u00df\5*\26\2\u00df"+
		"\u00e5\3\2\2\2\u00e0\u00e1\5*\26\2\u00e1\u00e2\7\61\2\2\u00e2\u00e3\5"+
		"*\26\2\u00e3\u00e5\3\2\2\2\u00e4\u00cc\3\2\2\2\u00e4\u00d0\3\2\2\2\u00e4"+
		"\u00d4\3\2\2\2\u00e4\u00d8\3\2\2\2\u00e4\u00dc\3\2\2\2\u00e4\u00e0\3\2"+
		"\2\2\u00e5\35\3\2\2\2\u00e6\u00e9\5\"\22\2\u00e7\u00e9\5 \21\2\u00e8\u00e6"+
		"\3\2\2\2\u00e8\u00e7\3\2\2\2\u00e9\u00ec\3\2\2\2\u00ea\u00e8\3\2\2\2\u00ea"+
		"\u00eb\3\2\2\2\u00eb\37\3\2\2\2\u00ec\u00ea\3\2\2\2\u00ed\u00ee\7\21\2"+
		"\2\u00ee\u00f0\7\64\2\2\u00ef\u00f1\5\"\22\2\u00f0\u00ef\3\2\2\2\u00f1"+
		"\u00f2\3\2\2\2\u00f2\u00f0\3\2\2\2\u00f2\u00f3\3\2\2\2\u00f3\u00f4\3\2"+
		"\2\2\u00f4\u00f5\7\65\2\2\u00f5!\3\2\2\2\u00f6\u00f9\5$\23\2\u00f7\u00f9"+
		"\5&\24\2\u00f8\u00f6\3\2\2\2\u00f8\u00f7\3\2\2\2\u00f9#\3\2\2\2\u00fa"+
		"\u00fb\7\30\2\2\u00fb\u00ff\7\62\2\2\u00fc\u00fe\5*\26\2\u00fd\u00fc\3"+
		"\2\2\2\u00fe\u0101\3\2\2\2\u00ff\u00fd\3\2\2\2\u00ff\u0100\3\2\2\2\u0100"+
		"\u0106\3\2\2\2\u0101\u00ff\3\2\2\2\u0102\u0103\7+\2\2\u0103\u0105\5*\26"+
		"\2\u0104\u0102\3\2\2\2\u0105\u0108\3\2\2\2\u0106\u0104\3\2\2\2\u0106\u0107"+
		"\3\2\2\2\u0107\u0109\3\2\2\2\u0108\u0106\3\2\2\2\u0109\u010a\7\63\2\2"+
		"\u010a\u010b\78\2\2\u010b%\3\2\2\2\u010c\u010d\5\60\31\2\u010d\u010e\7"+
		"#\2\2\u010e\u010f\5(\25\2\u010f\u0110\78\2\2\u0110\'\3\2\2\2\u0111\u0112"+
		"\5*\26\2\u0112\u0113\7&\2\2\u0113\u0114\5*\26\2\u0114\u012e\3\2\2\2\u0115"+
		"\u0116\5*\26\2\u0116\u0117\7\'\2\2\u0117\u0118\5*\26\2\u0118\u012e\3\2"+
		"\2\2\u0119\u011a\5*\26\2\u011a\u011b\7$\2\2\u011b\u011c\5*\26\2\u011c"+
		"\u012e\3\2\2\2\u011d\u011e\5*\26\2\u011e\u011f\7%\2\2\u011f\u0120\5*\26"+
		"\2\u0120\u012e\3\2\2\2\u0121\u0122\5*\26\2\u0122\u0123\7(\2\2\u0123\u0124"+
		"\5*\26\2\u0124\u012e\3\2\2\2\u0125\u0126\t\2\2\2\u0126\u0127\7\62\2\2"+
		"\u0127\u0128\5*\26\2\u0128\u0129\7+\2\2\u0129\u012a\5*\26\2\u012a\u012b"+
		"\7\63\2\2\u012b\u012e\3\2\2\2\u012c\u012e\5*\26\2\u012d\u0111\3\2\2\2"+
		"\u012d\u0115\3\2\2\2\u012d\u0119\3\2\2\2\u012d\u011d\3\2\2\2\u012d\u0121"+
		"\3\2\2\2\u012d\u0125\3\2\2\2\u012d\u012c\3\2\2\2\u012e)\3\2\2\2\u012f"+
		"\u0135\5.\30\2\u0130\u0135\5\60\31\2\u0131\u0135\5@!\2\u0132\u0135\5B"+
		"\"\2\u0133\u0135\5,\27\2\u0134\u012f\3\2\2\2\u0134\u0130\3\2\2\2\u0134"+
		"\u0131\3\2\2\2\u0134\u0132\3\2\2\2\u0134\u0133\3\2\2\2\u0135+\3\2\2\2"+
		"\u0136\u0137\5.\30\2\u0137\u0138\7\31\2\2\u0138-\3\2\2\2\u0139\u013a\7"+
		"\30\2\2\u013a\u013b\7\"\2\2\u013b\u013c\7\30\2\2\u013c/\3\2\2\2\u013d"+
		"\u013e\7\30\2\2\u013e\61\3\2\2\2\u013f\u0140\7\22\2\2\u0140\u0145\5\64"+
		"\33\2\u0141\u0142\7+\2\2\u0142\u0144\5\64\33\2\u0143\u0141\3\2\2\2\u0144"+
		"\u0147\3\2\2\2\u0145\u0143\3\2\2\2\u0145\u0146\3\2\2\2\u0146\u0148\3\2"+
		"\2\2\u0147\u0145\3\2\2\2\u0148\u0149\78\2\2\u0149\63\3\2\2\2\u014a\u014b"+
		"\7\32\2\2\u014b\65\3\2\2\2\u014c\u014d\7\16\2\2\u014d\u014e\5\60\31\2"+
		"\u014e\u0150\7\64\2\2\u014f\u0151\58\35\2\u0150\u014f\3\2\2\2\u0151\u0152"+
		"\3\2\2\2\u0152\u0150\3\2\2\2\u0152\u0153\3\2\2\2\u0153\u0154\3\2\2\2\u0154"+
		"\u0155\7\65\2\2\u0155\67\3\2\2\2\u0156\u0157\5\60\31\2\u0157\u0158\7#"+
		"\2\2\u0158\u0159\5,\27\2\u0159\u015a\78\2\2\u015a9\3\2\2\2\u015b\u015d"+
		"\7\b\2\2\u015c\u015e\7\6\2\2\u015d\u015c\3\2\2\2\u015d\u015e\3\2\2\2\u015e"+
		"\u015f\3\2\2\2\u015f\u0164\5\60\31\2\u0160\u0161\7+\2\2\u0161\u0163\5"+
		"\60\31\2\u0162\u0160\3\2\2\2\u0163\u0166\3\2\2\2\u0164\u0162\3\2\2\2\u0164"+
		"\u0165\3\2\2\2\u0165\u0167\3\2\2\2\u0166\u0164\3\2\2\2\u0167\u0168\78"+
		"\2\2\u0168;\3\2\2\2\u0169\u016a\7\t\2\2\u016a\u016f\5> \2\u016b\u016c"+
		"\7+\2\2\u016c\u016e\5> \2\u016d\u016b\3\2\2\2\u016e\u0171\3\2\2\2\u016f"+
		"\u016d\3\2\2\2\u016f\u0170\3\2\2\2\u0170\u0172\3\2\2\2\u0171\u016f\3\2"+
		"\2\2\u0172\u0173\78\2\2\u0173=\3\2\2\2\u0174\u0175\5\60\31\2\u0175\u0178"+
		"\7#\2\2\u0176\u0179\5@!\2\u0177\u0179\5B\"\2\u0178\u0176\3\2\2\2\u0178"+
		"\u0177\3\2\2\2\u0179?\3\2\2\2\u017a\u017b\t\3\2\2\u017bA\3\2\2\2\u017c"+
		"\u017d\t\4\2\2\u017dC\3\2\2\2%GINUW]bdjz\u0087\u0089\u008d\u0092\u0098"+
		"\u009f\u00a4\u00b1\u00b6\u00c2\u00e4\u00e8\u00ea\u00f2\u00f8\u00ff\u0106"+
		"\u012d\u0134\u0145\u0152\u015d\u0164\u016f\u0178";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}