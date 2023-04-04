/*
 * Copyright (C) 2018 emnga
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
package com.ensamblatore;

import com.ensamblatore.core.assembler.Instruction.Line;
import com.ensamblatore.core.assembler.Instruction.Value;

/**
 *
 * @author emnga
 */
public class TestValue {
    public static void main(String[] args) {
        Line.initDefaultLinesOnly();
        Value.initCustomSintax(Line.getDefaultLinesSintax());
        Value entero=new Value("17");
        Value binario = new Value("0b10001");
        Value hex = new Value("0x11");
        Value oct =  new Value("0o21");
        System.out.println("a entero");
        System.out.println(entero.toInteger().toString());
        System.out.println(binario.toInteger().toString());
        System.out.println(hex.toInteger().toString());
        System.out.println(oct.toInteger().toString());
        System.out.println("a binario");
        System.out.println(entero.toBinary().toString());
        System.out.println(binario.toBinary().toString());
        System.out.println(hex.toBinary().toString());
        System.out.println(oct.toBinary().toString());
        System.out.println("a hex");
        System.out.println(entero.toHex().toString());
        System.out.println(binario.toHex().toString());
        System.out.println(hex.toHex().toString());
        System.out.println(oct.toHex().toString());
        System.out.println("a oct");
        System.out.println(entero.toOctal().toString());
        System.out.println(binario.toOctal().toString());
        System.out.println(hex.toOctal().toString());
        System.out.println(oct.toOctal().toString());
    }
}
