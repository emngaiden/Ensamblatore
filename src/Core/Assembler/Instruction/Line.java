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
package Core.Assembler.Instruction;

import Core.Assembler.Assembler;
import Core.Assembler.Format.Sintax;
import Exceptions.ParsingException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author emnga
 */
public class Line implements Serializable{

    public String identifier;
    public Sintax sintax;
    public String superSintax;
    public int id;
    public static ArrayList<Line> DEFAULTLINES;
    
    
    static public char identifierOpenerCharacter = '<';
    static public char identifierCloserCharacter = '>';
    static public char inLineCommentCharacter = ';';
    static public char identifierDividerCharacter = '|';
    static public String longCommentOpener = "/*";
    static public String longCommentCloser = "*/";
    
    static private Object[] DEFAULTVALUES=new Object[]{
        '<',
        '>',
        ';',
        '|',
        "/*",
        "*/"
    };
    
    static public final int ID_LINE=0;
    static public final int ID_DIRECTIVE=1;
    static public final int ID_INSTRUCTION=2;
    static public final int ID_VARIABLEUSAGE=3;
    
    @Override
    public String toString(){
        StringBuilder sb=new StringBuilder();
        sb.append("Identifier: ").append(this.identifier).append(';');
        sb.append("Id: ").append(this.id).append(';');
        sb.append("Sintax: ").append(this.sintax).append(';');
        sb.append("Supersintax: ").append(this.superSintax);
        return sb.toString();
    }
    public Line() {
        this.id=Line.ID_LINE;
    }

    public Line(String identifier, Sintax sintax) {
        this(identifier);
        this.id=Line.ID_LINE;
        this.identifier = identifier;
        this.sintax = sintax;
    }
    
    public Line(String identifier, String toSintax){
        this(identifier);
        this.id=Line.ID_LINE;
        this.identifier = identifier;
        this.sintax = new Sintax(toSintax);
        this.sintax.process();
    }

    public Line(String identifier) {
        this();
        this.id=Line.ID_LINE;
        this.identifier = identifier;
    }

    public final void setSintax(String toSintax) {
        this.sintax = new Sintax(toSintax);
        this.sintax.process();
    }

    public final void setSintax(Sintax sintax) {
        this.sintax = sintax;
    }

    public String getIdentifier() {
        return identifier;
    }

    public Sintax getSintax() {
        return sintax;
    }

    public static void setIdentifierOpenerCharacter(char identifierOpenerCharacter) {
        Line.identifierOpenerCharacter = identifierOpenerCharacter;
    }

    public static void setIdentifierCloserCharacter(char identifierCloserCharacter) {
        Line.identifierCloserCharacter = identifierCloserCharacter;
    }

    public static void setIdentifierDividerCharacter(char identifierDividerCharacter) {
        Line.identifierDividerCharacter = identifierDividerCharacter;
    }

    public static void setInLineCommentCharacter(char inLineCommentCharacter) {
        Line.inLineCommentCharacter = inLineCommentCharacter;
    }
    
    public static int countParenthesis(String toCount, boolean highLevel){
        int ret=0;
        int opi=0;
        int cpi=0;
        boolean closed=false;
        for(int i=0; i<toCount.length(); i++){
            char found=toCount.charAt(i);
            
            if(found=='('){
                try{
                    if(toCount.charAt(i-1)!='\\')
                        opi++;
                }catch (StringIndexOutOfBoundsException e){
                    opi++;
                }
            }
            if(found==')'){
                 try{
                    if(toCount.charAt(i-1)!='\\'){
                        cpi++;
                        if(cpi==opi){
                            closed=true;
                        }
                    }
                }catch (StringIndexOutOfBoundsException e){
                    cpi++;
                }
            }
            if (closed){
                ret++;
                closed=false;
            }
        }
        if(opi!=cpi){
            throw new ParsingException("Missing parenthesis on "+toCount);
        }
        return highLevel?ret:opi;
    }
    
    public static String[] extractTextInParenthesis(String toExtract, boolean highLevelOnly){
        return highLevelOnly?extractHighLevel(toExtract):extractLowLevel(toExtract);
    }
    
    private static String[] extractHighLevel(String toExtract) {
        String[] ret=new String[Line.countParenthesis(toExtract, true)];
        int opi=0;
        int cpi=0;
        boolean closed=false;
        boolean inside=false;
        int aux=0;
        StringBuilder sb=new StringBuilder();
        for(int i=0; i<toExtract.length(); i++){
            char found=toExtract.charAt(i);
            switch (found) {
                case '(':
                    sb.append(found);
                    opi++;
                    inside=true;
                    break;
                case ')':
                    sb.append(found);
                    cpi++;
                    if(cpi==opi){
                        closed=true;
                        inside=false;
                    }   
                    break;
                default:
                    if(inside){
                        sb.append(found);
                    }    
                    break; 
            }
            if (closed){
                ret[aux]=sb.toString();
                sb=new StringBuilder();
                aux++;
                closed=false;
            }
        }
        return ret;
    }

    private static String[] extractLowLevel(String toExtract) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public static Line getLine(String identifier){
        for(Line lin: Line.DEFAULTLINES){
            if(lin.identifier.equals(identifier)){
                return lin;
            }
        }
        return null;
    }
    
    public static void init(Assembler ass){
        ArrayList<Line> ret=new ArrayList<>();
        ret.add(new Line("VALUE","(<HEX|BINARY|INTEGER|OCTAL>)"));
        ret.add(new Line("IDENTIFIER","[a-z|A-Z]+"));
        ret.add(new Line("HEX","0x(-?[0-9|a-f|A-F]+)"));
        ret.add(new Line("BINARY","0b([01]+)"));
        ret.add(new Line("INTEGER","-?[0-9]+"));
        ret.add(new Line("OCTAL","0o(-?[0-7]+)"));
        ret.add(new Line("TAG", "<IDENTIFIER>:"));
        ret.add(new Line("REGISTER","&<REGISTERNAME><INTEGER|OCTAL|HEX|BINARY>"));
        Line.DEFAULTLINES=ret;
        if(ass!=null){
            ass.addLines(Line.DEFAULTLINES);
        }
    }
    
    public static int identifyLine(String toIdentify){
        for(int i=1; i<Line.DEFAULTLINES.size(); i++){
            if(i!=0 && toIdentify.matches(Line.DEFAULTLINES.get(i).getSintax().getRegex())){
                return i;
            }
        }
        return -1;
    }
    
    public void setAsLine(){
        this.id=Line.ID_LINE;
    }
    
    public void sertAsDirective(){
        this.id=Line.ID_DIRECTIVE;
    }
    
    public void setAsInstruction(){
        this.id=Line.ID_INSTRUCTION;
    }
    
    public void setAsVariableUsage(){
        this.id=Line.ID_VARIABLEUSAGE;
    }
    
    public static void restoreDefaultValues(){
        Line.identifierOpenerCharacter=(char)Line.DEFAULTVALUES[0];
        Line.identifierCloserCharacter=(char)Line.DEFAULTVALUES[1];
        Line.inLineCommentCharacter=(char)Line.DEFAULTVALUES[2];
        Line.identifierDividerCharacter=(char)Line.DEFAULTVALUES[3];
        Line.longCommentOpener=(String)Line.DEFAULTVALUES[4];
        Line.longCommentCloser=(String)Line.DEFAULTVALUES[5];
    }
    
    public static boolean isDefault(Line line){
        for(Line lin: Line.DEFAULTLINES){
            if(lin.identifier.equals(line.identifier)){
                return true;
            }
        }
        return false;
    }
    
    public static Object[] getDefaultValues(){
        return Line.DEFAULTVALUES;
    }
}