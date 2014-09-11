
package it.mam.REST.controller.back;

import it.univaq.f4i.iw.framework.security.SecurityLayer;
import java.util.Calendar;

/**
 *
 * @author Mirko
 */
public class XD {
    public static void main (String[] args){
        System.out.println((SecurityLayer.checkDate("1/8/2013")).getTime());
    }
}
