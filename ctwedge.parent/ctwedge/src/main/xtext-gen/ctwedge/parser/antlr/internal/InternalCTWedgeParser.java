package ctwedge.parser.antlr.internal;

import org.eclipse.xtext.*;
import org.eclipse.xtext.parser.*;
import org.eclipse.xtext.parser.impl.*;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.common.util.Enumerator;
import org.eclipse.xtext.parser.antlr.AbstractInternalAntlrParser;
import org.eclipse.xtext.parser.antlr.XtextTokenStream;
import org.eclipse.xtext.parser.antlr.XtextTokenStream.HiddenTokens;
import org.eclipse.xtext.parser.antlr.AntlrDatatypeRuleToken;
import ctwedge.services.CTWedgeGrammarAccess;



import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class InternalCTWedgeParser extends AbstractInternalAntlrParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_ID", "RULE_INT", "RULE_NUMID", "RULE_STRING", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "RULE_ANY_OTHER", "'Model'", "'Parameters'", "':'", "'Constraints'", "';'", "'Boolean'", "'{'", "'}'", "','", "'['", "'..'", "']'", "'step'", "'#'", "'('", "')'", "'||'", "'or'", "'OR'", "'|'", "'&&'", "'and'", "'AND'", "'&'", "'!'", "'not'", "'false'", "'true'", "'FALSE'", "'TRUE'", "'-'", "'>'", "'<'", "'>='", "'<='", "'=='", "'!='", "'='", "'+'", "'%'", "'*'", "'/'", "'=>'", "'<=>'", "'->'", "'<->'"
    };
    public static final int T__50=50;
    public static final int T__19=19;
    public static final int T__15=15;
    public static final int T__16=16;
    public static final int T__17=17;
    public static final int T__18=18;
    public static final int T__55=55;
    public static final int T__12=12;
    public static final int T__56=56;
    public static final int T__13=13;
    public static final int T__57=57;
    public static final int T__14=14;
    public static final int T__51=51;
    public static final int T__52=52;
    public static final int T__53=53;
    public static final int T__54=54;
    public static final int RULE_ID=4;
    public static final int T__26=26;
    public static final int T__27=27;
    public static final int T__28=28;
    public static final int RULE_INT=5;
    public static final int T__29=29;
    public static final int T__22=22;
    public static final int RULE_ML_COMMENT=8;
    public static final int T__23=23;
    public static final int T__24=24;
    public static final int T__25=25;
    public static final int T__20=20;
    public static final int T__21=21;
    public static final int RULE_STRING=7;
    public static final int RULE_NUMID=6;
    public static final int RULE_SL_COMMENT=9;
    public static final int T__37=37;
    public static final int T__38=38;
    public static final int T__39=39;
    public static final int T__33=33;
    public static final int T__34=34;
    public static final int T__35=35;
    public static final int T__36=36;
    public static final int EOF=-1;
    public static final int T__30=30;
    public static final int T__31=31;
    public static final int T__32=32;
    public static final int RULE_WS=10;
    public static final int RULE_ANY_OTHER=11;
    public static final int T__48=48;
    public static final int T__49=49;
    public static final int T__44=44;
    public static final int T__45=45;
    public static final int T__46=46;
    public static final int T__47=47;
    public static final int T__40=40;
    public static final int T__41=41;
    public static final int T__42=42;
    public static final int T__43=43;

    // delegates
    // delegators


        public InternalCTWedgeParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public InternalCTWedgeParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return InternalCTWedgeParser.tokenNames; }
    public String getGrammarFileName() { return "InternalCTWedge.g"; }



     	private CTWedgeGrammarAccess grammarAccess;

        public InternalCTWedgeParser(TokenStream input, CTWedgeGrammarAccess grammarAccess) {
            this(input);
            this.grammarAccess = grammarAccess;
            registerRules(grammarAccess.getGrammar());
        }

        @Override
        protected String getFirstRuleName() {
        	return "CitModel";
       	}

       	@Override
       	protected CTWedgeGrammarAccess getGrammarAccess() {
       		return grammarAccess;
       	}




    // $ANTLR start "entryRuleCitModel"
    // InternalCTWedge.g:65:1: entryRuleCitModel returns [EObject current=null] : iv_ruleCitModel= ruleCitModel EOF ;
    public final EObject entryRuleCitModel() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCitModel = null;


        try {
            // InternalCTWedge.g:65:49: (iv_ruleCitModel= ruleCitModel EOF )
            // InternalCTWedge.g:66:2: iv_ruleCitModel= ruleCitModel EOF
            {
             newCompositeNode(grammarAccess.getCitModelRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleCitModel=ruleCitModel();

            state._fsp--;

             current =iv_ruleCitModel; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleCitModel"


    // $ANTLR start "ruleCitModel"
    // InternalCTWedge.g:72:1: ruleCitModel returns [EObject current=null] : ( () otherlv_1= 'Model' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= 'Parameters' otherlv_4= ':' ( (lv_parameters_5_0= ruleParameter ) )+ (otherlv_6= 'Constraints' otherlv_7= ':' ( (lv_constraints_8_0= ruleConstraint ) )+ )? ) ;
    public final EObject ruleCitModel() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token lv_name_2_0=null;
        Token otherlv_3=null;
        Token otherlv_4=null;
        Token otherlv_6=null;
        Token otherlv_7=null;
        EObject lv_parameters_5_0 = null;

        EObject lv_constraints_8_0 = null;



        	enterRule();

        try {
            // InternalCTWedge.g:78:2: ( ( () otherlv_1= 'Model' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= 'Parameters' otherlv_4= ':' ( (lv_parameters_5_0= ruleParameter ) )+ (otherlv_6= 'Constraints' otherlv_7= ':' ( (lv_constraints_8_0= ruleConstraint ) )+ )? ) )
            // InternalCTWedge.g:79:2: ( () otherlv_1= 'Model' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= 'Parameters' otherlv_4= ':' ( (lv_parameters_5_0= ruleParameter ) )+ (otherlv_6= 'Constraints' otherlv_7= ':' ( (lv_constraints_8_0= ruleConstraint ) )+ )? )
            {
            // InternalCTWedge.g:79:2: ( () otherlv_1= 'Model' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= 'Parameters' otherlv_4= ':' ( (lv_parameters_5_0= ruleParameter ) )+ (otherlv_6= 'Constraints' otherlv_7= ':' ( (lv_constraints_8_0= ruleConstraint ) )+ )? )
            // InternalCTWedge.g:80:3: () otherlv_1= 'Model' ( (lv_name_2_0= RULE_ID ) ) otherlv_3= 'Parameters' otherlv_4= ':' ( (lv_parameters_5_0= ruleParameter ) )+ (otherlv_6= 'Constraints' otherlv_7= ':' ( (lv_constraints_8_0= ruleConstraint ) )+ )?
            {
            // InternalCTWedge.g:80:3: ()
            // InternalCTWedge.g:81:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getCitModelAccess().getCitModelAction_0(),
            					current);
            			

            }

            otherlv_1=(Token)match(input,12,FOLLOW_3); 

            			newLeafNode(otherlv_1, grammarAccess.getCitModelAccess().getModelKeyword_1());
            		
            // InternalCTWedge.g:91:3: ( (lv_name_2_0= RULE_ID ) )
            // InternalCTWedge.g:92:4: (lv_name_2_0= RULE_ID )
            {
            // InternalCTWedge.g:92:4: (lv_name_2_0= RULE_ID )
            // InternalCTWedge.g:93:5: lv_name_2_0= RULE_ID
            {
            lv_name_2_0=(Token)match(input,RULE_ID,FOLLOW_4); 

            					newLeafNode(lv_name_2_0, grammarAccess.getCitModelAccess().getNameIDTerminalRuleCall_2_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getCitModelRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_2_0,
            						"org.eclipse.xtext.common.Terminals.ID");
            				

            }


            }

            otherlv_3=(Token)match(input,13,FOLLOW_5); 

            			newLeafNode(otherlv_3, grammarAccess.getCitModelAccess().getParametersKeyword_3());
            		
            otherlv_4=(Token)match(input,14,FOLLOW_3); 

            			newLeafNode(otherlv_4, grammarAccess.getCitModelAccess().getColonKeyword_4());
            		
            // InternalCTWedge.g:117:3: ( (lv_parameters_5_0= ruleParameter ) )+
            int cnt1=0;
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==RULE_ID) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // InternalCTWedge.g:118:4: (lv_parameters_5_0= ruleParameter )
            	    {
            	    // InternalCTWedge.g:118:4: (lv_parameters_5_0= ruleParameter )
            	    // InternalCTWedge.g:119:5: lv_parameters_5_0= ruleParameter
            	    {

            	    					newCompositeNode(grammarAccess.getCitModelAccess().getParametersParameterParserRuleCall_5_0());
            	    				
            	    pushFollow(FOLLOW_6);
            	    lv_parameters_5_0=ruleParameter();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getCitModelRule());
            	    					}
            	    					add(
            	    						current,
            	    						"parameters",
            	    						lv_parameters_5_0,
            	    						"ctwedge.CTWedge.Parameter");
            	    					afterParserOrEnumRuleCall();
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt1 >= 1 ) break loop1;
                        EarlyExitException eee =
                            new EarlyExitException(1, input);
                        throw eee;
                }
                cnt1++;
            } while (true);

            // InternalCTWedge.g:136:3: (otherlv_6= 'Constraints' otherlv_7= ':' ( (lv_constraints_8_0= ruleConstraint ) )+ )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==15) ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // InternalCTWedge.g:137:4: otherlv_6= 'Constraints' otherlv_7= ':' ( (lv_constraints_8_0= ruleConstraint ) )+
                    {
                    otherlv_6=(Token)match(input,15,FOLLOW_5); 

                    				newLeafNode(otherlv_6, grammarAccess.getCitModelAccess().getConstraintsKeyword_6_0());
                    			
                    otherlv_7=(Token)match(input,14,FOLLOW_7); 

                    				newLeafNode(otherlv_7, grammarAccess.getCitModelAccess().getColonKeyword_6_1());
                    			
                    // InternalCTWedge.g:145:4: ( (lv_constraints_8_0= ruleConstraint ) )+
                    int cnt2=0;
                    loop2:
                    do {
                        int alt2=2;
                        int LA2_0 = input.LA(1);

                        if ( (LA2_0==25) ) {
                            alt2=1;
                        }


                        switch (alt2) {
                    	case 1 :
                    	    // InternalCTWedge.g:146:5: (lv_constraints_8_0= ruleConstraint )
                    	    {
                    	    // InternalCTWedge.g:146:5: (lv_constraints_8_0= ruleConstraint )
                    	    // InternalCTWedge.g:147:6: lv_constraints_8_0= ruleConstraint
                    	    {

                    	    						newCompositeNode(grammarAccess.getCitModelAccess().getConstraintsConstraintParserRuleCall_6_2_0());
                    	    					
                    	    pushFollow(FOLLOW_8);
                    	    lv_constraints_8_0=ruleConstraint();

                    	    state._fsp--;


                    	    						if (current==null) {
                    	    							current = createModelElementForParent(grammarAccess.getCitModelRule());
                    	    						}
                    	    						add(
                    	    							current,
                    	    							"constraints",
                    	    							lv_constraints_8_0,
                    	    							"ctwedge.CTWedge.Constraint");
                    	    						afterParserOrEnumRuleCall();
                    	    					

                    	    }


                    	    }
                    	    break;

                    	default :
                    	    if ( cnt2 >= 1 ) break loop2;
                                EarlyExitException eee =
                                    new EarlyExitException(2, input);
                                throw eee;
                        }
                        cnt2++;
                    } while (true);


                    }
                    break;

            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleCitModel"


    // $ANTLR start "entryRuleParameter"
    // InternalCTWedge.g:169:1: entryRuleParameter returns [EObject current=null] : iv_ruleParameter= ruleParameter EOF ;
    public final EObject entryRuleParameter() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleParameter = null;


        try {
            // InternalCTWedge.g:169:50: (iv_ruleParameter= ruleParameter EOF )
            // InternalCTWedge.g:170:2: iv_ruleParameter= ruleParameter EOF
            {
             newCompositeNode(grammarAccess.getParameterRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleParameter=ruleParameter();

            state._fsp--;

             current =iv_ruleParameter; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleParameter"


    // $ANTLR start "ruleParameter"
    // InternalCTWedge.g:176:1: ruleParameter returns [EObject current=null] : ( (this_Bool_0= ruleBool | this_Enumerative_1= ruleEnumerative | this_Range_2= ruleRange ) (otherlv_3= ';' )? ) ;
    public final EObject ruleParameter() throws RecognitionException {
        EObject current = null;

        Token otherlv_3=null;
        EObject this_Bool_0 = null;

        EObject this_Enumerative_1 = null;

        EObject this_Range_2 = null;



        	enterRule();

        try {
            // InternalCTWedge.g:182:2: ( ( (this_Bool_0= ruleBool | this_Enumerative_1= ruleEnumerative | this_Range_2= ruleRange ) (otherlv_3= ';' )? ) )
            // InternalCTWedge.g:183:2: ( (this_Bool_0= ruleBool | this_Enumerative_1= ruleEnumerative | this_Range_2= ruleRange ) (otherlv_3= ';' )? )
            {
            // InternalCTWedge.g:183:2: ( (this_Bool_0= ruleBool | this_Enumerative_1= ruleEnumerative | this_Range_2= ruleRange ) (otherlv_3= ';' )? )
            // InternalCTWedge.g:184:3: (this_Bool_0= ruleBool | this_Enumerative_1= ruleEnumerative | this_Range_2= ruleRange ) (otherlv_3= ';' )?
            {
            // InternalCTWedge.g:184:3: (this_Bool_0= ruleBool | this_Enumerative_1= ruleEnumerative | this_Range_2= ruleRange )
            int alt4=3;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==RULE_ID) ) {
                int LA4_1 = input.LA(2);

                if ( (LA4_1==14) ) {
                    switch ( input.LA(3) ) {
                    case 17:
                        {
                        alt4=1;
                        }
                        break;
                    case 21:
                        {
                        alt4=3;
                        }
                        break;
                    case 18:
                        {
                        alt4=2;
                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("", 4, 2, input);

                        throw nvae;
                    }

                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 4, 1, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 4, 0, input);

                throw nvae;
            }
            switch (alt4) {
                case 1 :
                    // InternalCTWedge.g:185:4: this_Bool_0= ruleBool
                    {

                    				newCompositeNode(grammarAccess.getParameterAccess().getBoolParserRuleCall_0_0());
                    			
                    pushFollow(FOLLOW_9);
                    this_Bool_0=ruleBool();

                    state._fsp--;


                    				current = this_Bool_0;
                    				afterParserOrEnumRuleCall();
                    			

                    }
                    break;
                case 2 :
                    // InternalCTWedge.g:194:4: this_Enumerative_1= ruleEnumerative
                    {

                    				newCompositeNode(grammarAccess.getParameterAccess().getEnumerativeParserRuleCall_0_1());
                    			
                    pushFollow(FOLLOW_9);
                    this_Enumerative_1=ruleEnumerative();

                    state._fsp--;


                    				current = this_Enumerative_1;
                    				afterParserOrEnumRuleCall();
                    			

                    }
                    break;
                case 3 :
                    // InternalCTWedge.g:203:4: this_Range_2= ruleRange
                    {

                    				newCompositeNode(grammarAccess.getParameterAccess().getRangeParserRuleCall_0_2());
                    			
                    pushFollow(FOLLOW_9);
                    this_Range_2=ruleRange();

                    state._fsp--;


                    				current = this_Range_2;
                    				afterParserOrEnumRuleCall();
                    			

                    }
                    break;

            }

            // InternalCTWedge.g:212:3: (otherlv_3= ';' )?
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==16) ) {
                alt5=1;
            }
            switch (alt5) {
                case 1 :
                    // InternalCTWedge.g:213:4: otherlv_3= ';'
                    {
                    otherlv_3=(Token)match(input,16,FOLLOW_2); 

                    				newLeafNode(otherlv_3, grammarAccess.getParameterAccess().getSemicolonKeyword_1());
                    			

                    }
                    break;

            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleParameter"


    // $ANTLR start "entryRuleBool"
    // InternalCTWedge.g:222:1: entryRuleBool returns [EObject current=null] : iv_ruleBool= ruleBool EOF ;
    public final EObject entryRuleBool() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleBool = null;


        try {
            // InternalCTWedge.g:222:45: (iv_ruleBool= ruleBool EOF )
            // InternalCTWedge.g:223:2: iv_ruleBool= ruleBool EOF
            {
             newCompositeNode(grammarAccess.getBoolRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleBool=ruleBool();

            state._fsp--;

             current =iv_ruleBool; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleBool"


    // $ANTLR start "ruleBool"
    // InternalCTWedge.g:229:1: ruleBool returns [EObject current=null] : ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= ':' otherlv_2= 'Boolean' ) ;
    public final EObject ruleBool() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;
        Token otherlv_1=null;
        Token otherlv_2=null;


        	enterRule();

        try {
            // InternalCTWedge.g:235:2: ( ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= ':' otherlv_2= 'Boolean' ) )
            // InternalCTWedge.g:236:2: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= ':' otherlv_2= 'Boolean' )
            {
            // InternalCTWedge.g:236:2: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= ':' otherlv_2= 'Boolean' )
            // InternalCTWedge.g:237:3: ( (lv_name_0_0= RULE_ID ) ) otherlv_1= ':' otherlv_2= 'Boolean'
            {
            // InternalCTWedge.g:237:3: ( (lv_name_0_0= RULE_ID ) )
            // InternalCTWedge.g:238:4: (lv_name_0_0= RULE_ID )
            {
            // InternalCTWedge.g:238:4: (lv_name_0_0= RULE_ID )
            // InternalCTWedge.g:239:5: lv_name_0_0= RULE_ID
            {
            lv_name_0_0=(Token)match(input,RULE_ID,FOLLOW_5); 

            					newLeafNode(lv_name_0_0, grammarAccess.getBoolAccess().getNameIDTerminalRuleCall_0_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getBoolRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_0_0,
            						"org.eclipse.xtext.common.Terminals.ID");
            				

            }


            }

            otherlv_1=(Token)match(input,14,FOLLOW_10); 

            			newLeafNode(otherlv_1, grammarAccess.getBoolAccess().getColonKeyword_1());
            		
            otherlv_2=(Token)match(input,17,FOLLOW_2); 

            			newLeafNode(otherlv_2, grammarAccess.getBoolAccess().getBooleanKeyword_2());
            		

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleBool"


    // $ANTLR start "entryRuleEnumerative"
    // InternalCTWedge.g:267:1: entryRuleEnumerative returns [EObject current=null] : iv_ruleEnumerative= ruleEnumerative EOF ;
    public final EObject entryRuleEnumerative() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleEnumerative = null;


        try {
            // InternalCTWedge.g:267:52: (iv_ruleEnumerative= ruleEnumerative EOF )
            // InternalCTWedge.g:268:2: iv_ruleEnumerative= ruleEnumerative EOF
            {
             newCompositeNode(grammarAccess.getEnumerativeRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleEnumerative=ruleEnumerative();

            state._fsp--;

             current =iv_ruleEnumerative; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleEnumerative"


    // $ANTLR start "ruleEnumerative"
    // InternalCTWedge.g:274:1: ruleEnumerative returns [EObject current=null] : ( ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= ':' otherlv_2= '{' ( (lv_elements_3_0= ruleElement ) )+ otherlv_4= '}' ) ) ;
    public final EObject ruleEnumerative() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;
        Token otherlv_1=null;
        Token otherlv_2=null;
        Token otherlv_4=null;
        EObject lv_elements_3_0 = null;



        	enterRule();

        try {
            // InternalCTWedge.g:280:2: ( ( ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= ':' otherlv_2= '{' ( (lv_elements_3_0= ruleElement ) )+ otherlv_4= '}' ) ) )
            // InternalCTWedge.g:281:2: ( ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= ':' otherlv_2= '{' ( (lv_elements_3_0= ruleElement ) )+ otherlv_4= '}' ) )
            {
            // InternalCTWedge.g:281:2: ( ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= ':' otherlv_2= '{' ( (lv_elements_3_0= ruleElement ) )+ otherlv_4= '}' ) )
            // InternalCTWedge.g:282:3: ( (lv_name_0_0= RULE_ID ) ) (otherlv_1= ':' otherlv_2= '{' ( (lv_elements_3_0= ruleElement ) )+ otherlv_4= '}' )
            {
            // InternalCTWedge.g:282:3: ( (lv_name_0_0= RULE_ID ) )
            // InternalCTWedge.g:283:4: (lv_name_0_0= RULE_ID )
            {
            // InternalCTWedge.g:283:4: (lv_name_0_0= RULE_ID )
            // InternalCTWedge.g:284:5: lv_name_0_0= RULE_ID
            {
            lv_name_0_0=(Token)match(input,RULE_ID,FOLLOW_5); 

            					newLeafNode(lv_name_0_0, grammarAccess.getEnumerativeAccess().getNameIDTerminalRuleCall_0_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getEnumerativeRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_0_0,
            						"org.eclipse.xtext.common.Terminals.ID");
            				

            }


            }

            // InternalCTWedge.g:300:3: (otherlv_1= ':' otherlv_2= '{' ( (lv_elements_3_0= ruleElement ) )+ otherlv_4= '}' )
            // InternalCTWedge.g:301:4: otherlv_1= ':' otherlv_2= '{' ( (lv_elements_3_0= ruleElement ) )+ otherlv_4= '}'
            {
            otherlv_1=(Token)match(input,14,FOLLOW_11); 

            				newLeafNode(otherlv_1, grammarAccess.getEnumerativeAccess().getColonKeyword_1_0());
            			
            otherlv_2=(Token)match(input,18,FOLLOW_12); 

            				newLeafNode(otherlv_2, grammarAccess.getEnumerativeAccess().getLeftCurlyBracketKeyword_1_1());
            			
            // InternalCTWedge.g:309:4: ( (lv_elements_3_0= ruleElement ) )+
            int cnt6=0;
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( ((LA6_0>=RULE_ID && LA6_0<=RULE_STRING)||LA6_0==42) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // InternalCTWedge.g:310:5: (lv_elements_3_0= ruleElement )
            	    {
            	    // InternalCTWedge.g:310:5: (lv_elements_3_0= ruleElement )
            	    // InternalCTWedge.g:311:6: lv_elements_3_0= ruleElement
            	    {

            	    						newCompositeNode(grammarAccess.getEnumerativeAccess().getElementsElementParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_13);
            	    lv_elements_3_0=ruleElement();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getEnumerativeRule());
            	    						}
            	    						add(
            	    							current,
            	    							"elements",
            	    							lv_elements_3_0,
            	    							"ctwedge.CTWedge.Element");
            	    						afterParserOrEnumRuleCall();
            	    					

            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt6 >= 1 ) break loop6;
                        EarlyExitException eee =
                            new EarlyExitException(6, input);
                        throw eee;
                }
                cnt6++;
            } while (true);

            otherlv_4=(Token)match(input,19,FOLLOW_2); 

            				newLeafNode(otherlv_4, grammarAccess.getEnumerativeAccess().getRightCurlyBracketKeyword_1_3());
            			

            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleEnumerative"


    // $ANTLR start "entryRuleElement"
    // InternalCTWedge.g:337:1: entryRuleElement returns [EObject current=null] : iv_ruleElement= ruleElement EOF ;
    public final EObject entryRuleElement() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleElement = null;


        try {
            // InternalCTWedge.g:337:48: (iv_ruleElement= ruleElement EOF )
            // InternalCTWedge.g:338:2: iv_ruleElement= ruleElement EOF
            {
             newCompositeNode(grammarAccess.getElementRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleElement=ruleElement();

            state._fsp--;

             current =iv_ruleElement; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleElement"


    // $ANTLR start "ruleElement"
    // InternalCTWedge.g:344:1: ruleElement returns [EObject current=null] : ( ( (lv_name_0_0= ruleelementID ) ) (otherlv_1= ',' )? ) ;
    public final EObject ruleElement() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        AntlrDatatypeRuleToken lv_name_0_0 = null;



        	enterRule();

        try {
            // InternalCTWedge.g:350:2: ( ( ( (lv_name_0_0= ruleelementID ) ) (otherlv_1= ',' )? ) )
            // InternalCTWedge.g:351:2: ( ( (lv_name_0_0= ruleelementID ) ) (otherlv_1= ',' )? )
            {
            // InternalCTWedge.g:351:2: ( ( (lv_name_0_0= ruleelementID ) ) (otherlv_1= ',' )? )
            // InternalCTWedge.g:352:3: ( (lv_name_0_0= ruleelementID ) ) (otherlv_1= ',' )?
            {
            // InternalCTWedge.g:352:3: ( (lv_name_0_0= ruleelementID ) )
            // InternalCTWedge.g:353:4: (lv_name_0_0= ruleelementID )
            {
            // InternalCTWedge.g:353:4: (lv_name_0_0= ruleelementID )
            // InternalCTWedge.g:354:5: lv_name_0_0= ruleelementID
            {

            					newCompositeNode(grammarAccess.getElementAccess().getNameElementIDParserRuleCall_0_0());
            				
            pushFollow(FOLLOW_14);
            lv_name_0_0=ruleelementID();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getElementRule());
            					}
            					set(
            						current,
            						"name",
            						lv_name_0_0,
            						"ctwedge.CTWedge.elementID");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            // InternalCTWedge.g:371:3: (otherlv_1= ',' )?
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==20) ) {
                alt7=1;
            }
            switch (alt7) {
                case 1 :
                    // InternalCTWedge.g:372:4: otherlv_1= ','
                    {
                    otherlv_1=(Token)match(input,20,FOLLOW_2); 

                    				newLeafNode(otherlv_1, grammarAccess.getElementAccess().getCommaKeyword_1());
                    			

                    }
                    break;

            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleElement"


    // $ANTLR start "entryRuleRange"
    // InternalCTWedge.g:381:1: entryRuleRange returns [EObject current=null] : iv_ruleRange= ruleRange EOF ;
    public final EObject entryRuleRange() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleRange = null;


        try {
            // InternalCTWedge.g:381:46: (iv_ruleRange= ruleRange EOF )
            // InternalCTWedge.g:382:2: iv_ruleRange= ruleRange EOF
            {
             newCompositeNode(grammarAccess.getRangeRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleRange=ruleRange();

            state._fsp--;

             current =iv_ruleRange; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleRange"


    // $ANTLR start "ruleRange"
    // InternalCTWedge.g:388:1: ruleRange returns [EObject current=null] : ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= ':' otherlv_2= '[' ( (lv_begin_3_0= rulePossiblySignedNumber ) ) otherlv_4= '..' ( (lv_end_5_0= rulePossiblySignedNumber ) ) otherlv_6= ']' (otherlv_7= 'step' ( (lv_step_8_0= RULE_INT ) ) )? ) ;
    public final EObject ruleRange() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;
        Token otherlv_1=null;
        Token otherlv_2=null;
        Token otherlv_4=null;
        Token otherlv_6=null;
        Token otherlv_7=null;
        Token lv_step_8_0=null;
        AntlrDatatypeRuleToken lv_begin_3_0 = null;

        AntlrDatatypeRuleToken lv_end_5_0 = null;



        	enterRule();

        try {
            // InternalCTWedge.g:394:2: ( ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= ':' otherlv_2= '[' ( (lv_begin_3_0= rulePossiblySignedNumber ) ) otherlv_4= '..' ( (lv_end_5_0= rulePossiblySignedNumber ) ) otherlv_6= ']' (otherlv_7= 'step' ( (lv_step_8_0= RULE_INT ) ) )? ) )
            // InternalCTWedge.g:395:2: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= ':' otherlv_2= '[' ( (lv_begin_3_0= rulePossiblySignedNumber ) ) otherlv_4= '..' ( (lv_end_5_0= rulePossiblySignedNumber ) ) otherlv_6= ']' (otherlv_7= 'step' ( (lv_step_8_0= RULE_INT ) ) )? )
            {
            // InternalCTWedge.g:395:2: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= ':' otherlv_2= '[' ( (lv_begin_3_0= rulePossiblySignedNumber ) ) otherlv_4= '..' ( (lv_end_5_0= rulePossiblySignedNumber ) ) otherlv_6= ']' (otherlv_7= 'step' ( (lv_step_8_0= RULE_INT ) ) )? )
            // InternalCTWedge.g:396:3: ( (lv_name_0_0= RULE_ID ) ) otherlv_1= ':' otherlv_2= '[' ( (lv_begin_3_0= rulePossiblySignedNumber ) ) otherlv_4= '..' ( (lv_end_5_0= rulePossiblySignedNumber ) ) otherlv_6= ']' (otherlv_7= 'step' ( (lv_step_8_0= RULE_INT ) ) )?
            {
            // InternalCTWedge.g:396:3: ( (lv_name_0_0= RULE_ID ) )
            // InternalCTWedge.g:397:4: (lv_name_0_0= RULE_ID )
            {
            // InternalCTWedge.g:397:4: (lv_name_0_0= RULE_ID )
            // InternalCTWedge.g:398:5: lv_name_0_0= RULE_ID
            {
            lv_name_0_0=(Token)match(input,RULE_ID,FOLLOW_5); 

            					newLeafNode(lv_name_0_0, grammarAccess.getRangeAccess().getNameIDTerminalRuleCall_0_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getRangeRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_0_0,
            						"org.eclipse.xtext.common.Terminals.ID");
            				

            }


            }

            otherlv_1=(Token)match(input,14,FOLLOW_15); 

            			newLeafNode(otherlv_1, grammarAccess.getRangeAccess().getColonKeyword_1());
            		
            otherlv_2=(Token)match(input,21,FOLLOW_16); 

            			newLeafNode(otherlv_2, grammarAccess.getRangeAccess().getLeftSquareBracketKeyword_2());
            		
            // InternalCTWedge.g:422:3: ( (lv_begin_3_0= rulePossiblySignedNumber ) )
            // InternalCTWedge.g:423:4: (lv_begin_3_0= rulePossiblySignedNumber )
            {
            // InternalCTWedge.g:423:4: (lv_begin_3_0= rulePossiblySignedNumber )
            // InternalCTWedge.g:424:5: lv_begin_3_0= rulePossiblySignedNumber
            {

            					newCompositeNode(grammarAccess.getRangeAccess().getBeginPossiblySignedNumberParserRuleCall_3_0());
            				
            pushFollow(FOLLOW_17);
            lv_begin_3_0=rulePossiblySignedNumber();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getRangeRule());
            					}
            					set(
            						current,
            						"begin",
            						lv_begin_3_0,
            						"ctwedge.CTWedge.PossiblySignedNumber");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            otherlv_4=(Token)match(input,22,FOLLOW_16); 

            			newLeafNode(otherlv_4, grammarAccess.getRangeAccess().getFullStopFullStopKeyword_4());
            		
            // InternalCTWedge.g:445:3: ( (lv_end_5_0= rulePossiblySignedNumber ) )
            // InternalCTWedge.g:446:4: (lv_end_5_0= rulePossiblySignedNumber )
            {
            // InternalCTWedge.g:446:4: (lv_end_5_0= rulePossiblySignedNumber )
            // InternalCTWedge.g:447:5: lv_end_5_0= rulePossiblySignedNumber
            {

            					newCompositeNode(grammarAccess.getRangeAccess().getEndPossiblySignedNumberParserRuleCall_5_0());
            				
            pushFollow(FOLLOW_18);
            lv_end_5_0=rulePossiblySignedNumber();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getRangeRule());
            					}
            					set(
            						current,
            						"end",
            						lv_end_5_0,
            						"ctwedge.CTWedge.PossiblySignedNumber");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            otherlv_6=(Token)match(input,23,FOLLOW_19); 

            			newLeafNode(otherlv_6, grammarAccess.getRangeAccess().getRightSquareBracketKeyword_6());
            		
            // InternalCTWedge.g:468:3: (otherlv_7= 'step' ( (lv_step_8_0= RULE_INT ) ) )?
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0==24) ) {
                alt8=1;
            }
            switch (alt8) {
                case 1 :
                    // InternalCTWedge.g:469:4: otherlv_7= 'step' ( (lv_step_8_0= RULE_INT ) )
                    {
                    otherlv_7=(Token)match(input,24,FOLLOW_20); 

                    				newLeafNode(otherlv_7, grammarAccess.getRangeAccess().getStepKeyword_7_0());
                    			
                    // InternalCTWedge.g:473:4: ( (lv_step_8_0= RULE_INT ) )
                    // InternalCTWedge.g:474:5: (lv_step_8_0= RULE_INT )
                    {
                    // InternalCTWedge.g:474:5: (lv_step_8_0= RULE_INT )
                    // InternalCTWedge.g:475:6: lv_step_8_0= RULE_INT
                    {
                    lv_step_8_0=(Token)match(input,RULE_INT,FOLLOW_2); 

                    						newLeafNode(lv_step_8_0, grammarAccess.getRangeAccess().getStepINTTerminalRuleCall_7_1_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getRangeRule());
                    						}
                    						setWithLastConsumed(
                    							current,
                    							"step",
                    							lv_step_8_0,
                    							"org.eclipse.xtext.common.Terminals.INT");
                    					

                    }


                    }


                    }
                    break;

            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleRange"


    // $ANTLR start "entryRuleConstraint"
    // InternalCTWedge.g:496:1: entryRuleConstraint returns [EObject current=null] : iv_ruleConstraint= ruleConstraint EOF ;
    public final EObject entryRuleConstraint() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleConstraint = null;


        try {
            // InternalCTWedge.g:496:51: (iv_ruleConstraint= ruleConstraint EOF )
            // InternalCTWedge.g:497:2: iv_ruleConstraint= ruleConstraint EOF
            {
             newCompositeNode(grammarAccess.getConstraintRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleConstraint=ruleConstraint();

            state._fsp--;

             current =iv_ruleConstraint; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleConstraint"


    // $ANTLR start "ruleConstraint"
    // InternalCTWedge.g:503:1: ruleConstraint returns [EObject current=null] : (otherlv_0= '#' this_ImpliesExpression_1= ruleImpliesExpression otherlv_2= '#' ) ;
    public final EObject ruleConstraint() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_2=null;
        EObject this_ImpliesExpression_1 = null;



        	enterRule();

        try {
            // InternalCTWedge.g:509:2: ( (otherlv_0= '#' this_ImpliesExpression_1= ruleImpliesExpression otherlv_2= '#' ) )
            // InternalCTWedge.g:510:2: (otherlv_0= '#' this_ImpliesExpression_1= ruleImpliesExpression otherlv_2= '#' )
            {
            // InternalCTWedge.g:510:2: (otherlv_0= '#' this_ImpliesExpression_1= ruleImpliesExpression otherlv_2= '#' )
            // InternalCTWedge.g:511:3: otherlv_0= '#' this_ImpliesExpression_1= ruleImpliesExpression otherlv_2= '#'
            {
            otherlv_0=(Token)match(input,25,FOLLOW_21); 

            			newLeafNode(otherlv_0, grammarAccess.getConstraintAccess().getNumberSignKeyword_0());
            		

            			newCompositeNode(grammarAccess.getConstraintAccess().getImpliesExpressionParserRuleCall_1());
            		
            pushFollow(FOLLOW_7);
            this_ImpliesExpression_1=ruleImpliesExpression();

            state._fsp--;


            			current = this_ImpliesExpression_1;
            			afterParserOrEnumRuleCall();
            		
            otherlv_2=(Token)match(input,25,FOLLOW_2); 

            			newLeafNode(otherlv_2, grammarAccess.getConstraintAccess().getNumberSignKeyword_2());
            		

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleConstraint"


    // $ANTLR start "entryRuleImpliesExpression"
    // InternalCTWedge.g:531:1: entryRuleImpliesExpression returns [EObject current=null] : iv_ruleImpliesExpression= ruleImpliesExpression EOF ;
    public final EObject entryRuleImpliesExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleImpliesExpression = null;


        try {
            // InternalCTWedge.g:531:58: (iv_ruleImpliesExpression= ruleImpliesExpression EOF )
            // InternalCTWedge.g:532:2: iv_ruleImpliesExpression= ruleImpliesExpression EOF
            {
             newCompositeNode(grammarAccess.getImpliesExpressionRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleImpliesExpression=ruleImpliesExpression();

            state._fsp--;

             current =iv_ruleImpliesExpression; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleImpliesExpression"


    // $ANTLR start "ruleImpliesExpression"
    // InternalCTWedge.g:538:1: ruleImpliesExpression returns [EObject current=null] : (this_OrExpression_0= ruleOrExpression ( () ( (lv_op_2_0= ruleImpliesOperator ) ) ( (lv_right_3_0= ruleOrExpression ) ) )* ) ;
    public final EObject ruleImpliesExpression() throws RecognitionException {
        EObject current = null;

        EObject this_OrExpression_0 = null;

        Enumerator lv_op_2_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCTWedge.g:544:2: ( (this_OrExpression_0= ruleOrExpression ( () ( (lv_op_2_0= ruleImpliesOperator ) ) ( (lv_right_3_0= ruleOrExpression ) ) )* ) )
            // InternalCTWedge.g:545:2: (this_OrExpression_0= ruleOrExpression ( () ( (lv_op_2_0= ruleImpliesOperator ) ) ( (lv_right_3_0= ruleOrExpression ) ) )* )
            {
            // InternalCTWedge.g:545:2: (this_OrExpression_0= ruleOrExpression ( () ( (lv_op_2_0= ruleImpliesOperator ) ) ( (lv_right_3_0= ruleOrExpression ) ) )* )
            // InternalCTWedge.g:546:3: this_OrExpression_0= ruleOrExpression ( () ( (lv_op_2_0= ruleImpliesOperator ) ) ( (lv_right_3_0= ruleOrExpression ) ) )*
            {

            			newCompositeNode(grammarAccess.getImpliesExpressionAccess().getOrExpressionParserRuleCall_0());
            		
            pushFollow(FOLLOW_22);
            this_OrExpression_0=ruleOrExpression();

            state._fsp--;


            			current = this_OrExpression_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCTWedge.g:554:3: ( () ( (lv_op_2_0= ruleImpliesOperator ) ) ( (lv_right_3_0= ruleOrExpression ) ) )*
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);

                if ( ((LA9_0>=54 && LA9_0<=57)) ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // InternalCTWedge.g:555:4: () ( (lv_op_2_0= ruleImpliesOperator ) ) ( (lv_right_3_0= ruleOrExpression ) )
            	    {
            	    // InternalCTWedge.g:555:4: ()
            	    // InternalCTWedge.g:556:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getImpliesExpressionAccess().getImpliesExpressionLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    // InternalCTWedge.g:562:4: ( (lv_op_2_0= ruleImpliesOperator ) )
            	    // InternalCTWedge.g:563:5: (lv_op_2_0= ruleImpliesOperator )
            	    {
            	    // InternalCTWedge.g:563:5: (lv_op_2_0= ruleImpliesOperator )
            	    // InternalCTWedge.g:564:6: lv_op_2_0= ruleImpliesOperator
            	    {

            	    						newCompositeNode(grammarAccess.getImpliesExpressionAccess().getOpImpliesOperatorEnumRuleCall_1_1_0());
            	    					
            	    pushFollow(FOLLOW_21);
            	    lv_op_2_0=ruleImpliesOperator();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getImpliesExpressionRule());
            	    						}
            	    						set(
            	    							current,
            	    							"op",
            	    							lv_op_2_0,
            	    							"ctwedge.CTWedge.ImpliesOperator");
            	    						afterParserOrEnumRuleCall();
            	    					

            	    }


            	    }

            	    // InternalCTWedge.g:581:4: ( (lv_right_3_0= ruleOrExpression ) )
            	    // InternalCTWedge.g:582:5: (lv_right_3_0= ruleOrExpression )
            	    {
            	    // InternalCTWedge.g:582:5: (lv_right_3_0= ruleOrExpression )
            	    // InternalCTWedge.g:583:6: lv_right_3_0= ruleOrExpression
            	    {

            	    						newCompositeNode(grammarAccess.getImpliesExpressionAccess().getRightOrExpressionParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_22);
            	    lv_right_3_0=ruleOrExpression();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getImpliesExpressionRule());
            	    						}
            	    						set(
            	    							current,
            	    							"right",
            	    							lv_right_3_0,
            	    							"ctwedge.CTWedge.OrExpression");
            	    						afterParserOrEnumRuleCall();
            	    					

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop9;
                }
            } while (true);


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleImpliesExpression"


    // $ANTLR start "entryRuleOrExpression"
    // InternalCTWedge.g:605:1: entryRuleOrExpression returns [EObject current=null] : iv_ruleOrExpression= ruleOrExpression EOF ;
    public final EObject entryRuleOrExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleOrExpression = null;


        try {
            // InternalCTWedge.g:605:53: (iv_ruleOrExpression= ruleOrExpression EOF )
            // InternalCTWedge.g:606:2: iv_ruleOrExpression= ruleOrExpression EOF
            {
             newCompositeNode(grammarAccess.getOrExpressionRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleOrExpression=ruleOrExpression();

            state._fsp--;

             current =iv_ruleOrExpression; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleOrExpression"


    // $ANTLR start "ruleOrExpression"
    // InternalCTWedge.g:612:1: ruleOrExpression returns [EObject current=null] : (this_AndExpression_0= ruleAndExpression ( () ruleOR_OPERATOR ( (lv_right_3_0= ruleAndExpression ) ) )* ) ;
    public final EObject ruleOrExpression() throws RecognitionException {
        EObject current = null;

        EObject this_AndExpression_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCTWedge.g:618:2: ( (this_AndExpression_0= ruleAndExpression ( () ruleOR_OPERATOR ( (lv_right_3_0= ruleAndExpression ) ) )* ) )
            // InternalCTWedge.g:619:2: (this_AndExpression_0= ruleAndExpression ( () ruleOR_OPERATOR ( (lv_right_3_0= ruleAndExpression ) ) )* )
            {
            // InternalCTWedge.g:619:2: (this_AndExpression_0= ruleAndExpression ( () ruleOR_OPERATOR ( (lv_right_3_0= ruleAndExpression ) ) )* )
            // InternalCTWedge.g:620:3: this_AndExpression_0= ruleAndExpression ( () ruleOR_OPERATOR ( (lv_right_3_0= ruleAndExpression ) ) )*
            {

            			newCompositeNode(grammarAccess.getOrExpressionAccess().getAndExpressionParserRuleCall_0());
            		
            pushFollow(FOLLOW_23);
            this_AndExpression_0=ruleAndExpression();

            state._fsp--;


            			current = this_AndExpression_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCTWedge.g:628:3: ( () ruleOR_OPERATOR ( (lv_right_3_0= ruleAndExpression ) ) )*
            loop10:
            do {
                int alt10=2;
                int LA10_0 = input.LA(1);

                if ( ((LA10_0>=28 && LA10_0<=31)) ) {
                    alt10=1;
                }


                switch (alt10) {
            	case 1 :
            	    // InternalCTWedge.g:629:4: () ruleOR_OPERATOR ( (lv_right_3_0= ruleAndExpression ) )
            	    {
            	    // InternalCTWedge.g:629:4: ()
            	    // InternalCTWedge.g:630:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getOrExpressionAccess().getOrExpressionLeftAction_1_0(),
            	    						current);
            	    				

            	    }


            	    				newCompositeNode(grammarAccess.getOrExpressionAccess().getOR_OPERATORParserRuleCall_1_1());
            	    			
            	    pushFollow(FOLLOW_21);
            	    ruleOR_OPERATOR();

            	    state._fsp--;


            	    				afterParserOrEnumRuleCall();
            	    			
            	    // InternalCTWedge.g:643:4: ( (lv_right_3_0= ruleAndExpression ) )
            	    // InternalCTWedge.g:644:5: (lv_right_3_0= ruleAndExpression )
            	    {
            	    // InternalCTWedge.g:644:5: (lv_right_3_0= ruleAndExpression )
            	    // InternalCTWedge.g:645:6: lv_right_3_0= ruleAndExpression
            	    {

            	    						newCompositeNode(grammarAccess.getOrExpressionAccess().getRightAndExpressionParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_23);
            	    lv_right_3_0=ruleAndExpression();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getOrExpressionRule());
            	    						}
            	    						set(
            	    							current,
            	    							"right",
            	    							lv_right_3_0,
            	    							"ctwedge.CTWedge.AndExpression");
            	    						afterParserOrEnumRuleCall();
            	    					

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop10;
                }
            } while (true);


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleOrExpression"


    // $ANTLR start "entryRuleAndExpression"
    // InternalCTWedge.g:667:1: entryRuleAndExpression returns [EObject current=null] : iv_ruleAndExpression= ruleAndExpression EOF ;
    public final EObject entryRuleAndExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAndExpression = null;


        try {
            // InternalCTWedge.g:667:54: (iv_ruleAndExpression= ruleAndExpression EOF )
            // InternalCTWedge.g:668:2: iv_ruleAndExpression= ruleAndExpression EOF
            {
             newCompositeNode(grammarAccess.getAndExpressionRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleAndExpression=ruleAndExpression();

            state._fsp--;

             current =iv_ruleAndExpression; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleAndExpression"


    // $ANTLR start "ruleAndExpression"
    // InternalCTWedge.g:674:1: ruleAndExpression returns [EObject current=null] : (this_EqualExpression_0= ruleEqualExpression ( () ruleAND_OPERATOR ( (lv_right_3_0= ruleEqualExpression ) ) )* ) ;
    public final EObject ruleAndExpression() throws RecognitionException {
        EObject current = null;

        EObject this_EqualExpression_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCTWedge.g:680:2: ( (this_EqualExpression_0= ruleEqualExpression ( () ruleAND_OPERATOR ( (lv_right_3_0= ruleEqualExpression ) ) )* ) )
            // InternalCTWedge.g:681:2: (this_EqualExpression_0= ruleEqualExpression ( () ruleAND_OPERATOR ( (lv_right_3_0= ruleEqualExpression ) ) )* )
            {
            // InternalCTWedge.g:681:2: (this_EqualExpression_0= ruleEqualExpression ( () ruleAND_OPERATOR ( (lv_right_3_0= ruleEqualExpression ) ) )* )
            // InternalCTWedge.g:682:3: this_EqualExpression_0= ruleEqualExpression ( () ruleAND_OPERATOR ( (lv_right_3_0= ruleEqualExpression ) ) )*
            {

            			newCompositeNode(grammarAccess.getAndExpressionAccess().getEqualExpressionParserRuleCall_0());
            		
            pushFollow(FOLLOW_24);
            this_EqualExpression_0=ruleEqualExpression();

            state._fsp--;


            			current = this_EqualExpression_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCTWedge.g:690:3: ( () ruleAND_OPERATOR ( (lv_right_3_0= ruleEqualExpression ) ) )*
            loop11:
            do {
                int alt11=2;
                int LA11_0 = input.LA(1);

                if ( ((LA11_0>=32 && LA11_0<=35)) ) {
                    alt11=1;
                }


                switch (alt11) {
            	case 1 :
            	    // InternalCTWedge.g:691:4: () ruleAND_OPERATOR ( (lv_right_3_0= ruleEqualExpression ) )
            	    {
            	    // InternalCTWedge.g:691:4: ()
            	    // InternalCTWedge.g:692:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getAndExpressionAccess().getAndExpressionLeftAction_1_0(),
            	    						current);
            	    				

            	    }


            	    				newCompositeNode(grammarAccess.getAndExpressionAccess().getAND_OPERATORParserRuleCall_1_1());
            	    			
            	    pushFollow(FOLLOW_21);
            	    ruleAND_OPERATOR();

            	    state._fsp--;


            	    				afterParserOrEnumRuleCall();
            	    			
            	    // InternalCTWedge.g:705:4: ( (lv_right_3_0= ruleEqualExpression ) )
            	    // InternalCTWedge.g:706:5: (lv_right_3_0= ruleEqualExpression )
            	    {
            	    // InternalCTWedge.g:706:5: (lv_right_3_0= ruleEqualExpression )
            	    // InternalCTWedge.g:707:6: lv_right_3_0= ruleEqualExpression
            	    {

            	    						newCompositeNode(grammarAccess.getAndExpressionAccess().getRightEqualExpressionParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_24);
            	    lv_right_3_0=ruleEqualExpression();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getAndExpressionRule());
            	    						}
            	    						set(
            	    							current,
            	    							"right",
            	    							lv_right_3_0,
            	    							"ctwedge.CTWedge.EqualExpression");
            	    						afterParserOrEnumRuleCall();
            	    					

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop11;
                }
            } while (true);


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleAndExpression"


    // $ANTLR start "entryRuleEqualExpression"
    // InternalCTWedge.g:729:1: entryRuleEqualExpression returns [EObject current=null] : iv_ruleEqualExpression= ruleEqualExpression EOF ;
    public final EObject entryRuleEqualExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleEqualExpression = null;


        try {
            // InternalCTWedge.g:729:56: (iv_ruleEqualExpression= ruleEqualExpression EOF )
            // InternalCTWedge.g:730:2: iv_ruleEqualExpression= ruleEqualExpression EOF
            {
             newCompositeNode(grammarAccess.getEqualExpressionRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleEqualExpression=ruleEqualExpression();

            state._fsp--;

             current =iv_ruleEqualExpression; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleEqualExpression"


    // $ANTLR start "ruleEqualExpression"
    // InternalCTWedge.g:736:1: ruleEqualExpression returns [EObject current=null] : (this_RelationalExpression_0= ruleRelationalExpression ( () ( (lv_op_2_0= ruleEqualityOperators ) ) ( (lv_right_3_0= ruleRelationalExpression ) ) )* ) ;
    public final EObject ruleEqualExpression() throws RecognitionException {
        EObject current = null;

        EObject this_RelationalExpression_0 = null;

        Enumerator lv_op_2_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCTWedge.g:742:2: ( (this_RelationalExpression_0= ruleRelationalExpression ( () ( (lv_op_2_0= ruleEqualityOperators ) ) ( (lv_right_3_0= ruleRelationalExpression ) ) )* ) )
            // InternalCTWedge.g:743:2: (this_RelationalExpression_0= ruleRelationalExpression ( () ( (lv_op_2_0= ruleEqualityOperators ) ) ( (lv_right_3_0= ruleRelationalExpression ) ) )* )
            {
            // InternalCTWedge.g:743:2: (this_RelationalExpression_0= ruleRelationalExpression ( () ( (lv_op_2_0= ruleEqualityOperators ) ) ( (lv_right_3_0= ruleRelationalExpression ) ) )* )
            // InternalCTWedge.g:744:3: this_RelationalExpression_0= ruleRelationalExpression ( () ( (lv_op_2_0= ruleEqualityOperators ) ) ( (lv_right_3_0= ruleRelationalExpression ) ) )*
            {

            			newCompositeNode(grammarAccess.getEqualExpressionAccess().getRelationalExpressionParserRuleCall_0());
            		
            pushFollow(FOLLOW_25);
            this_RelationalExpression_0=ruleRelationalExpression();

            state._fsp--;


            			current = this_RelationalExpression_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCTWedge.g:752:3: ( () ( (lv_op_2_0= ruleEqualityOperators ) ) ( (lv_right_3_0= ruleRelationalExpression ) ) )*
            loop12:
            do {
                int alt12=2;
                int LA12_0 = input.LA(1);

                if ( ((LA12_0>=47 && LA12_0<=49)) ) {
                    alt12=1;
                }


                switch (alt12) {
            	case 1 :
            	    // InternalCTWedge.g:753:4: () ( (lv_op_2_0= ruleEqualityOperators ) ) ( (lv_right_3_0= ruleRelationalExpression ) )
            	    {
            	    // InternalCTWedge.g:753:4: ()
            	    // InternalCTWedge.g:754:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getEqualExpressionAccess().getEqualExpressionLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    // InternalCTWedge.g:760:4: ( (lv_op_2_0= ruleEqualityOperators ) )
            	    // InternalCTWedge.g:761:5: (lv_op_2_0= ruleEqualityOperators )
            	    {
            	    // InternalCTWedge.g:761:5: (lv_op_2_0= ruleEqualityOperators )
            	    // InternalCTWedge.g:762:6: lv_op_2_0= ruleEqualityOperators
            	    {

            	    						newCompositeNode(grammarAccess.getEqualExpressionAccess().getOpEqualityOperatorsEnumRuleCall_1_1_0());
            	    					
            	    pushFollow(FOLLOW_21);
            	    lv_op_2_0=ruleEqualityOperators();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getEqualExpressionRule());
            	    						}
            	    						set(
            	    							current,
            	    							"op",
            	    							lv_op_2_0,
            	    							"ctwedge.CTWedge.EqualityOperators");
            	    						afterParserOrEnumRuleCall();
            	    					

            	    }


            	    }

            	    // InternalCTWedge.g:779:4: ( (lv_right_3_0= ruleRelationalExpression ) )
            	    // InternalCTWedge.g:780:5: (lv_right_3_0= ruleRelationalExpression )
            	    {
            	    // InternalCTWedge.g:780:5: (lv_right_3_0= ruleRelationalExpression )
            	    // InternalCTWedge.g:781:6: lv_right_3_0= ruleRelationalExpression
            	    {

            	    						newCompositeNode(grammarAccess.getEqualExpressionAccess().getRightRelationalExpressionParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_25);
            	    lv_right_3_0=ruleRelationalExpression();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getEqualExpressionRule());
            	    						}
            	    						set(
            	    							current,
            	    							"right",
            	    							lv_right_3_0,
            	    							"ctwedge.CTWedge.RelationalExpression");
            	    						afterParserOrEnumRuleCall();
            	    					

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop12;
                }
            } while (true);


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleEqualExpression"


    // $ANTLR start "entryRuleRelationalExpression"
    // InternalCTWedge.g:803:1: entryRuleRelationalExpression returns [EObject current=null] : iv_ruleRelationalExpression= ruleRelationalExpression EOF ;
    public final EObject entryRuleRelationalExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleRelationalExpression = null;


        try {
            // InternalCTWedge.g:803:61: (iv_ruleRelationalExpression= ruleRelationalExpression EOF )
            // InternalCTWedge.g:804:2: iv_ruleRelationalExpression= ruleRelationalExpression EOF
            {
             newCompositeNode(grammarAccess.getRelationalExpressionRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleRelationalExpression=ruleRelationalExpression();

            state._fsp--;

             current =iv_ruleRelationalExpression; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleRelationalExpression"


    // $ANTLR start "ruleRelationalExpression"
    // InternalCTWedge.g:810:1: ruleRelationalExpression returns [EObject current=null] : (this_PlusMinus_0= rulePlusMinus ( () ( (lv_op_2_0= ruleRelationalOperators ) ) ( (lv_right_3_0= rulePlusMinus ) ) )* ) ;
    public final EObject ruleRelationalExpression() throws RecognitionException {
        EObject current = null;

        EObject this_PlusMinus_0 = null;

        Enumerator lv_op_2_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCTWedge.g:816:2: ( (this_PlusMinus_0= rulePlusMinus ( () ( (lv_op_2_0= ruleRelationalOperators ) ) ( (lv_right_3_0= rulePlusMinus ) ) )* ) )
            // InternalCTWedge.g:817:2: (this_PlusMinus_0= rulePlusMinus ( () ( (lv_op_2_0= ruleRelationalOperators ) ) ( (lv_right_3_0= rulePlusMinus ) ) )* )
            {
            // InternalCTWedge.g:817:2: (this_PlusMinus_0= rulePlusMinus ( () ( (lv_op_2_0= ruleRelationalOperators ) ) ( (lv_right_3_0= rulePlusMinus ) ) )* )
            // InternalCTWedge.g:818:3: this_PlusMinus_0= rulePlusMinus ( () ( (lv_op_2_0= ruleRelationalOperators ) ) ( (lv_right_3_0= rulePlusMinus ) ) )*
            {

            			newCompositeNode(grammarAccess.getRelationalExpressionAccess().getPlusMinusParserRuleCall_0());
            		
            pushFollow(FOLLOW_26);
            this_PlusMinus_0=rulePlusMinus();

            state._fsp--;


            			current = this_PlusMinus_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCTWedge.g:826:3: ( () ( (lv_op_2_0= ruleRelationalOperators ) ) ( (lv_right_3_0= rulePlusMinus ) ) )*
            loop13:
            do {
                int alt13=2;
                int LA13_0 = input.LA(1);

                if ( ((LA13_0>=43 && LA13_0<=46)) ) {
                    alt13=1;
                }


                switch (alt13) {
            	case 1 :
            	    // InternalCTWedge.g:827:4: () ( (lv_op_2_0= ruleRelationalOperators ) ) ( (lv_right_3_0= rulePlusMinus ) )
            	    {
            	    // InternalCTWedge.g:827:4: ()
            	    // InternalCTWedge.g:828:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getRelationalExpressionAccess().getRelationalExpressionLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    // InternalCTWedge.g:834:4: ( (lv_op_2_0= ruleRelationalOperators ) )
            	    // InternalCTWedge.g:835:5: (lv_op_2_0= ruleRelationalOperators )
            	    {
            	    // InternalCTWedge.g:835:5: (lv_op_2_0= ruleRelationalOperators )
            	    // InternalCTWedge.g:836:6: lv_op_2_0= ruleRelationalOperators
            	    {

            	    						newCompositeNode(grammarAccess.getRelationalExpressionAccess().getOpRelationalOperatorsEnumRuleCall_1_1_0());
            	    					
            	    pushFollow(FOLLOW_21);
            	    lv_op_2_0=ruleRelationalOperators();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getRelationalExpressionRule());
            	    						}
            	    						set(
            	    							current,
            	    							"op",
            	    							lv_op_2_0,
            	    							"ctwedge.CTWedge.RelationalOperators");
            	    						afterParserOrEnumRuleCall();
            	    					

            	    }


            	    }

            	    // InternalCTWedge.g:853:4: ( (lv_right_3_0= rulePlusMinus ) )
            	    // InternalCTWedge.g:854:5: (lv_right_3_0= rulePlusMinus )
            	    {
            	    // InternalCTWedge.g:854:5: (lv_right_3_0= rulePlusMinus )
            	    // InternalCTWedge.g:855:6: lv_right_3_0= rulePlusMinus
            	    {

            	    						newCompositeNode(grammarAccess.getRelationalExpressionAccess().getRightPlusMinusParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_26);
            	    lv_right_3_0=rulePlusMinus();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getRelationalExpressionRule());
            	    						}
            	    						set(
            	    							current,
            	    							"right",
            	    							lv_right_3_0,
            	    							"ctwedge.CTWedge.PlusMinus");
            	    						afterParserOrEnumRuleCall();
            	    					

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop13;
                }
            } while (true);


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleRelationalExpression"


    // $ANTLR start "entryRulePlusMinus"
    // InternalCTWedge.g:877:1: entryRulePlusMinus returns [EObject current=null] : iv_rulePlusMinus= rulePlusMinus EOF ;
    public final EObject entryRulePlusMinus() throws RecognitionException {
        EObject current = null;

        EObject iv_rulePlusMinus = null;


        try {
            // InternalCTWedge.g:877:50: (iv_rulePlusMinus= rulePlusMinus EOF )
            // InternalCTWedge.g:878:2: iv_rulePlusMinus= rulePlusMinus EOF
            {
             newCompositeNode(grammarAccess.getPlusMinusRule()); 
            pushFollow(FOLLOW_1);
            iv_rulePlusMinus=rulePlusMinus();

            state._fsp--;

             current =iv_rulePlusMinus; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRulePlusMinus"


    // $ANTLR start "rulePlusMinus"
    // InternalCTWedge.g:884:1: rulePlusMinus returns [EObject current=null] : (this_ModMultDiv_0= ruleModMultDiv ( () ( (lv_op_2_0= rulePlusMinusOperators ) ) ( (lv_right_3_0= ruleModMultDiv ) ) )* ) ;
    public final EObject rulePlusMinus() throws RecognitionException {
        EObject current = null;

        EObject this_ModMultDiv_0 = null;

        Enumerator lv_op_2_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCTWedge.g:890:2: ( (this_ModMultDiv_0= ruleModMultDiv ( () ( (lv_op_2_0= rulePlusMinusOperators ) ) ( (lv_right_3_0= ruleModMultDiv ) ) )* ) )
            // InternalCTWedge.g:891:2: (this_ModMultDiv_0= ruleModMultDiv ( () ( (lv_op_2_0= rulePlusMinusOperators ) ) ( (lv_right_3_0= ruleModMultDiv ) ) )* )
            {
            // InternalCTWedge.g:891:2: (this_ModMultDiv_0= ruleModMultDiv ( () ( (lv_op_2_0= rulePlusMinusOperators ) ) ( (lv_right_3_0= ruleModMultDiv ) ) )* )
            // InternalCTWedge.g:892:3: this_ModMultDiv_0= ruleModMultDiv ( () ( (lv_op_2_0= rulePlusMinusOperators ) ) ( (lv_right_3_0= ruleModMultDiv ) ) )*
            {

            			newCompositeNode(grammarAccess.getPlusMinusAccess().getModMultDivParserRuleCall_0());
            		
            pushFollow(FOLLOW_27);
            this_ModMultDiv_0=ruleModMultDiv();

            state._fsp--;


            			current = this_ModMultDiv_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCTWedge.g:900:3: ( () ( (lv_op_2_0= rulePlusMinusOperators ) ) ( (lv_right_3_0= ruleModMultDiv ) ) )*
            loop14:
            do {
                int alt14=2;
                int LA14_0 = input.LA(1);

                if ( (LA14_0==42||LA14_0==50) ) {
                    alt14=1;
                }


                switch (alt14) {
            	case 1 :
            	    // InternalCTWedge.g:901:4: () ( (lv_op_2_0= rulePlusMinusOperators ) ) ( (lv_right_3_0= ruleModMultDiv ) )
            	    {
            	    // InternalCTWedge.g:901:4: ()
            	    // InternalCTWedge.g:902:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getPlusMinusAccess().getPlusMinusLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    // InternalCTWedge.g:908:4: ( (lv_op_2_0= rulePlusMinusOperators ) )
            	    // InternalCTWedge.g:909:5: (lv_op_2_0= rulePlusMinusOperators )
            	    {
            	    // InternalCTWedge.g:909:5: (lv_op_2_0= rulePlusMinusOperators )
            	    // InternalCTWedge.g:910:6: lv_op_2_0= rulePlusMinusOperators
            	    {

            	    						newCompositeNode(grammarAccess.getPlusMinusAccess().getOpPlusMinusOperatorsEnumRuleCall_1_1_0());
            	    					
            	    pushFollow(FOLLOW_21);
            	    lv_op_2_0=rulePlusMinusOperators();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getPlusMinusRule());
            	    						}
            	    						set(
            	    							current,
            	    							"op",
            	    							lv_op_2_0,
            	    							"ctwedge.CTWedge.PlusMinusOperators");
            	    						afterParserOrEnumRuleCall();
            	    					

            	    }


            	    }

            	    // InternalCTWedge.g:927:4: ( (lv_right_3_0= ruleModMultDiv ) )
            	    // InternalCTWedge.g:928:5: (lv_right_3_0= ruleModMultDiv )
            	    {
            	    // InternalCTWedge.g:928:5: (lv_right_3_0= ruleModMultDiv )
            	    // InternalCTWedge.g:929:6: lv_right_3_0= ruleModMultDiv
            	    {

            	    						newCompositeNode(grammarAccess.getPlusMinusAccess().getRightModMultDivParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_27);
            	    lv_right_3_0=ruleModMultDiv();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getPlusMinusRule());
            	    						}
            	    						set(
            	    							current,
            	    							"right",
            	    							lv_right_3_0,
            	    							"ctwedge.CTWedge.ModMultDiv");
            	    						afterParserOrEnumRuleCall();
            	    					

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop14;
                }
            } while (true);


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "rulePlusMinus"


    // $ANTLR start "entryRuleModMultDiv"
    // InternalCTWedge.g:951:1: entryRuleModMultDiv returns [EObject current=null] : iv_ruleModMultDiv= ruleModMultDiv EOF ;
    public final EObject entryRuleModMultDiv() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleModMultDiv = null;


        try {
            // InternalCTWedge.g:951:51: (iv_ruleModMultDiv= ruleModMultDiv EOF )
            // InternalCTWedge.g:952:2: iv_ruleModMultDiv= ruleModMultDiv EOF
            {
             newCompositeNode(grammarAccess.getModMultDivRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleModMultDiv=ruleModMultDiv();

            state._fsp--;

             current =iv_ruleModMultDiv; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleModMultDiv"


    // $ANTLR start "ruleModMultDiv"
    // InternalCTWedge.g:958:1: ruleModMultDiv returns [EObject current=null] : (this_Primary_0= rulePrimary ( () ( (lv_op_2_0= ruleModMultDivOperators ) ) ( (lv_right_3_0= rulePrimary ) ) )* ) ;
    public final EObject ruleModMultDiv() throws RecognitionException {
        EObject current = null;

        EObject this_Primary_0 = null;

        Enumerator lv_op_2_0 = null;

        EObject lv_right_3_0 = null;



        	enterRule();

        try {
            // InternalCTWedge.g:964:2: ( (this_Primary_0= rulePrimary ( () ( (lv_op_2_0= ruleModMultDivOperators ) ) ( (lv_right_3_0= rulePrimary ) ) )* ) )
            // InternalCTWedge.g:965:2: (this_Primary_0= rulePrimary ( () ( (lv_op_2_0= ruleModMultDivOperators ) ) ( (lv_right_3_0= rulePrimary ) ) )* )
            {
            // InternalCTWedge.g:965:2: (this_Primary_0= rulePrimary ( () ( (lv_op_2_0= ruleModMultDivOperators ) ) ( (lv_right_3_0= rulePrimary ) ) )* )
            // InternalCTWedge.g:966:3: this_Primary_0= rulePrimary ( () ( (lv_op_2_0= ruleModMultDivOperators ) ) ( (lv_right_3_0= rulePrimary ) ) )*
            {

            			newCompositeNode(grammarAccess.getModMultDivAccess().getPrimaryParserRuleCall_0());
            		
            pushFollow(FOLLOW_28);
            this_Primary_0=rulePrimary();

            state._fsp--;


            			current = this_Primary_0;
            			afterParserOrEnumRuleCall();
            		
            // InternalCTWedge.g:974:3: ( () ( (lv_op_2_0= ruleModMultDivOperators ) ) ( (lv_right_3_0= rulePrimary ) ) )*
            loop15:
            do {
                int alt15=2;
                int LA15_0 = input.LA(1);

                if ( ((LA15_0>=51 && LA15_0<=53)) ) {
                    alt15=1;
                }


                switch (alt15) {
            	case 1 :
            	    // InternalCTWedge.g:975:4: () ( (lv_op_2_0= ruleModMultDivOperators ) ) ( (lv_right_3_0= rulePrimary ) )
            	    {
            	    // InternalCTWedge.g:975:4: ()
            	    // InternalCTWedge.g:976:5: 
            	    {

            	    					current = forceCreateModelElementAndSet(
            	    						grammarAccess.getModMultDivAccess().getModMultDivLeftAction_1_0(),
            	    						current);
            	    				

            	    }

            	    // InternalCTWedge.g:982:4: ( (lv_op_2_0= ruleModMultDivOperators ) )
            	    // InternalCTWedge.g:983:5: (lv_op_2_0= ruleModMultDivOperators )
            	    {
            	    // InternalCTWedge.g:983:5: (lv_op_2_0= ruleModMultDivOperators )
            	    // InternalCTWedge.g:984:6: lv_op_2_0= ruleModMultDivOperators
            	    {

            	    						newCompositeNode(grammarAccess.getModMultDivAccess().getOpModMultDivOperatorsEnumRuleCall_1_1_0());
            	    					
            	    pushFollow(FOLLOW_21);
            	    lv_op_2_0=ruleModMultDivOperators();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getModMultDivRule());
            	    						}
            	    						set(
            	    							current,
            	    							"op",
            	    							lv_op_2_0,
            	    							"ctwedge.CTWedge.ModMultDivOperators");
            	    						afterParserOrEnumRuleCall();
            	    					

            	    }


            	    }

            	    // InternalCTWedge.g:1001:4: ( (lv_right_3_0= rulePrimary ) )
            	    // InternalCTWedge.g:1002:5: (lv_right_3_0= rulePrimary )
            	    {
            	    // InternalCTWedge.g:1002:5: (lv_right_3_0= rulePrimary )
            	    // InternalCTWedge.g:1003:6: lv_right_3_0= rulePrimary
            	    {

            	    						newCompositeNode(grammarAccess.getModMultDivAccess().getRightPrimaryParserRuleCall_1_2_0());
            	    					
            	    pushFollow(FOLLOW_28);
            	    lv_right_3_0=rulePrimary();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getModMultDivRule());
            	    						}
            	    						set(
            	    							current,
            	    							"right",
            	    							lv_right_3_0,
            	    							"ctwedge.CTWedge.Primary");
            	    						afterParserOrEnumRuleCall();
            	    					

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop15;
                }
            } while (true);


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleModMultDiv"


    // $ANTLR start "entryRulePrimary"
    // InternalCTWedge.g:1025:1: entryRulePrimary returns [EObject current=null] : iv_rulePrimary= rulePrimary EOF ;
    public final EObject entryRulePrimary() throws RecognitionException {
        EObject current = null;

        EObject iv_rulePrimary = null;


        try {
            // InternalCTWedge.g:1025:48: (iv_rulePrimary= rulePrimary EOF )
            // InternalCTWedge.g:1026:2: iv_rulePrimary= rulePrimary EOF
            {
             newCompositeNode(grammarAccess.getPrimaryRule()); 
            pushFollow(FOLLOW_1);
            iv_rulePrimary=rulePrimary();

            state._fsp--;

             current =iv_rulePrimary; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRulePrimary"


    // $ANTLR start "rulePrimary"
    // InternalCTWedge.g:1032:1: rulePrimary returns [EObject current=null] : (this_NotExpression_0= ruleNotExpression | (otherlv_1= '(' this_ImpliesExpression_2= ruleImpliesExpression otherlv_3= ')' ) | this_AtomicPredicate_4= ruleAtomicPredicate ) ;
    public final EObject rulePrimary() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_3=null;
        EObject this_NotExpression_0 = null;

        EObject this_ImpliesExpression_2 = null;

        EObject this_AtomicPredicate_4 = null;



        	enterRule();

        try {
            // InternalCTWedge.g:1038:2: ( (this_NotExpression_0= ruleNotExpression | (otherlv_1= '(' this_ImpliesExpression_2= ruleImpliesExpression otherlv_3= ')' ) | this_AtomicPredicate_4= ruleAtomicPredicate ) )
            // InternalCTWedge.g:1039:2: (this_NotExpression_0= ruleNotExpression | (otherlv_1= '(' this_ImpliesExpression_2= ruleImpliesExpression otherlv_3= ')' ) | this_AtomicPredicate_4= ruleAtomicPredicate )
            {
            // InternalCTWedge.g:1039:2: (this_NotExpression_0= ruleNotExpression | (otherlv_1= '(' this_ImpliesExpression_2= ruleImpliesExpression otherlv_3= ')' ) | this_AtomicPredicate_4= ruleAtomicPredicate )
            int alt16=3;
            switch ( input.LA(1) ) {
            case 36:
            case 37:
                {
                alt16=1;
                }
                break;
            case 26:
                {
                alt16=2;
                }
                break;
            case RULE_ID:
            case RULE_INT:
            case RULE_NUMID:
            case RULE_STRING:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
                {
                alt16=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 16, 0, input);

                throw nvae;
            }

            switch (alt16) {
                case 1 :
                    // InternalCTWedge.g:1040:3: this_NotExpression_0= ruleNotExpression
                    {

                    			newCompositeNode(grammarAccess.getPrimaryAccess().getNotExpressionParserRuleCall_0());
                    		
                    pushFollow(FOLLOW_2);
                    this_NotExpression_0=ruleNotExpression();

                    state._fsp--;


                    			current = this_NotExpression_0;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 2 :
                    // InternalCTWedge.g:1049:3: (otherlv_1= '(' this_ImpliesExpression_2= ruleImpliesExpression otherlv_3= ')' )
                    {
                    // InternalCTWedge.g:1049:3: (otherlv_1= '(' this_ImpliesExpression_2= ruleImpliesExpression otherlv_3= ')' )
                    // InternalCTWedge.g:1050:4: otherlv_1= '(' this_ImpliesExpression_2= ruleImpliesExpression otherlv_3= ')'
                    {
                    otherlv_1=(Token)match(input,26,FOLLOW_21); 

                    				newLeafNode(otherlv_1, grammarAccess.getPrimaryAccess().getLeftParenthesisKeyword_1_0());
                    			

                    				newCompositeNode(grammarAccess.getPrimaryAccess().getImpliesExpressionParserRuleCall_1_1());
                    			
                    pushFollow(FOLLOW_29);
                    this_ImpliesExpression_2=ruleImpliesExpression();

                    state._fsp--;


                    				current = this_ImpliesExpression_2;
                    				afterParserOrEnumRuleCall();
                    			
                    otherlv_3=(Token)match(input,27,FOLLOW_2); 

                    				newLeafNode(otherlv_3, grammarAccess.getPrimaryAccess().getRightParenthesisKeyword_1_2());
                    			

                    }


                    }
                    break;
                case 3 :
                    // InternalCTWedge.g:1068:3: this_AtomicPredicate_4= ruleAtomicPredicate
                    {

                    			newCompositeNode(grammarAccess.getPrimaryAccess().getAtomicPredicateParserRuleCall_2());
                    		
                    pushFollow(FOLLOW_2);
                    this_AtomicPredicate_4=ruleAtomicPredicate();

                    state._fsp--;


                    			current = this_AtomicPredicate_4;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "rulePrimary"


    // $ANTLR start "entryRuleNotExpression"
    // InternalCTWedge.g:1080:1: entryRuleNotExpression returns [EObject current=null] : iv_ruleNotExpression= ruleNotExpression EOF ;
    public final EObject entryRuleNotExpression() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleNotExpression = null;


        try {
            // InternalCTWedge.g:1080:54: (iv_ruleNotExpression= ruleNotExpression EOF )
            // InternalCTWedge.g:1081:2: iv_ruleNotExpression= ruleNotExpression EOF
            {
             newCompositeNode(grammarAccess.getNotExpressionRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleNotExpression=ruleNotExpression();

            state._fsp--;

             current =iv_ruleNotExpression; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleNotExpression"


    // $ANTLR start "ruleNotExpression"
    // InternalCTWedge.g:1087:1: ruleNotExpression returns [EObject current=null] : ( ruleNOT_OPERATOR this_Primary_1= rulePrimary () ) ;
    public final EObject ruleNotExpression() throws RecognitionException {
        EObject current = null;

        EObject this_Primary_1 = null;



        	enterRule();

        try {
            // InternalCTWedge.g:1093:2: ( ( ruleNOT_OPERATOR this_Primary_1= rulePrimary () ) )
            // InternalCTWedge.g:1094:2: ( ruleNOT_OPERATOR this_Primary_1= rulePrimary () )
            {
            // InternalCTWedge.g:1094:2: ( ruleNOT_OPERATOR this_Primary_1= rulePrimary () )
            // InternalCTWedge.g:1095:3: ruleNOT_OPERATOR this_Primary_1= rulePrimary ()
            {

            			newCompositeNode(grammarAccess.getNotExpressionAccess().getNOT_OPERATORParserRuleCall_0());
            		
            pushFollow(FOLLOW_21);
            ruleNOT_OPERATOR();

            state._fsp--;


            			afterParserOrEnumRuleCall();
            		

            			newCompositeNode(grammarAccess.getNotExpressionAccess().getPrimaryParserRuleCall_1());
            		
            pushFollow(FOLLOW_2);
            this_Primary_1=rulePrimary();

            state._fsp--;


            			current = this_Primary_1;
            			afterParserOrEnumRuleCall();
            		
            // InternalCTWedge.g:1110:3: ()
            // InternalCTWedge.g:1111:4: 
            {

            				current = forceCreateModelElementAndSet(
            					grammarAccess.getNotExpressionAccess().getNotExpressionPredicateAction_2(),
            					current);
            			

            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleNotExpression"


    // $ANTLR start "entryRuleAtomicPredicate"
    // InternalCTWedge.g:1121:1: entryRuleAtomicPredicate returns [EObject current=null] : iv_ruleAtomicPredicate= ruleAtomicPredicate EOF ;
    public final EObject entryRuleAtomicPredicate() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAtomicPredicate = null;


        try {
            // InternalCTWedge.g:1121:56: (iv_ruleAtomicPredicate= ruleAtomicPredicate EOF )
            // InternalCTWedge.g:1122:2: iv_ruleAtomicPredicate= ruleAtomicPredicate EOF
            {
             newCompositeNode(grammarAccess.getAtomicPredicateRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleAtomicPredicate=ruleAtomicPredicate();

            state._fsp--;

             current =iv_ruleAtomicPredicate; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleAtomicPredicate"


    // $ANTLR start "ruleAtomicPredicate"
    // InternalCTWedge.g:1128:1: ruleAtomicPredicate returns [EObject current=null] : ( ( (lv_boolConst_0_0= ruleBoolConst ) ) | ( (lv_name_1_0= ruleelementID ) ) ) ;
    public final EObject ruleAtomicPredicate() throws RecognitionException {
        EObject current = null;

        AntlrDatatypeRuleToken lv_boolConst_0_0 = null;

        AntlrDatatypeRuleToken lv_name_1_0 = null;



        	enterRule();

        try {
            // InternalCTWedge.g:1134:2: ( ( ( (lv_boolConst_0_0= ruleBoolConst ) ) | ( (lv_name_1_0= ruleelementID ) ) ) )
            // InternalCTWedge.g:1135:2: ( ( (lv_boolConst_0_0= ruleBoolConst ) ) | ( (lv_name_1_0= ruleelementID ) ) )
            {
            // InternalCTWedge.g:1135:2: ( ( (lv_boolConst_0_0= ruleBoolConst ) ) | ( (lv_name_1_0= ruleelementID ) ) )
            int alt17=2;
            int LA17_0 = input.LA(1);

            if ( ((LA17_0>=38 && LA17_0<=41)) ) {
                alt17=1;
            }
            else if ( ((LA17_0>=RULE_ID && LA17_0<=RULE_STRING)||LA17_0==42) ) {
                alt17=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 17, 0, input);

                throw nvae;
            }
            switch (alt17) {
                case 1 :
                    // InternalCTWedge.g:1136:3: ( (lv_boolConst_0_0= ruleBoolConst ) )
                    {
                    // InternalCTWedge.g:1136:3: ( (lv_boolConst_0_0= ruleBoolConst ) )
                    // InternalCTWedge.g:1137:4: (lv_boolConst_0_0= ruleBoolConst )
                    {
                    // InternalCTWedge.g:1137:4: (lv_boolConst_0_0= ruleBoolConst )
                    // InternalCTWedge.g:1138:5: lv_boolConst_0_0= ruleBoolConst
                    {

                    					newCompositeNode(grammarAccess.getAtomicPredicateAccess().getBoolConstBoolConstParserRuleCall_0_0());
                    				
                    pushFollow(FOLLOW_2);
                    lv_boolConst_0_0=ruleBoolConst();

                    state._fsp--;


                    					if (current==null) {
                    						current = createModelElementForParent(grammarAccess.getAtomicPredicateRule());
                    					}
                    					set(
                    						current,
                    						"boolConst",
                    						lv_boolConst_0_0,
                    						"ctwedge.CTWedge.BoolConst");
                    					afterParserOrEnumRuleCall();
                    				

                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalCTWedge.g:1156:3: ( (lv_name_1_0= ruleelementID ) )
                    {
                    // InternalCTWedge.g:1156:3: ( (lv_name_1_0= ruleelementID ) )
                    // InternalCTWedge.g:1157:4: (lv_name_1_0= ruleelementID )
                    {
                    // InternalCTWedge.g:1157:4: (lv_name_1_0= ruleelementID )
                    // InternalCTWedge.g:1158:5: lv_name_1_0= ruleelementID
                    {

                    					newCompositeNode(grammarAccess.getAtomicPredicateAccess().getNameElementIDParserRuleCall_1_0());
                    				
                    pushFollow(FOLLOW_2);
                    lv_name_1_0=ruleelementID();

                    state._fsp--;


                    					if (current==null) {
                    						current = createModelElementForParent(grammarAccess.getAtomicPredicateRule());
                    					}
                    					set(
                    						current,
                    						"name",
                    						lv_name_1_0,
                    						"ctwedge.CTWedge.elementID");
                    					afterParserOrEnumRuleCall();
                    				

                    }


                    }


                    }
                    break;

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleAtomicPredicate"


    // $ANTLR start "entryRuleOR_OPERATOR"
    // InternalCTWedge.g:1179:1: entryRuleOR_OPERATOR returns [String current=null] : iv_ruleOR_OPERATOR= ruleOR_OPERATOR EOF ;
    public final String entryRuleOR_OPERATOR() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleOR_OPERATOR = null;


        try {
            // InternalCTWedge.g:1179:51: (iv_ruleOR_OPERATOR= ruleOR_OPERATOR EOF )
            // InternalCTWedge.g:1180:2: iv_ruleOR_OPERATOR= ruleOR_OPERATOR EOF
            {
             newCompositeNode(grammarAccess.getOR_OPERATORRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleOR_OPERATOR=ruleOR_OPERATOR();

            state._fsp--;

             current =iv_ruleOR_OPERATOR.getText(); 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleOR_OPERATOR"


    // $ANTLR start "ruleOR_OPERATOR"
    // InternalCTWedge.g:1186:1: ruleOR_OPERATOR returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (kw= '||' | kw= 'or' | kw= 'OR' | kw= '|' ) ;
    public final AntlrDatatypeRuleToken ruleOR_OPERATOR() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalCTWedge.g:1192:2: ( (kw= '||' | kw= 'or' | kw= 'OR' | kw= '|' ) )
            // InternalCTWedge.g:1193:2: (kw= '||' | kw= 'or' | kw= 'OR' | kw= '|' )
            {
            // InternalCTWedge.g:1193:2: (kw= '||' | kw= 'or' | kw= 'OR' | kw= '|' )
            int alt18=4;
            switch ( input.LA(1) ) {
            case 28:
                {
                alt18=1;
                }
                break;
            case 29:
                {
                alt18=2;
                }
                break;
            case 30:
                {
                alt18=3;
                }
                break;
            case 31:
                {
                alt18=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 18, 0, input);

                throw nvae;
            }

            switch (alt18) {
                case 1 :
                    // InternalCTWedge.g:1194:3: kw= '||'
                    {
                    kw=(Token)match(input,28,FOLLOW_2); 

                    			current.merge(kw);
                    			newLeafNode(kw, grammarAccess.getOR_OPERATORAccess().getVerticalLineVerticalLineKeyword_0());
                    		

                    }
                    break;
                case 2 :
                    // InternalCTWedge.g:1200:3: kw= 'or'
                    {
                    kw=(Token)match(input,29,FOLLOW_2); 

                    			current.merge(kw);
                    			newLeafNode(kw, grammarAccess.getOR_OPERATORAccess().getOrKeyword_1());
                    		

                    }
                    break;
                case 3 :
                    // InternalCTWedge.g:1206:3: kw= 'OR'
                    {
                    kw=(Token)match(input,30,FOLLOW_2); 

                    			current.merge(kw);
                    			newLeafNode(kw, grammarAccess.getOR_OPERATORAccess().getORKeyword_2());
                    		

                    }
                    break;
                case 4 :
                    // InternalCTWedge.g:1212:3: kw= '|'
                    {
                    kw=(Token)match(input,31,FOLLOW_2); 

                    			current.merge(kw);
                    			newLeafNode(kw, grammarAccess.getOR_OPERATORAccess().getVerticalLineKeyword_3());
                    		

                    }
                    break;

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleOR_OPERATOR"


    // $ANTLR start "entryRuleAND_OPERATOR"
    // InternalCTWedge.g:1221:1: entryRuleAND_OPERATOR returns [String current=null] : iv_ruleAND_OPERATOR= ruleAND_OPERATOR EOF ;
    public final String entryRuleAND_OPERATOR() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleAND_OPERATOR = null;


        try {
            // InternalCTWedge.g:1221:52: (iv_ruleAND_OPERATOR= ruleAND_OPERATOR EOF )
            // InternalCTWedge.g:1222:2: iv_ruleAND_OPERATOR= ruleAND_OPERATOR EOF
            {
             newCompositeNode(grammarAccess.getAND_OPERATORRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleAND_OPERATOR=ruleAND_OPERATOR();

            state._fsp--;

             current =iv_ruleAND_OPERATOR.getText(); 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleAND_OPERATOR"


    // $ANTLR start "ruleAND_OPERATOR"
    // InternalCTWedge.g:1228:1: ruleAND_OPERATOR returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (kw= '&&' | kw= 'and' | kw= 'AND' | kw= '&' ) ;
    public final AntlrDatatypeRuleToken ruleAND_OPERATOR() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalCTWedge.g:1234:2: ( (kw= '&&' | kw= 'and' | kw= 'AND' | kw= '&' ) )
            // InternalCTWedge.g:1235:2: (kw= '&&' | kw= 'and' | kw= 'AND' | kw= '&' )
            {
            // InternalCTWedge.g:1235:2: (kw= '&&' | kw= 'and' | kw= 'AND' | kw= '&' )
            int alt19=4;
            switch ( input.LA(1) ) {
            case 32:
                {
                alt19=1;
                }
                break;
            case 33:
                {
                alt19=2;
                }
                break;
            case 34:
                {
                alt19=3;
                }
                break;
            case 35:
                {
                alt19=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 19, 0, input);

                throw nvae;
            }

            switch (alt19) {
                case 1 :
                    // InternalCTWedge.g:1236:3: kw= '&&'
                    {
                    kw=(Token)match(input,32,FOLLOW_2); 

                    			current.merge(kw);
                    			newLeafNode(kw, grammarAccess.getAND_OPERATORAccess().getAmpersandAmpersandKeyword_0());
                    		

                    }
                    break;
                case 2 :
                    // InternalCTWedge.g:1242:3: kw= 'and'
                    {
                    kw=(Token)match(input,33,FOLLOW_2); 

                    			current.merge(kw);
                    			newLeafNode(kw, grammarAccess.getAND_OPERATORAccess().getAndKeyword_1());
                    		

                    }
                    break;
                case 3 :
                    // InternalCTWedge.g:1248:3: kw= 'AND'
                    {
                    kw=(Token)match(input,34,FOLLOW_2); 

                    			current.merge(kw);
                    			newLeafNode(kw, grammarAccess.getAND_OPERATORAccess().getANDKeyword_2());
                    		

                    }
                    break;
                case 4 :
                    // InternalCTWedge.g:1254:3: kw= '&'
                    {
                    kw=(Token)match(input,35,FOLLOW_2); 

                    			current.merge(kw);
                    			newLeafNode(kw, grammarAccess.getAND_OPERATORAccess().getAmpersandKeyword_3());
                    		

                    }
                    break;

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleAND_OPERATOR"


    // $ANTLR start "entryRuleNOT_OPERATOR"
    // InternalCTWedge.g:1263:1: entryRuleNOT_OPERATOR returns [String current=null] : iv_ruleNOT_OPERATOR= ruleNOT_OPERATOR EOF ;
    public final String entryRuleNOT_OPERATOR() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleNOT_OPERATOR = null;


        try {
            // InternalCTWedge.g:1263:52: (iv_ruleNOT_OPERATOR= ruleNOT_OPERATOR EOF )
            // InternalCTWedge.g:1264:2: iv_ruleNOT_OPERATOR= ruleNOT_OPERATOR EOF
            {
             newCompositeNode(grammarAccess.getNOT_OPERATORRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleNOT_OPERATOR=ruleNOT_OPERATOR();

            state._fsp--;

             current =iv_ruleNOT_OPERATOR.getText(); 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleNOT_OPERATOR"


    // $ANTLR start "ruleNOT_OPERATOR"
    // InternalCTWedge.g:1270:1: ruleNOT_OPERATOR returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (kw= '!' | kw= 'not' ) ;
    public final AntlrDatatypeRuleToken ruleNOT_OPERATOR() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalCTWedge.g:1276:2: ( (kw= '!' | kw= 'not' ) )
            // InternalCTWedge.g:1277:2: (kw= '!' | kw= 'not' )
            {
            // InternalCTWedge.g:1277:2: (kw= '!' | kw= 'not' )
            int alt20=2;
            int LA20_0 = input.LA(1);

            if ( (LA20_0==36) ) {
                alt20=1;
            }
            else if ( (LA20_0==37) ) {
                alt20=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 20, 0, input);

                throw nvae;
            }
            switch (alt20) {
                case 1 :
                    // InternalCTWedge.g:1278:3: kw= '!'
                    {
                    kw=(Token)match(input,36,FOLLOW_2); 

                    			current.merge(kw);
                    			newLeafNode(kw, grammarAccess.getNOT_OPERATORAccess().getExclamationMarkKeyword_0());
                    		

                    }
                    break;
                case 2 :
                    // InternalCTWedge.g:1284:3: kw= 'not'
                    {
                    kw=(Token)match(input,37,FOLLOW_2); 

                    			current.merge(kw);
                    			newLeafNode(kw, grammarAccess.getNOT_OPERATORAccess().getNotKeyword_1());
                    		

                    }
                    break;

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleNOT_OPERATOR"


    // $ANTLR start "entryRuleBoolConst"
    // InternalCTWedge.g:1293:1: entryRuleBoolConst returns [String current=null] : iv_ruleBoolConst= ruleBoolConst EOF ;
    public final String entryRuleBoolConst() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleBoolConst = null;


        try {
            // InternalCTWedge.g:1293:49: (iv_ruleBoolConst= ruleBoolConst EOF )
            // InternalCTWedge.g:1294:2: iv_ruleBoolConst= ruleBoolConst EOF
            {
             newCompositeNode(grammarAccess.getBoolConstRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleBoolConst=ruleBoolConst();

            state._fsp--;

             current =iv_ruleBoolConst.getText(); 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleBoolConst"


    // $ANTLR start "ruleBoolConst"
    // InternalCTWedge.g:1300:1: ruleBoolConst returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (kw= 'false' | kw= 'true' | kw= 'FALSE' | kw= 'TRUE' ) ;
    public final AntlrDatatypeRuleToken ruleBoolConst() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;


        	enterRule();

        try {
            // InternalCTWedge.g:1306:2: ( (kw= 'false' | kw= 'true' | kw= 'FALSE' | kw= 'TRUE' ) )
            // InternalCTWedge.g:1307:2: (kw= 'false' | kw= 'true' | kw= 'FALSE' | kw= 'TRUE' )
            {
            // InternalCTWedge.g:1307:2: (kw= 'false' | kw= 'true' | kw= 'FALSE' | kw= 'TRUE' )
            int alt21=4;
            switch ( input.LA(1) ) {
            case 38:
                {
                alt21=1;
                }
                break;
            case 39:
                {
                alt21=2;
                }
                break;
            case 40:
                {
                alt21=3;
                }
                break;
            case 41:
                {
                alt21=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 21, 0, input);

                throw nvae;
            }

            switch (alt21) {
                case 1 :
                    // InternalCTWedge.g:1308:3: kw= 'false'
                    {
                    kw=(Token)match(input,38,FOLLOW_2); 

                    			current.merge(kw);
                    			newLeafNode(kw, grammarAccess.getBoolConstAccess().getFalseKeyword_0());
                    		

                    }
                    break;
                case 2 :
                    // InternalCTWedge.g:1314:3: kw= 'true'
                    {
                    kw=(Token)match(input,39,FOLLOW_2); 

                    			current.merge(kw);
                    			newLeafNode(kw, grammarAccess.getBoolConstAccess().getTrueKeyword_1());
                    		

                    }
                    break;
                case 3 :
                    // InternalCTWedge.g:1320:3: kw= 'FALSE'
                    {
                    kw=(Token)match(input,40,FOLLOW_2); 

                    			current.merge(kw);
                    			newLeafNode(kw, grammarAccess.getBoolConstAccess().getFALSEKeyword_2());
                    		

                    }
                    break;
                case 4 :
                    // InternalCTWedge.g:1326:3: kw= 'TRUE'
                    {
                    kw=(Token)match(input,41,FOLLOW_2); 

                    			current.merge(kw);
                    			newLeafNode(kw, grammarAccess.getBoolConstAccess().getTRUEKeyword_3());
                    		

                    }
                    break;

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleBoolConst"


    // $ANTLR start "entryRuleelementID"
    // InternalCTWedge.g:1335:1: entryRuleelementID returns [String current=null] : iv_ruleelementID= ruleelementID EOF ;
    public final String entryRuleelementID() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleelementID = null;


        try {
            // InternalCTWedge.g:1335:49: (iv_ruleelementID= ruleelementID EOF )
            // InternalCTWedge.g:1336:2: iv_ruleelementID= ruleelementID EOF
            {
             newCompositeNode(grammarAccess.getElementIDRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleelementID=ruleelementID();

            state._fsp--;

             current =iv_ruleelementID.getText(); 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleelementID"


    // $ANTLR start "ruleelementID"
    // InternalCTWedge.g:1342:1: ruleelementID returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (this_ID_0= RULE_ID | this_NUMID_1= RULE_NUMID | this_STRING_2= RULE_STRING | ( (kw= '-' )? this_INT_4= RULE_INT ) ) ;
    public final AntlrDatatypeRuleToken ruleelementID() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token this_ID_0=null;
        Token this_NUMID_1=null;
        Token this_STRING_2=null;
        Token kw=null;
        Token this_INT_4=null;


        	enterRule();

        try {
            // InternalCTWedge.g:1348:2: ( (this_ID_0= RULE_ID | this_NUMID_1= RULE_NUMID | this_STRING_2= RULE_STRING | ( (kw= '-' )? this_INT_4= RULE_INT ) ) )
            // InternalCTWedge.g:1349:2: (this_ID_0= RULE_ID | this_NUMID_1= RULE_NUMID | this_STRING_2= RULE_STRING | ( (kw= '-' )? this_INT_4= RULE_INT ) )
            {
            // InternalCTWedge.g:1349:2: (this_ID_0= RULE_ID | this_NUMID_1= RULE_NUMID | this_STRING_2= RULE_STRING | ( (kw= '-' )? this_INT_4= RULE_INT ) )
            int alt23=4;
            switch ( input.LA(1) ) {
            case RULE_ID:
                {
                alt23=1;
                }
                break;
            case RULE_NUMID:
                {
                alt23=2;
                }
                break;
            case RULE_STRING:
                {
                alt23=3;
                }
                break;
            case RULE_INT:
            case 42:
                {
                alt23=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 23, 0, input);

                throw nvae;
            }

            switch (alt23) {
                case 1 :
                    // InternalCTWedge.g:1350:3: this_ID_0= RULE_ID
                    {
                    this_ID_0=(Token)match(input,RULE_ID,FOLLOW_2); 

                    			current.merge(this_ID_0);
                    		

                    			newLeafNode(this_ID_0, grammarAccess.getElementIDAccess().getIDTerminalRuleCall_0());
                    		

                    }
                    break;
                case 2 :
                    // InternalCTWedge.g:1358:3: this_NUMID_1= RULE_NUMID
                    {
                    this_NUMID_1=(Token)match(input,RULE_NUMID,FOLLOW_2); 

                    			current.merge(this_NUMID_1);
                    		

                    			newLeafNode(this_NUMID_1, grammarAccess.getElementIDAccess().getNUMIDTerminalRuleCall_1());
                    		

                    }
                    break;
                case 3 :
                    // InternalCTWedge.g:1366:3: this_STRING_2= RULE_STRING
                    {
                    this_STRING_2=(Token)match(input,RULE_STRING,FOLLOW_2); 

                    			current.merge(this_STRING_2);
                    		

                    			newLeafNode(this_STRING_2, grammarAccess.getElementIDAccess().getSTRINGTerminalRuleCall_2());
                    		

                    }
                    break;
                case 4 :
                    // InternalCTWedge.g:1374:3: ( (kw= '-' )? this_INT_4= RULE_INT )
                    {
                    // InternalCTWedge.g:1374:3: ( (kw= '-' )? this_INT_4= RULE_INT )
                    // InternalCTWedge.g:1375:4: (kw= '-' )? this_INT_4= RULE_INT
                    {
                    // InternalCTWedge.g:1375:4: (kw= '-' )?
                    int alt22=2;
                    int LA22_0 = input.LA(1);

                    if ( (LA22_0==42) ) {
                        alt22=1;
                    }
                    switch (alt22) {
                        case 1 :
                            // InternalCTWedge.g:1376:5: kw= '-'
                            {
                            kw=(Token)match(input,42,FOLLOW_20); 

                            					current.merge(kw);
                            					newLeafNode(kw, grammarAccess.getElementIDAccess().getHyphenMinusKeyword_3_0());
                            				

                            }
                            break;

                    }

                    this_INT_4=(Token)match(input,RULE_INT,FOLLOW_2); 

                    				current.merge(this_INT_4);
                    			

                    				newLeafNode(this_INT_4, grammarAccess.getElementIDAccess().getINTTerminalRuleCall_3_1());
                    			

                    }


                    }
                    break;

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleelementID"


    // $ANTLR start "entryRulePossiblySignedNumber"
    // InternalCTWedge.g:1394:1: entryRulePossiblySignedNumber returns [String current=null] : iv_rulePossiblySignedNumber= rulePossiblySignedNumber EOF ;
    public final String entryRulePossiblySignedNumber() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_rulePossiblySignedNumber = null;


        try {
            // InternalCTWedge.g:1394:60: (iv_rulePossiblySignedNumber= rulePossiblySignedNumber EOF )
            // InternalCTWedge.g:1395:2: iv_rulePossiblySignedNumber= rulePossiblySignedNumber EOF
            {
             newCompositeNode(grammarAccess.getPossiblySignedNumberRule()); 
            pushFollow(FOLLOW_1);
            iv_rulePossiblySignedNumber=rulePossiblySignedNumber();

            state._fsp--;

             current =iv_rulePossiblySignedNumber.getText(); 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRulePossiblySignedNumber"


    // $ANTLR start "rulePossiblySignedNumber"
    // InternalCTWedge.g:1401:1: rulePossiblySignedNumber returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : ( (kw= '-' )? this_INT_1= RULE_INT ) ;
    public final AntlrDatatypeRuleToken rulePossiblySignedNumber() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token kw=null;
        Token this_INT_1=null;


        	enterRule();

        try {
            // InternalCTWedge.g:1407:2: ( ( (kw= '-' )? this_INT_1= RULE_INT ) )
            // InternalCTWedge.g:1408:2: ( (kw= '-' )? this_INT_1= RULE_INT )
            {
            // InternalCTWedge.g:1408:2: ( (kw= '-' )? this_INT_1= RULE_INT )
            // InternalCTWedge.g:1409:3: (kw= '-' )? this_INT_1= RULE_INT
            {
            // InternalCTWedge.g:1409:3: (kw= '-' )?
            int alt24=2;
            int LA24_0 = input.LA(1);

            if ( (LA24_0==42) ) {
                alt24=1;
            }
            switch (alt24) {
                case 1 :
                    // InternalCTWedge.g:1410:4: kw= '-'
                    {
                    kw=(Token)match(input,42,FOLLOW_20); 

                    				current.merge(kw);
                    				newLeafNode(kw, grammarAccess.getPossiblySignedNumberAccess().getHyphenMinusKeyword_0());
                    			

                    }
                    break;

            }

            this_INT_1=(Token)match(input,RULE_INT,FOLLOW_2); 

            			current.merge(this_INT_1);
            		

            			newLeafNode(this_INT_1, grammarAccess.getPossiblySignedNumberAccess().getINTTerminalRuleCall_1());
            		

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "rulePossiblySignedNumber"


    // $ANTLR start "ruleRelationalOperators"
    // InternalCTWedge.g:1427:1: ruleRelationalOperators returns [Enumerator current=null] : ( (enumLiteral_0= '>' ) | (enumLiteral_1= '<' ) | (enumLiteral_2= '>=' ) | (enumLiteral_3= '<=' ) ) ;
    public final Enumerator ruleRelationalOperators() throws RecognitionException {
        Enumerator current = null;

        Token enumLiteral_0=null;
        Token enumLiteral_1=null;
        Token enumLiteral_2=null;
        Token enumLiteral_3=null;


        	enterRule();

        try {
            // InternalCTWedge.g:1433:2: ( ( (enumLiteral_0= '>' ) | (enumLiteral_1= '<' ) | (enumLiteral_2= '>=' ) | (enumLiteral_3= '<=' ) ) )
            // InternalCTWedge.g:1434:2: ( (enumLiteral_0= '>' ) | (enumLiteral_1= '<' ) | (enumLiteral_2= '>=' ) | (enumLiteral_3= '<=' ) )
            {
            // InternalCTWedge.g:1434:2: ( (enumLiteral_0= '>' ) | (enumLiteral_1= '<' ) | (enumLiteral_2= '>=' ) | (enumLiteral_3= '<=' ) )
            int alt25=4;
            switch ( input.LA(1) ) {
            case 43:
                {
                alt25=1;
                }
                break;
            case 44:
                {
                alt25=2;
                }
                break;
            case 45:
                {
                alt25=3;
                }
                break;
            case 46:
                {
                alt25=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 25, 0, input);

                throw nvae;
            }

            switch (alt25) {
                case 1 :
                    // InternalCTWedge.g:1435:3: (enumLiteral_0= '>' )
                    {
                    // InternalCTWedge.g:1435:3: (enumLiteral_0= '>' )
                    // InternalCTWedge.g:1436:4: enumLiteral_0= '>'
                    {
                    enumLiteral_0=(Token)match(input,43,FOLLOW_2); 

                    				current = grammarAccess.getRelationalOperatorsAccess().getGTEnumLiteralDeclaration_0().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_0, grammarAccess.getRelationalOperatorsAccess().getGTEnumLiteralDeclaration_0());
                    			

                    }


                    }
                    break;
                case 2 :
                    // InternalCTWedge.g:1443:3: (enumLiteral_1= '<' )
                    {
                    // InternalCTWedge.g:1443:3: (enumLiteral_1= '<' )
                    // InternalCTWedge.g:1444:4: enumLiteral_1= '<'
                    {
                    enumLiteral_1=(Token)match(input,44,FOLLOW_2); 

                    				current = grammarAccess.getRelationalOperatorsAccess().getLTEnumLiteralDeclaration_1().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_1, grammarAccess.getRelationalOperatorsAccess().getLTEnumLiteralDeclaration_1());
                    			

                    }


                    }
                    break;
                case 3 :
                    // InternalCTWedge.g:1451:3: (enumLiteral_2= '>=' )
                    {
                    // InternalCTWedge.g:1451:3: (enumLiteral_2= '>=' )
                    // InternalCTWedge.g:1452:4: enumLiteral_2= '>='
                    {
                    enumLiteral_2=(Token)match(input,45,FOLLOW_2); 

                    				current = grammarAccess.getRelationalOperatorsAccess().getGEEnumLiteralDeclaration_2().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_2, grammarAccess.getRelationalOperatorsAccess().getGEEnumLiteralDeclaration_2());
                    			

                    }


                    }
                    break;
                case 4 :
                    // InternalCTWedge.g:1459:3: (enumLiteral_3= '<=' )
                    {
                    // InternalCTWedge.g:1459:3: (enumLiteral_3= '<=' )
                    // InternalCTWedge.g:1460:4: enumLiteral_3= '<='
                    {
                    enumLiteral_3=(Token)match(input,46,FOLLOW_2); 

                    				current = grammarAccess.getRelationalOperatorsAccess().getLEEnumLiteralDeclaration_3().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_3, grammarAccess.getRelationalOperatorsAccess().getLEEnumLiteralDeclaration_3());
                    			

                    }


                    }
                    break;

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleRelationalOperators"


    // $ANTLR start "ruleEqualityOperators"
    // InternalCTWedge.g:1470:1: ruleEqualityOperators returns [Enumerator current=null] : ( (enumLiteral_0= '==' ) | (enumLiteral_1= '!=' ) | (enumLiteral_2= '=' ) ) ;
    public final Enumerator ruleEqualityOperators() throws RecognitionException {
        Enumerator current = null;

        Token enumLiteral_0=null;
        Token enumLiteral_1=null;
        Token enumLiteral_2=null;


        	enterRule();

        try {
            // InternalCTWedge.g:1476:2: ( ( (enumLiteral_0= '==' ) | (enumLiteral_1= '!=' ) | (enumLiteral_2= '=' ) ) )
            // InternalCTWedge.g:1477:2: ( (enumLiteral_0= '==' ) | (enumLiteral_1= '!=' ) | (enumLiteral_2= '=' ) )
            {
            // InternalCTWedge.g:1477:2: ( (enumLiteral_0= '==' ) | (enumLiteral_1= '!=' ) | (enumLiteral_2= '=' ) )
            int alt26=3;
            switch ( input.LA(1) ) {
            case 47:
                {
                alt26=1;
                }
                break;
            case 48:
                {
                alt26=2;
                }
                break;
            case 49:
                {
                alt26=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 26, 0, input);

                throw nvae;
            }

            switch (alt26) {
                case 1 :
                    // InternalCTWedge.g:1478:3: (enumLiteral_0= '==' )
                    {
                    // InternalCTWedge.g:1478:3: (enumLiteral_0= '==' )
                    // InternalCTWedge.g:1479:4: enumLiteral_0= '=='
                    {
                    enumLiteral_0=(Token)match(input,47,FOLLOW_2); 

                    				current = grammarAccess.getEqualityOperatorsAccess().getEQEnumLiteralDeclaration_0().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_0, grammarAccess.getEqualityOperatorsAccess().getEQEnumLiteralDeclaration_0());
                    			

                    }


                    }
                    break;
                case 2 :
                    // InternalCTWedge.g:1486:3: (enumLiteral_1= '!=' )
                    {
                    // InternalCTWedge.g:1486:3: (enumLiteral_1= '!=' )
                    // InternalCTWedge.g:1487:4: enumLiteral_1= '!='
                    {
                    enumLiteral_1=(Token)match(input,48,FOLLOW_2); 

                    				current = grammarAccess.getEqualityOperatorsAccess().getNEEnumLiteralDeclaration_1().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_1, grammarAccess.getEqualityOperatorsAccess().getNEEnumLiteralDeclaration_1());
                    			

                    }


                    }
                    break;
                case 3 :
                    // InternalCTWedge.g:1494:3: (enumLiteral_2= '=' )
                    {
                    // InternalCTWedge.g:1494:3: (enumLiteral_2= '=' )
                    // InternalCTWedge.g:1495:4: enumLiteral_2= '='
                    {
                    enumLiteral_2=(Token)match(input,49,FOLLOW_2); 

                    				current = grammarAccess.getEqualityOperatorsAccess().getEQEnumLiteralDeclaration_2().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_2, grammarAccess.getEqualityOperatorsAccess().getEQEnumLiteralDeclaration_2());
                    			

                    }


                    }
                    break;

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleEqualityOperators"


    // $ANTLR start "rulePlusMinusOperators"
    // InternalCTWedge.g:1505:1: rulePlusMinusOperators returns [Enumerator current=null] : ( (enumLiteral_0= '+' ) | (enumLiteral_1= '-' ) ) ;
    public final Enumerator rulePlusMinusOperators() throws RecognitionException {
        Enumerator current = null;

        Token enumLiteral_0=null;
        Token enumLiteral_1=null;


        	enterRule();

        try {
            // InternalCTWedge.g:1511:2: ( ( (enumLiteral_0= '+' ) | (enumLiteral_1= '-' ) ) )
            // InternalCTWedge.g:1512:2: ( (enumLiteral_0= '+' ) | (enumLiteral_1= '-' ) )
            {
            // InternalCTWedge.g:1512:2: ( (enumLiteral_0= '+' ) | (enumLiteral_1= '-' ) )
            int alt27=2;
            int LA27_0 = input.LA(1);

            if ( (LA27_0==50) ) {
                alt27=1;
            }
            else if ( (LA27_0==42) ) {
                alt27=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 27, 0, input);

                throw nvae;
            }
            switch (alt27) {
                case 1 :
                    // InternalCTWedge.g:1513:3: (enumLiteral_0= '+' )
                    {
                    // InternalCTWedge.g:1513:3: (enumLiteral_0= '+' )
                    // InternalCTWedge.g:1514:4: enumLiteral_0= '+'
                    {
                    enumLiteral_0=(Token)match(input,50,FOLLOW_2); 

                    				current = grammarAccess.getPlusMinusOperatorsAccess().getPlusEnumLiteralDeclaration_0().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_0, grammarAccess.getPlusMinusOperatorsAccess().getPlusEnumLiteralDeclaration_0());
                    			

                    }


                    }
                    break;
                case 2 :
                    // InternalCTWedge.g:1521:3: (enumLiteral_1= '-' )
                    {
                    // InternalCTWedge.g:1521:3: (enumLiteral_1= '-' )
                    // InternalCTWedge.g:1522:4: enumLiteral_1= '-'
                    {
                    enumLiteral_1=(Token)match(input,42,FOLLOW_2); 

                    				current = grammarAccess.getPlusMinusOperatorsAccess().getMinusEnumLiteralDeclaration_1().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_1, grammarAccess.getPlusMinusOperatorsAccess().getMinusEnumLiteralDeclaration_1());
                    			

                    }


                    }
                    break;

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "rulePlusMinusOperators"


    // $ANTLR start "ruleModMultDivOperators"
    // InternalCTWedge.g:1532:1: ruleModMultDivOperators returns [Enumerator current=null] : ( (enumLiteral_0= '%' ) | (enumLiteral_1= '*' ) | (enumLiteral_2= '/' ) ) ;
    public final Enumerator ruleModMultDivOperators() throws RecognitionException {
        Enumerator current = null;

        Token enumLiteral_0=null;
        Token enumLiteral_1=null;
        Token enumLiteral_2=null;


        	enterRule();

        try {
            // InternalCTWedge.g:1538:2: ( ( (enumLiteral_0= '%' ) | (enumLiteral_1= '*' ) | (enumLiteral_2= '/' ) ) )
            // InternalCTWedge.g:1539:2: ( (enumLiteral_0= '%' ) | (enumLiteral_1= '*' ) | (enumLiteral_2= '/' ) )
            {
            // InternalCTWedge.g:1539:2: ( (enumLiteral_0= '%' ) | (enumLiteral_1= '*' ) | (enumLiteral_2= '/' ) )
            int alt28=3;
            switch ( input.LA(1) ) {
            case 51:
                {
                alt28=1;
                }
                break;
            case 52:
                {
                alt28=2;
                }
                break;
            case 53:
                {
                alt28=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 28, 0, input);

                throw nvae;
            }

            switch (alt28) {
                case 1 :
                    // InternalCTWedge.g:1540:3: (enumLiteral_0= '%' )
                    {
                    // InternalCTWedge.g:1540:3: (enumLiteral_0= '%' )
                    // InternalCTWedge.g:1541:4: enumLiteral_0= '%'
                    {
                    enumLiteral_0=(Token)match(input,51,FOLLOW_2); 

                    				current = grammarAccess.getModMultDivOperatorsAccess().getModEnumLiteralDeclaration_0().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_0, grammarAccess.getModMultDivOperatorsAccess().getModEnumLiteralDeclaration_0());
                    			

                    }


                    }
                    break;
                case 2 :
                    // InternalCTWedge.g:1548:3: (enumLiteral_1= '*' )
                    {
                    // InternalCTWedge.g:1548:3: (enumLiteral_1= '*' )
                    // InternalCTWedge.g:1549:4: enumLiteral_1= '*'
                    {
                    enumLiteral_1=(Token)match(input,52,FOLLOW_2); 

                    				current = grammarAccess.getModMultDivOperatorsAccess().getMultEnumLiteralDeclaration_1().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_1, grammarAccess.getModMultDivOperatorsAccess().getMultEnumLiteralDeclaration_1());
                    			

                    }


                    }
                    break;
                case 3 :
                    // InternalCTWedge.g:1556:3: (enumLiteral_2= '/' )
                    {
                    // InternalCTWedge.g:1556:3: (enumLiteral_2= '/' )
                    // InternalCTWedge.g:1557:4: enumLiteral_2= '/'
                    {
                    enumLiteral_2=(Token)match(input,53,FOLLOW_2); 

                    				current = grammarAccess.getModMultDivOperatorsAccess().getDivEnumLiteralDeclaration_2().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_2, grammarAccess.getModMultDivOperatorsAccess().getDivEnumLiteralDeclaration_2());
                    			

                    }


                    }
                    break;

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleModMultDivOperators"


    // $ANTLR start "ruleImpliesOperator"
    // InternalCTWedge.g:1567:1: ruleImpliesOperator returns [Enumerator current=null] : ( (enumLiteral_0= '=>' ) | (enumLiteral_1= '<=>' ) | (enumLiteral_2= '->' ) | (enumLiteral_3= '<->' ) ) ;
    public final Enumerator ruleImpliesOperator() throws RecognitionException {
        Enumerator current = null;

        Token enumLiteral_0=null;
        Token enumLiteral_1=null;
        Token enumLiteral_2=null;
        Token enumLiteral_3=null;


        	enterRule();

        try {
            // InternalCTWedge.g:1573:2: ( ( (enumLiteral_0= '=>' ) | (enumLiteral_1= '<=>' ) | (enumLiteral_2= '->' ) | (enumLiteral_3= '<->' ) ) )
            // InternalCTWedge.g:1574:2: ( (enumLiteral_0= '=>' ) | (enumLiteral_1= '<=>' ) | (enumLiteral_2= '->' ) | (enumLiteral_3= '<->' ) )
            {
            // InternalCTWedge.g:1574:2: ( (enumLiteral_0= '=>' ) | (enumLiteral_1= '<=>' ) | (enumLiteral_2= '->' ) | (enumLiteral_3= '<->' ) )
            int alt29=4;
            switch ( input.LA(1) ) {
            case 54:
                {
                alt29=1;
                }
                break;
            case 55:
                {
                alt29=2;
                }
                break;
            case 56:
                {
                alt29=3;
                }
                break;
            case 57:
                {
                alt29=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 29, 0, input);

                throw nvae;
            }

            switch (alt29) {
                case 1 :
                    // InternalCTWedge.g:1575:3: (enumLiteral_0= '=>' )
                    {
                    // InternalCTWedge.g:1575:3: (enumLiteral_0= '=>' )
                    // InternalCTWedge.g:1576:4: enumLiteral_0= '=>'
                    {
                    enumLiteral_0=(Token)match(input,54,FOLLOW_2); 

                    				current = grammarAccess.getImpliesOperatorAccess().getImplEnumLiteralDeclaration_0().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_0, grammarAccess.getImpliesOperatorAccess().getImplEnumLiteralDeclaration_0());
                    			

                    }


                    }
                    break;
                case 2 :
                    // InternalCTWedge.g:1583:3: (enumLiteral_1= '<=>' )
                    {
                    // InternalCTWedge.g:1583:3: (enumLiteral_1= '<=>' )
                    // InternalCTWedge.g:1584:4: enumLiteral_1= '<=>'
                    {
                    enumLiteral_1=(Token)match(input,55,FOLLOW_2); 

                    				current = grammarAccess.getImpliesOperatorAccess().getIffEnumLiteralDeclaration_1().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_1, grammarAccess.getImpliesOperatorAccess().getIffEnumLiteralDeclaration_1());
                    			

                    }


                    }
                    break;
                case 3 :
                    // InternalCTWedge.g:1591:3: (enumLiteral_2= '->' )
                    {
                    // InternalCTWedge.g:1591:3: (enumLiteral_2= '->' )
                    // InternalCTWedge.g:1592:4: enumLiteral_2= '->'
                    {
                    enumLiteral_2=(Token)match(input,56,FOLLOW_2); 

                    				current = grammarAccess.getImpliesOperatorAccess().getImplEnumLiteralDeclaration_2().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_2, grammarAccess.getImpliesOperatorAccess().getImplEnumLiteralDeclaration_2());
                    			

                    }


                    }
                    break;
                case 4 :
                    // InternalCTWedge.g:1599:3: (enumLiteral_3= '<->' )
                    {
                    // InternalCTWedge.g:1599:3: (enumLiteral_3= '<->' )
                    // InternalCTWedge.g:1600:4: enumLiteral_3= '<->'
                    {
                    enumLiteral_3=(Token)match(input,57,FOLLOW_2); 

                    				current = grammarAccess.getImpliesOperatorAccess().getIffEnumLiteralDeclaration_3().getEnumLiteral().getInstance();
                    				newLeafNode(enumLiteral_3, grammarAccess.getImpliesOperatorAccess().getIffEnumLiteralDeclaration_3());
                    			

                    }


                    }
                    break;

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleImpliesOperator"

    // Delegated rules


 

    public static final BitSet FOLLOW_1 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_2 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_3 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_4 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_5 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_6 = new BitSet(new long[]{0x0000000000008012L});
    public static final BitSet FOLLOW_7 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_8 = new BitSet(new long[]{0x0000000002000002L});
    public static final BitSet FOLLOW_9 = new BitSet(new long[]{0x0000000000010002L});
    public static final BitSet FOLLOW_10 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_11 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_12 = new BitSet(new long[]{0x00000400000000F0L});
    public static final BitSet FOLLOW_13 = new BitSet(new long[]{0x00000400000800F0L});
    public static final BitSet FOLLOW_14 = new BitSet(new long[]{0x0000000000100002L});
    public static final BitSet FOLLOW_15 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_16 = new BitSet(new long[]{0x0000040000000020L});
    public static final BitSet FOLLOW_17 = new BitSet(new long[]{0x0000000000400000L});
    public static final BitSet FOLLOW_18 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_19 = new BitSet(new long[]{0x0000000001000002L});
    public static final BitSet FOLLOW_20 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_21 = new BitSet(new long[]{0x000007F0040000F0L});
    public static final BitSet FOLLOW_22 = new BitSet(new long[]{0x03C0000000000002L});
    public static final BitSet FOLLOW_23 = new BitSet(new long[]{0x00000000F0000002L});
    public static final BitSet FOLLOW_24 = new BitSet(new long[]{0x0000000F00000002L});
    public static final BitSet FOLLOW_25 = new BitSet(new long[]{0x0003800000000002L});
    public static final BitSet FOLLOW_26 = new BitSet(new long[]{0x0000780000000002L});
    public static final BitSet FOLLOW_27 = new BitSet(new long[]{0x0004040000000002L});
    public static final BitSet FOLLOW_28 = new BitSet(new long[]{0x0038000000000002L});
    public static final BitSet FOLLOW_29 = new BitSet(new long[]{0x0000000008000000L});

}