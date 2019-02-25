// Generated from /home/giacomo/IdeaProjects/xtrac/src/main/java/resources/xtra.g4 by ANTLR 4.7
package it.uniroma2.netprog.xtrac.parser;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link xtraParser}.
 */
public interface xtraListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link xtraParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(xtraParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link xtraParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(xtraParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link xtraParser#stage_program}.
	 * @param ctx the parse tree
	 */
	void enterStage_program(xtraParser.Stage_programContext ctx);
	/**
	 * Exit a parse tree produced by {@link xtraParser#stage_program}.
	 * @param ctx the parse tree
	 */
	void exitStage_program(xtraParser.Stage_programContext ctx);
	/**
	 * Enter a parse tree produced by {@link xtraParser#state_program}.
	 * @param ctx the parse tree
	 */
	void enterState_program(xtraParser.State_programContext ctx);
	/**
	 * Exit a parse tree produced by {@link xtraParser#state_program}.
	 * @param ctx the parse tree
	 */
	void exitState_program(xtraParser.State_programContext ctx);
	/**
	 * Enter a parse tree produced by {@link xtraParser#macro_action_definition}.
	 * @param ctx the parse tree
	 */
	void enterMacro_action_definition(xtraParser.Macro_action_definitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link xtraParser#macro_action_definition}.
	 * @param ctx the parse tree
	 */
	void exitMacro_action_definition(xtraParser.Macro_action_definitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link xtraParser#custom_event_definition}.
	 * @param ctx the parse tree
	 */
	void enterCustom_event_definition(xtraParser.Custom_event_definitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link xtraParser#custom_event_definition}.
	 * @param ctx the parse tree
	 */
	void exitCustom_event_definition(xtraParser.Custom_event_definitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link xtraParser#stage_statement}.
	 * @param ctx the parse tree
	 */
	void enterStage_statement(xtraParser.Stage_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link xtraParser#stage_statement}.
	 * @param ctx the parse tree
	 */
	void exitStage_statement(xtraParser.Stage_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link xtraParser#stage_body}.
	 * @param ctx the parse tree
	 */
	void enterStage_body(xtraParser.Stage_bodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link xtraParser#stage_body}.
	 * @param ctx the parse tree
	 */
	void exitStage_body(xtraParser.Stage_bodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link xtraParser#state_statement}.
	 * @param ctx the parse tree
	 */
	void enterState_statement(xtraParser.State_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link xtraParser#state_statement}.
	 * @param ctx the parse tree
	 */
	void exitState_statement(xtraParser.State_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link xtraParser#state_body}.
	 * @param ctx the parse tree
	 */
	void enterState_body(xtraParser.State_bodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link xtraParser#state_body}.
	 * @param ctx the parse tree
	 */
	void exitState_body(xtraParser.State_bodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link xtraParser#event_statement}.
	 * @param ctx the parse tree
	 */
	void enterEvent_statement(xtraParser.Event_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link xtraParser#event_statement}.
	 * @param ctx the parse tree
	 */
	void exitEvent_statement(xtraParser.Event_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link xtraParser#event_body}.
	 * @param ctx the parse tree
	 */
	void enterEvent_body(xtraParser.Event_bodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link xtraParser#event_body}.
	 * @param ctx the parse tree
	 */
	void exitEvent_body(xtraParser.Event_bodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link xtraParser#condition_statement}.
	 * @param ctx the parse tree
	 */
	void enterCondition_statement(xtraParser.Condition_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link xtraParser#condition_statement}.
	 * @param ctx the parse tree
	 */
	void exitCondition_statement(xtraParser.Condition_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link xtraParser#condition_body}.
	 * @param ctx the parse tree
	 */
	void enterCondition_body(xtraParser.Condition_bodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link xtraParser#condition_body}.
	 * @param ctx the parse tree
	 */
	void exitCondition_body(xtraParser.Condition_bodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link xtraParser#condition}.
	 * @param ctx the parse tree
	 */
	void enterCondition(xtraParser.ConditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link xtraParser#condition}.
	 * @param ctx the parse tree
	 */
	void exitCondition(xtraParser.ConditionContext ctx);
	/**
	 * Enter a parse tree produced by {@link xtraParser#body}.
	 * @param ctx the parse tree
	 */
	void enterBody(xtraParser.BodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link xtraParser#body}.
	 * @param ctx the parse tree
	 */
	void exitBody(xtraParser.BodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link xtraParser#serial}.
	 * @param ctx the parse tree
	 */
	void enterSerial(xtraParser.SerialContext ctx);
	/**
	 * Exit a parse tree produced by {@link xtraParser#serial}.
	 * @param ctx the parse tree
	 */
	void exitSerial(xtraParser.SerialContext ctx);
	/**
	 * Enter a parse tree produced by {@link xtraParser#action}.
	 * @param ctx the parse tree
	 */
	void enterAction(xtraParser.ActionContext ctx);
	/**
	 * Exit a parse tree produced by {@link xtraParser#action}.
	 * @param ctx the parse tree
	 */
	void exitAction(xtraParser.ActionContext ctx);
	/**
	 * Enter a parse tree produced by {@link xtraParser#call}.
	 * @param ctx the parse tree
	 */
	void enterCall(xtraParser.CallContext ctx);
	/**
	 * Exit a parse tree produced by {@link xtraParser#call}.
	 * @param ctx the parse tree
	 */
	void exitCall(xtraParser.CallContext ctx);
	/**
	 * Enter a parse tree produced by {@link xtraParser#assignment}.
	 * @param ctx the parse tree
	 */
	void enterAssignment(xtraParser.AssignmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link xtraParser#assignment}.
	 * @param ctx the parse tree
	 */
	void exitAssignment(xtraParser.AssignmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link xtraParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExpr(xtraParser.ExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link xtraParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExpr(xtraParser.ExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link xtraParser#term}.
	 * @param ctx the parse tree
	 */
	void enterTerm(xtraParser.TermContext ctx);
	/**
	 * Exit a parse tree produced by {@link xtraParser#term}.
	 * @param ctx the parse tree
	 */
	void exitTerm(xtraParser.TermContext ctx);
	/**
	 * Enter a parse tree produced by {@link xtraParser#index_access}.
	 * @param ctx the parse tree
	 */
	void enterIndex_access(xtraParser.Index_accessContext ctx);
	/**
	 * Exit a parse tree produced by {@link xtraParser#index_access}.
	 * @param ctx the parse tree
	 */
	void exitIndex_access(xtraParser.Index_accessContext ctx);
	/**
	 * Enter a parse tree produced by {@link xtraParser#field_elem}.
	 * @param ctx the parse tree
	 */
	void enterField_elem(xtraParser.Field_elemContext ctx);
	/**
	 * Exit a parse tree produced by {@link xtraParser#field_elem}.
	 * @param ctx the parse tree
	 */
	void exitField_elem(xtraParser.Field_elemContext ctx);
	/**
	 * Enter a parse tree produced by {@link xtraParser#identifier}.
	 * @param ctx the parse tree
	 */
	void enterIdentifier(xtraParser.IdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link xtraParser#identifier}.
	 * @param ctx the parse tree
	 */
	void exitIdentifier(xtraParser.IdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link xtraParser#file_import}.
	 * @param ctx the parse tree
	 */
	void enterFile_import(xtraParser.File_importContext ctx);
	/**
	 * Exit a parse tree produced by {@link xtraParser#file_import}.
	 * @param ctx the parse tree
	 */
	void exitFile_import(xtraParser.File_importContext ctx);
	/**
	 * Enter a parse tree produced by {@link xtraParser#file_name}.
	 * @param ctx the parse tree
	 */
	void enterFile_name(xtraParser.File_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link xtraParser#file_name}.
	 * @param ctx the parse tree
	 */
	void exitFile_name(xtraParser.File_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link xtraParser#packet_field_definition}.
	 * @param ctx the parse tree
	 */
	void enterPacket_field_definition(xtraParser.Packet_field_definitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link xtraParser#packet_field_definition}.
	 * @param ctx the parse tree
	 */
	void exitPacket_field_definition(xtraParser.Packet_field_definitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link xtraParser#packet_field_entry}.
	 * @param ctx the parse tree
	 */
	void enterPacket_field_entry(xtraParser.Packet_field_entryContext ctx);
	/**
	 * Exit a parse tree produced by {@link xtraParser#packet_field_entry}.
	 * @param ctx the parse tree
	 */
	void exitPacket_field_entry(xtraParser.Packet_field_entryContext ctx);
	/**
	 * Enter a parse tree produced by {@link xtraParser#register_definition}.
	 * @param ctx the parse tree
	 */
	void enterRegister_definition(xtraParser.Register_definitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link xtraParser#register_definition}.
	 * @param ctx the parse tree
	 */
	void exitRegister_definition(xtraParser.Register_definitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link xtraParser#constant_definition}.
	 * @param ctx the parse tree
	 */
	void enterConstant_definition(xtraParser.Constant_definitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link xtraParser#constant_definition}.
	 * @param ctx the parse tree
	 */
	void exitConstant_definition(xtraParser.Constant_definitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link xtraParser#const_assignement}.
	 * @param ctx the parse tree
	 */
	void enterConst_assignement(xtraParser.Const_assignementContext ctx);
	/**
	 * Exit a parse tree produced by {@link xtraParser#const_assignement}.
	 * @param ctx the parse tree
	 */
	void exitConst_assignement(xtraParser.Const_assignementContext ctx);
	/**
	 * Enter a parse tree produced by {@link xtraParser#integer}.
	 * @param ctx the parse tree
	 */
	void enterInteger(xtraParser.IntegerContext ctx);
	/**
	 * Exit a parse tree produced by {@link xtraParser#integer}.
	 * @param ctx the parse tree
	 */
	void exitInteger(xtraParser.IntegerContext ctx);
	/**
	 * Enter a parse tree produced by {@link xtraParser#addr}.
	 * @param ctx the parse tree
	 */
	void enterAddr(xtraParser.AddrContext ctx);
	/**
	 * Exit a parse tree produced by {@link xtraParser#addr}.
	 * @param ctx the parse tree
	 */
	void exitAddr(xtraParser.AddrContext ctx);
}