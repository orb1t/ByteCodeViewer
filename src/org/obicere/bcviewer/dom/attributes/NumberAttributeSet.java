package org.obicere.bcviewer.dom.attributes;

import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.Color;

/**
 */
public class NumberAttributeSet extends SimpleAttributeSet {

    public NumberAttributeSet(){
        addAttribute(StyleConstants.Foreground,new Color(86, 151, 250));
    }

}