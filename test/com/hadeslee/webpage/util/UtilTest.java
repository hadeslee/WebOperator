/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hadeslee.webpage.util;

import java.util.Properties;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Hadeslee
 */
public class UtilTest {

    public UtilTest() {
    }

    /**
     * Test of parseContent method, of class Util.
     */
    @Test
    public void testParseContent() {
     
    }

    /**
     * Test of replaceListTemplateTitle method, of class Util.
     */
    @Test
    public void testReplaceListTemplateTitle() {

    }

    /**
     * Test of replaceLinkOfListTemplate method, of class Util.
     */
    @Test
    public void testReplaceLinkOfListTemplate() {
        System.out.println("replaceLinkOfListTemplate");
        String source = "<TD width=150><A \n" +
                "href=\"http://www.qzbfjz.com/Product.asp?BigClassName=԰�ֹŽ������̶���\">԰�ֹŽ������̶���</A></TD></TR>" +
                "<TR>";
        String aName = "԰�ֹŽ������̶���";
        int index = 1;
        String expResult = "<TD width=150><A \n" +
                "href=\"{��������1}\">{���±���1}</A></TD></TR>" +
                "<TR>";
        String result = Util.replaceLinkOfListTemplate(source, aName, index);
        System.out.println("result=||"+result+"||");
        System.out.println("expResult=||"+expResult+"||");
        assertEquals(expResult, result);
    }

    /**
     * Test of replaceSrc_href method, of class Util.
     */
    @Test
    public void testReplaceSrc_href() {
      
    }
}
