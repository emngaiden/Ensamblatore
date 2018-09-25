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

import Core.Assembler.Format.Sintax;
import java.io.Serializable;

/**
 *
 * @author emnga
 */
public class Value implements Serializable{
    /**
    * Type is related to the index of the default lines
    * ex. the hex sintax is the index number 2 on the default lines
    * Therefore, type 0 and type 1 are not used for calculations
    */
    public static final int TYPE_HEX=2;
    public static final int TYPE_OCTAL=5;
    public static final int TYPE_INTEGER=4;
    public static final int TYPE_BINARY=3;
    private static Sintax[] sintaxis;
    
    private static Value binaryToHex(Value aThis) {
        checkInit();
        String binaryData = aThis.rawValue;
        String hexSintax = Value.sintaxis[TYPE_HEX].getRegex();
        String hexString=Long.toHexString(Long.parseLong(binaryData, 2));
        hexSintax = hexSintax.replaceAll("\\(.*?\\)", hexString);
        return new Value(hexSintax,TYPE_HEX,hexString);
    }

    private static Value octalToHex(Value aThis) {
        checkInit();
        String octalData = aThis.rawValue;
        String hexSintax = Value.sintaxis[TYPE_HEX].getRegex();
        String hexString = Long.toHexString(Long.parseLong(octalData, 8));
        hexSintax = hexSintax.replaceAll("\\(.*?\\)", hexString);
        return new Value(hexSintax,TYPE_HEX,hexString);
    }

    private static Value integerToHex(Value aThis) {
        checkInit();
        String intData = aThis.rawValue;
        String hexSintax = Value.sintaxis[TYPE_HEX].getRegex();
        String hexString = Long.toHexString(Long.parseLong(intData, 10));
        hexSintax = hexSintax.replaceAll("\\(.*?\\)", hexString);
        return new Value(hexSintax,TYPE_HEX,hexString);
    }

    private static Value octalToBinary(Value aThis) {
        checkInit();
        return Value.hexToBinary(Value.octalToHex(aThis));
    }

    private static Value integerToBinary(Value aThis) {
        checkInit();
        return Value.hexToBinary(Value.integerToHex(aThis));
    }


    private static Value octalToInteger(Value aThis) {
        String octalData = aThis.rawValue;
        String intSintax = Value.sintaxis[TYPE_INTEGER].getRegex();
        Long i = Long.parseLong(octalData,8);
        intSintax = intSintax.replaceAll("\\(.*?\\)", i.toString());
        return new Value(intSintax,TYPE_INTEGER,i.toString());
    }

    private static Value binaryToInteger(Value aThis) {
        checkInit();
        String binaryData = aThis.rawValue;
        String intSintax = Value.sintaxis[TYPE_INTEGER].getRegex();
        Long i = Long.parseLong(binaryData,2);
        intSintax = intSintax.replaceAll("\\(.*?\\)", i.toString());
        return new Value(intSintax,TYPE_INTEGER,i.toString());
    }

    private static Value hexToInteger(Value aThis) {
        checkInit();
        String hexData = aThis.rawValue;
        String intSintax = Value.sintaxis[TYPE_INTEGER].getRegex();
        Long i = Long.parseLong(hexData,16);
        intSintax = intSintax.replaceAll("\\(.*?\\)", i.toString());
        return new Value(intSintax,TYPE_INTEGER,i.toString());
    }

    private static Value hexToOctal(Value aThis) {
        checkInit();
        return Value.integerToOctal(Value.hexToInteger(aThis));
    }

    private static Value binaryToOctal(Value aThis) {
        checkInit();
        return Value.integerToOctal(Value.binaryToInteger(aThis));
    }

    private static Value integerToOctal(Value aThis) {
        checkInit();
        String intData = aThis.rawValue;
        String octalSintax = Value.sintaxis[TYPE_OCTAL].getRegex();
        String oct = Long.toOctalString(Long.parseLong(intData));
        octalSintax = octalSintax.replaceAll("\\(.*?\\)", oct);
        return new Value(octalSintax,TYPE_OCTAL,oct);
    }

    private static void checkInit() {
        if(sintaxis==null){
            Value.initAsDefaultLines();
        }
    }
    
    
    private final String raw;
    private final int type;
    private String rawValue="";
    
    public int getType(){
        return this.type;
    }
    
    public Value(String value, int type, String rawValue){
        this(value,type,false);
        this.rawValue=rawValue;
    }
    
    public Value(String value) {
        this.raw = value;
        this.type = identifyType();
        this.rawValue=identifyRawValue();
    }
    
    public static Value getIntegerValueInstance(int integer){
        String value=Integer.toString(integer);
        return new Value(value, Value.TYPE_INTEGER,value);
    }
    public Value(String value, int type, boolean isRawValue) {
        this.type=type;
        if(isRawValue){
            this.raw=null;
            this.rawValue=value;
        }
        else{
            this.raw=value;
            this.rawValue=identifyRawValue();
        }
    }

    private int identifyType() {
        return Line.identifyLine(raw,null);
    }

    public Value toHex() {
        switch (type) {
            case 2:
                return this;
            case 3:
                return Value.binaryToHex(this);
            case 4:
                return Value.integerToHex(this);
            case 5:
                return Value.octalToHex(this);
            case 1:
                return this;
            default:
                return null;
        }
    }
    
    public Value toBinary(){
        switch(type){
            case 2:
                return Value.hexToBinary(this);
            case 3:
                return this;
            case 4:
                return Value.integerToBinary(this);
            case 5:
                return Value.octalToBinary(this);
            case 1:
                return this;
            default:
                return null;
        }
    }
    
    public Value toInteger(){
        switch(type){
            case 2:
                return Value.hexToInteger(this);
            case 3:
                return Value.binaryToInteger(this);
            case 4:
                return this;
            case 5:
                return Value.octalToInteger(this);
            case 1:
                return this;
            default:
                return null;
        }
    }
    
    public Value toOctal(){
        switch(type){
            case 2:
                return Value.hexToOctal(this);
            case 3:
                return Value.binaryToOctal(this);
            case 4:
                return Value.integerToOctal(this);
            case 5:
                return this;
            case 1:
                return this;
            default:
                return null;
        }
    }

    private static Value hexToBinary(Value value) {
        checkInit();
        String hexData = value.rawValue;
        String binarySintax = Value.sintaxis[TYPE_BINARY].getRegex();
        StringBuilder binaryData = new StringBuilder();
        for (int i = 0; i < hexData.length(); i++) {
            switch (hexData.charAt(i)) {
                case '0':
                    binaryData.append("0000");
                    break;
                case '1':
                    binaryData.append("0001");
                    break;
                case '2':
                    binaryData.append("0010");
                    break;
                case '3':
                    binaryData.append("0011");
                    break;
                case '4':
                    binaryData.append("0100");
                    break;
                case '5':
                    binaryData.append("0101");
                    break;
                case '6':
                    binaryData.append("0110");
                    break;
                case '7':
                    binaryData.append("0111");
                    break;
                case '8':
                    binaryData.append("1000");
                    break;
                case '9':
                    binaryData.append("1001");
                    break;
                case 'A':
                    binaryData.append("1010");
                    break;
                case 'B':
                    binaryData.append("1011");
                    break;
                case 'C':
                    binaryData.append("1100");
                    break;
                case 'D':
                    binaryData.append("1101");
                    break;
                case 'E':
                    binaryData.append("1110");
                    break;
                case 'F':
                    binaryData.append("1111");
                    break;
                case 'a':
                    binaryData.append("1010");
                    break;
                case 'b':
                    binaryData.append("1011");
                    break;
                case 'c':
                    binaryData.append("1100");
                    break;
                case 'd':
                    binaryData.append("1101");
                    break;
                case 'e':
                    binaryData.append("1110");
                    break;
                case 'f':
                    binaryData.append("1111");
                    break;
                default:
                    break;
            }
        }
        binarySintax = binarySintax.replaceAll("\\(.*?\\)", binaryData.toString());
        return new Value(binarySintax, TYPE_BINARY,binaryData.toString());
    }

    public String getRaw() {
        return raw;
    }

    public String getRawValue() {
        return rawValue;
    }
    
    private String identifyRawValue(){
        switch(this.type){
            case 2:
                return this.raw.replaceAll(Line.DEFAULTLINES.get(2).getSintax().getRegex(), "$1");
            case 3:
                return this.raw.replaceAll(Line.DEFAULTLINES.get(3).getSintax().getRegex(), "$1");
            case 4:
                return this.raw.replaceAll(Line.DEFAULTLINES.get(4).getSintax().getRegex(), "$0");
            case 5:
                return this.raw.replaceAll(Line.DEFAULTLINES.get(5).getSintax().getRegex(), "$1");
            case 1:
                return this.raw;
            default:
                return null;
        }
    }
    
    public static void initAsDefaultLines(){
        Value.sintaxis=new Sintax[Line.DEFAULTLINES.size()];
        for(int i=0; i<Value.sintaxis.length; i++){
            Value.sintaxis[i]=Line.DEFAULTLINES.get(i).getSintax();
        }
    }
    
    public static void initCustomSintax(Sintax[] sintax){
        Value.sintaxis=sintax;
    }
    
    @Override
    public String toString(){
        StringBuilder sb=new StringBuilder();
        sb.append("Raw: ").append(this.raw).append(';');
        sb.append("Raw Value: ").append(this.rawValue).append(';');
        sb.append("Type ID: ").append(this.type).append(";");
        sb.append("Type name: ");
        String typeName="";
        switch(this.type){
            case TYPE_BINARY:
                typeName="binary";
                break;
            case TYPE_HEX:
                typeName="hex";
                break;
            case TYPE_INTEGER:
                typeName="integer";
                break;
            case TYPE_OCTAL:
                typeName="octal";
                break;
            case 1:
                typeName="value(E)";
                break;
            default:
                typeName="(E)";
                break;
        }
        sb.append(typeName).append(';');
        return sb.toString();
    }
}
