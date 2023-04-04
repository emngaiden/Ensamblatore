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

import com.ensamblatore.core.architecture.Sector;
import java.io.Serializable;

/**
 *
 * @author emnga
 */
public class InstructionSector extends Sector implements Serializable{
    
    
    public InstructionSector(int start, int finish, String name) {
        super(start, finish, name);
    }
    
    @Override
    public String toString(){
        StringBuilder sb=new StringBuilder();
        sb.append("start: ").append(start).append(", ");
        sb.append("finish: ").append(finish).append(", ");
        sb.append("name: ").append(name);
        return sb.toString();
    }
    
    public static InstructionSector parseInstructionSector(String toParse){
        String[] data=toParse.split(",");
        String name=data[0];
        int from = Integer.parseInt(data[1]);
        int to=Integer.parseInt(data[2]);
        return new InstructionSector(from, to, name);
    }
}
