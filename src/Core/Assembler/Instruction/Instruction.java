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

import Core.Architecture.Sector;
import Core.Architecture.Word;
import Core.Assembler.AddressingMode;
import Core.Assembler.Assembler;
import Core.Assembler.Format.Format;
import Core.Assembler.Format.Sintax;
import Core.Assembler.Format.Tristate;
import Exceptions.ReferenceNotFoundException;
import Exceptions.WordSizeException;
import java.io.Serializable;
import java.util.Arrays;

/**
 *
 * @author emnga
 */
public final class Instruction extends Line implements Serializable{

    private InstructionType type;
    public AddressingMode addressingMode;
    private Assembler parent;
    public SectionValue[] values;
    public Format format;
    public String[] dataOrder;
    
    public Instruction(InstructionType type, String identifier, Assembler parent, SectionValue[] values) {
        super(identifier);
        this.id=Line.ID_INSTRUCTION;
        if(parent==null){
            throw new NullPointerException("Parent can't be null");
        }
        this.parent=parent;
        this.type=type;
        Word OPCode=null;
        for(SectionValue val: values){
            if(val.identifier.toLowerCase().equals("opcode")){
                OPCode=val.value;
                break;
            }
        }
        if(OPCode==null){
            throw new ReferenceNotFoundException("SectionsValues","OPCode");
        }
        this.values=values;
        this.identifier=identifier;
    }

    public Instruction(InstructionType type, String identifier, Assembler parent, SectionValue[] values, String[] dataOrder){
        this(type, identifier,parent,values);
        this.id=Line.ID_INSTRUCTION;
        this.dataOrder=dataOrder;
    }
    
    public Instruction(InstructionType type) {
        super();
        this.id=Line.ID_INSTRUCTION;
        this.type = type;
    }
    
    public Instruction(Instruction instruction){
        this.addressingMode=instruction.addressingMode;
        this.dataOrder=instruction.dataOrder.clone();
        this.format=new Format(instruction.format);
        this.id=instruction.id;
        this.identifier=instruction.identifier;
        this.parent=instruction.parent;
        this.sintax=new Sintax(instruction.sintax);
        this.superSintax=instruction.superSintax;
        this.type=instruction.type;
        this.values=instruction.values.clone();
    }

    public void setType(InstructionType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString()).append(';');
        if(addressingMode!=null)
            sb.append("AdressingMode: ").append(this.addressingMode.getIdentifier()).append(';');
        sb.append("Format: ").append(this.format.toString()).append(';');
        sb.append("SectionValue: ").append(Arrays.deepToString(this.values)).append(';');
        sb.append("DataOrder").append(Arrays.deepToString(this.dataOrder));
        return sb.toString();
    }

    public boolean insertInSector(Word word, String sectorName, boolean ignoreSize) {
        Sector found = this.type.getFormat().findSector(sectorName);
        if (found == null) {
            System.out.println("Section " 
                    + sectorName 
                    + " not found on instruction format");
            return false;
        }
        if (word.size > found.size && !ignoreSize) {
            System.out.println("Word too big for this section. Word size: " 
                    + word.size 
                    + "; Sector size: " 
                    + found.size);
            return false;
        }
        for (int i = 0; i < word.size; i++) {
            this.type.getFormat().format.data[found.finish - i] = new Tristate((word.bits[i]));
        }
        return true;
    }

    public InstructionType getType() {
        return type;
    }
    
    public void createFormat(){
        this.format=new Format(this.type.getFormat().format);
        for(SectionValue sector: this.values){
            String sectoridentifier =sector.identifier;
            boolean found=false;
            for(InstructionSector sect: this.type.getFormat().sections){
                if(sectoridentifier.toLowerCase().equals(sect.name.toLowerCase())){
                    found=true;
                    int counteraux=sector.value.size-1;
                    for(int i=sect.start; i<=sect.finish; i++){
                        try{
                        format.data[i]=new Tristate(sector.value.bits[counteraux]);
                        counteraux--;}
                        catch(ArrayIndexOutOfBoundsException e){
                            break;
                        }
                    }
                    break;
                }
            }
            if(!found){
                throw new ReferenceNotFoundException("InstructionSector", sector.identifier);
            }
        }
    }
    
    public void addToParent(){
        this.parent.addInstruction(this);
    }
}
