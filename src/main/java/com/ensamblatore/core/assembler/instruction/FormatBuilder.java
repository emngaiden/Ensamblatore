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
import com.ensamblatore.core.architecture.Word;
import com.ensamblatore.core.assembler.format.Tristate;
import com.ensamblatore.exceptions.SectorNotFoundException;
import com.ensamblatore.exceptions.WordException;
import com.ensamblatore.exceptions.WordSizeException;
import java.io.Serializable;
import java.util.Arrays;

/**
 *
 * @author emnga
 */
public class FormatBuilder implements Serializable{

    private Instruction instruction;
    private String[] order;
    private Tristate[] data;
    private int index = 0;
    private boolean infered=false;

    public FormatBuilder(Instruction instruction, String[] order) {
        this.instruction = instruction;
        this.order = order;
        this.data = instruction.format.data.clone();
    }
    
    public FormatBuilder(Instruction instruction,String[] order, boolean infered){
        this(instruction, order);
        this.infered=infered;
    }

    public Word createWord(boolean canHaveEmptySectors) {
        if (!canHaveEmptySectors) {
            String[] names = this.instruction.getType().getFormat().getSectionsNames();
            InstructionSector found;
            for (int i = 0; i < names.length; i++) {
                found = this.instruction.getType().getFormat().findSector(names[i]);
                if (this.isSectorEmpty(found)) {
                    throw new WordException("Sector " + found.name + " is empty on "+this.instruction.identifier);
                }
            }
        }
        boolean[] newData = new boolean[this.data.length];
        for (int i = 0; i < data.length; i++) {
            newData[i] = data[i].booleanValue();
        }
        return new Word(newData.length, true, newData);
    }

    public boolean insertValue(Word value) {
        boolean state = false;
        InstructionSector found;
        if(infered){
            do {
                found = this.instruction.getType().getFormat().findSector(order[index]);
                if (found == null) {
                   throw new SectorNotFoundException(order[index], this.instruction.getIdentifier());
                }
                index++;
            } while (!this.isSectorEmpty(found));
        }
        else{
            found=this.instruction.getType().getFormat().findSector(order[index]);
            index++;
        }
        if (value.size > found.size) {
            throw new WordSizeException(value, found.name, value.size, found.size, instruction.identifier);
        }
        for (int i = 0; i < value.size; i++) {
            this.data[found.finish - i] = new Tristate((value.bits[i]));
        }
        return state;
    }

    private boolean isSectorEmpty(InstructionSector sector) {
        for (int i = sector.start; i <= sector.finish; i++) {
            if (this.data[i].value == 0 || this.data[i].value == 1) {
                return false;
            }
        }
        return true;
    }
}
