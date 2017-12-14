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

import Core.Assembler.Format.Format;
import Core.Architecture.SectorComparator;
import Core.Architecture.Word;
import Core.Assembler.AddressingMode;
import Core.Assembler.Format.Tristate;
import Exceptions.ReferenceNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author emnga
 */
public class InstructionFormat implements Serializable{
    
    public int wordSize;
    public ArrayList<InstructionSector> sections;
    public Format format;

    public InstructionFormat(int wordSize, ArrayList<InstructionSector> sections) {
        this.wordSize = wordSize;
        this.sections = sections;
        this.format=new Format(wordSize);
    }

    public InstructionFormat(int wordSize, ArrayList<InstructionSector> sections, Format format) {
        this.wordSize = wordSize;
        this.sections = sections;
        this.format = format;
    }

    public InstructionFormat(int wordSize, Format format) {
        this.wordSize = wordSize;
        this.format = format;
        this.sections=new ArrayList<>();
    }
    
    public InstructionFormat(int wordSize){
       this.wordSize = wordSize;
       this.sections = new ArrayList<>();
       this.format=new Format(wordSize);
    }
   
    public boolean addSector(int start, int finish, String name, Word value){
        for(InstructionSector sector: sections){
            if((start>=sector.start && finish<=sector.finish)||(start<=sector.start && finish>= sector.start)||(start<=sector.finish && finish>=sector.finish)|| (finish<start) || (name.equals(sector.name))){
                return false;
            }
        }
        this.sections.add(new InstructionSector(start,finish,name,AddressingMode.ABSOLUTO));
        this.sections.sort(new SectorComparator());
        return true;
    }
    
    public boolean removeSector(String name){
        for(int i=0; i<sections.size(); i++){
            if(sections.get(i).name.equals(name)){
                this.sections.remove(i);
                return true;
            }
        }
        return false;
    }
    
    public InstructionSector findSector(String name){
        for(InstructionSector sector: sections){
            if(name.toLowerCase().equals(sector.name.toLowerCase())){
                return sector;
            }
        }
        throw new ReferenceNotFoundException(name,"sections");
    }
    
    @Override
    public String toString(){
        StringBuilder sb=new StringBuilder();
        int[] starts = new int[this.sections.size()], ends = new int[this.sections.size()];
        for(int i=0; i<this.sections.size(); i++){
            InstructionSector sector=this.sections.get(i);
            starts[i]=sector.start;
            ends[i]=sector.finish;
        }
        int startscounter=0;
        int endscounter=0;
        for(int i=0; i<=this.wordSize; i++){
            if(endscounter<ends.length)
                if(ends[endscounter]+1==i){
                    sb.append(" <-").append(this.sections.get(endscounter).name).append(" ");
                    endscounter++;
                }
            if(startscounter<starts.length)
                if(starts[startscounter]==i){
                    sb.append(" ").append(this.sections.get(startscounter).name).append("-> ");
                    startscounter++;
                }
            try{
            Tristate dat=this.format.data[i];
            sb.append(dat.toString());
            }catch(ArrayIndexOutOfBoundsException e){
                
            }
        }
        return sb.toString();
    }
    
    public String[] getSectionsNames(){
        String[] ret=new String[this.sections.size()];
        for(int i=0; i<ret.length; i++){
            ret[i]=this.sections.get(i).name;
        }
        return ret;
    }

    public void setSections(ArrayList<InstructionSector> sections) {
        this.sections = sections;
    }
}
