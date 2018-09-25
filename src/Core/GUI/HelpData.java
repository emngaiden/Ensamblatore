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
package Core.GUI;

import java.net.URL;
import javax.swing.ImageIcon;

/**
 *
 * @author emnga
 */
public interface HelpData {

    public static final ImageIcon ICON = new ImageIcon(HelpData.class.getResource("/logoupiiz.png"));
    public static final String MAIN_HELP = "<html>\n"
            + "\n"
            + "<h2>Main window</h2>\n"
            + "\n"
            + "<p>In this window, you can see relevant information about the CPU file you are working with.<br />\n"
            + "Besides, you can save, edit and load your work in the File menu.&nbsp;</p>\n"
            + "\n"
            + "<p>The sections shown in this window are:</p>\n"
            + "\n"
            + "<ul>\n"
            + "	<li>CPU data: This section&nbsp;shows the data used to identify the CPU. This data is used by the users<br />\n"
            + "	for version control and identification of their files, it does not affect the CPU behavior.</li>\n"
            + "	<li>Architecture data: in this section you can see the architecture configuration for your<br />\n"
            + "	CPU file. You can modify this data by clicking Edit-&gt;Architecture. You can also modify<br />\n"
            + "	some of this parameters with directives inside your code.</li>\n"
            + "	<li>Assembler data: The assembler data sections propouse is to show you the number of instructions<br />\n"
            + "	you have in your CPU assembler, the instruction types and if the assembler should validate<br />\n"
            + "	empty sectors when asembling (see Assembler Editor help for more info). You can add<br />\n"
            + "	more instructions, lines and instruction types by clicking Edit-&gt;Assembler</li>\n"
            + "	<li>Memories: Here, you can see the memories that your CPU have, as well as the actual data<br />\n"
            + "	of each address, you can modify this data by clicking Edit-&gt;Memory and double click<br />\n"
            + "	in the name of the memory you want to modify.</li>\n"
            + "	<li>Assembler: This section displays a table where you can see all the instructions, lines, directives and<br />\n"
            + "	variable usages actually contained in your assembler. Also, you can see the type, identifier,<br />\n"
            + "	if its predefined by default, the sintaxis of the line, and the processed sintaxis.</li>\n"
            + "</ul>\n"
            + "</html>";
    public static final String ASSEMBLER_HELP = "<html>\n"
            + "<h2>Assembler editor</h2>\n"
            + "\n"
            + "<p>In this window, you can add, edit and remove instructions, lines, variable usages<br />\n"
            + "and instruction types. Also, you can modify the sintax of the predefined directives.</p>\n"
            + "\n"
            + "<p>Double click in any item to edit.<br />\n"
            + "To add a new item, select the tab of the desired type of&nbsp;item<br />\n"
            + "and click on New&nbsp;(you can add the by Edit-&gt;Add menu too).<br />\n"
            + "To delete an item double click on it and press the Delete button (you can&#39;t delete default) items.</p>\n"
            + "\n"
            + "<p>There are 5&nbsp;types of items you can modify, and 4 of them will&nbsp;be interpreted by the assembler.&nbsp;<br />\n"
            + "They share some properties that you must comprehend before you can create or edit them:</p>\n"
            + "\n"
            + "<ul>\n"
            + "	<li>Identifier: this is a textual representation of the item on the assembler. It is used inside<br />\n"
            + "	other item&#39;s sinstax and it MUST be unique for every item. Its recomended to use<br />\n"
            + "	all caps for every identifier and replace spaces for underscores.&nbsp;(ie. MY_IDENTIFIER).</li>\n"
            + "	<li>Sintax: this parameter is the representation of the correct sintax of the item inside the assembler.<br />\n"
            + "	This systems works with modified&nbsp;<a href=\"https://en.wikipedia.org/wiki/Regular_expression\">Regular expressions</a>, if you want to use parentheses you<br />\n"
            + "	must escape them with inverted dash,&nbsp;ie:&nbsp;add\\(value\\). You can reference the sintax of other<br />\n"
            + "	items by enclosing their identifier inside the reference characters (default: &lt; and &gt;).&nbsp;<br />\n"
            + "	Must scape characters: ( , ) , [ , ] , |&nbsp;<br />\n"
            + "	ie: add &lt;VALUE&gt;, &lt;VALUE&gt;, &lt;INTEGER&gt;\\(&lt;HEX&gt;\\)</li>\n"
            + "</ul>\n"
            + "\n"
            + "<p>The 4 types you can create and modify are:</p>\n"
            + "\n"
            + "<ul>\n"
            + "	<li>Instruction type: this item is the representaion of a instruction type inside the assembler.<br />\n"
            + "	In an instruction type, you can specify an instruction format and an identifier.<br />\n"
            + "	The instruction format is the way that the data is distributed in each word of the CPU.<br />\n"
            + "	For example, you can have an instruction type called &quot;Type2&quot; with 3 sections: OPcode,<br />\n"
            + "	addressing_mode and data, with sizes of 5 bits, 3 bits and 8 bits respectively in a 16 bit<br />\n"
            + "	processor. You need to specify each sector by clicking the checkbox of &quot;New Section&quot; and<br />\n"
            + "	writing the name, initial bit and final bit of the sector (MSB is bit 0), you would put for this example<br />\n"
            + "	the following data: Name: OPcode, From: 0, To: 4, and click in add. ALL ISNTRUCTION TYPES<br />\n"
            + "	MUST HAVE AN OPCODE SECTION.</li>\n"
            + "	<li>Instruction: this item is the representation of an instruction inside the assembler. You must<br />\n"
            + "	specify an identifier, a sintax and a type. For each instruction you can specify values that<br />\n"
            + "	will be filled with predefined data in the instruction format. Following the previous example<br />\n"
            + "	we will create the instruction &quot;not&quot;. Identifier: &quot;NOT&quot;, Sintax: &quot;not &lt;VAR&gt;&quot;, Type: Type2.<br />\n"
            + "	You can see the instruction type data in the text area below. For this example, we will specify<br />\n"
            + "	that the sector &quot;addressing_mode&quot; will have by default the value 110, so we add it in the panel on the right (Left=MSB). <br />\n"
            + "In the data order, you can specify the order in wich the data for each<br />\n"
            + "	empty sector will appear in the instruction.<br />\n"
            + "	For example you have the instruction &quot;add &lt;VALUE&gt;, &lt;INTEGER&gt;,&lt;VAR1&gt;&quot; and you have 3<br />\n"
            + "	empty sectors in this instruction in this order: val1, destiny, and val2. In this example your data<br />\n"
            + "	order does not match the order of the variables in the sintax, so you specify a data order by<br />\n"
            + "	selecting the checkbox of the data order and writing in the text area: &quot;val1, val2, destiny&quot;.&nbsp;&nbsp;</li>\n"
            + "	<li>Line: this item is used to simlify some complex sintaxes inside the program.&nbsp;In the las example, we used the <br />\n"
            + "identifier INTEGER&nbsp;inside the sintax of our instruction. With this, we are telling the<br />\n"
            + "	system that we are going to use the sintax of INTEGER in that part of the instruction. We&nbsp;<br />\n"
            + "	must build the sintax for INTEGER with Regular expresions. So we add a new line, with the data Identifier: Integer, Sintax: &quot;[0-9]+&quot;. <br />"
            + "The Line for INTEGER is included by default by "
            + "	the software and its reccomended to not modify it if it&#39;s not necesary.</li>\n"
            + "	<li>Variable usage: this item is like Line item, with the diference that this item is used as it name<br />\n"
            + "	implies, for calling variables inside another item. Following the past example, we must add a<br />\n"
            + "	new variable usage. Identifier: VAR1, Sintax: $&lt;IDENTIFIER&gt;</li>\n"
            + "</ul>\n"
            + "\n"
            + "<p>The fifth type is the directive. A&nbsp;directive is a line of code that, unlike the instructions,<br />\n"
            + "they are not converted to&nbsp;machine language when the code is assembled, instead, they are executed<br />\n"
            + "by the assembler.<br />\n"
            + "This is why we can&#39;t create new directives, but we can modify the sintax of any of them.</p>\n"
            + "</html>";
}
