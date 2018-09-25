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
public class InstructionType implements Serializable{

    private InstructionFormat format;
    private String identifier;
    public int id;

    public InstructionType(InstructionFormat format, String identifier) {
        this(identifier);
        this.format = format;
    }

    public InstructionType(String identifier) {
        this.identifier = identifier;
    }

    public InstructionType(InstructionType type) {
        this.format=new InstructionFormat(type.format);
        this.id=type.id;
        this.identifier=type.identifier;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Identifier: \'").append(this.identifier).append("\',");
        sb.append("Format: \'").append(this.format.toString()).append("\'");
        return sb.toString();
    }

    public InstructionFormat getFormat() {
        return format;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
    
    
}
