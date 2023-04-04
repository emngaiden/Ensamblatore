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
package com.ensamblatore.core.assembler.instruction;

import com.ensamblatore.core.architecture.Word;
import java.io.Serializable;

/**
 *
 * @author emnga
 */
public class SectionValue implements Serializable{
    public String identifier;
    public Word value;

    public SectionValue(String Identifier, Word value) {
        this.identifier = Identifier;
        this.value = value;
    }
    
    @Override
    public String toString(){
        StringBuilder sb=new StringBuilder();
        sb.append("identifier: ").append(this.identifier).append(", ");
        sb.append("value: ").append(this.value.toString());
        return sb.toString();
    }
    
    public static SectionValue parseSectionValue(String parse){
        String[] data=parse.split(",");
        String name=data[0];
        Word value=Word.parseWord(data[1]);
        return new SectionValue(name, value);
    }
}
