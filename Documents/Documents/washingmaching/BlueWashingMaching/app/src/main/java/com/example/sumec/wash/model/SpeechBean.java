package com.example.sumec.wash.model;

import java.util.ArrayList;

/**
 * Created by zhdk on 2019/1/16.
 */

public class SpeechBean {
    public ArrayList<WSBean> ws;

    public class WSBean {
        public ArrayList<CWBean> cw;
    }

    public class CWBean {
        public String w;
    }

}
