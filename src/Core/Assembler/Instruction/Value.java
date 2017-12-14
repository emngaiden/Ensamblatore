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

import java.io.Serializable;

/**
 *
 * @author emnga
 */
public class Value implements Serializable{

    private static Value binaryToHex(Value aThis) {
        String binaryData = aThis.rawValue;
        String hexSintax = Line.DEFAULTLINES.get(2).getSintax().getRegex();
        String hexString=Integer.toHexString(Integer.parseInt(binaryData, 2));
        hexSintax = hexSintax.replaceAll("\\(.*?\\)", hexString);
        return new Value(hexSintax,3,hexString);
    }

    private static Value octalToHex(Value aThis) {
        String octalData = aThis.rawValue;
        String hexSintax = Line.DEFAULTLINES.get(2).getSintax().getRegex();
        String hexString = Integer.toHexString(Integer.parseInt(octalData, 8));
        hexSintax = hexSintax.replaceAll("\\(.*?\\)", hexString);
        return new Value(hexSintax,3,hexString);
    }

    private static Value integerToHex(Value aThis) {
        String intData = aThis.rawValue;
        String hexSintax = Line.DEFAULTLINES.get(2).getSintax().getRegex();
        String hexString = Integer.toHexString(Integer.parseInt(intData, 10));
        hexSintax = hexSintax.replaceAll("\\(.*?\\)", hexString);
        return new Value(hexSintax,3,hexString);
    }

    private static Value octalToBinary(Value aThis) {
        return Value.hexToBinary(Value.octalToHex(aThis));
    }

    private static Value integerToBinary(Value aThis) {
        return Value.hexToBinary(Value.integerToHex(aThis));
    }


    private static Value octalToInteger(Value aThis) {
        return Value.hexToInteger(Value.octalToHex(aThis));
    }

    private static Value binaryToInteger(Value aThis) {
        return Value.hexToInteger(Value.binaryToHex(aThis));
    }

    private static Value hexToInteger(Value aThis) {
        String hexData = aThis.rawValue;
        String intSintax = Line.DEFAULTLINES.get(4).getSintax().getRegex();
        Integer i = Integer.parseInt(hexData,16);
        intSintax = intSintax.replaceAll("\\(.*?\\)", i.toString());
        return new Value(intSintax,4,i.toString());
    }

    private static Value hexToOctal(Value aThis) {
        return Value.integerToOctal(Value.hexToInteger(aThis));
    }

    private static Value binaryToOctal(Value aThis) {
        return Value.integerToOctal(Value.binaryToInteger(aThis));
    }

    private static Value integerToOctal(Value aThis) {
        String intData = aThis.rawValue;
        String octalSintax = Line.DEFAULTLINES.get(5).getSintax().getRegex();
        String oct = Integer.toOctalString(Integer.parseInt(intData));
        octalSintax = octalSintax.replaceAll("\\(.*?\\)", oct);
        return new Value(octalSintax,5,oct);
    }
    
    
    private final String raw;
    private final int type;
    private String rawValue="";

    @Override
    public String toString(){
        StringBuilder sb=new StringBuilder();
        sb.append("[[").append("Raw: ").append(raw).append("], ").append("[Type: ").append(type).append("], ").append("[Raw Value: ").append(rawValue).append("]]");
        return sb.toString();
    }
    
    public int getType(){
        return this.type;
    }
    
    public Value(String value, int type, String rawValue){
        this(value,type);
        this.rawValue=rawValue;
    }
    
    public Value(String value) {
        this.raw = value;
        this.type = identifyType();
        this.rawValue=identifyRawValue();
    }

    public Value(String value, int type) {
        this.raw = value;
        this.type = type;
        this.rawValue=identifyRawValue();
    }

    private int identifyType() {
        return Line.identifyLine(raw);
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
        String hexData = value.rawValue;
        String binarySintax = Line.DEFAULTLINES.get(3).getSintax().getRegex();
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
        return new Value(binarySintax, 3,binaryData.toString());
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
}
