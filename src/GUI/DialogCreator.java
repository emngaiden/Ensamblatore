/*
 * Copyright (C) 2017 emnga
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package GUI;

import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JOptionPane;

/**
 *
 * @author emnga
 */
public class DialogCreator {

    public static void createMessageDialog(Component component, String message) {
        JOptionPane.showMessageDialog(component, message);
    }

    public static void createWarningDialog(Component component, String title, String message) {
        JOptionPane.showMessageDialog(component, message, title, JOptionPane.WARNING_MESSAGE);
    }

    public static void createErrorDialog(Component component, String title, String message) {
        JOptionPane.showMessageDialog(component, message, title, JOptionPane.ERROR_MESSAGE);
    }

    public static void createUniconedDialog(Component component, String title, String message) {
        JOptionPane.showMessageDialog(component, message, title, JOptionPane.PLAIN_MESSAGE);
    }

    public static void createCustomDialog(Component component, String title, String message, Icon icon) {
        JOptionPane.showMessageDialog(component, message, title, JOptionPane.INFORMATION_MESSAGE, icon);
    }

    public static int createYesNoDialog(Component component, String message, String title) {
        return JOptionPane.showConfirmDialog(component, message, title, JOptionPane.YES_NO_OPTION);
    }

    public static int createCustomYesNoDialog(Component component, String message, String title, Object[] options, Object defaultOption, Icon icon) {
        return JOptionPane.showOptionDialog(component, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon, options, defaultOption);
    }

    public static int createYesNoCancelDialog(Component component, String message, String title, Icon icon) {
        return JOptionPane.showConfirmDialog(component, message, title, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, icon);
    }

    public static int createCustomYesNoCancelDialog(Component component, String message, String title, Icon icon, Object[] options, Object defaultOption) {
        return JOptionPane.showOptionDialog(component, message, title, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, icon, options, defaultOption);
    }

    public static Object createInputDialogWithComboBox(Component component, String title, String message, Icon icon, Object[] options, Object defaultOption) {
        return JOptionPane.showInputDialog(component,message,title,JOptionPane.PLAIN_MESSAGE,icon,options,defaultOption);
    }
    
    public static Object createInputDialogWithText(Component component, String title, String message, Icon icon, Object defaultOption){
        return JOptionPane.showInputDialog(component,message,title,JOptionPane.PLAIN_MESSAGE,icon,null,defaultOption);
    }
}
