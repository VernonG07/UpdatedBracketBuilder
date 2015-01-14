package com.example.mgriffin.public_references;

import android.graphics.Typeface;

/**
 * Created by mgriffin on 10/27/2014.
 */
public class PublicVars {
    //Main App SharedPreferences Key
    public static final String SP_GAME_MATCH_UP = "SP_GAME_MATCH_UP";
    public static final String SP_DEFAULT_RANDOM = "DEFAULT_RANDO";
    public static final String SP_CHECKBOX_RANDOM = "checkbox_randomize_opponents";
    public static final String SP_CHECKBOX_RANDOM_DEFAULT = "checkbox_randomize_opponents_default";

    public static enum ELIM_MODE {
        SINGLE_ELIM,
        DOUBLE_ELIM
    }

    //Transition Names
    public static final String TRANSITION_TOOLBAR = "toolbar";

    //Dialog Fragments
    public static final String DIALOG_TITLE_MODIFY_GAME = "Modify Game";
    public static final String DIALOG_TITLE_MODIFY_MATCHUP = "Modify Match Up";
    public static final String DIALOG_POS_DELETE = "Delete";
    public static final String DIALOG_NEG_EDIT = "Edit";

    public static final String STRING_EMPTY = "";

    //Intent Extras
    public static final String INTENT_EXTRA_GAME_ID = "GAME_ID";
    public static final String FRAGMENT_EXTRA_ROUND_NUMBER = "ROUND_NUMBER";

    //Result Codes
    public static final int RESULT_CODE_STARTING_ACTIVITY = 007;

    //Font File Names
    public static final String FONT_LION_KING = "lking.ttf";

    //Errors
    public static final String ERROR_NO_DATASOURCE = "Sorry, could not open datasource";

    //Tags
    public static final String FRAG_TAG_ROUND = "round_frag";
    public static final String FRAG_TAG_SETTINGS = "sett_frag";

    //Default Team Name
    public static final String TEAM_BYE = "BYE";

    //Toast Messages
    public static final String TOAST_SELECT_WINNER = "Please select a winner for all match ups";
    public static final String TOAST_ADD_MATCHUPS = "Please add match ups";

    //Button Text
    public static final String BUTTON_UPDATE = "Update";


}
